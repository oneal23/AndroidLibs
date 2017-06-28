package com.lei.baselib_java.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lei.baselib_java.network.cache.ACache;
import com.lei.baselib_java.network.progress.ProgressRequestBody;
import com.lei.baselib_java.network.progress.UIProgressListener;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rymyz on 2017/5/25.
 */

public class EasyHttp {
    private String BASE_URL = "";
    private Context mContext;
    private int mCacheTime;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private Map<String, String> mHeaders;
    private EasyService mEasyService;
    private Gson mGson;
    private static EasyHttp instance = null;
    private ACache mCache;

    private EasyHttp() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

    public static EasyHttp getInstance() {
        if (instance == null) {
            synchronized (EasyHttp.class) {
                if (instance == null) {
                    instance = new EasyHttp();
                }
            }
        }

        return instance;
    }

    public void install(Context context) {
        checkNotNull(context);
        checkNotNull(BASE_URL, "You should call setBaseUrl() first.");

        this.mContext = context.getApplicationContext();

        long max_size = Runtime.getRuntime().maxMemory();
        mCache = ACache.get(mContext, "EasyHttp", max_size);

        initOkHttpClient();
        initRetrofit();
    }

    private void initOkHttpClient() {
        mOkHttpClient = OkHttpClientProvider.getOkHttpClient(mContext, mHeaders);
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mEasyService = mRetrofit.create(EasyService.class);
    }

    public EasyHttp setBaseUrl(String baseUrl) {
        if (baseUrl.isEmpty()) throw new NullPointerException("BaseUrl must not be null.");
        this.BASE_URL = baseUrl;

        return this;
    }

    public EasyHttp setCacheTime(int cacheTime) {
        if (cacheTime < 0) cacheTime = 0;
        this.mCacheTime = cacheTime;

//        initOkHttpClient();
//        initRetrofit();

        return this;
    }

    public EasyHttp addHeaders(Map<String, String> headers) {
        if (headers == null || headers.size() == 0) return this;
        this.mHeaders = headers;

        initOkHttpClient();
        initRetrofit();
        return this;
    }

    /**
     * 请求
     *
     * @param path
     * @param map
     * @return
     */
    public Observable<String> get(String path, Map<String, Object> map) {
        return method(Method.GET, path, map);
    }

    public Observable<String> post(String path, Map<String, Object> map) {
        return method(Method.POST, path, map);
    }

    public Observable<String> postJson(String path, Map<String, Object> map) {
        return method(Method.POST_JSON, path, map);
    }

    public Observable<String> uploadOneFile(String url, Map<String, String> descriptions, File file, UIProgressListener progressListener) {
        List<File> files = new ArrayList<>();
        files.add(file);
        return uploadFile(url, descriptions, files, progressListener);
    }

    public Observable<String> uploadFile(String url, Map<String, String> descriptions, List<File> files, UIProgressListener progressListener) {
        if (descriptions == null) {
            descriptions = new HashMap<>();
        }
        List<MultipartBody.Part> bodys = new ArrayList<>();
        int size = files.size();

        for (int i = 0; i < size; i++) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));

            ProgressRequestBody body = new ProgressRequestBody(requestFile, progressListener);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file" + (size == 1 ? "" : i), files.get(i).getName(), body);
            bodys.add(filePart);
        }
        // MultipartBody.Part is used to send also the actual file name
        return mEasyService.uploadFile(url, descriptions, bodys)
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        Logger.json(result);
                        return result;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<String> method(Method method, final String path, Map<String, Object> map) {
        checkNotNull(mEasyService);
        final String key = path + transMapToString(map);
        if (mCacheTime != 0) {
            final String cacheData = mCache.getAsString(key);
            if (cacheData != null && !cacheData.isEmpty()) {
                Logger.i("Net: " + "从缓存中读取数据" + cacheData);
                mCacheTime = 0;
                return Observable.just(cacheData).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }

        Observable<ResponseBody> observable = null;

        switch (method) {
            case GET:
                observable = mEasyService.get(path, map);
                break;
            case POST:
                observable = mEasyService.post(path, map);
                break;
            case POST_JSON:
                JSONObject object = new JSONObject(map);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
                observable = mEasyService.postJson(path, body);
                break;
            default:
                break;
        }
        return observable.map(new Function<ResponseBody, String>() {
            @Override
            public String apply(ResponseBody responseBody) throws Exception {
                String result = "";
                try {
                    result = responseBody.string();
                    //缓存响应
                    if (mCacheTime != 0) {
                        mCache.put(key, result, mCacheTime);
                        mCacheTime = 0;
                    }
                    responseBody.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Logger.json(result);
                Logger.i("NET:"+result);
                return result;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    private String transMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

}
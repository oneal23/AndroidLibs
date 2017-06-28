package com.lei.baselib_java.network;

import android.content.Context;


import com.lei.baselib_java.network.interceptor.HeadInterceptor;
import com.lei.baselib_java.network.interceptor.LoggingInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by rymyz on 2017/5/25.
 */

public class OkHttpClientProvider {
    private final static long DEFAULT_CONNECT_TIMEOUT = 10;
    private final static long DEFAULT_WRITE_TIMEOUT = 30;
    private final static long DEFAULT_READ_TIMEOUT = 30;

    /**
     * 不使用缓存，且不设置请求头
     *
     * @param context
     * @return
     */
    public static OkHttpClient getOkHttpClient(Context context) {
        return getOkHttpClient(context, /*0, */null);
    }

    /**
     * 使用缓存，但不设置请求头
     *
     * @param context
     * @param cacheTime
     * @return
     */
   /* public static OkHttpClient getOkHttpClient(Context context, long cacheTime) {
        return getOkHttpClient(context, cacheTime, null);
    }*/

    /**
     * 不使用缓存，但设置请求头
     *
     * @param context
     * @param headers
     * @return
     */
   /* public static OkHttpClient getOkHttpClient(Context context, Map<String, String> headers) {
        return getOkHttpClient(context, 0, headers);
    }*/

    /**
     * @param context //@param cacheTime 传入0不使用缓存
     * @param headers
     * @return
     */
    public static OkHttpClient getOkHttpClient(Context context, /*long cacheTime,*/ Map<String, String> headers) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
//        File httpCacheDirectory = new File(context.getCacheDir(), "OkHttpCache");
//        long maxSize = Runtime.getRuntime().maxMemory() / 8;
//        httpClientBuilder.cache(new Cache(httpCacheDirectory, maxSize));
        //设置拦截器
        httpClientBuilder.addInterceptor(new LoggingInterceptor());

//        CacheControlInterceptor controlInterceptor = new CacheControlInterceptor(context, cacheTime);

//        httpClientBuilder.addInterceptor(controlInterceptor);
        /*if (cacheTime == 0) {
            httpClientBuilder.addNetworkInterceptor(new FromNetWorkControlInterceptor());
        } else {
            httpClientBuilder.addNetworkInterceptor(controlInterceptor);
        }*/

        //请求头设置
        if (headers != null && headers.size() > 0) {
            httpClientBuilder.interceptors().add(new HeadInterceptor(headers));
        }

        return httpClientBuilder.build();
    }
}

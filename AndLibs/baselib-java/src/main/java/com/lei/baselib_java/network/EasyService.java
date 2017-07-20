package com.lei.baselib_java.network;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by rymyz on 2017/5/25.
 */

public interface EasyService {
    @GET("{path}")
    Observable<ResponseBody> get(
            @Path(value = "path", encoded = true) String path,
            @QueryMap() Map<String, Object> map
    );

    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(
            @Path(value = "path", encoded = true) String path,
            @FieldMap() Map<String, Object> map
    );

    @POST("{path}")
    Observable<ResponseBody> postJson(
            @Path(value = "path", encoded = true) String path,
            @Body RequestBody body
    );

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(
            @Url String url,
            @PartMap() Map<String, String> description,
            @Part List<MultipartBody.Part> bodys);

}

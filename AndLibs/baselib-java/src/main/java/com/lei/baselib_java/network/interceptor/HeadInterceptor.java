package com.lei.baselib_java.network.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {
    Map<String, String> headers = new HashMap<>();

    public HeadInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request requst = null;
        for (String key : headers.keySet()) {
            requst = builder.addHeader(key, headers.get(key))
                    .build();
        }

        return chain.proceed(requst);
    }
}
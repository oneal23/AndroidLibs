package com.lei.baselib_java.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by rymyz on 2017/5/27.
 */

public class GsonUtil {
    private static Gson mGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create();

    public static <T> T fromJson(String json,Class<T> clazz){
        return mGson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Class<T> base, Class clazz) {
        Type objectType = type(base, clazz);
        return mGson.fromJson(json, objectType);
    }

    public static <T> String toJson(T src, Class<T> base, Class clazz) {
        Type objectType = type(base, clazz);
        return mGson.toJson(src, objectType);
    }

    private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}

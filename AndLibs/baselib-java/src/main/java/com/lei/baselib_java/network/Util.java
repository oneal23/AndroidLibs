package com.lei.baselib_java.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rymyz on 2017/5/26.
 */

public class Util {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        //如果仅仅是用来判断网络连接
        //则可以使用
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        return info.isAvailable();
    }
}
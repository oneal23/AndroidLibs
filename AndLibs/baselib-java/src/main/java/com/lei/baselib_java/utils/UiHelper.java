package com.lei.baselib_java.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.lei.baselib_java.EasyApplication;


/**
 * Created by rymyz on 2017/4/28.
 */

public class UiHelper {

    private static Context context = EasyApplication.getContext();
    //优化Toast
    private static long oneTime = 0;
    private static long twoTime = 0;
    protected static Toast toast = null;
    private static String oldMsg = null;

    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }

        twoTime = oneTime;
    }
}

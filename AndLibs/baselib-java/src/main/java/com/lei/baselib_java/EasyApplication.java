package com.lei.baselib_java;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.lei.baselib_java.utils.CrashHandler;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 需要使用这个Application
 */

public class EasyApplication extends Application {
    /**
     * 获取全局的Context
     */
    private static Context mContext;
    public static EasyApplication instance =null;

    public static EasyApplication getInstance() {
        return instance;
    }
    /**
     * 存放activity的列表
     */
    private static HashMap<Class<?>, Activity> activities;
    private static Activity currentActivity;
    private int progressBarStyle = R.style.LoadDialog;

    public void setProgressBarStyle(int progressBarStyle) {
        this.progressBarStyle = progressBarStyle;
    }

    public int getProgressBarStyle() {
        return progressBarStyle;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this.getApplicationContext();

        Logger.init("EasyProject");

        activities = new LinkedHashMap<>();
        //注册管理所有的Activity
        manageActivities();

        CrashHandler.getInstance().init(this);
    }

    private void manageActivities() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Logger.i("#########" + "Activity Created :" + activity.toString());
                activities.put(activity.getClass(), activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Logger.i("#########" + "Activity Started :" + activity.toString());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
                Logger.i("#########" + "Activity Resumed :" + activity.toString());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Logger.i("#########" + "Activity Paused :" + activity.toString());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.i("#########" + "Activity Stopped :" + activity.toString());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Logger.i("#########" + "Activity SaveInstanceState :" + activity.toString());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Logger.i("#########" + "Activity Destroyed :" + activity.toString());
                if (activities.containsValue(activity)) {
                    activities.remove(activity.getClass());
                }
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    public static void finishAllActivities() {
        if (activities != null && activities.size() > 0) {
            Set<Map.Entry<Class<?>, Activity>> sets = activities.entrySet();
            for (Map.Entry<Class<?>, Activity> s : sets) {
                if (!s.getValue().isFinishing()) {
                    s.getValue().finish();
                }
            }
        }
        activities.clear();
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
}

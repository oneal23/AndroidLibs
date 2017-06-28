package com.lei.baselib_java.mvp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lei.baselib_java.mvp.swipe.SwipeBackActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by rymyz on 2017/5/24.
 */

public class BaseActivity extends SwipeBackActivity {
    protected final String TAG = BaseActivity.this.getClass().getSimpleName();
    protected final Context tag = BaseActivity.this;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSwipeBackEnable(false);
        compositeDisposable = new CompositeDisposable();
        setPresenter();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    /**
     * override This to set your own presenter.
     */
    protected void setPresenter() {
    }
}

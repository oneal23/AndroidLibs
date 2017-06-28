package com.lei.baselib_java.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.lei.baselib_java.EasyApplication;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by rymyz on 2017/5/2.
 */

public abstract class BaseFragment extends RxFragment {
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    protected ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable;


    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public boolean controlFirstInit() {
        return true;
    }

    public boolean lazyLoad() {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInitView = true;
        progressDialog = new ProgressDialog(getContext(), EasyApplication.getInstance().getProgressBarStyle());
        compositeDisposable = new CompositeDisposable();
        lazyLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Logger.i("isVisibleToUser " + isVisibleToUser + " " + this.getClass().getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void lazyLoadData() {
        if (controlFirstInit()) {
            if (isFirstLoad) {
                Logger.i("第一次加载 " + " isInitView " + isInitView + " isVisible " + isVisible + " " + this.getClass().getSimpleName());
            } else {
                Logger.i("不是第一次加载" + " isInitView " + isInitView + " isVisible " + isVisible + " " + this.getClass().getSimpleName());
            }
            if (lazyLoad()) {
                if (!isFirstLoad || !isVisible || !isInitView) {
                    Logger.i("不加载" + " " + this.getClass().getSimpleName());
                    return;
                }
            } else {
                if (!isFirstLoad || !isInitView) {
                    Logger.i("不加载" + " " + this.getClass().getSimpleName());
                    return;
                }
            }
            Logger.i("完成数据第一次加载");
        } else {
            if (lazyLoad()) {
                if (!isVisible || !isInitView) {
                    Logger.i("不加载" + " " + this.getClass().getSimpleName());
                    return;
                }
            } else {
                if (!isInitView) {
                    Logger.i("不加载" + " " + this.getClass().getSimpleName());
                    return;
                }
            }
            Logger.i("完成数据加载");
        }
        initData();
        isFirstLoad = false;
    }


    /**
     * 加载要显示的数据
     */
    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroyView();
    }
}

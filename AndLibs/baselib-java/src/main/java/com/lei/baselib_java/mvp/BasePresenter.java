package com.lei.baselib_java.mvp;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by rymyz on 2017/5/25.
 */

public class BasePresenter<V extends IBaseView, S extends IBaseSource> implements IBasePresenter {
    protected V mvpView;

    CompositeDisposable compositeDisposable;
    protected S source;

    public BasePresenter(@NonNull V mvpView, @NonNull S source) {
        checkNotNull(mvpView, "View can not be null.");
        checkNotNull(source, "source can not be null.");
        this.source = source;
        this.mvpView = mvpView;
        this.mvpView.setPresenter(this);

        compositeDisposable = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {

        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(disposable);

        Logger.i("Disposable Added!" + compositeDisposable.size());
    }

    /**
     * 对 ACTIVITY 生命周期进行管理
     *
     * @return
     */
    protected <T> LifecycleProvider<T> getLifecycleProvider() {

        LifecycleProvider<T> provider = null;
        if (null != mvpView &&
                mvpView instanceof LifecycleProvider) {
            provider = (LifecycleProvider<T>) mvpView;
        }
        return provider;
    }

    @Override
    public void detach() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

    }
}

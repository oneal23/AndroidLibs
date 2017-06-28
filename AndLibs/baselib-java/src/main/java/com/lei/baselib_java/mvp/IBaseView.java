package com.lei.baselib_java.mvp;

/**
 * Created by rymyz on 2017/5/25.
 */

public interface IBaseView<T extends IBasePresenter> {
    void setPresenter(T presenter);

    void showLoading(String message);

    void hideLoading();

    void showUnCancelLoading(String message);


    void showError(String error);
}

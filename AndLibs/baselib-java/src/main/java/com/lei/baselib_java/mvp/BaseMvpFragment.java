package com.lei.baselib_java.mvp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;


import com.lei.baselib_java.utils.UiHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rymyz on 2017/5/2.
 */

public abstract class BaseMvpFragment<P extends IBasePresenter> extends BaseFragment implements IBaseView<P> {
    protected P presenter;

    @Override
    public void onDestroyView() {
        presenter.detach();

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                BaseMvpFragment.this.presenter.detach();
            }
        });
    }

    @Override
    public void setPresenter(P presenter) {
        checkNotNull(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showUnCancelLoading(String message) {
        checkNotNull(progressDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void showLoading(String message) {
        checkNotNull(progressDialog);

        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        checkNotNull(progressDialog);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            presenter.detach();
        }
    }

    @Override
    public void showError(String error) {
        UiHelper.showToast(error);
    }
}

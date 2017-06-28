package com.lei.baselib_java.mvp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lei.baselib_java.EasyApplication;
import com.lei.baselib_java.utils.UiHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rymyz on 2017/5/25.
 */

public class BaseMvpActivity<P extends IBasePresenter> extends BaseActivity implements IBaseView<P> {
    private ProgressDialog progressDialog;
    protected P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this, EasyApplication.getInstance().getProgressBarStyle());
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                BaseMvpActivity.this.presenter.detach();
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detach();

        super.onDestroy();
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

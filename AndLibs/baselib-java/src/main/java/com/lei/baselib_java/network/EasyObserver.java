package com.lei.baselib_java.network;

import android.util.MalformedJsonException;

import com.lei.baselib_java.network.exception.ApiException;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by rymyz on 2017/5/31.
 */

public abstract class EasyObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("NET_ERROR: " + e.toString());
        if (e instanceof SocketTimeoutException) {
            _onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
        } else if (e instanceof ConnectException) {
            _onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
        } else if (e instanceof UnknownHostException) {
            _onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
        } else if (e instanceof MalformedJsonException) {
            _onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
        } else if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    _onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    _onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    _onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof MalformedJsonException) {
                    _onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
                } else {
                    _onError(ApiException.Code_Default, e.getMessage() == null ? e.toString() : e.getMessage());
                }
            }
        } else {
            String msg = e.getMessage();
            int code;
            if (msg != null && !msg.isEmpty()) {
                if (msg.contains("#")) {
                    code = Integer.parseInt(msg.split("#")[0]);
                    _onError(code, msg.split("#")[1]);
                } else {
                    code = ApiException.Code_Default;
                    _onError(code, msg);
                }
            } else {
                code = ApiException.Code_Default;
                _onError(code, e.toString());
            }
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public abstract void _onError(int code, String msg);
}

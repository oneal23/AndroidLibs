package com.lei.baselib_java.network.cunsumer;

import android.util.MalformedJsonException;

import com.lei.baselib_java.network.exception.ApiException;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;

/**
 * Created by rymyz on 2017/6/8.
 */

public abstract class ErrorCunsumer implements Consumer<Throwable> {

    @Override
    public void accept(Throwable e) throws Exception {
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

    public abstract void _onError(int code, String msg);
}

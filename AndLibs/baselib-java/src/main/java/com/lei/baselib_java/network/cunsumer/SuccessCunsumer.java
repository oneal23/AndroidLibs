package com.lei.baselib_java.network.cunsumer;

import io.reactivex.functions.Consumer;

/**
 * Created by rymyz on 2017/6/8.
 */

public abstract class SuccessCunsumer<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}

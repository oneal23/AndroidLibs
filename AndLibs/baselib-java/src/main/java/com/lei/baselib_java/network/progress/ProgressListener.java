package com.lei.baselib_java.network.progress;

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
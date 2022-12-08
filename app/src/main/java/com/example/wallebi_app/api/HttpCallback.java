package com.example.wallebi_app.api;

import okhttp3.Response;

public interface HttpCallback {

    void onFialure(Response response, Throwable throwable);
    void onSuccess(Response response);
}

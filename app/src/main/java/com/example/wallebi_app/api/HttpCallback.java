package com.example.wallebi_app.api;

import okhttp3.Response;

public interface HttpCallback {

    public void onFialure(Response response, Throwable throwable);
    public void onSuccess(Response response);
}

package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.response.CreateG2fResponse;

import retrofit2.Call;
import retrofit2.http.POST;

public interface CreateG2fApi {
    @POST("v1/UserService/g2f/create/")
    Call<CreateG2fResponse> createG2f();
}

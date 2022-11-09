package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.models.ChangePassBody;
import com.example.wallebi_app.api.setting.response.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChangePasswordApi {
    @POST("v1/UserService/password/change/")
    Call<ChangePasswordResponse> changePassword(@Body ChangePassBody body);
}

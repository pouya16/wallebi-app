package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.bodies.AddMobileBody;
import com.example.wallebi_app.api.setting.response.AddMobileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddMobileApi {
    @POST("v1/UserService/mobile/add/")
    Call<AddMobileResponse> addMobile(@Body AddMobileBody body);
}

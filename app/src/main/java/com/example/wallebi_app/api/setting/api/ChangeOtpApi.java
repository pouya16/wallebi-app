package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.bodies.ChangeOtpBody;
import com.example.wallebi_app.api.setting.response.G2fResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChangeOtpApi {
    @POST("v1/UserService/otp/action/")
    Call<G2fResponse> changeOtp(@Body ChangeOtpBody body);
}

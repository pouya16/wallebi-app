package com.example.wallebi_app.api.reg.apis;

import com.example.wallebi_app.api.reg.model.SendOtpBody;
import com.example.wallebi_app.api.reg.responses.EOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AskOtpApi {
    @POST("v1/UserService/signup/send_code/")
    Call<EOtpResponse> sendEOtp(@Body SendOtpBody body);

}

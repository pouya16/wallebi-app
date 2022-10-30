package com.example.wallebi_app.api.reg.apis;

import com.example.wallebi_app.api.reg.model.SetPasswordBody;
import com.example.wallebi_app.api.reg.responses.SetPasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SetPasswordApi {
    @POST("v1/UserService/signup/set_password/")
    Call<SetPasswordResponse> setPass(@Body SetPasswordBody body);
}

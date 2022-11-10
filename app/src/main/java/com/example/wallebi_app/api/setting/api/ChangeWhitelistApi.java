package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.models.WhitelistChangeBody;
import com.example.wallebi_app.api.setting.response.G2fResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChangeWhitelistApi {
    @POST("v0/UserService/change/")
    Call<G2fResponse> changeWhitelistMode(@Body WhitelistChangeBody body);
}

package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.models.G2fBody;
import com.example.wallebi_app.api.setting.response.G2fResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface G2fVerifyApi {
    @POST("v1/UserService/g2f/action/")
    Call<G2fResponse> verifyG2f(@Body G2fBody body);
}

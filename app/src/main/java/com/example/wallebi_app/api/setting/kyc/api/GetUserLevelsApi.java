package com.example.wallebi_app.api.setting.kyc.api;

import com.example.wallebi_app.api.setting.kyc.response.KycLevelsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetUserLevelsApi {
    @GET("v0/UserService/user_levels/")
    Call<KycLevelsResponse> getKycList();
}

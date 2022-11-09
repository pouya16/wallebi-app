package com.example.wallebi_app.api.setting;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetMeApi {
    @GET("v0/UserService/me/")
    Call<MeResponse> getMe();
}

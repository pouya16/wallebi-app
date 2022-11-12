package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.bodies.AddWhiteListBody;
import com.example.wallebi_app.api.setting.response.AddWhitelistResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddWhitelistApi {
    @POST("v0/WhiteListService/white_list/")
    Call<AddWhitelistResponse> addWhiteList(@Body AddWhiteListBody body);
}

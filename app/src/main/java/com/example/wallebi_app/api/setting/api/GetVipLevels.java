package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.response.VipResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetVipLevels {
    @GET("v0/UserService/vip_levels/")
    Call<VipResponse> getVipLevels();
}

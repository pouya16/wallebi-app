package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.bodies.NetworkBody;
import com.example.wallebi_app.api.setting.response.NetworkResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetNetworkApi {
        @POST("v0/CryptoService/network_list_2/")
        Call<NetworkResponse> getNetwork(@Body NetworkBody body);
}

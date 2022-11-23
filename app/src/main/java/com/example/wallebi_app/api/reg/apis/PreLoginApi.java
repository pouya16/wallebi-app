package com.example.wallebi_app.api.reg.apis;

import com.example.wallebi_app.api.reg.responses.PreLoginRes;
import com.example.wallebi_app.api.reg.responses.PreLoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PreLoginApi {
    @POST("v1/UserService/pre_login/")
    Call<PreLoginResponse> sendPreLogin(@Body RequestBody body);
}

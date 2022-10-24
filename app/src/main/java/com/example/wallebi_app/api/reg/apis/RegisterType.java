package com.example.wallebi_app.api.reg.apis;

import com.example.wallebi_app.api.reg.model.RegisterTypeModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RegisterType {
    @GET("v0/GeneralService/signup_status/")
    Call<RegisterTypeModel> registerTypeApi();
}

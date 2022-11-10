package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.models.ValidateAddressBody;
import com.example.wallebi_app.api.setting.response.ValidateAddressResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ValidateAddressApi {
    @POST("v0/CryptoService/validate_address/")
    Call<ValidateAddressResponse> validate(@Body ValidateAddressBody body);
}

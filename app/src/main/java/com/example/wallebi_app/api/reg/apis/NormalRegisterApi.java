package com.example.wallebi_app.api.reg.apis;

import com.example.wallebi_app.api.reg.model.RegisterNormalBody;
import com.example.wallebi_app.api.reg.responses.VerifyEmailResponse;

import retrofit2.Call;
import retrofit2.http.Body;

public interface NormalRegisterApi {
    Call<VerifyEmailResponse> registerEmail(@Body RegisterNormalBody body);
}

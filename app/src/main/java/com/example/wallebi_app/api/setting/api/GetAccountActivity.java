package com.example.wallebi_app.api.setting.api;

import com.example.wallebi_app.api.setting.response.AccountActivityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetAccountActivity {
    @GET("v0/UserService/account_activity")
    Call<AccountActivityResponse> getAccountAct(@Query("all") boolean all);
}

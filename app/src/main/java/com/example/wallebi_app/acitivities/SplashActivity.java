package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.wallebi_app.MainActivity;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.GetDecimalPointsApi;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.data.CoinListModel;
import com.example.wallebi_app.api.data.GetCoinsApi;
import com.example.wallebi_app.api.reg.apis.RegisterType;
import com.example.wallebi_app.api.reg.model.RegisterTypeModel;
import com.example.wallebi_app.database.DataAccess;
import com.example.wallebi_app.database.LoginData;
import com.geetest.captcha.GTCaptcha4Client;
import com.geetest.captcha.GTCaptcha4Config;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    Retrofit retrofit;
    RegisterType registerType;
    MaterialButton btnCaptcha;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //btnCaptcha = findViewById(R.id.captcha);

        /*btnCaptcha.setOnClickListener(v -> {
            Log.i("Log1", "s is : " + "get recaptcha");
            GTCaptcha4Config config = new GTCaptcha4Config.Builder()
                    .setDebug(true) // TODO release version must be closed
                    .setLanguage("en")
                    .setTimeOut(10000)
                    .setCanceledOnTouchOutside(true)
                    .build();

            GTCaptcha4Client gtCaptcha4Client = GTCaptcha4Client.getClient(SplashActivity.this)
                    .init("f854c933accd1b0610717daaf9089e04", config);
            gtCaptcha4Client.addOnSuccessListener(new GTCaptcha4Client.OnSuccessListener() {
                @Override
                public void onSuccess(boolean b, String s) {
                    Log.i("Log1", "s is : " + s);
                }
            });
            gtCaptcha4Client.addOnFailureListener(new GTCaptcha4Client.OnFailureListener() {
                @Override
                public void onFailure(String s) {
                    Log.i("Log1", "failed s is : " + s);
                }
            });
            gtCaptcha4Client.verifyWithCaptcha();
        });
*/

        LoginData.access_token = "LFc5jxKz3KQl8Vvp3TilcEwJmzkvp5";


        getCoinList();
        getFiatList();
        retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        getRegisterType(this);
        new GetDecimalPointsApi(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //getRegisterType(this);
    }



    private void getFiatList(){
        GetCoinsApi getFiat = new GetCoinsApi(this,GetCoinsApi.MODE_FIAT);

    }


    private void getCoinList(){
        GetCoinsApi getCoinsApi = new GetCoinsApi(this,GetCoinsApi.MODE_CRYPTO);

    }

    private void getRegisterType(Context context){
        registerType = retrofit.create(RegisterType.class);
        Call<RegisterTypeModel> call = registerType.getTypeApi();
        call.enqueue(new Callback<RegisterTypeModel>() {
            @Override
            public void onResponse(Call<RegisterTypeModel> call, Response<RegisterTypeModel> response) {
                if (response.body().getSuccess() == true){
                    Log.i("Log1", "succeed");
                    if(response.body().getData() == "open"){
                        LoginData.registerModel = 1;
                    }else{
                        LoginData.registerModel = 0;
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }else{
                    Log.i("Log1", "else in Splash");

                }
            }

            @Override
            public void onFailure(Call<RegisterTypeModel> call, Throwable t) {
                Log.i("Log1", "failure in Splash");
            }
        });


    }

    private void goToError(){
        Intent intent = new Intent(SplashActivity.this,BlockedActivity.class);
        intent.putExtra("mode",1);
        startActivity(intent);
    }
}
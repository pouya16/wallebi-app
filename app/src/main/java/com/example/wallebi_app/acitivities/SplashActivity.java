package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.wallebi_app.MainActivity;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.reg.apis.RegisterType;
import com.example.wallebi_app.api.reg.model.RegisterTypeModel;
import com.example.wallebi_app.database.LoginData;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    Retrofit retrofit;
    RegisterType registerType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        getRegisterType(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRegisterType(this);
    }

    private void getRegisterType(Context context){
        registerType = retrofit.create(RegisterType.class);
        Call<RegisterTypeModel> call = registerType.getTypeApi();
        call.enqueue(new Callback<RegisterTypeModel>() {
            @Override
            public void onResponse(Call<RegisterTypeModel> call, Response<RegisterTypeModel> response) {
                if (response.body().getSuccess() == true){
                    if(response.body().getData() == "open"){
                        LoginData.registerModel = 1;
                    }else{
                        LoginData.registerModel = 0;
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }else{

                }
            }

            @Override
            public void onFailure(Call<RegisterTypeModel> call, Throwable t) {

            }
        });


    }

    private void goToError(){
        Intent intent = new Intent(SplashActivity.this,BlockedActivity.class);
        intent.putExtra("mode",1);
        startActivity(intent);
    }
}
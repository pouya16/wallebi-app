package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.wallebi_app.MainActivity;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.data.CoinListModel;
import com.example.wallebi_app.api.data.GetCoinsApi;
import com.example.wallebi_app.api.reg.apis.RegisterType;
import com.example.wallebi_app.api.reg.model.RegisterTypeModel;
import com.example.wallebi_app.database.DataAccess;
import com.example.wallebi_app.database.LoginData;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LoginData.access_token = "Bl9Gwfp5LB93HF0obPZCci5v5SP1ZE";

        getCoinList();
        getFiatList();
        retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        getRegisterType(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRegisterType(this);
    }



    private void getFiatList(){
        GetCoinsApi getFiat = new GetCoinsApi(this,GetCoinsApi.MODE_FIAT);
        /*
        String address = "v0/CryptoService/fiat_list/";
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                }catch (Exception e){

                }
            }
            @Override
            public void onSuccess(okhttp3.Response response) {

                Log.i("Log1","" + response.code());
                try{
                    String res =  response.body().string();
                    Log.i("Log3: ","response: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(response.code() == 200){
                        if(jsonObject.getBoolean("success")){
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<CoinListModel>>(){}.getType();
                            ArrayList<CoinListModel> list = gson.fromJson(String.valueOf(jsonObject.getJSONArray("msg")),userListType);
                            DataAccess dataAccess = new DataAccess();
                            dataAccess.createFiatList(list);
                        }
                    }else{
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
            }
        };
        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.get(address,null,callback,HttpUtil.MODE_NO_AUTH);*/
    }


    private void getCoinList(){
        GetCoinsApi getCoinsApi = new GetCoinsApi(this,GetCoinsApi.MODE_CRYPTO);
        /*String address = "v0/CryptoService/crypto_list/";
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                }catch (Exception e){

                }
            }
            @Override
            public void onSuccess(okhttp3.Response response) {

                Log.i("Log1","" + response.code());
                try{
                    String res =  response.body().string();
                    Log.i("Log2: ","response: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(response.code() == 200){
                        if(jsonObject.getBoolean("success")){
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<CoinListModel>>(){}.getType();
                            ArrayList<CoinListModel> list = gson.fromJson(String.valueOf(jsonObject.getJSONArray("msg")),userListType);
                            DataAccess dataAccess = new DataAccess();
                            dataAccess.createCryptoList(list);
                        }
                    }else{
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
            }
        };
        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.get(address,null,callback,HttpUtil.MODE_NO_AUTH);*/
    }

    private void getRegisterType(Context context){
        /*LoginData.registerModel = 1;
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();*/
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
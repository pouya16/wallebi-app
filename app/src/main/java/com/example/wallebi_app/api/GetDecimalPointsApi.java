package com.example.wallebi_app.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.bank.BankAccountsModel;
import com.example.wallebi_app.api.bank.IbanAccountsModel;
import com.example.wallebi_app.database.DataAccess;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.models.DecimalPointsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDecimalPointsApi {
    private OkHttpClient client;
    String address = "v0/GeneralService/decimal_points/";
    private String mainUrl = "";
    Context context;


    public GetDecimalPointsApi(Context context) {
        this.context = context;
        call(createRequest());
        mainUrl = context.getString(R.string.base_url);
    }

    private void call(Request request){

        Log.i("Log1","get decimal points");
            client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                Handler mainHandler = new Handler(context.getMainLooper());
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.i("Log1","" + response.code());
                    String res =  response.body().string();
                    Log.i("Log2: ","response: " + res);
                    if(response.code() == 200){
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if(jsonObject.getBoolean("success")){
                                Gson gson = new Gson();
                                Type userListType = new TypeToken<ArrayList<DecimalPointsModel>>(){}.getType();
                                ArrayList<DecimalPointsModel> list = gson.fromJson(String.valueOf(jsonObject.getJSONArray("data")),userListType);
                                DataAccess.decimalPointsModels = new HashMap<>();
                                if(list.size() != 0){
                                    for (DecimalPointsModel item:
                                         list) {
                                        DataAccess.decimalPointsModels.put(item.getMarket_id(),item);
                                    }

                                }


                                //LoginData.meClass = gson.fromJson(String.valueOf(jsonObject.getJSONObject("msg")), MeModel.class);
                            }
                        }catch (Exception e){

                        }
                    }else{

                    }

                }
            });



    }

    private void call(Request request, final HttpCallback callback){

        if(LoginData.access_token.length() > 3){
            client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                Handler mainHandler = new Handler(context.getMainLooper());
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFialure(null,e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    callback.onSuccess(response);
                }
            });
        }else{
            Log.i("Log1","User is not Loged in");
        }


    }

    private Request createRequest(){
        mainUrl = context.getString(R.string.base_url);
        String ad = mainUrl + address;
        Log.i("Log1",ad);
        Request.Builder request = new Request.Builder().url(ad);
        request.addHeader("M2M","Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==pouya");
        return request.build();
    }
}

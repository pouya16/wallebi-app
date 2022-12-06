package com.example.wallebi_app.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.wallebi_app.database.LoginData;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetMeApi {
    private OkHttpClient client;
    String address = "v0/UserService/me/";
    private final String mainUrl = "https://api.wallebi.run/";
    Context context;
    Button btn;
    ProgressBar progressBar;

    public GetMeApi(Context context, Button btn, ProgressBar progressBar,HttpCallback callback) {
        this.context = context;
        this.btn = btn;
        this.progressBar = progressBar;
        call(createRequest(),callback);
    }

    public GetMeApi(Context context,HttpCallback callback) {
        this.context = context;
        call(createRequest(),callback);
    }

    private void call(Request request, final HttpCallback callback){

        if(LoginData.access_token.length() < 3){
            client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                Handler mainHandler = new Handler(context.getMainLooper());
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFialure(null,e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.i("Log1","" + response.code());
                    String res =  response.body().string();
                    Log.i("Log2: ","response: " + res);
                    if(response.code() == 200){
                    }else{

                    }

                    callback.onSuccess(response);
                }
            });
        }else{
            Log.i("Log1","User is not Loged in");
        }


    }

    private Request createRequest(){
        Request.Builder request = new Request.Builder().url(mainUrl + address);
        request.addHeader("M2M","Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==pouya");
        request.addHeader("authorization","Bearer " + LoginData.access_token);
        return request.build();
    }
}
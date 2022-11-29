package com.example.wallebi_app.api;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMeApi {
    private OkHttpClient client;
    String address = "v0/UserService/me/";
    Context context;
    Button btn;
    ProgressBar progressBar;

    public GetMeApi(Context context, Button btn, ProgressBar progressBar) {
        this.context = context;
        this.btn = btn;
        this.progressBar = progressBar;
    }

    public GetMeApi(Context context) {
        this.context = context;
    }

    private void call(Request request, final HttpCallback callback){

        client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            Handler mainHandler = new Handler(context.getMainLooper());
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFialure(null,e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.code() == 200){

                }else{

                }

                callback.onSuccess(response);
            }
        });
    }
}

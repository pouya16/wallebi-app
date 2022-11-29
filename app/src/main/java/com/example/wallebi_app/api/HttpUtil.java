package com.example.wallebi_app.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wallebi_app.database.LoginData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private OkHttpClient client;
    private final String mainUrl = "https://api.wallebi.run/";
    private Context context;

    public static final int GET = 0;
    public static final int POST = 1;


    public static final int MODE_AUTH = 1;
    public static final int MODE_NO_AUTH = 0;

    public HttpUtil(Context context) {
        this.context = context;
    }


    public void get(String url,Map<String,String> map, HttpCallback callback, int mode){
        call(createRequest(mainUrl+url,map,null,mode),callback);
    }

    public void post(String url,String body,Map<String,String> map,HttpCallback callback,int mode){

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody reqBody = RequestBody.create(body,JSON);
        call(createRequest(mainUrl+url,map,reqBody,mode),callback);

    }


    private void call(Request request,final HttpCallback callback){
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
    }

    private Request createRequest(String url, Map<String,String> headers, RequestBody body, int mode){
        Request.Builder request = new Request.Builder().url(url);
        request.addHeader("M2M","Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==pouya");
        if(mode == MODE_AUTH)
            request.addHeader("authorization","Bearer " + LoginData.access_token);
        if(headers!=null){
            for (Map.Entry<String,String> header:
                    headers.entrySet()) {
                request.addHeader(header.getKey(),header.getValue());
            }
        }
       if(body!=null)
           request.post(body);
       return request.build();
    }



}

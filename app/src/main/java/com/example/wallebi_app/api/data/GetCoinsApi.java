package com.example.wallebi_app.api.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wallebi_app.R;
import com.example.wallebi_app.database.DataAccess;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetCoinsApi {
    private OkHttpClient client;
    private String address_crypto = "v0/CryptoService/crypto_list/";
    private String address_fiat = "v0/CryptoService/fiat_list/";
    private String mainUrl = "";
    private Context context;
    public static final int MODE_CRYPTO = 0;
    public static final int MODE_FIAT = 1;
    int mode = 0;

    public GetCoinsApi(Context context,int mode){
        this.context = context;
        this.mode = mode;
        mainUrl =  context.getString(R.string.base_url);
        call(createRequest());
    }


    private void call(Request request){
        client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(context.getMainLooper());
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("Log2","failed to get");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("Log2","" + response.code());
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
                            if(mode == MODE_CRYPTO){
                                dataAccess.createCryptoList(list);
                            }else if(mode == MODE_FIAT){
                                dataAccess.createFiatList(list);
                            }
                        }
                    }else{
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
            }
        });
    }

    private Request createRequest(){
        String local_address = (mode == MODE_CRYPTO) ?  address_crypto :  address_fiat;
        return new Request.Builder().url(mainUrl + local_address).build();
    }
}

package com.example.wallebi_app.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.bank.BankAccountsModel;
import com.example.wallebi_app.api.bank.IbanAccountsModel;
import com.example.wallebi_app.api.data.CoinListModel;
import com.example.wallebi_app.database.DataAccess;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.database.models.MeModel;
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

public class GetBankCardsApi {
    private OkHttpClient client;
    String address = "v0/UserService/get_bank_accounts/";
    private String mainUrl = "";
    Context context;


    public GetBankCardsApi(Context context, HttpCallback callback) {
        this.context = context;
        call(createRequest(),callback);
        mainUrl = context.getString(R.string.base_url);
    }

    public GetBankCardsApi(Context context) {
        this.context = context;
        call(createRequest());
        mainUrl = context.getString(R.string.base_url);
    }

    private void call(Request request){

        Log.i("Log1", "access inside get me is : " + LoginData.access_token );
        if(LoginData.access_token.length() > 3){
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
                                Type userListType = new TypeToken<ArrayList<BankAccountsModel>>(){}.getType();
                                ArrayList<BankAccountsModel> list = gson.fromJson(String.valueOf(jsonObject.getJSONArray("data")),userListType);
                                if(list.size() != 0){
                                    for(int i = 0;i<list.size();i++){
                                        if(list.get(i).getCard_number() !=null)
                                            if(list.get(i).getCard_number().length() > 3){
                                                DataAccess.addBankModel(list.get(i));
                                            }
                                        if(list.get(i).getIban() !=null)
                                            if(list.get(i).getIban().length() > 3){
                                                IbanAccountsModel model = new IbanAccountsModel(list.get(i).getId(),list.get(i).getCard_number(),list.get(i).getIban(),list.get(i).getBank_name());
                                                DataAccess.addIbanModel(model);
                                            }

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
        }else{
            Log.i("Log1","User is not Loged in");
        }


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
        request.addHeader("authorization","Bearer " + LoginData.access_token);
        return request.build();
    }
}

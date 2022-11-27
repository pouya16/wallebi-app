package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.BodyHandlingModel;
import com.example.wallebi_app.api.BodyMaker;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.ResponseErrorHandler;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.reg.apis.SetPasswordApi;
import com.example.wallebi_app.api.reg.model.DataRegisterPassword;
import com.example.wallebi_app.api.reg.model.SetPasswordBody;
import com.example.wallebi_app.api.reg.responses.DataLogin;
import com.example.wallebi_app.api.reg.responses.SetPasswordResponse;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.helpers.StringHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetPasswordActivity extends AppCompatActivity {

    EditText txtPass,txtPassRepeat;
    MaterialCardView btnShowPass,btnShowPassRepeat;
    MaterialButton btnSetPassword;
    ProgressBar progressBar;

    boolean showPass = false;
    String email = "";
    String allow_access = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        loadView();
        email = getIntent().getStringExtra("email");
        allow_access = getIntent().getStringExtra("allow_key");


        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeShowPassword();
            }
        });
        btnShowPassRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeShowPassword();
            }
        });


        btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPass.getText().toString().compareTo(txtPassRepeat.getText().toString()) == 0){
                    if(StringHelper.checkComplexity(txtPass.getText().toString())>3){
                        singUp(email,allow_access,txtPass.getText().toString());
                    }else{
                        StringHelper.showSnackBar(SetPasswordActivity.this,getString(R.string.weak_pass),getString(R.string.set_password),0);
                    }
                }else{
                    StringHelper.showSnackBar(SetPasswordActivity.this,getString(R.string.passwrd_match_error),getString(R.string.set_password),0);
                }
            }
        });


    }




    private void changeShowPassword(){
        if(showPass){
            txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtPassRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPass = false;
        }else{
            txtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            txtPassRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPass = true;
        }
    }





    public void singUp(String email,String allow_access,String pass){

        String url = "v1/UserService/signup/set_password/";

        btnSetPassword.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        List<BodyHandlingModel> bodyList = new ArrayList<>();
        bodyList.add(new BodyHandlingModel("email",email,"string"));
        bodyList.add(new BodyHandlingModel("allow_key",allow_access,"string"));
        bodyList.add(new BodyHandlingModel("new_password",pass,"string"));
        bodyList.add(new BodyHandlingModel("confirm_password",pass,"string"));
        String postBody= BodyMaker.Companion.getBody(bodyList);

        //Log.i("Log1","testString is: " + testString);
        Log.i("Log1", "generated string is: " + postBody);

        HttpCallback callback = new HttpCallback() {

            private void resetUi(){
                btnSetPassword.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            Handler mainHandler = new Handler(getBaseContext().getMainLooper());

            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(SetPasswordActivity.this);
                    responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));

                }catch (Exception e){

                }
                mainHandler.post(() -> {
                    resetUi();
                });
            }

            @Override
            public void onSuccess(okhttp3.Response response) {

                Log.i("Log1","" + response.code());
                try{
                    String res =  response.body().string();
                    Log.i("Log1: ","response: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(response.code() == 200){
                        Gson gson = new Gson();
                        DataRegisterPassword data = gson.fromJson(jsonObject.getJSONObject("data").toString(),DataRegisterPassword.class);
                        Log.i("Log1","otp: " + data.getPermissions().getOtp());
                        Log.i("Log1","g2f: " + data.getPermissions().getG2f());
                        LoginData.access_token = data.getAccess_token();
                        LoginData.refresh_token = data.getRefresh_token();

                        Intent intent = new Intent(SetPasswordActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        SetPasswordActivity.this.finish();

                        if(data.getPermissions().needPermission()){
                        }else{
                            //Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                            //startActivity(intent);
                        }
                        SetPasswordActivity.this.finish();

                    }else{
                        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(SetPasswordActivity.this);
                        responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
                mainHandler.post(() -> resetUi());
            }
        };

        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.post(url,postBody,null,callback,HttpUtil.MODE_NO_AUTH);




        /*Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        SetPasswordApi setPasswordApi = retrofit.create(SetPasswordApi.class);
        Call<SetPasswordResponse> call = setPasswordApi.setPass(new SetPasswordBody(allow_access,email,pass,pass));
        call.enqueue(new Callback<SetPasswordResponse>() {
            @Override
            public void onResponse(Call<SetPasswordResponse> call, Response<SetPasswordResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSetPassword.setVisibility(View.VISIBLE);
                try {
                    if (response.body().getSuccess()){
                        LoginData.access_token = response.body().getData().getAccess_token();
                        LoginData.refresh_token = response.body().getData().getRefresh_token();
                        Intent intent = new Intent(SetPasswordActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        SetPasswordActivity.this.finish();
                    }

                }catch (Exception e){
                    StringHelper.showSnackBar(SetPasswordActivity.this,getString(R.string.cant_set_password),getString(R.string.set_password),0);
                }
            }
            @Override
            public void onFailure(Call<SetPasswordResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSetPassword.setVisibility(View.VISIBLE);
                StringHelper.showSnackBar(SetPasswordActivity.this,getString(R.string.cant_set_password),getString(R.string.set_password),0);
            }
        });*/
    }



    private void loadView(){
        txtPass = findViewById(R.id.txt_new_pass);
        txtPassRepeat = findViewById(R.id.txt_repeat_pass);
        btnShowPass = findViewById(R.id.btn_show_pass);
        btnShowPassRepeat = findViewById(R.id.btn_show_repeat_pass);
        btnSetPassword = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressbar);
    }
}
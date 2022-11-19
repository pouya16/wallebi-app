package com.example.wallebi_app.acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.wallebi_app.MainActivity;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.BodyHandlingModel;
import com.example.wallebi_app.api.BodyMaker;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.reg.apis.AskOtpApi;
import com.example.wallebi_app.api.reg.apis.NormalRegisterApi;
import com.example.wallebi_app.api.reg.apis.PreLoginApi;
import com.example.wallebi_app.api.reg.model.RegisterNormalBody;
import com.example.wallebi_app.api.reg.model.SendOtpBody;
import com.example.wallebi_app.api.reg.responses.EOtpResponse;
import com.example.wallebi_app.api.reg.responses.PreLoginRes;
import com.example.wallebi_app.api.reg.responses.VerifyEmailResponse;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.helpers.StringHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginRegisterActivity extends AppCompatActivity {


    String main_url;

    //Register Mode Views:
    MaterialButton btnSendEmail, btnSignUp, btnSignUpSocial;
    EditText txtEmail, txtOTP, txtReferral;
    CheckBox checkTerms;
    TextView txtTerms, txtLogin, txtReferralError, txtAskReferral;
    ViewFlipper viewFlipper;
    ProgressBar signUpProgressBar;


    // Login Mode Views:
    TextView txtRegister;
    EditText txtEmailLogin, txtMobile, txtPassword;
    LinearLayout layoutMobile, layoutEmail;
    MaterialButton btnLogin, btnLoginSocial;
    ProgressBar loginProgressBar;
    MaterialButton btnMobile,btnEmail;
    View viewMobile,viewEmail;
    ImageButton btnShowPass;
    Boolean showPass = false;
    //TabLayout loginTabs;
    //TabItem tabEmail, tabMobile;


    private OkHttpClient client;
    int mode = 0;
    String loginType = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loadView();
        mode = getIntent().getIntExtra("mode", 0);


        Log.i("Log1","mode is : " + mode);
        changeMode();



        //REGISTER LOGIC

        main_url = getString(R.string.base_url);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringHelper.isValidEmail(txtEmail.getText())) {
                    btnSendEmail.setActivated(false);
                    sendEmail(txtEmail.getText().toString());
                    countDown(btnSendEmail);
                } else {
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.wrong_email), getString(R.string.email_header), 0);
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                changeMode();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringHelper.isValidEmail(txtEmail.getText())) {
                    if (txtOTP.getText().length() == 6) {
                        if (checkTerms.isChecked()) {
                            btnSignUpSocial.setActivated(false);
                            btnSignUp.setVisibility(View.GONE);
                            signUpProgressBar.setVisibility(View.VISIBLE);
                            signUp(txtEmail.getText().toString(), txtOTP.getText().toString());
                        } else {
                            StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.should_accept_terms), getString(R.string.term_of_use), 0);
                        }
                    } else {
                        StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.wrong_email_otp), getString(R.string.email_header), 0);
                    }

                } else {
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.wrong_email), getString(R.string.email_header), 0);
                }

            }
        });


        // LOGIN LOGIC

        changeLoginType(1);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 1;
                changeMode();
            }
        });


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLoginType(1);
            }
        });

        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLoginType(2);
            }
        });

        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeShowPass();
            }
        });
        /*tabMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLoginType(2);
            }
        });
        tabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLoginType(1);
            }
        });*/


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Log1","email is: " + txtEmailLogin.getText().toString());
                Log.i("Log1","pass is: " + txtPassword.getText().toString());
                if (loginType.compareTo("email") == 0) {
                    if (StringHelper.isValidEmail(txtEmailLogin.getText())){
                        if(txtPassword.getText().length()>5){
                            makeLogin();
                        }else{
                            StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_password_login),getString(R.string.email),0);
                        }
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_email),getString(R.string.email),0);
                    }
                }else{
                    if(txtMobile.getText().length() == 10){
                        if(txtPassword.getText().length()>5){
                            makeLogin();
                        }else{
                            StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_password_login),getString(R.string.email),0);
                        }
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_mobile),getString(R.string.email),0);
                    }

                }
            }
        });

    }




    public void makeLogin(){
        btnLogin.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.VISIBLE);
        /*Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        PreLoginApi preLoginApi = retrofit.create(PreLoginApi.class);
        Call<String> call;
        RequestBody body;
        if(loginType.compareTo("email") == 0){
            Map<String,Object> emailHash = new ArrayMap<>();
            emailHash.put("password",txtPassword.getText().toString());
            emailHash.put("type","pass");
            emailHash.put("username","pouyaa16@gmail.com");
            emailHash.put("username_type",loginType);
            emailHash.put("captcha_value",true);
            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(emailHash)).toString());
            call = preLoginApi.sendPreLogin(body);
        }else{

            Map<String,Object> mobileHash = new ArrayMap<>();
            mobileHash.put("password",txtPassword.getText().toString());
            mobileHash.put("type","pass");
            mobileHash.put("username",txtMobile.getText().toString());
            mobileHash.put("username_type",loginType);
            mobileHash.put("captcha_value",true);
            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(mobileHash)).toString());
            ///call = preLoginApi.sendPreLogin(mobileHash);
            call = preLoginApi.sendPreLogin(body);
        }



        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnLogin.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
                Log.i("Log1","response has come");
                Log.i("Log1","response code is: "  + response.code());
                try {
                    if(response.code() == 200){


                        Log.i("Log1","request exceeded : " + response.body());
                        */
        /*if(response.body().getSuccess()){

                            Log.i("Log1","success Login");
                            if(!response.body().getData().getPermission().getG2f()&&!response.body().getData().getPermission().getOtp()){
                                LoginData.access_token = response.body().getData().getAccess_token();
                                LoginData.refresh_token = response.body().getData().getRefresh_token();
                                Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginRegisterActivity.this.finish();
                            }else{
                                Intent intent = new Intent(LoginRegisterActivity.this, GetSecuritiesActivity.class);
                                intent.putExtra("token",response.body().getData().getAccess_token());
                                intent.putExtra("mode",0);
                                if(loginType.compareTo("email") == 0){
                                    intent.putExtra("email",txtEmail.getText().toString());
                                }else{
                                    intent.putExtra("mobile",txtMobile.getText().toString());
                                }
                                intent.putExtra("otp",response.body().getData().getPermission().getOtp());
                                intent.putExtra("g2f",response.body().getData().getPermission().getG2f());
                                startActivity(intent);
                                LoginRegisterActivity.this.finish();
                            }
                        }else{
                            StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.log_in_failed), response.body().getErr(), 0);
                            Log.i("Log1","failed Login");
                        }*/
        /*
                    }else if(response.code() == 429){
                        StringHelper.showSnackBar(LoginRegisterActivity.this, response.body(), getString(R.string.login) + "", 0);
                        Log.i("Log1","request exceeded : " + response.body());
                    }else if(response.code() == 404){
                        Log.i("Log1","user pass error" +response.body().toString());
                        StringHelper.showSnackBar(LoginRegisterActivity.this, response.body(), getString(R.string.login) + "", 0);
                    }
                }catch (Exception e){

                    Log.i("Log1","failed Login: " + e.toString());
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.log_in_failed), getString(R.string.login), 0);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnLogin.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
                StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.log_in_failed), getString(R.string.login), 0);
            }
        });*/

        String testString = "{\n" +
                "    \"username\": \"pouya16@gmail.com\",\n" +
                "    \"password\": \"my_pass\",\n" +
                "    \"type\": \"pass\",\n" +
                "    \"username_type\": \"password\",\n" +
                "    \"captcha_value\": \"1\"\n" +
                "}";

        List<BodyHandlingModel> bodyList = new ArrayList<>();
        bodyList.add(new BodyHandlingModel("username","pouya16@gmail.com","string"));
        bodyList.add(new BodyHandlingModel("password","my_pass","string"));
        bodyList.add(new BodyHandlingModel("type","pass","string"));
        bodyList.add(new BodyHandlingModel("username_type","password","string"));
        bodyList.add(new BodyHandlingModel("captcha_value","1","string"));
        String postBody= BodyMaker.Companion.getBody(bodyList);

        Log.i("Log1","testString is: " + testString);
        Log.i("Log1", "generated string is: " + postBody);


       final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(testString,JSON);
        client = new OkHttpClient();
        final Request request = new Request.Builder().url("https://api.wallebi.run/v1/UserService/pre_login/")
                .addHeader("M2M","Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                btnLogin.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
                Log.i("Log1","error is"  + e.toString());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {


                btnLogin.setVisibility(View.VISIBLE);
                loginProgressBar.setVisibility(View.GONE);
                Log.i("Log1","response is"  + response.body().string());
            }
        });

    }

    public void signUp(String email, String code) {
        Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        NormalRegisterApi registerApi = retrofit.create(NormalRegisterApi.class);
        Call<VerifyEmailResponse> call = registerApi.registerEmail(new RegisterNormalBody(code, email));
        call.enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                signUpProgressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignUpSocial.setActivated(true);
                try {
                    if (response.body().getSuccess()) {
                        Intent intent = new Intent(LoginRegisterActivity.this, SetPasswordActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("allow_key", response.body().getData().getAllow_key());
                        startActivity(intent);
                        LoginRegisterActivity.this.finish();
                    } else {
                        StringHelper.showSnackBar(LoginRegisterActivity.this, response.body().getErr(), getString(R.string.sign_up), 0);
                    }
                } catch (Exception e) {
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.sign_up_failed), getString(R.string.sign_up), 0);
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {/*
                signUpProgressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignUpSocial.setActivated(true);
                StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.sign_up_failed), getString(R.string.sign_up), 0);
*/
            }
        });

    }


    private void changeShowPass(){
        if(showPass){
            showPass = false;
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            showPass = true;
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    public void changeLoginType(int mode) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 40;

        if (mode == 1) {
            btnEmail.setTextColor(getColor(R.color.mvp_gray4));
            btnMobile.setTextColor(getColor(R.color.mvp_gray2));
            viewEmail.setBackgroundColor(getColor(R.color.mvp_yellow));
            viewEmail.setLayoutParams(new LinearLayout.LayoutParams(width/2,2));
            viewMobile.setBackgroundColor(getColor(R.color.mvp_gray2));
            viewEmail.setLayoutParams(new LinearLayout.LayoutParams(width/2,1));
            layoutEmail.setVisibility(View.VISIBLE);
            layoutMobile.setVisibility(View.GONE);
            loginType = "email";
        } else {
            btnEmail.setTextColor(getColor(R.color.mvp_gray2));
            btnMobile.setTextColor(getColor(R.color.mvp_gray4));
            viewEmail.setBackgroundColor(getColor(R.color.mvp_gray2));
            viewEmail.setLayoutParams(new LinearLayout.LayoutParams(width/2,1));
            viewMobile.setBackgroundColor(getColor(R.color.mvp_yellow));
            viewEmail.setLayoutParams(new LinearLayout.LayoutParams(width/2,2));
            layoutEmail.setVisibility(View.GONE);
            layoutMobile.setVisibility(View.VISIBLE);
            loginType = "mobile";
        }
    }


    public void changeMode() {
        if (mode != 0 && LoginData.registerModel != 0) {
            mode = 2;
        }
        Log.i("Log1","mode in change mode function is: " + mode);
        switch (mode) {
            case 0:
                viewFlipper.setDisplayedChild(0);
                break;
            case 1:
                viewFlipper.setDisplayedChild(1);
                break;
            case 2:
                viewFlipper.setDisplayedChild(2);
                break;
        }
    }


    public void sendEmail(String email) {
        Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        AskOtpApi askOtpApi = retrofit.create(AskOtpApi.class);
        Call<EOtpResponse> call = askOtpApi.sendEOtp(new SendOtpBody(email));
        call.enqueue(new Callback<EOtpResponse>() {
            @Override
            public void onResponse(Call<EOtpResponse> call, Response<EOtpResponse> response) {
                if (response.body().getSuccess()) {
                    Log.i("Log1","otp is: " );
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.email_otp_send), getString(R.string.otp_header), 1);
                } else {
                    StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.email_otp_failed), getString(R.string.otp_header), 0);
                }
            }

            @Override
            public void onFailure(Call<EOtpResponse> call, Throwable t) {
                StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.email_otp_failed), getString(R.string.otp_header), 0);
            }
        });

        /*String url = main_url + "v1/UserService/signup/send_code/";
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        countDown(btnSendEmail);
                        try {
                            jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("success");
                            if(success){

                            }else{

                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("M2M", "Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==");

                return params;
            }
        };
        queue.add(getRequest);
*/

    }


    public void countDown(final Button btn) {
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                btn.setText("" + seconds / 60 + ":" + seconds % 60);
            }

            public void onFinish() {
                btn.setText(R.string.get_code);
                btn.setActivated(true);
            }

        }.start();

    }


    public void loadView() {
        btnSendEmail = findViewById(R.id.btn_get_code_email);
        txtEmail = findViewById(R.id.txt_input);
        txtOTP = findViewById(R.id.txt_email_otp);
        txtReferral = findViewById(R.id.txt_referral);
        btnSignUp = findViewById(R.id.btn_register);
        btnSignUpSocial = findViewById(R.id.btn_register_social);
        txtLogin = findViewById(R.id.txt_btn_login);
        txtTerms = findViewById(R.id.btn_term);
        checkTerms = findViewById(R.id.checkbox_terms);
        txtReferralError = findViewById(R.id.txt_referral_error);
        txtAskReferral = findViewById(R.id.btn_ask_refer);
        viewFlipper = findViewById(R.id.login_vf);
        signUpProgressBar = findViewById(R.id.progress_signup);
        txtRegister = findViewById(R.id.txt_btn_register);
       /* loginTabs = findViewById(R.id.tab_login);
        tabEmail = findViewById(R.id.tab_email);
        tabMobile = findViewById(R.id.tab_mobile);*/
        viewEmail = findViewById(R.id.view_email);
        btnShowPass = findViewById(R.id.show_pass_login);
        viewMobile = findViewById(R.id.view_mobile);
        btnEmail = findViewById(R.id.btn_email);
        btnMobile = findViewById(R.id.btn_phone);
        txtEmailLogin = findViewById(R.id.txt_email_login);
        txtMobile = findViewById(R.id.txt_mobile);
        txtPassword = findViewById(R.id.txt_pass);
        layoutMobile = findViewById(R.id.layout_mobile);
        layoutEmail = findViewById(R.id.layout_email);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginSocial = findViewById(R.id.btn_login_social);
        loginProgressBar = findViewById(R.id.progress_login);

    }


}
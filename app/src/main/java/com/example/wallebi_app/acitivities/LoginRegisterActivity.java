package com.example.wallebi_app.acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.wallebi_app.MainActivity;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.BodyHandlingModel;
import com.example.wallebi_app.api.BodyMaker;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.ResponseErrorHandler;
import com.example.wallebi_app.api.reg.model.DataEmailVerify;
import com.example.wallebi_app.api.reg.responses.DataLogin;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.database.UserLogin;
import com.example.wallebi_app.helpers.StringHelper;
import com.example.wallebi_app.models.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;
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



    //REGISTER CLOSED
    EditText txtEmailClosed;
    MaterialButton btnDoneClosed;
    TextView txtLoginClosed;


    // Login Mode Views:
    ImageButton btnFingerLogin;
    TextView txtRegister;
    EditText txtEmailLogin, txtMobile, txtPassword;
    LinearLayout layoutMobile, layoutEmail;
    MaterialButton btnLogin, btnLoginSocial;
    ProgressBar loginProgressBar;
    MaterialButton btnMobile,btnEmail;
    View viewMobile,viewEmail;
    ImageButton btnShowPass;
    Boolean showPass = false;


    //BIOMETRICS
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    //TabLayout loginTabs;
    //TabItem tabEmail, tabMobile;


    int mode = 0;
    int permanent_mode;
    String loginType = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loadView();
        permanent_mode = getIntent().getIntExtra("mode", 0);
        mode = permanent_mode;


        Log.i("Log1","mode is : " + mode);
        changeMode();

        //REGISTER CLOSED LOGIC
        btnDoneClosed.setOnClickListener(view -> {
            if (StringHelper.isValidEmail(txtEmail.getText())) {
                StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.your_email_saved),getString(R.string.email_header),0);
                LoginRegisterActivity.this.finish();
            } else {
                StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.wrong_email), getString(R.string.email_header), 1);
            }
        });

        txtLoginClosed.setOnClickListener(view -> {
            mode = 0;
            changeMode();
        });



        //REGISTER LOGIC
        main_url = getString(R.string.base_url);
        btnSendEmail.setOnClickListener(view -> {
            if (StringHelper.isValidEmail(txtEmail.getText())) {
                btnSendEmail.setActivated(false);
                sendEmail(txtEmail.getText().toString());
                countDown(btnSendEmail);
            } else {
                StringHelper.showSnackBar(LoginRegisterActivity.this, getString(R.string.wrong_email), getString(R.string.email_header), 0);
            }
        });

        txtLogin.setOnClickListener(view -> {
            mode = 0;
            changeMode();
        });


        btnSignUp.setOnClickListener(view -> {
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

        });


        // LOGIN LOGIC
        changeLoginType(1);

        UserLogin userLogin = new UserLogin(this);


        ///BIO METRICS :
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginRegisterActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                UserLogin userLogin1 = new UserLogin(LoginRegisterActivity.this);
                makeLogin(userLogin1.getUser().getEmail(),userLogin1.getUser().getPassword(),"email");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
        if(userLogin.getUser().getEmail() == ""){
            btnFingerLogin.setVisibility(View.GONE);
        }

        btnFingerLogin.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });


        txtRegister.setOnClickListener(view -> {
            mode = permanent_mode;
            changeMode();
        });


        btnEmail.setOnClickListener(v -> changeLoginType(1));
        btnMobile.setOnClickListener(v -> changeLoginType(2));
        btnShowPass.setOnClickListener(view -> changeShowPass());


        btnLogin.setOnClickListener(view -> {
            Log.i("Log1","email is: " + txtEmailLogin.getText().toString());
            Log.i("Log1","pass is: " + txtPassword.getText().toString());
            if (loginType.compareTo("email") == 0) {
                if (StringHelper.isValidEmail(txtEmailLogin.getText())){
                    if(txtPassword.getText().length()>5){
                        makeLogin(txtEmailLogin.getText().toString(),txtPassword.getText().toString(),"email");
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_password_login),getString(R.string.email),0);
                    }
                }else{
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_email),getString(R.string.email),0);
                }
            }else{
                if(txtMobile.getText().length() == 10){
                    if(txtPassword.getText().length()>5){
                        makeLogin(txtMobile.getText().toString(),txtPassword.getText().toString(),"mobile");
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_password_login),getString(R.string.email),0);
                    }
                }else{
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_mobile),getString(R.string.email),0);
                }

            }
        });

    }




    public void makeLogin(String email,String password,String mode){
        btnLogin.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.VISIBLE);


        List<BodyHandlingModel> bodyList = new ArrayList<>();
        //String loginMode = "email";
        /*if(loginType.compareTo("email") == 0){
            txtEmailLogin.getText().toString();
            loginMode = "email";
        }else{
            loginMode = "mobile";
        }*/
        bodyList.add(new BodyHandlingModel("username",email,"string"));
        bodyList.add(new BodyHandlingModel("password",password,"string"));
        bodyList.add(new BodyHandlingModel("type","pass","string"));
        bodyList.add(new BodyHandlingModel("username_type",mode,"string"));
        bodyList.add(new BodyHandlingModel("captcha_value","1","string"));
        String postBody= BodyMaker.Companion.getBody(bodyList);

        //Log.i("Log1","testString is: " + testString);
        Log.i("Log1", "generated string is: " + postBody);

        HttpCallback callback = new HttpCallback() {

            private void resetUi(){
                loginProgressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }

            Handler mainHandler = new Handler(getBaseContext().getMainLooper());

            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(LoginRegisterActivity.this);
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
                        DataLogin data = gson.fromJson(jsonObject.getJSONObject("data").toString(),DataLogin.class);
                        Log.i("Log1","otp: " + data.getPermissions().getOtp());
                        Log.i("Log1","g2f: " + data.getPermissions().getG2f());
                        UserLogin userLogin = new UserLogin(LoginRegisterActivity.this);
                        userLogin.saveUser(new UserModel(email,txtPassword.getText().toString(),email));
                        LoginData.access_token = data.getAccess_token();
                        LoginData.refresh_token = data.getRefresh_token();
                        if(data.getPermissions().needPermission()){
                            Intent intent = new Intent(LoginRegisterActivity.this, GetSecuritiesActivity.class);
                            intent.putExtra("token",data.getAccess_token());
                            intent.putExtra("mode",0);
                            if(loginType.compareTo("email") == 0){
                                intent.putExtra("email",txtEmail.getText().toString());
                            }else{
                                intent.putExtra("mobile",txtMobile.getText().toString());
                            }
                            startActivity(intent);
                        }else{
                            //Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                            //startActivity(intent);
                        }
                        LoginRegisterActivity.this.finish();

                    }else{
                        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(LoginRegisterActivity.this);
                        responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
                mainHandler.post(() -> resetUi());
            }
        };

        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.post("v1/UserService/pre_login/",postBody,null,callback,HttpUtil.MODE_NO_AUTH);



    }



    public void signUp(String email, String code) {

        String endPoint = "v1/UserService/signup/confirm_code/";
        List<BodyHandlingModel> bodyList = new ArrayList<>();
        if(txtReferral.getText().toString().length()>0){
            //bodyList
        }
        bodyList.add(new BodyHandlingModel("email",email,"string"));
        bodyList.add(new BodyHandlingModel("code",code,"string"));
        String postBody= BodyMaker.Companion.getBody(bodyList);

        HttpCallback httpCallback = new HttpCallback() {

            private void resetUI(){
            }
            Handler mainHandler = new Handler(getBaseContext().getMainLooper());

            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(LoginRegisterActivity.this);
                    responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));

                }catch (Exception e){

                }
                mainHandler.post(() -> {
                    resetUI();
                });
            }

            @Override
            public void onSuccess(okhttp3.Response response) {
                resetUI();
                Log.i("Log1","" + response.code());
                try{
                    String res =  response.body().string();
                    Log.i("Log1: ","response: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(response.code() == 200){
                        Gson gson = new Gson();
                        DataEmailVerify data = gson.fromJson(jsonObject.getJSONObject("data").toString(),DataEmailVerify.class);
                        Intent intent = new Intent(LoginRegisterActivity.this, SetPasswordActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("allow_key", data.getAllow_key());
                        startActivity(intent);
                        LoginRegisterActivity.this.finish();
                    }else{
                        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(LoginRegisterActivity.this);
                        responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));
                    }
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
                mainHandler.post(() -> resetUI());

            }
        };

        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.post(endPoint,postBody,null,httpCallback,HttpUtil.MODE_NO_AUTH);



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
        String url = "v1/UserService/signup/send_code/";

        List<BodyHandlingModel> bodyList = new ArrayList<>();
        if(txtReferral.getText().toString().length()>0){
            //bodyList
        }
        bodyList.add(new BodyHandlingModel("email",email,"string"));
        String postBody= BodyMaker.Companion.getBody(bodyList);
        HttpCallback httpCallback = new HttpCallback() {

            private void resetUI(){
                signUpProgressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
            }
            Handler mainHandler = new Handler(getBaseContext().getMainLooper());

            @Override
            public void onFialure(okhttp3.Response response, Throwable throwable) {
                try {
                    String res = response.body().string();
                    Log.i("Log1: ","response is: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler(LoginRegisterActivity.this);
                    responseErrorHandler.createError(response.code(),jsonObject,getString(R.string.log_in_failed));

                }catch (Exception e){

                }
                mainHandler.post(() -> {
                    resetUI();
                });
            }

            @Override
            public void onSuccess(okhttp3.Response response) {
                resetUI();
                Log.i("Log1","" + response.code());
                try{
                    String res =  response.body().string();
                    Log.i("Log1: ","response: " + res);
                }catch (Exception e){
                    Log.i("Log1","failed to convert to json: " + e);
                }
                mainHandler.post(() -> resetUI());

            }
        };

        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.post(url,postBody,null,httpCallback,HttpUtil.MODE_NO_AUTH);


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

        //Resgister mode closed
        txtLoginClosed = findViewById(R.id.txt_btn_login_closed);
        btnDoneClosed = findViewById(R.id.btn_register_closed);
        txtEmailClosed = findViewById(R.id.txt_input_closed);
        btnFingerLogin = findViewById(R.id.btn_login_finger);

    }


}
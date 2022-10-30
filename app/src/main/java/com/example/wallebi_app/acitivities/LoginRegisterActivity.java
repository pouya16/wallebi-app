package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.RetrofitNoAuthBuilder;
import com.example.wallebi_app.api.VolleyRequests;
import com.example.wallebi_app.api.reg.apis.AskOtpApi;
import com.example.wallebi_app.api.reg.apis.NormalRegisterApi;
import com.example.wallebi_app.api.reg.model.RegisterNormalBody;
import com.example.wallebi_app.api.reg.model.SendOtpBody;
import com.example.wallebi_app.api.reg.responses.EOtpResponse;
import com.example.wallebi_app.api.reg.responses.VerifyEmailResponse;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.helpers.StringHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginRegisterActivity extends AppCompatActivity{


    String main_url;
    MaterialButton btnSendEmail,btnSignUp,btnSignUpSocial;
    EditText txtEmail,txtOTP,txtReferral;
    CheckBox checkTerms;
    TextView txtTerms,txtLogin,txtReferralError,txtAskReferral;
    ViewFlipper viewFlipper;
    ProgressBar signUpProgressBar;
    int mode =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loadView();
        mode = getIntent().getIntExtra("mode",0);
        changeMode();


        main_url = getString(R.string.base_url);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringHelper.isValidEmail(txtEmail.getText())){
                    btnSendEmail.setActivated(false);
                    sendEmail(txtEmail.getText().toString());
                    countDown(btnSendEmail);
                }else{
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_email),getString(R.string.email_header),0);
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
                if(StringHelper.isValidEmail(txtEmail.getText())){
                    if(txtOTP.getText().length() == 6){
                        if(checkTerms.isChecked()){
                            btnSignUpSocial.setActivated(false);
                            btnSignUp.setVisibility(View.GONE);
                            signUpProgressBar.setVisibility(View.VISIBLE);
                            signUp(txtEmail.getText().toString(),txtOTP.getText().toString());
                        }else{
                            StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.should_accept_terms),getString(R.string.term_of_use),0);
                        }
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_email_otp),getString(R.string.email_header),0);
                    }

                }else{
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.wrong_email),getString(R.string.email_header),0);
                }

            }
        });






    }

    public void signUp(String email, String code){
        Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        NormalRegisterApi registerApi = retrofit.create(NormalRegisterApi.class);
        Call<VerifyEmailResponse> call = registerApi.registerEmail(new RegisterNormalBody(code,email));
        call.enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                signUpProgressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignUpSocial.setActivated(true);
                try {
                    if(response.body().getSuccess()){
                        Intent intent = new Intent(LoginRegisterActivity.this,SetPasswordActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("allow_key",response.body().getData().getAllow_key());
                        startActivity(intent);
                        LoginRegisterActivity.this.finish();
                    }else{
                        StringHelper.showSnackBar(LoginRegisterActivity.this,response.body().getErr(),getString(R.string.sign_up),0);
                    }
                }catch (Exception e){
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.sign_up_failed),getString(R.string.sign_up),0);
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                signUpProgressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignUpSocial.setActivated(true);
                StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.sign_up_failed),getString(R.string.sign_up),0);

            }
        });

    }



    public void changeMode(){
        if(mode != 0 && LoginData.registerModel != 0){
            mode = 2;
        }
        switch (mode){
            case 0:
                viewFlipper.setDisplayedChild(0);
            case 1:
                viewFlipper.setDisplayedChild(1);
            case 2:
                viewFlipper.setDisplayedChild(2);
        }
    }



    public void sendEmail(String email){
        Retrofit retrofit = RetrofitNoAuthBuilder.getRetrofitAuthSingleton(this).getRetrofit();
        AskOtpApi askOtpApi = retrofit.create(AskOtpApi.class);
        Call<EOtpResponse> call = askOtpApi.sendEOtp(new SendOtpBody(email));
        call.enqueue(new Callback<EOtpResponse>() {
            @Override
            public void onResponse(Call<EOtpResponse> call, Response<EOtpResponse> response) {
                if (response.body().getSuccess()){
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.email_otp_send),getString(R.string.otp_header),1);
                }else{
                    StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.email_otp_failed),getString(R.string.otp_header),0);
                }
            }

            @Override
            public void onFailure(Call<EOtpResponse> call, Throwable t) {
                StringHelper.showSnackBar(LoginRegisterActivity.this,getString(R.string.email_otp_failed),getString(R.string.otp_header),0);
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


    public void loadView(){
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
    }


}
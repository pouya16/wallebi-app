package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallebi_app.R;
import com.example.wallebi_app.api.VolleyRequests;
import com.example.wallebi_app.helpers.StringHelper;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity{

    RequestQueue queue;
    String main_url;
    JSONObject jsonObject;
    MaterialButton btnSendEmail,btnSignUp,btnSignUpSocial;
    EditText txtEmail,txtOTP,txtReferral;
    CheckBox checkTerms;
    TextView txtTerms,txtLogin,txtReferralError,txtAskReferral;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loadView();

        queue = Volley.newRequestQueue(this);
        main_url = getString(R.string.base_url);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSendEmail.setActivated(false);
                if(StringHelper.isValidEmail(txtEmail.getText())){
                    sendEmail(txtEmail.getText().toString());
                }else{

                }
            }
        });





    }






    public void sendEmail(String email){
        String url = main_url + "v1/UserService/signup/send_code/";
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
    }


}
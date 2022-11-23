package com.example.wallebi_app.acitivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.BodyHandlingModel;
import com.example.wallebi_app.api.BodyMaker;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.reg.model.PermissionModel;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.helpers.StringHelper;
import com.example.wallebi_app.helpers.UiHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class GetSecuritiesActivity extends AppCompatActivity {

    int mode = 0;

    //view initialize
    MaterialCardView cardMobile,cardOTP,cardEmail,cardG2f;
    EditText txtMobile,txtOTP,txtEmail,txtG2f;
    MaterialButton btnGetOTP,btnGetEmail,btnSubmit;
    UiHelper uiHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_securities);

        loadView();

        // set view due to different incomes
        mode = getIntent().getIntExtra("mode",0);
        PermissionModel permissionModel = LoginData.permissionModel;

        mode = (permissionModel.getOtp() == false) ?  mode + 1 : mode;
        mode = (permissionModel.getG2f() == false) ?  mode + 2 : mode;
        Log.i("Log1", "mode is: " + mode);
        updateView();



    }



    //api calls for this page :

    public void sendOTP(){
        uiHelper = new UiHelper(GetSecuritiesActivity.this,btnGetOTP);
        btnGetOTP.setActivated(false);
        uiHelper.countDown();
        List<BodyHandlingModel> bodyList = new ArrayList<>();
        String postBody= BodyMaker.Companion.getBody(bodyList);
        bodyList.add(new BodyHandlingModel("type","otp","string"));
        HttpCallback callback = new HttpCallback() {
            Handler mainHandler = new Handler(getBaseContext().getMainLooper());
            @Override
            public void onFialure(Response response, Throwable throwable) {
                mainHandler.post(() ->{
                    StringHelper.showSnackBar(GetSecuritiesActivity.this, getString(R.string.failed_otp), getString(R.string.otp_code), 2);
                });

            }

            @Override
            public void onSuccess(Response response) {
                mainHandler.post(() ->{
                    StringHelper.showSnackBar(GetSecuritiesActivity.this, getString(R.string.success_otp), getString(R.string.otp_code), 1);
                });
            }
        };
        HttpUtil httpUtil = new HttpUtil(this);
        httpUtil.post("v1/UserService/pre_login/send_code/",postBody,null,callback,HttpUtil.MODE_AUTH);


    }







    // view handling :
    private void updateView(){
        switch (mode){
            case 1:
                modeLogin();
                cardOTP.setVisibility(View.GONE);
                break;
            case 2:
                modeLogin();
                cardG2f.setVisibility(View.GONE);
                break;

            default:
                modeLogin();
                break;
        }
    }

    private void modeLogin(){
        cardMobile.setVisibility(View.GONE);
        cardEmail.setVisibility(View.GONE);
    }




    private void loadView(){
        cardMobile = findViewById(R.id.card_mobile);
        cardOTP = findViewById(R.id.card_otp);
        cardEmail = findViewById(R.id.card_email);
        cardG2f = findViewById(R.id.card_g2f);
        txtMobile = findViewById(R.id.txt_mobile);
        txtOTP = findViewById(R.id.txt_otp);
        txtEmail = findViewById(R.id.txt_email);
        txtG2f = findViewById(R.id.txt_g2f);
        btnGetOTP = findViewById(R.id.btn_get_code);
        btnGetEmail = findViewById(R.id.btn_get_code_email);
        btnSubmit = findViewById(R.id.btn_login);
    }


}
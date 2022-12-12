package com.example.wallebi_app.fragments.setting;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.GetMeApi;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.setting.MeDataModel;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.database.models.MeModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.Response;

public class SettingFragment extends Fragment {



    TextView txtEmail,txtKycLevel,txtUserId,txtTaker,txtMaker;
    MaterialCardView cardBasicInfo,cardSecurities,cardWhiteList,cardAccountActivity,cardLogOut;
    ImageButton btnBack,btnCpyUid;
    RelativeLayout layoutLoad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        loadView(view);

        btnBack.setOnClickListener(v -> {getActivity().onBackPressed();});
        btnCpyUid.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("uid", txtUserId.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(),"uid copied to clipboard successfully",Toast.LENGTH_SHORT).show();
        });

        cardBasicInfo.setOnClickListener(v ->{
            findNavController(this).navigate(R.id.action_settingFragment_to_basicInfoFragment);
        });

        if(LoginData.meClass != null){
            loadData(LoginData.meClass);
        }else{
            layoutLoad.setVisibility(View.VISIBLE);
            getMe();
        }

        return view;
    }



    private void getMe(){
        HttpCallback httpCallback = new HttpCallback() {
            Handler handler = new Handler(getContext().getMainLooper());
            @Override
            public void onFialure(Response response, Throwable throwable) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().onBackPressed();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {
                Log.i("Log1", "" + response.code());
                try {
                    String res = response.body().toString();
                    Log.i("Log1: ", "response: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if(response.code() == 200){
                        if(jsonObject.getBoolean("success")){
                            Gson gson = new Gson();
                            MeModel meClass = gson.fromJson(jsonObject.getJSONObject("msg").toString(),MeModel.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadData(meClass);
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    Log.i("Log1", "failed to convert to json: " + e.toString());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutLoad.setVisibility(View.GONE);
                    }
                });
            }
        };
        GetMeApi getMeApi = new GetMeApi(getContext(),httpCallback);
    }


    private void loadData(MeModel meModel){
        txtEmail.setText(meModel.getEmail());
        txtKycLevel.setText(meModel.getKyc_level().getLevel());
        txtUserId.setText(meModel.getUid());
        txtTaker.setText(meModel.getUser_level().getTaker_fee() + "");
        txtMaker.setText(meModel.getUser_level().getMaker_fee() + "");
    }

    private void loadView(View v){
        btnBack = v.findViewById(R.id.img_back);
        btnCpyUid = v.findViewById(R.id.img_cpy_uid);
        txtEmail = v.findViewById(R.id.txt_email);
        txtKycLevel = v.findViewById(R.id.txt_kyc_level);
        txtUserId = v.findViewById(R.id.txt_uid);
        txtTaker = v.findViewById(R.id.txt_taker);
        txtMaker = v.findViewById(R.id.txt_maker);
        cardBasicInfo = v.findViewById(R.id.card_basic);
        cardSecurities = v.findViewById(R.id.card_securities);
        cardWhiteList = v.findViewById(R.id.card_whitelist);
        cardAccountActivity = v.findViewById(R.id.card_account_activity);
        layoutLoad = v.findViewById(R.id.layout_load);
    }
}
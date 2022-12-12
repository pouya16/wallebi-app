package com.example.wallebi_app.fragments.setting;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallebi_app.R;
import com.example.wallebi_app.acitivities.kyc.KycMainActivity;
import com.example.wallebi_app.database.LoginData;
import com.example.wallebi_app.database.models.MeModel;
import com.google.android.material.card.MaterialCardView;


public class BasicInfoFragment extends Fragment {


    ImageButton btnBack,btnCpyUid;
    TextView txtEmail,txtUid,txtKycLevel,txtKycVerified,txtMaxWithdrawal,txtUserLevel,txtMaker,txtTaker,txtBankAccountNumber;
    MaterialCardView cardKyc,cardVip,cardBankAccounts;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_basic_info, container, false);
        loadView(view);

        btnCpyUid.setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("uid", txtUid.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(),"uid copied to clipboard successfully",Toast.LENGTH_SHORT).show();
        });
        cardKyc.setOnClickListener(v-> {
            Intent intent = new Intent(getContext(), KycMainActivity.class);
            requireContext().startActivity(intent);
        });


        btnBack.setOnClickListener(v-> requireActivity().onBackPressed());
        cardVip.setOnClickListener(v-> findNavController(this).navigate(R.id.action_basicInfoFragment_to_VIPFragment));
        cardBankAccounts.setOnClickListener(v-> findNavController(this).navigate(R.id.action_basicInfoFragment_to_bankFragment));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(LoginData.meClass);
    }

    private void loadData(MeModel meClass){
        txtEmail.setText(meClass.getEmail());
        txtUid.setText(meClass.getUid());
        int kycLevel = meClass.getKyc_level().getLevel();
        txtKycLevel.setText(kycLevel + "");
        if (kycLevel != 0) {
            if(meClass.getKyc_level().getState() == 0){
                txtKycVerified.setText("verified");
                txtKycVerified.setTextColor(Color.GREEN);
            }else{
                txtKycVerified.setText("pending");
                txtKycVerified.setTextColor(Color.YELLOW);
            }
        }else{
            txtKycVerified.setText(getContext().getString(R.string.unverified));
            txtKycVerified.setTextColor(Color.RED);
        }
        txtMaxWithdrawal.setText(meClass.getKyc_level().getMax_withdraw() + "BTC");
        txtUserLevel.setText(meClass.getUser_level().getLevel());
        txtMaker.setText(meClass.getUser_level().getMaker_fee() + " %");
        txtTaker.setText(meClass.getUser_level().getTaker_fee() + " %");
        if(meClass.getBankAccount_No() > 0){
            txtBankAccountNumber.setText(meClass.getBankAccount_No() + " back accounts added");
        }else{
            txtBankAccountNumber.setText(getContext().getString(R.string.you_didnt_add_card));
        }
    }

    private void loadView(View v){
        btnBack = v.findViewById(R.id.img_back);
        btnCpyUid = v.findViewById(R.id.img_cpy);
        txtEmail = v.findViewById(R.id.txt_email);
        txtUid = v.findViewById(R.id.txt_uid);
        txtKycLevel = v.findViewById(R.id.txt_kyc_level);
        txtKycVerified = v.findViewById(R.id.kyc_verification);
        txtMaxWithdrawal = v.findViewById(R.id.txt_max_withdraw);
        txtUserLevel = v.findViewById(R.id.txt_vip_level);
        txtMaker = v.findViewById(R.id.txt_maker);
        txtTaker = v.findViewById(R.id.txt_taker);
        txtBankAccountNumber = v.findViewById(R.id.txt_back_account_number);
        cardKyc = v.findViewById(R.id.card_kyc);
        cardVip = v.findViewById(R.id.card_vip);
        cardBankAccounts = v.findViewById(R.id.card_bankcards);


    }
}
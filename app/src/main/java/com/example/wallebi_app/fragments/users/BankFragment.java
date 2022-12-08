package com.example.wallebi_app.fragments.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.HttpCallback;
import com.example.wallebi_app.api.HttpUtil;
import com.example.wallebi_app.api.bank.BankAccountsModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import okhttp3.Response;

public class BankFragment extends Fragment {


    public BankFragment() {
        // Required empty public constructor
    }

    //views part
    MaterialButton btnCard,btnIban,btnAddNew;
    View lineCard,lineIban;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageView imgNoCard;


    //CONTROLERS
    int mode = 1;//if 0 card mode - if 1 iban mode
    ArrayList<BankAccountsModel> bankCards;
    ArrayList<BankAccountsModel> bankIbans;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank, container, false);
        loadViews(view);
        changeMode();
        bankCards = new ArrayList<>();
        bankIbans = new ArrayList<>();




        return view;
    }


    public void getCards(){

        HttpUtil httpUtil = new HttpUtil(getContext());
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onFialure(Response response, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNoCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Response response) {

            }
        };
        httpUtil.get("v0/UserService/get_bank_accounts/",null,callback,HttpUtil.MODE_AUTH);

    }

    public void changeMode(){
        if(mode == 0){
            mode = 1;
            btnIban.setTextColor(getContext().getColor(R.color.mvp_gray4));
            btnCard.setTextColor(getContext().getColor(R.color.mvp_gray2));
            lineIban.setBackgroundColor(getContext().getColor(R.color.mvp_yellow));
            lineCard.setBackgroundColor(getContext().getColor(R.color.mvp_gray2));
            btnAddNew.setText(getContext().getString(R.string.add_new_iban));
        }else{
            mode = 0;
            btnIban.setTextColor(getContext().getColor(R.color.mvp_gray2));
            btnCard.setTextColor(getContext().getColor(R.color.mvp_gray4));
            lineCard.setBackgroundColor(getContext().getColor(R.color.mvp_yellow));
            lineIban.setBackgroundColor(getContext().getColor(R.color.mvp_gray2));
            btnAddNew.setText(getContext().getString(R.string.add_new_card));
        }
    }

    public void loadViews(View v){
        btnCard = v.findViewById(R.id.btn_card);
        btnIban = v.findViewById(R.id.btn_iban);
        lineCard = v.findViewById(R.id.view_card);
        lineIban = v.findViewById(R.id.view_iban);
        btnAddNew = v.findViewById(R.id.btn_add);
        recyclerView = v.findViewById(R.id.recycler_cards);
        progressBar = v.findViewById(R.id.progressbar);
        imgNoCard = v.findViewById(R.id.img_no_card);
    }
}
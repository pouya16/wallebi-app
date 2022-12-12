package com.example.wallebi_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.bank.BankAccountsModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class BankCardsAdapter extends RecyclerView.Adapter<BankCardsAdapter.BankCardsViewHolder> {

    ArrayList<BankAccountsModel> arrayList;
    Context context;
    int mode;//0 for card 1 for iban

    public BankCardsAdapter(ArrayList<BankAccountsModel> arrayList, Context context,int mode) {
        this.arrayList = arrayList;
        this.context = context;
        this.mode = mode;
    }

    @NonNull
    @Override
    public BankCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BankCardsViewHolder(inflater.inflate(R.layout.recycler_back_accounts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankCardsViewHolder holder, int position) {
        holder.txtCardName.setText(arrayList.get(position).getBank_name());
        if(mode == 0){
            holder.txtCardNumber.setText(arrayList.get(position).getCard_number());
        }else{
            holder.txtCardNumber.setText(arrayList.get(position).getIban());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BankCardsViewHolder extends RecyclerView.ViewHolder{

        MaterialButton btnDelete,btnWithdraw;
        TextView txtCardName,txtCardNumber;

        public BankCardsViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnWithdraw = itemView.findViewById(R.id.btn_withdraw);
            txtCardName = itemView.findViewById(R.id.txt_bank_name);
            txtCardNumber = itemView.findViewById(R.id.txt_acc_number);
        }
    }

}

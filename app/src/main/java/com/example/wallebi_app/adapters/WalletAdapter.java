package com.example.wallebi_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.wallet.MarketsModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

    ArrayList<MarketsModel> arrayList;
    Context context;
    int mode = 0;
    double change = 1;//0 for card 1 for iban
    public static final int MODE_USDT = 0;
    public static final int MODE_TOMAN = 1;


    public WalletAdapter(ArrayList<MarketsModel> arrayList, Context context, Double change,int mode) {
        this.arrayList = arrayList;
        this.context = context;
        this.change = change;
        this.mode = mode;
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new WalletViewHolder(inflater.inflate(R.layout.recycler_back_accounts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        holder.layoutExpand.setVisibility(View.GONE);
        holder.imageExpand.setOnClickListener(v -> changeMoreVisibility(holder));
        holder.imageMore.setOnClickListener(v -> changeActionVisibility(holder));
        holder.btnClose.setOnClickListener(v -> changeActionVisibility(holder));
        holder.balance.setText((arrayList.get(position).getLine_price() * change) + "");
        if(mode == MODE_TOMAN){
            holder.marketType.setText(context.getString(R.string.irt));
            holder.marketCoin1.setText(context.getString(R.string.irt));
            holder.marketCoin3.setText(context.getString(R.string.irt));
            holder.marketCoin4.setText(context.getString(R.string.irt));
        }else{
            holder.marketType.setText(context.getString(R.string.usdt));
            holder.marketCoin1.setText(context.getString(R.string.usdt));
            holder.marketCoin3.setText(context.getString(R.string.usdt));
            holder.marketCoin4.setText(context.getString(R.string.usdt));
        }

    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private void changeActionVisibility(WalletViewHolder holder){
        if(holder.cardAction.getVisibility() == View.GONE){
            holder.cardAction.setVisibility(View.VISIBLE);
        }else{
            holder.cardAction.setVisibility(View.GONE);
        }
    }

    private void changeMoreVisibility(WalletViewHolder holder){
        if(holder.layoutExpand.getVisibility() == View.GONE){
            holder.layoutExpand.setVisibility(View.VISIBLE);
        }else{
            holder.layoutExpand.setVisibility(View.GONE);
        }
    }
    public class WalletViewHolder extends RecyclerView.ViewHolder{

        MaterialButton btnTrade,btnWithdraw,btnDeposit;
        ImageButton btnClose,imageExpand,imageMore,imgCoin;
        MaterialCardView cardAction;
        LinearLayout layoutExpand;
        TextView totalBalanceMore,usdtEquivalent,availableBalance,inOrderBalance,coinName,balance;
        TextView marketType,marketCoin1,marketCoin3,marketCoin4;


        public WalletViewHolder(@NonNull View itemView) {
            super(itemView);
            btnTrade = itemView.findViewById(R.id.btn_trade);
            btnWithdraw = itemView.findViewById(R.id.btn_withdraw);
            btnDeposit = itemView.findViewById(R.id.btn_deposit);
            btnClose = itemView.findViewById(R.id.btn_img_shrink);
            imageExpand = itemView.findViewById(R.id.btn_img_arrow);
            imageMore = itemView.findViewById(R.id.btn_img_action);
            imgCoin = itemView.findViewById(R.id.img_coin_image);
            cardAction = itemView.findViewById(R.id.card_action);
            layoutExpand = itemView.findViewById(R.id.layout_expand);
            usdtEquivalent = itemView.findViewById(R.id.txt_usdt_equivalent);
            availableBalance = itemView.findViewById(R.id.txt_available);
            inOrderBalance = itemView.findViewById(R.id.txt_in_order);
            totalBalanceMore = itemView.findViewById(R.id.txt_total_coin);
            coinName = itemView.findViewById(R.id.txt_coin_symbol);
            balance = itemView.findViewById(R.id.balance);
            marketType = itemView.findViewById(R.id.txt_market_type);
            marketCoin1 = itemView.findViewById(R.id.txt_market_coin1);
            //marketCoin2 = itemView.findViewById(R.id.txt_market_coin2);
            marketCoin3 = itemView.findViewById(R.id.txt_market_coin3);
            marketCoin4 = itemView.findViewById(R.id.txt_market_coin4);


        }
    }

}

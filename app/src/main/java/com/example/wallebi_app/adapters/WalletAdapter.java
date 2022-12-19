package com.example.wallebi_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallebi_app.R;
import com.example.wallebi_app.api.wallet.models.WalletModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

    ArrayList<WalletModel> arrayList;
    HashMap<String,Double> marketsHash;
    Context context;
    boolean hide_small;
    boolean hide_amount;
    boolean coin_mode;
    int mode = 0;
    public static final int MODE_USDT = 0;
    public static final int MODE_TOMAN = 1;


    public WalletAdapter(ArrayList<WalletModel> arrayList, Context context, HashMap<String,
            Double> marketsHash, int mode,boolean hide_small,boolean hide_amount,boolean coin_mode) {
        this.arrayList = arrayList;
        this.context = context;
        this.marketsHash = marketsHash;
        this.mode = mode;
        this.hide_amount = hide_amount;
        this.hide_small = hide_small;
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new WalletViewHolder(inflater.inflate(R.layout.recycler_wallet,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        holder.layoutExpand.setVisibility(View.GONE);
        holder.imageExpand.setOnClickListener(v -> changeMoreVisibility(holder));
        holder.imageMore.setOnClickListener(v -> changeActionVisibility(holder));
        holder.btnClose.setOnClickListener(v -> changeActionVisibility(holder));
        holder.coinName.setText(arrayList.get(position).getName());
        holder.balance.setText(arrayList.get(position).getBalance().getTotal());
        holder.marketCoin1.setText(arrayList.get(position).getName());
        holder.marketCoin3.setText(arrayList.get(position).getName());
        holder.marketCoin4.setText(arrayList.get(position).getName());
        /*if (arrayList.get(position).getName().toLowerCase().compareTo("irt") == 0){
            if(coin_mode){
                holder.itemView.setVisibility(View.GONE);
            }else{
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }else{
            if(!coin_mode){
                holder.itemView.setVisibility(View.GONE);
            }else{
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }*/
        holder.totalBalanceMore.setText(arrayList.get(position).getBalance().getTotal());
        holder.availableBalance.setText(arrayList.get(position).getBalance().getAvailable());
        holder.inOrderBalance.setText(arrayList.get(position).getBalance().getBlocked());
        try{
            double price = Double.parseDouble(arrayList.get(position).getBalance().getAvailable()) * marketsHash.get(arrayList.get(position).getName());
            holder.usdtEquivalent.setText(price + "");
            if(hide_small){
                if(price < 0.1){
                    holder.itemView.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            holder.usdtEquivalent.setText("0");
        }
        if(hide_amount){
            holder.balance.setText("*");
            holder.totalBalanceMore.setText("*");
            holder.availableBalance.setText("*");
            holder.inOrderBalance.setText("*");
            holder.usdtEquivalent.setText("*");
        }
        if(mode == MODE_TOMAN){
            holder.txtMarketFiat.setText(context.getString(R.string.irt));
            holder.txtFiatExp.setText(context.getString(R.string.irt_equivalent));
        }else{
            holder.txtMarketFiat.setText(context.getString(R.string.usdt));
            holder.txtFiatExp.setText(context.getString(R.string.usdt_equivalent));
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
        ImageButton btnClose,imageExpand,imageMore;
        ImageView imgCoin;
        MaterialCardView cardAction;
        LinearLayout layoutExpand;
        TextView totalBalanceMore,availableBalance,inOrderBalance,coinName,balance;
        TextView txtMarketFiat,usdtEquivalent,txtFiatExp;
        TextView marketCoin1,marketCoin3,marketCoin4;


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
            availableBalance = itemView.findViewById(R.id.txt_available);
            inOrderBalance = itemView.findViewById(R.id.txt_in_order);
            totalBalanceMore = itemView.findViewById(R.id.txt_total_coin);
            coinName = itemView.findViewById(R.id.txt_coin_symbol);
            balance = itemView.findViewById(R.id.balance);
            marketCoin1 = itemView.findViewById(R.id.txt_market_coin1);
            //marketCoin2 = itemView.findViewById(R.id.txt_market_coin2);
            marketCoin3 = itemView.findViewById(R.id.txt_market_coin3);
            marketCoin4 = itemView.findViewById(R.id.txt_market_coin4);

            txtMarketFiat = itemView.findViewById(R.id.txt_market_coin2);
            usdtEquivalent = itemView.findViewById(R.id.txt_usdt_equivalent);
            txtFiatExp = itemView.findViewById(R.id.fiat_explain);
        }
    }

}

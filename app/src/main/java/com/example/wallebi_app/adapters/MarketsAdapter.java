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
import com.example.wallebi_app.api.markets.MarketApiModel;
import com.example.wallebi_app.api.wallet.models.WalletModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;

public class MarketsAdapter extends RecyclerView.Adapter<MarketsAdapter.MarketViewHolder> {

    ArrayList<MarketApiModel> arrayList;
    HashMap<String,MarketApiModel> favoritesHash;
    Context context;


    public MarketsAdapter(ArrayList<MarketApiModel> arrayList, Context context, HashMap<String,
            MarketApiModel> favorites) {
        this.arrayList = arrayList;
        this.context = context;
        this.favoritesHash = favorites;
    }

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MarketViewHolder(inflater.inflate(R.layout.recycler_markets,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        holder.txtMarket.setText(arrayList.get(position).getTicker_from());
        holder.txtTicker.setText(arrayList.get(position).getTicker_to());
        holder.txtPrice.setText(arrayList.get(position).getPrice() +"");
        holder.txtMarketCap.setText(arrayList.get(position).getMarket_cap());
        holder.txtChange.setText(arrayList.get(position).getChanged_amount_24());
        holder.txtVol.setText(context.getString(R.string.vol) + " " + arrayList.get(position).getVolume_24());
        if(arrayList.get(position).getChanged_amount_24().contains("-")){
            holder.txtChange.setTextColor(context.getColor(R.color.mvp_red));
        }else{
            holder.txtChange.setTextColor(context.getColor(R.color.mvp_greem));
        }
        if(favoritesHash.containsKey((arrayList.get(position).getTicker_from() + arrayList.get(position).getTicker_to()))){
            holder.imgFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_star_filled));
        }else{
            holder.imgFavorite.setImageDrawable(context.getDrawable(R.drawable.ic_star_outline));
        }


    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MarketViewHolder extends RecyclerView.ViewHolder{
        ImageView imgFavorite;
        TextView txtMarket,txtTicker,txtPrice,txtMarketCap,txtVol,txtChange;
        public MarketViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            txtMarket = itemView.findViewById(R.id.txt_market);
            txtTicker = itemView.findViewById(R.id.txt_ticker);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtMarketCap = itemView.findViewById(R.id.txt_marketcap);
            txtChange = itemView.findViewById(R.id.txt_24change);
            txtVol = itemView.findViewById(R.id.txt_24vol);

        }
    }

}

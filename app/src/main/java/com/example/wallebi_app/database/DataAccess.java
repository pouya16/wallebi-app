package com.example.wallebi_app.database;

import com.example.wallebi_app.api.data.CoinListModel;

import java.util.ArrayList;

public class DataAccess {


    private static ArrayList<CoinListModel> cryptoListModels = null;
    private static ArrayList<CoinListModel> coinListModels = null;




    public void createCryptoList(ArrayList<CoinListModel> models){
        cryptoListModels = null;
        for (CoinListModel model:
             models) {
            if(model.is_active()){
                cryptoListModels.add(model);
            }
        }
    }

    public ArrayList<CoinListModel> getCryptoListModels(){
        return cryptoListModels;
    }


    public void createFiatList(ArrayList<CoinListModel> models){
        cryptoListModels = null;
        for (CoinListModel model:
                models) {
            if(model.is_active()){
                cryptoListModels.add(model);
            }
        }
    }

    public ArrayList<CoinListModel> getFiatListModels(){
        return cryptoListModels;
    }


}

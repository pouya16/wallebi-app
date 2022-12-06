package com.example.wallebi_app.database;

import com.example.wallebi_app.api.data.CoinListModel;

import java.util.ArrayList;

public class DataAccess {


    private static ArrayList<CoinListModel> cryptoListModels = null;
    private static ArrayList<CoinListModel> coinListModels = null;




    public void createCryptoList(ArrayList<CoinListModel> models){
        cryptoListModels = null;
        cryptoListModels = new ArrayList<>();
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
        coinListModels = null;
        coinListModels = new ArrayList<>();
        for (CoinListModel model:
                models) {
            if(model.is_active()){
                coinListModels.add(model);
            }
        }
    }

    public ArrayList<CoinListModel> getFiatListModels(){
        return coinListModels;
    }

    /*public String[] getFiatsStringArray(){
        if(coinListModels != null){
            String fiatsArray[] = new String[coinListModels.size()];
            for (int i = 0;i<coinListModels.size();i++) {
                fiatsArray[i] = coinListModels.get(i).getName()
            }
        }
    }*/


}

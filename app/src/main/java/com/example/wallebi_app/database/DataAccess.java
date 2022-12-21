package com.example.wallebi_app.database;

import com.example.wallebi_app.api.bank.BankAccountsModel;
import com.example.wallebi_app.api.bank.IbanAccountsModel;
import com.example.wallebi_app.api.data.CoinListModel;
import com.example.wallebi_app.models.DecimalPointsModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DataAccess {


    private static ArrayList<CoinListModel> cryptoListModels = null;
    private static ArrayList<CoinListModel> coinListModels = null;
    private static ArrayList<BankAccountsModel> bankAccountsModels = new ArrayList<>();
    private static ArrayList<IbanAccountsModel> ibanAccountsModels = new ArrayList<>();
    public static HashMap<Integer,DecimalPointsModel> decimalPointsModels;

    public void createIbanModel(ArrayList<IbanAccountsModel> models){
        bankAccountsModels = null;
        bankAccountsModels = new ArrayList<>();
        for (IbanAccountsModel model:
                models) {
            ibanAccountsModels.add(model);
        }
    }



    public static void createBankModel(ArrayList<BankAccountsModel> models){
        bankAccountsModels = null;
        bankAccountsModels = new ArrayList<>();
        for (BankAccountsModel model:
                models) {
            bankAccountsModels.add(model);
        }
    }

    public static ArrayList<IbanAccountsModel> addIbanModel(IbanAccountsModel model){
        if (bankAccountsModels == null){
            ibanAccountsModels = new ArrayList<>();
        }
        ibanAccountsModels.add(model);
        return ibanAccountsModels;
    }

    public static ArrayList<BankAccountsModel> addBankModel(BankAccountsModel model){
        if (bankAccountsModels == null){
            bankAccountsModels = new ArrayList<>();
        }
        bankAccountsModels.add(model);
        return bankAccountsModels;
    }

    public static ArrayList<IbanAccountsModel> getIbanAccountsModels() {
        return ibanAccountsModels;
    }

    public static ArrayList<BankAccountsModel> getBankAccountsModels() {
        return bankAccountsModels;
    }

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

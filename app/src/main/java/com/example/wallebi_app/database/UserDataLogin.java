package com.example.wallebi_app.database;

import android.content.Context;
import android.content.SharedPreferences;



public class UserData {


    //show if in get marketPref or userLoginModel
    int mode;
    // user login data
    final String USER_NAME = "user_name";
    final String PASSWORD = "password";
    final String TOKEN = "token";
    final String PREF_NAME = "LoginPref";




    // LOGIN MODEL DATA
    private String userName;
    private String password;
    private String token;
    private Context context;


    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public UserData(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,0);
        editor = pref.edit();
    }


    // constructor for login data
    public UserData(LoginModel loginModel, Context context) {
        this.userName = loginModel.getUserName().get();
        this.password = loginModel.getPassword().get();
        this.token = loginModel.getToken();
        this.context = context;
        new UserData(context);
    }


    //Save Login
    public boolean saveLogin(){
        boolean success = false;
        if(mode == 1){
            pref = context.getSharedPreferences(PREF_NAME,0);
            editor = pref.edit();
            if(pref.contains(USER_NAME)){
                return success;
            }
            success = true;
            editor.putString(USER_NAME,userName);
            editor.putString(PASSWORD,password);
            editor.putString(TOKEN,token);
            editor.commit();
        }
        return success;
    }


    //check Login
    public boolean isLoginSaved(){
        boolean isSaved = false;
        if(mode == 1){
            if(pref.contains(USER_NAME)){
                isSaved = true;
            }
        }
        return isSaved;
    }


    // get Login DataLogin
    public LoginModel getLogin(){
        userName = pref.getString(USER_NAME,null);
        password = pref.getString(PASSWORD,null);
        token = pref.getString(TOKEN,null);
        return new LoginModel(userName,password,token);
    }

    //clear login - logout
    public void logOut(){
        editor.clear();
        editor.commit();
    }


}

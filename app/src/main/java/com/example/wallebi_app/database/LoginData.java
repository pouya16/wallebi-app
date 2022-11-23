package com.example.wallebi_app.database;

import com.example.wallebi_app.api.reg.model.PermissionModel;

public class LoginData {
    public static String access_token = "";
    public static String refresh_token = "";
    public static int registerModel = 0;
    public static PermissionModel permissionModel = new PermissionModel(false,false);
    public static final String G2F_DOWNLOAD_ADDRESS = "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en_US&gl=US";
}

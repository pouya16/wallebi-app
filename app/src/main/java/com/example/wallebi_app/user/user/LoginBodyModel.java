package com.example.wallebi_app.user.user;

public class LoginBodyModel {

    String client_id;
    String client_secret;
    String grant_type;
    String recaptcha;
    int mobile_request;
    String action;
    String password;
    String username;

    public LoginBodyModel() {
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
    }

    public int getMobile_request() {
        return mobile_request;
    }

    public void setMobile_request(int mobile_request) {
        this.mobile_request = mobile_request;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /*

                params.put("client_id",context.getResources().getString(R.string.CLIENT_ID));
                params.put("client_secret",context.getResources().getString(R.string.CLIENT_SERCRET));
                params.put("grant_type","password");
                params.put("recaptcha",captcha);
                params.put("mobile_request","1");
                params.put("action","login");
                params.put("password", password);
                params.put("username", userName);
     */
}

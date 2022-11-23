package com.example.wallebi_app.user.user;

public class MfaChangeModel {
    String token;

    public MfaChangeModel(String token) {
        this.token = token;
    }

    public MfaChangeModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

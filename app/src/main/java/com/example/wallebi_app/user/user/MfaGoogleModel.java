package com.example.wallebi_app.user.user;

public class MfaGoogleModel {

    String url;
    String secret_key;

    public MfaGoogleModel(String url, String secret_key) {
        this.url = url;
        this.secret_key = secret_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }
}

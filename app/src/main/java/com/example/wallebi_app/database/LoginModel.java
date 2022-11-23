package com.example.wallebi_app.database;

import androidx.databinding.ObservableField;

public class LoginModel {
    public ObservableField<String> userName;
    private ObservableField<String> password;
    private String token;
    private String refreshToken;
    private String captcha;

    public LoginModel() {
    }

    public LoginModel(String userName, String password, String token) {
        this.userName = new ObservableField<>(userName);
        this.password = new ObservableField<>(password);
        this.token = token;
    }

    public LoginModel(String userName, String password, String token, String captcha) {
        this.userName = new ObservableField<>(userName);
        this.password = new ObservableField<>(password);
        this.token = token;
        this.refreshToken = captcha;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}

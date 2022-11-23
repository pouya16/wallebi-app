package com.example.wallebi_app.user.user;

public class RegisterBodyModel {
    String username;
    int user_level;
    String recaptcha;
    int mobile_request;
    String invite_code;
    String password;

    public RegisterBodyModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
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

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

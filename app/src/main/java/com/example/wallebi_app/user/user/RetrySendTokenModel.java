package com.example.wallebi_app.user.user;

public class RetrySendTokenModel {

    String mobile;

    public RetrySendTokenModel() {
    }

    public RetrySendTokenModel(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

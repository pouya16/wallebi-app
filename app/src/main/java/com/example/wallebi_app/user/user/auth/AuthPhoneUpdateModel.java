package com.example.wallebi_app.user.user.auth;

public class AuthPhoneUpdateModel {
    String phone;

    public AuthPhoneUpdateModel(String phone) {
        this.phone = phone;
    }

    public AuthPhoneUpdateModel() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

package com.example.wallebi_app.user.user.auth;

public class AuthLevelTwoModel {


    String state;
    String city;
    String landline_phone;
    String address;

    public AuthLevelTwoModel(String state, String city, String landline_phone, String address) {
        this.state = state;
        this.city = city;
        this.landline_phone = landline_phone;
        this.address = address;
    }

    public AuthLevelTwoModel() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandline_phone() {
        return landline_phone;
    }

    public void setLandline_phone(String landline_phone) {
        this.landline_phone = landline_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

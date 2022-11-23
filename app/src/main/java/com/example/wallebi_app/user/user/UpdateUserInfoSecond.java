package com.example.wallebi_app.user.user;

public class UpdateUserInfoSecond {

    String first_name;
    String last_name;
    String email;
    String identity_id;
    String address;
    String state;
    String city;
    String landline_phone;

    public UpdateUserInfoSecond(UpdateUserInfoFirstModel firstModel,String address, String state, String city, String landline_phone) {
        this.address = address;
        this.state = state;
        this.city = city;
        this.landline_phone = landline_phone;
        this.first_name = firstModel.getFirst_name();
        this.last_name = firstModel.getLast_name();
        this.email = firstModel.getEmail();
        this.identity_id = firstModel.getIdentity_id();
    }

    public UpdateUserInfoSecond() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}

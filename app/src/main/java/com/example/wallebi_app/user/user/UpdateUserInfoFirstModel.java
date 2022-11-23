package com.example.wallebi_app.user.user;

public class UpdateUserInfoFirstModel {

    String first_name;
    String last_name;
    String email;
    String identity_id;

    public UpdateUserInfoFirstModel(String first_name, String last_name, String email, String identity_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.identity_id = identity_id;
    }

    public UpdateUserInfoFirstModel() {
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
}

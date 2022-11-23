package com.example.wallebi_app.user.user;

public class MfaModel {

    String username;
    String token;
    String identifier;

    public MfaModel(String username, String token, String identifier) {
        this.username = username;
        this.token = token;
        this.identifier = identifier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

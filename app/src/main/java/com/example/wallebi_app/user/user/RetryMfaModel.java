package com.example.wallebi_app.user.user;

public class RetryMfaModel {
    String identifier;

    public RetryMfaModel(String identifier) {
        this.identifier = identifier;
    }

    public RetryMfaModel() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

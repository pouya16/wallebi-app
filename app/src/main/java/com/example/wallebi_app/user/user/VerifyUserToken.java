package com.example.wallebi_app.user.user;

public class VerifyUserToken {
    String username;
    String verify_code;

    public VerifyUserToken() {
    }

    public VerifyUserToken(String userName, String verify_code) {
        this.username = userName;
        this.verify_code = verify_code;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }
}

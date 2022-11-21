package com.example.wallebi_app.api.reg.responses;

public class PreLoginRes {

    String message;
    Boolean success;
    int availableIn;
    String err;
    int remain;
    DataLogin data;




    public PreLoginRes() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getAvailableIn() {
        return availableIn;
    }

    public void setAvailableIn(int availableIn) {
        this.availableIn = availableIn;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public DataLogin getData() {
        return data;
    }

    public void setData(DataLogin data) {
        this.data = data;
    }
}

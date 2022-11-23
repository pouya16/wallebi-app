package com.example.wallebi_app.user.user;

public class CommissionModel {
    String commission_percent;
    int from_user_count;
    int until_user_count;

    public CommissionModel() {
    }

    public String getCommission_percent() {
        return commission_percent;
    }

    public void setCommission_percent(String commission_percent) {
        this.commission_percent = commission_percent;
    }

    public int getFrom_user_count() {
        return from_user_count;
    }

    public void setFrom_user_count(int from_user_count) {
        this.from_user_count = from_user_count;
    }

    public int getUntil_user_count() {
        return until_user_count;
    }

    public void setUntil_user_count(int until_user_count) {
        this.until_user_count = until_user_count;
    }
}

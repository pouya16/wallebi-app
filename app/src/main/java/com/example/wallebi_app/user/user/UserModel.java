package com.example.wallebi_app.user.user;

public class UserModel {
    int pk;
    String phone;
    String first_name;
    String last_name;
    String avatar_id;
    int inviter_code;
    int signup_from;
    String avatar;
    String address;
    String email;
    String state;
    String city;
    String created_at;
    int old_id_document;
    int new_id_document;
    boolean is_email_verified;
    boolean is_mobile_verified;
    int waiting_for_upgrade;
    boolean is_address_verified;
    boolean is_profile_verified;
    boolean is_mobile_owner;
    boolean is_golden;
    boolean is_superuser;
    String landline_phone;
    String identity_id;
    boolean is_landline_phone_owner;
    boolean mfa_enabled;
    int mfa_type;
    CommissionModel commission_detail;

    public UserModel() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(String avatar_id) {
        this.avatar_id = avatar_id;
    }

    public int getInviter_code() {
        return inviter_code;
    }

    public void setInviter_code(int inviter_code) {
        this.inviter_code = inviter_code;
    }

    public int getSignup_from() {
        return signup_from;
    }

    public void setSignup_from(int signup_from) {
        this.signup_from = signup_from;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getOld_id_document() {
        return old_id_document;
    }

    public void setOld_id_document(int old_id_document) {
        this.old_id_document = old_id_document;
    }

    public int getNew_id_document() {
        return new_id_document;
    }

    public void setNew_id_document(int new_id_document) {
        this.new_id_document = new_id_document;
    }

    public boolean isIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public boolean isIs_mobile_verified() {
        return is_mobile_verified;
    }

    public void setIs_mobile_verified(boolean is_mobile_verified) {
        this.is_mobile_verified = is_mobile_verified;
    }

    public int getWaiting_for_upgrade() {
        return waiting_for_upgrade;
    }

    public void setWaiting_for_upgrade(int waiting_for_upgrade) {
        this.waiting_for_upgrade = waiting_for_upgrade;
    }

    public boolean isIs_address_verified() {
        return is_address_verified;
    }

    public void setIs_address_verified(boolean is_address_verified) {
        this.is_address_verified = is_address_verified;
    }

    public boolean isIs_profile_verified() {
        return is_profile_verified;
    }

    public void setIs_profile_verified(boolean is_profile_verified) {
        this.is_profile_verified = is_profile_verified;
    }

    public boolean isIs_mobile_owner() {
        return is_mobile_owner;
    }

    public void setIs_mobile_owner(boolean is_mobile_owner) {
        this.is_mobile_owner = is_mobile_owner;
    }

    public boolean isIs_golden() {
        return is_golden;
    }

    public void setIs_golden(boolean is_golden) {
        this.is_golden = is_golden;
    }

    public boolean isIs_superuser() {
        return is_superuser;
    }

    public void setIs_superuser(boolean is_superuser) {
        this.is_superuser = is_superuser;
    }

    public String getLandline_phone() {
        return landline_phone;
    }

    public void setLandline_phone(String landline_phone) {
        this.landline_phone = landline_phone;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public boolean isIs_landline_phone_owner() {
        return is_landline_phone_owner;
    }

    public void setIs_landline_phone_owner(boolean is_landline_phone_owner) {
        this.is_landline_phone_owner = is_landline_phone_owner;
    }

    public boolean isMfa_enabled() {
        return mfa_enabled;
    }

    public void setMfa_enabled(boolean mfa_enabled) {
        this.mfa_enabled = mfa_enabled;
    }

    public int getMfa_type() {
        return mfa_type;
    }

    public void setMfa_type(int mfa_type) {
        this.mfa_type = mfa_type;
    }

    public CommissionModel getCommission_detail() {
        return commission_detail;
    }

    public void setCommission_detail(CommissionModel commission_detail) {
        this.commission_detail = commission_detail;
    }
}

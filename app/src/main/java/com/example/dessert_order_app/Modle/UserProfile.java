package com.example.dessert_order_app.Modle;

import android.net.Uri;

public class UserProfile {

    String Uid;
    String name;
    String email;
    String phone;
    String password;
    String imagePath;
    String lead;
    String createDate;

    public UserProfile(String uid, String name, String email, String phone, String password, String imagePath, String lead, String createDate) {
        Uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imagePath = imagePath;
        this.lead = lead;
        this.createDate = createDate;
    }

    public UserProfile(){

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

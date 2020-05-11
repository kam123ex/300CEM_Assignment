package com.example.a300cem_assignment.Modle;

import android.net.Uri;

public class UserProfile {

    String Uid;
    String name;
    String email;
    String phone;
    String password;
    String imagePath;

    public UserProfile(String Uid, String name, String email, String phone, String password, String imagePath) {
        this.Uid = Uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imagePath = imagePath;
    }

    public UserProfile(){

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
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

}

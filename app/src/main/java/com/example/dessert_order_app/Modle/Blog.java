package com.example.dessert_order_app.Modle;

import android.media.Image;

public class Blog {

    String blog_title, blog_date, blog_user_name, blog_description, blog_user_id;
    String image_path;

    public Blog(String blog_title, String blog_date, String blog_description, String image_path, String blog_user_id) {
        this.blog_title = blog_title;
        this.blog_date = blog_date;
        this.blog_description = blog_description;
        this.image_path = image_path;
        this.blog_user_id = blog_user_id;
    }

    public Blog(){

    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_date() {
        return blog_date;
    }

    public void setBlog_date(String blog_date) {
        this.blog_date = blog_date;
    }

    public String getBlog_description() {
        return blog_description;
    }

    public void setBlog_description(String blog_description) {
        this.blog_description = blog_description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getBlog_user_id() {
        return blog_user_id;
    }

    public void setBlog_user_id(String blog_user_id) {
        this.blog_user_id = blog_user_id;
    }
}

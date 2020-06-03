package com.example.dessert_order_app.Modle;

import android.net.Uri;

public class Product {

    String product_id, product_name, product_price;
    String image_path;

    public Product(String product_name, String image_path, String product_price) {
        this.product_name = product_name;
        this.image_path = image_path;
        this.product_price = product_price;
    }

    public Product() {

    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}

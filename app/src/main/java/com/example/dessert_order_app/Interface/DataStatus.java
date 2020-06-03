package com.example.dessert_order_app.Interface;

import com.example.dessert_order_app.Modle.Product;

import java.util.ArrayList;
import java.util.List;

public interface DataStatus {
    void DataIsLoaded(ArrayList<Product> products, List<String> keys);
    void DataIsInserted();
    void DataIsUpdated();
    void DataIsDeleted();
}

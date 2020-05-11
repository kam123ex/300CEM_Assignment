package com.example.a300cem_assignment.Interface;

import com.example.a300cem_assignment.Modle.Product;

import java.util.ArrayList;
import java.util.List;

public interface DataStatus {
    void DataIsLoaded(ArrayList<Product> products, List<String> keys);
    void DataIsInserted();
    void DataIsUpdated();
    void DataIsDeleted();
}

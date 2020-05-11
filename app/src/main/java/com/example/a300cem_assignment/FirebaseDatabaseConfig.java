package com.example.a300cem_assignment;

import androidx.annotation.NonNull;

import com.example.a300cem_assignment.Interface.DataStatus;
import com.example.a300cem_assignment.Modle.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseConfig {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceProduct;
    private ArrayList<Product> products = new ArrayList<>();

    public FirebaseDatabaseConfig() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceProduct = mDatabase.getReference("Product");

    }

    public void readProducts(final DataStatus dataStatus) {
        mReferenceProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot sn : dataSnapshot.getChildren()){
                    keys.add(sn.getKey());
                    Product product = sn.getValue(Product.class);
                    products.add(product);
                }
                dataStatus.DataIsLoaded(products, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

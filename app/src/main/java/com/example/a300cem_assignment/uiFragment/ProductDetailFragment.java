package com.example.a300cem_assignment.uiFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.a300cem_assignment.Modle.Product;
import com.example.a300cem_assignment.Modle.UserProfile;
import com.example.a300cem_assignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailFragment extends Fragment {
    private static final String TAG = "ProfileFragment=========";

    private ImageView image_product;
    private ElegantNumberButton btn_number;
    private TextView txt_product_detail_name, txt_product_price;
    private String product_id = "";
    private String userId;
    private String product_name, product_price;
    private UserProfile userProfile = new UserProfile();
    private Product product = new Product();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        Button btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart);

        product_id = getArguments().getString("pid");

        image_product = view.findViewById(R.id.image_product_detail);
        txt_product_detail_name = view.findViewById(R.id.txt_product_detail_name);
        txt_product_price = view.findViewById(R.id.txt_product_detail_price);
        btn_number = view.findViewById(R.id.btn_number);

        firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences preferences = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        String lan = "zh";
        if (language.equals(lan)) {
            Log.d(TAG, "zh");
            databaseReference = firebaseDatabase.getReference("ProductChi");
        } else {

            Log.d(TAG, "en");
            databaseReference = firebaseDatabase.getReference("ProductEng");

        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();

        getUserData(userId);
        getProductDetail(product_id);

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

            }
        });


        return view;
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        getProductDetail(product_id);

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", product_id);
        cartMap.put("pname", product_name);
        cartMap.put("price", product_price);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", btn_number.getNumber());


        cartListRef.child("Users").child(userId)
                .child("Products").child(product_id)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showMessage("Added to Cart");

                            Fragment newFragment = new MenuFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, newFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();



                        }
                    }
                });

    }


    private void getProductDetail(String product_id) {

        databaseReference.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    product = dataSnapshot.getValue(Product.class);
                    Log.d(TAG, product.getProduct_name());
                    product_name = product.getProduct_name();
                    product_price = product.getProduct_price();

                    txt_product_detail_name.setText(product_name);
                    txt_product_price.setText("Price: " + product_price);

                    Picasso.get().load(product.getImage_path()).into(image_product);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getUserData(String myId) {
        DatabaseReference userRef = firebaseDatabase.getReference();

        userRef.child("Users").child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile.setUid(dataSnapshot.child("uid").getValue().toString());
                userProfile.setName(dataSnapshot.child("name").getValue().toString());
                userProfile.setEmail(dataSnapshot.child("email").getValue().toString());
                userProfile.setPhone(dataSnapshot.child("phone").getValue().toString());
                userProfile.setPassword(dataSnapshot.child("password").getValue().toString());
                userProfile.setImagePath(dataSnapshot.child("imagePath").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void showMessage(String message) {

        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}

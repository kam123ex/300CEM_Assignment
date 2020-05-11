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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.Modle.Product;
import com.example.a300cem_assignment.ViewHolder.ProductViewHolder;
import com.example.a300cem_assignment.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Locale;

public class MenuFragment extends Fragment {
    private static final String TAG = "Menu Fragment======";

    View view;
    RecyclerView recyclerView;
    Button btn_favorite;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference engReference;
    DatabaseReference chiReference;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    String myId;
    private boolean checkFavorite = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);


        recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = currentUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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

        FloatingActionButton fab = view.findViewById(R.id.btn_menu_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new CartFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference().child("Favorites");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(databaseReference, Product.class)
                .build();


        final FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull final Product product) {
                final String product_id = getRef(i).getKey();

                databaseReference.child(product_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String product_name = dataSnapshot.child("product_name").getValue().toString();
                        String product_path = dataSnapshot.child("product_path").getValue().toString();
                        String product_price = dataSnapshot.child("product_price").getValue().toString();


                        productViewHolder.getRow_title().setText(product_name);
                        productViewHolder.getRow_price().setText("$" + product_price);

                        Picasso.get().load(product_path).into(productViewHolder.getRow_image());


                        favoriteRef.child("Users").child(myId).child("Products").child(product_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() == null) {

                                    Log.d(TAG, "NULL" + product_id);
                                    productViewHolder.getImage_favorite().setImageResource(R.drawable.ic_not_favorite);
                                    productViewHolder.getImage_favorite().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            favoriteRef.child("Users").child(myId).child("Products").child(product_id).setValue(product_id);
                                            productViewHolder.getImage_favorite().setImageResource(R.drawable.ic_favorite);
                                            showMessage(product_name + " added in your favorite");
                                        }
                                    });

                                } else {
                                    Log.d(TAG, product_id);
                                    productViewHolder.getImage_favorite().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            favoriteRef.child("Users").child(myId).child("Products").child(product_id).removeValue();
                                            productViewHolder.getImage_favorite().setImageResource(R.drawable.ic_not_favorite);
                                            showMessage(product_name + " removed in your favorite");

                                        }
                                    });
                                    productViewHolder.getImage_favorite().setImageResource(R.drawable.ic_favorite);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        productViewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment newFragment = new ProductDetailFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("pid", product_id);
                                newFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false);
                ProductViewHolder viewHolder = new ProductViewHolder(view);

                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}

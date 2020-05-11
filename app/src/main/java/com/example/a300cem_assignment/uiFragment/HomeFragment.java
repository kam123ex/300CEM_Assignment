package com.example.a300cem_assignment.uiFragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.Modle.Product;
import com.example.a300cem_assignment.ViewHolder.ProductViewHolder;
import com.example.a300cem_assignment.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment=========";

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View contactsView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = contactsView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        return contactsView;
    }


    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(databaseReference, Product.class)
                .build();


        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull Product product) {
                String product_id = getRef(i).getKey();

                databaseReference.child(product_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String product_name = dataSnapshot.child("product_name").getValue().toString();
                        String product_path = dataSnapshot.child("product_path").getValue().toString();

                        productViewHolder.getRow_title().setText(product_name);
                        Picasso.get().load(product_path).into(productViewHolder.getRow_image());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                ProductViewHolder viewHolder = new ProductViewHolder(view);

                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        ;
    }

}

package com.example.a300cem_assignment.uiFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a300cem_assignment.Modle.Cart;
import com.example.a300cem_assignment.Modle.Product;
import com.example.a300cem_assignment.Modle.Receipt;
import com.example.a300cem_assignment.R;
import com.example.a300cem_assignment.ViewHolder.CartViewHolder;
import com.example.a300cem_assignment.ViewHolder.ProductViewHolder;
import com.example.a300cem_assignment.ViewHolder.ReceiptViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class ReceiptFragment extends Fragment {
    private static final String TAG = "Receipt Fragment======";

    private RecyclerView recyclerView;
    private String myId;
    private DatabaseReference databaseReference;
    private TextView txt_no_receipt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        recyclerView = view.findViewById(R.id.recycler_receipt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txt_no_receipt = view.findViewById(R.id.txt_no_receipt);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = currentUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders").child("Users")
                .child(myId);


        return view;
    }


    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStart() {
        super.onStart();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    txt_no_receipt.setVisibility(View.GONE);

                    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Receipt>()
                            .setQuery(databaseReference, Receipt.class)
                            .build();

                    final FirebaseRecyclerAdapter<Receipt, ReceiptViewHolder> adapter = new FirebaseRecyclerAdapter<Receipt, ReceiptViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final ReceiptViewHolder receiptViewHolder, int i, @NonNull Receipt receipt) {
                            String address = receipt.getAddress();
                            String date = receipt.getDate();
                            String pname = receipt.getPname();
                            String quantity = receipt.getQuantity();
                            String time = receipt.getTime();
                            String total_amount = receipt.getTotal_amount();
                            String user_name = receipt.getUser_name();

                            Log.d(TAG, address);
                            receiptViewHolder.getTxt_receipt_address().setText("Deliver Address: \n" + address);
                            receiptViewHolder.getTxt_receipt_amount().setText("Total Amount: $" + total_amount);
                            receiptViewHolder.getTxt_receipt_product_name().setText(pname);
                            receiptViewHolder.getTxt_receipt_date().setText("Order Date: \n" + date);
                            receiptViewHolder.getTxt_receipt_quantity().setText("Quantity: " + quantity);
                            receiptViewHolder.getTxt_receipt_user().setText("Order User: " + user_name);
                            receiptViewHolder.getTxt_receipt_time().setText("Order Time: " + time);

                        }

                        @NonNull
                        @Override
                        public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt, parent, false);
                            ReceiptViewHolder viewHolder = new ReceiptViewHolder(view);

                            return viewHolder;
                        }
                    };

                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                } else {
                    txt_no_receipt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
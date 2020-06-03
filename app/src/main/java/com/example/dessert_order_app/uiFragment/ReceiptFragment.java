package com.example.dessert_order_app.uiFragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.Adapter.AdapterReceipt;
import com.example.dessert_order_app.Modle.Receipt;
import com.example.dessert_order_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceiptFragment extends Fragment {
    private static final String TAG = "Receipt Fragment======";

    private AdapterReceipt adapterReceipt;
    private ArrayList<Receipt> receiptList = new ArrayList<>();
    private RecyclerView recyclerView;

    private String myId;
    private DatabaseReference databaseReference;
    private TextView txt_no_receipt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        recyclerView = view.findViewById(R.id.recycler_receipt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        txt_no_receipt = view.findViewById(R.id.txt_no_receipt);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = currentUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");

        return view;
    }


    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStart() {
        super.onStart();

        databaseReference.child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    txt_no_receipt.setVisibility(View.GONE);

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Log.d("TAG", snap.getKey());

                        for (DataSnapshot data : snap.getChildren()) {
                            Log.d("TAG", data.getKey());

                            receiptList.add(data.getValue(Receipt.class));
                            Log.d("TAG", data.getValue(Receipt.class).toString());
                        }

//                        for (DataSnapshot data : dataSnapshot.getChildren()) {
//
//                            String orderid = data.getKey();
//
//                            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Receipt>()
//                                    .setQuery(databaseReference.child(myId).child(orderid), Receipt.class)
//                                    .build();
//                            FirebaseRecyclerAdapter<Receipt, ReceiptViewHolder> adapter = new FirebaseRecyclerAdapter<Receipt, ReceiptViewHolder>(options) {
//                                @Override
//                                protected void onBindViewHolder(@NonNull final ReceiptViewHolder receiptViewHolder, int i, @NonNull Receipt receipt) {
//                                    String address = receipt.getAddress();
//                                    String date = receipt.getDate();
//                                    String order_id = receipt.getOrder_id();
//                                    String phone = receipt.getPhone();
//                                    String pid = receipt.getPid();
//                                    String pname = receipt.getPname();
//                                    String quantity = receipt.getQuantity();
//                                    String time = receipt.getTime();
//                                    String total_amount = receipt.getTotal_amount();
//                                    String user_id = receipt.getUser_id();
//                                    String user_name = receipt.getUser_name();
//
//                                    Log.d(TAG, address);
//                                    receiptViewHolder.getTxt_receipt_address().setText("Deliver Address: " + address);
//                                    receiptViewHolder.getTxt_receipt_date().setText("Order Date: " + date);
//                                    receiptViewHolder.getTxt_receipt_order_id().setText("Order ID: " + order_id);
//                                    receiptViewHolder.getTxt_receipt_phone().setText("Phone Number: " + phone);
//                                    receiptViewHolder.getTxt_receipt_pid().setText("Product ID: " + pid);
//                                    receiptViewHolder.getTxt_receipt_pname().setText("Product Name: " + pname);
//                                    receiptViewHolder.getTxt_receipt_quantity().setText("Quantity: " + quantity);
//                                    receiptViewHolder.getTxt_receipt_time().setText("Order Time: " + time);
//                                    receiptViewHolder.getTxt_receipt_total_amount().setText("Total Amount: $" + total_amount);
//                                    receiptViewHolder.getTxt_receipt_user_id().setText("User ID: " + user_id);
//                                    receiptViewHolder.getTxt_receipt_user_name().setText("Order User: " + user_name);
//
//
//                                }
//
//                                @NonNull
//                                @Override
//                                public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt, parent, false);
//                                    ReceiptViewHolder viewHolder = new ReceiptViewHolder(view);
//
//                                    return viewHolder;
//                                }
//                            };
//                            recyclerView.setAdapter(adapter);
//                            adapter.startListening();
//                        }
                    }
                    adapterReceipt = new AdapterReceipt(receiptList, getContext());
                    recyclerView.setAdapter(adapterReceipt);
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
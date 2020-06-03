package com.example.dessert_order_app.uiFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dessert_order_app.Modle.Cart;
import com.example.dessert_order_app.Modle.UserProfile;
import com.example.dessert_order_app.R;
import com.example.dessert_order_app.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment======";

    private static int totalPrice = 0;
    private String myId, pid, product_name, quantity;
    private RecyclerView recyclerView;
    private UserProfile userProfile = new UserProfile();
    private DatabaseReference databaseReference;
    private TextView txt_total_price, txt_alert_message;
    private Button btn_confirm;
    RelativeLayout cart_bottom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        txt_total_price = view.findViewById(R.id.txt_total_price);
        txt_alert_message = view.findViewById(R.id.txt_alert_message);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        cart_bottom = view.findViewById(R.id.cart_bottom);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        myId = currentUser.getUid();
        getUserData(myId);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new ConfirmOrderFragment();

                Bundle bundle = new Bundle();
                bundle.putString("total price", String.valueOf(totalPrice));
                bundle.putString("quantity", quantity);
                newFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;

    }


    private void getUserData(String myId) {
        databaseReference.child("Users").child(myId).addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onStart() {
        super.onStart();
        totalPrice= 0;


        final DatabaseReference itemListRef = FirebaseDatabase.getInstance().getReference().child("CartList").child(myId);

        itemListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    txt_alert_message.setVisibility(View.GONE);

                    FirebaseRecyclerOptions<Cart> option = new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(itemListRef.child("Products"), Cart.class)
                            .build();

                    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(option) {
                        @Override
                        protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                            pid = cart.getPid();
                            product_name = cart.getPname();
                            quantity = cart.getQuantity();

                            cartViewHolder.txt_item_name.setText(product_name);
                            cartViewHolder.txt_item_price.setText("$" + cart.getPrice());
                            cartViewHolder.txt_item_quantity.setText("" + cart.getQuantity());
                            int oneProductPrice = (Integer.parseInt(cart.getPrice())) * Integer.parseInt(cart.getQuantity());
                            totalPrice = totalPrice + oneProductPrice;

                            txt_total_price.setText("$" + totalPrice);
                            cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]{
                                            "Edit", "Remove"
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Cart Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                Fragment newFragment = new ProductDetailFragment();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("pid", cart.getPid());
                                                newFragment.setArguments(bundle);

                                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.fragment_container, newFragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }
                                            if (which == 1) {


                                                databaseReference.child("CartList").child(myId)
                                                        .child("Products").child(cart.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    showMessage("Item removed successfully.");

                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                    builder.show();
                                }
                            });

                        }

                        @NonNull
                        @Override
                        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_items, parent, false);
                            CartViewHolder holder = new CartViewHolder(view);


                            return holder;
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else {
                    cart_bottom.setVisibility(View.GONE);
                    txt_alert_message.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

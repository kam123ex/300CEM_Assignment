package com.example.dessert_order_app.uiFragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dessert_order_app.Modle.UserProfile;
import com.example.dessert_order_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ConfirmOrderFragment extends Fragment {
    private static final String TAG = "CartFragment======";

    View view;

    private EditText edt_user_name, edt_phone, edt_address;
    private String myId;
    private String pid = "";
    private String totalAmount = "";
    private UserProfile userProfile = new UserProfile();
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private double lat, lnt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        pid = getArguments().getString("pid");
        totalAmount = getArguments().getString("total price");
        showMessage("Total Amount: $" + totalAmount);

        Button btn_confirm_detail = view.findViewById(R.id.btn_confirm_detail);
        Button btn_user_location = view.findViewById(R.id.btn_user_location);
        edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_address = view.findViewById(R.id.edt_address);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        myId = currentUser.getUid();
        getUserData(myId);

        initLocation();


        btn_confirm_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

        btn_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLocation();
            }
        });
        return view;

    }

    private void initLocation() {
        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                lat = currentLocation.getLatitude();
                lnt = currentLocation.getLongitude();
            }
        };


    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }

    private void getUserLocation() {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList;
            String address = "";

            addressList = geocoder.getFromLocation(lat, lnt, 5);
            if (addressList != null) {
                address = addressList.get(0).getAddressLine(0);
                edt_address.setText(address);
            } else {
                showMessage("Location not found");
            }
            Log.d(TAG, address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Check() {
        if (TextUtils.isEmpty(edt_user_name.getText().toString())) {
            showMessage("Name is require");
        } else if (TextUtils.isEmpty(edt_phone.getText().toString())) {
            showMessage("Phone number is require");
        } else if (TextUtils.isEmpty(edt_address.getText().toString())) {
            showMessage("Delivery Address is require");

        } else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        final DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        final DatabaseReference productCountRef = FirebaseDatabase.getInstance().getReference().child("ProductCount");
            cartListRef.child(myId).child("Products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String saveCurrentTime, saveCurrentDate;
                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
                    saveCurrentDate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:s");
                    saveCurrentTime = currentTime.format(calendar.getTime());

                    String order_id = saveCurrentDate + saveCurrentTime;
                    final HashMap<String, Object> orderMap = new HashMap<>();

                    for (final DataSnapshot snap : dataSnapshot.getChildren()) {

                        totalAmount += Integer.parseInt(snap.child("quantity").getValue().toString()) * Integer.parseInt(snap.child("price").getValue().toString());
                        pid = snap.child("pid").getValue().toString();
                        orderMap.put("order_id", order_id);
                        orderMap.put("pid", snap.child("pid").getValue());
                        orderMap.put("user_name", edt_user_name.getText().toString());
                        orderMap.put("user_id", myId);
                        orderMap.put("phone", edt_phone.getText().toString());
                        orderMap.put("address", edt_address.getText().toString());
                        orderMap.put("pname", snap.child("pname").getValue());
                        orderMap.put("total_amount", totalAmount);
                        orderMap.put("quantity", snap.child("quantity").getValue());
                        orderMap.put("date", saveCurrentDate);
                        orderMap.put("time", saveCurrentTime);


                        orderListRef.child(myId).child(order_id).child(pid).updateChildren(orderMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("CartList")
                                        .child(myId)
                                        .child("Products")
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    showMessage("Your order has been confirm.");
                                                    Fragment newFragment = new CartFragment();
                                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.fragment_container, newFragment);
                                                    fragmentTransaction.commit();
                                                }
                                            }
                                        });
                            }
                        });

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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
    public void onResume() {
        super.onResume();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}

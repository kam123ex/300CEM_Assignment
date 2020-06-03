package com.example.dessert_order_app.Modle;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserData {
    String userId;
    UserProfile userProfile = new UserProfile();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public static UserProfile currentOnlineUser;

    public static final String UserId = "uid";
    public static final String UserPwd = "password";

    public FirebaseUserData(String userId) {
        this.userId = userId;

        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
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

    public void getUserData(){
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
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

    public UserProfile getUserProfile() {
        return userProfile;
    }
}

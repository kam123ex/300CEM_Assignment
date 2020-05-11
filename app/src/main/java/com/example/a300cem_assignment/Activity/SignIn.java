package com.example.a300cem_assignment.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a300cem_assignment.Modle.UserProfile;
import com.example.a300cem_assignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText edt_user_email, edt_user_password;
    Button btn_sign_in;
    TextView txt_sign_up;


    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edt_user_email = findViewById(R.id.edt_user_email);
        edt_user_password = findViewById(R.id.edt_user_password);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        txt_sign_up = findViewById(R.id.txt_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(signUpIntent);
                finish();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn(){

        final String email = edt_user_email.getText().toString().trim();
        final String password = edt_user_password.getText().toString().trim();

        if(email.isEmpty()){
            showMessage("Email is required!");
            edt_user_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showMessage("Please enter a valid email!");
            edt_user_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            showMessage("Password is required");
            edt_user_email.requestFocus();
            return;
        }
        showMessage("Connecting");

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser = firebaseAuth.getCurrentUser();
                    AllowAccount(email, password);

                    updateUI();
                }else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void AllowAccount(final String email, final String password){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final UserProfile userProfile = new UserProfile();
        databaseReference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
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

    private void updateUI(){
        Intent userIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(userIntent);
        finish();
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            updateUI();
        }
    }
}

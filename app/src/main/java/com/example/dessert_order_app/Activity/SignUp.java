package com.example.dessert_order_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dessert_order_app.Modle.UserProfile;
import com.example.dessert_order_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;


public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp=====";

    EditText edt_user_name, edt_user_email, edt_user_phone, edt_user_password, edt_user_password2;
    Button btn_user_icon, btn_sign_up;
    TextView txt_sign_in;
    ImageView image_user_icon;

    UserProfile userProfile = new UserProfile();
    Uri image_Uri;


    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_user_name = findViewById(R.id.edt_user_name);
        edt_user_email = findViewById(R.id.edt_user_email);
        edt_user_phone = findViewById(R.id.edt_user_phone);
        edt_user_password = findViewById(R.id.edt_user_password);
        edt_user_password2 = findViewById(R.id.edt_user_password2);

        btn_user_icon = findViewById(R.id.btn_user_icon);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        image_user_icon = findViewById(R.id.image_user_icon);
        txt_sign_in = findViewById(R.id.txt_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        getDefaultIcon();

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signIntent);
                finish();

            }
        });


        btn_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIcon();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    private void getDefaultIcon() {
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/icrmwithapp.appspot.com/o/default_user_icon.jpg?alt=media&token=9d9eb19f-6172-46d6-8484-827ce2d0744b").into(image_user_icon);

//        StorageReference storageReference = mStorageRef;
//
//        final StorageReference imageFilePath = storageReference.child("default_user_icon.jpg");
//        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(image_user_icon);
//            }
//        });


    }

    private void chooseIcon() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);

    }

    private void register() {
        final String name = edt_user_name.getText().toString();
        String email = edt_user_email.getText().toString();
        String phone = edt_user_phone.getText().toString();
        String password = edt_user_password.getText().toString();
        String password2 = edt_user_password2.getText().toString();
        String lead, createDate;

        if (TextUtils.isEmpty(name)) {
            showMessage("Name is required!.");
            edt_user_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showMessage("Email is required!");
            edt_user_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email!");
            edt_user_email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showMessage("Password is required!");
            edt_user_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            showMessage("Password length should be 6!");
            edt_user_password.requestFocus();
            return;
        }
        if (!password.equals(password2)) {
            showMessage("Please confirm your password");
            edt_user_password2.requestFocus();
            return;
        }


        userProfile.setName(name);
        userProfile.setEmail(email);
        userProfile.setPassword(password);
        userProfile.setPhone(phone);
//        String email, String password, String phone,

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Date date = new Date();
                            String StringDate = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
                            userProfile.setUid(user.getUid());
                            userProfile.setLead("No");
                            userProfile.setCreateDate(StringDate);
                            if (image_Uri != null) {
                                updateUriInfo(name, image_Uri, firebaseAuth.getCurrentUser());
                                showMessage("Account created");

                            } else {
                                updateNoUriInfo(name, firebaseAuth.getCurrentUser());
                                showMessage("Account created");

                            }


                        }
                    }
                });


    }

    private void updateNoUriInfo(final String name, final FirebaseUser currentUser){

        final StorageReference imageFilePath = mStorageRef.child("default_user_icon.jpg");


        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();

                userProfile.setImagePath(uri.toString());

                DatabaseReference dRef = firebaseDatabase.getReferenceFromUrl("https://icrmwithapp.firebaseio.com/Users");

                dRef.child(currentUser.getUid()).setValue(userProfile);

                currentUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showMessage("Register Complete");
                                    updateUI();
                                }
                            }
                        });


            }
        });
    }
    private void updateUriInfo(final String name, final Uri imageUri, final FirebaseUser currentUser) {

        StorageReference storageReference = mStorageRef.child("user_icons");
        final StorageReference imageFilePath = storageReference.child(imageUri.getLastPathSegment());


        imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Get image uri
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        userProfile.setImagePath(uri.toString());

                        DatabaseReference dRef = firebaseDatabase.getReferenceFromUrl("https://icrmwithapp.firebaseio.com/Users");

                        dRef.child(currentUser.getUid()).setValue(userProfile);

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });


                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent signInIntent = new Intent(getApplication(), SignIn.class);
        startActivity(signInIntent);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image_Uri = data.getData();
            Log.d(TAG, image_Uri.toString());
            image_user_icon.setImageURI(image_Uri);

        }
    }
}

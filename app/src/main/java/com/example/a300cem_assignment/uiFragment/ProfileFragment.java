package com.example.a300cem_assignment.uiFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a300cem_assignment.Activity.MainActivity;
import com.example.a300cem_assignment.Modle.UserProfile;
import com.example.a300cem_assignment.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment=========";

    private UserProfile userProfile = new UserProfile();
    private CircleImageView update_image;
    private EditText update_name, update_email, update_phone, update_password, now_password;
    private boolean isChecked;
    private Uri image_Uri;
    private String myUri;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        update_image = view.findViewById(R.id.circle_image_view);
        update_name = view.findViewById(R.id.edt_update_name);
        update_email = view.findViewById(R.id.edt_update_email);
        update_phone = view.findViewById(R.id.edt_update_phone);
        update_password = view.findViewById(R.id.edt_update_password);
        now_password = view.findViewById(R.id.edt_now_password);
        Button btn_new_photo = view.findViewById(R.id.btn_new_photo);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_clean = view.findViewById(R.id.btn_clean);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        view.setScrollContainer(true);
        String userId = currentUser.getUid();
        getUserData(userId);
        showUserInfo(update_image, update_name, update_email, update_phone);

        btn_new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = true;
                chooseIcon();

                showMessage(userProfile.getPassword());

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (now_password.getText().toString().equals(userProfile.getPassword())) {
                    if (isChecked) {
                        userInfoSaved();

                    } else {
                        updateSelectedUserInfo();
                    }
                } else {
                    showMessage(userProfile.getPassword());

                    showMessage("Please enter your password");
                    return;
                }


            }
        });


        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_name.getText().clear();
                update_email.getText().clear();
                update_phone.getText().clear();
                update_password.getText().clear();
                now_password.getText().clear();

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


    private void updateSelectedUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", update_name.getText().toString());
        userMap.put("email", update_email.getText().toString());
        userMap.put("phone", update_phone.getText().toString());
        if (!update_password.getText().toString().equals("")) {
            userMap.put("password", update_password.getText().toString());
        } else {
            userMap.put("password", userProfile.getPassword());
            currentUser.updatePassword(userProfile.getPassword());
        }
        ref.child(currentUser.getUid()).updateChildren(userMap);

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(update_name.getText().toString())
                .build();

        currentUser.updateProfile(profileUpdate);
        currentUser.updateEmail(update_email.toString());
        startActivity(new Intent(getActivity(), MainActivity.class));
        showMessage("Profile Info update successfully.");


        showMessage("Update is success");
    }


    private void userInfoSaved() {
        if (TextUtils.isEmpty(update_name.getText().toString())) {
            showMessage("Name is require");
        } else if (TextUtils.isEmpty(update_email.getText().toString())) {
            showMessage("Email is require");

        } else if (TextUtils.isEmpty(update_phone.getText().toString())) {
            showMessage("Phone is require");

        } else if (TextUtils.isEmpty(now_password.getText().toString())) {
            showMessage("password is require");

        } else if (isChecked) {
            uploadImage();

        }
    }


    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (image_Uri != null) {
            final StorageReference fileRef = storageReference.child("user_icons").child(image_Uri.getLastPathSegment());
            StorageTask uploadTask = fileRef.putFile(image_Uri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        myUri = downUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", update_name.getText().toString());
                        userMap.put("email", update_email.getText().toString());
                        userMap.put("phone", update_phone.getText().toString());
                        if (!update_password.getText().toString().equals("")) {
                            userMap.put("password", update_password.getText().toString());
                        } else {
                            userMap.put("password", userProfile.getPassword());

                        }
                        userMap.put("imagePath", myUri);

                        DatabaseReference infoRef = databaseReference.child("Users");
                        infoRef.child(currentUser.getUid()).updateChildren(userMap);

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(update_name.getText().toString())
                                .setPhotoUri(image_Uri)
                                .build();

                        currentUser.updateProfile(profileUpdate);
                        progressDialog.dismiss();
                        showMessage("Update is success");
                        startActivity(new Intent(getActivity(), MainActivity.class));


                    } else {

                        progressDialog.dismiss();
                        showMessage("Error");
                    }
                }
            });

        } else {
            showMessage("Image is not selected.");
        }
    }


    private void chooseIcon() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }


    private void showUserInfo(final CircleImageView update_image, final EditText update_name, final EditText update_email, final EditText update_phone) {
        DatabaseReference infoRef = databaseReference.child("Users");

        if (currentUser != null) {
            infoRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String image = dataSnapshot.child("imagePath").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();

                    Picasso.get().load(image).into(update_image);
                    update_name.setText(name);
                    update_email.setText(email);
                    update_phone.setText(phone);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void showMessage(String message) {

        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image_Uri = data.getData();
            Log.d(TAG, image_Uri.toString());
            update_image.setImageURI(image_Uri);

        } else {
            showMessage("Choose image again.");
        }
    }
}

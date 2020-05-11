package com.example.a300cem_assignment.uiFragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.a300cem_assignment.Activity.SignIn;
import com.example.a300cem_assignment.Modle.Blog;
import com.example.a300cem_assignment.Modle.Product;
import com.example.a300cem_assignment.Modle.UserProfile;
import com.example.a300cem_assignment.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class PostBlogFragment extends Fragment {
    private static final String TAG = "PostBlogFragment=========";

    Button btn_post_blog_camera, btn_post_blog_add_image, btn_post_blog_submit;
    EditText txt_post_blog_title, txt_product_detail_des;
    ImageView image_post_blog;
    Uri image_Uri;
    Blog blog = new Blog();
    private static final String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA = Manifest.permission.CAMERA;

    private boolean cameraPermissionsGranted = false;
    private static final int REQUEST_CODE = 10;

    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_blog, container, false);
        getPermission();

        image_post_blog = view.findViewById(R.id.image_post_blog);
        txt_post_blog_title = view.findViewById(R.id.txt_post_blog_title);
        txt_product_detail_des = view.findViewById(R.id.txt_product_detail_des);

        btn_post_blog_camera = view.findViewById(R.id.btn_post_blog_camera);
        btn_post_blog_add_image = view.findViewById(R.id.btn_post_blog_add_image);
        btn_post_blog_submit = view.findViewById(R.id.btn_post_blog_submit);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_post_blog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBlog();
                showMessage("Uploading...");
            }
        });

        btn_post_blog_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();

            }
        });
        btn_post_blog_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseIcon();

            }
        });

        return view;
    }

    private void uploadBlog() {
        String title = txt_post_blog_title.getText().toString();
        String description = txt_product_detail_des.getText().toString();
        String user_id = currentUser.getUid();


        if (TextUtils.isEmpty(title)) {
            showMessage("Please type in Title.");
            txt_post_blog_title.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            showMessage("Please type in Description.");
            txt_product_detail_des.requestFocus();
            return;
        }

        blog.setBlog_title(title);
        blog.setBlog_description(description);
        blog.setBlog_user_id(user_id);

        if (image_Uri != null) {
            updateBlog(user_id, image_Uri);
        }

    }

    private void updateBlog(final String user_id, Uri imageUri) {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");

        saveCurrentDate = currentDate.format(calendar.getTime());
        saveCurrentTime = currentTime.format(calendar.getTime());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = firebaseStorage.getReference();

        StorageReference storageReference = mStorageRef.child("Blog image");

        final StorageReference imagePath = storageReference.child(imageUri.getLastPathSegment());

        imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String time = saveCurrentDate + saveCurrentTime;
                        blog.setImage_path(uri.toString());
                        blog.setBlog_date(saveCurrentDate + ", " + saveCurrentTime);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Blog").child(user_id + time).setValue(blog);
                        updateUI();
                    }
                });


            }
        });


    }

    private void updateUI() {
        Fragment newFragment = new BlogFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);

    }

    private void chooseIcon() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    }


    private void getPermission() {
        Log.d(TAG, "Getting Permission");
        String[] permissions = {READ, WRITE, CAMERA};
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionsGranted = true;
            Log.d(TAG, "Permission is true");
        } else {
            Log.d(TAG, "Getting CAMERA Permission");
            requestPermissions(permissions, REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            cameraPermissionsGranted = false;
                            Log.d(TAG, "Permission Create");
                        }
                    }
                    Log.d(TAG, "Permission Failed");

                    cameraPermissionsGranted = true;
                }

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data.getExtras().get("data") != null) {
            Bitmap cpImage = (Bitmap) data.getExtras().get("data");
            Log.d(TAG, cpImage.toString());
            Uri tempUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), cpImage, null, null));
            image_Uri = tempUri;
            Log.d(TAG, image_Uri.toString());

            image_post_blog.setImageBitmap(cpImage);

        } else if (requestCode == 2) {

            image_Uri = data.getData();
            Log.d(TAG, image_Uri.toString());
            image_post_blog.setImageURI(image_Uri);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

//    private void handleUpload(Bitmap bitmap) {
//        image = new Image();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        getcurrenttime();
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("FoodImage");
//        final StorageReference reference = storageReference.child(restaurantNo).child(str + ".jpeg");
//        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                getDownloadUrl(reference);
//                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String url = uri.toString();
//                        image.setImage(url);
//                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference ref = database.getReference("Image");
//                        ref.child(restaurantNo).child(str).setValue(image);
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "onFailure: ", e.getCause());
//            }
//        });
//    }
//
//    private void getDownloadUrl(StorageReference reference) {
//        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d(TAG, "onSuccess: " + uri);
//            }
//        });
//    }
}

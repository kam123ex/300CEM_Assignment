package com.example.dessert_order_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dessert_order_app.Modle.UserProfile;
import com.example.dessert_order_app.uiFragment.MenuFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//https://www.youtube.com/watch?v=zYVEMCiDcmY
//https://www.youtube.com/watch?v=pFc59hCnbPQ&list=PLbte_tgDKVWQOCRIzkgEQ8umdn_S6ZnHr&index=2
public class test extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    Button btnSignIn;
    ImageView nav_user_icon;
    TextView nav_user_name, nav_user_email, phone;

    String username;
    String useremail;
    String phoneNum;

    private DrawerLayout drawer1;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    ArrayList<UserProfile> userProfileArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar1.setTitle("Order System");
        setSupportActionBar(toolbar1);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        drawer1 = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        View navbar = navigationView.findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer1, toolbar1,
                R.string.navigation_drawer_open1,
                R.string.navigation_drawer_close1);
        drawer1.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MenuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_menu);
            updateNavHeader();

        }



    }

    public void updateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
//        nav_user_icon = (ImageView) findViewById(R.id.nav_user_icon);
//        nav_user_name = (TextView) findViewById(R.id.nav_txt_user_name);
//        nav_user_email = (TextView) findViewById(R.id.nav_txt_user_email);
        loadUserInfor();


    }

    public void loadUserInfor() {
        if(firebaseAuth.getCurrentUser() != null){
            currentUser = firebaseAuth.getCurrentUser();
            if(currentUser.getPhotoUrl() != null){
                Glide.with(this).load(currentUser.getPhotoUrl()).into(nav_user_icon);
            }
            databaseReference = firebaseDatabase.getReference().child("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userProfileArrayList = new ArrayList<UserProfile>();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        UserProfile userProfile = data.getValue(UserProfile.class);
                        Log.d(TAG, data.getValue().toString());
                        if(currentUser.getUid().equals(userProfile.getUid())){
                            username = String.valueOf(userProfile.getName());
                            useremail = String.valueOf(userProfile.getEmail());
                            phoneNum = String.valueOf(userProfile.getPhone());
                            nav_user_name.setText(currentUser.getDisplayName());
                            nav_user_email.setText(currentUser.getEmail());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MenuFragment()).commit();
                break;
//            case R.id.nav_orders:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new CartFragment()).commit();
//                break;
//            case R.id.nav_receipt:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ReceiptFragment()).commit();
//                break;
//
//            case R.id.nav_favorite1:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new FavoriteFragment()).commit();
//                break;
//            case R.id.nav_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ProfileFragment()).commit();
//                break;
//            case R.id.nav_setting:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new SettingFragment()).commit();
//            case R.id.nav_sign_up:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new SignUpFragment()).commit();
//                break;


        }

        drawer1.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
            drawer1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}

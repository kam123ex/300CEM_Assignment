package com.example.dessert_order_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dessert_order_app.Modle.FirebaseUserData;
import com.example.dessert_order_app.uiFragment.BlogFragment;
import com.example.dessert_order_app.uiFragment.HomeFragment;
import com.example.dessert_order_app.R;
import com.example.dessert_order_app.uiFragment.LocationFragment;
import com.example.dessert_order_app.uiFragment.MenuFragment;
import com.example.dessert_order_app.uiFragment.CartFragment;
import com.example.dessert_order_app.uiFragment.ProfileFragment;
import com.example.dessert_order_app.uiFragment.ReceiptFragment;
import com.example.dessert_order_app.uiFragment.SettingFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

//https://drive.google.com/drive/folders/1B2EDHQ2viFfTnFKFacWWriw8X6taFb9w?fbclid=IwAR3iVbeoGBQIpboNsNoKsRbrm1wXY4ErR7nQBtT2yeIxSgifoPMxYkb-ovs
//https://www.youtube.com/watch?v=zYVEMCiDcmY
//https://www.youtube.com/watch?v=pFc59hCnbPQ&list=PLbte_tgDKVWQOCRIzkgEQ8umdn_S6ZnHr&index=2
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity======";

    TextView nav_user_name, nav_user_email;
    Button btn_sign_in;
    CircleImageView nav_user_icon;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View headerView;
    FirebaseUserData userData;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private Boolean locationPermissionsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar1 = findViewById(R.id.toolbar);
//        toolbar1.setTitle("Sweet System");
        setSupportActionBar(toolbar1);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        nav_user_name = headerView.findViewById(R.id.nav_user_name);
        nav_user_email = headerView.findViewById(R.id.nav_user_email);
        nav_user_icon = headerView.findViewById(R.id.nav_user_icon);
        btn_sign_in = headerView.findViewById(R.id.btnGoSignIn);


        if (firebaseAuth.getCurrentUser() == null) {

            hideItem();
        } else {

            currentUser = firebaseAuth.getCurrentUser();
            btn_sign_in.setVisibility(View.GONE);
            setHeaderInfo();
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1,
                R.string.navigation_drawer_open1,
                R.string.navigation_drawer_close1);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }


        navigationView.setNavigationItemSelectedListener(this);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

    }


    private void hideItem() {

        nav_user_name.setVisibility(View.INVISIBLE);
        nav_user_email.setVisibility(View.INVISIBLE);
        nav_user_icon.setVisibility(View.INVISIBLE);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_logOut).setVisible(false);

    }


    public void setHeaderInfo() {

        nav_user_name.setText(currentUser.getDisplayName());
        nav_user_email.setText(currentUser.getEmail());

        databaseReference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();

                nav_user_name.setText(name);
                nav_user_email.setText(email);
                String icon_path = dataSnapshot.child("imagePath").getValue().toString();
                Picasso.get().load(icon_path).into(nav_user_icon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }



    private void getPermission() {
        Log.d(TAG, "Getting Permission");
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
                Log.d(TAG, "Permission is true");
            } else {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "COURSE Permission");

            }

        } else {
            Log.d(TAG, "FINE Permission");

            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Request Permission");

        locationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionsGranted = false;
                            Log.d(TAG, "Permission Failed");

                            return;
                        }
                    }
                    Log.d(TAG, "Permission Create");
                    locationPermissionsGranted = true;
                }

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_setting) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SettingFragment()).commit();
        }

        if (currentUser == null) {

            showMessage("Please Sing In!");
        } else {

            switch (item.getItemId()) {

                case R.id.nav_menu:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MenuFragment()).commit();
                    break;

                case R.id.nav_orders:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new CartFragment()).commit();
                    break;

                case R.id.nav_receipt:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ReceiptFragment()).commit();
                    break;
                case R.id.nav_blog:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new BlogFragment()).commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                    break;

                case R.id.nav_location:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new LocationFragment()).commit();
                    break;

                case R.id.nav_logOut:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(logoutIntent);
                    finish();

            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }


}

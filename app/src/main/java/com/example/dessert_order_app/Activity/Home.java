package com.example.dessert_order_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dessert_order_app.uiFragment.HomeFragment;
import com.example.dessert_order_app.uiFragment.LocationFragment;
import com.example.dessert_order_app.uiFragment.MenuFragment;
import com.example.dessert_order_app.uiFragment.CartFragment;
import com.example.dessert_order_app.uiFragment.ProfileFragment;
import com.example.dessert_order_app.uiFragment.ReceiptFragment;
import com.example.dessert_order_app.uiFragment.SettingFragment;
import com.google.android.material.navigation.NavigationView;

import com.example.dessert_order_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    TextView txt_user_name, txt_user_email;
    ImageView image_user_icon;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);
        NavigationView navigationView = findViewById(R.id.nav_view_home);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open1,
                R.string.navigation_drawer_close1);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        navigationView.setNavigationItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        updateHeaderInfo();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void updateHeaderInfo() {
        NavigationView navigationView = findViewById(R.id.nav_view_home);
        View headerView = navigationView.getHeaderView(0);
        TextView nav_user_name = headerView.findViewById(R.id.nav_user_name);
        TextView nav_user_email = headerView.findViewById(R.id.nav_user_email);
        ImageView nav_user_icon = headerView.findViewById(R.id.nav_user_icon);

        nav_user_name.setText(currentUser.getDisplayName());
        nav_user_email.setText(currentUser.getEmail());

        //Get user icon
        if (currentUser.getPhotoUrl() == null) {
            return;
        } else {
            Glide.with(this).load(currentUser.getPhotoUrl()).into(nav_user_icon);

        }
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new MenuFragment()).commit();
                break;

            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new CartFragment()).commit();
                break;

            case R.id.nav_receipt:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new ReceiptFragment()).commit();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new ProfileFragment()).commit();
                break;

            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new SettingFragment()).commit();
                break;

            case R.id.nav_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        new LocationFragment()).commit();
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(logoutIntent);
                finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_home);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}

package com.example.dessert_order_app.uiFragment;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dessert_order_app.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "LocationFragment=========";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private static final float DEFAULT_ZOOM = 15;

    private GoogleMap mMap;
    private Boolean locationPermissionsGranted = false;
    private double mLatitude, mLongitude;
    private double storeLat = 22.279098, storeLnt = 114.185158;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ImageView image_gps = view.findViewById(R.id.image_gps);
        ImageView image_store = view.findViewById(R.id.image_store);

        getPermission();

        if (locationPermissionsGranted) {
            initMap();
        }

        image_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        image_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStoreLocation();
            }
        });
        return view;
    }

    private void getPermission() {
        Log.d(TAG, "Getting Permission");
        showMessage("Getting Permission");
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private void initMap() {
        Log.d(TAG, "Map is ready");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view);

        if (mapFragment == null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map_view, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        showMessage("Map is Initial");

    }


    private void getLocationDetail(double lat, double lnt) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList;
            String address = "";

            addressList = geocoder.getFromLocation(lat, lnt, 5);
            if (addressList != null) {
                address = addressList.get(0).getAddressLine(0);
                showMessage(address);
            }
            Log.d(TAG, address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getStoreLocation() {
        moveCamera(new LatLng(storeLat, storeLnt), DEFAULT_ZOOM);

    }

    private void getDeviceLocation() {

        Log.d(TAG, "DeviceLocation true");
        FusedLocationProviderClient fusedLocationProviderCLient = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            if (locationPermissionsGranted) {
                Task location = fusedLocationProviderCLient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "found location");
                            Location currentLocation = (Location) task.getResult();
                            mLatitude = currentLocation.getLatitude();
                            mLongitude = currentLocation.getLongitude();
                            moveCamera(new LatLng(mLatitude, mLongitude), DEFAULT_ZOOM);
                            Log.d(TAG, currentLocation.toString());

                        } else {
                            Log.d(TAG, "current location null");
                            showMessage("unable get current location");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "SecurityException" + e);

        }

    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG, "Move to Lat:" + latlng.latitude + ", Lng" + latlng.longitude);
        showMessage("The location is: " + latlng.latitude + ", " + latlng.longitude);
        getLocationDetail(latlng.latitude, latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

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
                    initMap();
                    Log.d(TAG, "Permission Create");
                    locationPermissionsGranted = true;
                }

            }
        }
    }


    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        showMessage("Map is ready");
        mMap = googleMap;

        LatLng storeLatLng = new LatLng(storeLat, storeLnt);
        mMap.addMarker(new MarkerOptions().position(storeLatLng).title("Store Location"));
        moveCamera(storeLatLng, DEFAULT_ZOOM);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

}

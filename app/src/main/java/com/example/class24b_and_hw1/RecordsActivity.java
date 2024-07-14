package com.example.class24b_and_hw1;
import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textview.MaterialTextView;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//
//import java.util.ArrayList;
//import java.util.List;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity implements RecordAdapter.OnItemClickListener, OnMapReadyCallback {


    private RecyclerView main_LST_records;
    private double latitude ;
    private double longitude;
    private GoogleMap myMap;
    private MaterialButton btn_back_to_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);

        findViews();
        initViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    void initViews(){
        btn_back_to_menu.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),InitialActivity.class);
            startActivity(i);
        });
        String json = MSP.getInstance().readString(Constants.SP_RECORDS_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Record>>() {
            }.getType();
            ArrayList<Record> records = new Gson().fromJson(json, type);

            RecordAdapter recordAdapter = new RecordAdapter(records,this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            main_LST_records.setLayoutManager(linearLayoutManager);
            main_LST_records.setAdapter(recordAdapter);

        }
    }

    void findViews(){
        main_LST_records = findViewById(R.id.main_LST_records);
        btn_back_to_menu = findViewById(R.id.btn_back_to_menu);
    }

    @Override
    public void onItemClick(Record record) {
        latitude =record.getLatitude();
        longitude = record.getLongitude();
        if (latitude != 0 && longitude!= 0) {
             updateMapLocation(latitude, longitude);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMapLocation(double latitude, double longitude) {
        if (myMap != null) {
            LatLng newLocation = new LatLng(latitude, longitude);
            myMap.clear();  // Clear existing markers
            myMap.addMarker(new MarkerOptions().position(newLocation).title("Player Location"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    myMap = googleMap;
        LatLng sydney = new LatLng(32.0853,34.7818);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}


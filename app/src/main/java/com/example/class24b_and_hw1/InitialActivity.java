package com.example.class24b_and_hw1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;


public class InitialActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private MaterialButton btn_start_game;
    private MaterialButton btn_speed_slow;
    private MaterialButton btn_speed_fast;
    private MaterialButton btn_buttons;
    private MaterialButton btn_phone_tilt;
    private MaterialTextView txt_speed_setting;
    private MaterialTextView txt_game_type_setting;
    private EditText input_enter_name;
    private int speedChoice;
    private int gameTypeChoice;
    private int deafultSpeed=1;
    private int deafultGameType=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);


        findViews();
        MSP.init(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        speedChoice=deafultSpeed;
        gameTypeChoice=deafultGameType;

        btn_speed_slow.setOnClickListener(view -> {
            speedChoice=1;
            txt_speed_setting.setText("current game speed: slow");
        });
        btn_speed_fast.setOnClickListener(view -> {
            speedChoice=2;
            txt_speed_setting.setText("current game speed: fast");});
        btn_buttons.setOnClickListener(view -> {
            gameTypeChoice=1;
            txt_game_type_setting.setText("current game type: buttons");
        });
        btn_phone_tilt.setOnClickListener(view -> {
            gameTypeChoice=2;
            txt_game_type_setting.setText("current game type: phone tilt");
        });

        initViews();
    }

    private void findViews(){
        btn_start_game = findViewById(R.id.btn_start_game);
        btn_speed_slow = findViewById(R.id.btn_speed_slow);
        btn_speed_fast = findViewById(R.id.btn_speed_fast);
        btn_phone_tilt = findViewById(R.id.btn_phone_tilt);
        btn_buttons = findViewById(R.id.btn_buttons);
        txt_speed_setting= findViewById(R.id.txt_speed_setting);
        txt_game_type_setting = findViewById(R.id.txt_game_type_setting);
        input_enter_name = findViewById(R.id.input_enter_name);
    }

    private void initViews(){

        btn_start_game.setOnClickListener((view)->{
            String inputText = input_enter_name.getText().toString();
            Intent i = new Intent(getApplicationContext(),PlayGameActivity.class);
            Bundle bundle = new Bundle();
            if(!inputText.isEmpty()){
                bundle.putString("key_player_name",inputText);
            }else{
                bundle.putString("key_player_name","anonymous player");
            }

            bundle.putInt(Constants.KEY_SPEED,speedChoice);
            bundle.putInt(Constants.KEY_GAME_TYPE,gameTypeChoice);
            bundle.putDouble(Constants.KEY_LATITUDE, lastKnownLocation.getLatitude());
            bundle.putDouble(Constants.KEY_LONGITUDE, lastKnownLocation.getLongitude());

            i.putExtras(bundle);

            startActivity(i);
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastKnownLocation() {
        try {
            Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            Log.d("RecordsActivity", "Location: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
                        }
                    } else {
                        Log.e("RecordsActivity", "Exception: %s", task.getException());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }



}
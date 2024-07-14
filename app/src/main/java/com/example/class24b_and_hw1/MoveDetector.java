package com.example.class24b_and_hw1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.class24b_and_hw1.Interfaces.MoveCallBack;

public class MoveDetector {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private int moveCountLeftX = 0;
    private int moveCountRightX = 0;
    private int moveCountY = 0;
    private long timeStamp = 0l;

    private MoveCallBack moveCallBack;

    public MoveDetector(Context context, MoveCallBack moveCallBack){
        this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
        this.moveCallBack=moveCallBack;
        initEventListener();
    }

    private void initEventListener(){
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[2];
                calculateMove(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //pass
            }
        };
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }
    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }

    private void calculateMove(float x, float y){
        if(System.currentTimeMillis()-timeStamp >500){
            timeStamp = System.currentTimeMillis();
            if (x >3.0){
                moveCountLeftX++;
                if(moveCallBack!=null){
                    moveCallBack.moveLeftX();
                }
            }
            if (x<-3.0){
                moveCountRightX++;
                if(moveCallBack!=null){
                    moveCallBack.moveRigthX();
                }
            }

            if (y >6.0 || y<-6.0){
                moveCountY++;
                if(moveCallBack!=null){
                    moveCallBack.moveY();
                }
            }
        }
    }

}

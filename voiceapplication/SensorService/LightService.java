package com.example.voiceapplication.SensorService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.voiceapplication.util.logUtil;

public class LightService implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor lightSensor;

    logUtil log = new logUtil("LightValue.txt");
    logUtil lowDataLog = new logUtil("LightValueRawData.csv");

    public LightService(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.getSensorList(Sensor.TYPE_ALL);
        this.lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void getLightValue(){
        if (lightSensor != null){
            sensorManager.registerListener(this,lightSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //Listenerを解除
    public void releaseRegister(){
        if (sensorManager != null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null){
            if (event.sensor.getType() == Sensor.TYPE_LIGHT){
                String str = "照度:" + event.values[0];
                System.out.println(str);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

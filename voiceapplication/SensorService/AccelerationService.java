package com.example.voiceapplication.SensorService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.voiceapplication.util.logUtil;

import java.util.ArrayList;
import java.util.Collections;

public class AccelerationService implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor accelerationSensor;
    logUtil log = new logUtil("Acceleration.txt");
    logUtil lowDataLog = new logUtil("AccelerationRawData.csv");

    public AccelerationService(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void getAccelerationValue(){
        if (accelerationSensor != null){
            sensorManager.registerListener(this,accelerationSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //Listenerを解除
    public void releaseRegister(){
        if (sensorManager != null){
            sensorManager.unregisterListener(this);
        }
    }

    long startTime = 0;
    int count = 0;
    float totalAccelerationValue = 0f;
    ArrayList<Float> accelerationArray = new ArrayList<Float>();

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorX;
        float sensorY;
        float sensorZ;
        if (event != null){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                sensorX = event.values[0];
                sensorY = event.values[1];
                sensorZ = event.values[2];
                System.out.println("X軸:" + sensorX + "/" + "Y軸:" + sensorY + "/" + "Z軸:" + sensorZ);
                lowDataLog.outputLog("X軸:" + sensorX + "/" + "Y軸:" + sensorY + "/" + "Z軸:" + sensorZ);

                long currentTime = System.currentTimeMillis();
                if (startTime == 0L){
                    startTime = currentTime;
                }

                if (currentTime - startTime >= 1000){
                    if (count < 30){
                        //Math.sqrtは平方根
                        Double absoluteVectorDouble = Math.sqrt(Double.parseDouble(String.valueOf(sensorX * sensorX + sensorY * sensorY + sensorZ * sensorZ)));
                        //小数点第3位を四捨五入
                        float absoluteVector = (float)(Math.round(absoluteVectorDouble * 100.0) / 100.0);
                        totalAccelerationValue += absoluteVector;
                        System.out.println("absoluteVector:" + absoluteVector);
                        System.out.println("totalAccelerationValue:" + totalAccelerationValue);
                        log.outputLog("absoluteVector:" + absoluteVector);
                        //absoluteVectorの最頻値がbasic_accelerator_valueに該当
                        accelerationArray.add(absoluteVector);

                        startTime = currentTime;
                        count++;
                    } else {
                        //平均値
                        float accelerationAverage = totalAccelerationValue / 30;
                        System.out.println("accelerationAverage:" + accelerationAverage);
                        log.outputLog("accelerationAverage:" + accelerationAverage);
                        //accelerator_difference_from_basicの計算式
                        //最頻値の抽出(最頻値は過去24時間分のデータを基に算出する必要がある)
                        float basicAcceleratorValue = getModeInArray();
                        //加速度の基礎値と平均値との差分(絶対値)
                        float acceleratorDifferenceFromBasic = Math.abs(accelerationAverage - basicAcceleratorValue);
                        System.out.println("basicAcceleratorValue:" + basicAcceleratorValue);
                        System.out.println("acceleratorDifferenceFromBasic:" + acceleratorDifferenceFromBasic);
                        log.outputLog("basicAcceleratorValue:" + basicAcceleratorValue);
                        log.outputLog("acceleratorDifferenceFromBasic:" + acceleratorDifferenceFromBasic);


                        totalAccelerationValue = 0f;
//                        sensorManager.unregisterListener(this, accelerationSensor);
                        startTime = 0L;
                        count = 0;
                    }
                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    float mode;
    float preMode;
    //最大出現数
    int maxNum;
    //出現回数
    int num;
    public float getModeInArray(){
        Collections.sort(accelerationArray);
        //初期値を代入
        mode = accelerationArray.get(0);
        maxNum = 1;
        preMode = accelerationArray.get(0);
        num = 1;


        for(int i = 1;i < accelerationArray.size();i++){
            if (preMode == accelerationArray.get(i)){
                //同じ値の場合出現回数に1を足す
                num++;
            }else {
                if (num > maxNum){
                    mode = preMode;
                    maxNum = num;
                }
                //出現する回数を数える値を変更
                preMode = accelerationArray.get(i);
                num = 1;
            }
        }

        if (num > maxNum) {
            mode = preMode;
            maxNum = num;
        }

        return mode;
    }
}

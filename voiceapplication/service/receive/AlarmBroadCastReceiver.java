package com.example.voiceapplication.service.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.voiceapplication.service.CustomService;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ServiceReceiver.setBroadCastIntent(context);
        Log.d("TestReceiver","AlarmBroadCastReceiver");
        Intent serviceIntent = new Intent(context, CustomService.class);
        context.startForegroundService(serviceIntent);
    }
}

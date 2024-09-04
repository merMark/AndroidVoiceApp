package com.example.voiceapplication.service.receive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.voiceapplication.MainActivity;
import com.example.voiceapplication.service.CustomService;
import com.example.voiceapplication.util.logUtil;

public class ServiceReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        logUtil log = new logUtil("deviceBoot.txt");
        if (intent != null){
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                Log.d("TestReceiver","ACTION_BOOT_COMPLETED");
                log.outputLog("BOOT_COMPLETED");
//                Intent serviceIntent = new Intent(context, AlarmBroadCastReceiver.class);
//                context.sendBroadcast(serviceIntent);
                try {

                    Log.d("TestReceiver","setBroadCastIntent");
                    log.outputLog("setBroadCastIntent");
                    setBroadCastIntent(context);
                }catch (Exception e){
                    Log.d("TestReceiver","intentFailed");
                }
                Intent startIntent = new Intent(context, MainActivity.class);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startIntent);

            }else {
                Log.d("TestReceiver","another");
            }
        }
    }

    public static void setBroadCastIntent(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,serviceIntent,0);
        long startTime = (System.currentTimeMillis() % (24 * 60 * 60 * 1000)) % (1 * 60 * 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + startTime, pendingIntent);
        }
    }

}

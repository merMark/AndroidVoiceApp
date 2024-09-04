package com.example.voiceapplication.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.voiceapplication.MainActivity;
import com.example.voiceapplication.SensorService.DisplayStatusService;
import com.example.voiceapplication.service.receive.ServiceReceiver;

public class CustomService extends IntentService {

    public CustomService(String name) {
        super(name);
    }

    public CustomService(){
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TestReceiver","CustomService");
        DisplayStatusService displayStatusService = new DisplayStatusService(getApplicationContext());
        displayStatusService.getDisplayStatus();
    }


    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("TestReceiver","onCreate");

        Context context = getApplicationContext();
        Resources rsrc = context.getResources();
        // タイトルを取得
        final String TITLE = "test";
        // 通知マネージャを生成
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知チャンネルを生成
        NotificationChannel channel =
                new NotificationChannel("1", TITLE, NotificationManager.IMPORTANCE_LOW);
        if(notificationManager != null) {
            // 通知バーをタップした時のIntentを作成
            Intent notifyIntent = new Intent(context, MainActivity.class);
            notifyIntent.putExtra("fromNotification", true);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            // サービス起動の通知を送信、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、-、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, "1")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentTitle(TITLE)
                    .setContentIntent(intent)
                    .build();
            // フォアグラウンドで実行
            startForeground(1, notification);
        }
        Toast.makeText(getBaseContext(),"CustomService",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}

package com.molidt.baby.touch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import com.molidt.baby.touch.data.db.DbManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * create by Jianan at 2019-03-11
 **/
public class BabyService extends Service {

    public static final String ACTION_TOUCH = "com.molidt.baby.touch.ACTION.TOUCH";
    public static final String ACTION_TOUCH_SAVE_SUCCESS = "com.molidt.baby.touch.ACTION_TOUCH_SAVE_SUCCESS";

    @Override
    public void onCreate() {
        super.onCreate();
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(getString(R.string.app_name), getString(R.string.app_name));
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        Intent intent = new Intent(this, BabyService.class);
        intent.setAction(ACTION_TOUCH);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        startForeground(1, builder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (service != null) {
            service.createNotificationChannel(chan);
        }
        return channelId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_TOUCH.equals(action)) {
            DbManager.get(this).addNewTouch();
            Intent successIntent = new Intent();
            successIntent.setAction(ACTION_TOUCH_SAVE_SUCCESS);
            sendBroadcast(successIntent);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}

package com.valdizz.penaltycheck.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.autoactivity.AutoActivity;

import java.util.List;

public class NotificationUtils extends ContextWrapper {

    public static final String CHECK_PENALTY_CHANNEL_ID = "com.valdizz.penaltycheck.PENALTYCHECK";
    public static final String CHECK_PENALTY_CHANNEL_NAME = "PENALTYCHECK_CHANNEL";
    public static final String CHECK_PENALTY_CHANNEL_DESCRIPTION = "Penalty check channel";
    public static final int NOTIFY_ID = 8;
    private NotificationManager notificationManager;

    public NotificationUtils(Context base) {
        super(base);
        createChannel();
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHECK_PENALTY_CHANNEL_ID, CHECK_PENALTY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHECK_PENALTY_CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel);
        }
    }

    public Notification getNotification(long count) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHECK_PENALTY_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(getString(R.string.dialog_penaltyfound, count))
                .setContentText(getString(R.string.dialog_numberofpenalties, count))
                .setTicker(getString(R.string.dialog_penaltyfound, count))
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.empty_car))
                .setLights(Color.RED, 0, 1)
                //.setVibrate(new long[]{500, 500, 500, 500, 500})
                //.setStyle()
                .setContentIntent(getContentIntent())
                .setAutoCancel(true);
        return builder.build();
    }

    private PendingIntent getContentIntent() {
        Intent notificationIntent = new Intent(this, AutoActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

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
import com.valdizz.penaltycheck.mvp.autoactivity.AutoActivity;

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
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1, 500, 1, 1000, 1, 500});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel);
        }
    }

    public Notification getNotification(long count) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHECK_PENALTY_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(getResources().getQuantityString(R.plurals.dialog_penaltyfound, (int)count, (int)count))
                .setContentText(getResources().getQuantityString(R.plurals.dialog_numberofpenalties, (int)count, (int)count))
                .setTicker(getResources().getQuantityString(R.plurals.dialog_penaltyfound, (int)count, (int)count))
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.empty_car))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getQuantityString(R.plurals.dialog_numberofpenalties, (int)count, (int)count)))
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

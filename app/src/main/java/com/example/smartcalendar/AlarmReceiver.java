package com.example.smartcalendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioPlay.playAudio(context);

        intent = new Intent();
        intent.setClass(context, ActivityAlarm.class); //Test is a dummy class name where to redirect
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("msg", (Serializable) mediaPlayer);
        context.startActivity(intent);


    }
}

/*  // This is the Notification Channel ID. More about this in the next section
    String NOTIFICATION_CHANNEL_ID = "channel_id";

    String CHANNEL_NAME = "Notification Channel";
    // Importance applicable to all the notifications in this Channel
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
//Boolean value to set if lights are enabled for Notifications from this Channel
        notificationChannel.enableLights(true);
                //Boolean value to set if vibration are enabled for Notifications from this Channel
                notificationChannel.enableVibration(true);
                //Sets the color of Notification Light
                notificationChannel.setLightColor(Color.GREEN);
                //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
                notificationChannel.setVibrationPattern(new long[] {
                500,
                500,
                500,
                500,
                500
                });
                //Sets whether notifications from these Channel should be visible on Lockscreen or not
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
                builder.setContentTitle("This is heading");
                builder.setContentText("This is description");

                builder.setSmallIcon(R.drawable.ic_alarm_black_24dp);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.alarm_icon1_background));
                Notification notification = builder.build();

                int NOTIFICATION_ID = 101;  //This is what will will issue the notification i.e.notification will be visible
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.notify(NOTIFICATION_ID, notification);
*/
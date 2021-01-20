package com.example.beekeeping;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.beekeeping.HiveInspectionView;
import com.example.beekeeping.R;

import java.util.Date;
import java.util.Locale;

public class NotificationIntentService extends JobIntentService {
    private String title;
    private String content;

    private static final int JOB_ID = 2;

    private String NOTIFICATION_CHANNEL_ID = "10001";

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null){
            title = extras.getString("title");
            content = extras.getString("content");
        }

        createNotificationChannel();
        //create intent to allow user to open app from notification
        Intent notifyIntent = new Intent(this, ApiaryView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, createID(), notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.hive)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(createID(), notification);
    }

    public static int createID(){
        Date currentDate = new Date();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.UK).format(currentDate));
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
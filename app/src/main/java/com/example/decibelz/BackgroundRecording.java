package com.example.decibelz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class BackgroundRecording extends Service {
    private static final int notificationId = 1;
    private static final String channelID = "Backgroundrecording";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create notification channel (needed for Android 8.0+)
        createNotificationChannel();

        // Build the notification
        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("Recorder")
                .setContentText("Recording Sound....")
                .build();

        // Start the service in the foreground
        startForeground(notificationId, notification);

        new Thread(()->{
            
        });

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // to nie dziala dla wersji nizszych niz 7.0
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't support binding
    }
}

package com.example.decibelz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.NotificationChannel;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

public class BackgroundRecording extends Service {
    private static final int notificationId = 1;
    private static final String channelID = "Backgroundrecording";

    //variables for audio
    public double dBFS;
    public double offset;
    //



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

        new Thread(() -> {
            int BufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, BufferSize);
            short[] bufferStorage = new short[BufferSize];
            audioRecord.startRecording();

            while (true) {

                audioRecord.read(bufferStorage, 0, BufferSize);

                double sum = 0;
                for (short sample : bufferStorage) {
                    double normSample = sample / 32768.0;
                    sum += normSample * normSample;
                }
                double rms = Math.sqrt(sum / bufferStorage.length);

                dBFS = 20 * Math.log10(rms);
                double dBSPL = dBFS + offset;

                System.out.println(dBSPL);
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();

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

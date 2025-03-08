package com.example.decibelz;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.CircularProgressIndicator;


public class MainActivity extends AppCompatActivity {

    SharedPreferences offsetStorage;
    public int getOffset() {
        return offsetStorage.getInt("offset", 100);
    }


    /////////////////handles permisions
    public boolean AllowRecording = false;

    private void requestAccessToRecording() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int id, String[] permissions, int[] grant) {
        super.onRequestPermissionsResult(id, permissions, grant);

        if (id == 1 && grant[0] == PackageManager.PERMISSION_GRANTED ) {
            AllowRecording = true;
        } else if (id == 1) {
            Toast.makeText(this,"This app needs to record audio", Toast.LENGTH_SHORT).show();
        }
    }
////////////////////////////////
    public Boolean records;
    SharedPreferences RecordBoolStorage;
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor recordbooleditor = RecordBoolStorage.edit();
        recordbooleditor.putBoolean("RecordVar", records);
        recordbooleditor.apply();
        System.out.println("saved recordstate");
    }


    public boolean getRecords() {
        return RecordBoolStorage.getBoolean("RecordVar", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //////////////////////////////
        ImageButton startButton = findViewById(R.id.startButton);
        Intent serviceIntent = new Intent(this, BackgroundRecording.class);
        // Zaczyna foregroundserice
        RecordBoolStorage = getSharedPreferences("RecordBoolStorage", MODE_PRIVATE);
        records = getRecords();





        if (records==false){
            startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play2));
        } else {
            startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause));
        }
        startButton.setOnClickListener(view -> {
            if (records==false) {
                records = true;
                startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause));
                System.out.println("StartsService");
                if (AllowRecording == true) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }
                } else {
                    Toast.makeText(this,"This app wont work if you dont allow to record", Toast.LENGTH_SHORT).show();
                }
            } else if (records){
                records = false;
                startButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play2));
                System.out.println("stopping service");
                stopService(serviceIntent);


            }

        });
        ///////////////////////////////

        ///////////////////////////////
        //checks for permision

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            AllowRecording = true;
        } else {
            requestAccessToRecording();
        }
        ////////////////////////////////
        //gets data from livedata and posts it to textview
        TextView a = findViewById(R.id.testView);
        CircularProgressIndicator progressIndicator = findViewById(R.id.circularProgressIndicator);
        offsetStorage = getSharedPreferences("offsetStorage", MODE_PRIVATE);

        int offset = getOffset();
        LiveData.get().getData().observe(this, dBFS -> {
            a.setText(String.valueOf(dBFS + offset + 57));
            progressIndicator.setProgress(dBFS + offset + 57);
        });

        ///////
        //moving to other pages
        ImageButton b = findViewById(R.id.settingsMove);
        b.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Settings_page.class);
            startActivity(intent);
        });
    }
}
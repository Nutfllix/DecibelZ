package com.example.decibelz;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
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
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);
        Intent serviceIntent = new Intent(this, BackgroundRecording.class);
        // Zaczyna foregroundserice
        startButton.setOnClickListener(view -> {
            if (AllowRecording == true) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }
            } else {
                Toast.makeText(this,"This app wont work if you dont allow to record", Toast.LENGTH_SHORT).show();
            }
        });

        //zatrzymuje
       stopButton.setOnClickListener(view -> {
            stopService(serviceIntent);
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

        offsetStorage = getSharedPreferences("offsetStorage", MODE_PRIVATE);

        int offset = getOffset();

        LiveData.get().getData().observe(this, dBFS -> {
            a.setText(String.valueOf(dBFS + offset + 57));
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
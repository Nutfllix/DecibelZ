package com.example.decibelz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Settings_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        Button c = findViewById(R.id.buttontomain);
        c.setOnClickListener(v -> {
            Intent intent = new Intent(Settings_page.this, MainActivity.class);
            startActivity(intent);
        });
    }
}

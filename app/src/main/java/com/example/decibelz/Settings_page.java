package com.example.decibelz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings_page extends AppCompatActivity {

    SharedPreferences offsetStorage;

    public static int offset;
    public void updateOffset() {
        SharedPreferences.Editor editor = offsetStorage.edit();
        editor.putInt("offset", offset);
        editor.apply();
    }

    public int getOffset() {
        return offsetStorage.getInt("offset", 100);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        ImageButton c = findViewById(R.id.buttontomain);
        c.setOnClickListener(v -> {
            Intent intent = new Intent(Settings_page.this, MainActivity.class);
            startActivity(intent);
        });

        SeekBar seekBar = findViewById(R.id.seekBar2);
        Button kalibruj = findViewById(R.id.calibrate);

        offsetStorage = getSharedPreferences("offsetStorage", MODE_PRIVATE);
        offset = getOffset();
        seekBar.setProgress(offset);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                offset = seekBar.getProgress();
                System.out.println("changed");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateOffset();
                System.out.println("stoped");
            }
        });

        TextView d = findViewById(R.id.testView2);
        LiveData.get().getData().observe(this, dBFS -> {
            d.setText(String.valueOf(dBFS + offset + 57));
        });
    }
}

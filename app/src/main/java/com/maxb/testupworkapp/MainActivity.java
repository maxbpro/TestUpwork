package com.maxb.testupworkapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maxb.testupworkapp.service.NotificationForegroundService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.txtTitle);
        textView.setText(BuildConfig.BUILD_TYPE);

        Intent intent = NotificationForegroundService.newIntent(this);
        intent.setAction("start");
        startService(intent);

        Button btnStop = findViewById(R.id.btnStop);

        btnStop.setOnClickListener(view -> {

            Intent intent1 = new Intent(getApplicationContext(), NotificationForegroundService.class);
            intent1.setAction("stop");
            startService(intent1);
        });
    }
}

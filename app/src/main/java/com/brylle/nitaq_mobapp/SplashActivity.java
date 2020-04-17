package com.brylle.nitaq_mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    int loadTime = 1000;        // time to display splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler for splash screen display duration
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do following after specified delay
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, loadTime);
    }
}

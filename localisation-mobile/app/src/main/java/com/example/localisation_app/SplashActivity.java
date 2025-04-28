package com.example.localisation_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Splash screen display time in milliseconds
    private static final long SPLASH_DISPLAY_TIME = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Use Handler to delay the start of MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Start MainActivity after the splash screen times out
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish(); // Close the splash activity so it can't be returned to
        }, SPLASH_DISPLAY_TIME);
    }
}
package com.example.engspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.engspace.login.Welcome;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 6000;
    ImageView quote, bg_main;
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        quote = findViewById(R.id.engspace_quote);

        lottieAnimationView = findViewById(R.id.astronaut_anim);

        bg_main = findViewById(R.id.bg);

        quote.animate().translationY(-250).setDuration(250).setStartDelay(100);

        lottieAnimationView.animate().translationY(-1000).setDuration(1000).setStartDelay(5000);

        bg_main.animate().translationY(-2500).setDuration(750).setStartDelay(5000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String access_token = sharedPreferences.getString("access", "");

                if (access_token != null && !access_token.equals("")) {
                    i = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    i = new Intent(SplashScreen.this, Welcome.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
package com.revno.greenleafz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity{
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splash.this,
                        MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}

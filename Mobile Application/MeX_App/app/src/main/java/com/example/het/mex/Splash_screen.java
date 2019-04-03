package com.example.het.mex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_screen extends AppCompatActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash_screen.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Splash_screen.this.startActivity(mainIntent);
                Splash_screen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    }


package p14141535.superslidepenguin.Controller;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import p14141535.superslidepenguin.R;

public class SplashActivity extends AppCompatActivity {
    private static int SplashWaitTime = 3000; //States how long the splash screen is on for.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Delays the screen for the splash wait time then runs the run function
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Opens the main activity
                Intent MainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(MainIntent);
                //If the main activity is backed out the game closes.
                finish();
            }

        }, SplashWaitTime);

    }
}

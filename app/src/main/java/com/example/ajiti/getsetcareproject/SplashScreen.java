package com.example.ajiti.getsetcareproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;


/**
 * Created by KH1748 on 30-Mar-16.
 */
public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ctrlActivityIndicator);
        if(progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }

        Thread timerThread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

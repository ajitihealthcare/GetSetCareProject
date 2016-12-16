package com.example.ajiti.getsetcareproject;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.R.attr.id;
import static com.example.ajiti.getsetcareproject.R.id.toolbar;

/**
 * Created by Ajiti on 12/13/2016.
 */

public class AccountRoomActivity extends AppCompatActivity {
    TextView  textView;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountroom);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        textView = (TextView) findViewById(R.id.textView2);
        if(toolbar != null){
            toolbar.setTitle(R.string.title_accountroom);
        }
        ImageView right=(ImageView)findViewById(R.id.rightImage);
        right.setImageResource(R.drawable.ic_network);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}







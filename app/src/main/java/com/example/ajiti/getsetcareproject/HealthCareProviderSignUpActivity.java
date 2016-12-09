package com.example.ajiti.getsetcareproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by AJITI on 12/7/2016.
 */

public class HealthCareProviderSignUpActivity extends AppCompatActivity{
 private Toolbar toolbar;
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_healthcareprovider_signup);
          toolbar = (Toolbar) findViewById(R.id.toolbar);
          if(toolbar != null){
              setSupportActionBar(toolbar);
          }



      }
}

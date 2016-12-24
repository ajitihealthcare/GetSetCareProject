package com.example.ajiti.getsetcareproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AJITI on 12/19/2016.
 */

public class HealthCareProviderSignUpSendOTP extends AppCompatActivity{
    private AutoCompleteTextView countryCode_field;
    private EditText mobileNo_field;
    private Button  sendOtpButton;
    private String[] countriesCodeList;
    private List<String> countryCodeList;
    private ArrayAdapter<String> countryCodeAdapter;
    private String countryCode, mobileNo;
    private String smsURL = ServerUtil.serverUrl+"rest/otp/sms";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);

        countryCode_field = (AutoCompleteTextView) findViewById(R.id.countryCodeField);
        mobileNo_field = (EditText) findViewById(R.id.phoneNumberField);
        sendOtpButton = (Button) findViewById(R.id.sendOtpButton);

        countriesCodeList = getResources().getStringArray(R.array.array_countryCodes);
        countryCodeList = Arrays.asList(countriesCodeList);
        countryCodeAdapter = new ArrayAdapter<>(this, R.layout.drop_down_countries_states_districts_list, countryCodeList);
        countryCode_field.setAdapter(countryCodeAdapter);
        countryCode_field.getAdapter();
        countryCode_field.setThreshold(1);
        countryCode_field.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                countryCode_field.showDropDown();
                countryCode_field.requestFocus();
                return false;
            }
        });

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countryCode= countryCode_field.getText().toString();
                if (TextUtils.isEmpty(countryCode)) {
                    countryCode_field.requestFocus();
                    countryCode_field.setError("CountryCode is Required");
                    return;
                } else {
                    countryCode_field.setError(null);
                }
                mobileNo = mobileNo_field.getText().toString().trim();
                if (!isValidMobile(mobileNo)) {
                    mobileNo_field.requestFocus();
                    mobileNo_field.setError("Invalid Mobile Number");
                    return;
                } else {
                    mobileNo_field.setError(null);
                }
                sendOtp();
            }
        });

    }
    private void sendOtp()
    {
        Map<String, String> params = new HashMap<>();
        params.put("serverUrl", smsURL);
        params.put("countrycode",countryCode);
        params.put("mobileno", mobileNo);
        params.put("operationType", "get");
        AsyncWorkerNetwork worker = new AsyncWorkerNetwork(
                HealthCareProviderSignUpSendOTP.this, new ActionCallback() {
            public void onCallback(Map<String, String> result) {
            }
        },"Sending OTP");
        worker.execute(params);

    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}

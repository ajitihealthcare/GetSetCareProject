package com.example.ajiti.getsetcareproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ajiti.getsetcare.ProviderDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AJITI on 12/9/2016.
 */

public class LoginActivity extends AppCompatActivity {

    public static String currentLoggedInEmail;
    public static String currentLoggedInPassword;
    private static long back_pressed_time;
    private static long BACK_PRESS_TIME_GAP = 2000;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private CheckBox rememberme;
    private AutoCompleteTextView usernameText;
    private TextView passwordText;
    private DBUserAdapter dbUserAdapter;
    private ArrayList<String> usernameList;
    private int clearDrawable;
    private Button signupBtn,forgetAndOtp,submitButton;
    private ToggleButton toggleButton;
    private EditText emailText;
    private String email;
    private TextView otpforgotText;
    private String passwordField= "common";
    private Button passwordButton, otpButton,forgotPassword,resendOTP,healthcareProviderSignUp;
    private static LoginActivity ins;

   // private static String serverUrl = ServerUtil.serverUrl + "rest/login";
   //private static String loginSmsOtp = ServerUtil.serverUrl +"rest/otp/sms";
    private static String serverUrl;
    private static String loginSmsOtp;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        setContentView(R.layout.activity_login_details);

        ins = this;
        dbUserAdapter = new DBUserAdapter(this);
        try {
            dbUserAdapter.open();
            usernameList = dbUserAdapter.retrieveData();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        } finally {
            dbUserAdapter.close();
        }
        clearDrawable = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? R.drawable.ic_clear_white : R.drawable.ic_action_cancel;
        rememberme = (CheckBox) findViewById(R.id.rememberme);
        usernameText = ((AutoCompleteTextView) findViewById(R.id.editText));
        passwordButton = (Button) findViewById(R.id.forgotpasswordbutton);
        otpButton = (Button) findViewById(R.id.otpbutton);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);
        resendOTP = (Button) findViewById(R.id.resendOTP);
        usernameText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_24dp, 0, 0, 0);
        passwordText = ((EditText) findViewById(R.id.editText1));
        passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_image, 0, 0, 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.username_item, R.id.multi, usernameList);
        usernameText.setAdapter(adapter);
        usernameText.setThreshold(1);
        if (forgotPassword != null) {
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, ForgotAndOTP.class);
                    startActivity(intent);
                }
            });
        }
        if (passwordButton != null) {
            passwordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        passwordButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, null));
                        passwordButton.setTextColor(getResources().getColor(R.color.white, null));
                        otpButton.setBackgroundColor(getResources().getColor(R.color.white, null));
                        otpButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
                        otpButton.setBackground(getDrawable(R.drawable.edittext_border));
                    } else {
                        passwordButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        passwordButton.setTextColor(getResources().getColor(R.color.white));
                        otpButton.setBackgroundColor(getResources().getColor(R.color.white));
                        otpButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        otpButton.setBackground(getResources().getDrawable(R.drawable.edittext_border));
                    }
                    passwordText.setHint("Password");
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    forgotPassword.setVisibility(View.VISIBLE);
                    resendOTP.setVisibility(View.GONE);
                    passwordField = "common";


                }
            });
        }

        if (otpButton != null) {
            otpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        otpButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, null));
                        otpButton.setTextColor(getResources().getColor(R.color.white, null));
                        passwordButton.setBackgroundColor(getResources().getColor(R.color.white, null));
                        passwordButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
                        passwordButton.setBackground(getDrawable(R.drawable.edittext_border));
                    } else {
                        otpButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        otpButton.setTextColor(getResources().getColor(R.color.white));
                        passwordButton.setBackgroundColor(getResources().getColor(R.color.white));
                        passwordButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        passwordButton.setBackground(getResources().getDrawable(R.drawable.edittext_border));
                    }
                    passwordText.setHint("Enter OTP");
                    passwordText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    passwordField = "otp";
                    forgotPassword.setVisibility(View.GONE);
                    resendOTP.setVisibility(View.VISIBLE);
                    currentLoggedInEmail = usernameText.getText().toString().trim();
                    currentLoggedInEmail = currentLoggedInEmail.replace(" ", "");

                    if (currentLoggedInEmail.isEmpty()) {
                        usernameText.requestFocus();
                        usernameText.setError("Username cannot be empty");
                        return;
                    }
                    sendOtp();
                    initiateOTP();

                }
            });
        }
        passwordButton.performClick();
        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (usernameText.getText().length() > 0) {

                    usernameText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_24dp, 0, clearDrawable, 0);
                    currentLoggedInEmail = usernameText.getText().toString().trim();
                    dbUserAdapter = new DBUserAdapter(LoginActivity.this);
                    try {
                        dbUserAdapter.open();
                        currentLoggedInEmail = currentLoggedInEmail.replace(" ", "");
                        currentLoggedInPassword = dbUserAdapter.getPassword(currentLoggedInEmail);

                        if (currentLoggedInPassword != null && !currentLoggedInPassword.isEmpty()) {
                            passwordText.setText(currentLoggedInPassword);
                            rememberme.setChecked(true);
                        } else {
                            passwordText.setText("");
                            rememberme.setChecked(false);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.getLocalizedMessage(), e);
                    } finally {
                        dbUserAdapter.close();
                    }

                } else {
                    usernameText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_24dp, 0, 0, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (passwordText.getText().length() > 0) {
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_image, 0, clearDrawable, 0);
                } else {
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_image, 0, 0, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });
        usernameText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (usernameText.getCompoundDrawables()[2] != null) {
                        if (event.getX() >= (usernameText.getRight() - usernameText.getLeft() - usernameText.getCompoundDrawables()[2].getBounds().width() - usernameText.getPaddingRight())) {
                            usernameText.setText("");
                        }
                    }
                }
                return false;
            }
        });
        passwordText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (passwordText.getCompoundDrawables()[2] != null) {
                        if (event.getX() >= (passwordText.getRight() - passwordText.getLeft() - passwordText.getCompoundDrawables()[2].getBounds().width() - passwordText.getPaddingRight())) {
                            passwordText.setText("");
                        }
                    }
                }
                return false;
            }
        });

        final Button loginBtn = (Button) findViewById(R.id.loginButton);
        if (loginBtn != null) {
            loginBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            Thread timerThread = new Thread() {
                                public void run() {
                                    try {
                                        sleep(1000);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                    loginBtn.setAlpha(0.95F);
                                                }
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        Log.e(TAG, e.getLocalizedMessage(), e);
                                    }
                                }
                            };
                            timerThread.start();
                            return false;

                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                loginBtn.setAlpha(0.55F);
                            }
                            return false; // if you want to handle the touch event
                    }
                    return false;
                }
            });


            loginBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    currentLoggedInEmail = usernameText.getText().toString().trim();
                    currentLoggedInEmail = currentLoggedInEmail.replace(" ", "");
                    currentLoggedInPassword = passwordText.getText().toString().trim();

                    if (currentLoggedInEmail.isEmpty()) {
                        usernameText.requestFocus();
                        usernameText.setError("Username cannot be empty");
                        return;
                    }
                    if (currentLoggedInPassword.isEmpty()) {
                        passwordText.requestFocus();
                        passwordText.setError("Password cannot be empty");
                        return;
                    }

                    dbUserAdapter = new DBUserAdapter(getApplicationContext());
                    dbUserAdapter.open();

                    try {
                        dbUserAdapter.deleteLoginDetails(currentLoggedInEmail);
                        if (rememberme != null && rememberme.isChecked()) {
                            dbUserAdapter.AddUserDetails(currentLoggedInEmail, currentLoggedInPassword);
                        } else {
                            dbUserAdapter.AddUserDetails(currentLoggedInEmail, "");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getLocalizedMessage(), e);
                    } finally {
                        dbUserAdapter.close();
                    }

                    if (isNetworkConnectionAvailable()) {
                        doLogin();
                    }
                }
            });
        }

        healthcareProviderSignUp = (Button) findViewById(R.id.providerAccountButton);
        if (healthcareProviderSignUp != null) {
            healthcareProviderSignUp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            Thread timerThread = new Thread() {
                                public void run() {
                                    try {
                                        sleep(1000);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                                    healthcareProviderSignUp.setAlpha(0.95F);
                                                }
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        Log.e(TAG, e.getLocalizedMessage(), e);
                                    }
                                }
                            };
                            timerThread.start();
                            return false;

                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                healthcareProviderSignUp.setAlpha(0.55F);
                            }
                            return false; // if you want to handle the touch event
                    }
                    return false;
                }
            });
            healthcareProviderSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, HealthCareProviderSignUpActivity.class);
                    startActivity(intent);

                }
            });
        }

    }
        @SuppressWarnings({"deprecation", "unchecked"})
        private void doLogin() {
            Map<String, String> nameValues = new HashMap<>();
            nameValues.put("serverUrl", serverUrl);
            nameValues.put("loginType", passwordField);
            nameValues.put("username", currentLoggedInEmail);
            nameValues.put("password", currentLoggedInPassword);
            nameValues.put("operationType", "1");
            AsyncWorkerNetwork worker = new AsyncWorkerNetwork(
                    LoginActivity.this, new ActionCallback() {
                public void onCallback(Map<String, String> result) {
                    if (!result.isEmpty() && result.containsKey("result") && result.get("result") != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.get("result"));
                            ProviderDTO.userName = jsonObject.get("userName").toString();
                            ProviderDTO.id = jsonObject.get("id").toString();
                            ProviderDTO.memberType = jsonObject.get("memberType").toString();

                            if((boolean) jsonObject.get("resetPwd"))
                            {
                                Intent confirmDetails = new Intent(LoginActivity.this,ConfirmPasswordDetails.class);
                                confirmDetails.putExtra("usernameDetails", currentLoggedInEmail);
                                startActivity(confirmDetails);
                            }
                            else if ((boolean) jsonObject.get("otpVerified")) {
                                Intent accountroom = new Intent(
                                        getApplicationContext(),
                                        AccountRoomActivity.class);
                                startActivity(accountroom);
                            } else {
                                Intent otpverify = new Intent(
                                        getApplicationContext(),
                                        OtpVerifiedActivity.class);
                                startActivity(otpverify);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage(), e);
                        }

                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage(result.get("error"))
                                .show();
                    }
                }
            }, "Logging in");
            worker.execute(nameValues);
        }

        private void sendOtp()
        {
            Map<String, String> params = new HashMap<>();
            params.put("serverUrl", loginSmsOtp);
            params.put("id", currentLoggedInEmail);
            params.put("operationType", "16");
            AsyncWorkerNetwork worker = new AsyncWorkerNetwork(
                    getApplicationContext(), new ActionCallback() {
                public void onCallback(Map<String, String> result) {
                }
            },"Sending OTP");
            worker.execute(params);

        }

        public void updateTheTextView(final String t) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    passwordText.setText(t);

                }
            });
        }
        public static LoginActivity getInstance(){
            return ins;
        }
    private void initiateOTP() {

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_SMS}, 12345);
        }
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, 123456);
        }
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateOTP();

            }
        });

    }

    @Override
    public void onBackPressed() {

        if ((back_pressed_time + BACK_PRESS_TIME_GAP) > System.currentTimeMillis()) {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        back_pressed_time = System.currentTimeMillis();
    }
    private boolean isNetworkConnectionAvailable() {

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            new AlertDialog.Builder(LoginActivity.this).setTitle("Error")
                    .setMessage("Internet Connection is Required").show();
            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().stopSync();
    }

    @SuppressWarnings("deprecation")
    public void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().sync();
    }



}

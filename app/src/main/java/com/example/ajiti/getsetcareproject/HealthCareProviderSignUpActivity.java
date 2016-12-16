package com.example.ajiti.getsetcareproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by AJITI on 12/7/2016.
 */

public class HealthCareProviderSignUpActivity extends AppCompatActivity{

    private Toolbar toolbar;
    //private String verifyURL =ServerUtil.serverUrl+"rest/provider/verify";
    public String firstName = null;
    public String lastName = null;
    public String mobileNo = null;
    public String email = null;
    public String gender = null;
    public String dateOfBirth = null;
    public String country = null;
    public String state = null;
    public String userName = null;
    public String rePwd = null;
    public String password = null;
    private int datediff;
    private String isEmptyUserName;
    private Date selectedDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    EditText emailId_field ,firstName_field,lastName_field,gender_field , dob_field ,mobileNo_field,username_field,countryCode_field,password_field,repwd_field;
    AutoCompleteTextView country_field;
    private String [] list = new String[0];
    private String[] countriesList, statesList, districtsList;
    private List<String> countryList, stateList, districtList;
    private ArrayAdapter<String> countryAdapter;
    private int groupId;
    private ArrayList<String> code, countryData;
    private ArrayList<String> fCode, fCountry;
    private String correspondingCode;
    RadioGroup radiogroup;
    boolean isEmailVerified;
    boolean isMobileVerified;
    private String verifyURL;
    private String createURL;
    //private String verifyURL = ServerUtil.serverUrl+"rest/patient/verify";
    //private String createURL = ServerUtil.serverUrl+"rest/patient/create";
    @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_healthcareprovider_signup);
          toolbar = (Toolbar) findViewById(R.id.toolbar);
          if(toolbar != null){
              setSupportActionBar(toolbar);
          }
        firstName_field = (EditText) findViewById(R.id.firstName);
        lastName_field = (EditText) findViewById(R.id.lastName);
        dob_field = (EditText) findViewById(R.id.dob_Date);
        gender_field = (EditText) findViewById(R.id.genderStatus);
        country_field = (AutoCompleteTextView) findViewById(R.id.countrySelection);
        countryCode_field = (EditText) findViewById(R.id.countryCode);
        mobileNo_field = (EditText) findViewById(R.id.phoneNumber);
        emailId_field = (EditText) findViewById(R.id.emailIdDetails);
        username_field = (EditText) findViewById(R.id.username);
        password_field = (EditText) findViewById(R.id.password);
        repwd_field = (EditText) findViewById(R.id.retypePassword);

        Resources r = getResources();
        TypedArray countrieCodes = r.obtainTypedArray(R.array.countries);

        countryData = new ArrayList<String>();
        code = new ArrayList<String>();

        int cpt = countrieCodes.length();
        for (int i = 0; i < cpt; ++i) {
            int id = countrieCodes.getResourceId(i, 0);
            code.add(r.getStringArray(id)[0]);
            countryData.add(r.getStringArray(id)[1]);
        }

        countrieCodes.recycle();

        fCode = code;
        fCountry = countryData;

        list = getResources().getStringArray(R.array.array_gender);

        gender_field.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.requestFocus();
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(HealthCareProviderSignUpActivity.this);
                    alertdialogbuilder.setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            String selectedField = Arrays.asList(list).get(position);
                            gender_field.setText(selectedField);

                        }
                    });
                    AlertDialog dialog = alertdialogbuilder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });

        dob_field.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.requestFocus();
                    SetDate(v);
                    return true;
                }
                return false;
            }
        });

        countriesList = getResources().getStringArray(R.array.countries_list);
        countryList = Arrays.asList(countriesList);
        countryAdapter = new ArrayAdapter<>(this, R.layout.drop_down_countries_states_districts_list, countryList);
        country_field.setAdapter(countryAdapter);
        country_field.getAdapter();
        country_field.setThreshold(1);
        country_field.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                country_field.showDropDown();
                country_field.requestFocus();
                return false;
            }
        });
        country_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                String selectedCountry = (String) parent.getItemAtPosition(position);
                String countryselection = selectedCountry;
                int selectedPosition = fCountry.indexOf(countryselection);
                correspondingCode = fCode.get(selectedPosition);
                countryCode_field.setText(correspondingCode);
            }

        });

        mobileNo_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                country = country_field.getText().toString();
                if ((country.isEmpty() || "Select Country".equalsIgnoreCase(country)) && hasFocus) {
                    country_field.setError("Country is required");
                    country_field.requestFocus();

                }
                mobileNo = mobileNo_field.getText().toString().trim();

                if (!hasFocus) {

                    if (mobileNo.isEmpty()) {
                        mobileNo_field.setError("Mobile No is required");

                    } else if (!isValidMobile(mobileNo)) {
                        mobileNo_field.requestFocus();
                        mobileNo_field.setError("Invalid Mobile No");
                        return;

                    } else {
                        mobileNo_field.setError(null);
                    }

                    if (isValidMobile(mobileNo)) {
                        isMobileNoExists();
                    }
                }
            }
        });

        emailId_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emailId_field.hasFocus()) {
                    emailId_field.setFocusableInTouchMode(true);
                }
            }
        });

        emailId_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                isEmailVerified = false;

                email = emailId_field.getText().toString().trim();

                if (!hasFocus && isMobileVerified) {
                    if (email.isEmpty()) {
                        emailId_field.requestFocus();
                        emailId_field.setError("Email Id is required");
                        return;

                    } else if (!isValidEmail(email)) {
                        emailId_field.requestFocus();
                        emailId_field.setError("Invalid email address");
                        return;

                    } else {
                        emailId_field.setError(null);
                    }

                    if (isValidEmail(email)) {
                        isEmailExists();
                    }

                }

            }
        });
        username_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                userName = username_field.getText().toString().trim();

                if (!hasFocus && isEmailVerified) {

                    if (userName.isEmpty()) {
                        username_field.setError("username is required");

                    } else if (!isValidName(userName)) {
                        username_field.requestFocus();
                        //username_field.setError("Invalid username");
                        username_field.setError("Only characters and numbers are allowed");
                        return;

                    } else {
                        username_field.setError(null);
                    }

                    if (isValidName(userName)) {
                        isUserExists();
                    }
                }
            }
        });
        Button submit = (Button)findViewById(R.id.next);
        if (submit != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = emailId_field.getText().toString().trim();
                    if (!isValidEmail(email)) {
                        emailId_field.requestFocus();
                        emailId_field.setError("Invalid email address");
                        return;
                    } else {
                        emailId_field.setError(null);
                    }
                    firstName = firstName_field.getText().toString().trim();
                    if (TextUtils.isEmpty(firstName)) {
                        firstName_field.requestFocus();
                        firstName_field.setError("First Name is Required");
                        return;
                    } else if (!isValidName(firstName)) {
                        firstName_field.requestFocus();
                        firstName_field.setError("Only characters and space allowed");
                        return;
                    } else {
                        firstName_field.setError(null);
                    }

                    lastName = lastName_field.getText().toString().trim();
                    if (TextUtils.isEmpty(lastName)) {
                        lastName_field.requestFocus();
                        lastName_field.setError("Last Name is Required");
                        return;
                    } else if (!isValidName(lastName)) {
                        lastName_field.requestFocus();
                        lastName_field.setError("Only characters and space allowed");
                        return;
                    } else {
                        lastName_field.setError(null);
                    }

                    gender = gender_field.getText().toString();
                    if (TextUtils.isEmpty(gender) || "Select Gender".equalsIgnoreCase(gender)) {
                        gender_field.requestFocus();
                        gender_field.setError("Gender is Required");
                        return;
                    } else {
                        gender_field.setError(null);
                        if ("Decline To Mention".equalsIgnoreCase(gender)) {
                            gender = "Decline";
                        }
                    }

                    dateOfBirth = dob_field.getText().toString().trim();
                    try {
                        selectedDate = sdf.parse(dateOfBirth);
                        datediff = new Date().compareTo(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(dateOfBirth)) {
                        dob_field.requestFocus();
                        dob_field.setError("Date of Birth is Required");
                        return;
                    } else if (datediff < 0) {
                        dob_field.requestFocus();
                        dob_field.setError("Please select valid date.");
                        return;
                    } else {
                        dob_field.setError(null);
                    }

                    country = country_field.getText().toString();
                    if (TextUtils.isEmpty(country) || "Select Country".equalsIgnoreCase(country)) {
                        country_field.requestFocus();
                        country_field.setError("Country is Required");
                        return;
                    } else {
                        country_field.setError(null);
                    }

                    mobileNo = mobileNo_field.getText().toString().trim();
                    if (!isValidMobile(mobileNo)) {
                        mobileNo_field.requestFocus();
                        mobileNo_field.setError("Invalid Mobile Number");
                        return;
                    } else {
                        mobileNo_field.setError(null);
                    }
                    userName = username_field.getText().toString().trim();
                    if (TextUtils.isEmpty(userName)) {
                        username_field.requestFocus();
                        username_field.setError("User Name is Required");
                        return;
                    } else if (!isValidUserName(userName)) {
                        username_field.requestFocus();
                        username_field.setError("Only characters and numbers are allowed");
                        return;
                    } else {
                        username_field.setError(null);
                    }

                    password = password_field.getText().toString().trim();
                    if (TextUtils.isEmpty(password)) {
                        password_field.requestFocus();
                        password_field.setError("password is Required");
                        return;
                    } else {
                        password_field.setError(null);
                    }

                    rePwd = repwd_field.getText().toString().trim();
                    if (TextUtils.isEmpty(rePwd)) {
                        repwd_field.requestFocus();
                        repwd_field.setError("Repassword is Required");
                        return;
                    } else if (!isValidRePassword(password, rePwd)) {
                        repwd_field.requestFocus();
                        repwd_field.setError("Password should be same");
                        return;
                    } else {
                        repwd_field.setError(null);
                    }


                    //createAccount();

                }
            });
        }



    }
    private void createAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("serverUrl", createURL);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("dob", dateOfBirth);
        params.put("gender", gender);
        params.put("country", country);
        params.put("countryCode",correspondingCode);
        params.put("mobileNo", mobileNo);
        params.put("email", email);
        params.put("password", password);
        params.put("operationType", "signup");
        AsyncWorkerNetwork worker = new AsyncWorkerNetwork(
                this, new ActionCallback() {
            public void onCallback(Map<String, String> result) {
                if (!result.isEmpty()) {
                    if (result.get("result") != null || result.get("result") != "null") {
                        HashMap<String, Object> data = new Gson().fromJson(result.get("result"), HashMap.class);
                        if (data != null && data.get("created") != null && (Boolean) data.get("created")) {
                            AlertDialog.Builder builder = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(HealthCareProviderSignUpActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(HealthCareProviderSignUpActivity.this);
                            }
                            builder.setCancelable(false);
                            builder.setTitle("Success !!");
                            builder.setMessage("Patient Registration Successfully");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }

                            }).show();

                        }
                    }
                }
            }
        }, "creating personal account");
        worker.execute(params);
    }

    private void isEmailExists() {
        Map<String, String> nameValues = new HashMap<String, String>();
        nameValues.put("input",email);
        nameValues.put("type","email");
        nameValues.put("serverUrl", verifyURL);
        nameValues.put("operationType", "sendData");
        final Boolean isPatientExists[] = new Boolean[1];
        AsyncWorkerNetwork worker = new AsyncWorkerNetwork(this, new ActionCallback() {
            public void onCallback(Map<String, String> result) {
                try{
                    if (result.get("result") != null && !result.isEmpty() ) {
                        HashMap<String,Object> data = new Gson().fromJson(result.get("result"), HashMap.class);
                        if(data != null && (boolean)data.get("isPatientExists")){
                            isEmailVerified = false;
                            emailId_field.requestFocus();
                            emailId_field.setError("email id is already exists!!");
                        } else{
                            isEmailVerified = true;
                            password_field.performClick();
                        }
                    }
                }catch(Exception e){
                    //Log.e("com.ajiti.getsetclinic",e.getMessage());
                    Log.e("com.example.ajiti",e.getMessage());
                }

            }
        }, "Verifying health provider Email");
        worker.execute(nameValues);
    }
    private void isMobileNoExists() {
        Map<String, String> nameValues = new HashMap<String, String>();
        nameValues.put("input",mobileNo);
        nameValues.put("type","mobileNo");
        nameValues.put("serverUrl",verifyURL);
        nameValues.put("operationType", "sendData");
        final Boolean isPatientExists[] = new Boolean[1];
        AsyncWorkerNetwork worker = new AsyncWorkerNetwork(this, new ActionCallback() {
            public void onCallback(Map<String, String> result) {
                try{
                    if (result.get("result") != null && !result.isEmpty() ) {
                        HashMap<String,Object> data = new Gson().fromJson(result.get("result"), HashMap.class);
                        if(data != null && (boolean)data.get("isPatientExists")){
                            isMobileVerified = false;
                            mobileNo_field.requestFocus();
                            mobileNo_field.setError("mobile number is already exists!!");
                        }else{
                            isMobileVerified = true;
                        }
                    }
                }catch(Exception e){
                    //Log.e("com.ajiti.getsetclinic",e.getMessage());
                    Log.e("com.example.ajiti",e.getMessage());
                }

            }
        }, "Verifying health provider Mobile No");
        worker.execute(nameValues);
    }
    private void isUserExists() {
        Map<String, String> nameValues = new HashMap<String, String>();
        nameValues.put("name",userName);
        nameValues.put("verifyType","userName");
        nameValues.put("serverUrl", verifyURL);
        nameValues.put("operationType", "16");
        final Boolean isUserExists[] = new Boolean[1];
        AsyncWorkerNetwork worker = new AsyncWorkerNetwork(this, new ActionCallback() {
            public void onCallback(Map<String, String> result) {
                try{
                    if (result.get("result") != null && !result.isEmpty() ) {
                        HashMap<String,Object> data = new Gson().fromJson(result.get("result"), HashMap.class);
                        if(data != null && (boolean)data.get("isUserExists")){
                            username_field.requestFocus();
                            username_field.setError("user is already exists!!");
                        }
                    }
                }catch(Exception e){
                    Log.e("com.example.ajiti",e.getMessage());
                }

            }
        }, "Verifying health provider UserName");
        worker.execute(nameValues);
    }


    private boolean isValidEmail(CharSequence target) {
        return (target == null) ? Boolean.FALSE : android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidName(String name) {
        String nameRegEx = "^[A-Za-z]((\\s)?[A-Za-z])*$";
        return (name != null && name.matches(nameRegEx)) ? Boolean.TRUE : Boolean.FALSE;
    }
    private boolean isValidUserName(String userName) {
        String nameRegEx = "^[a-z0-9_.]{3,15}$";
        return (userName != null && userName.matches(nameRegEx)) ? Boolean.TRUE : Boolean.FALSE;
    }
    public boolean isValidRePassword(String password,String confirmPassword)
    {
        boolean pstatus = false;
        if (confirmPassword != null && password != null)
        {
            if (password.equals(confirmPassword))
            {
                pstatus = true;
            }
        }
        return pstatus;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Calendar userAge = new GregorianCalendar(arg1,arg2,arg3);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                dob_field.requestFocus();
                dob_field.setError("Age must be minimum 18 years");
                return;
            }
            String date = String.format(Locale.getDefault(), "%02d", arg0.getDayOfMonth()) + "-" + String.format(Locale.getDefault(), "%02d", (arg0.getMonth() + 1)) + "-" + arg0.getYear();
            dob_field.setText(date);
            dob_field.setError(null);

        }
    };

    private void SetDate(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new DatePickerDialog(this,android.R.style.Theme_Material_Light_Dialog_Alert,myDateListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }else{
            new DatePickerDialog(this,this.myDateListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        EditText et;

        switch (item.getGroupId()) {
            case 1 :
                et = dob_field;
                break;

            default :
                return super.onContextItemSelected(item);
        }

        if(et != null) {
            et.setText(title);

        }

        return true;
    }
}

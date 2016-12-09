package com.example.ajiti.getsetcareproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Ajiti on 10/9/2016.
 */
public class SMSReceiver extends BroadcastReceiver {

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "PLVSMS";


    // public static final String SMS_ORIGIN = "1234";
    public static final String OTP = "";


    private static final String TAG = SMSReceiver.class.getSimpleName();

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = "code:";
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.substring(3).toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    // verification code from smse
                    if(!message.contains("verification")){
                        return;
                    }
                    String verificationCode = getVerificationCode(message);
                    if(LoginActivity.getInstance()!= null){
                       LoginActivity.getInstance().updateTheTextView(verificationCode);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    } /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = "";
        int index = message.indexOf(OTP_DELIMITER);
        int indexToScan = index+OTP_DELIMITER.length();
        code = message.substring(indexToScan,indexToScan+4);
        return code;
    }


}

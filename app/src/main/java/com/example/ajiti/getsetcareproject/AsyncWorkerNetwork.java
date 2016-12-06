package com.example.ajiti.getsetcareproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.CookieManager;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AJITI on 12/6/2016.
 */

public class AsyncWorkerNetwork extends AsyncTask<Map<String,String>, String, Map<String,String>>
{

    ProgressDialog pd;
    ActionCallback func;
    private static final String COOKIES_HEADER = "Set-Cookie";
    private static final String COOKIE = "Cookie";

    //	private static final String SECRET_HEADER = "X-ACCESS-TOKEN";
    private CookieManager cookieManager = CookieManager.getInstance();

    public AsyncWorkerNetwork(Context con, ActionCallback callback, String loadingMessage) {
        // TODO Auto-generated constructor stub
        pd = new ProgressDialog(con);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //pd.setProgressDrawable(con.getResources().getDrawable(R.drawable.custom_progress_background, null));
        }
        pd.setMessage(loadingMessage + "...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        func = callback;
    }
    @Override
    protected void onPostExecute(Map<String,String> result) {
        // TODO Auto-generated method stub
        pd.dismiss();
        if(func != null)
        {
            try {
                func.onCallback(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(result);
    }

    @Override
    protected Map<String, String> doInBackground(Map<String, String>... maps) {
        URL url = null;
        HttpURLConnection conn = null;
        String cookie = null;
        PrintWriter out = null;
        Map<String,String> result = new HashMap<>();

        DataOutputStream dataOutputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        return null;
    }
}

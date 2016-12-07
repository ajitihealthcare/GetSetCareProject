package com.example.ajiti.getsetcareproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @SuppressWarnings("deprecation")
    @Override
    protected Map<String, String> doInBackground(Map<String, String>... params) {

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

        try {
            url = new URL(params[0].get("serverUrl"));
            params[0].remove("serverUrl");

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(70000);
            conn.setConnectTimeout(70000);
            conn.setDoInput(true);

            //cookie = cookieManager.getCookie(conn.getURL().toString());
            cookie = cookieManager.getCookie(conn.getURL().getHost());

            conn.setRequestProperty(COOKIE, cookie!=null ? URLDecoder.decode(cookie) : "");
//			conn.setRequestProperty(SECRET_HEADER, User.secretHeader);

            if("get".equals(params[0].get("operationType")))
                conn.setRequestMethod("GET");
            else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }

            if("login".equals(params[0].get("operationType"))) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				/*conn.setFixedLengthStreamingMode(getPostDataString(params[0]).getBytes().length);*/
                out = new PrintWriter(conn.getOutputStream());
                out.print(getPostDataString(params[0]));
            }
            else if("get".equals(params[0].get("operationType"))) {
                conn.setRequestProperty("Accept", "application/json");
            }else if("post".equals(params[0].get("operationType"))) {
                conn.setRequestProperty("Accept", "application/json");
            }
            else if("sendData".equals(params[0].get("operationType"))) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                String inputParams = new Gson().toJson(params[0]);
                JSONObject inputJson = new JSONObject(inputParams);
                inputJson.remove("operationType");
                out = new PrintWriter(conn.getOutputStream());
                out.print(inputJson);

            }
            else if ("signup".equals(params[0].get("operationType"))) {
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                File file = null;
                dataOutputStream = new DataOutputStream(conn.getOutputStream());
                if(params[0].get("inputfile") != null){
                    file =  new File(params[0].get("inputfile"));
                }
                if(file!=null) {
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"" + lineEnd);
                    //dataOutputStream.writeBytes("Content-Type: "+params[0].get("mime")+lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    FileInputStream fileInputStream = new FileInputStream(file);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    fileInputStream.close();
                    dataOutputStream.writeBytes(lineEnd);
                }
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                String inputParams = new Gson().toJson(params[0]);
                JSONObject inputJson = new JSONObject(inputParams);
                inputJson.remove("operationType");
                inputJson.remove("inputfile");
                dataOutputStream.writeBytes(inputJson.toString());
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            }
            else if ("savecaserecord".equals(params[0].get("operationType"))) {
                ArrayList<String> data = null;
                data = new Gson().fromJson(params[0].get("inputfile"), new TypeToken<ArrayList<String>>(){}.getType());
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //File file = new File(data.get(0));
                dataOutputStream = new DataOutputStream(conn.getOutputStream());
                File file = null;
                for(String filepath:data){
                    file = new File(filepath);
                    if(file!=null) {
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"files\";filename=\"" + file.getName() + "\"" + lineEnd);
                        //dataOutputStream.writeBytes("Content-Type: "+params[0].get("mime")+lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        FileInputStream fileInputStream = new FileInputStream(file);

                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dataOutputStream.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }
                        fileInputStream.close();
                        dataOutputStream.writeBytes(lineEnd);
                    }
                }

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                String deleFid = params[0].get("deleteFileIds");
                List<Long> deleList = null;
                if(deleFid != null) {
                    deleList = new Gson().fromJson(deleFid, new TypeToken<List<Long>>(){}.getType());
                    params[0].remove("deleteFileIds");
                }
                params[0].remove("operationType");
                params[0].remove("inputfile");

                Map<String, Object> hmap = new HashMap<>();
                hmap.putAll(params[0]);
                if(deleList != null) {
                    hmap.put("deleteFileIds", deleList);
                }
                String inputJson = new Gson().toJson(hmap);

/*				String inputParams = new Gson().toJson(params[0]);
				JSONObject inputJson = new JSONObject(inputParams);
				if(deleList != null) {
					inputJson.put("deleteFileIds", deleList);
				}*/
                dataOutputStream.writeBytes(inputJson);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            }


            if(out != null) {
                out.close();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                if(params[0].get("operationType") == "login"){
//					User.secretHeader = conn.getHeaderField(SECRET_HEADER);
                } else if(params[0].get("operationType") == "logout"){
                    //User.secretHeader = "";
                    //cookie=null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.removeAllCookies(null);
                    }
                    else cookieManager.removeAllCookie();
                }
                List<String> cookieList = conn.getHeaderFields().get(COOKIES_HEADER);
                boolean isLogout = false;
                if (cookieList != null) {
                    String multipleCookie = "";
                    for (String cookieTemp : cookieList){

                        if(cookieTemp != null && !cookieTemp.isEmpty()) {
                            multipleCookie = cookieTemp + ";" + multipleCookie;
                        }
                        if("logout=success".equalsIgnoreCase(cookieTemp)) {
                            cookieManager.removeAllCookie();
                            isLogout = true;
                            break;
                        }
                    }
                    //cookieManager.setCookie(conn.getURL().toString(), cookieTemp)
                    if(!multipleCookie.isEmpty() && !isLogout) {
                        cookieManager.setCookie(conn.getURL().getHost(), URLEncoder.encode(multipleCookie));
                    }


                }

                JsonParser jsonParser = new JsonParser();
				/*String s = "";
				String str = "";
				BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				while((str = bufferedReader.readLine()) != null){
					s = s+str;
				}
				String s2 = s;*/
                if(conn.getInputStream() != null){
                    JsonElement jsonElement = jsonParser.parse(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    if(jsonElement != null && !(jsonElement instanceof JsonNull)){
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if(jsonObject != null){
                            result.put("result", jsonObject.get("data").toString());
                            result.put("cookie",cookieManager.getCookie(conn.getURL().getHost()));
                        }
                    }
                }

            }
            else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                result.put("error", "Invalid Credentials!!!");
            }
            else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST && "5"!=params[0].get("operationType")){
                result.put("error", "No Records Found!!!");
            }
            else {
                result.put("error", "Problem while connecting to server!!!");
            }
        }
        catch (Exception e){
            result.put("error", "Problem while connecting to server!!!");
        }
        finally {
            conn.disconnect();
        }

        return result;

    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        try
        {
            pd.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        super.onPreExecute();
    }



}

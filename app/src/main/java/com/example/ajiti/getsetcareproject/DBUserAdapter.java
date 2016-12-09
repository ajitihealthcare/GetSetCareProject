package com.example.ajiti.getsetcareproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AJITI on 13-Jul-16.
 */
public class DBUserAdapter {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String TAG = DBUserAdapter.class.getSimpleName();
    private static final String DATABASE_NAME = "users.db";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ( " + KEY_USERNAME + " TEXT UNIQUE, " + KEY_PASSWORD + " TEXT)";

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBUserAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void close() throws SQLException {
        DBHelper.close();
    }

    public void AddUserDetails(String username, String password) throws SQLException {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        db.insert(DATABASE_TABLE, null, initialValues);
    }

    public void deleteLoginDetails(String username) throws SQLException {
        db.delete(DATABASE_TABLE, KEY_USERNAME + " = ?",
                new String[] {username});

    }

    public String[] getRememberMeDetails() throws Exception {
        String[] rememberMe = new String[2];
        Cursor res = db.rawQuery("Select * from " + DATABASE_TABLE , null);
        if( res != null && res.getCount()>0) {
            res.moveToFirst();
            String username = res.getString(res.getColumnIndex(KEY_USERNAME));
            String pass = res.getString(res.getColumnIndex(KEY_PASSWORD));
            rememberMe[0] = username;
            rememberMe[1] = pass;
        }
        return rememberMe;
    }

    public String getPassword(String username) throws Exception {
        String password = "";

        Cursor res=db.rawQuery("select " + KEY_PASSWORD + " from " + DATABASE_TABLE + " where " + KEY_USERNAME + "='" + username+"'", null);
        if(res !=null && res.getCount()>0){
            res.moveToFirst();
            password = res.getString(res.getColumnIndex(KEY_PASSWORD));
        }
        return password;
    }

    public ArrayList<String> retrieveData()throws Exception
    {
        ArrayList<String> results = new ArrayList<String>();
        Cursor res = db.rawQuery("Select * from " + DATABASE_TABLE , null);
        if(res != null){
            if(res.moveToFirst()){
                do{
                    String user= res.getString(res.getColumnIndex(KEY_USERNAME));
                    results.add(user);
                }while(res.moveToNext());
            }
        }
        return results;
    }
}

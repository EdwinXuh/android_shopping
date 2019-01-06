package com.xuh.shopping.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlHellper extends SQLiteOpenHelper {
    private static final String TAG = "TestSQLite";
    public SqlHellper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE userInfo(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT ,password TEXT )";
        String sql1 = "CREATE TABLE goods(id INTEGER PRIMARY KEY AUTOINCREMENT,info TEXT,price TEXT,counts TEXT,imgName Text)";
        String sql2 = "CREATE TABLE carts(id INTEGER PRIMARY KEY AUTOINCREMENT ,imgName Text,num TEXT,info TEXT,price TEXT,isActive INTEGER default(1))";
        String sql3 = "CREATE TABLE dingdans(id INTEGER PRIMARY KEY AUTOINCREMENT ,imgName Text,num TEXT,info TEXT,price TEXT,isActive INTEGER default(1))";
        Log.i(TAG,"Create database userinfo-----------");
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"Update database -----------");
    }
}

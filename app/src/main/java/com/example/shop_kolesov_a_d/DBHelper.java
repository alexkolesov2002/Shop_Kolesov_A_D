package com.example.shop_kolesov_a_d;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ShopKolesovDb";
    public static final String TABLE_GOODS= "goods";
    public static final String TABLE_USERS= "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAZVANIE = "nazvanie";
    public static final String KEY_PRICE = "price";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public DBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_GOODS + "(" + KEY_ID  + " integer primary key," + KEY_NAZVANIE + " text," + KEY_PRICE + " text" +")");
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID  + " integer primary key," + KEY_LOGIN + " text," + KEY_PASSWORD + " text" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GOODS);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }
}

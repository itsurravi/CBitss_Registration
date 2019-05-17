package com.ravisharma.messagetowhatsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    public static String DB_NAME = "CBITSS";
    public static String TB_NAME = "Info";
    public static String ID = "ID";
    public static String USER = "USER";
    public static String PASS = "PASS";

    private String names[] = {"Shikha",
            "Nancy",
            "Gurroop",
    };

    private String pas[] = {"shikha34",
            "nancy34",
            "roop34",
            };

    public static int DB_VERSION = 1;

    DB(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +TB_NAME
                +"(" +ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +USER+
                " VARCHAR, " +PASS+
                " VARCHAR);";
        db.execSQL(sql);
    }

    public void AddData() {
        for(int i=0;i<names.length;i++)
        {
            setData(names[i], pas[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public void setData(String data, String data2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER, data);
        cv.put(PASS, data2);

        db.insert(TB_NAME, null, cv);
    }

    public Cursor getPass(String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TB_NAME+" WHERE "+PASS+"='"+password+"'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
}

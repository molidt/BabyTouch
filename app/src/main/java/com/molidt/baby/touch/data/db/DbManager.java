package com.molidt.baby.touch.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.molidt.baby.touch.data.Touch;

import java.util.List;

/**
 * create by Jianan at 2019-03-11
 **/
public class DbManager extends SQLiteOpenHelper {

    private static DbManager dbManager;
    private TouchTable touchTable;

    public static DbManager get(Context context) {
        if (dbManager == null) {
            synchronized (DbManager.class) {
                if (dbManager == null) {
                    dbManager = new DbManager(context, 1);
                }
            }
        }
        return dbManager;
    }

    private DbManager(Context context, int version) {
        super(context, "Baby.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private TouchTable getTouchTable() {
        if (touchTable == null) {
            touchTable = new TouchTable(getWritableDatabase());
        }
        return touchTable;
    }

    public void addNewTouch() {
        Touch touch = new Touch();
        touch.setTime(System.currentTimeMillis());
        getTouchTable().addTouch(touch);
    }

    public List<Touch> getAllTouch() {
        return getTouchTable().getAllTouch();
    }
}

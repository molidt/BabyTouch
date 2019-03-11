package com.molidt.baby.touch.data.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * create by Jianan at 2019-03-11
 **/
public class TouchTable extends DbEntry {

    private static final String TABLE_TOUCH = "baby_touch";
    private static final String TOUCH_COL_TIME = "touch_time";
    private static final String SQL_CREATE_TABLE_TOUCH =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TOUCH + "("
                    + _ID + " INTEGER PRIMARY KEY asc AUTOINCREMENT NOT NULL, "
                    + TOUCH_COL_TIME + " INTEGER NOT NULL)";
    private static final String SQL_INSERT_TOUCH =
            "INSERT INTO " + TABLE_TOUCH + "(" + TOUCH_COL_TIME + ") VALUES(?)";

    public TouchTable(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TOUCH);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {

    }

    public void addTouch() {
        db.execSQL(SQL_INSERT_TOUCH, new Object[]{System.currentTimeMillis()});
    }
}

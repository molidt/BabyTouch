package com.molidt.baby.touch.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.molidt.baby.touch.data.Touch;
import com.molidt.baby.touch.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Jianan at 2019-03-11
 **/
public class TouchTable extends DbTable {

    private static final String TABLE_TOUCH = "baby_touch";
    private static final String TOUCH_TIME = "touch_time";
    private static final String TOUCH_STATE = "touch_state";
    private static final String SQL_CREATE_TABLE_TOUCH =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TOUCH + "("
                    + _ID + " INTEGER PRIMARY KEY asc AUTOINCREMENT NOT NULL, "
                    + TOUCH_TIME + " INTEGER NOT NULL, "
                    + TOUCH_STATE + " INTEGER DEFAULT 0" + ")";
    private static final String SQL_INSERT_TOUCH =
            "INSERT INTO " + TABLE_TOUCH + "("
                    + TOUCH_TIME + ", "
                    + TOUCH_STATE + ") VALUES(?, ?)";
    private static final String SQL_SELECT_ALL_TOUCH =
            "SELECT * FROM " + TABLE_TOUCH + " ORDER BY " + TOUCH_TIME + " DESC";

    public TouchTable(SQLiteDatabase db) {
        super(db);
        createTable(db);
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TOUCH);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {

    }

    void addTouch(Touch touch) {
        db.execSQL(SQL_INSERT_TOUCH, new Object[]{touch.getTime(), touch.getState()});
        LogUtil.d("touch save success:" + touch.getTime());
    }

    List<Touch> getAllTouch() {
        List<Touch> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_TOUCH, new String[]{});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Touch touch = new Touch();
                touch.setTime(getColumnLong(cursor, TOUCH_TIME));
                touch.setState(getColumnInt(cursor, TOUCH_STATE));
                resultList.add(touch);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return resultList;
    }
}

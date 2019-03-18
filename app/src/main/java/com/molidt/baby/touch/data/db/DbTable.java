package com.molidt.baby.touch.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * 数据库实体操作类
 * Created by Jianan on 2017/8/4.
 */
public abstract class DbTable implements BaseColumns {

    protected static int parseBoolToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    protected static boolean parseIntToBool(int intBool) {
        return intBool > 0;
    }

    protected static String getColumnString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getString(index);
        } else {
            return "";
        }
    }

    protected static int getColumnInt(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getInt(index);
        } else {
            return 0;
        }
    }

    protected static long getColumnLong(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getLong(index);
        } else {
            return 0;
        }
    }

    protected static boolean getColumnBool(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return index != -1 && !cursor.isNull(index) && cursor.getInt(index) > 0;
    }

    protected SQLiteDatabase db;

    public DbTable(SQLiteDatabase db) {
        this.db = db;
    }

    public abstract void createTable(SQLiteDatabase db);
    public abstract void dropTable(SQLiteDatabase db);
}

package com.mediapp.ft.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Frank on 3/27/2015.
 */
public final class SessionContract {

    public SessionContract() {}

    public static abstract class Session implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String COLUMN_NAME_TOKEN = "token";
    }

    // Database Information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MediApp.db";

    // SQL Params for functions
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // SQL Functions
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Session.TABLE_NAME + " (" + Session._ID + " INTEGER PRIMARY KEY," + Session.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Session.TABLE_NAME;

    public class SessionDbHelper extends SQLiteOpenHelper {

        public SessionDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}

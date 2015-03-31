package com.mediapp.ft.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Frank on 3/30/2015.
 */
public final class DatabaseContract {

    public static final String AUTHORITY = "com.mediapp.ft";
    public static final String SCHEME = "content://";
    public static final String SLASH = "/";
    public static final  String DATABASE_NAME      = "database.db";

    public static final  int    DATABASE_VERSION   = 1;
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    /* An array list of all the SQL create table statements */
    public static final String[] SQL_CREATE_TABLE_ARRAY = {
        Treatments.CREATE_TABLE
    };


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static final class Treatments implements BaseColumns {

        private Treatments(){}
        public static final String TABLE_NAME       = "Treatments";
        public static final String COLUMN_NAME_NAME= "name";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_FINISH = "finish";
        public static final String COLUMN_NAME_HOUR = "hour";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";
        public static final String COLUMN_NAME_PICTURE = "picture";

        /*
         * URI definitions
         */
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME);

        /**
         * The content URI base for a single row. An ID must be appended.
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + SLASH + TABLE_NAME + SLASH);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = COLUMN_NAME_HOUR + " ASC";

        /*
         * MIME type definitions
         */
        /**
         * The MIME type of {@link #CONTENT_URI} providing rows
         */
        public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.mediapp.treatments";

        /**
         * The MIME type of a {@link #CONTENT_URI} single row
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.mediapp.treatments";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_START + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_FINISH + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HOUR + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PICTURE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_FREQUENCY + "INTEGER" + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}

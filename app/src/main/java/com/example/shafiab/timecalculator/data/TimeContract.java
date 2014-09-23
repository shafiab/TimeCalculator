package com.example.shafiab.timecalculator.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shafiab on 9/11/14.
 */
// content://AUTHORITY/PATH/ID
// PATH can be 0 or more segment
// ID if optional
// here PATH  is either Time or Factor, depending on the fragment used


public class TimeContract {

    public static final String AUTHORITY = "com.example.shafiab.timecalculator";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://"+AUTHORITY);

    public static final String TIME_TABLE = "time";
    public static final String FACTOR_TABLE = "factor";

    //public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE;

    // Uri for time
    //public static final Uri TIME_URI = Uri.withAppendedPath(CONTENT_URI, TIME_TABLE);

    // Uri for factor
    //public static final Uri FACTOR_URI = Uri.withAppendedPath(CONTENT_URI, FACTOR_TABLE);

    public static final class TimeEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TIME_TABLE);
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + TIME_TABLE;

        public static final String TABLE_NAME = "timeDb";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_HR = "hr";
        public static final String COLUMN_MIN = "min";
        public static final String COLUMN_SEC = "sec";
        public static final String COLUMN_isMIN = "isMinus";

        public static Uri buildTimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FactorEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, FACTOR_TABLE);
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "/" + FACTOR_TABLE;


        public static final String TABLE_NAME = "factorDb";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FACTOR = "factor";
    }





}

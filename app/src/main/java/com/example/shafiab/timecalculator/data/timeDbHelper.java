package com.example.shafiab.timecalculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shafiab.timecalculator.data.TimeContract.TimeEntry;
import com.example.shafiab.timecalculator.data.TimeContract.FactorEntry;

/**
 * Created by shafiab on 9/14/14.
 */
public class timeDbHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "myData.db";


    public timeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteOpenHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version)

        final String DATABASE_CREATE_TIME = "create table "
                + TimeEntry.TABLE_NAME + " ( " + TimeEntry.COLUMN_ID
                + " integer primary key autoincrement, "
                +  TimeEntry.COLUMN_HR + " real not null, "
                + TimeEntry.COLUMN_MIN + " real not null, "
                + TimeEntry.COLUMN_SEC + " real not null, "
                + TimeEntry.COLUMN_isMIN + " INTEGER not null); ";

        db = getWritableDatabase();
        //db.execSQL(DATABASE_CREATE_TIME);



    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String DATABASE_CREATE_TIME = "create table "
                + TimeEntry.TABLE_NAME + " ( " + TimeEntry.COLUMN_ID
                + " integer primary key autoincrement, "
                +  TimeEntry.COLUMN_HR + " real not null, "
                + TimeEntry.COLUMN_MIN + " real not null, "
                + TimeEntry.COLUMN_SEC + " real not null, "
                + TimeEntry.COLUMN_isMIN + " INTEGER not null); ";

        final String DATABASE_CREATE_FACTOR = "create table "
                + FactorEntry.TABLE_NAME + " ( " + FactorEntry.COLUMN_ID
                + " integer primary key autoincrement, "
                +  FactorEntry.COLUMN_FACTOR + " real not null ); ";

        sqLiteDatabase.execSQL(DATABASE_CREATE_TIME);
        sqLiteDatabase.execSQL(DATABASE_CREATE_FACTOR);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TimeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FactorEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

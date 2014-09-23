package com.example.shafiab.timecalculator.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class TimeContentProvider extends ContentProvider
{
    // Data storage
    timeDbHelper mDbHelper;


    // helper constants for use with the UriMatcher
    private static final int TIME_DATA = 1;
    private static final int FACTOR_DATA = 2;

    // URI matcher
    public static UriMatcher URI_MATCHER = buildUriMatcher();

    //Uri matcher builder
    static UriMatcher buildUriMatcher()
    {
        UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(TimeContract.AUTHORITY, TimeContract.TIME_TABLE, TIME_DATA);
        URI_MATCHER.addURI(TimeContract.AUTHORITY, TimeContract.FACTOR_TABLE, FACTOR_DATA);
        return URI_MATCHER;
    }

    @Override
    public boolean onCreate() {
        //getContext().deleteDatabase(timeDbHelper.DATABASE_NAME);
        mDbHelper = new timeDbHelper(getContext());
        return true;
    }



    @Override
    public Cursor query(Uri uri,  String[] projection, String selection, String[] selectionArgs,
                        String sortOrder)
    {

        //MatrixCursor cursor;
        Cursor cursor;
        switch (URI_MATCHER.match(uri))
        {
            case TIME_DATA:
                cursor = mDbHelper.getReadableDatabase().query(
                        TimeContract.TimeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
                /*String []DatCol = {"hr","min","sec","isMinus"};
                 cursor= new MatrixCursor(DatCol);

                if (time!=null)
                {
                    int isMinus1 = 0;
                    if (isMinus)
                        isMinus1 = 1;
                    cursor.newRow().add(time[0]).add(time[1]).add(time[2]).add(isMinus1);
                }
                else
                    cursor=null;
                break;*/
            case FACTOR_DATA:
                cursor = mDbHelper.getReadableDatabase().query(
                        TimeContract.FactorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

/*            String []DatCol1 = {"factor"};
                cursor = new MatrixCursor(DatCol1);
                cursor.newRow().add(factor);
                break;*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri))
        {
            case TIME_DATA:
                return TimeContract.TimeEntry.CONTENT_ITEM_TYPE;

            case FACTOR_DATA:
                return TimeContract.FactorEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (db.isOpen())
        {
            Log.i("open","good");
        }
        Uri returnUri;
        long _id;

        switch (URI_MATCHER.match(uri))
        {
            case TIME_DATA:
                _id = db.insert(TimeContract.TimeEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = TimeContract.TimeEntry.buildTimeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

/*            if (time==null)
                    time = new double[]{0,0,0};

                if (contentValues.containsKey("hr"))
                    time[0] = contentValues.getAsDouble("hr");

                if (contentValues.containsKey("min"))
                    time[1] = contentValues.getAsDouble("min");

                if (contentValues.containsKey("sec"))
                    time[2] = contentValues.getAsDouble("sec");

                if (contentValues.containsKey("isMinus"))
                    isMinus = contentValues.getAsBoolean("isMinus");

                return TimeContract.TIME_URI;*/

            case FACTOR_DATA:
                _id = db.insert(TimeContract.FactorEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = TimeContract.TimeEntry.buildTimeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
              /*  if (contentValues.containsKey("factor"))
                    factor = contentValues.getAsDouble("factor");
                else
                    factor = 0;
                return TimeContract.FACTOR_URI;*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int numRecordsRemoved = 0;

        switch (URI_MATCHER.match(uri))
        {
            case TIME_DATA:
                numRecordsRemoved = db.delete(
                        TimeContract.TimeEntry.TABLE_NAME, selection, selectionArgs);
                /*if (time!=null) //record exist
                {
                    time = null;
                    isMinus = false;
                    numRecordsRemoved++;
                }*/
                break;

            case FACTOR_DATA:
                numRecordsRemoved = db.delete(
                        TimeContract.FactorEntry.TABLE_NAME, selection, selectionArgs);
                /*factor = 0;
                numRecordsRemoved++;*/
                break;
        }
        if (numRecordsRemoved != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRecordsRemoved;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated;

        switch (match) {
            case TIME_DATA:
                rowsUpdated = db.update(TimeContract.TimeEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case FACTOR_DATA:
                rowsUpdated = db.update(TimeContract.FactorEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}
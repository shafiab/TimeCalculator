package com.example.shafiab.timecalculator;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by shafiab on 9/14/14.
 */
public class getDataFromContentProvider extends AsyncTask {

    Context context;
    ContentResolver contentResolver;
    Cursor cursor;

    getDataFromContentProvider(Context context)
    {
        context = this.context;

    }
    void getData()
    {

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}

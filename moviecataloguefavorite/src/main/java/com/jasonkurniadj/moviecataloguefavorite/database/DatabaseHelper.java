package com.jasonkurniadj.moviecataloguefavorite.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.jasonkurniadj.moviecataloguefavorite.model.ItemModel;

import java.util.ArrayList;

public class DatabaseHelper {

    private static final String LOG_TAG = "JS_LOG";
    private static final String AUTH = "content://com.jasonkurniadj.moviecatalogue.PROVIDER";
    private Context context;

    public DatabaseHelper (Context context) {
        this.context = context;
    }

    public ArrayList<ItemModel> fetchData(String type) {
        Uri uri = Uri.parse(AUTH);

        String[] projection = null;
        String selection = DatabaseContract.ItemColumns.TYPE + " LIKE '" + type + "'";
        String[] selectionArgs = null;
        String sortOrder = DatabaseContract.ItemColumns._ID + " ASC";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        Log.i(LOG_TAG, "Fetch data on content provider apps. Total: " + cursor.getCount());

        ArrayList<ItemModel> modelList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ItemModel model = new ItemModel();
                model.setItemExternalID(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.EXTERNAL_ID)));
                model.setItemType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.TYPE)));
                model.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.NAME)));
                model.setItemPoster(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.POSTER)));
                model.setItemDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.DESCRIPTION)));
                model.setItemRating(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.RATING)));
                model.setItemReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.DATE)));

                modelList.add(model);
            } while(cursor.moveToNext());
        }

        cursor.close();
        return modelList;
    }
}

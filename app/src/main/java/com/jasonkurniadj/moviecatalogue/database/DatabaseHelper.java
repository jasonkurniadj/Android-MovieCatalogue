package com.jasonkurniadj.moviecatalogue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "MovieCatalogueDB";
    private static final int DATABASE_VERSION = 4;

    private static final String SQL_CREATE_ITEM_TABLE = String.format("" +
            " CREATE TABLE %s (" +
            " %s INTEGER PRIMARY KEY AUTOINCREMENT, " + // _ID
            " %s TEXT NOT NULL UNIQUE, " + // EXTERNAL ID
            " %s TEXT NOT NULL, " + // TYPE
            " %s TEXT NOT NULL UNIQUE, " + // NAME
            " %s TEXT, " + // POSTER
            " %s TEXT, " + // DESCRIPTION
            " %s TEXT NOT NULL, " + // RATING
            " %s TEXT NOT NULL) ", // DATE
            DatabaseContract.TABLE_ITEM_NAME,
            DatabaseContract.ItemColumns._ID,
            DatabaseContract.ItemColumns.EXTERNAL_ID,
            DatabaseContract.ItemColumns.TYPE,
            DatabaseContract.ItemColumns.NAME,
            DatabaseContract.ItemColumns.POSTER,
            DatabaseContract.ItemColumns.DESCRIPTION,
            DatabaseContract.ItemColumns.RATING,
            DatabaseContract.ItemColumns.DATE
    );

    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
        Log.i("JS_LOG", "CREATE TABLE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_ITEM_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<ItemModel> getAllItems(String type) {
        ArrayList<ItemModel> modelList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                DatabaseContract.TABLE_ITEM_NAME,
                null,
                DatabaseContract.ItemColumns.TYPE + " LIKE '" + type + "'",
                null,
                null,
                null,
                DatabaseContract.ItemColumns._ID + " ASC",
                null
        );
        cursor.moveToFirst();

        ItemModel model;
        if (cursor.getCount() > 0) {
            do {
                model = new ItemModel();
                model.setItemExternalID(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.EXTERNAL_ID)));
                model.setItemType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.TYPE)));
                model.setItemName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.NAME)));
                model.setItemPoster(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.POSTER)));
                model.setItemDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.DESCRIPTION)));
                model.setItemRating(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.RATING)));
                model.setItemReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.DATE)));

                modelList.add(model);
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }

        cursor.close();
        sqLiteDatabase.close();
        return modelList;
    }

    public ArrayList<ItemModel> getAllItems() {
        ArrayList<ItemModel> modelList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                DatabaseContract.TABLE_ITEM_NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.ItemColumns._ID + " ASC",
                null
        );
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
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
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }

        cursor.close();
        sqLiteDatabase.close();
        return modelList;
    }

    public Boolean checkItem(String externalID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DatabaseContract.TABLE_ITEM_NAME,
                new String[]{DatabaseContract.ItemColumns._ID,
                        DatabaseContract.ItemColumns.EXTERNAL_ID,
                        DatabaseContract.ItemColumns.TYPE,
                        DatabaseContract.ItemColumns.NAME,
                        DatabaseContract.ItemColumns.POSTER,
                        DatabaseContract.ItemColumns.DESCRIPTION,
                        DatabaseContract.ItemColumns.RATING,
                        DatabaseContract.ItemColumns.DATE},
                DatabaseContract.ItemColumns.EXTERNAL_ID + " = ?", new String[]{externalID},
                null,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String tempExternalID;
        try {
            tempExternalID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ItemColumns.EXTERNAL_ID));
        }
        catch (Exception ex) {
            tempExternalID = "";
        }
        cursor.close();

        if (tempExternalID != "") {
            return true;
        }
        return false;
    }

    public boolean insertItem(ItemModel model){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(DatabaseContract.ItemColumns.EXTERNAL_ID, model.getItemExternalID());
        args.put(DatabaseContract.ItemColumns.TYPE, model.getItemType());
        args.put(DatabaseContract.ItemColumns.NAME, model.getItemName());
        args.put(DatabaseContract.ItemColumns.POSTER, model.getItemPoster());
        args.put(DatabaseContract.ItemColumns.DESCRIPTION, model.getItemDescription());
        args.put(DatabaseContract.ItemColumns.RATING, model.getItemRating());
        args.put(DatabaseContract.ItemColumns.DATE, model.getItemReleaseDate());

        long result = sqLiteDatabase.insert(DatabaseContract.TABLE_ITEM_NAME, null, args);
        sqLiteDatabase.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void deleteItem(ItemModel model) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DatabaseContract.TABLE_ITEM_NAME,
                DatabaseContract.ItemColumns.EXTERNAL_ID + " = ?", new String[]{String.valueOf(model.getItemExternalID())});
        sqLiteDatabase.close();
    }
}

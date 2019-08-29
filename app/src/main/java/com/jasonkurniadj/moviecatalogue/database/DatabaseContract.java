package com.jasonkurniadj.moviecatalogue.database;

import android.provider.BaseColumns;

class DatabaseContract {

    static final String TABLE_ITEM_NAME = "item";
    static final class ItemColumns implements BaseColumns {
        static String EXTERNAL_ID = "itemExternalID";
        static String TYPE = "itemType";
        static String NAME = "itemName";
        static String POSTER = "itemPoster";
        static String DESCRIPTION = "itemDescription";
        static String RATING = "itemRating";
        static String DATE = "itemDate";
    }
}

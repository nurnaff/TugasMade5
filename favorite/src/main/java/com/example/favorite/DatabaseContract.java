package com.example.favorite;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.andinurnaf.cobatugas5.activity";
    public static final String SCHEME = "content";


    public static final class FavoriteColumns implements BaseColumns {

        public static String TABLE_NAME = "favourite";

        public static String JUDUL = "judul";
        public static String DESKRIPSI = "deskripsi";
        public static String RILIS = "rilis";
        public static String FOTO = "foto";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

    }
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}

package com.example.andinurnaf.cobatugas5.activity.Database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String FAVORIT_TABLE = "favorit";

    public static final class FavoritColumns implements BaseColumns {
        public static String VOTECOUNT = "votecount";
        public static String ID = "id";
        public static String VIDEO = "video";
        public static String VOTEAVERAGE = "voteaverage";
        public static String TITLE = "title";
        public static String POPULARITY = "popularity";
        public static String POSTERPATH = "posterpath";
        public static String ORIGINALLANGUAGE = "originallanguage";
        public static String ORIGINALTITLE = "originaltitle";
        public static String STRGENRES = "strgenres";
        public static String BACKDROPPATH = "backdroppath";
        public static String ADULT = "adult";
        public static String OVERVIEW = "overview";
        public static String RELEASEDATE = "releasedate";
        public static String FAVTIMESTAMP = "favtimestamp";
    }

    public static final String AUTHORITY = "com.sofyanthayf.nontonapa";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(FAVORIT_TABLE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }

}


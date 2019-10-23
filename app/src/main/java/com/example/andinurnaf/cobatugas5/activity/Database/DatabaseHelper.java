package com.example.andinurnaf.cobatugas5.activity.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovie";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVORIT = "CREATE TABLE " + DatabaseContract.FAVORIT_TABLE +
            "( "+ DatabaseContract.FavoritColumns.VOTECOUNT +" INTEGER, " +
            DatabaseContract.FavoritColumns.ID + " INTEGER PRIMARY KEY, " +
            DatabaseContract.FavoritColumns.VIDEO + " INTEGER, " +
            DatabaseContract.FavoritColumns.VOTEAVERAGE + " REAL, " +
            DatabaseContract.FavoritColumns.TITLE + " STRING, " +
            DatabaseContract.FavoritColumns.POPULARITY + " REAL, " +
            DatabaseContract.FavoritColumns.POSTERPATH + " STRING, " +
            DatabaseContract.FavoritColumns.ORIGINALLANGUAGE + " STRING, " +
            DatabaseContract.FavoritColumns.ORIGINALTITLE + " STRING, " +
            DatabaseContract.FavoritColumns.STRGENRES + " STRING, " +
            DatabaseContract.FavoritColumns.BACKDROPPATH + " STRING, " +
            DatabaseContract.FavoritColumns.ADULT + " INTEGER, " +
            DatabaseContract.FavoritColumns.OVERVIEW + " STRING, " +
            DatabaseContract.FavoritColumns.RELEASEDATE + " STRING, " +
            DatabaseContract.FavoritColumns.FAVTIMESTAMP + " INTEGER ) " ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FAVORIT_TABLE );
        onCreate(sqLiteDatabase);
    }
}

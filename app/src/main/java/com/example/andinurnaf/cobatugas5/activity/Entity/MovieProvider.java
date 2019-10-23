package com.example.andinurnaf.cobatugas5.activity.Entity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.andinurnaf.cobatugas5.activity.Database.DatabaseContract;
import com.example.andinurnaf.cobatugas5.activity.Database.DatabaseMovie;

import java.sql.SQLException;

import static com.example.andinurnaf.cobatugas5.activity.Database.DatabaseContract.CONTENT_URI;
import static com.example.andinurnaf.cobatugas5.activity.Database.DatabaseContract.AUTHORITY;

public class MovieProvider extends ContentProvider {

    private static final int FAVORIT = 1;
    private static final int FAVORIT_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
                sUriMatcher.addURI(AUTHORITY, DatabaseContract.FAVORIT_TABLE, FAVORIT);

                sUriMatcher.addURI(AUTHORITY,DatabaseContract.FAVORIT_TABLE+ "/#", FAVORIT_ID);
    }

    private DatabaseMovie databaseMovie;

    @Override
    public boolean onCreate() {
        databaseMovie = new DatabaseMovie(getContext());
        try {
            databaseMovie.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case FAVORIT:
                cursor = databaseMovie.queryProvider();
                break;

            case FAVORIT_ID:
                cursor = databaseMovie.queryByIdProvider( uri.getLastPathSegment() );
                break;

            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        switch (sUriMatcher.match(uri)){
            case FAVORIT:
                added = databaseMovie.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORIT_ID:
                deleted =  databaseMovie.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated ;
        switch (sUriMatcher.match(uri)) {
            case FAVORIT_ID:
                updated =  databaseMovie.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
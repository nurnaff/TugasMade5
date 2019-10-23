package com.example.andinurnaf.cobatugas5.activity.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMovie {
    private static String DATABASE_TABLE = DatabaseContract.FAVORIT_TABLE;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public DatabaseMovie(Context context) {
        this.context = context;
    }

    public DatabaseMovie open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public List<Movie> listfavorit(){
        List<Movie> movieList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseContract.FAVORIT_TABLE,null,null,null,null,null, DatabaseContract.FavoritColumns.FAVTIMESTAMP +" DESC",null);
        cursor.moveToFirst();
        Movie movie;

        if (cursor.getCount()>0) {
            do {
                movie = movieset(cursor);
                movieList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return movieList;
    }

    public long addFavorit(Movie movie){
        ContentValues initialValues =  new ContentValues();
        movie.setFavTimeStamp();

        initialValues.put(DatabaseContract.FavoritColumns.ID, movie.getId());
        initialValues.put(DatabaseContract.FavoritColumns.VOTECOUNT, movie.getVoteCount());
        initialValues.put(DatabaseContract.FavoritColumns.VIDEO, movie.getVideo());
        initialValues.put(DatabaseContract.FavoritColumns.VOTEAVERAGE, movie.getVoteAverage());
        initialValues.put(DatabaseContract.FavoritColumns.TITLE, movie.getTitle());
        initialValues.put(DatabaseContract.FavoritColumns.POPULARITY, movie.getPopularity());
        initialValues.put(DatabaseContract.FavoritColumns.POSTERPATH, movie.getPosterPath());
        initialValues.put(DatabaseContract.FavoritColumns.ORIGINALLANGUAGE, movie.getOriginalLanguage());
        initialValues.put(DatabaseContract.FavoritColumns.ORIGINALTITLE, movie.getOriginalTitle());
        initialValues.put(DatabaseContract.FavoritColumns.STRGENRES, movie.getStrgenres());
        initialValues.put(DatabaseContract.FavoritColumns.BACKDROPPATH, movie.getBackdropPath());
        initialValues.put(DatabaseContract.FavoritColumns.ADULT, movie.getAdult());
        initialValues.put(DatabaseContract.FavoritColumns.OVERVIEW, movie.getOverview());
        initialValues.put(DatabaseContract.FavoritColumns.RELEASEDATE, movie.getReleaseDate());
        initialValues.put(DatabaseContract.FavoritColumns.FAVTIMESTAMP, movie.getFavTimeStamp());

        return database.insert(DatabaseContract.FAVORIT_TABLE, null, initialValues);
    }

    public Movie movie(Integer id){
        Movie movie = null;
        Cursor cursor = database.query(DatabaseContract.FAVORIT_TABLE,null, DatabaseContract.FavoritColumns.ID + "=" + id.toString(), null,null,null,null);
        cursor.moveToFirst();

        if(cursor.getCount() != 0 ){
            movie = movieset(cursor);
        }
        cursor.close();

        return movie;
    }

    public int deleteFavorit(int id){
        return database.delete(DatabaseContract.FAVORIT_TABLE, DatabaseContract.FavoritColumns.ID + " = '"+id+"'", null);
    }

    private Movie movieset( Cursor cursor ){
        Movie movie = new Movie();
        movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.ID)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.VOTECOUNT)));
        movie.setVideo( cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.VIDEO))!=0 );
        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.VOTEAVERAGE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.TITLE)));
        movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.POPULARITY)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.POSTERPATH)));
        movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.ORIGINALLANGUAGE)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.ORIGINALTITLE)));
        movie.setStrgenres(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.STRGENRES)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.BACKDROPPATH)));
        movie.setAdult( cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.ADULT)) !=0 );
        movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.OVERVIEW)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.RELEASEDATE)));
        movie.setFavTimeStamp(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritColumns.FAVTIMESTAMP)));
        return movie;
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                , DatabaseContract.FavoritColumns.ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                , DatabaseContract.FavoritColumns.ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values, DatabaseContract.FavoritColumns.ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, DatabaseContract.FavoritColumns.ID + " = ?", new String[]{id});
    }

}
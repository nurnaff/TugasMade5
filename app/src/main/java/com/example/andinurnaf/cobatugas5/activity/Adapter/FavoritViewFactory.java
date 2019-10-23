package com.example.andinurnaf.cobatugas5.activity.Adapter;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Database.DatabaseMovie;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;
import com.example.andinurnaf.cobatugas5.activity.Widget.FavoritWidget;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoritViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;

    private DatabaseMovie movieHelper;
    private List<Movie> listMovie = new ArrayList<>();

    public FavoritViewFactory(Context context, Intent intent) {
        Log.d(">>_STACK FACTORY", "started");
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        movieHelper = new DatabaseMovie(mContext);
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listMovie = movieHelper.listfavorit();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        rv.setTextViewText(R.id.tvWidgetText, listMovie.get(position).getTitle());

        Bitmap poster = getImageBitmap("http://image.tmdb.org/t/p/w300" + listMovie.get(position).getPosterPath() );
        rv.setImageViewBitmap(R.id.ivWidgetPic, poster);

        Bundle extras = new Bundle();
        extras.putInt(FavoritWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.ivWidgetPic, fillInIntent);
        return rv;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Bitmap getImageBitmap(String url_string) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(url_string);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            try {
                InputStream in = new BufferedInputStream( urlConnection.getInputStream() );
                bitmap = BitmapFactory.decodeStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}


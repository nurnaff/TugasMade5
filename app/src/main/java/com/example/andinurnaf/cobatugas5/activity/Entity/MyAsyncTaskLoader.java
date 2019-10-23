package com.example.andinurnaf.cobatugas5.activity.Entity;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import com.example.andinurnaf.cobatugas5.activity.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private ArrayList<Movie> movies;
    private boolean hasResult = false;
    private String keyword;
    private int page;

    private MoviesLoadingListener listener;

    public MyAsyncTaskLoader(final Context context, String keyword, int page, MoviesLoadingListener listener) {
        super(context);

        onContentChanged();
        this.keyword = keyword;
        this.page = page;
        this.listener = listener;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(movies);
    }

    @Override
    public void deliverResult(final ArrayList<Movie> data) {
        movies = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            onReleaseResources(movies);
            movies = null;
            hasResult = false;
        }
    }

    private void onReleaseResources(ArrayList<Movie> movies) {

    }


    @Override
    public ArrayList<Movie> loadInBackground() {
        String url;
        String API_KEY = BuildConfig.TMDB_API_KEY;

        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<Movie> movies = new ArrayList<>();

        if( keyword.equals("") ) {
            url = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY + "&page=" + page +
                    "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
        }
        else if( keyword.equals("_np") ) {
            url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ API_KEY +
                    "&page=" + page + "&language=en-US";
        }
        else if( keyword.equals("_up") ) {
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY +
                    "&page=" + page + "&language=en-US";
        }
        else {
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY +
                    "&page=" + page + "&language=en-US&query=" + keyword ;
        }

        client.get( url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);

                    Integer total_results = responseObject.getInt("total_results");
                    Integer total_pages = responseObject.getInt("total_pages");
                    Integer page = responseObject.getInt("page");

                    listener.onMoviesLoaded(total_results, total_pages, page);

                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0 ; i < list.length() ; i++){
                        JSONObject movieitem = list.getJSONObject(i);
                        Movie movie = new Movie(movieitem);
                        movies.add(movie);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movies;
    }

    public interface MoviesLoadingListener {
        void onMoviesLoaded(int total_results, int total_pages, int page);
    }
}
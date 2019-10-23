package com.example.andinurnaf.cobatugas5.activity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Adapter.RecyclerViewAdapter;
import com.example.andinurnaf.cobatugas5.activity.Database.DatabaseMovie;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {
    @BindView(R.id.rvMovieList)
    RecyclerView rvMovieList;
    RecyclerViewAdapter adapter;
    List<Movie> movies;
    DatabaseMovie databaseMovie = new DatabaseMovie(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        try {
            databaseMovie.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        movies = databaseMovie.listfavorit();
        databaseMovie.close();

        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        rvMovieList.setLayoutManager(rvLayoutManager);
        adapter = new RecyclerViewAdapter(this, movies);
        rvMovieList.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            databaseMovie.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        movies = databaseMovie.listfavorit();
        databaseMovie.close();
        adapter.setMovieList( movies );
        adapter.notifyDataSetChanged();
    }

}

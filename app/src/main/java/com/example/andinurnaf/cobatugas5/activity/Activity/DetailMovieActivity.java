package com.example.andinurnaf.cobatugas5.activity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Database.DatabaseMovie;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity {
    @BindView(R.id.ivBackdrop)
    ImageView ivBackdrop;
    @BindView(R.id.ivDetailPoster) ImageView ivPoster;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetailGenre) TextView tvGenre;
    @BindView(R.id.tvDetailRating) TextView tvRating;
    @BindView(R.id.tvDetailOverview) TextView tvOverview;
    @BindView(R.id.tvDetailPopularity) TextView tvPopularity;
    @BindView(R.id.tvDetailYear) TextView tvYear;
    @BindView(R.id.ivFavorit) ImageView ivFavorit;

    String posterurl;
    Boolean favorit = false;
    Movie movie;
    DatabaseMovie databaseMovieHelper = new DatabaseMovie(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        movie = getIntent().getParcelableExtra("movie");

        String imgurl = "http://image.tmdb.org/t/p/w780" + movie.getBackdropPath();
        Glide.with(this).load(imgurl).into(ivBackdrop);

        tvTitle.setText( movie.getTitle() );
        tvGenre.setText( movie.getStrgenres() );
        tvYear.setText( movie.getReleaseDate().substring(0,4) );
        tvOverview.setText( movie.getOverview() );

        String starting = String.format(getResources().getString(R.string.rating), movie.getVoteAverage() );
        tvRating.setText( starting );

        String starpop = String.format(  getResources().getString(R.string.popularity), movie.getPopularity() );
        tvPopularity.setText( starpop );

        posterurl = "http://image.tmdb.org/t/p/w185" + movie.getPosterPath();
        Glide.with(this).load(posterurl).into(ivPoster);

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMovieActivity.this, PosterActivity.class);
                intent.putExtra("posterurl", posterurl );
                startActivity(intent);
            }
        });

        if( isFavorit( movie.getId() ) ){
            favorit = true;
            ivFavorit.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        ivFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!favorit ){
                    setFavorit(favorit);
                    favorit = true;
                    ivFavorit.setImageResource(R.drawable.ic_favorite_black_24dp);

                    Toast.makeText(DetailMovieActivity.this, R.string.toast_favorit_added,
                            Toast.LENGTH_LONG).show();

                } else {
                    unsetFavorit( movie.getId() );
                    favorit = false;
                    ivFavorit.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    Toast.makeText(DetailMovieActivity.this, R.string.toast_favorit_removed,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setFavorit(Boolean favorit) {
        try {
            databaseMovieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseMovieHelper.addFavorit(movie);
        databaseMovieHelper.close();
    }

    private void unsetFavorit(Integer id){
        try {
            databaseMovieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseMovieHelper.deleteFavorit(id);
        databaseMovieHelper.close();
    }

    private boolean isFavorit(Integer id){
        boolean fav = false;
        try {
            databaseMovieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if( databaseMovieHelper.movie(id) != null ) {
            fav = true;
        }

        databaseMovieHelper.close();
        return fav;
    }


}
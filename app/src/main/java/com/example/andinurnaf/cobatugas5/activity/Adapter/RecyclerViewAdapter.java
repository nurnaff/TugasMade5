package com.example.andinurnaf.cobatugas5.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Activity.DetailMovieActivity;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieHolder> {
    List<Movie> movies;
    private LayoutInflater inflater;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
        inflater = LayoutInflater.from(context);
    }

    public void setMovieList(List<Movie> movies){
        this.movies = movies;
    }

    public List<Movie> getMovieList(){
        return this.movies;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_movie, parent, false);
        MovieHolder holder = new MovieHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie current = movies.get( position );
        holder.setData( current, position );
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPoster)
        ImageView ivPoster;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvGenre) TextView tvGenre;
        @BindView(R.id.tvRating) TextView tvRating;
        @BindView(R.id.tvPopularity) TextView tvPopularity;
        @BindView(R.id.tvYear) TextView tvYear;

        int position;
        Movie current;
        View itemView;

        public MovieHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

        }

        public void setData(Movie current, int position) {

            this.current = current;
            ButterKnife.bind(this, itemView);

            String poster = current.getPosterPath();
            if( poster != null || !poster.equals("")){
                String imgurl = "http://image.tmdb.org/t/p/w92" + poster;
                Glide.with(context).load(imgurl).into(ivPoster);
            }

            tvTitle.setText( current.getTitle() );

            String strgenre = String.format( context.getResources().getString(R.string.genre), current.getStrgenres() );
            tvGenre.setText( strgenre );

            String strrating = String.format( context.getResources().getString(R.string.rating),current.getVoteAverage() );
            tvRating.setText( strrating );

            String strpop = String.format( context.getResources().getString(R.string.popularity),current.getPopularity() );
            tvPopularity.setText( strpop );

            String releasedate = current.getReleaseDate();
            if( !releasedate.equals("") ){
                String stryear = String.format( context.getResources().getString(R.string.year),releasedate.substring(0,4));
                tvYear.setText( stryear );
            }
        }

        public void setListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailMovieActivity.class);
                    intent.putExtra("movie", current);
                    context.startActivity(intent);

                }
            });

        }
    }
}


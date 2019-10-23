package com.example.andinurnaf.cobatugas5.activity.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    private Integer voteCount;
    private Integer id;
    private Boolean video;
    private Double voteAverage;
    private String title;
    private Double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private List<String> genreIds = null;
    private String strgenres;
    private String backdropPath;
    private Boolean adult;
    private String overview;
    private String releaseDate;

    private Long favTimeStamp;

    public Movie(JSONObject movieobject){
        try {
            Integer votecount = movieobject.getInt("vote_count");
            Integer id = movieobject.getInt("id");
            Boolean video = movieobject.getBoolean("video");
            Double voteaverage = movieobject.getDouble("vote_average");
            String title = movieobject.getString("title");
            Double popularity = movieobject.getDouble("popularity");
            String posterpath = movieobject.getString("poster_path");
            String originallanguage = movieobject.getString("original_language");
            String originaltitle = movieobject.getString("original_title");
            String backdroppath = movieobject.getString("backdrop_path");
            Boolean adult = movieobject.getBoolean("adult");
            String overview = movieobject.getString("overview");
            String releasedate = movieobject.getString("release_date");

            MovieItem genre;
            String genrestr = "";
            List<String> genreid = new ArrayList<String>();

            JSONArray genres = movieobject.getJSONArray("genre_ids");
            for (int i=0; i<genres.length(); i++){
                genreid.add( genres.get(i).toString() );

                genre = new MovieItem( genres.get(i).toString() );
                genrestr += genre.getName();
                if( i < genres.length()-1 ) genrestr += ", ";
            }
            this.strgenres = genrestr;

            this.voteCount = votecount;
            this.id = id;
            this.video = video;
            this.voteAverage = voteaverage;
            this.title = title;
            this.popularity = popularity;
            this.posterPath = posterpath;
            this.originalLanguage = originallanguage;
            this.originalTitle = originaltitle;
            this.genreIds = genreid;
            this.backdropPath = backdroppath;
            this.adult = adult;
            this.overview = overview;
            this.releaseDate = releasedate;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Movie() {

    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getStrgenres() {
        return strgenres;
    }

    public void setStrgenres(String strgenres) {
        this.strgenres = strgenres;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setFavTimeStamp() {
        long unixTime = System.currentTimeMillis() / 1000L;
        this.favTimeStamp = unixTime;
    }

    public void setFavTimeStamp(Long timeStamp) {
        this.favTimeStamp = timeStamp;
    }

    public Long getFavTimeStamp() {
        return favTimeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.voteCount);
        dest.writeValue(this.id);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.title);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeStringList(this.genreIds);
        dest.writeString(this.strgenres);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    protected Movie(Parcel in) {
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = in.createStringArrayList();
        this.strgenres = in.readString();
        this.backdropPath = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

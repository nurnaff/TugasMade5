package com.example.favorite;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String photo;
    private String judul;
    private String deskrisi;
    private String rilis;


    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskrisi() {
        return deskrisi;
    }

    public void setDeskrisi(String deskrisi) {
        this.deskrisi = deskrisi;
    }

    public String getRilis() {
        return rilis;
    }

    public void setRilis(String rilis) {
        this.rilis = rilis;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.photo);
        dest.writeString(this.judul);
        dest.writeString(this.deskrisi);
        dest.writeString(this.rilis);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.photo = in.readString();
        this.judul = in.readString();
        this.deskrisi = in.readString();
        this.rilis = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

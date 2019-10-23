package com.example.favorite;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.example.favorite.DatabaseContract.getColumnInt;
import static com.example.favorite.DatabaseContract.getColumnString;

public class FavoriteItem implements Parcelable {
    private int id;
    private String title, description, rilis, foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRilis() {
        return rilis;
    }

    public void setRilis(String rilis) {
        this.rilis = rilis;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.rilis);
        dest.writeString(this.foto);
    }

    public FavoriteItem() {
    }

    public FavoriteItem(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.FavoriteColumns._ID);
        this.title = getColumnString(cursor, DatabaseContract.FavoriteColumns.JUDUL);
        this.description = getColumnString(cursor, DatabaseContract.FavoriteColumns.DESKRIPSI);
        this.rilis = getColumnString(cursor, DatabaseContract.FavoriteColumns.RILIS);
        this.foto = getColumnString(cursor, DatabaseContract.FavoriteColumns.FOTO);
    }

    protected FavoriteItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.rilis = in.readString();
        this.foto = in.readString();
    }

    public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel source) {
            return new FavoriteItem(source);
        }


        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}
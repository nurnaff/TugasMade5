package com.example.favorite;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity{

    public static String EXTRA_MOVIE = "extra_movie";
    private TextView tvObject;
    ImageView img;
    public static int RESULT_ADD = 101;
    ImageButton btnFavourite;

    private FavoriteItem favouriteItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvObject = (TextView)findViewById(R.id.tv_object_received);
        img = findViewById(R.id.photo);

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) favouriteItem = new FavoriteItem(cursor);
                cursor.close();
            }
        }

        Glide.with(DetailActivity.this)
                .load(favouriteItem.getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(img);
        String text = "Judul : "+ favouriteItem.getTitle()+"\nDeskripsi : "+ favouriteItem.getDescription()+"\nRilis : "+ favouriteItem.getRilis();
        tvObject.setText(text);
    }

}
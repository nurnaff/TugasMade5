package com.example.andinurnaf.cobatugas5.activity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.andinurnaf.cobatugas5.activity.R;

public class PosterActivity extends AppCompatActivity {
    ImageView BigPoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        BigPoster = (ImageView) findViewById(R.id.ivBigPoster);
        String posterurl = getIntent().getStringExtra("posterurl");
        Glide.with(this).load(posterurl).into(BigPoster);

    }
}

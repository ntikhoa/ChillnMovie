package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "tmdb_movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        TextView textView = findViewById(R.id.textView);
        textView.setText(id + "");
    }
}
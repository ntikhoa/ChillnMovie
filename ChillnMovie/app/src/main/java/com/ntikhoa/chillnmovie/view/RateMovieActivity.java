package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.viewmodel.RateMovieViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class RateMovieActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "movie id";

    private FrameLayout frameLayout;
    private RateMovieFragment plotFragment, visualEffectFragment, soundEffectFragment;
    private int plotRate, visualEffectRate, soundEffectRate;
    private RateMovieViewModel viewModel;
    private Button btn;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_movie);

        initComponent();
        addRatingFragment();
        loadData();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plotRate = plotFragment.getRate();
                visualEffectRate = visualEffectFragment.getRate();
                soundEffectRate = soundEffectFragment.getRate();

                String rate = String.valueOf(plotRate) + " "
                        + String.valueOf(visualEffectRate) + " "
                        + String.valueOf(soundEffectRate);
                Toast.makeText(getApplicationContext(), rate, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(RateMovieViewModel.class);
        btn = findViewById(R.id.button);
        frameLayout = findViewById(R.id.framelayout);
    }

    private void addRatingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        plotFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerPlot, plotFragment);
        ft.addToBackStack(null);

        visualEffectFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerVisualEffect, visualEffectFragment);
        ft.addToBackStack(null);

        soundEffectFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerSoundEffect, soundEffectFragment);
        ft.addToBackStack(null);

        ft.commit();
    }

    private void loadData() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);

        viewModel.getMLDmovieDetail(id)
                .observe(this, new Observer<MovieDetail>() {
                    @Override
                    public void onChanged(MovieDetail movieDetail) {
                        setBackground(movieDetail);
                    }
                });
    }

    private void setBackground(MovieDetail movieDetail) {
        String posterUrl = Movie.path + movieDetail.getPosterPath();
        Picasso.get()
                .load(posterUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        frameLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }
                });
    }
}
package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.RateMovieViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class RateMovieActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "movie id";
    public static final String EXTRA_TITLE = "movie title";
    public static final String EXTRA_POSTER_PATH = "movie poster path";

    private FrameLayout frameLayout;
    private RateMovieFragment plotFragment, visualEffectFragment, soundEffectFragment;

    private RateMovieViewModel viewModel;
    private Button btn;
    private TextView textViewTitle;

    private int plotRate, visualEffectRate, soundEffectRate;
    private String comment;

    private int id;
    private String title;
    private String posterPath;

    private UserRate userRate;

    FirebaseAuth auth;

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
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CommentFragment fragment = new CommentFragment();
                ft.add(R.id.fragmentContainer, fragment);
                ft.addToBackStack(null);
                ft.commit();

                setOnClickFragment(fragment);
            }
        });
    }

    private void setOnClickFragment(CommentFragment fragment) {
        fragment.setOnClickSubmit(new CommentFragment.OnClickSubmit() {
            @Override
            public void onClick() {
                plotRate = plotFragment.getRate();
                visualEffectRate = visualEffectFragment.getRate();
                soundEffectRate = soundEffectFragment.getRate();
                comment = fragment.getComment();

                viewModel.getMLDmovieRate(id)
                        .observe(RateMovieActivity.this, new Observer<MovieRate>() {
                            @Override
                            public void onChanged(MovieRate movieRate) {

                                UserRate newUserRate = new UserRate(auth.getUid());
                                newUserRate.setPlotVote(plotRate);
                                newUserRate.setVisualEffectVote(visualEffectRate);
                                newUserRate.setSoundEffectVote(soundEffectRate);
                                newUserRate.setComment(comment);

                                if (userRate != null) {
                                    movieRate.updateVote(userRate, newUserRate);
                                } else
                                    movieRate.vote(plotRate, visualEffectRate, soundEffectRate);
                                viewModel.addUserRate(id, newUserRate);
                                viewModel.updateMovieRate(id, movieRate);
                            }
                        });
                finish();
            }
        });
    }

    private void initComponent() {
        auth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(RateMovieViewModel.class);
        btn = findViewById(R.id.btnNext);
        frameLayout = findViewById(R.id.fragmentContainer);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    private void addRatingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        plotFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerPlot, plotFragment);

        visualEffectFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerVisualEffect, visualEffectFragment);

        soundEffectFragment = new RateMovieFragment();
        ft.add(R.id.fragmentContainerSoundEffect, soundEffectFragment);

        ft.commit();
    }

    private void loadData() {
        Intent intent = getIntent();
        id = intent.getIntExtra(EXTRA_ID, -1);
        title = intent.getStringExtra(EXTRA_TITLE);
        posterPath = intent.getStringExtra(EXTRA_POSTER_PATH);
        textViewTitle.setText(title);
        setBackground();

        viewModel.getMLDuserRate(id, auth.getUid())
                .observe(this, new Observer<UserRate>() {
                    @Override
                    public void onChanged(UserRate userRate) {
                        RateMovieActivity.this.userRate = userRate;
                    }
                });
    }

    private void setBackground() {
        String posterURL = Movie.path + posterPath;
        Picasso.get()
                .load(posterURL)
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
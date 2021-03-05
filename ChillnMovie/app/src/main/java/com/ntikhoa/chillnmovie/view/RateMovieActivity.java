package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
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
import com.ntikhoa.chillnmovie.databinding.ActivityRateMovieBinding;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserModeSingleton;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.RateMovieViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RateMovieActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "movie id";
    public static final String EXTRA_TITLE = "movie title";
    public static final String EXTRA_POSTER_PATH = "movie poster path";

    private ActivityRateMovieBinding binding;

    private int mode;

    private RateMovieFragment plotFragment, visualEffectFragment, soundEffectFragment;

    private RateMovieViewModel viewModel;

    private int plotRate, visualRate, audioRate;
    private String comment;

    private long id;
    private String title;
    private String posterPath;

    @Inject
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRateMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initComponent();
        addRatingFragment();
        loadData();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
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
                visualRate = visualEffectFragment.getRate();
                audioRate = soundEffectFragment.getRate();
                comment = fragment.getComment();

                UserRate newUserRate = new UserRate(auth.getUid());
                newUserRate.setPlotVote(plotRate);
                newUserRate.setVisualVote(visualRate);
                newUserRate.setAudioVote(audioRate);
                newUserRate.setComment(comment);

                if (mode == UserAccount.USER) {
                    viewModel.rateMovie(id, newUserRate).observe(RateMovieActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean success) {
                            if (success)
                                finish();
                            else
                                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (mode == UserAccount.EDITOR) {
                    viewModel.reviewMovie(id, newUserRate).observe(RateMovieActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean success) {
                            if (success)
                                finish();
                            else
                                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else finish();
            }
        });
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this).get(RateMovieViewModel.class);
        mode = ((UserModeSingleton) getApplicationContext()).getMode();
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
        id = intent.getLongExtra(EXTRA_ID, -1);
        title = intent.getStringExtra(EXTRA_TITLE);
        posterPath = intent.getStringExtra(EXTRA_POSTER_PATH);
        binding.textViewTitle.setText(title);
        setBackground();
    }

    private void setBackground() {
        Picasso.get()
                .load(posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        binding.fragmentContainer.setBackground(new BitmapDrawable(getResources(), bitmap));
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
package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class UserRateActivity extends AppCompatActivity {
    public static final String MOVIE_ID = "movieId";
    public static final String POSTER = "poster";

    private UserRateViewModel viewModel;

    private FrameLayout frameBackground;

    private long movieId;
    private String posterPath;

    private ProgressBar pbVisual;
    private TextView tvVisual;

    private ProgressBar pbPlot;
    private TextView tvPlot;

    private ProgressBar pbAudio;
    private TextView tvAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rate);

        initComponent();
        loadData();

        setBackground();
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserRateViewModel.class);

        frameBackground = findViewById(R.id.frameBackground);

        View visual = findViewById(R.id.rateVisual);
        pbVisual = visual.findViewById(R.id.progressBarRating);
        tvVisual = visual.findViewById(R.id.textViewRate);

        View plot = findViewById(R.id.ratePlot);
        pbPlot = plot.findViewById(R.id.progressBarRating);
        tvPlot = plot.findViewById(R.id.textViewRate);

        View audio = findViewById(R.id.rateAudio);
        pbAudio = audio.findViewById(R.id.progressBarRating);
        tvAudio = audio.findViewById(R.id.textViewRate);
    }

    private void loadData() {
        movieId = getIntent().getLongExtra(MOVIE_ID, -1);
        posterPath = getIntent().getStringExtra(POSTER);

        setBackground();

        viewModel.getMLDreview(movieId)
                .observe(this, new Observer<UserRate>() {
                    @Override
                    public void onChanged(UserRate userRate) {
                        setRating(userRate.getPlotVote(), pbPlot, tvPlot);
                        setRating(userRate.getVisualVote(), pbVisual, tvVisual);
                        setRating(userRate.getAudioVote(), pbAudio, tvAudio);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ReviewFragment fragment = ReviewFragment.newInstance(movieId);
                        ft.add(R.id.fragmentContainer, fragment);
                        ft.commit();
                        fragment.setOnClickListener(new ReviewFragment.OnClickListener() {
                            @Override
                            public void onClick() {
                            }
                        });
                    }
                });
    }


    private void setBackground() {
        Picasso.get()
                .load(posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        frameBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
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

    private void setRating(double rating, ProgressBar pbRating, TextView tvRating) {

        if (rating >= 8.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.circle_green));
        } else if (rating >= 5.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(10);
        pbRating.setProgress((int) rating);
        String ratingFormatted = String.format("%.1f", rating);
        tvRating.setText(ratingFormatted);
    }
}
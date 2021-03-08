package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.ActivityUserRateBinding;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserRateActivity extends AppCompatActivity {
    public static final String MOVIE_ID = "movieId";
    public static final String POSTER = "poster";

    private ActivityUserRateBinding binding;

    private UserRateViewModel viewModel;

    private long movieId;
    private String posterPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this)
                .get(UserRateViewModel.class);

        loadData();

        setBackground();
    }

    private void loadData() {
        movieId = getIntent().getLongExtra(MOVIE_ID, -1);
        posterPath = getIntent().getStringExtra(POSTER);

        setBackground();

        viewModel.getMLDreview(movieId)
                .observe(this, userRate -> {
                    setRating(userRate.getPlotVote(), binding.ratePlot.progressBarRating, binding.ratePlot.textViewRate);
                    setRating(userRate.getVisualVote(), binding.rateVisual.progressBarRating, binding.rateVisual.textViewRate);
                    setRating(userRate.getAudioVote(), binding.rateAudio.progressBarRating, binding.rateAudio.textViewRate);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ReviewFragment fragment = ReviewFragment.newInstance(movieId);
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.commit();
                    fragment.setOnClickListener(() -> {
                    });
                });
    }


    private void setBackground() {
        Picasso.get()
                .load(posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        binding.frameBackground.setBackground(new BitmapDrawable(getResources(), bitmap));
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
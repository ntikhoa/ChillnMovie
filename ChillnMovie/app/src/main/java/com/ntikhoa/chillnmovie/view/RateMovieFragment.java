package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ProgressBar;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentRateMovieBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RateMovieFragment extends Fragment {
    private static final int GREEN_RATING = 8;
    private static final int YELLOW_RATING = 5;
    public static final int MAX_RATE = 10;
    public static final int MIN_RATE = 0;
    public static final int STEP = 1;

    private FragmentRateMovieBinding binding;

    int rate = MAX_RATE;

    public RateMovieFragment() {
        super(R.layout.fragment_rate_movie);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRateMovieBinding.bind(view);

        addListener();
        setRating();
    }

    private void addListener() {
        binding.btnDecrease.setOnClickListener(v -> {
            if (rate > MIN_RATE) {
                rate -= STEP;
                setRating();
            }
        });

        binding.btnIncrease.setOnClickListener(v -> {
            if (rate < MAX_RATE) {
                rate += STEP;
                setRating();
            }
        });
    }

    private void setRating() {
        ProgressBar pbRating = binding.include.progressBarRating;

        if (rate >= GREEN_RATING) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_green));
        } else if (rate >= YELLOW_RATING) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(MAX_RATE);
        pbRating.setProgress(rate);

        binding.include.textViewRate.setText(String.valueOf(rate));
    }

    public int getRate() {
        return rate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
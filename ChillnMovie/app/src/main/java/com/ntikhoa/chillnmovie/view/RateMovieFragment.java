package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;

public class RateMovieFragment extends Fragment {
    private static final int GREEN_RATING = 8;
    private static final int YELLOW_RATING = 5;
    public static final int MAX_RATE = 10;
    public static final int MIN_RATE = 0;
    public static final int STEP = 1;

    Button btnDecrease, btnIncrease;
    ProgressBar pbRating;
    TextView tvRating;

    int rate = MAX_RATE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rate_movie, container, false);
        initComponent(root);
        addListener();
        setRating();
        return root;
    }

    private void initComponent(View root) {
        btnDecrease = root.findViewById(R.id.btnDecrease);
        btnIncrease = root.findViewById(R.id.btnIncrease);
        View include = root.findViewById(R.id.include);
        pbRating = include.findViewById(R.id.progressBarRating);
        tvRating = include.findViewById(R.id.textViewRate);
    }

    private void addListener() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rate > MIN_RATE) {
                    rate -= STEP;
                    setRating();
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rate < MAX_RATE) {
                    rate += STEP;
                    setRating();
                }
            }
        });
    }

    private void setRating() {

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

        tvRating.setText(String.valueOf(rate));
    }

    public int getRate() {
        return rate;
    }
}
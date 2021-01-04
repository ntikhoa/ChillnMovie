package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.squareup.picasso.Picasso;

public class MovieHeaderFragment extends Fragment {

    private final String backdropPath;
    private final String title;
    private final Double voteAverage;

    private ImageView imageViewBackdrop;
    private TextView textViewTitle;
    private ProgressBar pbRating;
    private TextView textViewRating;

    private OnClickPBrating onClickPBrating;

    public void setOnClickPBrating(OnClickPBrating onClickPBrating) {
        this.onClickPBrating = onClickPBrating;
    }

    public MovieHeaderFragment(String backdropPath, String title, Double voteAverage) {
        this.backdropPath = backdropPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_header, container, false);
        initComponent(root);
        setBackdropImage();
        setRating();
        pbRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPBrating.onClick();
            }
        });

        return root;
    }

    public void initComponent(View view) {
        imageViewBackdrop = view.findViewById(R.id.imageViewBackdrop);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        View root = view.findViewById(R.id.ratingView);
        pbRating = root.findViewById(R.id.progressBarRating);
        textViewRating = root.findViewById(R.id.textViewRate);
    }

    private void setBackdropImage() {
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.colorShimmerBase))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.colorShimmerHighlight))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .setDuration(500)
                .build();
        ShimmerDrawable drawable = new ShimmerDrawable();
        drawable.setShimmer(shimmer);
        Picasso.get()
                .load(backdropPath)
                .placeholder(drawable)
                .into(imageViewBackdrop);
        textViewTitle.setText(title);
    }

    private void setRating() {
        double rating = voteAverage;

        if (rating >= 8.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_green));
        } else if (rating >= 5.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(10);
        pbRating.setProgress((int) rating);
        String ratingFormatted = String.format("%.1f", rating);
        textViewRating.setText(ratingFormatted);
    }

    interface OnClickPBrating {
        void onClick();
    }
}
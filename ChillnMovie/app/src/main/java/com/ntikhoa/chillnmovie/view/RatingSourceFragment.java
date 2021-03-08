package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentRatingSourceBinding;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.viewmodel.RatingSourceViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RatingSourceFragment extends Fragment {
    private static final String ID = "movie id";
    private static final String IMDB_ID = "imdb id";

    private FragmentRatingSourceBinding binding;

    private String imdbId;
    private Long id;

    private static final int UNIT_10 = 1;
    private static final int UNIT_100 = 10;

    private static final Double GREEN_RATING = 8.0d;
    private static final Double YELLOW_RATING = 5.0d;

    private RatingSourceViewModel viewModel;

    public RatingSourceFragment() {
        super(R.layout.fragment_rating_source);
    }

    public static RatingSourceFragment newInstance(String imdbId, Long id) {
        Bundle args = new Bundle();
        args.putString(IMDB_ID, imdbId);
        args.putLong(ID, id);
        RatingSourceFragment fragment = new RatingSourceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imdbId = getArguments().getString(IMDB_ID);
            id = getArguments().getLong(ID);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRatingSourceBinding.bind(view);

        viewModel = new ViewModelProvider(this)
                .get(RatingSourceViewModel.class);
        loadData();
    }

    private void loadData() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getMLDmovieRate(id)
                .observe(getViewLifecycleOwner(), movieRate -> fetchChillnMovieData(movieRate));

        viewModel.getMLDratingSource(imdbId)
                .observe(getViewLifecycleOwner(), ratingSource -> {
                    fetchData(ratingSource);
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private void fetchData(RatingSource ratingSource) {
        String imdbStr = ratingSource.getImDb();
        double imdb;
        if (imdbStr != null && !imdbStr.isEmpty()) {
            imdb = Double.parseDouble(imdbStr);
            setRating(imdb, binding.imdb.progressBarRating, binding.imdb.textViewRate, UNIT_10);
        }

        String metacriticStr = ratingSource.getMetacritic();
        double metacritic;
        if (metacriticStr != null && !metacriticStr.isEmpty()) {
            metacritic = Double.parseDouble(metacriticStr);
            setRating(metacritic, binding.metacritic.progressBarRating, binding.metacritic.textViewRate, UNIT_100);
        }

        String tmdbStr = ratingSource.getTheMovieDb();
        if (tmdbStr != null && !tmdbStr.isEmpty()) {
            double tmdb = Double.parseDouble(tmdbStr);
            setRating(tmdb, binding.theMovieDb.progressBarRating, binding.theMovieDb.textViewRate, UNIT_10);
        }

        String rottenTomatoesStr = ratingSource.getRottenTomatoes();
        if (rottenTomatoesStr != null && !rottenTomatoesStr.isEmpty()) {
            double rottenTomatoes = Double.parseDouble(rottenTomatoesStr);
            setRating(rottenTomatoes, binding.rottenTomatoes.progressBarRating, binding.rottenTomatoes.textViewRate, UNIT_100);
        }
    }

    private void fetchChillnMovieData(MovieRate movieRate) {
        setRating(movieRate.getVoteAverage(), binding.chillnMovie.progressBarRating, binding.chillnMovie.textViewRate, UNIT_10);
        setRating(movieRate.getVisualVoteAverage(), binding.visual.progressBarRating, binding.visual.textViewRate, UNIT_10);
        setRating(movieRate.getPlotVoteAverage(), binding.plot.progressBarRating, binding.plot.textViewRate, UNIT_10);
        setRating(movieRate.getAudioVoteAverage(), binding.audio.progressBarRating, binding.audio.textViewRate, UNIT_10);
    }

    private void setRating(double rating, ProgressBar pbRating, TextView tvRating, int UNIT) {

        if (rating >= GREEN_RATING * UNIT) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_green));
        } else if (rating >= YELLOW_RATING * UNIT) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),
                    R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(10 * UNIT);
        pbRating.setProgress((int) rating);
        String ratingFormatted;
        if (UNIT == UNIT_10)
            ratingFormatted = String.format("%.1f", rating);
        else ratingFormatted = String.valueOf((int) rating);
        tvRating.setText(ratingFormatted);
    }
}
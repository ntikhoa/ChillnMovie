package com.ntikhoa.chillnmovie.view;

import android.media.Rating;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.viewmodel.RatingSourceViewModel;

public class RatingSourceFragment extends Fragment {
    private String imdbId;
    private Long id;

    private static final int UNIT_10 = 1;
    private static final int UNIT_100 = 10;

    private static final Double GREEN_RATING = 8.0d;
    private static final Double YELLOW_RATING = 5.0d;

    private RatingSourceViewModel viewModel;

    private ProgressBar progressBar;

    private ProgressBar pbRatingChillnMovie;
    private TextView tvRatingChillnMovie;

    private ProgressBar pbVisualEffect;
    private TextView tvVisualEffect;

    private ProgressBar pbPlot;
    private TextView tvPlot;

    private ProgressBar pbSoundEffect;
    private TextView tvSoundEffect;

    private ProgressBar pbRatingIMDb;
    private TextView tvRatingIMDb;

    private ProgressBar pbRatingMetacritic;
    private TextView tvRatingMetacritic;

    private ProgressBar pbRatingTMDb;
    private TextView tvRatingTMDb;

    private ProgressBar pbRatingRottenTomatoes;
    private TextView tvRatingRottenTomatoes;

    public RatingSourceFragment(String imdbId, Long id) {
        this.imdbId = imdbId;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rating_source, container, false);
        initComponent(root);
        loadData();
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(RatingSourceViewModel.class);

        progressBar = root.findViewById(R.id.progress);

        View chillnMovie = root.findViewById(R.id.chillnMovie);
        pbRatingChillnMovie = chillnMovie.findViewById(R.id.progressBarRating);
        tvRatingChillnMovie = chillnMovie.findViewById(R.id.textViewRate);

        View visualEffect = root.findViewById(R.id.visualEffect);
        pbVisualEffect = visualEffect.findViewById(R.id.progressBarRating);
        tvVisualEffect = visualEffect.findViewById(R.id.textViewRate);

        View plot = root.findViewById(R.id.plot);
        pbPlot = plot.findViewById(R.id.progressBarRating);
        tvPlot = plot.findViewById(R.id.textViewRate);

        View soundEffect = root.findViewById(R.id.soundEffect);
        pbSoundEffect = soundEffect.findViewById(R.id.progressBarRating);
        tvSoundEffect = soundEffect.findViewById(R.id.textViewRate);

        View imdb = root.findViewById(R.id.imdb);
        pbRatingIMDb = imdb.findViewById(R.id.progressBarRating);
        tvRatingIMDb = imdb.findViewById(R.id.textViewRate);

        View metacritic = root.findViewById(R.id.metacritic);
        pbRatingMetacritic = metacritic.findViewById(R.id.progressBarRating);
        tvRatingMetacritic = metacritic.findViewById(R.id.textViewRate);

        View tmdb = root.findViewById(R.id.theMovieDb);
        pbRatingTMDb = tmdb.findViewById(R.id.progressBarRating);
        tvRatingTMDb = tmdb.findViewById(R.id.textViewRate);

        View rottenTomatoes = root.findViewById(R.id.rottenTomatoes);
        pbRatingRottenTomatoes = rottenTomatoes.findViewById(R.id.progressBarRating);
        tvRatingRottenTomatoes = rottenTomatoes.findViewById(R.id.textViewRate);
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        viewModel.getMLDmovieRate(id)
                .observe(this, new Observer<MovieRate>() {
                    @Override
                    public void onChanged(MovieRate movieRate) {
                        fetchChillnMovieData(movieRate);
                    }
                });

        viewModel.getMLDratingSource(imdbId)
                .observe(this, new Observer<RatingSource>() {
                    @Override
                    public void onChanged(RatingSource ratingSource) {
                        fetchData(ratingSource);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void fetchData(RatingSource ratingSource) {
        String imdbStr = ratingSource.getImDb();
        Double imdb;
        if (imdbStr != null && !imdbStr.isEmpty()) {
            imdb = Double.parseDouble(imdbStr);
            setRating(imdb, pbRatingIMDb, tvRatingIMDb, UNIT_10);
        }

        String metacriticStr = ratingSource.getMetacritic();
        Double metacritic;
        if (metacriticStr != null && !metacriticStr.isEmpty()) {
            metacritic = Double.parseDouble(metacriticStr);
            setRating(metacritic, pbRatingMetacritic, tvRatingMetacritic, UNIT_100);
        } else metacritic = 0d;

        String tmdbStr = ratingSource.getTheMovieDb();
        Double tmdb;
        if (tmdbStr != null && !tmdbStr.isEmpty()) {
            tmdb = Double.parseDouble(tmdbStr);
            setRating(tmdb, pbRatingTMDb, tvRatingTMDb, UNIT_10);
        }

        String rottenTomatoesStr = ratingSource.getRottenTomatoes();
        Double rottenTomatoes;
        if (rottenTomatoesStr != null && !rottenTomatoesStr.isEmpty()) {
            rottenTomatoes = Double.parseDouble(rottenTomatoesStr);
            setRating(rottenTomatoes, pbRatingRottenTomatoes, tvRatingRottenTomatoes, UNIT_100);
        }
    }

    private void fetchChillnMovieData(MovieRate movieRate) {
        setRating(movieRate.getVoteAverage(), pbRatingChillnMovie, tvRatingChillnMovie, UNIT_10);
        setRating(movieRate.getVisualVoteAverage(), pbVisualEffect, tvVisualEffect, UNIT_10);
        setRating(movieRate.getPlotVoteAverage(), pbPlot, tvPlot, UNIT_10);
        setRating(movieRate.getAudioVoteAverage(), pbSoundEffect, tvSoundEffect, UNIT_10);
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
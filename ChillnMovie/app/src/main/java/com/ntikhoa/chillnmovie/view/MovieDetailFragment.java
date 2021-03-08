package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentMovieDetailBinding;
import com.ntikhoa.chillnmovie.model.MovieDetail;

import java.text.DecimalFormat;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailFragment extends Fragment {

    private FragmentMovieDetailBinding binding;

    private MovieDetail movieDetail;

    public MovieDetailFragment(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

    public MovieDetailFragment() {
        super(R.layout.fragment_movie_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMovieDetailBinding.bind(view);
        setTextInfo();
    }

    private void setTextInfo() {
        String title = movieDetail.getTitle();
        setDescription(binding.textViewTitle, title);

        String originalTitle = movieDetail.getOriginalTitle();
        setDescription(binding.textViewOriginalTitle, originalTitle);

        String status = movieDetail.getStatus();
        setDescription(binding.textViewStatus, status);

        String releaseDate = movieDetail.getReleaseDate();
        setDescription(binding.textViewReleaseDate, releaseDate);

        setGenres(binding.textViewGenres);

        Integer runtime = movieDetail.getRuntime();
        if (runtime != null && runtime != 0)
            binding.textViewRuntime.setText(runtime + " ph");
        else binding.textViewRuntime.setText("-");

        String originalLanguage = movieDetail.getOriginalLanguage();
        setDescription(binding.textViewOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        setFormattedCurrency(binding.textViewBudget, budget);

        Integer revenue = movieDetail.getRevenue();
        setFormattedCurrency(binding.textViewRevenue, revenue);

        String overview = movieDetail.getOverview();
        setDescription(binding.textViewOverview, overview);
    }

    private void setDescription(TextView textView, String data) {
        if (data != null && !data.equals(""))
            textView.setText(data);
        else textView.setText(getString(R.string.default_description));
    }

    private void setGenres(TextView textViewGenres) {
        String genres;
        StringBuilder genresBuilder = new StringBuilder();
        if (movieDetail.getGenres().size() > 0) {
            for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                genresBuilder.append("-");
                genresBuilder.append(movieDetail.getGenres().get(i).getName());
                genresBuilder.append("\n");
            }
            genresBuilder.deleteCharAt(genresBuilder.length() - 1);
            genres = genresBuilder.toString();
            textViewGenres.setText(genres);
        }
    }

    private void setFormattedCurrency(TextView textViewCurrency, Integer data) {
        if (data != null && data != 0) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String revenueFormatted = "$";
            revenueFormatted += formatter.format(data);
            textViewCurrency.setText(revenueFormatted);
        } else textViewCurrency.setText("-");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
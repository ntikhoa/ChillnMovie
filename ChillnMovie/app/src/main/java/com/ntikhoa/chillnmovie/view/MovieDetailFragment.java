package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.MovieDetail;

import java.text.DecimalFormat;

public class MovieDetailFragment extends Fragment {

    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewStatus;
    private TextView textViewReleaseDate;
    private TextView textViewGenres;
    private TextView textViewRuntime;
    private TextView textViewOriginalLanguage;
    private TextView textViewBudget;
    private TextView textViewRevenue;
    private TextView textViewOverview;

    private MovieDetail movieDetail;

    public MovieDetailFragment(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initComponent(root);
        setTextInfo();
        return root;
    }

    private void initComponent(View root) {
        textViewTitle = root.findViewById(R.id.textViewTitle);
        textViewOriginalTitle = root.findViewById(R.id.textViewOriginalTitle);
        textViewStatus = root.findViewById(R.id.textViewStatus);
        textViewReleaseDate = root.findViewById(R.id.textViewReleaseDate);
        textViewGenres = root.findViewById(R.id.textViewGenres);
        textViewRuntime = root.findViewById(R.id.textViewRuntime);
        textViewOriginalLanguage = root.findViewById(R.id.textViewOriginalLanguage);
        textViewBudget = root.findViewById(R.id.textViewBudget);
        textViewRevenue = root.findViewById(R.id.textViewRevenue);
        textViewOverview = root.findViewById(R.id.textViewOverview);
    }

    private void setTextInfo() {
        String title = movieDetail.getTitle();
        setDescription(textViewTitle, title);

        String originalTitle = movieDetail.getOriginalTitle();
        setDescription(textViewOriginalTitle, originalTitle);

        String status = movieDetail.getStatus();
        setDescription(textViewStatus, status);

        String releaseDate = movieDetail.getReleaseDate();
        setDescription(textViewReleaseDate, releaseDate);

        setGenres(textViewGenres);

        Integer runtime = movieDetail.getRuntime();
        if (runtime != null && runtime != 0)
            textViewRuntime.setText(runtime + " ph");
        else textViewRuntime.setText("-");

        String originalLanguage = movieDetail.getOriginalLanguage();
        setDescription(textViewOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        setFormattedCurrency(textViewBudget, budget);

        Integer revenue = movieDetail.getRevenue();
        setFormattedCurrency(textViewRevenue, revenue);

        String overview = movieDetail.getOverview();
        setDescription(textViewOverview, overview);
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
}
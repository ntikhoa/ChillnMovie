package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.FrontPageViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FrontPageViewModel viewModel;

    MoviePagerAdapter trendingMovieAdapter;
    ViewPager2 viewPagerTrendingMovie;
    TabLayout tabLayout;

    MovieAdapter popularMovieAdapter;
    RecyclerView recyclerViewPopularMovie;

    MovieAdapter nowPlayingMovieAdapter;
    RecyclerView recyclerViewNowPlayingMovie;

    MovieAdapter upcomingMovieAdapter;
    RecyclerView recyclerViewUpcomingMovie;

    MovieAdapter topRatedMovieAdapter;
    RecyclerView recyclerViewTopRatedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        loadData();
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(FrontPageViewModel.class);

        viewPagerTrendingMovie = findViewById(R.id.viewPagerTrending);
        trendingMovieAdapter = new MoviePagerAdapter(MainActivity.this);
        viewPagerTrendingMovie.setAdapter(trendingMovieAdapter);

        tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPagerTrendingMovie, (tab, position) -> {
        }).attach();

        recyclerViewPopularMovie = findViewById(R.id.recyclerViewPopularMovie);
        recyclerViewPopularMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewPopularMovie.setAdapter(popularMovieAdapter);

        recyclerViewNowPlayingMovie = findViewById(R.id.recyclerViewNowPlaying);
        recyclerViewNowPlayingMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewNowPlayingMovie.setAdapter(nowPlayingMovieAdapter);

        recyclerViewUpcomingMovie = findViewById(R.id.recyclerViewUpcoming);
        recyclerViewUpcomingMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        upcomingMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewUpcomingMovie.setAdapter(upcomingMovieAdapter);

        recyclerViewTopRatedMovie = findViewById(R.id.recyclerViewTopRated);
        recyclerViewTopRatedMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topRatedMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewTopRatedMovie.setAdapter(topRatedMovieAdapter);
    }

    private void loadData() {
        viewModel.getMLDtrendingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        trendingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDpopularMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        popularMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDnowPlayingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        nowPlayingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDupcomingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        upcomingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDtopRatedMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        topRatedMovieAdapter.submitList(movies);
                    }
                });
    }

    public void onClickSeeMore(View view) {
        Intent intent = new Intent(this, MovieActivity.class);
        switch (view.getId()){
            case R.id.textViewMorePopular:
                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.POPULAR);
                break;
            case R.id.textViewMoreNowPlaying:
                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.NOW_PLAYING);
                break;
            case R.id.textViewMoreUpcoming:
                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.UPCOMING);
                break;
            case R.id.textViewMoreTopRated:
                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.TOP_RATED);
                break;
        }
        startActivity(intent);
    }
}
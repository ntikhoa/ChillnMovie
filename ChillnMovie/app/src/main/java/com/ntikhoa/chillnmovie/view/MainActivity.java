package com.ntikhoa.chillnmovie.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MovieViewModel movieViewModel;

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
        getData();
    }

    private void initComponent() {
        movieViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieViewModel.class);

        viewPagerTrendingMovie = findViewById(R.id.viewPagerTrending);
        trendingMovieAdapter = new MoviePagerAdapter(MainActivity.this);
        viewPagerTrendingMovie.setAdapter(trendingMovieAdapter);

        tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPagerTrendingMovie, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        }).attach();

        recyclerViewPopularMovie = findViewById(R.id.recycerViewPopularMovie);
        recyclerViewPopularMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewPopularMovie.setAdapter(popularMovieAdapter);

        recyclerViewNowPlayingMovie = findViewById(R.id.recycerViewNowPlaying);
        recyclerViewNowPlayingMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewNowPlayingMovie.setAdapter(nowPlayingMovieAdapter);

        recyclerViewUpcomingMovie = findViewById(R.id.recycerViewUpcoming);
        recyclerViewUpcomingMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        upcomingMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewUpcomingMovie.setAdapter(upcomingMovieAdapter);

        recyclerViewTopRatedMovie = findViewById(R.id.recycerViewTopRated);
        recyclerViewTopRatedMovie.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topRatedMovieAdapter = new MovieAdapter(MainActivity.this);
        recyclerViewTopRatedMovie.setAdapter(topRatedMovieAdapter);
    }

    private void getData() {
        movieViewModel.getMLDtrendingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        trendingMovieAdapter.submitList(movies);
                    }
                });

        movieViewModel.getMLDpopularMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        popularMovieAdapter.submitList(movies);
                    }
                });

        movieViewModel.getMLDnowPlayingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        nowPlayingMovieAdapter.submitList(movies);
                    }
                });

        movieViewModel.getMLDupcomingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        upcomingMovieAdapter.submitList(movies);
                    }
                });

        movieViewModel.getMLDtopRatedMovie()
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
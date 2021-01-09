package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.EditorFrontPageViewModel;

import java.util.List;

public class EditorFrontFragment extends Fragment {

    private EditorFrontPageViewModel viewModel;

    private MoviePagerAdapter trendingMovieAdapter;
    private ViewPager2 viewPagerTrendingMovie;
    private TabLayout tabLayout;

    private MovieAdapter popularMovieAdapter;
    private RecyclerView recyclerViewPopularMovie;

    private MovieAdapter nowPlayingMovieAdapter;
    private RecyclerView recyclerViewNowPlayingMovie;

    private MovieAdapter upcomingMovieAdapter;
    private RecyclerView recyclerViewUpcomingMovie;

    private MovieAdapter topRatedMovieAdapter;
    private RecyclerView recyclerViewTopRatedMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editor_front, container, false);
        initComponent(root);
        loadData();
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(EditorFrontPageViewModel.class);

        viewPagerTrendingMovie = root.findViewById(R.id.viewPagerTrending);
        trendingMovieAdapter = new MoviePagerAdapter(getActivity());
        viewPagerTrendingMovie.setAdapter(trendingMovieAdapter);

        tabLayout = root.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPagerTrendingMovie, (tab, position) -> {
        }).attach();

        recyclerViewPopularMovie = root.findViewById(R.id.recyclerViewTrendingMovie);
        recyclerViewPopularMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewPopularMovie.setAdapter(popularMovieAdapter);

        recyclerViewNowPlayingMovie = root.findViewById(R.id.recyclerViewNowPlaying);
        recyclerViewNowPlayingMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewNowPlayingMovie.setAdapter(nowPlayingMovieAdapter);

        recyclerViewUpcomingMovie = root.findViewById(R.id.recyclerViewUpcoming);
        recyclerViewUpcomingMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        upcomingMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewUpcomingMovie.setAdapter(upcomingMovieAdapter);

        recyclerViewTopRatedMovie = root.findViewById(R.id.recyclerViewTopRated);
        recyclerViewTopRatedMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topRatedMovieAdapter = new MovieAdapter(getActivity());
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

//    public void onClickSeeMore(View view) {
//        Intent intent = new Intent(this, MovieActivity.class);
//        switch (view.getId()) {
//            case R.id.textViewMoreTrending:
//                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.POPULAR);
//                break;
//            case R.id.textViewMoreNowPlaying:
//                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.NOW_PLAYING);
//                break;
//            case R.id.textViewMoreUpcoming:
//                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.UPCOMING);
//                break;
//            case R.id.textViewMoreTopRated:
//                intent.putExtra(MovieActivity.EXTRA_CATEGORY, Movie.TOP_RATED);
//                break;
//        }
//        startActivity(intent);
//    }
}
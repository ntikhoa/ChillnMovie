package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.HomePageViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomePageViewModel viewModel;

    private MovieAdapter topRatedMovieAdapter;
    private RecyclerView recyclerViewTopRatedMovie;

    private MovieAdapter nowPlayingMovieAdapter;
    private RecyclerView recyclerViewNowPlayingMovie;

    private MovieAdapter upcomingMovieAdapter;
    private RecyclerView recyclerViewUpcomingMovie;

    private MovieAdapter trendingMovieAdapter;
    private RecyclerView recyclerViewTrendingMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initComponent(root);
        loadData();
        return root;
    }

    private void initComponent(View root) {
        initViewModel();
        initRecyclerView(root);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                        getActivity().getApplication()))
                .get(HomePageViewModel.class);
    }

    private void initRecyclerView(View root) {
        recyclerViewTopRatedMovie = root.findViewById(R.id.recyclerViewTopRated);
        recyclerViewTopRatedMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false));
        topRatedMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewTopRatedMovie.setAdapter(topRatedMovieAdapter);

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

        recyclerViewTrendingMovie = root.findViewById(R.id.recyclerViewTrendingMovie);
        recyclerViewTrendingMovie.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        trendingMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewTrendingMovie.setAdapter(trendingMovieAdapter);
    }

    private void loadData() {
        viewModel.getMLDtopRatedMovie()
                .observe(getActivity(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        topRatedMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDnowPlayingMovie()
                .observe(getActivity(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        nowPlayingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDupcomingMovie()
                .observe(getActivity(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        upcomingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDtrendingMovie()
                .observe(getActivity(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        trendingMovieAdapter.submitList(movies);
                    }
                });
    }
}
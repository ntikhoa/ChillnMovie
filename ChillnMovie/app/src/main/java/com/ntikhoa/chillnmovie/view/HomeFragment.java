package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.HomePageViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomePageViewModel viewModel;

    private MoviePagerAdapter trendingMovieAdapter;
    private ViewPager2 viewPagerTrendingMovie;
    private TabLayout tabLayout;

    private MovieAdapter topRatedMovieAdapter;
    private RecyclerView recyclerViewTopRatedMovie;

    private MovieAdapter nowPlayingMovieAdapter;
    private RecyclerView recyclerViewNowPlayingMovie;

    private MovieAdapter upcomingMovieAdapter;
    private RecyclerView recyclerViewUpcomingMovie;

    private MovieAdapter vietnameseMovieAdapter;
    private RecyclerView recyclerViewVietnameseMovie;

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
        recyclerViewVietnameseMovie = root.findViewById(R.id.recyclerViewVietnamese);
        recyclerViewVietnameseMovie.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        vietnameseMovieAdapter = new MovieAdapter(getActivity());
        recyclerViewVietnameseMovie.setAdapter(vietnameseMovieAdapter);

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

        viewPagerTrendingMovie = root.findViewById(R.id.viewPagerTrending);
        trendingMovieAdapter = new MoviePagerAdapter(getActivity());
        viewPagerTrendingMovie.setAdapter(trendingMovieAdapter);
        tabLayout = root.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPagerTrendingMovie, (tab, position) -> {
        }).attach();
    }

    private void loadData() {
        viewModel.getMLDvietnameseMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        vietnameseMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDtopRatedMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        topRatedMovieAdapter.submitList(movies);
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

        viewModel.getMLDtrendingMovie()
                .observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        trendingMovieAdapter.submitList(movies);
                    }
                });
    }
}
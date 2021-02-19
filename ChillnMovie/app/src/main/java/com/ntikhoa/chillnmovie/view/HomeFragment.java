package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.common.api.Api;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.HomePageViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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

    private Button btnMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initComponent(root);
        loadData();

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initComponent(View root) {
        initViewModel();
        initRecyclerView(root);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this)
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

        btnMore = root.findViewById(R.id.btnMore);
    }

    private void loadData() {
        viewModel.getMLDvietnameseMovie()
                .observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        vietnameseMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDtopRatedMovie()
                .observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        topRatedMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDnowPlayingMovie()
                .observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        nowPlayingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDupcomingMovie()
                .observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        upcomingMovieAdapter.submitList(movies);
                    }
                });

        viewModel.getMLDtrendingMovie()
                .observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        trendingMovieAdapter.submitList(movies);
                    }
                });
    }
}
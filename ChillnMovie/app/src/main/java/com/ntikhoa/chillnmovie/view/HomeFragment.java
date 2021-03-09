package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.databinding.FragmentHomeBinding;
import com.ntikhoa.chillnmovie.viewmodel.HomePageViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomePageViewModel viewModel;

    private MoviePagerAdapter trendingMovieAdapter;
    private MovieAdapter topRatedMovieAdapter;
    private MovieAdapter nowPlayingMovieAdapter;
    private MovieAdapter upcomingMovieAdapter;
    private MovieAdapter vietnameseMovieAdapter;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);
        initComponent();
        loadData();

        binding.btnMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MovieActivity.class);
            startActivity(intent);
        });
    }

    private void initComponent() {
        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(HomePageViewModel.class);
    }

    private void initRecyclerView() {
        vietnameseMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewVietnamese.setAdapter(vietnameseMovieAdapter);

        topRatedMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewTopRated.setAdapter(topRatedMovieAdapter);

        nowPlayingMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewNowPlaying.setAdapter(nowPlayingMovieAdapter);

        upcomingMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewUpcoming.setAdapter(upcomingMovieAdapter);

        trendingMovieAdapter = new MoviePagerAdapter(getActivity());
        binding.viewPagerTrending.setAdapter(trendingMovieAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewPagerTrending, (tab, position) -> {
        }).attach();
    }

    private void loadData() {
        viewModel.getMLDvietnameseMovie()
                .observe(getViewLifecycleOwner(), movies -> vietnameseMovieAdapter.submitList(movies));

        viewModel.getMLDtopRatedMovie()
                .observe(getViewLifecycleOwner(), movies -> topRatedMovieAdapter.submitList(movies));

        viewModel.getMLDnowPlayingMovie()
                .observe(getViewLifecycleOwner(), movies -> nowPlayingMovieAdapter.submitList(movies));

        viewModel.getMLDupcomingMovie()
                .observe(getViewLifecycleOwner(), movies -> upcomingMovieAdapter.submitList(movies));

        viewModel.getMLDtrendingMovie()
                .observe(getViewLifecycleOwner(), movies -> trendingMovieAdapter.submitList(movies));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
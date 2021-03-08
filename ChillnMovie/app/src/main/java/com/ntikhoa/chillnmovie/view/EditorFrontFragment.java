package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagerAdapter;
import com.ntikhoa.chillnmovie.databinding.FragmentEditorFrontBinding;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.EditorFrontPageViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditorFrontFragment extends Fragment {

    private FragmentEditorFrontBinding binding;

    private EditorFrontPageViewModel viewModel;

    private MoviePagerAdapter trendingMovieAdapter;

    private MovieAdapter popularMovieAdapter;

    private MovieAdapter nowPlayingMovieAdapter;

    private MovieAdapter upcomingMovieAdapter;

    private MovieAdapter topRatedMovieAdapter;

    public EditorFrontFragment() {
        super(R.layout.fragment_editor_front);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditorFrontBinding.bind(view);
        initComponent();
        loadData();
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this)
                .get(EditorFrontPageViewModel.class);

        trendingMovieAdapter = new MoviePagerAdapter(getActivity());
        binding.viewPagerTrending.setAdapter(trendingMovieAdapter);

        new TabLayoutMediator(binding.tabs, binding.viewPagerTrending, (tab, position) -> {
        }).attach();

        binding.recyclerViewPopular.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewPopular.setAdapter(popularMovieAdapter);

        binding.recyclerViewNowPlaying.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewNowPlaying.setAdapter(nowPlayingMovieAdapter);

        binding.recyclerViewUpcoming.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        upcomingMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewUpcoming.setAdapter(upcomingMovieAdapter);

        binding.recyclerViewTopRated.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topRatedMovieAdapter = new MovieAdapter(getActivity());
        binding.recyclerViewTopRated.setAdapter(topRatedMovieAdapter);


        binding.textViewMoreNowPlaying.setOnClickListener(onClickMore);
        binding.textViewMorePopular.setOnClickListener(onClickMore);
        binding.textViewMoreUpcoming.setOnClickListener(onClickMore);
        binding.textViewMoreTopRated.setOnClickListener(onClickMore);
    }

    private void loadData() {
        viewModel.getMLDtrendingMovie()
                .observe(getViewLifecycleOwner(),
                        movies -> trendingMovieAdapter.submitList(movies));

        viewModel.getMLDpopularMovie()
                .observe(getViewLifecycleOwner(),
                        movies -> popularMovieAdapter.submitList(movies));

        viewModel.getMLDnowPlayingMovie()
                .observe(getViewLifecycleOwner(),
                        movies -> nowPlayingMovieAdapter.submitList(movies));

        viewModel.getMLDupcomingMovie()
                .observe(getViewLifecycleOwner(),
                        movies -> upcomingMovieAdapter.submitList(movies));

        viewModel.getMLDtopRatedMovie()
                .observe(getViewLifecycleOwner(),
                        movies -> topRatedMovieAdapter.submitList(movies));
    }

    private View.OnClickListener onClickMore = view -> {
        Intent intent = new Intent(getActivity().getApplicationContext(), MovieActivity.class);
        switch (view.getId()) {
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
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
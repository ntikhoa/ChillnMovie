package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MovieBackdropAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagedListAdapter;
import com.ntikhoa.chillnmovie.databinding.FragmentSearchBinding;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.SearchViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {
    public static final int MODE_TMDB = 0;
    public static final int MODE_FIRESTORE = 1;

    private static final String SEARCH_MODE = "search mode";

    private FragmentSearchBinding binding;

    private SearchViewModel viewModel;

    private int searchMode;

    private MoviePagedListAdapter movieAdapter;
    private MovieBackdropAdapter movieBackdropAdapter;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    public static SearchFragment newInstance(int searchMode) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(SEARCH_MODE, searchMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchMode = getArguments().getInt(SEARCH_MODE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSearchBinding.bind(view);

        initComponent();

        binding.searchView.setOnClickListener(v -> binding.searchView.setIconified(false));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    if (searchMode == MODE_TMDB) {
                        viewModel.getMLDmoviesFromTMDB(query)
                                .observe(getViewLifecycleOwner(), movies -> movieAdapter.submitList(movies));
                    } else if (searchMode == MODE_FIRESTORE) {
                        viewModel.getMLDmoviesFromFirestore(query)
                                .observe(getViewLifecycleOwner(),
                                        (Observer<List<Movie>>) movies -> movieBackdropAdapter.submitList(movies));
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this)
                .get(SearchViewModel.class);

        binding.recyclerViewSearchMovie.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        if (searchMode == MODE_TMDB) {
            movieAdapter = new MoviePagedListAdapter(getActivity());
            binding.recyclerViewSearchMovie.setAdapter(movieAdapter);
        } else if (searchMode == MODE_FIRESTORE) {
            movieBackdropAdapter = new MovieBackdropAdapter(getActivity());
            binding.recyclerViewSearchMovie.setAdapter(movieBackdropAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
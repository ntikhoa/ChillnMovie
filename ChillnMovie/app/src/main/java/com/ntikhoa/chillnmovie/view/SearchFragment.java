package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.FavoriteAdapter;
import com.ntikhoa.chillnmovie.adapter.MovieAdapter;
import com.ntikhoa.chillnmovie.adapter.MovieBackdropAdapter;
import com.ntikhoa.chillnmovie.adapter.MoviePagedListAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.SearchViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {
    public static final int MODE_TMDB = 0;
    public static final int MODE_FIRESTORE = 1;

    private static final String SEARCH_MODE = "search mode";

    private SearchViewModel viewModel;

    private int searchMode;

    private SearchView searchView;

    private RecyclerView recyclerViewSearchMovie;
    private MoviePagedListAdapter movieAdapter;
    private MovieBackdropAdapter movieBackdropAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        initComponent(root);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    if (searchMode == MODE_TMDB) {
                        viewModel.getMLDmoviesFromTMDB(query)
                                .observe(SearchFragment.this, new Observer<PagedList<Movie>>() {
                                    @Override
                                    public void onChanged(PagedList<Movie> movies) {
                                        movieAdapter.submitList(movies);
                                    }
                                });
                    } else if (searchMode == MODE_FIRESTORE) {
                        viewModel.getMLDmoviesFromFirestore(query)
                                .observe(SearchFragment.this, new Observer<List<Movie>>() {
                                    @Override
                                    public void onChanged(List<Movie> movies) {
                                        movieBackdropAdapter.submitList(movies);
                                    }
                                });
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(this)
                .get(SearchViewModel.class);

        searchView = root.findViewById(R.id.searchView);

        recyclerViewSearchMovie = root.findViewById(R.id.recyclerViewSearchMovie);
        recyclerViewSearchMovie.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        if (searchMode == MODE_TMDB) {
            movieAdapter = new MoviePagedListAdapter(getActivity());
            recyclerViewSearchMovie.setAdapter(movieAdapter);
        } else if (searchMode == MODE_FIRESTORE) {
            movieBackdropAdapter = new MovieBackdropAdapter(getActivity());
            recyclerViewSearchMovie.setAdapter(movieBackdropAdapter);
        }
    }
}
package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntikhoa.chillnmovie.R;

public class SearchFragment extends Fragment {
    public static final int MODE_TMDB = 0;
    public static final int MODE_FIRESTORE = 1;

    private static final String SEARCH_MODE = "search mode";

    private int searchMode;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
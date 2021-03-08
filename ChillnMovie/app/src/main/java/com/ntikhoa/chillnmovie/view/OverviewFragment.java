package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentOverviewBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OverviewFragment extends Fragment {
    public static final String OVERVIEW = "overview";
    public static final String TRENDING = "trending";
    public static final String UPCOMING = "upcoming";
    public static final String NOW_PLAYING = "now playing";

    private FragmentOverviewBinding binding;

    private OverviewFragment.OnClickSubmit onClickSubmit;

    private String overview;

    private boolean isTrending;
    private boolean isUpcoming;
    private boolean isNowPlaying;

    public OverviewFragment() {
        super(R.layout.fragment_overview);
    }

    public void setOnClickSubmit(OverviewFragment.OnClickSubmit onClickSubmit) {
        this.onClickSubmit = onClickSubmit;
    }

    public static OverviewFragment newInstance(String overview,
                                               boolean isTrending,
                                               boolean isUpcoming,
                                               boolean isNowPlaying) {
        Bundle args = new Bundle();
        args.putString(OVERVIEW, overview);
        args.putBoolean(TRENDING, isTrending);
        args.putBoolean(UPCOMING, isUpcoming);
        args.putBoolean(NOW_PLAYING, isNowPlaying);
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            overview = getArguments().getString(OVERVIEW);
            isTrending = getArguments().getBoolean(TRENDING, false);
            isUpcoming = getArguments().getBoolean(UPCOMING, false);
            isNowPlaying = getArguments().getBoolean(NOW_PLAYING, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentOverviewBinding.bind(view);

        binding.editTextOverview.setText(overview);
        binding.checkBoxTrending.setChecked(isTrending);
        binding.checkBoxUpcoming.setChecked(isUpcoming);
        binding.checkBoxNowPlaying.setChecked(isNowPlaying);

        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.editTextOverview.getText().toString().trim().equalsIgnoreCase("")) {
                binding.editTextOverview.setError("This field cannot be blank");
            } else
                onClickSubmit.onClick();
        });
    }

    public String getOverview() {
        return binding.editTextOverview.getText().toString();
    }

    public boolean isTrending() {
        return binding.checkBoxTrending.isChecked();
    }

    public boolean isUpcoming() {
        return binding.checkBoxUpcoming.isChecked();
    }

    public boolean isNowPlaying() {
        return binding.checkBoxNowPlaying.isChecked();
    }

    interface OnClickSubmit {
        void onClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
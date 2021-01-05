package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.ntikhoa.chillnmovie.R;

public class OverviewFragment extends Fragment {
    public static final String OVERVIEW = "overview";
    public static final String TRENDING = "trending";
    public static final String UPCOMING = "upcoming";
    public static final String NOW_PLAYING = "now playing";


    private OverviewFragment.OnClickSubmit onClickSubmit;

    private String overview;

    private MaterialCheckBox checkBoxTrending;
    private MaterialCheckBox checkBoxUpcoming;
    private MaterialCheckBox checkBoxNowPlaying;

    private boolean isTrending;
    private boolean isUpcoming;
    private boolean isNowPlaying;

    private Button btnSubmit;
    private EditText editTextOverview;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        initComponent(root);

        editTextOverview.setText(overview);
        checkBoxTrending.setChecked(isTrending);
        checkBoxUpcoming.setChecked(isUpcoming);
        checkBoxNowPlaying.setChecked(isNowPlaying);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOverview.getText().toString().trim().equalsIgnoreCase("")) {
                    editTextOverview.setError("This field cannot be blank");
                } else
                    onClickSubmit.onClick();
            }
        });

        return root;
    }

    private void initComponent(View root) {
        btnSubmit = root.findViewById(R.id.btnSubmit);
        editTextOverview = root.findViewById(R.id.editTextOverview);

        checkBoxTrending = root.findViewById(R.id.checkboxTrending);
        checkBoxNowPlaying = root.findViewById(R.id.checkboxNowPlaying);
        checkBoxUpcoming = root.findViewById(R.id.checkboxUpcoming);
    }

    public String getOverview() {
        return editTextOverview.getText().toString();
    }

    public boolean isTrending() {
        return checkBoxTrending.isChecked();
    }

    public boolean isUpcoming() {
        return checkBoxUpcoming.isChecked();
    }

    public boolean isNowPlaying() {
        return checkBoxNowPlaying.isChecked();
    }

    interface OnClickSubmit {
        void onClick();
    }
}
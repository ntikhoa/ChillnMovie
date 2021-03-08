package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentEditorFabBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditorFABFragment extends Fragment {
    public static final String MOVIE_ID = "movie id";

    private FragmentEditorFabBinding binding;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation from_bottom_vertical;
    private Animation to_bottom_vertical;
    private Animation from_bottom_horizontal;
    private Animation to_bottom_horizontal;

    private boolean clicked = false;

    private Long id;

    private OnClickFAB onClickFABadd;

    public void setOnClickFABadd(OnClickFAB onClickFABadd) {
        this.onClickFABadd = onClickFABadd;
    }

    public EditorFABFragment() {
        super(R.layout.fragment_editor_fab);
    }

    public static EditorFABFragment newInstance(Long movieId) {
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID, movieId);
        EditorFABFragment fragment = new EditorFABFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getLong(MOVIE_ID);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEditorFabBinding.bind(view);
        initAnimation();
        setOnClickFAB();
    }

    private void initAnimation() {
        rotateOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_close_anim);
        from_bottom_vertical = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom_vertical);
        to_bottom_vertical = AnimationUtils.loadAnimation(getActivity(), R.anim.to_bottom_vertical);
        from_bottom_horizontal = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom_horizontal);
        to_bottom_horizontal = AnimationUtils.loadAnimation(getActivity(), R.anim.to_bottom_horizontal);
    }

    private void setOnClickFAB() {
        binding.fabExpand.setOnClickListener(v -> {
            clicked = !clicked;
            setVisibility(clicked);
            setAnimation(clicked);
        });

        binding.fabRefresh.setOnClickListener(v ->
                new Handler().post(() -> {
                    Intent intent = getActivity().getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();

                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                }));

        binding.fabEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditMovieActivity.class);
            intent.putExtra(EditMovieActivity.EXTRA_ID, id);
            startActivity(intent);
        });

        binding.fabAdd.setOnClickListener(v -> onClickFABadd.onClick());
    }


    private void setVisibility(boolean clicked) {
        if (clicked) {
            binding.fabEdit.setVisibility(View.VISIBLE);
            binding.fabAdd.setVisibility(View.VISIBLE);
            binding.fabRefresh.setVisibility(View.VISIBLE);
            binding.fabEdit.setClickable(true);
            binding.fabAdd.setClickable(true);
            binding.fabRefresh.setClickable(true);
        } else {
            binding.fabEdit.setVisibility(View.INVISIBLE);
            binding.fabAdd.setVisibility(View.INVISIBLE);
            binding.fabRefresh.setVisibility(View.INVISIBLE);
            binding.fabEdit.setClickable(false);
            binding.fabAdd.setClickable(false);
            binding.fabRefresh.setClickable(false);
        }
    }

    private void setAnimation(boolean clicked) {
        if (clicked) {
            binding.fabExpand.startAnimation(rotateOpen);
            binding.fabEdit.startAnimation(from_bottom_vertical);
            binding.fabAdd.startAnimation(from_bottom_vertical);
            binding.fabRefresh.startAnimation(from_bottom_horizontal);
        } else {
            binding.fabExpand.startAnimation(rotateClose);
            binding.fabEdit.startAnimation(to_bottom_vertical);
            binding.fabAdd.startAnimation(to_bottom_vertical);
            binding.fabRefresh.startAnimation(to_bottom_horizontal);
        }
    }

    interface OnClickFAB {
        void onClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
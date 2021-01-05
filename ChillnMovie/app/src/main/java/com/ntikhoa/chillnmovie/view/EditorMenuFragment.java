package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntikhoa.chillnmovie.R;

public class EditorMenuFragment extends Fragment {
    public static final String MOVIE_ID = "movie id";

    private FloatingActionButton fabExpand;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabRefresh;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation from_bottom_vertical;
    private Animation to_bottom_vertical;
    private Animation from_bottom_horizontal;
    private Animation to_bottom_horizontal;

    private boolean clicked = false;

    private Integer id;

    private OnClickFAB onClickFABadd;

    public void setOnClickFABadd(OnClickFAB onClickFABadd) {
        this.onClickFABadd = onClickFABadd;
    }

    public EditorMenuFragment() {
        //require default constructor
    }

    public static EditorMenuFragment newInstance(Integer movieId) {

        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movieId);
        EditorMenuFragment fragment = new EditorMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getInt(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editor_menu, container, false);
        initComponent(root);
        setOnClickFAB();
        return root;
    }

    private void initComponent(View root) {
        initEditorMenuView(root);
        initAnimation();
    }

    private void initEditorMenuView(View root) {
        fabExpand = root.findViewById(R.id.fabExpand);
        fabAdd = root.findViewById(R.id.fabAdd);
        fabEdit = root.findViewById(R.id.fabEdit);
        fabRefresh = root.findViewById(R.id.fabRefresh);
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
        fabExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = !clicked;
                setVisibility(clicked);
                setAnimation(clicked);
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run()
                    {
                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                });
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditMovieActivity.class);
                intent.putExtra(EditMovieActivity.EXTRA_ID, id);
                startActivity(intent);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFABadd.onClick();
            }
        });
    }


    private void setVisibility(boolean clicked) {
        if (clicked) {
            fabEdit.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
            fabRefresh.setVisibility(View.VISIBLE);
            fabEdit.setClickable(true);
            fabAdd.setClickable(true);
            fabRefresh.setClickable(true);
        } else {
            fabEdit.setVisibility(View.INVISIBLE);
            fabAdd.setVisibility(View.INVISIBLE);
            fabRefresh.setVisibility(View.INVISIBLE);
            fabEdit.setClickable(false);
            fabAdd.setClickable(false);
            fabRefresh.setClickable(false);
        }
    }

    private void setAnimation(boolean clicked) {
        if (clicked) {
            fabExpand.startAnimation(rotateOpen);
            fabEdit.startAnimation(from_bottom_vertical);
            fabAdd.startAnimation(from_bottom_vertical);
            fabRefresh.startAnimation(from_bottom_horizontal);
        } else {
            fabExpand.startAnimation(rotateClose);
            fabEdit.startAnimation(to_bottom_vertical);
            fabAdd.startAnimation(to_bottom_vertical);
            fabRefresh.startAnimation(to_bottom_horizontal);
        }
    }

    interface OnClickFAB {
        void onClick();
    }
}
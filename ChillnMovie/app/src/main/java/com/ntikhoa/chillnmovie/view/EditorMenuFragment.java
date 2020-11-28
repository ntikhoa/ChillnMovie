package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.viewmodel.MovieDetailViewModel;

public class EditorMenuFragment extends Fragment {

    private MovieDetailViewModel viewModel;
    private MovieDetail movieDetail;
    private MaterialButton btnAdd, btnEdit, btnRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor_menu, container, false);
        initComponent(view);
        return view;
    }

    public void setMovieDetail(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
    }

    private void initComponent(View view) {
        viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(MovieDetailViewModel.class);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addToDatabase(movieDetail);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditMovieActivity.class);
                intent.putExtra(EditMovieActivity.EXTRA_ID, movieDetail.getId());
                startActivity(intent);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().recreate();
            }
        });
    }
}
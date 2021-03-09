package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MoviePagedListAdapter;
import com.ntikhoa.chillnmovie.databinding.ActivityMovieBinding;
import com.ntikhoa.chillnmovie.viewmodel.MovieViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;

    public static final String EXTRA_CATEGORY = "category";
    private MoviePagedListAdapter adapter;
    private MovieViewModel viewModel;
    private int category = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initComponent();
        loadData();
    }

    private void initComponent() {
        adapter = new MoviePagedListAdapter(MovieActivity.this);
        binding.recyclerViewMovie.setAdapter(adapter);
        viewModel = new ViewModelProvider(this)
                .get(MovieViewModel.class);
    }

    private void loadData() {
        category = getIntent().getIntExtra(EXTRA_CATEGORY, -1);
        if (category != -1)
            viewModel.getMoviePagedListLiveData(category)
                    .observe(this, movies -> adapter.submitList(movies));
        else viewModel.getMoviePagedListFirestore()
                .observe(this, movies -> adapter.submitList(movies));
    }
}
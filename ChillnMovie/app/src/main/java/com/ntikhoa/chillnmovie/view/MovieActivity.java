package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.MoviePagedListAdapter;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.viewmodel.MovieViewModel;

public class MovieActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "category";
    private MoviePagedListAdapter adapter;
    private RecyclerView recyclerView;
    private MovieViewModel viewModel;
    private int category = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        initComponent();
        loadData();
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerViewMovie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MoviePagedListAdapter(MovieActivity.this);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieViewModel.class);
    }

    private void loadData() {
        category = getIntent().getIntExtra(EXTRA_CATEGORY, -1);
        if (category != -1)
            viewModel.getMoviePagedListLiveData(category)
                    .observe(this, movies -> adapter.submitList(movies));
        else viewModel.getMovieFirestore()
                .observe(this, new Observer<PagedList<Movie>>() {
                    @Override
                    public void onChanged(PagedList<Movie> movies) {
                        adapter.submitList(movies);
                    }
                });
    }
}
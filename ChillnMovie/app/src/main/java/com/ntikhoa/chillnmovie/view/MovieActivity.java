package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
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
    private MovieViewModel movieViewModel;
    private int category = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        initComponent();

        getData();
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerViewMovie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MoviePagedListAdapter();
        recyclerView.setAdapter(adapter);
        movieViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieViewModel.class);
    }

    private void getData() {
        category = getIntent().getIntExtra(EXTRA_CATEGORY, -1);
        movieViewModel.getMoviePagedListLiveData(category)
                .observe(this, new Observer<PagedList<Movie>>() {
                    @Override
                    public void onChanged(PagedList<Movie> movies) {
                        adapter.submitList(movies);
                    }
                });
    }
}
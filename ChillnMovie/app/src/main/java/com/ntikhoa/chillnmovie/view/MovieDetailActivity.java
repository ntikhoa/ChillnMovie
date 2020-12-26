package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.CasterAdapter;
import com.ntikhoa.chillnmovie.adapter.UserRateAdapter;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_ID = "tmdb_movie_id";

    private MovieDetailViewModel viewModel;
    private FrameLayout fragmentContainer;

    private RecyclerView recyclerViewCaster;
    private CasterAdapter casterAdapter;

    private RecyclerView recyclerViewUserRate;
    private UserRateAdapter userRateAdapter;

    private int id;
    private MovieDetail movieDetail;

    private MaterialButton btnPlayTrailer;
    private ImageButton btnRateMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initComp();
        loadData();

        btnPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    Intent intent = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                    intent.putExtra(TrailerPlayerActivity.EXTRA_TRAILER_KEY, movieDetail.getTrailer_key());
                    startActivity(intent);
                }
            }
        });

        btnRateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    Intent intent = new Intent(MovieDetailActivity.this, RateMovieActivity.class);
                    intent.putExtra(RateMovieActivity.EXTRA_ID, movieDetail.getId());
                    intent.putExtra(RateMovieActivity.EXTRA_POSTER_PATH, movieDetail.getPosterPath());
                    intent.putExtra(RateMovieActivity.EXTRA_TITLE, movieDetail.getTitle());
                    startActivity(intent);
                }
            }
        });
    }

    private void initComp() {
        initViewModel();
        initRecyclerView();
        initGeneralView();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(MovieDetailActivity.this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieDetailViewModel.class);
    }

    private void initRecyclerView() {
        recyclerViewCaster = findViewById(R.id.recyclerViewCaster);
        recyclerViewCaster.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        casterAdapter = new CasterAdapter(this);
        recyclerViewCaster.setAdapter(casterAdapter);

        recyclerViewUserRate = findViewById(R.id.recyclerViewUserRate);
        recyclerViewUserRate.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        userRateAdapter = new UserRateAdapter();
        recyclerViewUserRate.setAdapter(userRateAdapter);
    }

    private void initGeneralView() {
        fragmentContainer = findViewById(R.id.fragmentContainer);
        btnPlayTrailer = findViewById(R.id.btnPlayTrailer);
        btnRateMovie = findViewById(R.id.btnRate);
    }

    private void loadData() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        movieDetail = viewModel.getMLDmovieDetail(id).getValue();
        viewModel.getMLDmovieDetail(id).observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(MovieDetail movieDetail) {
                MovieDetailActivity.this.movieDetail = movieDetail;
                setBackground(movieDetail);
                setHeaderFragment(movieDetail);
                setMovieInfoFragment(movieDetail);
                setEditorMenu(movieDetail);
            }
        });

        viewModel.getMLDcaster(id).observe(this, new Observer<List<Caster>>() {
            @Override
            public void onChanged(List<Caster> casters) {
                casterAdapter.submitList(casters);
            }
        });

        viewModel.getMLDuserRate(id).observe(this, new Observer<List<UserRate>>() {
            @Override
            public void onChanged(List<UserRate> userRates) {
                userRateAdapter.submitList(userRates);
            }
        });
    }

    private void setHeaderFragment(MovieDetail movieDetail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieHeaderFragment fragment = new MovieHeaderFragment(movieDetail.getBackdropPath(),
                movieDetail.getTitle(),
                movieDetail.getVoteAverage());
        ft.add(R.id.fragmentMovieHeader, fragment);
        ft.commit();

        fragment.setOnClickPBrating(new MovieHeaderFragment.OnClickPBrating() {
            @Override
            public void onClick() {
                if (movieDetail.getImdbId() != null && !movieDetail.getImdbId().isEmpty()) {
                    RatingSourceFragment fragment = new RatingSourceFragment(movieDetail.getImdbId(), movieDetail.getId());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
    }

    private void setMovieInfoFragment(MovieDetail movieDetail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieDetailFragment fragment = new MovieDetailFragment(movieDetail);
        ft.add(R.id.fragmentMovieInfo, fragment);
        ft.commit();
    }

    private void setBackground(MovieDetail movieDetail) {
        String posterUrl = Movie.path + movieDetail.getPosterPath();
        Picasso.get()
                .load(posterUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        fragmentContainer.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }
                });
    }

    private void setEditorMenu(MovieDetail movieDetail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditorMenuFragment fragment = new EditorMenuFragment(movieDetail.getId());
        ft.add(R.id.fragmentEditorMenu, fragment);
        ft.commit();

        fragment.setOnClickFABadd(new EditorMenuFragment.OnClickFAB() {
            @Override
            public void onClick() {
                viewModel.addToDatabase(movieDetail);
            }
        });
    }
}
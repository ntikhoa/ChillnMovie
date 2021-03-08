package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.CasterAdapter;
import com.ntikhoa.chillnmovie.adapter.ReviewViewPagerAdapter;
import com.ntikhoa.chillnmovie.databinding.ActivityMovieDetailBinding;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserModeSingleton;
import com.ntikhoa.chillnmovie.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_ID = "tmdb_movie_id";

    private ActivityMovieDetailBinding binding;

    private MovieDetailViewModel viewModel;

    private CasterAdapter casterAdapter;

    private ReviewViewPagerAdapter pagerAdapter;

    private long id;
    private MovieDetail movieDetail;

    @Inject
    FirebaseAuth auth;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mode = ((UserModeSingleton) getApplicationContext()).getMode();

        initComponent();
        loadData();

        binding.btnPlayTrailer.setOnClickListener(v -> {
            if (movieDetail != null) {
                if (movieDetail.getTrailer_key() != null && !movieDetail.getTrailer_key().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                    intent.putExtra(TrailerPlayerActivity.EXTRA_TRAILER_KEY, movieDetail.getTrailer_key());
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "Trailer is not available", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRate.setOnClickListener(v -> {
            if (movieDetail != null && auth.getCurrentUser() != null) {
                Intent intent = new Intent(MovieDetailActivity.this, RateMovieActivity.class);
                intent.putExtra(RateMovieActivity.EXTRA_ID, movieDetail.getId());
                intent.putExtra(RateMovieActivity.EXTRA_POSTER_PATH, movieDetail.getPosterPath());
                intent.putExtra(RateMovieActivity.EXTRA_TITLE, movieDetail.getTitle());
                startActivity(intent);
            }
        });

        binding.btnAddToFavorite.setOnClickListener(v -> {
            if (movieDetail != null && auth.getCurrentUser() != null) {
                viewModel.addToFavorite(auth.getCurrentUser().getUid(), id);
            }
        });
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(MovieDetailActivity.this)
                .get(MovieDetailViewModel.class);

        binding.recyclerViewCaster.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        casterAdapter = new CasterAdapter(this);
        binding.recyclerViewCaster.setAdapter(casterAdapter);
    }

    private void loadData() {
        id = getIntent().getLongExtra(EXTRA_ID, -1);
        movieDetail = viewModel.getMLDmovieDetail(id).getValue();
        viewModel.getMLDmovieDetail(id).observe(this, movieDetail -> {
            MovieDetailActivity.this.movieDetail = movieDetail;
            setBackground(movieDetail.getPosterPath());
            setHeaderFragment(movieDetail.getBackdropPath(), movieDetail.getTitle(), movieDetail.getVoteAverage());
            setMovieInfoFragment(movieDetail);
            setViewPagerReview(movieDetail.getId(), movieDetail.getPosterPath());
            if (mode == UserAccount.EDITOR)
                setEditorMenu(movieDetail.getId());

            //for testing
            Toast.makeText(getApplicationContext(), String.valueOf(movieDetail.getId()), Toast.LENGTH_LONG).show();
        });

        viewModel.getMLDcaster(id).observe(this, casters -> casterAdapter.submitList(casters));
    }

    private void setViewPagerReview(Long movieId, String posterPath) {
        pagerAdapter = new ReviewViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                getApplicationContext(),
                movieId);
        binding.viewPagerReview.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPagerReview);
        pagerAdapter.setOnClickListener(() -> {
            Intent intent = new Intent(getApplicationContext(), UserRateActivity.class);
            intent.putExtra(UserRateActivity.MOVIE_ID, movieId);
            intent.putExtra(UserRateActivity.POSTER, posterPath);
            startActivity(intent);
        });
    }

    private void setHeaderFragment(String backdropPath, String title, Double voteAverage) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieHeaderFragment fragment = MovieHeaderFragment.newInstance(backdropPath, title, voteAverage);
        ft.add(R.id.fragmentMovieHeader, fragment);
        ft.commit();

        fragment.setOnClickPBrating(() -> {
            if (movieDetail.getImdbId() != null && !movieDetail.getImdbId().isEmpty()) {
                RatingSourceFragment fragment1 = RatingSourceFragment.newInstance(movieDetail.getImdbId(), movieDetail.getId());
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.add(R.id.fragmentContainer, fragment1);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft1.addToBackStack(null);
                ft1.commit();
            }
        });
    }

    private void setMovieInfoFragment(MovieDetail movieDetail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MovieDetailFragment fragment = new MovieDetailFragment(movieDetail);
        ft.add(R.id.fragmentMovieInfo, fragment);
        ft.commit();
    }

    private void setBackground(String posterPath) {
        Picasso.get()
                .load(posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        binding.fragmentContainer.setBackground(new BitmapDrawable(getResources(), bitmap));
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

    private void setEditorMenu(Long movieId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditorFABFragment fragment = EditorFABFragment.newInstance(movieId);
        ft.add(R.id.fragmentEditorMenu, fragment);
        ft.commit();

        fragment.setOnClickFABadd(() -> viewModel.addToDatabase(movieDetail));
    }
}
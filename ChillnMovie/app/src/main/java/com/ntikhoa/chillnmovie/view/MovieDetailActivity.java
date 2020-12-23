package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.CasterAdapter;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_ID = "tmdb_movie_id";

    private MovieDetailViewModel viewModel;
    private FrameLayout fragmentContainer;
    private ImageView imageViewBackdrop;

    private RecyclerView recyclerViewCaster;
    private CasterAdapter casterAdapter;

    private int id;
    private MovieDetail movieDetail;

    private MaterialButton btnPlayTrailer;
    private ImageButton btnRateMovie;

    private TextView textViewTitle;
    private ProgressBar pbRating;
    private TextView textViewRating;

    private TextView textViewDesTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewStatus;
    private TextView textViewReleaseDate;
    private TextView textViewGenres;
    private TextView textViewRuntime;
    private TextView textViewOriginalLanguage;
    private TextView textViewBudget;
    private TextView textViewRevenue;
    private TextView textViewOverview;

    private FloatingActionButton fabExpand;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabRefresh;

    Animation rotateOpen;
    Animation rotateClose;
    Animation from_bottom_vertical;
    Animation to_bottom_vertical;
    Animation from_bottom_horizontal;
    Animation to_bottom_horizontal;

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initComp();
        loadData();

        setOnClickFAB();

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

        pbRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null &&
                        movieDetail.getImdbId() != null &&
                        !movieDetail.getImdbId().isEmpty()) {
                    RatingSourceFragment fragment = new RatingSourceFragment(movieDetail.getImdbId(), movieDetail.getId());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        btnRateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    Intent intent = new Intent(MovieDetailActivity.this, RateMovieActivity.class);
                    intent.putExtra(RateMovieActivity.EXTRA_ID, movieDetail.getId());
                    startActivity(intent);
                }
            }
        });
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
                recreate();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    Intent intent = new Intent(MovieDetailActivity.this, EditMovieActivity.class);
                    intent.putExtra(EditMovieActivity.EXTRA_ID, movieDetail.getId());
                    startActivity(intent);
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addToDatabase(movieDetail);
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

    private void initComp() {
        initViewModel();
        initRecyclerView();
        initDescriptionView();
        initGeneralView();
        initEditorMenuView();
        initAnimation();
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
    }

    private void initGeneralView() {
        fragmentContainer = findViewById(R.id.fragmentContainer);
        imageViewBackdrop = findViewById(R.id.imageViewBackdrop);
        textViewTitle = findViewById(R.id.editTextTitle);
        btnPlayTrailer = findViewById(R.id.btnPlayTrailer);
        btnRateMovie = findViewById(R.id.btnRate);
        View root = findViewById(R.id.ratingView);
        pbRating = root.findViewById(R.id.progressBarRating);
        textViewRating = root.findViewById(R.id.textViewRating);
    }

    private void initDescriptionView() {
        View rootView = findViewById(R.id.layout_description);
        textViewDesTitle = rootView.findViewById(R.id.textViewTitle);
        textViewOriginalTitle = rootView.findViewById(R.id.textViewOriginalTitle);
        textViewStatus = rootView.findViewById(R.id.textViewStatus);
        textViewReleaseDate = rootView.findViewById(R.id.textViewReleaseDate);
        textViewGenres = rootView.findViewById(R.id.textViewGenres);
        textViewRuntime = rootView.findViewById(R.id.textViewRuntime);
        textViewOriginalLanguage = rootView.findViewById(R.id.textViewOriginalLanguage);
        textViewBudget = rootView.findViewById(R.id.textViewBudget);
        textViewRevenue = rootView.findViewById(R.id.textViewRevenue);
        textViewOverview = rootView.findViewById(R.id.textViewOverview);
    }

    private void initEditorMenuView() {
        View root = findViewById(R.id.layoutEditorMenu);
        fabExpand = root.findViewById(R.id.fabExpand);
        fabAdd = root.findViewById(R.id.fabAdd);
        fabEdit = root.findViewById(R.id.fabEdit);
        fabRefresh = root.findViewById(R.id.fabRefresh);
    }

    private void initAnimation() {
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        from_bottom_vertical = AnimationUtils.loadAnimation(this, R.anim.from_bottom_vertical);
        to_bottom_vertical = AnimationUtils.loadAnimation(this, R.anim.to_bottom_vertical);
        from_bottom_horizontal = AnimationUtils.loadAnimation(this, R.anim.from_bottom_horizontal);
        to_bottom_horizontal = AnimationUtils.loadAnimation(this, R.anim.to_bottom_horizontal);
    }

    private void loadData() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        movieDetail = viewModel.getMLDmovieDetail(id).getValue();
        viewModel.getMLDmovieDetail(id).observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(MovieDetail movieDetail) {
                MovieDetailActivity.this.movieDetail = movieDetail;
                setBackground(movieDetail);
                setBackdropImage(movieDetail);
                setTextInfo(movieDetail);
                setRating(movieDetail);
            }
        });

        viewModel.getMLDcaster(id).observe(this, new Observer<List<Caster>>() {
            @Override
            public void onChanged(List<Caster> casters) {
                casterAdapter.submitList(casters);
            }
        });
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

    private void setBackdropImage(MovieDetail movieDetail) {
        String backdropUrl = Movie.path + movieDetail.getBackdropPath();
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(getApplicationContext(), R.color.colorShimmerBase))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(getApplicationContext(), R.color.colorShimmerHighlight))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .setDuration(500)
                .build();
        ShimmerDrawable drawable = new ShimmerDrawable();
        drawable.setShimmer(shimmer);
        Picasso.get()
                .load(backdropUrl)
                .placeholder(drawable)
                .into(imageViewBackdrop);
        textViewTitle.setText(movieDetail.getTitle());
    }

    private void setRating(MovieDetail movieDetail) {
        double rating = movieDetail.getVoteAverage();

        if (rating >= 8.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_green));
        } else if (rating >= 5.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(10);
        pbRating.setProgress((int) rating);
        String ratingFormatted = String.format("%.1f", rating);
        textViewRating.setText(ratingFormatted);
    }

    private void setTextInfo(MovieDetail movieDetail) {
        String title = movieDetail.getTitle();
        setDescription(textViewDesTitle, title);

        String originalTitle = movieDetail.getOriginalTitle();
        setDescription(textViewOriginalTitle, originalTitle);

        String status = movieDetail.getStatus();
        setDescription(textViewStatus, status);

        String releaseDate = movieDetail.getReleaseDate();
        setDescription(textViewReleaseDate, releaseDate);

        setGenres(textViewGenres);

        Integer runtime = movieDetail.getRuntime();
        if (runtime != null && runtime != 0)
            textViewRuntime.setText(runtime + " ph");
        else textViewRuntime.setText("-");

        String originalLanguage = movieDetail.getOriginalLanguage();
        setDescription(textViewOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        setFormattedCurrency(textViewBudget, budget);

        Integer revenue = movieDetail.getRevenue();
        setFormattedCurrency(textViewRevenue, revenue);

        String overview = movieDetail.getOverview();
        setDescription(textViewOverview, overview);
    }

    private void setDescription(TextView textView, String data) {
        if (data != null && !data.equals(""))
            textView.setText(data);
        else textView.setText(getString(R.string.default_description));
    }

    private void setGenres(TextView textViewGenres) {
        String genres;
        StringBuilder genresBuilder = new StringBuilder();
        if (movieDetail.getGenres().size() > 0) {
            for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                genresBuilder.append("-");
                genresBuilder.append(movieDetail.getGenres().get(i).getName());
                genresBuilder.append("\n");
            }
            genresBuilder.deleteCharAt(genresBuilder.length() - 1);
            genres = genresBuilder.toString();
            textViewGenres.setText(genres);
        }
    }

    private void setFormattedCurrency(TextView textViewCurrency, Integer data) {
        if (data != null && data != 0) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String revenueFormatted = "$";
            revenueFormatted += formatter.format(data);
            textViewCurrency.setText(revenueFormatted);
        } else textViewCurrency.setText("-");
    }
}
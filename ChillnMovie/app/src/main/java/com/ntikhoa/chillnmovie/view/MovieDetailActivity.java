package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
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

    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initComp();
        loadData();

        FloatingActionButton btn = findViewById(R.id.fabAddToDb);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
                EditorMenuFragment editorMenuFragment = new EditorMenuFragment();
                editorMenuFragment.setMovieDetail(movieDetail);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragmentContainer, editorMenuFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }


    private void initComp() {
        viewModel = new ViewModelProvider(MovieDetailActivity.this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieDetailViewModel.class);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        imageViewBackdrop = findViewById(R.id.imageViewBackdrop);

        recyclerViewCaster = findViewById(R.id.recyclerViewCaster);
        recyclerViewCaster.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        casterAdapter = new CasterAdapter(this);
        recyclerViewCaster.setAdapter(casterAdapter);

        textViewTitle = findViewById(R.id.editTextTitle);
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

    private void setTextInfo(MovieDetail movieDetail) {
        View rootView = findViewById(R.id.layout_description);
        TextView textViewTitle = rootView.findViewById(R.id.editTextTitle);
        TextView textViewOriginalTitle = rootView.findViewById(R.id.editTextOriginalTitle);
        TextView textViewStatus = rootView.findViewById(R.id.editTextStatus);
        TextView textViewReleaseDate = rootView.findViewById(R.id.editTextReleaseDate);
        TextView textViewGenres = rootView.findViewById(R.id.editTextGenres);
        TextView textViewRuntime = rootView.findViewById(R.id.editTextRuntime);
        TextView textViewOriginalLanguage = rootView.findViewById(R.id.editTextOriginalLanguage);
        TextView textViewBudget = rootView.findViewById(R.id.editTextBudget);
        TextView textViewRevenue = rootView.findViewById(R.id.editTextRevenue);
        TextView textViewOverview = rootView.findViewById(R.id.editTextOverview);

        String title = movieDetail.getTitle();
        setDescription(textViewTitle, title);

        String originalTitle = movieDetail.getOriginalTitle();
        setDescription(textViewOriginalTitle, originalTitle);


        String status = movieDetail.getStatus();
        setDescription(textViewStatus, status);


        String releaseDate = movieDetail.getReleaseDate();
        setDescription(textViewReleaseDate, releaseDate);

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

        Integer runtime = movieDetail.getRuntime();
        if (runtime != null && runtime != 0)
            textViewRuntime.setText(runtime + " ph");
        else textViewRuntime.setText("-");

        String originalLanguage = movieDetail.getOriginalLanguage();
        setDescription(textViewOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        if (budget != null && budget != 0) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String budgetFormatted = "$";
            budgetFormatted += formatter.format(budget);
            textViewBudget.setText(budgetFormatted);
        } else textViewBudget.setText("-");

        Integer revenue = movieDetail.getRevenue();
        if (revenue != null && revenue != 0) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String revenueFormatted = "$";
            revenueFormatted += formatter.format(revenue);
            textViewRevenue.setText(revenueFormatted);
        } else textViewRevenue.setText("-");

        String overview = movieDetail.getOverview();
        setDescription(textViewOverview, overview);
    }

    private void setDescription(TextView textView, String data) {
        if (data != null && !data.equals(""))
            textView.setText(data);
        else textView.setText(getString(R.string.default_description));
    }
}
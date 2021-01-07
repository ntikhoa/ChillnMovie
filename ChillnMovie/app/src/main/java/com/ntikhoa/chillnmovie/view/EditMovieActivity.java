package com.ntikhoa.chillnmovie.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Genre;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.viewmodel.EditMovieViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class EditMovieActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "tmdb_movie_id";

    public static final int REQUEST_POSTER = 1;
    public static final int REQUEST_BACKDROP = 2;

    private EditMovieViewModel viewModel;

    private EditText editTextTitle;
    private EditText editTextOriginalTitle;
    private EditText editTextStatus;
    private EditText editTextReleaseDate;
    private TextView textViewGenres;
    private EditText editTextTrailer;
    private EditText editTextRuntime;
    private EditText editTextOriginalLanguage;
    private EditText editTextBudget;
    private EditText editTextRevenue;

    private MaterialButton btnNext;
    private MaterialButton btnCancel;
    private MaterialButton btnEditGenres;
    private MaterialButton btnPlayTrailer;
    private ImageButton btnHelp;

    private MaterialButton btnSetPoster;
    private MaterialButton btnSetBackdrop;
    private MaterialButton btnPreviewPoster;
    private MaterialButton btnPreviewBackdrop;

    private Uri poster, backdrop;

    private Boolean isTrending = false;
    private Boolean isUpcoming = false;
    private Boolean isNowPlaying = false;
    private Boolean isVietnamese = false;

    private String[] listGenres;
    private boolean[] checkedGenres;
    private ArrayList<Integer> indexGenres = new ArrayList<>();
    private List<Genre> newGenres;

    private MovieDetail movieDetail;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        initComponent();
        loadData();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null)
                    editGenres();
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTrailer.setError(getString(R.string.help), null);
            }
        });

        btnPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    String videoKey = editTextTrailer.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), TrailerPlayerActivity.class);
                    intent.putExtra(TrailerPlayerActivity.EXTRA_TRAILER_KEY, videoKey);
                    startActivity(intent);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    OverviewFragment fragment = OverviewFragment.newInstance(
                            movieDetail.getOverview(), isTrending, isUpcoming, isNowPlaying);
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    setOnSubmitBtn(fragment);
                }
            }
        });

        btnSetPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_POSTER);
            }
        });

        btnPreviewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poster != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    PreviewFragment fragment = PreviewFragment.newInstance(poster, PreviewFragment.POSTER);
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    fragment.setOnClickListener(new PreviewFragment.onClickListener() {
                        @Override
                        public void onClick(int mode) {
                            viewModel.uploadImage(poster, PreviewFragment.POSTER)
                                    .observe(EditMovieActivity.this, new Observer<String>() {
                                        @Override
                                        public void onChanged(String url) {
                                            movieDetail.setPosterPath(url);
                                            getSupportFragmentManager().popBackStack();
                                        }
                                    });
                        }
                    });
                }
            }
        });

        btnSetBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_BACKDROP);
            }
        });

        btnPreviewBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backdrop != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    PreviewFragment fragment = PreviewFragment.newInstance(backdrop, PreviewFragment.BACKDROP);
                    ft.add(R.id.fragmentContainer, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    fragment.setOnClickListener(new PreviewFragment.onClickListener() {
                        @Override
                        public void onClick(int mode) {
                            viewModel.uploadImage(backdrop, PreviewFragment.BACKDROP)
                                    .observe(EditMovieActivity.this, new Observer<String>() {
                                        @Override
                                        public void onChanged(String url) {
                                            movieDetail.setBackdropPath(url);
                                            getSupportFragmentManager().popBackStack();
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POSTER && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                poster = data.getData();
            }
        } else if (requestCode == REQUEST_BACKDROP && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                backdrop = data.getData();
            }
        }
    }

    private void setOnSubmitBtn(OverviewFragment fragment) {
        //updateData();
        fragment.setOnClickSubmit(new OverviewFragment.OnClickSubmit() {
            @Override
            public void onClick() {
                String title = editTextTitle.getText().toString();
                if (editTextTitle.getText().toString().trim().equalsIgnoreCase("")) {
                    editTextTitle.setError("This field cannot be blank");
                    return;
                }

                String originalTitle = editTextOriginalTitle.getText().toString();
                String status = editTextStatus.getText().toString();
                String videoKey = editTextTrailer.getText().toString();

                String releaseDate = editTextReleaseDate.getText().toString();
                if (!releaseDate.isEmpty() && !validate(releaseDate)) {
                    editTextReleaseDate.setError("Invalid input");
                    return;
                }

                int runtime;
                if (editTextRuntime.getText().toString().trim().equalsIgnoreCase("")) {
                    runtime = 0;
                } else runtime = Integer.parseInt(editTextRuntime.getText().toString());

                String originalLanguage = editTextOriginalLanguage.getText().toString();

                int budget;
                if (editTextBudget.getText().toString().trim().equalsIgnoreCase("")) {
                    budget = 0;
                } else budget = Integer.parseInt(editTextBudget.getText().toString());

                int revenue;
                if (editTextRevenue.getText().toString().trim().equalsIgnoreCase("")) {
                    revenue = 0;
                } else revenue = Integer.parseInt(editTextRevenue.getText().toString());

                String overview = fragment.getOverview();

                if (newGenres.size() < 1) {
                    textViewGenres.requestFocus();
                    textViewGenres.setError("Cannot empty");
                    return;
                }

                isTrending = fragment.isTrending();
                isUpcoming = fragment.isUpcoming();
                isNowPlaying = fragment.isNowPlaying();

                movieDetail.setTitle(title);
                movieDetail.setOriginalTitle(originalTitle);
                movieDetail.setStatus(status);
                movieDetail.setReleaseDate(releaseDate);
                movieDetail.setTrailer_key(videoKey);
                movieDetail.setRuntime(runtime);
                movieDetail.setOriginalLanguage(originalLanguage);
                movieDetail.setBudget(budget);
                movieDetail.setRevenue(revenue);
                movieDetail.setOverview(overview);
                movieDetail.setGenres(newGenres);

                viewModel.updateToDatabase(movieDetail,
                        isTrending,
                        isUpcoming,
                        isNowPlaying)
                        .observe(EditMovieActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean success) {
                                if (success) {
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void initComponent() {
        initViewModel();
        initEditText();
        initBtn();
        initGenresList();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(EditMovieViewModel.class);
    }

    private void initEditText() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextOriginalTitle = findViewById(R.id.editTextOriginalTitle);
        editTextStatus = findViewById(R.id.editTextStatus);
        editTextReleaseDate = findViewById(R.id.editTextReleaseDate);
        textViewGenres = findViewById(R.id.textViewGenres);
        editTextTrailer = findViewById(R.id.editTextTrailer);
        editTextRuntime = findViewById(R.id.editTextRuntime);
        editTextOriginalLanguage = findViewById(R.id.editTextCountry);
        editTextBudget = findViewById(R.id.editTextBudget);
        editTextRevenue = findViewById(R.id.editTextRevenue);
    }

    private void initBtn() {
        btnNext = findViewById(R.id.btnNext);
        btnCancel = findViewById(R.id.btnSkip);
        btnEditGenres = findViewById(R.id.btnEditGenres);
        btnPlayTrailer = findViewById(R.id.btnPlayTrailer);
        btnHelp = findViewById(R.id.btnHelp);

        btnSetPoster = findViewById(R.id.btnSetPoster);
        btnSetBackdrop = findViewById(R.id.btnSetBackdrop);
        btnPreviewPoster = findViewById(R.id.btnPreviewPoster);
        btnPreviewBackdrop = findViewById(R.id.btnPreviewBackdrop);
    }

    private void initGenresList() {
        listGenres = getResources().getStringArray(R.array.genres);
        checkedGenres = new boolean[listGenres.length];
    }

    private void loadData() {
        id = getIntent().getLongExtra(EXTRA_ID, -1);
        if (id != -1) {
            viewModel.getMLDmovieDetail(id)
                    .observe(this, movieDetail -> {
                        EditMovieActivity.this.movieDetail = movieDetail;
                        setBackground(movieDetail.getPosterPath());
                        fetchData(movieDetail);
                        fetchCheckBox(movieDetail.getId());
                    });
        }
        else {
            movieDetail = new MovieDetail((int) System.currentTimeMillis());
            movieDetail.set
            isVietnamese = true;
        }
    }

    private void fetchData(MovieDetail movieDetail) {
        String title = movieDetail.getTitle();
        fetchViewString(editTextTitle, title);

        String originalTitle = movieDetail.getOriginalTitle();
        fetchViewString(editTextOriginalTitle, originalTitle);

        String status = movieDetail.getStatus();
        fetchViewString(editTextStatus, status);

        String releaseDate = movieDetail.getReleaseDate();
        fetchViewString(editTextReleaseDate, releaseDate);

        fetchGenres();

        String videoKey = movieDetail.getTrailer_key();
        fetchViewString(editTextTrailer, videoKey);

        Integer runtime = movieDetail.getRuntime();
        fetchViewInteger(editTextRuntime, runtime);

        String originalLanguage = movieDetail.getOriginalLanguage();
        fetchViewString(editTextOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        fetchViewInteger(editTextBudget, budget);

        Integer revenue = movieDetail.getRevenue();
        fetchViewInteger(editTextRevenue, revenue);
    }

    private void fetchCheckBox(Long id) {
        viewModel.isTrendingExist(movieDetail.getId())
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isExist) {
                        isTrending = isExist;
                    }
                });

        viewModel.isNowPlayingExist(movieDetail.getId())
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isExist) {
                        isNowPlaying = isExist;
                    }
                });


        viewModel.isUpcomingExist(movieDetail.getId())
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isExist) {
                        isUpcoming = isExist;
                    }
                });
    }

    private void fetchViewString(EditText editText, String data) {
        if (data != null)
            editText.setText(data);
    }

    private void fetchViewInteger(EditText editText, Integer data) {
        if (data != null && data != 0) {
            editText.setText(String.valueOf(data));
        }
    }

    private void fetchGenres() {
        String genres;
        StringBuilder genresBuilder = new StringBuilder();
        if (movieDetail.getGenres().size() > 0) {
            newGenres = movieDetail.getGenres();
            for (int i = 0; i < movieDetail.getGenres().size(); i++) {
                genresBuilder.append("-");
                genresBuilder.append(movieDetail.getGenres().get(i).getName());
                genresBuilder.append("\n");
                for (int j = 0; j < listGenres.length; j++) {
                    if (movieDetail.getGenres().get(i).getName().equals(listGenres[j])) {
                        indexGenres.add(j);
                        checkedGenres[j] = true;
                        break;
                    }
                }
            }
            genresBuilder.deleteCharAt(genresBuilder.length() - 1);
            genres = genresBuilder.toString();
            textViewGenres.setText(genres);
        } else textViewGenres.setText("-");
    }

    private void updateData() {

    }

    private boolean validate(String date) {
        String[] t = date.split("-");
        if (t.length != 3)
            return false;
        int year = Integer.parseInt(t[0]);
        int month = Integer.parseInt(t[1]);
        int day = Integer.parseInt(t[2]);
        if (month < 10 && !t[1].contains("0"))
            return false;
        if (day < 10 && !t[2].contains("0"))
            return false;
        if (year < 1900)
            return false;
        if (month < 1 || month > 12)
            return false;
        if (day < 1 || day > 31)
            return false;

        return true;
    }

    private void setBackground(String posterPath) {
        Picasso.get()
                .load(posterPath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        FrameLayout fragmentContainer = findViewById(R.id.fragmentContainer);
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

    private void editGenres() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Edit genres");
        builder.setMultiChoiceItems(listGenres, checkedGenres, (dialog, position, isChecked) -> {
            if (isChecked) {
                indexGenres.add(position);
            } else {
                indexGenres.remove((Integer.valueOf(position)));
            }
        });

        builder.setPositiveButton("Ok", (dialog, position) -> {
            newGenres = new ArrayList<>();
            for (int i = 0; i < indexGenres.size(); i++) {
                Genre genre = new Genre();
                genre.setId(i);
                genre.setName(listGenres[indexGenres.get(i)]);
                newGenres.add(genre);
            }

            String genres;
            StringBuilder genresBuilder = new StringBuilder();
            if (newGenres.size() > 0) {
                for (int i = 0; i < newGenres.size(); i++) {
                    genresBuilder.append("-");
                    genresBuilder.append(newGenres.get(i).getName());
                    genresBuilder.append("\n");
                }
                genres = genresBuilder.toString();
                textViewGenres.setText(genres);
            } else textViewGenres.setText("-");
        });

        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
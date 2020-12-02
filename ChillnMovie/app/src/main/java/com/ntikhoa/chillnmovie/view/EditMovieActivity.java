package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Genre;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMovieActivity extends AppCompatActivity {
    private static final String TAG = "EditMovieActivity";
    public static final String EXTRA_ID = "tmdb_movie_id";

    private MovieDetailViewModel viewModel;

    private EditText editTextTitle;
    private EditText editTextOriginalTitle;
    private EditText editTextStatus;
    private EditText editTextReleaseDate;
    private TextView textViewGenres;
    private EditText editTextRuntime;
    private EditText editTextOriginalLanguage;
    private EditText editTextBudget;
    private EditText editTextRevenue;
    private EditText editTextOverview;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;
    private MaterialButton btnEditGenres;

    private String[] listGenres;
    private boolean[] checkedGenres;
    private ArrayList<Integer> indexGenres = new ArrayList<>();
    private List<Genre> newGenres;

    private MovieDetail movieDetail;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        initComponent();
        loadData();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGenres();
            }
        });
    }

    private void initComponent() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextOriginalTitle = findViewById(R.id.editTextOriginalTitle);
        editTextStatus = findViewById(R.id.editTextStatus);
        editTextReleaseDate = findViewById(R.id.editTextReleaseDate);
        textViewGenres = findViewById(R.id.textViewGenres);
        editTextRuntime = findViewById(R.id.editTextRuntime);
        editTextOriginalLanguage = findViewById(R.id.editTextOriginalLanguage);
        editTextBudget = findViewById(R.id.editTextBudget);
        editTextRevenue = findViewById(R.id.editTextRevenue);
        editTextOverview = findViewById(R.id.editTextOverview);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnEditGenres = findViewById(R.id.btnEditGenres);

        listGenres = getResources().getStringArray(R.array.genres);
        checkedGenres = new boolean[listGenres.length];

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MovieDetailViewModel.class);
    }

    private void loadData() {
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        viewModel.getMLDmovieDetail(id)
                .observe(this, movieDetail -> {
                    EditMovieActivity.this.movieDetail = movieDetail;
                    setBackground(movieDetail);
                    fetchData(movieDetail);
                });
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

        Integer runtime = movieDetail.getRuntime();
        fetchViewInteger(editTextRuntime, runtime);

        String originalLanguage = movieDetail.getOriginalLanguage();
        fetchViewString(editTextOriginalLanguage, originalLanguage);

        Integer budget = movieDetail.getBudget();
        fetchViewInteger(editTextBudget, budget);

        Integer revenue = movieDetail.getRevenue();
        fetchViewInteger(editTextRevenue, revenue);

        String overview = movieDetail.getOverview();
        fetchViewString(editTextOverview, overview);
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
        String title = editTextTitle.getText().toString();
        if (editTextTitle.getText().toString().trim().equalsIgnoreCase("")) {
            editTextTitle.setError("This field cannot be blank");
            return;
        }

        String originalTitle = editTextOriginalTitle.getText().toString();
        String status = editTextStatus.getText().toString();


        String releaseDate = editTextReleaseDate.getText().toString();
        if (!releaseDate.isEmpty() && !validate(releaseDate)) {
            editTextReleaseDate.setError("Unvalidated");
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

        String overview = editTextOverview.getText().toString();
        if (editTextOverview.getText().toString().trim().equalsIgnoreCase("")) {
            editTextOverview.setError("This field cannot be blank");
            return;
        }

        if (newGenres.size() < 1) {
            textViewGenres.requestFocus();
            textViewGenres.setError("Cannot empty");
            return;
        }

        movieDetail.setTitle(title);
        movieDetail.setOriginalTitle(originalTitle);
        movieDetail.setStatus(status);
        movieDetail.setReleaseDate(releaseDate);
        movieDetail.setRuntime(runtime);
        movieDetail.setOriginalLanguage(originalLanguage);
        movieDetail.setBudget(budget);
        movieDetail.setRevenue(revenue);
        movieDetail.setOverview(overview);
        movieDetail.setGenres(newGenres);

        viewModel.addToDatabase(movieDetail);
        finish();
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

    private void setBackground(MovieDetail movieDetail) {
        String posterUrl = Movie.path + movieDetail.getPosterPath();
        Picasso.get()
                .load(posterUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
                        nestedScrollView.setBackground(new BitmapDrawable(getResources(), bitmap));
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
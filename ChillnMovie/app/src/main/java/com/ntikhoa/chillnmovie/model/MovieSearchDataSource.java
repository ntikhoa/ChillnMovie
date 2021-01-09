package com.ntikhoa.chillnmovie.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieSearchDataSource extends PageKeyedDataSource<Integer, Movie> {

    private Application application;
    private String search;

    public MovieSearchDataSource(Application application, String search) {
        this.application = application;
        this.search = search;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getSearchMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
                        1,
                        true,
                        search)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                            }
                            callback.onResult(movies, null, 2);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getSearchMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
                        params.key,
                        true,
                        search)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                            }
                            callback.onResult(movies, params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

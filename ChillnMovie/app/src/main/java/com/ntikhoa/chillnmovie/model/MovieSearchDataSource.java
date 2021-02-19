package com.ntikhoa.chillnmovie.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.ntikhoa.chillnmovie.BuildConfig;
import com.ntikhoa.chillnmovie.api.MovieAPI;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MovieSearchDataSource extends PageKeyedDataSource<Integer, Movie> {

    private Application application;
    private MovieAPI movieAPI;
    private String search;

    @Inject
    public MovieSearchDataSource(Application application, MovieAPI movieAPI, String search) {
        this.application = application;
        this.movieAPI = movieAPI;
        this.search = search;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        movieAPI.getSearchMovie(BuildConfig.TMDB_ACCESS_KEY,
                        BuildConfig.VN_LANG,
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
        movieAPI.getSearchMovie(BuildConfig.TMDB_ACCESS_KEY,
                        BuildConfig.VN_LANG,
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

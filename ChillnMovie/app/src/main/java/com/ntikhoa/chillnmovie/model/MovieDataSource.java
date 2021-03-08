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

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private Application application;
    private int category;
    private Call<MovieDBresponse> dBresponseCall;
    private MovieAPI movieAPI;

    @Inject
    public MovieDataSource(Application application, MovieAPI movieAPI, int category) {
        this.application = application;
        this.movieAPI = movieAPI;
        this.category = category;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        initDataSourceType(1);
        dBresponseCall.enqueue(new Callback<MovieDBresponse>() {
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
        initDataSourceType(params.key);
        dBresponseCall.enqueue(new Callback<MovieDBresponse>() {
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

    private void initDataSourceType(int pageKey) {
        if (category == Movie.POPULAR) {
            dBresponseCall = movieAPI.getPopularMovie(BuildConfig.TMDB_ACCESS_KEY,
                            BuildConfig.VN_LANG,
                            pageKey);
        } else if (category == Movie.NOW_PLAYING) {
            dBresponseCall = movieAPI.getNowPlayingMovie(BuildConfig.TMDB_ACCESS_KEY,
                            BuildConfig.VN_LANG,
                            pageKey);
        } else if (category == Movie.UPCOMING) {
            dBresponseCall = movieAPI.getUpcomingMovie(BuildConfig.TMDB_ACCESS_KEY,
                            BuildConfig.VN_LANG,
                            pageKey);
        } else if (category == Movie.TOP_RATED) {
            dBresponseCall = movieAPI.getTopRatedMovie(BuildConfig.TMDB_ACCESS_KEY,
                            BuildConfig.VN_LANG,
                            pageKey);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

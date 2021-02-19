package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.BuildConfig;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDBresponse;
import com.ntikhoa.chillnmovie.service.MovieAPI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class EditorFrontPageRepository {
    private MutableLiveData<List<Movie>> MLDtrendingMovie;
    private MutableLiveData<List<Movie>> MLDpopularMovie;
    private MutableLiveData<List<Movie>> MLDupcomingMovie;
    private MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    private MutableLiveData<List<Movie>> MLDtopRatedMovie;

    private Application application;

    private MovieAPI movieAPI;

    @Inject
    public EditorFrontPageRepository(Application application, MovieAPI movieAPI) {
        this.application = application;
        this.movieAPI = movieAPI;

        MLDpopularMovie = new MutableLiveData<>();
        MLDupcomingMovie = new MutableLiveData<>();
        MLDnowPlayingMovie = new MutableLiveData<>();
        MLDtopRatedMovie = new MutableLiveData<>();
        MLDtrendingMovie = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMLDtrendingMovie() {
        movieAPI.getTrendingMovie(BuildConfig.TMDB_ACCESS_KEY)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            List<Movie> subMovie = new ArrayList<>();
                            for (int i = 0; i < 10; i++) {
                                Movie movie = movies.get(i);
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                                subMovie.add(movies.get(i));
                            }
                            MLDtrendingMovie.postValue(subMovie);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDtrendingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDpopularMovie() {
        movieAPI.getPopularMovie(BuildConfig.TMDB_ACCESS_KEY,
                BuildConfig.VN_LANG,
                1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                            }
                            MLDpopularMovie.postValue(movies);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDpopularMovie;
    }

    public MutableLiveData<List<Movie>> getMLDupcomingMovie() {
        movieAPI.getUpcomingMovie(BuildConfig.TMDB_ACCESS_KEY,
                        BuildConfig.VN_LANG,
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                            }
                            MLDupcomingMovie.postValue(movies);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDupcomingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDnowPlayingMovie() {
        movieAPI.getNowPlayingMovie(BuildConfig.TMDB_ACCESS_KEY,
                        BuildConfig.VN_LANG,
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                            }
                            MLDnowPlayingMovie.postValue(movies);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDnowPlayingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDtopRatedMovie() {
        movieAPI.getTopRatedMovie(BuildConfig.TMDB_ACCESS_KEY,
                        BuildConfig.VN_LANG,
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            for (int i = 0; i < movies.size(); i++) {
                                Movie movie = movies.get(i);
                                movie.setPosterPath(Movie.path + movie.getPosterPath());
                                movie.setBackdropPath(Movie.path + movie.getBackdropPath());
                            }
                            MLDtopRatedMovie.postValue(movies);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDtopRatedMovie;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDBresponse;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorFrontPageRepository {
    MutableLiveData<List<Movie>> MLDtrendingMovie;
    MutableLiveData<List<Movie>> MLDpopularMovie;
    MutableLiveData<List<Movie>> MLDupcomingMovie;
    MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    MutableLiveData<List<Movie>> MLDtopRatedMovie;

    Application application;

    public EditorFrontPageRepository(Application application) {
        this.application = application;
        MLDpopularMovie = new MutableLiveData<>();
        MLDupcomingMovie = new MutableLiveData<>();
        MLDnowPlayingMovie = new MutableLiveData<>();
        MLDtopRatedMovie = new MutableLiveData<>();
        MLDtrendingMovie = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMLDtrendingMovie() {
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getTrendingMovie(application.getString(R.string.TMDb_API_key))
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
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getPopularMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
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
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getUpcomingMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
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
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getNowPlayingMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
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
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getTopRatedMovie(application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese),
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

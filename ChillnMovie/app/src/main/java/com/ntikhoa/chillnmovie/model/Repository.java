package com.ntikhoa.chillnmovie.model;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    MutableLiveData<List<Movie>> MLDtrendingMovie;
    MutableLiveData<List<Movie>> MLDpopularMovie;
    MutableLiveData<List<Movie>> MLDupcomingMovie;
    MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    MutableLiveData<List<Movie>> MLDtopRatedMovie;
    Application application;

    public Repository(Application application) {
        this.application = application;
        MLDpopularMovie = new MutableLiveData<>();
        MLDupcomingMovie = new MutableLiveData<>();
        MLDnowPlayingMovie = new MutableLiveData<>();
        MLDtopRatedMovie = new MutableLiveData<>();
        MLDtrendingMovie = new MutableLiveData<>();
    }

    public MutableLiveData getMLDtrendingMovie() {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getTrendingMovie(application.getString(R.string.API_key))
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
                            List<Movie> subMovie = new ArrayList<>();
                            for (int i = 0; i < 10; i++)
                                subMovie.add(movies.get(i));
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

    public MutableLiveData getMLDpopularMovie() {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getPopularMovie(application.getString(R.string.API_key),
                        application.getString(R.string.lang_vietnamses),
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
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

    public MutableLiveData getMLDupcomingMovie() {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getUpcomingMovie(application.getString(R.string.API_key),
                        application.getString(R.string.lang_vietnamses),
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
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

    public MutableLiveData getMLDnowPlayingMovie() {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getNowPlayingMovie(application.getString(R.string.API_key),
                        application.getString(R.string.lang_vietnamses),
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
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

    public MutableLiveData getMLDtopRatedMovie() {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getTopRatedMovie(application.getString(R.string.API_key),
                        application.getString(R.string.lang_vietnamses),
                        1)
                .enqueue(new Callback<MovieDBresponse>() {
                    @Override
                    public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movies = response.body().getMovies();
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

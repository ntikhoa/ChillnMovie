package com.ntikhoa.chillnmovie.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private Application application;
    private int category;
    private Call<MovieDBresponse> dBresponseCall;

    public MovieDataSource(Application application, int category) {
        this.application = application;
        this.category = category;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        initDataSourceType(1);
        dBresponseCall.enqueue(new Callback<MovieDBresponse>() {
            @Override
            public void onResponse(Call<MovieDBresponse> call, Response<MovieDBresponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().getMovies(), null, 2);
                }
            }

            @Override
            public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                Toast.makeText(
                        application.getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
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
                    callback.onResult(response.body().getMovies(), params.key + 1);
                }
            }

            @Override
            public void onFailure(Call<MovieDBresponse> call, Throwable t) {
                Toast.makeText(
                        application.getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDataSourceType(int pageKey) {
        if (category == Movie.POPULAR) {
            dBresponseCall = RetrofitClient.getInstance()
                    .getMovieAPI()
                    .getPopularMovie(application.getString(R.string.API_key),
                            application.getString(R.string.lang_vietnamses),
                            pageKey);
        } else if (category == Movie.NOW_PLAYING) {
            dBresponseCall = RetrofitClient.getInstance()
                    .getMovieAPI()
                    .getNowPlayingMovie(application.getString(R.string.API_key),
                            application.getString(R.string.lang_vietnamses),
                            pageKey);
        } else if (category == Movie.UPCOMING) {
            dBresponseCall = RetrofitClient.getInstance()
                    .getMovieAPI()
                    .getUpcomingMovie(application.getString(R.string.API_key),
                            application.getString(R.string.lang_vietnamses),
                            pageKey);
        } else if (category == Movie.TOP_RATED) {
            dBresponseCall = RetrofitClient.getInstance()
                    .getMovieAPI()
                    .getTopRatedMovie(application.getString(R.string.API_key),
                            application.getString(R.string.lang_vietnamses),
                            pageKey);
        }
    }
}

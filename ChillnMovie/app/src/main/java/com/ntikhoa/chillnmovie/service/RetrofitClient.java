package com.ntikhoa.chillnmovie.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL = "https://api.themoviedb.org/3/";
    private static RetrofitClient mInstance;
    private static Retrofit retrofit;
    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public MovieAPI getMovieAPI() {
        return retrofit.create(MovieAPI.class);
    }
}

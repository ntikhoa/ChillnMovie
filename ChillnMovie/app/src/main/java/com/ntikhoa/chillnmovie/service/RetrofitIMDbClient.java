package com.ntikhoa.chillnmovie.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitIMDbClient {
    private final String BASE_URL = "https://imdb-api.com/";
    private static RetrofitIMDbClient retrofitIMDbClient;
    private static Retrofit retrofit;

    private RetrofitIMDbClient() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitIMDbClient getInstance() {
        if (retrofitIMDbClient == null) {
            retrofitIMDbClient = new RetrofitIMDbClient();
        }
        return retrofitIMDbClient;
    }

    public RatingSourceAPI getAPI() {
        return retrofit.create(RatingSourceAPI.class);
    }
}

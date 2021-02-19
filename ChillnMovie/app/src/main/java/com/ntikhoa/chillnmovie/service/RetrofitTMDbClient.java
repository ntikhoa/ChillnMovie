package com.ntikhoa.chillnmovie.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class RetrofitTMDbClient {
//    private static String BASE_URL = "https://api.themoviedb.org/3/";
//    private static RetrofitTMDbClient mInstance;
//    private static Retrofit retrofit;
//    private RetrofitTMDbClient() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public static RetrofitTMDbClient getInstance() {
//        if (mInstance == null) {
//            mInstance = new RetrofitTMDbClient();
//        }
//        return mInstance;
//    }
//
//    public MovieAPI getMovieAPI() {
//        return retrofit.create(MovieAPI.class);
//    }
//}

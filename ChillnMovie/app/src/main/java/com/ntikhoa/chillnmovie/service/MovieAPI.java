package com.ntikhoa.chillnmovie.service;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;

import com.ntikhoa.chillnmovie.model.CreditDBresponse;
import com.ntikhoa.chillnmovie.model.MovieDBresponse;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {
    @GET("movie/popular")
    Call<MovieDBresponse> getPopularMovie(
            @Query("api_key") String key,
            @Query("language") String lang,
            @Query("page") int page);

    @GET("movie/upcoming")
    Call<MovieDBresponse> getUpcomingMovie(
            @Query("api_key") String key,
            @Query("language") String lang,
            @Query("page") int page);

    @GET("movie/now_playing")
    Call<MovieDBresponse> getNowPlayingMovie(
            @Query("api_key") String key,
            @Query("language") String lang,
            @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieDBresponse> getTopRatedMovie(
            @Query("api_key") String key,
            @Query("language") String lang,
            @Query("page") int page);

    @GET("trending/movie/day")
    Call<MovieDBresponse> getTrendingMovie(
            @Query("api_key") String key);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(
            @Path("movie_id") int id,
            @Query("api_key") String key,
            @Query("language") String lang);

    @GET("movie/{movie_id}/credits")
    Call<CreditDBresponse> getCaster(
            @Path("movie_id") int movieId,
            @Query("api_key") String key);

    @GET("movie/{movie_id}/videos")
    Call<VideoDBResponse> getVideo(
            @Path("movie_id") int movieId,
            @Query("api_key") String key);
}

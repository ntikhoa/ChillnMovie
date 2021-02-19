package com.ntikhoa.chillnmovie.api;

import com.ntikhoa.chillnmovie.model.RatingSource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RatingSourceAPI {
    @GET("en/API/Ratings/{apiKey}/{id}")
    Call<RatingSource> getRatingSource(
            @Path("apiKey") String apiKey,
            @Path("id") String id);
}

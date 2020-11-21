package com.ntikhoa.chillnmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDBresponse {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> Movies = null;

    public List<Movie> getMovies() {
        return Movies;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}

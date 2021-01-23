package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends Movie {
    @SerializedName("budget")
    @Expose
    private Integer budget;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("revenue")
    @Expose
    private Integer revenue;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tagline")
    @Expose
    private String tagline;

    private String trailer_key;

    public MovieDetail() {
        //require empty constructor
    }

    public MovieDetail(long id) {
        super(id);
    }


    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Integer getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }


    public String getImdbId() {
        return imdbId;
    }


    public Integer getRevenue() {
        return revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTrailer_key() {
        return trailer_key;
    }

    public void setTrailer_key(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

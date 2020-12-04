package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingSource {
    @SerializedName("imDb")
    @Expose
    private Double imDb;
    @SerializedName("metacritic")
    @Expose
    private Integer metacritic;
    @SerializedName("theMovieDb")
    @Expose
    private Double theMovieDb;
    @SerializedName("rottenTomatoes")
    @Expose
    private Integer rottenTomatoes;
    @SerializedName("tV_com")
    @Expose
    private Double tV_com;
    @SerializedName("filmAffinity")
    @Expose
    private Double filmAffinity;

    public Double getImDb() {
        return imDb;
    }

    public Integer getMetacritic() {
        return metacritic;
    }

    public Integer getRottenTomatoes() {
        return rottenTomatoes;
    }

    public Double gettV_com() {
        return tV_com;
    }

    public Double getFilmAffinity() {
        return filmAffinity;
    }
}

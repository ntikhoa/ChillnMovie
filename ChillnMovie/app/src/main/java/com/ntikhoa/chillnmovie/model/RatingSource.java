package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingSource {
    @SerializedName("imDb")
    @Expose
    private String imDb; //10
    @SerializedName("metacritic")
    @Expose
    private String metacritic; //100
    @SerializedName("theMovieDb")
    @Expose
    private String theMovieDb; //10
    @SerializedName("rottenTomatoes")
    @Expose
    private String rottenTomatoes; //100

    public String getImDb() {
        return imDb;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public String getTheMovieDb() {
        return theMovieDb;
    }
}

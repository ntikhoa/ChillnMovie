package com.ntikhoa.chillnmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoDBResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    public List<Video> videos;
}

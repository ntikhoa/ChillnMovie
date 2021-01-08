package com.ntikhoa.chillnmovie.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    public static final String path = "https://image.tmdb.org/t/p/w500";
    public static final String MEDIA_TYPE = "movie";
    public static final String TIME_WINDOW = "day";
    public static final int POPULAR = 1;
    public static final int NOW_PLAYING = 2;
    public static final int UPCOMING = 3;
    public static final int TOP_RATED = 4;


    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    private String updated_date;
    private boolean isTrending;
    private boolean isUpcoming;
    private boolean isNowPlaying;
    private boolean isVietnamese;

    public static final DiffUtil.ItemCallback<Movie> CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.id.equals(newItem.id);
        }
    };

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public Movie() {
        isTrending = false;
        isUpcoming = false;
        isNowPlaying = false;
        isVietnamese = false;
    }

    public Movie(MovieDetail movieDetail) {
        this.id = movieDetail.getId();
        this.title = movieDetail.getTitle();
        this.posterPath = movieDetail.getPosterPath();
        this.backdropPath = movieDetail.getBackdropPath();
        this.voteCount = movieDetail.getVoteCount();
        this.voteAverage = movieDetail.getVoteAverage();
        this.releaseDate = movieDetail.getReleaseDate();

        this.isTrending = movieDetail.getIsTrending();
        this.isNowPlaying = movieDetail.getIsNowPlaying();
        this.isUpcoming = movieDetail.getIsUpcoming();
        this.isVietnamese = movieDetail.getIsVietnamese();
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Long getId() {
        return id;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean getIsTrending() {
        return isTrending;
    }

    public boolean getIsUpcoming() {
        return isUpcoming;
    }

    public boolean getIsNowPlaying() {
        return isNowPlaying;
    }

    public boolean getIsVietnamese() {
        return isVietnamese;
    }
}

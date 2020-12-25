package com.ntikhoa.chillnmovie.model;

public class MovieRate {
    private Integer voteCount;
    private Double voteAverage;
    private Double plotVoteAverage;
    private Double visualEffectVoteAverage;
    private Double soundEffectVoteAverage;

    public MovieRate() {}

    public MovieRate(Double voteAverage) {
        this.voteCount = 1;
        this.voteAverage = voteAverage;
        this.plotVoteAverage = voteAverage;
        this.visualEffectVoteAverage = voteAverage;
        this.soundEffectVoteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Double getPlotVoteAverage() {
        return plotVoteAverage;
    }

    public Double getVisualEffectVoteAverage() {
        return visualEffectVoteAverage;
    }

    public Double getSoundEffectVoteAverage() {
        return soundEffectVoteAverage;
    }
}

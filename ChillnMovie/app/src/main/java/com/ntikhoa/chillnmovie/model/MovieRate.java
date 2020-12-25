package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.auth.User;

public class MovieRate {
    private Integer voteCount;
    private Double voteAverage;
    private Double plotVoteAverage;
    private Double visualEffectVoteAverage;
    private Double soundEffectVoteAverage;

    public MovieRate() {
    }

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

    public void vote(double plotVote, double visualEffectVote, double soundEffectVote) {
        plotVoteAverage = (plotVoteAverage * voteCount + plotVote) / (voteCount + 1);
        visualEffectVoteAverage = (visualEffectVoteAverage * voteCount + visualEffectVote) / (voteCount + 1);
        soundEffectVoteAverage = (soundEffectVoteAverage * voteCount + soundEffectVote) / (voteCount + 1);

        voteAverage = (plotVoteAverage + visualEffectVoteAverage + soundEffectVoteAverage) / 3;
        voteCount += 1;
    }

    public void updateVote(UserRate oldUserRate, UserRate newUserRate) {
        double plot = newUserRate.getPlotVote() - oldUserRate.getPlotVote();
        plotVoteAverage = (plotVoteAverage * voteCount + plot) / voteCount;
        double visualEffect = newUserRate.getVisualEffectVote() - oldUserRate.getVisualEffectVote();
        visualEffectVoteAverage = (visualEffectVoteAverage * voteCount + visualEffect) / voteCount;
        double soundEffect = newUserRate.getSoundEffectVote() - oldUserRate.getSoundEffectVote();
        soundEffectVoteAverage = (soundEffectVoteAverage * voteCount + soundEffect) / voteCount;

        voteAverage = (plotVoteAverage + visualEffectVoteAverage + soundEffectVoteAverage) / 3;
    }
}

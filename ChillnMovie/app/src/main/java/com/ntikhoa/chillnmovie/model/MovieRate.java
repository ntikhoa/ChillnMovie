package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.auth.User;

public class MovieRate {
    private Integer voteCount;
    private Double voteAverage;
    private Double plotVoteAverage;
    private Double visualVoteAverage;
    private Double audioVoteAverage;

    public MovieRate() {
    }

    public MovieRate(Double voteAverage) {
        this.voteCount = 1;
        this.voteAverage = voteAverage;
        this.plotVoteAverage = voteAverage;
        this.visualVoteAverage = voteAverage;
        this.audioVoteAverage = voteAverage;
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

    public Double getVisualVoteAverage() {
        return visualVoteAverage;
    }

    public Double getAudioVoteAverage() {
        return audioVoteAverage;
    }

    public void vote(UserRate userRate) {
        double plotVote = userRate.getPlotVote();
        double visualVote = userRate.getVisualVote();
        double audioVote = userRate.getAudioVote();
        plotVoteAverage = (plotVoteAverage * voteCount + plotVote) / (voteCount + 1);
        visualVoteAverage = (visualVoteAverage * voteCount + visualVote) / (voteCount + 1);
        audioVoteAverage = (audioVoteAverage * voteCount + audioVote) / (voteCount + 1);

        voteAverage = (plotVoteAverage + visualVoteAverage + audioVoteAverage) / 3;
        voteCount += 1;
    }

    public void updateVote(UserRate oldUserRate, UserRate newUserRate) {
        double plot = newUserRate.getPlotVote() - oldUserRate.getPlotVote();
        plotVoteAverage = (plotVoteAverage * voteCount + plot) / voteCount;
        double visualEffect = newUserRate.getVisualVote() - oldUserRate.getVisualVote();
        visualVoteAverage = (visualVoteAverage * voteCount + visualEffect) / voteCount;
        double soundEffect = newUserRate.getAudioVote() - oldUserRate.getAudioVote();
        audioVoteAverage = (audioVoteAverage * voteCount + soundEffect) / voteCount;

        voteAverage = (plotVoteAverage + visualVoteAverage + audioVoteAverage) / 3;
    }
}

package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;

public class UserRate {
    private Integer plotVote;
    private Integer visualEffectVote;
    private Integer soundEffectVote;
    private String comment;
    private String userId;
    private String rateDate;

    public UserRate() {}

    public UserRate(String userId) {
        this.userId = userId;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getPlotVote() {
        return plotVote;
    }

    public Integer getVisualEffectVote() {
        return visualEffectVote;
    }

    public Integer getSoundEffectVote() {
        return soundEffectVote;
    }

    public String getComment() {
        return comment;
    }

    public void setPlotVote(Integer plotVote) {
        this.plotVote = plotVote;
    }

    public void setVisualEffectVote(Integer visualEffectVote) {
        this.visualEffectVote = visualEffectVote;
    }

    public void setSoundEffectVote(Integer soundEffectVote) {
        this.soundEffectVote = soundEffectVote;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

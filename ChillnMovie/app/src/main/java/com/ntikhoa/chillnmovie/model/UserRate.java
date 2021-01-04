package com.ntikhoa.chillnmovie.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.auth.User;

public class UserRate {
    private Integer plotVote;
    private Integer visualVote;
    private Integer audioVote;
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

    public Integer getVisualVote() {
        return visualVote;
    }

    public Integer getAudioVote() {
        return audioVote;
    }

    public String getComment() {
        return comment;
    }

    public void setPlotVote(Integer plotVote) {
        this.plotVote = plotVote;
    }

    public void setVisualVote(Integer visualVote) {
        this.visualVote = visualVote;
    }

    public void setAudioVote(Integer audioVote) {
        this.audioVote = audioVote;
    }

    public void setComment(String comment) {
        //comment = comment.replace("\n", "\\n");
        this.comment = comment;
    }

    public static final DiffUtil.ItemCallback<UserRate> CALLBACK = new DiffUtil.ItemCallback<UserRate>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserRate oldItem, @NonNull UserRate newItem) {
            return oldItem.getUserId().equals(newItem.getUserId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserRate oldItem, @NonNull UserRate newItem) {
            return false;
        }
    };
}

package com.ntikhoa.chillnmovie.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class RateJoinUser {
    private UserAccount userAccount;
    private UserRate userRate;

    public RateJoinUser(UserAccount userAccount, UserRate userRate) {
        this.userAccount = userAccount;
        this.userRate = userRate;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public UserRate getUserRate() {
        return userRate;
    }

    public static final DiffUtil.ItemCallback<RateJoinUser> CALLBACK = new DiffUtil.ItemCallback<RateJoinUser>() {
        @Override
        public boolean areItemsTheSame(@NonNull RateJoinUser oldItem, @NonNull RateJoinUser newItem) {
            return oldItem.getUserRate().getUserId() == newItem.getUserRate().getUserId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RateJoinUser oldItem, @NonNull RateJoinUser newItem) {
            return oldItem.getUserRate().getUserId().equals(newItem.getUserRate().getUserId());
        }
    };
}

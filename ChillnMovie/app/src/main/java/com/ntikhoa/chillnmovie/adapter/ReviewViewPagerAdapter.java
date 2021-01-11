package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.view.ReviewFragment;
import com.ntikhoa.chillnmovie.view.UserRateFragment;

public class ReviewViewPagerAdapter extends FragmentPagerAdapter {

    private static final int SIZE = 2;
    private final Context context;
    private long id;

    private OnClickListener onClickReviewListener;

    public void setOnClickListener(OnClickListener onClickReviewListener) {
        this.onClickReviewListener = onClickReviewListener;
    }

    public ReviewViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context, Long id) {
        super(fm, behavior);
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                ReviewFragment fragment = ReviewFragment.newInstance(id);
                fragment.setOnClickListener(new ReviewFragment.OnClickListener() {
                    @Override
                    public void onClick() {
                        onClickReviewListener.onClick();
                    }
                });
                return fragment;
            }
            case 1:
                return UserRateFragment.newInstance(id);
            default: {
                ReviewFragment fragment = ReviewFragment.newInstance(id);
                fragment.setOnClickListener(new ReviewFragment.OnClickListener() {
                    @Override
                    public void onClick() {
                        onClickReviewListener.onClick();
                    }
                });
                return fragment;
            }
        }
    }

    @Override
    public int getCount() {
        return SIZE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.review);
            case 1:
                return context.getResources().getString(R.string.user_rate);
            default:
                return context.getResources().getString(R.string.review);
        }
    }

    public interface OnClickListener {
        void onClick();
    }
}

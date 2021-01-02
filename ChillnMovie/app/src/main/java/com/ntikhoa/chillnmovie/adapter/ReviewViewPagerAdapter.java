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

    public ReviewViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ReviewFragment();
            case 1:
                return new UserRateFragment();
            default:
                return new ReviewFragment();
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
}

package com.ntikhoa.chillnmovie.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;

public class ConstantShimmerEffect {

    private ShimmerDrawable drawable;

    public ConstantShimmerEffect(Context context) {
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, R.color.colorShimmerBase))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(context, R.color.colorShimmerHighlight))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .setDuration(500)
                .build();
        drawable = new ShimmerDrawable();
        drawable.setShimmer(shimmer);
    }

    public ShimmerDrawable getDrawable() {
        return drawable;
    }
}

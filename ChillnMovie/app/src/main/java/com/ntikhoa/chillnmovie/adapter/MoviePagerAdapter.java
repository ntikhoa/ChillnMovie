package com.ntikhoa.chillnmovie.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.squareup.picasso.Picasso;

public class MoviePagerAdapter extends ListAdapter<Movie, MoviePagerAdapter.MovieViewHolder> {
    public MoviePagerAdapter() {
        super(Movie.CALLBACK);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item_pager, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(Color.parseColor("#F3F3F3"))
                    .setBaseAlpha(1)
                    .setHighlightColor(Color.parseColor("#C3C3C3"))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .setDuration(500)
                    .build();
            ShimmerDrawable drawable = new ShimmerDrawable();
            drawable.setShimmer(shimmer);

            String path = Movie.path + movie.getBackdropPath();
            Picasso.get().load(path)
                    .placeholder(drawable)
                    .into(holder.imageViewBackdrop);
        }
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewBackdrop;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBackdrop = itemView.findViewById(R.id.imageViewBackdrop);
        }
    }
}

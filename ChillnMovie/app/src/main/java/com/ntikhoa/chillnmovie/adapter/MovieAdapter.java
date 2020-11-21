package com.ntikhoa.chillnmovie.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    public MovieAdapter() {
        super(Movie.CALLBACK);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
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

            String path = Movie.path + movie.getPosterPath();
            Picasso.get().load(path)
                    .placeholder(drawable)
                    .into(holder.imageViewPoster);

//            holder.textViewTitle.setText(movie.getTitle());
        }
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewPoster;
//        public TextView textViewTitle;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
//            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}

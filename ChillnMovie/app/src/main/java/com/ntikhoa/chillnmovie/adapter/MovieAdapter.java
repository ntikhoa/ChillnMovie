package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    private final Context context;

    public MovieAdapter(Context context) {
        super(Movie.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(ContextCompat.getColor(context, R.color.colorShimmerBase))
                    .setBaseAlpha(1)
                    .setHighlightColor(ContextCompat.getColor(context, R.color.colorShimmerHighlight))
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.EXTRA_ID, movie.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewPoster;
        //        public TextView textViewTitle;
        public MovieViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
//            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}

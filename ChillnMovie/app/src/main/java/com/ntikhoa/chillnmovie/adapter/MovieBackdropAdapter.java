package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;
import com.squareup.picasso.Picasso;

public class MovieBackdropAdapter extends ListAdapter<Movie, MovieBackdropAdapter.MovieViewHolder> {
    private final Context context;

    public MovieBackdropAdapter(Context context) {
        super(Movie.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public MovieBackdropAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_movie_header, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBackdropAdapter.MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.textViewTitle.setText(movie.getTitle());

            setBackdrop(holder, movie.getBackdropPath());

            setRating(holder, movie.getVoteAverage());

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

    private void setBackdrop(MovieBackdropAdapter.MovieViewHolder holder, String backdropPath) {
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

        Picasso.get().load(backdropPath)
                .placeholder(drawable)
                .into(holder.imageBackdrop);
    }

    private void setRating(MovieBackdropAdapter.MovieViewHolder holder, double voteAverage) {
        ProgressBar pbRating = holder.progressBarRating;
        TextView textViewRating = holder.textViewRating;

        if (voteAverage >= 8.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(context,
                    R.drawable.circle_green));
        } else if (voteAverage >= 5.0d) {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(context,
                    R.drawable.circle_yellow));
        } else {
            pbRating.setProgressDrawable(ContextCompat.getDrawable(context,
                    R.drawable.circle_red));
        }
        //when setProgressDrawable manually, have to add this line
        pbRating.setProgress(1);
        pbRating.setMax(10);
        pbRating.setProgress((int) voteAverage);
        String ratingFormatted = String.format("%.1f", voteAverage);
        textViewRating.setText(ratingFormatted);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageBackdrop;
        private ProgressBar progressBarRating;
        private TextView textViewRating;
        private TextView textViewTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBackdrop = itemView.findViewById(R.id.imageViewBackdrop);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            View view = itemView.findViewById(R.id.ratingView);
            progressBarRating = view.findViewById(R.id.progressBarRating);
            textViewRating = view.findViewById(R.id.textViewRate);
        }
    }
}

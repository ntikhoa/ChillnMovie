package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentMovieHeaderBinding;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
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
        FragmentMovieHeaderBinding binding =
                FragmentMovieHeaderBinding.inflate(inflater, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieBackdropAdapter.MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.bind(movie);
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private FragmentMovieHeaderBinding binding;

        public MovieViewHolder(FragmentMovieHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Movie movie = getItem(position);
                    if (movie != null) {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailActivity.EXTRA_ID, movie.getId());
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Movie movie) {
            binding.textViewTitle.setText(movie.getTitle());

            setBackdrop(movie.getBackdropPath());
            setRating(movie.getVoteAverage());
        }

        private void setBackdrop(String backdropPath) {
            ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
            Picasso.get().load(backdropPath)
                    .placeholder(shimmerEffect.getDrawable())
                    .into(binding.imageViewBackdrop);
        }

        private void setRating(double voteAverage) {
            ProgressBar pbRating = binding.ratingView.progressBarRating;
            TextView textViewRating = binding.ratingView.textViewRate;

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
    }
}

package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ntikhoa.chillnmovie.databinding.MovieItemBinding;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
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
        MovieItemBinding binding = MovieItemBinding.inflate(inflater, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.bind(movie);
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private MovieItemBinding binding;

        public MovieViewHolder(MovieItemBinding binding) {
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
            ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
            Picasso.get().load(movie.getPosterPath())
                    .placeholder(shimmerEffect.getDrawable())
                    .into(binding.imageViewPoster);
        }
    }
}

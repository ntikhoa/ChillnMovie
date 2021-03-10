package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentMovieHeaderBinding;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;
import com.squareup.picasso.Picasso;


public class FavoriteAdapter extends ListAdapter<Long, FavoriteAdapter.FavoriteViewHolder> {
    private static final DiffUtil.ItemCallback<Long> CALLBACK = new DiffUtil.ItemCallback<Long>() {
        @Override
        public boolean areItemsTheSame(@NonNull Long oldItem, @NonNull Long newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Long oldItem, @NonNull Long newItem) {
            return false;
        }
    };

    private final Context context;

    public FavoriteAdapter(Context context) {
        super(CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentMovieHeaderBinding binding =
                FragmentMovieHeaderBinding.inflate(inflater, parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Long movieId = getItem(position);
        if (movieId != null) {
            holder.bind(movieId);
        }
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private FragmentMovieHeaderBinding binding;

        public FavoriteViewHolder(FragmentMovieHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Long movieId = getItem(position);
                    if (movieId != null) {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailActivity.EXTRA_ID, movieId);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Long movieId) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(CollectionName.MOVIE)
                    .document(String.valueOf(movieId))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Movie movie = documentSnapshot.toObject(Movie.class);
                            binding.textViewTitle.setText(movie.getTitle());

                            setBackdrop(movie.getBackdropPath());
                            setRating(movie.getVoteAverage());
                        }
                    });
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

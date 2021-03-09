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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

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

    private Context context;

    public FavoriteAdapter(Context context) {
        super(CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_movie_header, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Long movieId = getItem(position);
        if (movieId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(CollectionName.MOVIE)
                    .document(String.valueOf(movieId))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Movie movie = documentSnapshot.toObject(Movie.class);
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
                    });
        }
    }

    private void setBackdrop(FavoriteViewHolder holder, String backdropPath) {
        ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
        Picasso.get().load(backdropPath)
                .placeholder(shimmerEffect.getDrawable())
                .into(holder.imageBackdrop);
    }

    private void setRating(FavoriteViewHolder holder, double voteAverage) {
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

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageBackdrop;
        private ProgressBar progressBarRating;
        private TextView textViewRating;
        private TextView textViewTitle;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBackdrop = itemView.findViewById(R.id.imageViewBackdrop);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            View view = itemView.findViewById(R.id.ratingView);
            progressBarRating = view.findViewById(R.id.progressBarRating);
            textViewRating = view.findViewById(R.id.textViewRate);
        }
    }
}

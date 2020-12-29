package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ntikhoa.chillnmovie.model.RateJoinUser;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.util.Formatter;

public class UserRateAdapter extends ListAdapter<RateJoinUser, UserRateAdapter.RateViewHolder> {

    private Context context;

    public UserRateAdapter(Context context) {
        super(RateJoinUser.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.user_rate_item, parent, false);
        return new RateViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        RateJoinUser rateJoinUser = getItem(position);
        if (rateJoinUser != null) {
            holder.textViewUserName.setText(rateJoinUser.getUserAccount().getName());
            holder.textViewRateDate.setText(rateJoinUser.getUserRate().getRateDate());

            Integer plot = rateJoinUser.getUserRate().getPlotVote();
            Integer visualEffect = rateJoinUser.getUserRate().getVisualEffectVote();
            Integer soundEffect = rateJoinUser.getUserRate().getSoundEffectVote();
            Double average = (plot + visualEffect + soundEffect) / 3d;
            String avgStr = String.format("%.1f", average);
            holder.textViewRate.setText(avgStr);

            holder.textViewComment.setText(rateJoinUser.getUserRate().getComment());




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

            String path = rateJoinUser.getUserAccount().getAvatarPath();
            Picasso.get().load(path)
                    .placeholder(drawable)
                    .into(holder.imageViewAvatar);
        }
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName;
        private TextView textViewRateDate;
        private Button textViewRate;
        private TextView textViewComment;
        private ImageView imageViewAvatar;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewRateDate = itemView.findViewById(R.id.textViewRateDate);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewRate = itemView.findViewById(R.id.textViewRate);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }
}

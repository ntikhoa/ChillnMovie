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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.squareup.picasso.Picasso;

public class UserRateAdapter extends ListAdapter<UserRate, UserRateAdapter.RateViewHolder> {

    private Context context;
    FirebaseFirestore db;

    public UserRateAdapter(Context context) {
        super(UserRate.CALLBACK);
        this.context = context;

        db = FirebaseFirestore.getInstance();
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
        UserRate userRate = getItem(position);
        if (userRate != null) {
            db.collection(CollectionName.USER_PROFILE)
                    .document(userRate.getUserId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserAccount userAccount = documentSnapshot.toObject(UserAccount.class);

                            holder.textViewUserName.setText(userAccount.getName());
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

                            String path = userAccount.getAvatarPath();
                            Picasso.get().load(path)
                                    .placeholder(drawable)
                                    .into(holder.imageViewAvatar);

                            holder.textViewComment.setMaxHeight(113);//112.5px = 150dp
                        }
                    });

            holder.textViewRateDate.setText(userRate.getRateDate());

            setUserRate(userRate, holder);

            String comment = userRate.getComment();
            if (comment.length() < 200)
                holder.textViewComment.setText(userRate.getComment());
            else {
                String limit = comment.substring(0, 200) + " ...";
                holder.textViewComment.setText(limit);
            }
        }
    }

    private void setUserRate(UserRate userRate, RateViewHolder holder) {
        Integer plot = userRate.getPlotVote();
        Integer visualEffect = userRate.getVisualVote();
        Integer soundEffect = userRate.getAudioVote();
        Double average = (plot + visualEffect + soundEffect) / 3d;
        String avgStr = String.format("%.1f", average);
        holder.textViewRate.setText(avgStr);
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

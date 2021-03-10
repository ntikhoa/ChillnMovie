package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.databinding.UserRateItemBinding;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.squareup.picasso.Picasso;


public class UserRateAdapter extends ListAdapter<UserRate, UserRateAdapter.RateViewHolder> {

    private Context context;
    private FirebaseFirestore db;

    public UserRateAdapter(Context context) {
        super(UserRate.CALLBACK);
        this.context = context;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserRateItemBinding binding =
                UserRateItemBinding.inflate(inflater, parent, false);
        return new RateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        UserRate userRate = getItem(position);
        if (userRate != null) {
            holder.bind(userRate);
        }
    }

    class RateViewHolder extends RecyclerView.ViewHolder {

        private UserRateItemBinding binding;

        public RateViewHolder(UserRateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserRate userRate) {
            db.collection(CollectionName.USER_PROFILE)
                    .document(userRate.getUserId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        UserAccount userAccount = documentSnapshot.toObject(UserAccount.class);

                        binding.textViewUserName.setText(userAccount.getName());

                        ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
                        String path = userAccount.getAvatarPath();
                        Picasso.get().load(path)
                                .placeholder(shimmerEffect.getDrawable())
                                .into(binding.imageViewAvatar);

                        binding.textViewComment.setMaxHeight(113);//112.5px = 150dp
                    });

            binding.textViewRateDate.setText(userRate.getRateDate());

            setUserRate(userRate);

            String comment = userRate.getComment();
            if (comment.length() < 200)
                binding.textViewComment.setText(userRate.getComment());
            else {
                String limit = comment.substring(0, 200) + " ...";
                binding.textViewComment.setText(limit);
            }
        }

        private void setUserRate(UserRate userRate) {
            Integer plot = userRate.getPlotVote();
            Integer visualEffect = userRate.getVisualVote();
            Integer soundEffect = userRate.getAudioVote();
            Double average = (plot + visualEffect + soundEffect) / 3d;
            String avgStr = String.format("%.1f", average);
            binding.textViewRate.setText(avgStr);
        }
    }
}

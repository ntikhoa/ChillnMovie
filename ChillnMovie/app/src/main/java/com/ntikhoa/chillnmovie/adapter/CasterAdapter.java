package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
import com.ntikhoa.chillnmovie.model.Movie;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CasterAdapter extends ListAdapter<Caster, CasterAdapter.CasterViewHolder> {
    private final Context context;

    public CasterAdapter(Context context) {
        super(Caster.CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public CasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.caster_item, parent, false);
        return new CasterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CasterViewHolder holder, int position) {
        Caster caster = getItem(position);
        if (caster != null) {

            if (caster.getProfilePath() == null) {
                if (caster.getGender() == 0 || caster.getGender() == 2)
                    holder.imageViewProfile.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.male_default));
                else if (caster.getGender() == 1)
                    holder.imageViewProfile.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.female_default));
            } else {


                ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
                String path = Movie.path + caster.getProfilePath();
                Picasso.get().load(path)
                        .placeholder(shimmerEffect.getDrawable())
                        .into(holder.imageViewProfile);
            }
            holder.textViewCasterName.setText(caster.getName());
            holder.textViewCharacter.setText(caster.getCharacter());
        }
    }

    static class CasterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewProfile;
        TextView textViewCasterName, textViewCharacter;

        public CasterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.profile_image);
            textViewCasterName = itemView.findViewById(R.id.textViewCasterName);
            textViewCharacter = itemView.findViewById(R.id.textViewCharacter);
        }
    }
}

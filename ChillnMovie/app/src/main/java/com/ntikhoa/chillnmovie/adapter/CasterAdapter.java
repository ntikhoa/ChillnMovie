package com.ntikhoa.chillnmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.CasterItemBinding;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.ConstantShimmerEffect;
import com.ntikhoa.chillnmovie.model.Movie;
import com.squareup.picasso.Picasso;


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
        CasterItemBinding binding = CasterItemBinding.inflate(inflater, parent, false);
        return new CasterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CasterViewHolder holder, int position) {
        Caster caster = getItem(position);
        if (caster != null) {
            holder.bind(caster);
        }
    }

    class CasterViewHolder extends RecyclerView.ViewHolder {
        private CasterItemBinding binding;

        public CasterViewHolder(CasterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //add listener here
        }

        public void bind(Caster caster) {
            if (caster.getProfilePath() == null) {
                if (caster.getGender() == 0 || caster.getGender() == 2)
                    binding.profileImage.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.male_default));
                else if (caster.getGender() == 1)
                    binding.profileImage.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.female_default));
            } else {
                ConstantShimmerEffect shimmerEffect = new ConstantShimmerEffect(context);
                String path = Movie.path + caster.getProfilePath();
                Picasso.get().load(path)
                        .placeholder(shimmerEffect.getDrawable())
                        .into(binding.profileImage);
            }
            binding.textViewCasterName.setText(caster.getName());
            binding.textViewCharacter.setText(caster.getCharacter());
        }
    }
}

package com.ntikhoa.chillnmovie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.UserRate;

import java.text.Format;
import java.util.Formatter;

public class UserRateAdapter extends ListAdapter<UserRate, UserRateAdapter.RateViewHolder> {
    public UserRateAdapter() {
        super(UserRate.CALLBACK);
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
            holder.textViewUserName.setText("Quí Bửu");
            holder.textViewRateDate.setText(userRate.getRateDate());

            Integer plot = userRate.getPlotVote();
            Integer visualEffect = userRate.getVisualEffectVote();
            Integer soundEffect = userRate.getSoundEffectVote();
            Double average = (plot + visualEffect + soundEffect) / 3d;
            String avgStr = String.format("%.1f", average);
            holder.textViewRate.setText(avgStr);

            holder.textViewComment.setText(userRate.getComment());
        }
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName;
        private TextView textViewRateDate;
        private Button textViewRate;
        private TextView textViewComment;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewRateDate = itemView.findViewById(R.id.textViewRateDate);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewRate = itemView.findViewById(R.id.textViewRate);
        }
    }
}

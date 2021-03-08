package com.ntikhoa.chillnmovie.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.UserRateItemBinding;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    private static final String MOVIE_ID = "movie id";

    private UserRateItemBinding binding;

    private Long movieId;

    private UserRateViewModel viewModel;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ReviewFragment() {
        super(R.layout.user_rate_item);
    }

    public static ReviewFragment newInstance(Long movieId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getLong(MOVIE_ID);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = UserRateItemBinding.bind(view);

        initComponent();
        loadData();
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(getActivity())
                .get(UserRateViewModel.class);

        Drawable icVerify = ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_circle);
        binding.textViewUserName.setCompoundDrawablesWithIntrinsicBounds(icVerify, null, null, null);
        binding.textViewUserName.setCompoundDrawablePadding(6); //6px = 8dp
    }

    private void loadData() {
        viewModel.getMLDreview(movieId)
                .observe(getViewLifecycleOwner(), userRate -> {
                    if (userRate != null) {
                        binding.textViewComment.setText(userRate.getComment());
                        binding.textViewRateDate.setText(userRate.getRateDate());
                        setUserRate(userRate);
                        setUserInfo(userRate.getUserId());

                        binding.getRoot().setOnClickListener(v -> onClickListener.onClick());

                    } else {
                        binding.textViewEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setUserRate(UserRate userRate) {
        Integer plot = userRate.getPlotVote();
        Integer visualEffect = userRate.getVisualVote();
        Integer soundEffect = userRate.getAudioVote();
        Double average = (plot + visualEffect + soundEffect) / 3d;
        String avgStr = String.format("%.1f", average);
        binding.textViewRate.setText(avgStr);
    }

    private void setUserInfo(String userId) {
        viewModel.getMLDuserAccount(userId)
                .observe(getViewLifecycleOwner(), userAccount -> {
                    binding.textViewUserName.setText(userAccount.getName());
                    setAvatar(userAccount.getAvatarPath());
                });
    }

    private void setAvatar(String avatarPath) {
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(getActivity(), R.color.colorShimmerBase))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(getActivity(), R.color.colorShimmerHighlight))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .setDuration(500)
                .build();
        ShimmerDrawable drawable = new ShimmerDrawable();
        drawable.setShimmer(shimmer);

        Picasso.get().load(avatarPath)
                .placeholder(drawable)
                .into(binding.imageViewAvatar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface OnClickListener {
        void onClick();
    }
}
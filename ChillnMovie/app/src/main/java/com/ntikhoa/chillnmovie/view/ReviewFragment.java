package com.ntikhoa.chillnmovie.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;
import com.squareup.picasso.Picasso;

public class ReviewFragment extends Fragment {

    private static final String MOVIE_ID = "movie id";

    private Integer movieId;

    private UserRateViewModel viewModel;

    private TextView textViewUserName;
    private TextView textViewRateDate;
    private Button textViewRate;
    private TextView textViewComment;
    private ImageView imageViewAvatar;

    public static ReviewFragment newInstance(Integer movieId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.user_rate_item, container, false);
        initComponent(root);
        loadData();
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(UserRateViewModel.class);

        textViewUserName = root.findViewById(R.id.textViewUserName);
        textViewRateDate = root.findViewById(R.id.textViewRateDate);
        textViewComment = root.findViewById(R.id.textViewComment);
        textViewRate = root.findViewById(R.id.textViewRate);
        imageViewAvatar = root.findViewById(R.id.imageViewAvatar);

        Drawable icVerify = ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_circle);
        textViewUserName.setCompoundDrawablesWithIntrinsicBounds(icVerify, null, null, null);
        textViewUserName.setCompoundDrawablePadding(6); //6px = 8dp

        //for testing
        textViewUserName.setText("@realNtikhoa");
        imageViewAvatar.setImageResource(R.drawable.shinobu);
    }

    private void loadData() {
        viewModel.getMLDreview(movieId)
                .observe(this, new Observer<UserRate>() {
                    @Override
                    public void onChanged(UserRate userRate) {
                        textViewComment.setText(userRate.getComment());
                        textViewRateDate.setText(userRate.getRateDate());
                        setUserRate(userRate);
                        setUserInfo(userRate.getUserId());
                    }
                });
    }

    private void setUserRate(UserRate userRate) {
        Integer plot = userRate.getPlotVote();
        Integer visualEffect = userRate.getVisualVote();
        Integer soundEffect = userRate.getAudioVote();
        Double average = (plot + visualEffect + soundEffect) / 3d;
        String avgStr = String.format("%.1f", average);
        textViewRate.setText(avgStr);
    }

    private void setUserInfo(String userId) {
        viewModel.getMLDuserAccount(userId)
            .observe(this, new Observer<UserAccount>() {
                @Override
                public void onChanged(UserAccount userAccount) {
                    textViewUserName.setText(userAccount.getName());
                    setAvatar(userAccount.getAvatarPath());
                }
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
                .into(imageViewAvatar);
    }
}
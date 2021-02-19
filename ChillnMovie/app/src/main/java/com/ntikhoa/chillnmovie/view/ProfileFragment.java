package com.ntikhoa.chillnmovie.view;

import android.content.Intent;
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

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserModeSingleton;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private UserAccountViewModel viewModel;
    private Button btnLogout;
    private TextView textViewUserName;
    private TextView textViewEmail;
    private ImageView imageViewAvatar;

    private int mode = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mode = ((UserModeSingleton) getActivity().getApplication()).getMode();
        initComponent(root);
        loadData();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(this)
                .get(UserAccountViewModel.class);

        btnLogout = root.findViewById(R.id.btnLogout);
        textViewUserName = root.findViewById(R.id.textViewUserName);
        textViewEmail = root.findViewById(R.id.textViewEmail);
        imageViewAvatar = root.findViewById(R.id.imageViewAvatar);
    }

    private void loadData() {
        if (mode != 3)
            viewModel.getMLDuserAccount(FirebaseAuth.getInstance().getUid())
                    .observe(getViewLifecycleOwner(), new Observer<UserAccount>() {
                        @Override
                        public void onChanged(UserAccount userAccount) {
                            textViewUserName.setText(userAccount.getName());
                            textViewEmail.setText(userAccount.getEmail());
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
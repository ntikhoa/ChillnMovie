package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.UserRateAdapter;
import com.ntikhoa.chillnmovie.databinding.FragmentUserRateBinding;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserRateFragment extends Fragment {
    private static final String MOVIE_ID = "movie id";

    private FragmentUserRateBinding binding;

    private UserRateViewModel viewModel;

    private UserRateAdapter userRateAdapter;

    private Long movieId;

    public UserRateFragment() {
        super(R.layout.fragment_user_rate);
    }

    public static UserRateFragment newInstance(Long movieId) {
        UserRateFragment fragment = new UserRateFragment();
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
        binding = FragmentUserRateBinding.bind(view);

        initComponent();
        loadData();
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(getActivity())
                .get(UserRateViewModel.class);

        binding.recyclerViewUserRate.setLayoutManager(
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        userRateAdapter = new UserRateAdapter(getActivity());
        binding.recyclerViewUserRate.setAdapter(userRateAdapter);
    }

    private void loadData() {
        viewModel.getMLDuserRate(movieId).observe(getViewLifecycleOwner(), new Observer<List<UserRate>>() {
            @Override
            public void onChanged(List<UserRate> userRates) {
                if (userRates != null && userRates.size() != 0)
                    userRateAdapter.submitList(userRates);
                else binding.textViewEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
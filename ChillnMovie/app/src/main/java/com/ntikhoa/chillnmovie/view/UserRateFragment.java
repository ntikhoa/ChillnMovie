package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

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
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.viewmodel.UserRateViewModel;

import java.util.List;

public class UserRateFragment extends Fragment {
    private static final String MOVIE_ID = "movie id";

    private UserRateViewModel viewModel;
    private RecyclerView recyclerViewUserRate;
    private UserRateAdapter userRateAdapter;
    private TextView textViewEmpty;

    private Long movieId;

    public UserRateFragment() {
        //require default constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_rate, container, false);
        initComponent(root);
        loadData();
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(UserRateViewModel.class);

        recyclerViewUserRate = root.findViewById(R.id.recyclerViewUserRate);
        recyclerViewUserRate.setLayoutManager(
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        userRateAdapter = new UserRateAdapter(getActivity());
        recyclerViewUserRate.setAdapter(userRateAdapter);

        textViewEmpty = root.findViewById(R.id.textViewEmpty);
    }

    private void loadData() {
        viewModel.getMLDuserRate(movieId).observe(this, new Observer<List<UserRate>>() {
            @Override
            public void onChanged(List<UserRate> userRates) {
                if (userRates != null && userRates.size() != 0)
                    userRateAdapter.submitList(userRates);
                else textViewEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
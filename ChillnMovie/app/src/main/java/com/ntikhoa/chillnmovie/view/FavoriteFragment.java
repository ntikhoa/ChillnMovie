package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.FavoriteAdapter;
import com.ntikhoa.chillnmovie.viewmodel.FavoriteViewModel;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerViewFavorite;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteViewModel viewModel;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        initComponent(root);
        if (auth.getCurrentUser() != null)
            loadData(auth.getUid());
        return root;
    }

    private void initComponent(View root) {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(FavoriteViewModel.class);

        auth = FirebaseAuth.getInstance();

        recyclerViewFavorite = root.findViewById(R.id.recyclerViewFavorite);
        recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        favoriteAdapter = new FavoriteAdapter(getActivity());
        recyclerViewFavorite.setAdapter(favoriteAdapter);
    }

    private void loadData(String userId) {
        viewModel.getMLDmovieFavorite(userId)
                .observe(this, new Observer<List<Integer>>() {
                    @Override
                    public void onChanged(List<Integer> integers) {
                        favoriteAdapter.submitList(integers);
                    }
                });
    }
}
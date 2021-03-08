package com.ntikhoa.chillnmovie.view;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.adapter.FavoriteAdapter;
import com.ntikhoa.chillnmovie.databinding.FragmentFavoriteBinding;
import com.ntikhoa.chillnmovie.viewmodel.FavoriteViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;

    private FavoriteAdapter favoriteAdapter;
    private FavoriteViewModel viewModel;
    @Inject
    FirebaseAuth auth;

    public FavoriteFragment() {
        super(R.layout.fragment_favorite);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentFavoriteBinding.bind(view);
        initComponent();
        if (auth.getCurrentUser() != null)
            loadData(auth.getUid());
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this)
                .get(FavoriteViewModel.class);

        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        favoriteAdapter = new FavoriteAdapter(getActivity());
        binding.recyclerViewFavorite.setAdapter(favoriteAdapter);

        new ItemTouchHelper(mItemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewFavorite);
    }

    private void loadData(String userId) {
        viewModel.getMLDmovieFavorite(userId)
                .observe(getViewLifecycleOwner(),
                        movieId -> {
                            if (movieId != null && movieId.size() != 0)
                                favoriteAdapter.submitList(movieId);
                            else binding.textViewEmpty.setVisibility(View.VISIBLE);
                        });
    }

    private ItemTouchHelper.SimpleCallback mItemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(
                    0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setTitle("Are you sure you want to remove this movie");
                    builder.setNeutralButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                        favoriteAdapter.notifyDataSetChanged();
                    });
                    builder.setPositiveButton("Remove", (dialog, which) -> {
                        List<Long> currentList = favoriteAdapter.getCurrentList();
                        viewModel.removeMovieFromFavorite(auth.getUid(),
                                currentList.get(viewHolder.getAdapterPosition()));
                        viewModel.getMLDmovieFavorite(auth.getUid())
                                .observe(getActivity(), movieId -> favoriteAdapter.submitList(movieId));
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
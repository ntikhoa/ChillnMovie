package com.ntikhoa.chillnmovie.view;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

    private TextView textViewEmpty;

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

        new ItemTouchHelper(mItemTouchHelperCallback).attachToRecyclerView(recyclerViewFavorite);

        textViewEmpty = root.findViewById(R.id.textViewEmpty);
    }

    private void loadData(String userId) {
        viewModel.getMLDmovieFavorite(userId)
                .observe(this, new Observer<List<Long>>() {
                    @Override
                    public void onChanged(List<Long> movieId) {
                        if (movieId != null && movieId.size() != 0)
                            favoriteAdapter.submitList(movieId);
                        else textViewEmpty.setVisibility(View.VISIBLE);
                    }
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
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            favoriteAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Long> currentList = favoriteAdapter.getCurrentList();
                            viewModel.removeMovieFromFavorite(auth.getUid(),
                                    currentList.get(viewHolder.getAdapterPosition()));
                            viewModel.getMLDmovieFavorite(auth.getUid())
                                    .observe(getActivity(), new Observer<List<Long>>() {
                                        @Override
                                        public void onChanged(List<Long> movieId) {
                                            favoriteAdapter.submitList(movieId);
                                        }
                                    });
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            };
}
package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.repository.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository repository;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepository(application);
    }

    public MutableLiveData<List<Integer>> getMLDmovieFavorite(String userId) {
        return repository.getMLDmovieFavorite(userId);
    }
}

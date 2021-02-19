package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntikhoa.chillnmovie.repository.FavoriteRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoriteViewModel extends ViewModel {

    private FavoriteRepository repository;

    @Inject
    public FavoriteViewModel(FavoriteRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<Long>> getMLDmovieFavorite(String userId) {
        return repository.getMLDmovieFavorite(userId);
    }

    public void removeMovieFromFavorite(String uid, Long movieId) {
        repository.removeMovieFromFavorite(uid, movieId);
    }
}

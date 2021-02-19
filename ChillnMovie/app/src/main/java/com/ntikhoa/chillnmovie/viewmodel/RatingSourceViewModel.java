package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.repository.RatingSourceRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RatingSourceViewModel extends AndroidViewModel {
    private final RatingSourceRepository repository;

    @Inject
    public RatingSourceViewModel(@NonNull Application application, RatingSourceRepository repository) {
        super(application);
        this.repository = repository;
    }

    public MutableLiveData<RatingSource> getMLDratingSource(String id) {
        return repository.getMLDratingSource(id);
    }

    public MutableLiveData<MovieRate> getMLDmovieRate(Long id) {
        return repository.getMLDmovieRate(id);
    }
}

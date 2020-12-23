package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.repository.RatingSourceRepository;

public class RatingSourceViewModel extends AndroidViewModel {
    private final RatingSourceRepository repository;

    public RatingSourceViewModel(@NonNull Application application) {
        super(application);
        repository = new RatingSourceRepository(application);
    }

    public MutableLiveData<RatingSource> getMLDratingSource(String id) {
        return repository.getMLDratingSource(id);
    }

    public MutableLiveData<MovieRate> getMLDmovieRate(Integer id) {
        return repository.getMLDmovieRate(id);
    }
}

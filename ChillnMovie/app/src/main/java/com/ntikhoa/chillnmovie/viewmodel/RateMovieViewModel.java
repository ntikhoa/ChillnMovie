package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.repository.RateMovieRepository;

public class RateMovieViewModel extends AndroidViewModel {
    private Application application;
    private MutableLiveData<MovieDetail> MLDmovieDetail;
    private final RateMovieRepository repository;

    public RateMovieViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new RateMovieRepository(application);
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Integer id) {
        return repository.getMLDmovieDetail(id);
    }
}

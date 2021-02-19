package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.repository.RateMovieRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RateMovieViewModel extends AndroidViewModel {
    private Application application;
    private final RateMovieRepository repository;

    @Inject
    public RateMovieViewModel(@NonNull Application application, RateMovieRepository repository) {
        super(application);
        this.application = application;
        this.repository = repository;
    }

    public MutableLiveData<Boolean> rateMovie(Long id, UserRate newUserRate) {
        return repository.rateMovie(id, newUserRate);
    }

    public MutableLiveData<Boolean> reviewMovie(Long id, UserRate review) {
        return repository.reviewMovie(id, review);
    }
}

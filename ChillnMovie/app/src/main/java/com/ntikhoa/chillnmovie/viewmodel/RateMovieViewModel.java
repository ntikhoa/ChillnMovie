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

public class RateMovieViewModel extends AndroidViewModel {
    private Application application;
    private final RateMovieRepository repository;

    public RateMovieViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new RateMovieRepository(application);
    }

    public MutableLiveData<Boolean> rateMovie(Long id, UserRate newUserRate) {
        return repository.rateMovie(id, newUserRate);
    }

    public MutableLiveData<Boolean> reviewMovie(Long id, UserRate review) {
        return repository.reviewMovie(id, review);
    }
}

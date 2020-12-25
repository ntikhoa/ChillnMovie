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

    public MutableLiveData<MovieRate> getMLDmovieRate(Integer id) {
        return repository.getMLDmovieRate(id);
    }

    public void addUserRate(Integer movieId, UserRate userRate) {
        repository.addUserRate(movieId, userRate);
    }

    public MutableLiveData<UserRate> getMLDuserRate(Integer movieId, String userId) {
        return repository.getMLDuserRate(movieId, userId);
    }

    public void updateMovieRate(Integer movieId, MovieRate movieRate) {
        repository.updateMovieRate(movieId, movieRate);
    }
}

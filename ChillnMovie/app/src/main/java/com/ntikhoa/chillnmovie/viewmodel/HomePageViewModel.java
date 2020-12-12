package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.repository.HomePageRepository;

import java.util.List;

public class HomePageViewModel extends AndroidViewModel {
    private Application application;
    private final HomePageRepository repository;

    private final MutableLiveData<List<Movie>> MLDtopRatedMovie;
    private final MutableLiveData<List<Movie>> MLDupcomingMovie;
    private final MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    private final MutableLiveData<List<Movie>> MLDtrendingMovie;



    public HomePageViewModel(@NonNull Application application) {
        super(application);
        repository = new HomePageRepository(application);
        MLDtopRatedMovie = repository.getMLDtopRatedMovie();
        MLDnowPlayingMovie = repository.getMLDnowPlaying();
        MLDupcomingMovie = repository.getMLDupcoming();
        MLDtrendingMovie = repository.getMLDtrending();
    }

    public MutableLiveData<List<Movie>> getMLDtopRatedMovie() {
        return MLDtopRatedMovie;
    }

    public MutableLiveData<List<Movie>> getMLDupcomingMovie() {
        return MLDupcomingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDnowPlayingMovie() {
        return MLDnowPlayingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDtrendingMovie() {
        return MLDtrendingMovie;
    }
}

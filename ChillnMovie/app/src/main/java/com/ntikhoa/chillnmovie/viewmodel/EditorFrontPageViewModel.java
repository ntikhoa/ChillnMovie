package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.repository.EditorFrontPageRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditorFrontPageViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Movie>> MLDtrendingMovie;
    private final MutableLiveData<List<Movie>> MLDpopularMovie;
    private final MutableLiveData<List<Movie>> MLDupcomingMovie;
    private final MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    private final MutableLiveData<List<Movie>> MLDtopRatedMovie;

    private EditorFrontPageRepository repository;

    @Inject
    public EditorFrontPageViewModel(@NonNull Application application, EditorFrontPageRepository repository) {
        super(application);
        this.repository = repository;

        MLDtrendingMovie = repository.getMLDtrendingMovie();
        MLDpopularMovie = repository.getMLDpopularMovie();
        MLDupcomingMovie = repository.getMLDupcomingMovie();
        MLDnowPlayingMovie = repository.getMLDnowPlayingMovie();
        MLDtopRatedMovie = repository.getMLDtopRatedMovie();
    }

    //mutable live data for front page (main) activity
    public MutableLiveData<List<Movie>> getMLDtrendingMovie() {
        return MLDtrendingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDpopularMovie() {
        return MLDpopularMovie;
    }

    public MutableLiveData<List<Movie>> getMLDupcomingMovie() {
        return MLDupcomingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDnowPlayingMovie() {
        return MLDnowPlayingMovie;
    }

    public MutableLiveData<List<Movie>> getMLDtopRatedMovie() {
        return MLDtopRatedMovie;
    }
}

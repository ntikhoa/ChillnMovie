package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.repository.HomePageRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomePageViewModel extends ViewModel {
    private final HomePageRepository repository;

    private final MutableLiveData<List<Movie>> MLDtopRatedMovie;
    private final MutableLiveData<List<Movie>> MLDupcomingMovie;
    private final MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    private final MutableLiveData<List<Movie>> MLDtrendingMovie;
    private final MutableLiveData<List<Movie>> MLDvietnameseMovie;

    @Inject
    public HomePageViewModel(HomePageRepository repository) {
        this.repository = repository;

        MLDtopRatedMovie = repository.getMLDtopRatedMovie();
        MLDnowPlayingMovie = repository.getMLDnowPlaying();
        MLDupcomingMovie = repository.getMLDupcoming();
        MLDtrendingMovie = repository.getMLDtrending();
        MLDvietnameseMovie = repository.getMLDvietnamese();
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

    public MutableLiveData<List<Movie>> getMLDvietnameseMovie() {
        return MLDvietnameseMovie;
    }
}

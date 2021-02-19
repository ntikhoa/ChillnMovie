package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.ntikhoa.chillnmovie.api.MovieAPI;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieSearchDataSourceFactory extends DataSource.Factory {

    private Application application;
    private MovieSearchDataSource movieSearchDataSource;
    private MutableLiveData<MovieSearchDataSource> mutableLiveData;
    private String search;
    private MovieAPI movieApi;

    @Inject
    public MovieSearchDataSourceFactory(Application application, MovieAPI movieApi, String search) {
        this.application = application;
        mutableLiveData = new MutableLiveData<>();
        this.search = search;
        this.movieApi = movieApi;
    }

    @NonNull
    @Override
    public DataSource create() {
        movieSearchDataSource = new MovieSearchDataSource(application, movieApi, search);
        mutableLiveData.postValue(movieSearchDataSource);
        return movieSearchDataSource;
    }
}

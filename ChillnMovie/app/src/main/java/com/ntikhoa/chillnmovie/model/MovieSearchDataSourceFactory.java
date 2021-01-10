package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieSearchDataSourceFactory extends DataSource.Factory{

    private Application application;
    private MovieSearchDataSource movieSearchDataSource;
    private MutableLiveData<MovieSearchDataSource> mutableLiveData;
    private String search;

    public MovieSearchDataSourceFactory(Application application, String search) {
        this.application = application;
        mutableLiveData = new MutableLiveData<>();
        this.search = search;
    }

    @NonNull
    @Override
    public DataSource create() {
        movieSearchDataSource = new MovieSearchDataSource(application, search);
        mutableLiveData.postValue(movieSearchDataSource);
        return movieSearchDataSource;
    }
}

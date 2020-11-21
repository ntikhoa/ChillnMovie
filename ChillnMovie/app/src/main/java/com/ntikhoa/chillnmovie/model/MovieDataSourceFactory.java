package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory {

    private Application application;
    private MovieDataSource movieDataSource;
    private MutableLiveData<MovieDataSource> mutableLiveData;
    private int category;

    public MovieDataSourceFactory(Application application, int category) {
        this.application = application;
        mutableLiveData = new MutableLiveData<>();
        this.category = category;
    }

    @NonNull
    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource(application, category);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}

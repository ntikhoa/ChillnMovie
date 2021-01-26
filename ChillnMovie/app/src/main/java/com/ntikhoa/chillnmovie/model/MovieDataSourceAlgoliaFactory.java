package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceAlgoliaFactory extends DataSource.Factory {

    private Application application;
    private MovieDataSourceAlgolia movieDataSource;
    private MutableLiveData<MovieDataSourceAlgolia> mutableLiveData;
    private String search;

    public MovieDataSourceAlgoliaFactory(Application application, String search) {
        this.application = application;
        this.search = search;
        mutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSourceAlgolia(application, search);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}

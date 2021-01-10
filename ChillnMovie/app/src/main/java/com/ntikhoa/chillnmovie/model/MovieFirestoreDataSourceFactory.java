package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class MovieFirestoreDataSourceFactory extends DataSource.Factory {

    private Application application;
    private MovieFirestoreDataSource dataSource;
    private MutableLiveData<PageKeyedDataSource> liveData;

    public MovieFirestoreDataSourceFactory(Application application) {
        this.application = application;
        liveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        dataSource = new MovieFirestoreDataSource(application);
        liveData.postValue(dataSource);
        return dataSource;
    }
}

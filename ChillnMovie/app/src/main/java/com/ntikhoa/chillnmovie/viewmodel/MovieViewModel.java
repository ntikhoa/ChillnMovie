package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDataSource;
import com.ntikhoa.chillnmovie.model.MovieDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieViewModel extends AndroidViewModel {
    private final Application application;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    //page list for movie activity
    public LiveData<PagedList<Movie>> getMoviePagedListLiveData(int category) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(application, category);
        //LiveData<MovieDataSource> movieDataSourceLiveData = factory.getMutableLiveData();
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

        Executor executor = Executors.newFixedThreadPool(5);

        LiveData<PagedList<Movie>> moviePagedListLiveData = (new LivePagedListBuilder<Integer, Movie>(factory, config))
                .setFetchExecutor(executor)
                .build();

        return moviePagedListLiveData;
    }
}

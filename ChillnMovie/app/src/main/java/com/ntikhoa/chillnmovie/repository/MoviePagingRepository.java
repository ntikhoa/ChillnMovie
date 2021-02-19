package com.ntikhoa.chillnmovie.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDataSourceFactory;
import com.ntikhoa.chillnmovie.model.MovieFirestoreDataSourceFactory;
import com.ntikhoa.chillnmovie.service.MovieAPI;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoviePagingRepository {
    private Application application;
    private MovieAPI movieApi;

    @Inject
    public MoviePagingRepository (Application application, MovieAPI movieApi) {
        this.application = application;
        this.movieApi = movieApi;
    }

    public LiveData<PagedList<Movie>> getMoviePagedListLiveData(int category) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(application, movieApi,category);
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

    public LiveData<PagedList<Movie>> getMoviePagedListFirestore() {
        MovieFirestoreDataSourceFactory factory = new MovieFirestoreDataSourceFactory(application);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

        Executor executor = Executors.newFixedThreadPool(5);

        LiveData<PagedList<Movie>> moviePagedListLiveData = (new LivePagedListBuilder<DocumentSnapshot, Movie>(factory, config))
                .setFetchExecutor(executor)
                .build();

        return moviePagedListLiveData;
    }
}

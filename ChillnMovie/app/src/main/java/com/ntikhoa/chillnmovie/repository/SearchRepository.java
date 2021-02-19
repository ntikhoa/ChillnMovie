package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDataSourceAlgoliaFactory;
import com.ntikhoa.chillnmovie.model.MovieSearchDataSourceFactory;
import com.ntikhoa.chillnmovie.api.MovieAPI;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SearchRepository {
    private Application application;
    private MovieAPI movieApi;

    @Inject
    public SearchRepository(Application application, MovieAPI movieApi) {
        this.application = application;
        this.movieApi = movieApi;
    }

    public LiveData<PagedList<Movie>> getMLDmoviesFromTMDB(String search) {
        MovieSearchDataSourceFactory factory = new MovieSearchDataSourceFactory(application, movieApi,search);

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

    public LiveData<PagedList<Movie>> getMLDmoviesFromFirestore(String search) {
        MovieDataSourceAlgoliaFactory factory = new MovieDataSourceAlgoliaFactory(application, search);

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

package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDataSource;
import com.ntikhoa.chillnmovie.model.MovieDataSourceFactory;
import com.ntikhoa.chillnmovie.model.Repository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieViewModel extends AndroidViewModel {
    private Application application;
    private Repository repository;

    private MutableLiveData<List<Movie>> MLDtrendingMovie;
    private MutableLiveData<List<Movie>> MLDpopularMovie;
    private MutableLiveData<List<Movie>> MLDupcomingMovie;
    private MutableLiveData<List<Movie>> MLDnowPlayingMovie;
    private MutableLiveData<List<Movie>> MLDtopRatedMovie;

    private LiveData<MovieDataSource> movieDataSourceLiveData;
    private LiveData<PagedList<Movie>> moviePagedListLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new Repository(application);
        MLDtrendingMovie = repository.getMLDtrendingMovie();
        MLDpopularMovie = repository.getMLDpopularMovie();
        MLDupcomingMovie = repository.getMLDupcomingMovie();
        MLDnowPlayingMovie = repository.getMLDnowPlayingMovie();
        MLDtopRatedMovie = repository.getMLDtopRatedMovie();
    }

    //mutable live data for front page (main) activity
    public MutableLiveData getMLDtrendingMovie() {
        return MLDtrendingMovie;
    }

    public MutableLiveData getMLDpopularMovie() {
        return MLDpopularMovie;
    }

    public MutableLiveData getMLDupcomingMovie() {
        return MLDupcomingMovie;
    }

    public MutableLiveData getMLDnowPlayingMovie() {
        return MLDnowPlayingMovie;
    }

    public MutableLiveData getMLDtopRatedMovie() {
        return MLDtopRatedMovie;
    }

    //page list for movie activity
    public LiveData<PagedList<Movie>> getMoviePagedListLiveData(int category) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(application, category);
        movieDataSourceLiveData = factory.getMutableLiveData();
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

        Executor executor = Executors.newFixedThreadPool(5);

        moviePagedListLiveData = (new LivePagedListBuilder<Integer, Movie>(factory, config))
                .setFetchExecutor(executor)
                .build();

        return moviePagedListLiveData;
    }
}

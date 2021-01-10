package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDataSource;
import com.ntikhoa.chillnmovie.model.MovieDataSourceFactory;
import com.ntikhoa.chillnmovie.model.MovieFirestoreDataSource;
import com.ntikhoa.chillnmovie.model.MovieFirestoreDataSourceFactory;
import com.ntikhoa.chillnmovie.repository.MoviePagingRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieViewModel extends AndroidViewModel {
    private final Application application;
    private final MoviePagingRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new MoviePagingRepository(application);
    }

    //page list for movie activity
    public LiveData<PagedList<Movie>> getMoviePagedListLiveData(int category) {
        return repository.getMoviePagedListLiveData(category);
    }

    public LiveData<PagedList<Movie>> getMoviePagedListFirestore() {
        return repository.getMoviePagedListFirestore();
    }
}

package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieSearchDataSource;
import com.ntikhoa.chillnmovie.model.MovieSearchDataSourceFactory;
import com.ntikhoa.chillnmovie.repository.SearchRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private SearchRepository repository;

    @Inject
    public SearchViewModel(SearchRepository repository) {
        this.repository = repository;
    }

    public LiveData<PagedList<Movie>> getMLDmoviesFromTMDB(String search) {
        return repository.getMLDmoviesFromTMDB(search);
    }

    public LiveData<PagedList<Movie>> getMLDmoviesFromFirestore(String search) {
        return repository.getMLDmoviesFromFirestore(search);
    }
}

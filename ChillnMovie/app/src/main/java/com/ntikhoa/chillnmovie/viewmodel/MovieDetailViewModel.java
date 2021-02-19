package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.repository.MovieDetailRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieDetailViewModel extends AndroidViewModel {

    private final MovieDetailRepository repository;

    @Inject
    public MovieDetailViewModel(@NonNull Application application, MovieDetailRepository repository) {
        super(application);
        this.repository = repository;
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Long id) {
        return repository.getMLDmovieDetail(id);
    }

    public MutableLiveData<List<Caster>> getMLDcaster(Long id) {
        return repository.getMLDcaster(id);
    }

    public void addToDatabase(MovieDetail movieDetail) {
        repository.addToFirestore(movieDetail);
    }

    public void addToFavorite(String userId, Long movieId) {
        repository.addToFavorite(userId, movieId);
    }
}

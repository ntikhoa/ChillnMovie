package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.repository.MovieDetailRepository;

import java.util.List;

public class MovieDetailViewModel extends AndroidViewModel {

    private final MovieDetailRepository repository;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieDetailRepository(application);
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

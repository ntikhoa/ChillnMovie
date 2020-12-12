package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.repository.EditMovieRepository;

public class EditMovieViewModel extends AndroidViewModel {

    private EditMovieRepository repository;

    public EditMovieViewModel(@NonNull Application application) {
        super(application);
        repository = new EditMovieRepository(application);
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Integer id) {
        return repository.getMLDmovieDetail(id);
    }

    public void addToDatabase(MovieDetail movieDetail) {
        repository.addToFirestore(movieDetail);
    }

    public void addToCategoryList(MovieDetail movieDetail, String collectionPath) {
        repository.addToCategoryList(movieDetail, collectionPath);
    }

    public void removeFromCategoryList(MovieDetail movieDetail, String collectionPath) {
        repository.removeFromCategoryList(movieDetail, collectionPath);
    }

    public MutableLiveData<Boolean> isTrendingExist(Integer id) {
        return repository.isTrendingExist(id);
    }

    public MutableLiveData<Boolean> isNowPlayingExist(Integer id) {
        return repository.isNowPlayingExist(id);
    }

    public MutableLiveData<Boolean> isUpcomingExist(Integer id) {
        return repository.isUpcomingExist(id);
    }
}

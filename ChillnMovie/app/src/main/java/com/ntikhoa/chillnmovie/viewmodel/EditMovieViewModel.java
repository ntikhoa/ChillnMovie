package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;
import android.net.Uri;

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

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Long id) {
        return repository.getMLDmovieDetail(id);
    }

    public MutableLiveData<Boolean> updateToDatabase(
            MovieDetail movieDetail,
            boolean trending,
            boolean upcoming,
            boolean nowPlaying) {
        return repository.updateToDatabase(movieDetail, trending, upcoming, nowPlaying);
    }

    public MutableLiveData<Boolean> isTrendingExist(Long id) {
        return repository.isTrendingExist(id);
    }

    public MutableLiveData<Boolean> isNowPlayingExist(Long id) {
        return repository.isNowPlayingExist(id);
    }

    public MutableLiveData<Boolean> isUpcomingExist(Long id) {
        return repository.isUpcomingExist(id);
    }

    public MutableLiveData<String> uploadImage(Uri imageUri, int mode) {
        return repository.uploadImage(imageUri, mode);
    }
}

package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.repository.EditMovieRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditMovieViewModel extends ViewModel {

    private EditMovieRepository repository;

    @Inject
    public EditMovieViewModel(EditMovieRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Long id) {
        return repository.getMLDmovieDetail(id);
    }

    public MutableLiveData<Boolean> updateToDatabase(MovieDetail movieDetail) {
        return repository.updateToDatabase(movieDetail);
    }

    public MutableLiveData<String> uploadImage(Uri imageUri, int mode) {
        return repository.uploadImage(imageUri, mode);
    }
}

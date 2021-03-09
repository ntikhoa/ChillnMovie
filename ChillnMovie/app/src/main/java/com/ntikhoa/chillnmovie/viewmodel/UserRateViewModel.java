package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.repository.UserRateRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserRateViewModel extends ViewModel {

    private UserRateRepository repository;

    @Inject
    public UserRateViewModel(UserRateRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<UserRate>> getMLDuserRate(Long movideId) {
        return repository.getMLDuserRate(movideId);
    }

    public MutableLiveData<UserRate> getMLDreview(Long movieId) {
        return repository.getMLDreview(movieId);
    }

    public MutableLiveData<UserAccount> getMLDuserAccount(String userId) {
        return repository.getMLDuserAccount(userId);
    }
}

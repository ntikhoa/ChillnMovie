package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.repository.UserRateRepository;

import java.util.List;

public class UserRateViewModel extends AndroidViewModel {

    private Application application;
    private UserRateRepository repository;

    public UserRateViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new UserRateRepository(application);
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

package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.repository.UserAccountRepository;

public class UserAccountViewModel extends AndroidViewModel {
    private final UserAccountRepository repository;

    public UserAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new UserAccountRepository(application);
    }

    public void login(String email, String password) {
        repository.login(email, password);
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        return repository.signUp(email, password);
    }
}

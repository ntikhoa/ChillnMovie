package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ntikhoa.chillnmovie.repository.UserAccountRepository;

public class UserAccountViewModel extends AndroidViewModel {
    private UserAccountRepository repository;

    public UserAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new UserAccountRepository(application);
    }

    public void login(String email, String password) {
        repository.login(email, password);
    }
}

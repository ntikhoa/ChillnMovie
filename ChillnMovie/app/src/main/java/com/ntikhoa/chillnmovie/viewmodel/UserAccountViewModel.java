package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.repository.UserAccountRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserAccountViewModel extends AndroidViewModel {
    private final UserAccountRepository repository;

    @Inject
    public UserAccountViewModel(@NonNull Application application, UserAccountRepository repository) {
        super(application);
        this.repository = repository;
    }

    public MutableLiveData<Boolean> login(String email, String password) {
        return repository.login(email, password);
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        return repository.signUp(email, password);
    }

    public MutableLiveData<Boolean> createUserInfo(UserAccount userAccount, String userId) {
        return repository.createUserInfo(userAccount, userId);
    }

    public MutableLiveData<Boolean> uploadAvatar(Uri imageUri, String userId) {
        return repository.uploadAvatar(imageUri, userId);
    }

    public MutableLiveData<Boolean> setUserMode(String userId) {
        return repository.setUserMode(userId);
    }

    public MutableLiveData<UserAccount> getMLDuserAccount(String userId) {
        return repository.getMLDuserAccount(userId);
    }
}

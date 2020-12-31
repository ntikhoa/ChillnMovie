package com.ntikhoa.chillnmovie.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.repository.UserAccountRepository;

public class UserAccountViewModel extends AndroidViewModel {
    private final UserAccountRepository repository;

    public UserAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new UserAccountRepository(application);
    }

    public MutableLiveData<Boolean> login(String email, String password) {
        return repository.login(email, password);
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        return repository.signUp(email, password);
    }

    public void uploadAvatar(Uri imageUri, String userId) {
        repository.uploadAvatar(imageUri, userId);
    }

    public void createUserProfile(UserAccount userAccount, String userId) {
        repository.createUserProfile(userAccount, userId);
    }

    public MutableLiveData<Boolean> initUserData(String uid, String email) {
        return repository.initUserData(uid, email);
    }

//    public void initUserProfile(String uid, String email) {
//        repository.initUserProfile(uid, email);
//    }
//
//    public void initUserFavoriteList(String uid){
//        repository.initUserFavoriteList(uid);
//    }
}

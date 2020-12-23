package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.UserAccount;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserAccountRepository {
    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private MutableLiveData<Boolean> isSuccess;

    public UserAccountRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        isSuccess = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserAccountProfile(mAuth.getUid(), email);
                            isSuccess.postValue(true);
                        } else {
                            Toast.makeText(application, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            isSuccess.postValue(false);
                        }
                    }
                });
        return isSuccess;
    }

    public void addUserAccountProfile(String uid, String email) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        UserAccount userAccount = new UserAccount(email, currentStr, UserAccount.USER);
        firestore.collection(CollectionName.USER_PROFILE)
                .document(uid)
                .set(userAccount);
    }
}

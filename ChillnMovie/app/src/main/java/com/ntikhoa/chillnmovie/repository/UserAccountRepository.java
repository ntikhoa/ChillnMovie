package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.UserAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAccountRepository {
    private final Application application;
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final MutableLiveData<Boolean> isSignupSuccess;
    private final MutableLiveData<Boolean> isLoginSuccess;
    private final MutableLiveData<Boolean> isInitSuccess;

    public UserAccountRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        isSignupSuccess = new MutableLiveData<>();
        isLoginSuccess = new MutableLiveData<>();
        isInitSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            isLoginSuccess.postValue(true);
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            isLoginSuccess.postValue(false);
                        }
                    }
                });
        return isLoginSuccess;
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        isSignupSuccess.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(application, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        isSignupSuccess.postValue(false);
                    }
                });
        return isSignupSuccess;
    }

    public MutableLiveData<Boolean> initUserData(String uid, String email) {
        Task taskInitUserProfile = initUserProfile(uid, email);
        Task taskInitUserFavoriteList = initUserFavoriteList(uid);
        Task combineTask = Tasks.whenAllSuccess(taskInitUserFavoriteList, taskInitUserProfile)
                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        isInitSuccess.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isInitSuccess.postValue(false);
                    }
                });
        return isInitSuccess;
    }


    private Task initUserProfile(String uid, String email) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        UserAccount userAccount = new UserAccount(email, currentStr, UserAccount.USER);
        userAccount.setName("Anonymous");
        return db.collection(CollectionName.USER_PROFILE)
                .document(uid)
                .set(userAccount);
    }

    private Task initUserFavoriteList(String uid) {
        Map<String, Object> map = new HashMap<>();
        List<String> favorite = new ArrayList<>();
        map.put("favorite_list", favorite);
        return db.collection(CollectionName.USER_FAVORITE)
                .document(uid)
                .set(map);
    }

    public void uploadAvatar(Uri imageUri, String userId) {
        StorageReference avatarRef = storage.getReference().child(CollectionName.IMAGE_AVATAR + System.currentTimeMillis()
                + "." + getFileExtension(imageUri));

        avatarRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri downloadUri = task.getResult();
                                        String downloadUrl = downloadUri.toString();
                                        updateUserAccountAvatar(userId, downloadUrl);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showMessage(exception.getMessage());
                    }
                });
    }

    public void createUserProfile(UserAccount userAccount, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("birthdate", userAccount.getBirthdate());
        map.put("country", userAccount.getCountry());
        map.put("gender", userAccount.getGender());
        map.put("name", userAccount.getName());
        db.collection(CollectionName.USER_PROFILE)
                .document(userId)
                .update(map);
    }

    private void updateUserAccountAvatar(String userId, String downloadUrl) {
        db.collection(CollectionName.USER_PROFILE)
                .document(userId)
                .update("avatarPath", downloadUrl);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = application.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserModeSingleton;

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
    private final MutableLiveData<Integer> userMode;
    private final MutableLiveData<Boolean> isSignupSuccess;
    private final MutableLiveData<Boolean> isLoginSuccess;
    private final MutableLiveData<Boolean> isCreateInfoSuccess;
    private final MutableLiveData<Boolean> isUploadImageSuccess;

    private final MutableLiveData<UserAccount> MLDuserAccount;

    //login signup

    public UserAccountRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        isSignupSuccess = new MutableLiveData<>();
        isLoginSuccess = new MutableLiveData<>();
        isCreateInfoSuccess = new MutableLiveData<>();
        isUploadImageSuccess = new MutableLiveData<>();
        userMode = new MutableLiveData<>();
        MLDuserAccount = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        setUserMode(user.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isLoginSuccess.postValue(false);
                    }
                });

        return isLoginSuccess;
    }

    public MutableLiveData<Boolean> setUserMode(String userId) {
        db.collection(CollectionName.USER_PROFILE)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String modeStr = documentSnapshot.get("typeAccount").toString();
                        Integer mode = Integer.parseInt(modeStr);
                        UserModeSingleton.getInstance().setMode(mode);
                        isLoginSuccess.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isLoginSuccess.postValue(false);
            }
        });
        return isLoginSuccess;
    }

    public MutableLiveData<Boolean> signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        initUserData(authResult.getUser().getUid(), email);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSignupSuccess.postValue(false);
            }
        });
        return isSignupSuccess;
    }

    private void initUserData(String uid, String email) {
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                initUserProfile(uid, email, transaction);
                initUserFavoriteList(uid, transaction);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isSignupSuccess.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSignupSuccess.postValue(false);
            }
        });
    }

    private void initUserProfile(String uid, String email, Transaction transaction) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        UserAccount userAccount = new UserAccount(email, currentStr, UserAccount.USER);
        userAccount.setName("Anonymous");
        DocumentReference userProfileRef = db.collection(CollectionName.USER_PROFILE)
                .document(uid);
        transaction.set(userProfileRef, userAccount);
    }

    private void initUserFavoriteList(String uid, Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        List<String> favorite = new ArrayList<>();
        map.put("favorite_list", favorite);
        DocumentReference userFavoriteRef = db.collection(CollectionName.USER_FAVORITE)
                .document(uid);
        transaction.set(userFavoriteRef, map);
    }


    //create user profile

    public MutableLiveData<Boolean> createUserInfo(UserAccount userAccount, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("birthdate", userAccount.getBirthdate());
        map.put("country", userAccount.getCountry());
        map.put("gender", userAccount.getGender());
        map.put("name", userAccount.getName());
        db.collection(CollectionName.USER_PROFILE)
                .document(userId)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isCreateInfoSuccess.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isCreateInfoSuccess.postValue(false);
            }
        });
        return isCreateInfoSuccess;
    }

    public MutableLiveData<Boolean> uploadAvatar(Uri imageUri, String userId) {
        StorageReference avatarRef = storage.getReference().child(CollectionName.IMAGE_AVATAR + System.currentTimeMillis()
                + "." + getFileExtension(imageUri));

        avatarRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        avatarRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        updateUserAccountAvatar(userId, uri.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        isUploadImageSuccess.postValue(false);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isUploadImageSuccess.postValue(false);
            }
        });

        return isUploadImageSuccess;
    }

    private void updateUserAccountAvatar(String userId, String downloadUrl) {
        db.collection(CollectionName.USER_PROFILE)
                .document(userId)
                .update("avatarPath", downloadUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isUploadImageSuccess.postValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isUploadImageSuccess.postValue(false);
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = application.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public MutableLiveData<UserAccount> getMLDuserAccount(String uid) {
        db.collection(CollectionName.USER_PROFILE)
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MLDuserAccount.postValue(documentSnapshot.toObject(UserAccount.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDuserAccount;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

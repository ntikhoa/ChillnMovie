package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RateMovieRepository {
    private final MutableLiveData<MovieRate> MLDmovieRate;
    private final MutableLiveData<UserRate> MLDuserRate;
    private final Application application;
    private final FirebaseFirestore db;

    public RateMovieRepository(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
        MLDmovieRate = new MutableLiveData<>();
        MLDuserRate = new MutableLiveData<>();
    }

    public MutableLiveData<MovieRate> getMLDmovieRate(Integer id) {
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(id))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            MLDmovieRate.postValue(documentSnapshot.toObject(MovieRate.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDmovieRate;
    }

    public void addUserRate(Integer movieId, UserRate userRate) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        userRate.setRateDate(currentStr);
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieId))
                .collection(CollectionName.USER_RATE)
                .document(userRate.getUserId())
                .set(userRate)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public MutableLiveData<UserRate> getMLDuserRate(Integer movieId, String userId) {
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieId))
                .collection(CollectionName.USER_RATE)
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                MLDuserRate.postValue(task.getResult().toObject(UserRate.class));
                            } else MLDuserRate.postValue(null);
                        }
                    }
                });
        return MLDuserRate;
    }

    public void updateMovieRate(Integer movieId, MovieRate movieRate) {
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieId))
                .set(movieRate)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

        Map<String, Object> map = new HashMap<>();
        map.put("voteAverage", movieRate.getVoteAverage());
        map.put("voteCount", movieRate.getVoteCount());

        updateAllCategory(movieId, map);
    }

    private void updateAllCategory(Integer movieId, Map<String, Object> map) {
        db.collection(CollectionName.MOVIE)
                .document(String.valueOf(movieId))
                .update(map);
        db.collection(CollectionName.MOVIE_DETAIL)
                .document(String.valueOf(movieId))
                .update(map);
        db.collection(CollectionName.MOVIE_TRENDING)
                .document(String.valueOf(movieId))
                .update(map);
        db.collection(CollectionName.MOVIE_UPCOMING)
                .document(String.valueOf(movieId))
                .update(map);
        db.collection(CollectionName.MOVIE_NOW_PLAYING)
                .document(String.valueOf(movieId))
                .update(map);
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    private final OnSuccessListener<Void> onSuccessListener = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(application.getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        }
    };
    private final OnFailureListener onFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d(MovieDetailActivity.TAG, "addToFireStore: " + e.getMessage());
        }
    };
}

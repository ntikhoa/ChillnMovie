package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.auth.User;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RateMovieRepository {
    private final MutableLiveData<Boolean> success;
    private final Application application;
    private final FirebaseFirestore db;

    @Inject
    public RateMovieRepository(Application application, FirebaseFirestore db) {
        this.application = application;
        this.db = db;

        success = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> rateMovie(Long id, UserRate newUserRate) {
        db.runTransaction(new Transaction.Function<Void>() {

            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference movieRateRef = db.collection(CollectionName.MOVIE_RATE)
                        .document(String.valueOf(id));
                MovieRate movieRate = transaction.get(movieRateRef).toObject(MovieRate.class);

                DocumentReference userRateRef = movieRateRef.collection(CollectionName.USER_RATE)
                        .document(newUserRate.getUserId());
                UserRate userRate = transaction.get(userRateRef).toObject(UserRate.class);

                if (userRate != null) {
                    movieRate.updateVote(userRate, newUserRate);
                } else
                    movieRate.vote(newUserRate);

                addUserRate(id, newUserRate, transaction);
                updateMovieRate(id, movieRate, transaction);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success.postValue(false);
            }
        });
        return success;
    }

    private void updateMovieRate(Long movieId, MovieRate movieRate, Transaction transaction) {
        DocumentReference movieRateRef = db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieId));
        transaction.set(movieRateRef, movieRate);

        Map<String, Object> map = new HashMap<>();
        map.put("voteAverage", movieRate.getVoteAverage());
        map.put("voteCount", movieRate.getVoteCount());

        DocumentReference movieRef = db.collection(CollectionName.MOVIE)
                .document(String.valueOf(movieId));
        transaction.update(movieRef, map);

        DocumentReference movieDetailRef = db.collection(CollectionName.MOVIE_DETAIL)
                .document(String.valueOf(movieId));
        transaction.update(movieDetailRef, map);
    }

    private void addUserRate(Long movieId, UserRate userRate, Transaction transaction) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        userRate.setRateDate(currentStr);

        DocumentReference userRateRef = db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieId))
                .collection(CollectionName.USER_RATE)
                .document(userRate.getUserId());
        transaction.set(userRateRef, userRate);
    }

    public MutableLiveData<Boolean> reviewMovie(Long id, UserRate review) {
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        review.setRateDate(currentStr);
        db.collection(CollectionName.MOVIE_REVIEW)
                .document(String.valueOf(id))
                .set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        success.postValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success.postValue(false);
            }
        });
        return success;
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

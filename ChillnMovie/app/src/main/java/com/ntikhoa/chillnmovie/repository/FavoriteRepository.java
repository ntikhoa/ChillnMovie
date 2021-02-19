package com.ntikhoa.chillnmovie.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FavoriteRepository {
    private FirebaseFirestore db;
    private MutableLiveData<List<Long>> MLDmovieFavorite;

    @Inject
    public FavoriteRepository(FirebaseFirestore db) {
        this.db = db;

        MLDmovieFavorite = new MutableLiveData<>();
    }

    public MutableLiveData<List<Long>> getMLDmovieFavorite(String userId) {
        db.collection(CollectionName.USER_FAVORITE)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String resultStr = documentSnapshot.get("favorite_list").toString();
                        resultStr = resultStr.substring(1, resultStr.length() - 1);
                        if (!resultStr.isEmpty()) {
                            String[] moviesStr = resultStr.split(", ");
                            List<Long> movies = new ArrayList<>();
                            for (int i = 0; i < moviesStr.length; i++) {
                                movies.add(Long.parseLong(moviesStr[i]));
                            }
                            MLDmovieFavorite.postValue(movies);
                        }
                    }
                });
        return MLDmovieFavorite;
    }

    public void removeMovieFromFavorite(String uid, Long movieId) {
        db.collection(CollectionName.USER_FAVORITE)
                .document(uid)
                .update("favorite_list", FieldValue.arrayRemove(movieId));
    }
}

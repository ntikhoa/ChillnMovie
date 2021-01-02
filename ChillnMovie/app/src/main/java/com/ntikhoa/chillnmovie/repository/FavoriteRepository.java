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

public class FavoriteRepository {
    private FirebaseFirestore db;
    private Application application;
    private MutableLiveData<List<Integer>> MLDmovieFavorite;

    public FavoriteRepository(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
        MLDmovieFavorite = new MutableLiveData<>();
    }

    public MutableLiveData<List<Integer>> getMLDmovieFavorite(String userId) {
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
                            List<Integer> movies = new ArrayList<>();
                            for (int i = 0; i < moviesStr.length; i++) {
                                movies.add(Integer.parseInt(moviesStr[i]));
                            }
                            MLDmovieFavorite.postValue(movies);
                        }
                    }
                });
        return MLDmovieFavorite;
    }

    public void removeMovieFromFavorite(String uid, Integer movieId) {
        db.collection(CollectionName.USER_FAVORITE)
                .document(uid)
                .update("favorite_list", FieldValue.arrayRemove(movieId));
    }
}

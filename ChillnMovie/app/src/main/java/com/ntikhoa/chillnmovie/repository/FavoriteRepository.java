package com.ntikhoa.chillnmovie.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;

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
                        List<Integer> movies = (List<Integer>) documentSnapshot.get("favorite_list");
                        MLDmovieFavorite.postValue(movies);
                    }
                });
        return MLDmovieFavorite;
    }
}

package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.BuildConfig;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.api.RatingSourceAPI;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class RatingSourceRepository {

    private MutableLiveData<RatingSource> MLDratingSource;
    private MutableLiveData<MovieRate> MLDmovieRate;
    private Application application;
    private RatingSourceAPI ratingSourceApi;
    private FirebaseFirestore db;

    @Inject
    public RatingSourceRepository(Application application,
                                  RatingSourceAPI ratingSourceApi,
                                  FirebaseFirestore db) {
        this.application = application;
        MLDratingSource = new MutableLiveData<>();
        MLDmovieRate = new MutableLiveData<>();
        this.ratingSourceApi = ratingSourceApi;
        this.db = db;
    }

    public MutableLiveData<RatingSource> getMLDratingSource(String id) {
        ratingSourceApi.getRatingSource(BuildConfig.IMDB_ACCESS_KEY,
                        id)
                .enqueue(new Callback<RatingSource>() {
                    @Override
                    public void onResponse(Call<RatingSource> call, Response<RatingSource> response) {
                        if (response.isSuccessful()) {
                            MLDratingSource.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<RatingSource> call, Throwable t) {
                        Log.d("RatingSourceRepository", "onFailure: ");
                        showMessage(t.getMessage());

                    }
                });
        return MLDratingSource;
    }

    public MutableLiveData<MovieRate> getMLDmovieRate(Long id) {
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

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

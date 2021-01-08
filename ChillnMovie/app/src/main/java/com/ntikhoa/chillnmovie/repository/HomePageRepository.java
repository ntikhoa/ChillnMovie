package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageRepository {
    private Application application;
    private FirebaseFirestore firestore;

    private MutableLiveData<List<Movie>> MLDtopRatedMovie;
    private MutableLiveData<List<Movie>> MLDupcoming;
    private MutableLiveData<List<Movie>> MLDnowPlaying;
    private MutableLiveData<List<Movie>> MLDtrending;

    public HomePageRepository(Application application) {
        this.application = application;
        firestore = FirebaseFirestore.getInstance();

        MLDtopRatedMovie = new MutableLiveData<>();
        MLDupcoming = new MutableLiveData<>();
        MLDnowPlaying = new MutableLiveData<>();
        MLDtrending = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMLDtopRatedMovie() {
        firestore.collection(CollectionName.MOVIE)
                .orderBy("voteAverage", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDtopRatedMovie.postValue(queryDocumentSnapshots.toObjects(Movie.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });

        return MLDtopRatedMovie;
    }

    public MutableLiveData<List<Movie>> getMLDupcoming() {
        firestore.collection(CollectionName.MOVIE)
                .whereEqualTo("isUpcoming", true)
                .orderBy("updated_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDupcoming.postValue(queryDocumentSnapshots.toObjects(Movie.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDupcoming;
    }

    public MutableLiveData<List<Movie>> getMLDnowPlaying() {
        firestore.collection(CollectionName.MOVIE)
                .whereEqualTo("isNowPlaying", true)
                .orderBy("updated_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDnowPlaying.postValue(queryDocumentSnapshots.toObjects(Movie.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDnowPlaying;
    }

    public MutableLiveData<List<Movie>> getMLDtrending() {
        firestore.collection(CollectionName.MOVIE)
                .whereEqualTo("isTrending", true)
                .orderBy("updated_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDtrending.postValue(queryDocumentSnapshots.toObjects(Movie.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDtrending;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomePageRepository {
    private final Application application;
    private final FirebaseFirestore db;

    private final MutableLiveData<List<Movie>> MLDtopRatedMovie;
    private final MutableLiveData<List<Movie>> MLDupcoming;
    private final MutableLiveData<List<Movie>> MLDnowPlaying;
    private final MutableLiveData<List<Movie>> MLDtrending;
    private final MutableLiveData<List<Movie>> MLDvietnamese;

    @Inject
    public HomePageRepository(Application application, FirebaseFirestore db) {
        this.application = application;
        this.db = db;

        MLDtopRatedMovie = new MutableLiveData<>();
        MLDupcoming = new MutableLiveData<>();
        MLDnowPlaying = new MutableLiveData<>();
        MLDtrending = new MutableLiveData<>();
        MLDvietnamese = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMLDtopRatedMovie() {
        db.collection(CollectionName.MOVIE)
                .orderBy("voteAverage", Query.Direction.DESCENDING)
                .limit(20)
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
        db.collection(CollectionName.MOVIE)
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
        db.collection(CollectionName.MOVIE)
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
        db.collection(CollectionName.MOVIE)
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

    public MutableLiveData<List<Movie>> getMLDvietnamese() {
        db.collection(CollectionName.MOVIE)
                .whereEqualTo("isVietnamese", true)
                .orderBy("updated_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDvietnamese.postValue(queryDocumentSnapshots.toObjects(Movie.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
            }
        });
        return MLDvietnamese;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

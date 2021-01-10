package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.List;

public class MovieFirestoreDataSource extends PageKeyedDataSource<DocumentSnapshot, Movie> {

    private static final int LIMIT = 10;

    private Application application;
    private DocumentSnapshot lastResult;
    private FirebaseFirestore db;

    public MovieFirestoreDataSource(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<DocumentSnapshot> params, @NonNull LoadInitialCallback<DocumentSnapshot, Movie> callback) {
        db.collection(CollectionName.MOVIE)
                .orderBy("voteAverage", Query.Direction.DESCENDING)
                .limit(LIMIT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Movie> movies = queryDocumentSnapshots.toObjects(Movie.class);
                        callback.onResult(movies, null, queryDocumentSnapshots.getDocuments().get(movies.size() - 1));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<DocumentSnapshot> params, @NonNull LoadCallback<DocumentSnapshot, Movie> callback) {
        db.collection(CollectionName.MOVIE)
                .orderBy("voteAverage", Query.Direction.DESCENDING)
                .startAfter(params.key)
                .limit(LIMIT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Movie> movies = queryDocumentSnapshots.toObjects(Movie.class);
                            callback.onResult(movies, queryDocumentSnapshots.getDocuments().get(movies.size() - 1));
                        }
                    }
                });
    }
}

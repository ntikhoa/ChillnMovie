package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDBresponse;
import com.ntikhoa.chillnmovie.model.MovieSearchDataSourceFactory;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private Application application;
    private MutableLiveData<List<Movie>> MLDmovies;
    private FirebaseFirestore db;

    public SearchRepository(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
        MLDmovies = new MutableLiveData<>();
    }

    public LiveData<PagedList<Movie>> getMLDmoviesFromTMDB(String search) {
        MovieSearchDataSourceFactory factory = new MovieSearchDataSourceFactory(application, search);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

        Executor executor = Executors.newFixedThreadPool(5);

        LiveData<PagedList<Movie>> moviePagedListLiveData = (new LivePagedListBuilder<Integer, Movie>(factory, config))
                .setFetchExecutor(executor)
                .build();

        return moviePagedListLiveData;
    }

    public MutableLiveData<List<Movie>> getMLDmoviesFromFirestore(String search) {
        db.collection(CollectionName.MOVIE)
                .orderBy("title", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String denormalizeSearch = search.toLowerCase();
                        List<Movie> resultMovies = new ArrayList<>();
                        List<Movie> movies = queryDocumentSnapshots.toObjects(Movie.class);
                        for (int i = 0; i < movies.size(); i++) {
                            if (movies.get(i).getTitle().toLowerCase().contains(denormalizeSearch))
                                resultMovies.add(movies.get(i));
                        }
                        MLDmovies.postValue(resultMovies);
                    }
                });
        return MLDmovies;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

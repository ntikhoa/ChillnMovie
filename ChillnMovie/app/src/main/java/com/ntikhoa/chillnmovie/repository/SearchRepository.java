package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDBresponse;
import com.ntikhoa.chillnmovie.model.MovieSearchDataSourceFactory;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Client client;

    public SearchRepository(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
        MLDmovies = new MutableLiveData<>();
        client = new Client(application.getString(R.string.ALGOLIA_APP_ID),
                application.getString(R.string.ALGOLIA_ADMIN_KEY));
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
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(search)
                .setAttributesToRetrieve("title")
                .setPage(0)
                .setHitsPerPage(20);
        Index index = client.getIndex("movie");
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                try {
                    JSONArray hits = jsonObject.getJSONArray("hits");
                    List<String> moviesId = new ArrayList<>();
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hitsJSONObject = hits.getJSONObject(i);
                        String movieId = hitsJSONObject.getString("objectID");
                        moviesId.add(movieId);
                    }

                    index.getObjectsAsync(moviesId, new CompletionHandler() {
                        @Override
                        public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                            Gson gson = new Gson();
                            try {
                                List<Movie> movies = new ArrayList<>();
                                JSONArray movieJSONArray = jsonObject.getJSONArray("results");
                                for (int i = 0; i < movieJSONArray.length(); i++) {
                                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
                                    Movie movie = gson.fromJson(movieJSONObject.toString(), Movie.class);
                                    movies.add(movie);
                                }
                                MLDmovies.postValue(movies);
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

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

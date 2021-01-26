package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PageKeyedDataSource;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.gson.Gson;
import com.ntikhoa.chillnmovie.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDataSourceAlgolia extends PageKeyedDataSource<Integer, Movie> {

    private Client client;
    private String search;

    public MovieDataSourceAlgolia(Application application, String search) {
        client = new Client(application.getString(R.string.ALGOLIA_APP_ID),
                application.getString(R.string.ALGOLIA_ADMIN_KEY));
        this.search = search;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        Index index = client.getIndex("movie");
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(search)
                .setAttributesToRetrieve("title")
                .setPage(0)
                .setHitsPerPage(20);

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
                                callback.onResult(movies, null, 2);
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
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        Index index = client.getIndex("movie");
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(search)
                .setAttributesToRetrieve("title")
                .setPage(params.key)
                .setHitsPerPage(20);

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
                                callback.onResult(movies, params.key + 1);
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
    }
}

package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.Video;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovieRepository {
    private MutableLiveData<MovieDetail> MLDmovieDetail;
    private Application application;
    private String videoKey;

    private final MutableLiveData<Boolean> isTrendingExist;
    private final MutableLiveData<Boolean> isNowPlayingExist;
    private final MutableLiveData<Boolean> isUpcomingExist;


    private final RetrofitTMDbClient tmDbClient;
    private final FirebaseFirestore db;


    public EditMovieRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        isTrendingExist = new MutableLiveData<>();
        isNowPlayingExist = new MutableLiveData<>();
        isUpcomingExist = new MutableLiveData<>();

        db = FirebaseFirestore.getInstance();
        tmDbClient = RetrofitTMDbClient.getInstance();
    }

    private MutableLiveData<MovieDetail> getMovieDetailFromTMDb(Integer id) {
        getVideo(id);

        tmDbClient.getMovieAPI()
                .getMovieDetail(id,
                        application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese))
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                        if (response.isSuccessful()) {
                            MovieDetail movieDetail = response.body();
                            movieDetail.setTrailer_key(videoKey);
                            movieDetail.setVoteCount(1);
                            addToMovieRate(movieDetail);
                            MLDmovieDetail.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetail> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDmovieDetail;
    }

    private void addToMovieRate(MovieDetail movieDetail) {
        MovieRate movieRate = new MovieRate(movieDetail.getVoteAverage());
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieDetail.getId()))
                .set(movieRate);
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Integer id) {
        db.collection(CollectionName.MOVIE_DETAIL)
                .document(String.valueOf(id))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            MovieDetail movieDetail = documentSnapshot.toObject(MovieDetail.class);
                            MLDmovieDetail.postValue(movieDetail);
                        } else {
                            MLDmovieDetail = getMovieDetailFromTMDb(id);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
        return MLDmovieDetail;
    }

    private void getVideo(Integer id) {
        tmDbClient.getMovieAPI()
                .getVideo(id, application.getString(R.string.TMDb_API_key))
                .enqueue(new Callback<VideoDBResponse>() {
                    @Override
                    public void onResponse(Call<VideoDBResponse> call, Response<VideoDBResponse> response) {
                        if (response.isSuccessful()) {
                            List<Video> videos;
                            videos = response.body().videos;
                            for (int i = 0; i < videos.size(); i++) {
                                if (videos.get(i).getType().equals("Trailer")) {
                                    videoKey = videos.get(i).getKey();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoDBResponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
    }

    public void addToFirestore(MovieDetail movieDetail) {
        if (movieDetail != null) {
            int id = movieDetail.getId();
            db.collection(CollectionName.MOVIE_DETAIL)
                    .document(String.valueOf(id))
                    .set(movieDetail)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
            Movie movie = new Movie(movieDetail);
            db.collection(CollectionName.MOVIE)
                    .document(String.valueOf(id))
                    .set(movie)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        }
    }

    public void addToCategoryList(MovieDetail movieDetail, String collectionPath) {
        if (movieDetail != null) {
            int id = movieDetail.getId();
            Movie movie = new Movie(movieDetail);
            db.collection(collectionPath)
                    .document(String.valueOf(id))
                    .set(movie)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
            Date current = new Date();
            String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
            Map<String, Object> map = new HashMap<>();
            map.put("updated_date", currentStr);
            db.collection(collectionPath)
                    .document(String.valueOf(id))
                    .update(map)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        }
    }

    public void removeFromCategoryList(MovieDetail movieDetail, String collectionPath) {
        if (movieDetail != null) {
            int id = movieDetail.getId();
            Movie movie = new Movie(movieDetail);
            db.collection(collectionPath)
                    .document(String.valueOf(id))
                    .delete();
        }
    }


    public MutableLiveData<Boolean> isTrendingExist(Integer id) {
        db.collection(CollectionName.MOVIE_TRENDING)
                .document(String.valueOf(id))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            isTrendingExist.postValue(documentSnapshot.exists());
                        }
                    }
                });

        return isTrendingExist;
    }

    public MutableLiveData<Boolean> isNowPlayingExist(Integer id) {
        db.collection(CollectionName.MOVIE_UPCOMING)
                .document(String.valueOf(id))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            isNowPlayingExist.postValue(documentSnapshot.exists());
                        }
                    }
                });

        return isNowPlayingExist;
    }

    public MutableLiveData<Boolean> isUpcomingExist(Integer id) {
        db.collection(CollectionName.MOVIE_NOW_PLAYING)
                .document(String.valueOf(id))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            isUpcomingExist.postValue(documentSnapshot.exists());
                        }
                    }
                });

        return isUpcomingExist;
    }


    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    private final OnSuccessListener<Void> onSuccessListener = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(application.getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        }
    };
    private final OnFailureListener onFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d("EditMovieRepository: ", "addToFireStore: " + e.getMessage());
        }
    };
}

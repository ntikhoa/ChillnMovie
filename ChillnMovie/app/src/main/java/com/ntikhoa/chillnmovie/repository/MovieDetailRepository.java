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
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.CreditDBresponse;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.model.Video;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;
import com.ntikhoa.chillnmovie.service.RetrofitIMDbClient;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepository {
    private MutableLiveData<MovieDetail> MLDmovieDetail;
    private MutableLiveData<List<Caster>> MLDcaster;
    private MutableLiveData<RatingSource> MLDratingSource;
    private Application application;
    private String videoKey;
    private FirebaseFirestore db;

    public MovieDetailRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        MLDcaster = new MutableLiveData<>();
        MLDratingSource = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    private MutableLiveData<MovieDetail> getMovieDetailFromTMDb(Integer id) {
        getVideo(id);

        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getMovieDetail(id,
                        application.getString(R.string.TMDb_API_key),
                        application.getString(R.string.lang_vietnamese))
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                        if (response.isSuccessful()) {
                            MovieDetail movieDetail = response.body();
                            movieDetail.setTrailer_key(videoKey);
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
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getVideo(id, application.getString(R.string.TMDb_API_key))
                .enqueue(new Callback<VideoDBResponse>() {
                    @Override
                    public void onResponse(Call<VideoDBResponse> call, Response<VideoDBResponse> response) {
                        if (response.isSuccessful()) {
                            List<Video> videos;
                            videos = response.body().videos;
                            for (int i = 0; i<videos.size(); i++){
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

    public MutableLiveData<List<Caster>> getMLDcaster(Integer id) {
        RetrofitTMDbClient.getInstance()
                .getMovieAPI()
                .getCaster(id, application.getString(R.string.TMDb_API_key))
                .enqueue(new Callback<CreditDBresponse>() {
                    @Override
                    public void onResponse(Call<CreditDBresponse> call, Response<CreditDBresponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            MLDcaster.postValue(response.body().casters);
                        }
                    }

                    @Override
                    public void onFailure(Call<CreditDBresponse> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDcaster;
    }

    public MutableLiveData<RatingSource> getMLDratingSource(String id) {
        RetrofitIMDbClient.getInstance()
                .getAPI()
                .getRatingSource(application.getString(R.string.IMDb_API_key),
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
                        showMessage(t.getMessage());
                    }
                });
        return MLDratingSource;
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
            Log.d(MovieDetailActivity.TAG, "addToFireStore: " + e.getMessage());
        }
    };
}

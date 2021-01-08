package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.CreditDBresponse;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.model.Video;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepository {
    private MutableLiveData<MovieDetail> MLDmovieDetail;
    private final MutableLiveData<List<Caster>> MLDcaster;
    private final MutableLiveData<List<UserRate>> MLDuserRate;

    private final Application application;
    private String videoKey;

    private final RetrofitTMDbClient tmDbClient;
    private final FirebaseFirestore db;

    public MovieDetailRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        MLDcaster = new MutableLiveData<>();
        MLDuserRate = new MutableLiveData<>();

        db = FirebaseFirestore.getInstance();
        tmDbClient = RetrofitTMDbClient.getInstance();
    }

    //repeat with EditMovieRepository
    private MutableLiveData<MovieDetail> getMovieDetailFromTMDb(Long id) {
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
                            movieDetail.setBackdropPath(Movie.path + movieDetail.getBackdropPath());
                            movieDetail.setPosterPath(Movie.path + movieDetail.getPosterPath());
                            MLDmovieDetail.postValue(movieDetail);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetail> call, Throwable t) {
                        showMessage(t.getMessage());
                    }
                });
        return MLDmovieDetail;
    }

    //repeat with EditMovieRepository
    public MutableLiveData<MovieDetail> getMLDmovieDetail(Long id) {
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

    //repeat with EditMovieRepository
    private void getVideo(Long id) {
        String strId = String.valueOf(id);
        Integer intId = Integer.parseInt(strId);

        tmDbClient.getMovieAPI()
                .getVideo(intId, application.getString(R.string.TMDb_API_key))
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

    public MutableLiveData<List<Caster>> getMLDcaster(Long id) {
        tmDbClient.getMovieAPI()
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

    //repeat with EditMovieRepository
    public void addToFirestore(MovieDetail movieDetail) {
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                long id = movieDetail.getId();
                DocumentReference movieRateRef = db.collection(CollectionName.MOVIE_RATE)
                        .document(String.valueOf(id));
                if (!transaction.get(movieRateRef)
                        .exists())
                    initMovieRate(movieDetail, transaction);

                addMovie(movieDetail, transaction);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Success");
            }
        }).addOnFailureListener(onFailureListener);
    }

    private void initMovieRate(MovieDetail movieDetail, Transaction transaction) throws FirebaseFirestoreException {
        MovieRate movieRate = new MovieRate(movieDetail.getVoteAverage());
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieDetail.getId()))
                .set(movieRate);
    }

    private void addMovie(MovieDetail movieDetail, Transaction transaction) {
        long id = movieDetail.getId();

        DocumentReference movieDetailRef = db.collection(CollectionName.MOVIE_DETAIL)
                .document(String.valueOf(id));
        transaction.set(movieDetailRef, movieDetail);

        Movie movie = new Movie(movieDetail);
        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        movie.setUpdated_date(currentStr);

        DocumentReference movieRef = db.collection(CollectionName.MOVIE)
                .document(String.valueOf(id));
        transaction.set(movieRef, movie);
    }

    public void addToFavorite(String userId, Long movieId) {
        db.collection(CollectionName.USER_FAVORITE)
                .document(userId)
                .update("favorite_list", FieldValue.arrayUnion(movieId))
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    private final OnSuccessListener<Void> onSuccessListener = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void o) {
            Toast.makeText(application.getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        }
    };
    private final OnFailureListener onFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d(MovieDetailActivity.TAG, "addToFireStore: " + e.getMessage());
        }
    };

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

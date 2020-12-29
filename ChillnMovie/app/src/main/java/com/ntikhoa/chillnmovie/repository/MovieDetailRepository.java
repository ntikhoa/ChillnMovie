package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.provider.Telephony;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.CreditDBresponse;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.RateJoinUser;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserRate;
import com.ntikhoa.chillnmovie.model.Video;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;
import com.ntikhoa.chillnmovie.service.RetrofitIMDbClient;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;
import com.ntikhoa.chillnmovie.view.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepository {
    private MutableLiveData<MovieDetail> MLDmovieDetail;
    private MutableLiveData<List<Caster>> MLDcaster;
    private MutableLiveData<List<RateJoinUser>> MLDuserRate;
    private Application application;
    private String videoKey;

    private RetrofitTMDbClient tmDbClient;
    private FirebaseFirestore db;

    public MovieDetailRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        MLDcaster = new MutableLiveData<>();
        MLDuserRate = new MutableLiveData<>();

        db = FirebaseFirestore.getInstance();
        tmDbClient = RetrofitTMDbClient.getInstance();
    }

    //repeat with EditMovieRepository
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

    //repeat with EditMovieRepository
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

    //repeat with EditMovieRepository
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

    public MutableLiveData<List<Caster>> getMLDcaster(Integer id) {
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

    public MutableLiveData<List<RateJoinUser>> getMLDuserRate(Integer id) {
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(id))
                .collection(CollectionName.USER_RATE)
                .orderBy("updated_date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<RateJoinUser> rateJoinUsers = new ArrayList<>();
                        List<UserRate> userRates = queryDocumentSnapshots.toObjects(UserRate.class);
                        for (int i = 0; i < userRates.size(); i++) {
                            int index = i;
                            db.collection(CollectionName.USER_PROFILE)
                                    .document(userRates.get(i).getUserId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            UserAccount userAccount = documentSnapshot.toObject(UserAccount.class);
                                            rateJoinUsers.add(new RateJoinUser(userAccount, userRates.get(index)));
                                        }
                                    });
                        }
                        MLDuserRate.postValue(rateJoinUsers);
                    }
                });
        return MLDuserRate;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    private void addToMovieRate(MovieDetail movieDetail) {
        MovieRate movieRate = new MovieRate(movieDetail.getVoteAverage());
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieDetail.getId()))
                .set(movieRate);
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

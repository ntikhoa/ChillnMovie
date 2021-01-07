package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.Movie;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.model.MovieRate;
import com.ntikhoa.chillnmovie.model.Video;
import com.ntikhoa.chillnmovie.model.VideoDBResponse;
import com.ntikhoa.chillnmovie.service.RetrofitTMDbClient;
import com.ntikhoa.chillnmovie.view.PreviewFragment;

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
    private final Application application;
    private String videoKey;

    private final MutableLiveData<Boolean> isTrendingExist;
    private final MutableLiveData<Boolean> isNowPlayingExist;
    private final MutableLiveData<Boolean> isUpcomingExist;

    private final MutableLiveData<Boolean> isSuccess;

    private final MutableLiveData<String> posterUrl;
    private final MutableLiveData<String> backdropUrl;

    private final FirebaseStorage storage;
    private final RetrofitTMDbClient tmDbClient;
    private final FirebaseFirestore db;


    public EditMovieRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        isTrendingExist = new MutableLiveData<>();
        isNowPlayingExist = new MutableLiveData<>();
        isUpcomingExist = new MutableLiveData<>();

        isSuccess = new MutableLiveData<>();

        posterUrl = new MutableLiveData<>();
        backdropUrl = new MutableLiveData<>();

        db = FirebaseFirestore.getInstance();
        tmDbClient = RetrofitTMDbClient.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    private MutableLiveData<MovieDetail> getMovieDetailFromTMDb(Long id) {
        getVideo(id);

        String strId = String.valueOf(id);
        Integer intId = Integer.parseInt(strId);
        tmDbClient.getMovieAPI()
                .getMovieDetail(intId,
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

    public MutableLiveData<Boolean> updateToDatabase(
            MovieDetail movieDetail,
            boolean trending,
            boolean upcoming,
            boolean nowPlaying) {

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

                updateMovie(movieDetail, transaction);
                updateCategory(movieDetail, trending, upcoming, nowPlaying, transaction);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isSuccess.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSuccess.postValue(false);
            }
        });
        return isSuccess;
    }

    private void initMovieRate(MovieDetail movieDetail, Transaction transaction) throws FirebaseFirestoreException {
        MovieRate movieRate = new MovieRate(movieDetail.getVoteAverage());
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(movieDetail.getId()))
                .set(movieRate);
    }

    private void updateMovie(MovieDetail movieDetail, Transaction transaction) {
        long id = movieDetail.getId();

        DocumentReference movieDetailRef = db.collection(CollectionName.MOVIE_DETAIL)
                .document(String.valueOf(id));
        transaction.set(movieDetailRef, movieDetail);

        Movie movie = new Movie(movieDetail);
        DocumentReference movieRef = db.collection(CollectionName.MOVIE)
                .document(String.valueOf(id));
        transaction.set(movieRef, movie);
    }

    private void updateCategory(
            MovieDetail movieDetail,
            boolean trending,
            boolean upcoming,
            boolean nowPlaying,
            Transaction transaction) {

        if (trending) {
            addToCategoryList(movieDetail, CollectionName.MOVIE_TRENDING, transaction);
        } else removeFromCategoryList(movieDetail, CollectionName.MOVIE_TRENDING, transaction);

        if (upcoming) {
            addToCategoryList(movieDetail, CollectionName.MOVIE_UPCOMING, transaction);
        } else removeFromCategoryList(movieDetail, CollectionName.MOVIE_UPCOMING, transaction);

        if (nowPlaying) {
            addToCategoryList(movieDetail, CollectionName.MOVIE_NOW_PLAYING, transaction);
        } else removeFromCategoryList(movieDetail, CollectionName.MOVIE_NOW_PLAYING, transaction);
    }

    private void addToCategoryList(MovieDetail movieDetail, String collectionPath, Transaction transaction) {
        long id = movieDetail.getId();
        Movie movie = new Movie(movieDetail);

        DocumentReference collectionRef = db.collection(collectionPath)
                .document(String.valueOf(id));
        transaction.set(collectionRef, movie);

        Date current = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(current);
        Map<String, Object> map = new HashMap<>();
        map.put("updated_date", currentStr);
        transaction.update(collectionRef, map);
    }

    private void removeFromCategoryList(MovieDetail movieDetail, String collectionPath, Transaction transaction) {
        long id = movieDetail.getId();
        DocumentReference collectionRef = db.collection(collectionPath)
                .document(String.valueOf(id));
        transaction.delete(collectionRef);
    }


    public MutableLiveData<Boolean> isTrendingExist(Long id) {
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

    public MutableLiveData<Boolean> isNowPlayingExist(Long id) {
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

    public MutableLiveData<Boolean> isUpcomingExist(Long id) {
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

    public MutableLiveData<String> uploadImage(Uri imageUri, int mode) {
        StorageReference imageRef = storage.getReference().child(CollectionName.IMAGE + System.currentTimeMillis()
                + "." + getFileExtension(imageUri));

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (mode == PreviewFragment.POSTER)
                                    posterUrl.postValue(uri.toString());
                                else if (mode == PreviewFragment.BACKDROP)
                                    backdropUrl.postValue(uri.toString());
                            }
                        });
                    }
                });
        if (mode == PreviewFragment.POSTER)
            return posterUrl;
        else if (mode == PreviewFragment.BACKDROP)
            return backdropUrl;
        return null;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = application.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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

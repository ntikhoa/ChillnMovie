package com.ntikhoa.chillnmovie.di;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.api.MovieAPI;
import com.ntikhoa.chillnmovie.api.RatingSourceAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private static String BASE_URL_TMDB = "https://api.themoviedb.org/3/";
    private final String BASE_URL_IMDB = "https://imdb-api.com/";

    @RetrofitTMDB
    @Provides
    @Singleton
    public Retrofit provideRetrofitTMDB() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_TMDB)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public MovieAPI provideMovieApi(@RetrofitTMDB Retrofit retrofitTMDB) {
        return retrofitTMDB.create(MovieAPI.class);
    }

    @Provides
    @Singleton
    public RatingSourceAPI provideRatingSourceApi (@RetrofitIMDb Retrofit retrofitIMDB) {
        return retrofitIMDB.create(RatingSourceAPI.class);
    }

    @RetrofitIMDb
    @Provides
    @Singleton
    public Retrofit provideRetrofitIMDB() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_IMDB)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseStorage provideFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @ActivityRetainedScoped
    public ShimmerDrawable provideShimmerDrawable(Context context) {
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, R.color.colorShimmerBase))
                .setBaseAlpha(1)
                .setHighlightColor(ContextCompat.getColor(context, R.color.colorShimmerHighlight))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .setDuration(500)
                .build();
        ShimmerDrawable drawable = new ShimmerDrawable();
        drawable.setShimmer(shimmer);
        return drawable;
    }
}

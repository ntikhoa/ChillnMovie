package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.RatingSource;
import com.ntikhoa.chillnmovie.service.RetrofitIMDbClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingSourceRepository {

    private MutableLiveData<RatingSource> MLDratingSource;
    private Application application;
    private RetrofitIMDbClient imDbClient;

    public RatingSourceRepository(Application application) {
        this.application = application;
        MLDratingSource = new MutableLiveData<>();
        imDbClient = RetrofitIMDbClient.getInstance();
    }

    public MutableLiveData<RatingSource> getMLDratingSource(String id) {
        imDbClient.getAPI()
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
                        Log.d("RatingSourceRepository", "onFailure: ");
                        showMessage(t.getMessage());

                    }
                });
        return MLDratingSource;
    }

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

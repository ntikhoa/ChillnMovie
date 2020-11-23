package com.ntikhoa.chillnmovie.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.Caster;
import com.ntikhoa.chillnmovie.model.CreditDBresponse;
import com.ntikhoa.chillnmovie.model.MovieDetail;
import com.ntikhoa.chillnmovie.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepository {
    MutableLiveData<MovieDetail> MLDmovieDetail;
    MutableLiveData<List<Caster>> MLDcaster;
    Application application;

    public MovieDetailRepository(Application application) {
        this.application = application;
        MLDmovieDetail = new MutableLiveData<>();
        MLDcaster = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDetail> getMLDmovieDetail(Integer id) {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getMovieDetail(id,
                        application.getString(R.string.API_key),
                        application.getString(R.string.lang_vietnamses))
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                        if (response.isSuccessful()) {
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

    public MutableLiveData<List<Caster>> getMLDcaster(Integer id) {
        RetrofitClient.getInstance()
                .getMovieAPI()
                .getCaster(id, application.getString(R.string.API_key))
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

    private void showMessage(String message) {
        Toast.makeText(application.getApplicationContext(),
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}

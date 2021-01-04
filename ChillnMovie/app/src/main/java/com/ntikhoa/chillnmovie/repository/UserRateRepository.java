package com.ntikhoa.chillnmovie.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ntikhoa.chillnmovie.model.CollectionName;
import com.ntikhoa.chillnmovie.model.UserRate;

import java.util.List;

public class UserRateRepository {
    private FirebaseFirestore db;
    private Application application;
    private MutableLiveData<List<UserRate>> MLDuserRate;
    private MutableLiveData<UserRate> MLDreview;

    public UserRateRepository(Application application) {
        this.application = application;
        db = FirebaseFirestore.getInstance();
        MLDuserRate = new MutableLiveData<>();
        MLDreview = new MutableLiveData<>();
    }

    public MutableLiveData<List<UserRate>> getMLDuserRate(Integer id) {
        db.collection(CollectionName.MOVIE_RATE)
                .document(String.valueOf(id))
                .collection(CollectionName.USER_RATE)
                .orderBy("rateDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MLDuserRate.postValue(queryDocumentSnapshots.toObjects(UserRate.class));
                    }
                });
        return MLDuserRate;
    }

    public MutableLiveData<UserRate> getMLDreview(Integer id) {
        db.collection(CollectionName.MOVIE_REVIEW)
                .document(String.valueOf(id))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        MLDreview.postValue(documentSnapshot.toObject(UserRate.class));
                    }
                });
        return MLDreview;
    }
}

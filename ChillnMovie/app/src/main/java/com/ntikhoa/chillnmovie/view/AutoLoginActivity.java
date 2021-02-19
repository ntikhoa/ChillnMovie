package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AutoLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private UserAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(UserAccountViewModel.class);

        if (mAuth.getCurrentUser() != null) {
            viewModel.setUserMode(mAuth.getUid())
                    .observe(this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean success) {
                            if (success) {
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }
}
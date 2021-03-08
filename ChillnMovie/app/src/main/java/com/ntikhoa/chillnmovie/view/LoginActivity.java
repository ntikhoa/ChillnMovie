package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ntikhoa.chillnmovie.databinding.ActivityLoginBinding;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private UserAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this)
                .get(UserAccountViewModel.class);

        binding.btnGuest.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            email = email.trim();
            password = password.trim();
            if (email.isEmpty()) {
                binding.editTextEmail.setError("This field cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                binding.editTextPassword.setError("This field cannot be empty");
                return;
            }

            viewModel.login(email, password).observe(LoginActivity.this, success -> {
                if (success) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    binding.textViewError.setVisibility(View.VISIBLE);
                }
            });
        });

        binding.textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }
}
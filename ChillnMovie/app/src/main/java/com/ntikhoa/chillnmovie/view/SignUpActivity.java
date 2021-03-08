package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ntikhoa.chillnmovie.databinding.ActivitySignUpBinding;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private UserAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this)
                .get(UserAccountViewModel.class);

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString();
            String confirmPassword = binding.editTextConfirmePassword.getText().toString();
            if (email.isEmpty()) {
                binding.editTextEmail.setError("This field cannot be empty");
                return;
            }
            if (!validateEmail(email)) {
                binding.editTextEmail.setError("Email is not validate");
                return;
            }
            if (password.isEmpty()) {
                binding.editTextPassword.setError("This field cannot be empty");
                return;
            }
            if (!password.equals(confirmPassword)) {
                binding.textViewError.setText("The confirm password does not match");
                binding.textViewError.setVisibility(View.VISIBLE);
                return;
            }

            viewModel.signUp(email, password)
                    .observe(SignUpActivity.this, isSuccess -> {
                        if (isSuccess) {
                            Intent intent = new Intent(getApplicationContext(), CreateUserProfileActivity.class);
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser signupUser = auth.getCurrentUser();
                            intent.putExtra(CreateUserProfileActivity.UID, signupUser.getUid());
                            intent.putExtra(CreateUserProfileActivity.EMAIL, email);
                            auth.signOut();
                            startActivity(intent);

                            finish();
                        } else {
                            binding.textViewError.setText("Email is already exist!");
                            binding.textViewError.setVisibility(View.VISIBLE);
                        }
                    });
        });
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
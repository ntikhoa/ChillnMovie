package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    private MaterialButton btnRegister;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private TextView textViewError;

    private UserAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initComponent();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                if (email.isEmpty()) {
                    editTextEmail.setError("This field cannot be empty");
                    return;
                }
                if (!validateEmail(email)) {
                    editTextEmail.setError("Email is not validate");
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("This field cannot be empty");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    textViewError.setText("The confirm password does not match");
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                viewModel.signUp(email, password)
                        .observe(SignUpActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean isSuccess) {
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
                                    textViewError.setText("Email is already exist!");
                                    textViewError.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this)
                .get(UserAccountViewModel.class);

        btnRegister = findViewById(R.id.btnRegister);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmePassword);

        textViewError = findViewById(R.id.textViewError);
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
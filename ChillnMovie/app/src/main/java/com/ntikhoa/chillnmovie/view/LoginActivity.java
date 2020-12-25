package com.ntikhoa.chillnmovie.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnGuest;
    private MaterialButton btnLogin;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private TextView textViewError;
    private TextView textViewRegister;

    private FirebaseAuth mAuth;

    private UserAccountViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                email = email.trim();
                password = password.trim();
                if (email.isEmpty()) {
                    editTextEmail.setError("This field cannot be empty");
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("This field cannot be empty");
                    return;
                }

                viewModel.login(email, password);
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(getApplicationContext(), mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(UserAccountViewModel.class);

        btnGuest = findViewById(R.id.btnGuest);
        btnLogin = findViewById(R.id.btnLogin);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        textViewError = findViewById(R.id.textViewError);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAuth.signOut();
    }
}
package com.ntikhoa.chillnmovie.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

public class CreateUserProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 1;

    private FirebaseAuth auth;

    private ImageView imageViewAvatar;
    private EditText editTextUserName;
    private EditText editTextCountry;
    private EditText editTextBirthdate;

    private MaterialButton btnSubmit, btnSkip;

    private Uri imageUri;

    private UserAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);

        initComponent();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    viewModel.uploadAvatar(imageUri, auth.getUid());

                }
                finish();
            }
        });

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                imageViewAvatar.setImageURI(imageUri);
            }
        }
    }

    private void initComponent() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(UserAccountViewModel.class);

        auth = FirebaseAuth.getInstance();

        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextBirthdate = findViewById(R.id.editTextBirthdate);

        btnSkip = findViewById(R.id.btnSkip);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.signOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
    }
}
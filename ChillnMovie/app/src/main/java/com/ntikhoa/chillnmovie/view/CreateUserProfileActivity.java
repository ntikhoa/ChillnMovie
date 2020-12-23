package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.ntikhoa.chillnmovie.R;

public class CreateUserProfileActivity extends AppCompatActivity {

    ImageView imageViewAvatar;
    EditText editTextUserName;
    EditText editTextCountry;
    EditText editTextBirthdate;

    MaterialButton btnSubmit, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);

        initComponent();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initComponent() {
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextBirthdate = findViewById(R.id.editTextBirthdate);

        btnSkip = findViewById(R.id.btnSkip);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
}
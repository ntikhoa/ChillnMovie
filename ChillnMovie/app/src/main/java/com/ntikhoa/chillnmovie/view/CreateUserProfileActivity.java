package com.ntikhoa.chillnmovie.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.viewmodel.UserAccountViewModel;

public class CreateUserProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 1;

    private FirebaseAuth auth;

    private ImageView imageViewAvatar;
    private EditText editTextUserName;
    private EditText editTextCountry;
    private EditText editTextBirthdate;
    private EditText editTextEmail;
    private RadioGroup radioGroupGender;

    private MaterialButton btnSubmit, btnSkip;

    int gender = -1;

    private Uri imageUri;

    private UserAccountViewModel viewModel;

    private String uid;

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
                    viewModel.uploadAvatar(imageUri, uid);
                }
                createUserProfile();
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

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMale:
                        gender = UserAccount.MALE;
                        break;
                    case R.id.radioFemale:
                        gender = UserAccount.FEMALE;
                        break;
                    case R.id.radioOther:
                        gender = UserAccount.OTHER;
                        break;
                }
            }
        });
    }

    private void createUserProfile() {
        String name = editTextUserName.getText().toString();
        if (name.trim().length() > 16) {
            editTextUserName.setError("Max length 16");
            return;
        }
        String country = editTextCountry.getText().toString();

        String birthdate = editTextBirthdate.getText().toString();
        if (!validate(birthdate.trim())) {
            editTextBirthdate.setError("Invalid input");
            return;
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setName(name);
        userAccount.setCountry(country);
        userAccount.setBirthdate(birthdate);
        userAccount.setGender(gender);

        viewModel.createUserProfile(userAccount, uid);
    }

    private boolean validate(String date) {
        String[] t = date.split("-");
        if (t.length != 3)
            return false;
        int year = Integer.parseInt(t[0]);
        int month = Integer.parseInt(t[1]);
        int day = Integer.parseInt(t[2]);
        if (month < 10 && !t[1].contains("0"))
            return false;
        if (day < 10 && !t[2].contains("0"))
            return false;
        if (year < 1900)
            return false;
        if (month < 1 || month > 12)
            return false;
        if (day < 1 || day > 31)
            return false;

        return true;
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

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEmail.setText(auth.getCurrentUser().getEmail());

        btnSkip = findViewById(R.id.btnSkip);
        btnSubmit = findViewById(R.id.btnSubmit);

        radioGroupGender = findViewById(R.id.radioGroupGender);

        uid = auth.getCurrentUser().getUid();
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
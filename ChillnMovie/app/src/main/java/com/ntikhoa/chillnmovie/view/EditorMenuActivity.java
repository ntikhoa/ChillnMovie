package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.ActivityEditorMenuBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditorMenuActivity extends AppCompatActivity {

    private ActivityEditorMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        addDefaultFragment();

        binding.fabAddNewMovie.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditMovieActivity.class);
            startActivity(intent);
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener
            = item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new EditorFrontFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = SearchFragment.newInstance(SearchFragment.MODE_TMDB);
                        break;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer, selectedFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                return true;
            };

    private void addDefaultFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, new EditorFrontFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
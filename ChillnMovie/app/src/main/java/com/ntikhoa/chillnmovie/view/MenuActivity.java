package com.ntikhoa.chillnmovie.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.ActivityMenuBinding;
import com.ntikhoa.chillnmovie.model.UserAccount;
import com.ntikhoa.chillnmovie.model.UserModeSingleton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mode = ((UserModeSingleton) getApplicationContext()).getMode();
        //for testing purposes
        Toast.makeText(this, String.valueOf(mode), Toast.LENGTH_LONG).show();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        if (mode == UserAccount.EDITOR) {
            binding.fabEditor.setVisibility(View.VISIBLE);
            binding.fabEditor.setClickable(true);
            binding.fabEditor.setFocusable(true);
        }
        binding.fabEditor.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditorMenuActivity.class);
            startActivity(intent);
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = SearchFragment.newInstance(SearchFragment.MODE_FIRESTORE);
                        break;
                    case R.id.nav_list:
                        selectedFragment = new FavoriteFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer, selectedFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                return true;
            };
}
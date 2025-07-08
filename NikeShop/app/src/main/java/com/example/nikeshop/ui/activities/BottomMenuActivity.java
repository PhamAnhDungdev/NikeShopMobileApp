package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nikeshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BottomMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomMenu(int selectedItemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) return;
        bottomNavigationView.setSelectedItemId(selectedItemId);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                if (selectedItemId != R.id.nav_home) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_search) {
                if (selectedItemId != R.id.nav_search) {
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_favourites) {
                if (selectedItemId != R.id.nav_favourites) {
                    Intent intent = new Intent(this, FavouriteActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_profile) {
                if (selectedItemId != R.id.nav_profile) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            return false;
        });
    }
}

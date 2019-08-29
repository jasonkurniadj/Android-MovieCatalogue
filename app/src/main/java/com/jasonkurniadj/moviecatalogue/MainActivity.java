package com.jasonkurniadj.moviecatalogue;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jasonkurniadj.moviecatalogue.fragment.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.tab_home:
                    fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_page, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.tab_movie:
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_page, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.tab_tv_show:
                    fragment = new TvShowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_page, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.tab_favorite:
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_page, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.tab_home);
        }
    }
}

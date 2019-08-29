package com.jasonkurniadj.moviecataloguefavorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.jasonkurniadj.moviecataloguefavorite.adapter.ViewPagerAdapter;
import com.jasonkurniadj.moviecataloguefavorite.fragment.FavMovieFragment;
import com.jasonkurniadj.moviecataloguefavorite.fragment.FavTvShowFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tl_menu);
        ViewPager viewPager = findViewById(R.id.vp_page);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FavMovieFragment(), getResources().getString(R.string.tab_movie));
        adapter.addFragment(new FavTvShowFragment(), getResources().getString(R.string.tab_tv_show));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

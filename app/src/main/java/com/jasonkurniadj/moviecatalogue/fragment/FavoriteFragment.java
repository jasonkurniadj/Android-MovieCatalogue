package com.jasonkurniadj.moviecatalogue.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.adapter.FavoriteViewPagerAdapter;
import com.jasonkurniadj.moviecatalogue.fragment.subFragment.FavMovieSubFragment;
import com.jasonkurniadj.moviecatalogue.fragment.subFragment.FavTvShowSubFragment;

public class FavoriteFragment extends Fragment {

    private static final String TAG_LOG = "JS_LOG";

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tl_favorite);
        ViewPager viewPager = view.findViewById(R.id.vp_sub_page);
        FavoriteViewPagerAdapter adapter = new FavoriteViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FavMovieSubFragment(), getResources().getString(R.string.tab_movie));
        adapter.addFragment(new FavTvShowSubFragment(), getResources().getString(R.string.tab_tv_show));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if (savedInstanceState == null) {
            Log.i(TAG_LOG, "Bundle on Favorite Fragment is null.");
        }
        else {
            Log.i(TAG_LOG, "Bundle on Favorite Fragment is not null.");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

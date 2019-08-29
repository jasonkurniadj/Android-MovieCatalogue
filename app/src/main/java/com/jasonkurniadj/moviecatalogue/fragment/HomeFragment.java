package com.jasonkurniadj.moviecatalogue.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.adapter.HomePageAdapter;
import com.jasonkurniadj.moviecatalogue.asyncTask.GetHomeListTask;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;
import com.jasonkurniadj.moviecatalogue.notification.*;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String LOG_TAG = "JS_LOG";
    private static final String LIST_TAG_1 = "LIST_STATE_TAG_1";
    private static final String LIST_TAG_2 = "LIST_STATE_TAG_2";
    private static ArrayList<ItemModel> nowPlayingInstance = new ArrayList<>();
    private static ArrayList<ItemModel> releaseInstance = new ArrayList<>();

    private DailyNotificationReceiver dailyNotificationReceiver = new DailyNotificationReceiver();
    private ReleaseNotificationReceiver releaseNotificationReceiver = new ReleaseNotificationReceiver();

    public void setNowPlayingInstance (ArrayList<ItemModel> nowPlayingInstance) {
        HomeFragment.nowPlayingInstance = nowPlayingInstance;
    }

    public void setReleaseInstance (ArrayList<ItemModel> releaseInstance) {
        HomeFragment.releaseInstance = releaseInstance;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.tb_home);
        toolbar.inflateMenu(R.menu.top_nav_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.language_setting) {
                    Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.reminder_setting) {
                    initNotificationDialog(view);
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvNowPlaying, rvUpcoming;
        rvNowPlaying = view.findViewById(R.id.rv_now_playing);
        rvUpcoming = view.findViewById(R.id.rv_release);

        if (savedInstanceState != null) {
            Log.i(LOG_TAG, "Bundle on Home Fragment is not null.");

            nowPlayingInstance = savedInstanceState.getParcelableArrayList(LIST_TAG_1);
            releaseInstance = savedInstanceState.getParcelableArrayList(LIST_TAG_2);

            HomePageAdapter adapter = new HomePageAdapter(nowPlayingInstance);
            adapter.notifyDataSetChanged();
            rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvNowPlaying.setHasFixedSize(true);
            rvNowPlaying.setAdapter(adapter);

            HomePageAdapter adapter2 = new HomePageAdapter(releaseInstance);
            adapter.notifyDataSetChanged();
            rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvUpcoming.setHasFixedSize(true);
            rvUpcoming.setAdapter(adapter2);
        }
        else {
            Log.i(LOG_TAG, "Bundle on Home Fragment is null.");

            int fragmentIdx = 0;
            GetHomeListTask getHomeListTask = new GetHomeListTask(getContext(), fragmentIdx);
            getHomeListTask.execute(fragmentIdx);
        }

        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_TAG_1, nowPlayingInstance);
        outState.putParcelableArrayList(LIST_TAG_2, releaseInstance);
        Log.i(LOG_TAG, "SaveInstance on Home Fragment. NowPlayingInstance: " + nowPlayingInstance);
        Log.i(LOG_TAG, "SaveInstance on Home Fragment. ReleaseInstance: " + releaseInstance);
    }


    private void initNotificationDialog(final View view) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_notification_settings);

        TextView tvClose = dialog.findViewById(R.id.tv_notification_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Switch swDailyNotification, swReleaseNotification;
        swDailyNotification = dialog.findViewById(R.id.sw_daily_notification);
        swReleaseNotification = dialog.findViewById(R.id.sw_release_notification);

        boolean isSetDailyNotification = dailyNotificationReceiver.isSetNotification(getContext());
        boolean isSetReleaseNotification = releaseNotificationReceiver.isSetNotification(getContext());

        if (isSetDailyNotification) {
            swDailyNotification.setChecked(true);
        }
        if (isSetReleaseNotification) {
            swReleaseNotification.setChecked(true);
        }

        swDailyNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(LOG_TAG, "Daily notification status: " + b);

                if (b) {
                    dailyNotificationReceiver.setNotification(getContext());
                    showSnackbarMessage(view, getResources().getString(R.string.daily_notification_on));
                }
                else {
                    dailyNotificationReceiver.cancelNotification(getContext());
                    showSnackbarMessage(view, getResources().getString(R.string.daily_notification_off));
                }
            }
        });
        swReleaseNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(LOG_TAG, "Release notification status: " + b);

                if (b) {
                    releaseNotificationReceiver.setNotification(getContext(), releaseInstance);
                    showSnackbarMessage(view, getResources().getString(R.string.release_notification_on));
                }
                else {
                    releaseNotificationReceiver.cancelNotification(getContext());
                    showSnackbarMessage(view, getResources().getString(R.string.release_notification_off));
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showSnackbarMessage (View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}

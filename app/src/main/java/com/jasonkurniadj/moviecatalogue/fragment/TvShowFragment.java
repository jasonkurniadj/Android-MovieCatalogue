package com.jasonkurniadj.moviecatalogue.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.adapter.PageAdapter;
import com.jasonkurniadj.moviecatalogue.asyncTask.GetListTask;
import com.jasonkurniadj.moviecatalogue.asyncTask.GetSearchListTask;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class TvShowFragment extends Fragment {

    private static final String LOG_TAG = "JS_LOG";
    private static final String LIST_TAG = "LIST_STATE_TAG";
    private static final String LIST_SEARCH_TAG = "LIST_STATE_SEARCH_TAG";
    private RecyclerView recyclerView;
    private static ArrayList<ItemModel> tvShowInstance = new ArrayList<>();
    private static ArrayList<ItemModel> tvShowSearchInstance = new ArrayList<>();

    public TvShowFragment() {
    }

    public void setTvShowInstance(ArrayList<ItemModel> tvShowInstance) {
        TvShowFragment.tvShowInstance = tvShowInstance;
    }
    public void setTvShowSearchInstance(ArrayList<ItemModel> tvShowSearchInstance) {
        TvShowFragment.tvShowSearchInstance = tvShowSearchInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        final TextView tvHeader = view.findViewById(R.id.tv_header_tv_show);
        SearchView searchView = view.findViewById(R.id.sv_tv_show);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHeader.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvHeader.setVisibility(View.VISIBLE);

                if (tvShowSearchInstance != null) {
                    setDefaultRecyclerView();
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_tv_show);
        if (savedInstanceState != null) {
            Log.i(LOG_TAG, "Bundle on TV Show Fragment is not null.");

            tvShowInstance= savedInstanceState.getParcelableArrayList(LIST_TAG);
            setDefaultRecyclerView();
        }
        else {
            Log.i(LOG_TAG, "Bundle on TV Show Fragment is null.");

            final int fragmentIdx = 2;
            GetListTask getListTask = new GetListTask(getContext(), fragmentIdx);
            getListTask.execute(fragmentIdx);

            final SearchView searchView = view.findViewById(R.id.sv_tv_show);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    GetSearchListTask getSearchListTask = new GetSearchListTask(getContext(), fragmentIdx);
                    getSearchListTask.execute(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private final int HIDE_THRESHOLD = 20;
            private int scrollDistance = 0;
            Toolbar toolbar = view.findViewById(R.id.tb_tv_show);

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (scrollDistance > HIDE_THRESHOLD) {
                    toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                }
                else if (scrollDistance < -HIDE_THRESHOLD) {
                    toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                }

                scrollDistance = 0;
                scrollDistance += dy;
            }
        });

        setRetainInstance(true);
    }

    private void setDefaultRecyclerView() {
        PageAdapter adapter = new PageAdapter(tvShowInstance);
        adapter.notifyDataSetChanged();

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_TAG, tvShowInstance);
        outState.putParcelableArrayList(LIST_SEARCH_TAG, tvShowSearchInstance);
        Log.i(LOG_TAG, "SaveInstance on TV Show Fragment.");
    }
}

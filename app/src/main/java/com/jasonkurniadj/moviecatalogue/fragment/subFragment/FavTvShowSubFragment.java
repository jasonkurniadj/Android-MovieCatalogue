package com.jasonkurniadj.moviecatalogue.fragment.subFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.adapter.FavoriteRecyclerViewAdapter;
import com.jasonkurniadj.moviecatalogue.database.DatabaseHelper;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class FavTvShowSubFragment extends Fragment {

    public FavTvShowSubFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_fragment_fav_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<ItemModel> modelList = db.getAllItems("TV Show");

        if (modelList.size() > 0) {
            RecyclerView rvFavTvShow = view.findViewById(R.id.rv_fav_tv_show);
            FavoriteRecyclerViewAdapter recyclerViewAdapter = new FavoriteRecyclerViewAdapter(modelList);

            rvFavTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
            rvFavTvShow.setAdapter(recyclerViewAdapter);
        }
    }
}

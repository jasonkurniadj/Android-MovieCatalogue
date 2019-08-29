package com.jasonkurniadj.moviecataloguefavorite.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasonkurniadj.moviecataloguefavorite.R;
import com.jasonkurniadj.moviecataloguefavorite.adapter.RecyclerViewAdapter;
import com.jasonkurniadj.moviecataloguefavorite.database.DatabaseHelper;
import com.jasonkurniadj.moviecataloguefavorite.model.ItemModel;

import java.util.ArrayList;

public class FavMovieFragment extends Fragment {

    public FavMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        ArrayList<ItemModel> modelList = databaseHelper.fetchData("Movie");

        if (modelList.size() > 0) {
            RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(modelList);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }


}

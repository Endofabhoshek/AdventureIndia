package com.abhiandroid.adventureindia.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.abhiandroid.adventureindia.Adapters.AllPlacesAdapter;
import com.abhiandroid.adventureindia.MVP.Place;
import com.abhiandroid.adventureindia.MainActivity;
import com.abhiandroid.adventureindia.R;
import com.abhiandroid.adventureindia.SplashScreen;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Favorite extends Fragment {

    View view;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.empty)
    TextView empty;
    public static ArrayList<Place> imagesList;
    public static ArrayList<Place> imagesList1;
    public static AllPlacesAdapter newsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        MainActivity.searchView.setVisibility(View.GONE);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        // get favorite list data and set it in RecyclerView
        Log.d("arrayListSize", MainActivity.imageIds.size() + "");
        imagesList = new ArrayList<>();
        imagesList1 = new ArrayList<>();
        imagesList.addAll(SplashScreen.newsListResponsesData);
        for (int j = 0; j < imagesList.size(); j++) {

            for (int k = 0; k < MainActivity.imageIds.size(); k++) {
                Log.d("compare", imagesList.get(j).getPlaceId().trim() + " " + MainActivity.imageIds.get(k).trim());
                if (imagesList.get(j).getPlaceId().trim().equalsIgnoreCase(MainActivity.imageIds.get(k).trim())) {
                    imagesList1.add(imagesList.get(j));

                }
            }

        }
        Log.d("imagesSize", imagesList1.size() + "");
        if (imagesList1.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(gridLayoutManager);
        newsListAdapter = new AllPlacesAdapter(getActivity(), imagesList1);
        recyclerview.setAdapter(newsListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.searchView.setVisibility(View.VISIBLE);

    }
}

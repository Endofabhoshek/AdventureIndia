package com.abhiandroid.adventureindia.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhiandroid.adventureindia.Adapters.AllPlacesAdapter;
import com.abhiandroid.adventureindia.DetectConnection;
import com.abhiandroid.adventureindia.MVP.AllPlacesResponse;
import com.abhiandroid.adventureindia.R;
import com.abhiandroid.adventureindia.Retrofit.Api;
import com.abhiandroid.adventureindia.SplashScreen;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AllPlaces extends Fragment {

    View view;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    public static AllPlacesAdapter allPlacesAdapter;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_places, container, false);
        ButterKnife.bind(this, view);
        setData();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getNewsList();
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }

    public void getNewsList() {
        Api.getClient().getAllPlaces(new Callback<AllPlacesResponse>() {
            @Override
            public void success(AllPlacesResponse newsListResponses, Response response) {
                SplashScreen.newsListResponsesData.clear();
                SplashScreen.newsListResponsesData.addAll(newsListResponses.getPlace());
//                allPlacesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                setData();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(gridLayoutManager);
        allPlacesAdapter = new AllPlacesAdapter(getActivity(), SplashScreen.newsListResponsesData);
        recyclerview.setAdapter(allPlacesAdapter);
    }
}

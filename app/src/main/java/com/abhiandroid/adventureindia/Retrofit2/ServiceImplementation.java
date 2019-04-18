package com.abhiandroid.adventureindia.Retrofit2;

import com.abhiandroid.adventureindia.Retrofit.ApiInterface;

import retrofit.RestAdapter;
import retrofit2.Retrofit;

public class ServiceImplementation {
    public static Serive getClient() {

        // change your base URL
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://192.168.1.105") //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        Serive api = adapter.create(Serive.class);
        return api;
    }
}

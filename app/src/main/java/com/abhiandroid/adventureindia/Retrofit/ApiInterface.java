package com.abhiandroid.adventureindia.Retrofit;


import java.util.List;

import com.abhiandroid.adventureindia.MVP.AllPlacesResponse;
import com.abhiandroid.adventureindia.MVP.CategoryListResponse;
import com.abhiandroid.adventureindia.MVP.RegistrationResponse;
import com.abhiandroid.adventureindia.MVP.RegisterUserResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints
    @GET("/app_dashboard/JSON/allplaces.php")
    public void getAllPlaces(
            Callback<AllPlacesResponse> callback);

    @GET("/app_dashboard/JSON/placenew.php")
    public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @FormUrlEncoded
    @POST("/app_dashboard/JSON/pushadd.php")
    public void sendAccessToken(@Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);

    @FormUrlEncoded
    @POST("")
    public void sendRegistrationDetails(@Field("username") String username,@Field("email") String email,
                                        @Field("password") String password, Callback<RegisterUserResponse> callback);
}

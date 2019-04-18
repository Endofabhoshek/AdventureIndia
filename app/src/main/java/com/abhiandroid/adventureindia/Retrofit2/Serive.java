package com.abhiandroid.adventureindia.Retrofit2;

import com.abhiandroid.adventureindia.MVP.RegisterUserResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;

public interface Serive {
    @FormUrlEncoded
    @POST("/app_dashboard/JSON/register.php")
    public Call<RegisterUserResponse> sendRegistrationDetails(@Field("username") String username, @Field("email") String email,
                                                       @Field("password") String password, @Field("gender") String gender);
}

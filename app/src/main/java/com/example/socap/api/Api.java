package com.example.socap.api;

import com.example.socap.model.Auth;
import com.example.socap.model.News;
import com.example.socap.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @GET("news")
    Call<List<News>> getNews();

    @POST("auth/login")
    @FormUrlEncoded
    Call<Auth> authenticateUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("auth/loginById")
    @FormUrlEncoded
    Call<Auth> authenticateUserById(@Field("id") int id);
}

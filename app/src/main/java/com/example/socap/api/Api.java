package com.example.socap.api;

import androidx.annotation.Nullable;

import com.example.socap.model.Auth;
import com.example.socap.model.Conversation;
import com.example.socap.model.News;
import com.example.socap.model.ResponseJson;
import com.example.socap.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    @GET("conversation")
    Call<List<Conversation>> getUserConversations();

    @GET("friends")
    Call<List<User>> getFriends();

    @GET("conversation/to/{user}/get")
    Call<Conversation> getMessages(@Path("user") int id);

    @POST("message")
    @FormUrlEncoded
    Call<Conversation> sendMessage(@Field("text") String text, @Field("to_user_id") int id, @Field("conversation_id") int conversation_id  );

    Call<Auth> logout(int id);
    @Multipart
    @POST("user/profile/update")
    Call<ResponseJson> updateProfile(
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone_number") RequestBody phone_number,
            @Part("bio") RequestBody bio,
            @Part MultipartBody.Part fichier
    );

    @GET("user/{user}")
    Call<User> getUser(@Path("user") int id);
}

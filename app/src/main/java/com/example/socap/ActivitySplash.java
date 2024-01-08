package com.example.socap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Tools;
import com.example.socap.model.Auth;
import com.example.socap.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.pusher.pushnotifications.PushNotifications;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {
    private Api api;
    SharedPreferences sharedPreferences;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_splash);
        parent_view = findViewById(android.R.id.content);
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);



        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);
        if (sharedPreferences != null) {
            if (sharedPreferences.contains("name") && sharedPreferences.contains("id")) {
                createNewSessionById(sharedPreferences.getInt("id", 1));
            }
        }

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void createNewSessionById(int id) {
        Call<Auth> call = api.authenticateUserById(
                id
        );
        call.enqueue(new Callback<Auth>() {

            @Override
            public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                Auth auth = response.body();
                if (auth != null) {
                    if (response.message().equals("OK") && auth.getStatus().equals("success")) {
                        User user = auth.getUser();
                        PushNotifications.start(getApplicationContext(), "1e06e9af-9177-4b35-839a-cad08fc28ff6");
                        PushNotifications.addDeviceInterest("App.User."+user.getId());
                        Snackbar.make(parent_view, "Login Success " + user.getName(), Snackbar.LENGTH_LONG).show();
                        setUserInLocalStorage(auth);
                        Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setUserInLocalStorage(Auth auth){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", auth.getUser().getId());
        editor.putString("name", auth.getUser().getName());
        editor.putString("token", auth.getToken());
        editor.apply();
    }
}
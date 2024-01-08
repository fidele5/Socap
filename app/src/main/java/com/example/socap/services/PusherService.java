package com.example.socap.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.socap.model.Conversation;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONObject;

public class PusherService extends Service {
    private static final String TAG = "PusherService";

    SharedPreferences sharedPreferences;

    public PusherService(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initPusher(){
        PusherOptions options = new PusherOptions();
        options.setCluster("ap2");
        sharedPreferences = getApplicationContext().getSharedPreferences("auth", MODE_PRIVATE);

        Pusher pusher = new Pusher("e883ca9f0cf5b5f2d41e", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("message." + sharedPreferences.getInt("id", 1));
        Channel post_channel = pusher.subscribe("post-channel");

        channel.bind("new_message", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Intent intent = new Intent("NEW_MESSAGE");
                intent.putExtra("message",event.toString());
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onError(String message, Exception e) {
                SubscriptionEventListener.super.onError(message, e);
            }
        });

        post_channel.bind("new_post", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Intent intent = new Intent("NEW_POST");
                intent.putExtra("post", event.toString());
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onError(String message, Exception e) {
                SubscriptionEventListener.super.onError(message, e);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        initPusher();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

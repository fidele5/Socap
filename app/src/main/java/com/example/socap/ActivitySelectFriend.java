package com.example.socap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.adapter.FriendsListAdapter;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Constant;
import com.example.socap.data.Tools;
import com.example.socap.fragment.PageMessageFragment;
import com.example.socap.model.Auth;
import com.example.socap.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySelectFriend extends AppCompatActivity {

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    private SearchView search;
    SharedPreferences sharedPreferences;
    private Api api;
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_select_friend);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);
        users = new ArrayList<>();

        initToolbar();

        if(!taskRunning){
            new AttempGetUsersTask().execute("");
        }

        mAdapter = new FriendsListAdapter(this, Constant.getFriendsData(this));
        recyclerView.setAdapter(mAdapter);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_social_menu_activity_select_friend, menu);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return true;
            }
        });
        search.onActionViewCollapsed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_search) {// this do magic
            supportInvalidateOptionsMenu();
            return true;
        }
        return false;
    }

    public void setAdapter(List<User> users){
        mAdapter = new FriendsListAdapter(this, users);
        recyclerView.setAdapter(mAdapter);
        actionBar.setSubtitle(users.size()+" friends");
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User obj, int position) {
                Intent i = new Intent(getApplicationContext(), ActivityChatDetails.class);
                i.putExtra(ActivityChatDetails.KEY_FRIEND, obj);
                startActivity(i);
            }
        });
    }

    private boolean taskRunning = false;
    private class AttempGetUsersTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                recyclerView.setVisibility(View.GONE);
                Call<List<User>> call = api.getFriends();
                call.enqueue(new Callback<List<User>>() {

                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        List<User> data = response.body();
                        if (data != null) {
                            for (User u:data) {
                                u = new User(u.getId(), u.getName(), u.getEmail(), u.getPhoto());
                                users.add(u);
                            }
                            if (!users.isEmpty()) {
                                setAdapter(users);
                            }
                        }

                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

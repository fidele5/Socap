package com.example.socap.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.ActivityFriendDetails;
import com.example.socap.MainActivity;
import com.example.socap.R;
import com.example.socap.adapter.FriendsListAdapter;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Constant;
import com.example.socap.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageFriendFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    private View view;
    private SearchView search;
    SharedPreferences sharedPreferences;
    private Api api;

    public List<User> users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_friend, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);
        users = new ArrayList<>();

        mAdapter = new FriendsListAdapter(getActivity(), users);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, User obj, int position) {
                ActivityFriendDetails.navigate((MainActivity) getActivity(), v, obj);
            }
        });

        if (!taskRunning){
            new AttempGetUsersTask().execute("");
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_social_menu_fragment_friend, menu);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setQueryHint("Search Friend...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {}
                return true;
            }
        });
        search.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_new_friend) {
//            Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
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

    private void setAdapter(List<User> users) {
        //set data and list adapter
        mAdapter = new FriendsListAdapter(getActivity(), users);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, User obj, int position) {
                ActivityFriendDetails.navigate((MainActivity) getActivity(), v, obj);
            }
        });
    }

}

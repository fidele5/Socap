package com.example.socap.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.ActivityChatDetails;
import com.example.socap.ActivitySelectFriend;
import com.example.socap.MainActivity;
import com.example.socap.R;
import com.example.socap.adapter.FeedListAdapter;
import com.example.socap.adapter.MessageListAdapter;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Constant;
import com.example.socap.model.Conversation;
import com.example.socap.model.Message;
import com.example.socap.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageMessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageListAdapter mAdapter;
    private ProgressBar progressbar;
    private View view;
    private SearchView search;
    private List<Conversation> conversations;
    SharedPreferences sharedPreferences;
    private Api api;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_message, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        conversations = new ArrayList<>();

        sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);

        mAdapter = new MessageListAdapter(getActivity(), conversations);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Conversation obj, int position) {
                if (obj.getToUserId() == sharedPreferences.getInt("id", 1)){
                    ActivityChatDetails.navigate((MainActivity)getActivity(), view.findViewById(R.id.lyt_parent), obj.getFromUser(), "");
                }else{
                    ActivityChatDetails.navigate((MainActivity)getActivity(), view.findViewById(R.id.lyt_parent), obj.getToUser(), "");
                }

            }
        });

        if(!taskRunning){
            new DummyMessageLoader().execute("");
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("NEW_MESSAGE"));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_social_menu_fragment_message, menu);search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setQueryHint("Search Message...");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {

                }
                return true;
            }
        });
        search.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_message) {
            Intent i = new Intent(getActivity(), ActivitySelectFriend.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        //mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private boolean taskRunning = false;
    private class DummyMessageLoader extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            taskRunning = true;
            conversations.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                progressbar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                Call<List<Conversation>> call = api.getUserConversations();
                call.enqueue(new Callback<List<Conversation>>() {
                    @Override
                    public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                        conversations = response.body();

                        if (conversations != null) {
                            if (!conversations.isEmpty()) {
                                setAdapter(conversations);
                            }
                        }

                        progressbar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<List<Conversation>> call, Throwable t) {
                        Log.d("news", call.request().url().toString());
                        t.printStackTrace();
                    }
                });
            }catch (Exception e){

            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            taskRunning = false;
            super.onProgressUpdate(values);
        }
    }

    private void setAdapter(List<Conversation> conversations) {
        mAdapter = new MessageListAdapter(getActivity(), conversations);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Conversation obj, int position) {
                sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
                if (obj.getToUserId() == sharedPreferences.getInt("id", 1)){
                    ActivityChatDetails.navigate((MainActivity)getActivity(), view.findViewById(R.id.lyt_parent), obj.getFromUser(), "");
                }else{
                    ActivityChatDetails.navigate((MainActivity)getActivity(), view.findViewById(R.id.lyt_parent), obj.getToUser(), "");
                }

            }
        });
    }

    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent ) {
            String data = intent.getStringExtra("message");
            if (data != null) {
                new DummyMessageLoader().execute("");
            }
        }
    };

}

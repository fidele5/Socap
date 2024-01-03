package com.example.socap.fragment;

import static android.content.Context.MODE_PRIVATE;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.R;
import com.example.socap.adapter.FeedListAdapter;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Constant;
import com.example.socap.model.News;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PageFeedFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private FeedListAdapter mAdapter;
    private Api api;
    private List<News> news;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_feed, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);

        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);

        news = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        if(!taskRunning){
            new DummyFeedLoader().execute("");
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_social_menu_fragment_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_feed) {
            Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean taskRunning = false;
    private class DummyFeedLoader extends AsyncTask<String, String, String> {
        private String status="";
        private List<News> items = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            taskRunning = true;
            items.clear();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                progressbar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                Call<List<News>> call = api.getNews();
                call.enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        news = response.body();

                        if (news != null) {
                            if (!news.isEmpty()) {
                                mAdapter = new FeedListAdapter(getActivity(), news);
                                recyclerView.setAdapter(mAdapter);
                            }
                        }

                        progressbar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {
                        Log.d("news", call.request().url().toString());
                        t.printStackTrace();
                    }
                });
            }catch (Exception e){
                status = "failed";
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

}

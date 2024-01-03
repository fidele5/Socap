package com.example.socap.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.ActivityFriendDetails;
import com.example.socap.R;
import com.example.socap.adapter.FeedListAdapter;
import com.example.socap.model.News;
import com.example.socap.model.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class FriendActivitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FeedListAdapter mAdapter;
    private List<News> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_social_fragment_friend_activities, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);

        TypedArray feed_photo = getResources().obtainTypedArray(R.array.app_social_feed_photos);
        // define feed wrapper
        User user = ActivityFriendDetails.user;
        items.add(new News(0, "14:56", user, getString(R.string.app_social_middle_lorem_ipsum), ""));
        items.add(new News(1, "11:30", user, feed_photo.getResourceId(0, -1)));
        items.add(new News(2, "09:10", user, getString(R.string.app_social_lorem_ipsum), ""));
        items.add(new News(3, "Yesterday", user, getString(R.string.app_social_short_lorem_ipsum), "", feed_photo.getResourceId(2, -1)));
        items.add(new News(4, "05 Nov 2015", user, getString(R.string.app_social_long_lorem_ipsum), ""));

        mAdapter = new FeedListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}

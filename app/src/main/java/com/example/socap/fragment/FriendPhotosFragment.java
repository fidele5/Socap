package com.example.socap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socap.R;
import com.example.socap.adapter.AlbumGridAdapter;
import com.example.socap.data.Constant;
import com.example.socap.data.Tools;


public class FriendPhotosFragment extends Fragment {
    private RecyclerView recyclerView;
    public AlbumGridAdapter mAdapter;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_fragment_friend_photos, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //set data and list adapter
        mAdapter = new AlbumGridAdapter(getActivity(), Constant.getFriendsAlbumData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

}

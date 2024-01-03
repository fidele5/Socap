package com.example.socap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socap.R;
import com.google.android.material.snackbar.Snackbar;

public class PageProfileFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_profile, container, false);
        return view;
    }

    public void actionClick(View view){
        int id = view.getId();
        if (id == R.id.lyt_view_profile) {
            Snackbar.make(view, "View Profile Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_group_cat) {
            Snackbar.make(view, "Group - Cat Lover Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_group_hangout) {
            Snackbar.make(view, "Group - Hangout Friend Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_group_collage) {
            Snackbar.make(view, "Group - Collage Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_setting) {
            Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_help) {
            Snackbar.make(view, "Help nad FAQ Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.lyt_logout) {
            Snackbar.make(view, "Logout Clicked", Snackbar.LENGTH_SHORT).show();
        }
    }

}

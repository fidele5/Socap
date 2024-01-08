package com.example.socap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.socap.fragment.FriendAboutFragment;
import com.example.socap.fragment.FriendActivitiesFragment;
import com.example.socap.fragment.FriendPhotosFragment;
import com.example.socap.model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityFriendDetails extends AppCompatActivity {
    public static final String EXTRA_OBJCT = "template.social.FRIEND";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, User obj) {
        Intent intent = new Intent(activity, ActivityFriendDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private ViewPager mViewPager;
    private FriendAboutFragment frag_friendAbout;
    private FriendActivitiesFragment frag_friendActivity;
    private FriendPhotosFragment frag_friendPhotos;
    private ActionBar actionBar;
    public static User user;

    private TextView description_1;
    private TextView description_2;
    private TextView description2;
    private TextView descriptioxn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_friend_details);

        // animation transition
        ViewCompat.setTransitionName(findViewById(android.R.id.content), EXTRA_OBJCT);

        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get extra object
        user = (User) getIntent().getSerializableExtra(EXTRA_OBJCT);

        // scollable toolbar
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getName());
        ImageView ivImage = (ImageView)findViewById(R.id.ivImage);
        ivImage.setImageResource(user.getPhoto());
        description_1 = (TextView) findViewById(R.id.description_1);
        description_2 = (TextView) findViewById(R.id.description1);
        description2 = (TextView) findViewById(R.id.description2);
        descriptioxn = (TextView) findViewById(R.id.descriptioxn);

        description_1.setText(user.getName());
        description_2.setText(user.getPhoneNumber());
        description2.setText(user.getEmail());
        descriptioxn.setText(user.getBiography());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.action_send_message){
            Intent i = new Intent(getApplicationContext(), ActivityChatDetails.class);
            i.putExtra(ActivityChatDetails.KEY_FRIEND, user);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_social_menu_activity_friend_details, menu);
        return true;
    }
}

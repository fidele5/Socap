package com.example.socap;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.socap.adapter.PageFragmentAdapter;
import com.example.socap.data.Tools;
import com.example.socap.fragment.PageFeedFragment;
import com.example.socap.fragment.PageFriendFragment;
import com.example.socap.fragment.PageMessageFragment;
import com.example.socap.fragment.PageNotifFragment;
import com.example.socap.fragment.PageProfileFragment;
import com.example.socap.services.PusherService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private View parent_view;

    private PageFragmentAdapter adapter;

    private PageFeedFragment f_feed;
    private PageFriendFragment f_friend;
    private PageMessageFragment f_message;
    private PageNotifFragment f_notif;
    private PageProfileFragment f_profile;
    private static int[] imageResId = {
            R.drawable.app_social_ic_feed_dark,
            R.drawable.app_social_ic_tab_friend_dark,
            R.drawable.app_social_ic_tab_message_dark,
            R.drawable.app_social_ic_tab_notification_dark,
            R.drawable.app_social_ic_tab_profile_dark
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_main);
        parent_view = findViewById(android.R.id.content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();
        startService(new Intent(this, PusherService.class));

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_feed == null) { f_feed = new PageFeedFragment(); }
        if (f_friend == null) { f_friend = new PageFriendFragment(); }
        if (f_message == null) { f_message = new PageMessageFragment(); }
        //if (f_notif == null) { f_notif = new PageNotifFragment(); }
        if (f_profile == null) { f_profile = new PageProfileFragment(); }
        adapter.addFragment(f_feed, getString(R.string.app_social_tab_feed));
        adapter.addFragment(f_friend, getString(R.string.app_social_tab_friend));
        adapter.addFragment(f_message, getString(R.string.app_social_tab_message));
        //adapter.addFragment(f_notif, getString(R.string.app_social_tab_notif));
        adapter.addFragment(f_profile, getString(R.string.app_social_tab_profile));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        //tabLayout.getTabAt(3).setIcon(imageResId[3]);
        tabLayout.getTabAt(3).setIcon(imageResId[4]);

        int tabIconColor = ContextCompat.getColor(this, android.R.color.white);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                actionbar.setTitle(adapter.getTitle(position));
                int tabIconColor = ContextCompat.getColor(MainActivity.this, android.R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.app_social_colorPrimaryDark);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_about) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("About");
//            builder.setMessage(getString(R.string.app_social_about_text));
//            builder.setNeutralButton("OK", null);
//            builder.show();
//        } else if (id == R.id.action_login) {
//            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
//            startActivity(i);
//        } else if (id == R.id.action_settings) {
//            Snackbar.make(parent_view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_social_menu_activity_main, menu);
        return true;
    }

    // handle click profile page
    public void actionClick(View view){
        f_profile.actionClick(view);
    }

}
package com.example.socap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.socap.adapter.MessageDetailsListAdapter;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Tools;
import com.example.socap.model.Conversation;
import com.example.socap.model.MessageDetails;
import com.example.socap.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChatDetails extends AppCompatActivity {

    public static String KEY_FRIEND     = "template.social.FRIEND";
    public static String KEY_SNIPPET   = "template.social.SNIPPET";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, User obj, String snippet) {
        Intent intent = new Intent(activity, ActivityChatDetails.class);
        intent.putExtra(KEY_FRIEND, obj);
        intent.putExtra(KEY_SNIPPET, snippet);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_FRIEND);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Button btn_send;
    private EditText et_content;
    public static MessageDetailsListAdapter adapter;

    private ListView listview;
    private ActionBar actionBar;
    private User user;
    private List<MessageDetails> items = new ArrayList<>();
    private View parent_view;
    SharedPreferences sharedPreferences;
    private Api api;
    private Conversation conversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_chat_details);
        parent_view = findViewById(android.R.id.content);
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);
        conversation = new Conversation();
        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_FRIEND);

        // initialize conversation data
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable(KEY_FRIEND);
        String snippets = intent.getStringExtra(KEY_SNIPPET);
        initToolbar();

        iniComponen();
//        if(snippets != null){
//            items.add(new MessageDetails(999, "09:55", user, snippets, false));
//        }
//        items.addAll(Constant.getMessageDetailsData(this, user));

        adapter = new MessageDetailsListAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setSelectionFromTop(adapter.getCount(), 0);
        listview.requestFocus();
        registerForContextMenu(listview);

        if (!taskRunning){
            new AttempGetMessagesTask().execute("");
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(listener, new IntentFilter("NEW_MESSAGE"));

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(user.getName());
    }

    public void bindView() {
        try {
            adapter.notifyDataSetChanged();
            listview.setSelectionFromTop(adapter.getCount(), 0);
        } catch (Exception e) {

        }
    }

    public void iniComponen() {
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messageTaskRunning){
                    Log.d("Test", "test");
                    new AttemptSendMessage().execute("");
                }
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setEnabled(false);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
            //draft.setContent(etd.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_social_menu_chat_details, menu);
        return true;
    }

    /**
     * Handle click on action bar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
//        else if (itemId == R.id.action_sample) {
//            Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private boolean messageTaskRunning = false;
    private class AttemptSendMessage extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Call<Conversation> call = api.sendMessage(et_content.getText().toString(), user.getId(), conversation.getId());
                call.enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(@NonNull Call<Conversation> call, @NonNull Response<Conversation> response) {
                        Conversation data = response.body();

                        if (data != null) {
                            conversation = new Conversation(
                                    data.getId(),
                                    data.getDate(),
                                    data.getFromUserId(),
                                    data.getToUserId(),
                                    data.getFromUser(),
                                    data.getToUser(),
                                    data.getMessages()
                            );

                            if (conversation.getMessages() != null) {
                                if (!conversation.getMessages().isEmpty()) {
                                    for (MessageDetails message : conversation.getMessages()) {
                                        Log.d("message", "is from me " + message.isFromMe());
                                        items.add(new MessageDetails(
                                                message.getId(),
                                                message.getDate(),
                                                message.getFriend(),
                                                message.getContent(),
                                                message.isFromMe())
                                        );
                                    }
                                }
                            }

                            et_content.setText("");
                            bindView();
                            hideKeyboard();
                        }

                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private boolean taskRunning = false;
    private class AttempGetMessagesTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("id", "user "+user.getId());
            try {
                Call<Conversation> call = api.getMessages(user.getId());
                call.enqueue(new Callback<Conversation>() {

                    @Override
                    public void onResponse(@NonNull Call<Conversation> call, @NonNull Response<Conversation> response) {
                        Conversation data = response.body();
                        Log.d("response", call.request().url().toString());
                        if (data != null) {
                            conversation = new Conversation(
                                    data.getId(),
                                    data.getDate(),
                                    data.getFromUserId(),
                                    data.getToUserId(),
                                    data.getFromUser(),
                                    data.getToUser(),
                                    data.getMessages()
                            );

                            //Log.d("conversation", conversation.getDate());

                            if (conversation.getMessages() != null) {
                                if (!conversation.getMessages().isEmpty()) {
                                    for (MessageDetails message : conversation.getMessages()) {
                                        items.add(new MessageDetails(
                                                message.getId(),
                                                message.getDate(),
                                                message.getFriend(),
                                                message.getContent(),
                                                message.isFromMe())
                                        );
                                    }

                                    adapter = new MessageDetailsListAdapter(ActivityChatDetails.this, items);
                                    listview.setAdapter(adapter);
                                    listview.setSelectionFromTop(adapter.getCount(), 0);
                                    listview.requestFocus();
                                    registerForContextMenu(listview);
                                }
                            }
                            //                            for (User u:data) {
//                                u = new User(u.getId(), u.getName(), u.getPhoto());
//                                users.add(u);
//                            }
//                            if (!users.isEmpty()) {
//                                setAdapter(users);
//                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {
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

    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive( Context context, Intent intent ) {
            String data = intent.getStringExtra("message");
            if (data != null) {
                new AttempGetMessagesTask().execute("");
            }
        }
    };
}

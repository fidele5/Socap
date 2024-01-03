package com.example.socap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.data.Tools;
import com.example.socap.model.Auth;
import com.example.socap.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.pusher.pushnotifications.PushNotifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private View parent_view;
    private Api api;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_login);

        parent_view = findViewById(android.R.id.content);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        new AttempLoginTask().execute("");
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.app_social_err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.app_social_err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else if (inputPassword.getText().length()<5){
            inputLayoutPassword.setError(getString(R.string.app_social_inv_msg_password));
            requestFocus(inputPassword);
            return false;
        }else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int id = view.getId();
            if (id == R.id.input_email) {
                validateEmail();
            } else if (id == R.id.input_password) {
                validatePassword();
            }
        }
    }

    private class AttempLoginTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Thread.sleep(1500);
                Call<Auth> call = api.authenticateUser(
                        inputEmail.getText().toString().trim(),
                        inputPassword.getText().toString().trim()
                );
                call.enqueue(new Callback<Auth>() {

                    @Override
                    public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                        Auth auth = response.body();

                        if (auth != null) {
                            if (response.message().equals("OK") && auth.getStatus().equals("success")) {
                                User user = auth.getUser();
                                PushNotifications.start(getApplicationContext(), "1e06e9af-9177-4b35-839a-cad08fc28ff6");
                                PushNotifications.addDeviceInterest("App.User."+user.getId());
                                Snackbar.make(parent_view, "Login Success " + user.getName(), Snackbar.LENGTH_SHORT).show();
                                hideKeyboard();
                                setUserInLocalStorage(auth);
                                Intent i = new Intent(ActivityLogin.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {
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

    public void setUserInLocalStorage(Auth auth){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", auth.getUser().getId());
        editor.putString("name", auth.getUser().getName());
        editor.putString("token", auth.getToken());
        editor.apply();
    }

    public void logout(){
        SharedPreferences sharedPrefs = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }
}


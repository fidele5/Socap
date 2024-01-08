package com.example.socap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.model.ResponseJson;
import com.example.socap.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {
    private static final int GALLERY_REQ_SERVICE = 1000;
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private ActionBar actionBar;
    private String picturePath = "";
    private ImageView imageView;
    Uri filePath;
    public FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;
    private Api api;
    private AppCompatEditText profile_name;
    private AppCompatEditText profile_phone;
    private AppCompatEditText profile_email;
    private TextInputLayout inputLayoutEmail;
    private Button btn_edit_profile;
    private ProgressBar progressBar;

    public static UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initToolbar();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.upload_image);
        imageView = (ImageView) findViewById(R.id.photo_profile);
        profile_name = (AppCompatEditText) findViewById(R.id.profile_name);
        profile_email = (AppCompatEditText) findViewById(R.id.profile_email);
        profile_phone = (AppCompatEditText) findViewById(R.id.profile_phone);
        profile_email.addTextChangedListener(new MyTextWatcher(profile_email));
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.profile_layout_email);
        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);

        user = (UserModel) getIntent().getSerializableExtra("user");
        Log.d("@@phone", user.getPhoneNumber() + "");

        profile_name.setText(user.getName());
        profile_email.setText(user.getEmail());
        profile_phone.setText(user.getPhoneNumber() + "");
        requestStoragePermission();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granter", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Not Granter", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Modifier le profil");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi)
    {
        if (mi.getItemId() == android.R.id.home) {
            finish();
        }
        else {
            save();
        }
        return super.onOptionsItemSelected(mi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imageView.setImageURI(filePath);
            Log.d("uri", filePath.getPath());
        }
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select A Display Image"), PICK_IMAGE_REQUEST);
    }

    private String getPath(Uri contentURI) {
        String result;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        Log.d("result", result);
        return result;
    }

    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

//        if (!validatePassword()) {
//            return;
//        }
        new AttempUpdateProfileTask().execute("");
    }

    private class AttempUpdateProfileTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btn_edit_profile.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                save();
            } catch (Exception e) {
                e.printStackTrace();
                progressBar.setVisibility(View.VISIBLE);
                btn_edit_profile.setVisibility(View.VISIBLE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        btn_edit_profile.setVisibility(View.GONE);
        String mediaPath = "";
        MultipartBody.Part fileData = null;
        if (filePath != null) {
            mediaPath = getPath(filePath);
            File file = new File(mediaPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
             fileData = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        }

        int user_id = sharedPreferences.getInt("id", 0);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), profile_email.getText().toString());
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"),  profile_phone.getText().toString());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),  profile_name.getText().toString());
        RequestBody bio = RequestBody.create(MediaType.parse("text/plain"),  profile_name.getText().toString());
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),  user_id + "");

        Call<ResponseJson> call = api.updateProfile(id, name, email, phoneNumber, bio, fileData);
        call.enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                ResponseJson statusSchema = response.body();
                if (response.message().equals("OK")) {
                    if (statusSchema != null) {
                        if (statusSchema.getStatus().equals("success")) {
                            hideKeyboard();
                            setUserInLocalStorage(profile_name.getText().toString());
                            progressBar.setVisibility(View.GONE);
                            btn_edit_profile.setVisibility(View.VISIBLE);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btn_edit_profile.setVisibility(View.VISIBLE);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    btn_edit_profile.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    btn_edit_profile.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btn_edit_profile.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

    private void setUserInLocalStorage(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
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
            if (id == R.id.profile_email) {
                validateEmail();
            } else if (id == R.id.input_password) {
                //validatePassword();
            }
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail() {
        String email = profile_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.app_social_err_msg_email));
            requestFocus(profile_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
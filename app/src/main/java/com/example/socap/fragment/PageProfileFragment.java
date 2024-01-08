package com.example.socap.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.socap.ActivitySplash;
import com.example.socap.EditProfileActivity;
import com.example.socap.R;
import com.example.socap.api.Api;
import com.example.socap.api.ApiClient;
import com.example.socap.model.User;
import com.example.socap.model.UserModel;
import com.google.android.material.snackbar.Snackbar;
//import io.github.pierry.progress.Progress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageProfileFragment extends Fragment {
    View view;
    TextView profile_name;
    SharedPreferences sharedPreferences;
    private Api api;
    public static User user;
//    public Progress progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_profile, container, false);
        sharedPreferences = getContext().getSharedPreferences("auth", MODE_PRIVATE);
        api = ApiClient.getClient(sharedPreferences.getString("token", "")).create(Api.class);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_name.setText(sharedPreferences.getString("name", "User"));
//        progress = new Progress(getContext());
//        progress.setBackgroundColor(getResources().getColor(R.color.white))
//                .setMessage("Chargement..")
//                .setMessageColor(getResources().getColor(R.color.blue_grey_500))
//                .setProgressColor(getResources().getColor(R.color.blue_grey_300));

        new AttempGetUserTask().execute("");
        return view;
    }

    public void actionClick(View view){
        int id = view.getId();
        if (id == R.id.lyt_view_profile) {
            Snackbar.make(view, "View Profile Clicked", Snackbar.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), EditProfileActivity.class);
            Bundle bundle = new Bundle();
            UserModel userModel = new UserModel(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getBiography(),
                    user.getPhoto()
            );

            bundle.putSerializable("user", userModel);
            i.putExtras(bundle);
            //i.putExtra("user", user);
            startActivity(i);
        } else if (id == R.id.lyt_setting) {
            Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("About");
            builder.setMessage("A venir dans les versions suivantes :-) fidele plk");
            builder.setNeutralButton("OK", null);
            builder.show();
        } else if (id == R.id.lyt_help) {
            Snackbar.make(view, "See on web page", Snackbar.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.lyt_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Deconnexion");
            builder.setMessage("Etes-vous sûr de vouloir vous déconnecter ?");
            builder.setPositiveButton("Deconnexion", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout();
                }
            });
            builder.setNegativeButton("Annuler", null);
            builder.show();
        }
    }

    public void logout(){
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(getActivity(), ActivitySplash.class);
        startActivity(i);
        if (getActivity() != null) {
            getActivity().finishAffinity();
        }
        Snackbar.make(view, "Logout Clicked", Snackbar.LENGTH_SHORT).show();
    }

    private class AttempGetUserTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
//                progress.show();
                //Thread.sleep(1500);
                Call<User> call = api.getUser(sharedPreferences.getInt("id", 0));
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User data = response.body();

                        if (data != null) {
                            if (response.message().equals("OK")) {
                                user = new User(
                                     data.getId(),
                                     data.getName(),
                                     data.getEmail(),
                                     data.getPhoto()
                                );

                                user.setPhoneNumber(data.getPhoneNumber());
                                user.setBiography(data.getBiography());
                                user.setCommunities(data.getCommunities());
                                setUserInLocalStorage(user);


                                Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
//                                progress.dismiss();
                            }
                        }

//                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
//                        progress.dismiss();
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

    private void setUserInLocalStorage(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.apply();
    }

}

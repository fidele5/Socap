package com.example.socap.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;


import com.example.socap.R;
import com.example.socap.model.News;
import com.example.socap.model.User;
import com.example.socap.model.Friend_photos;
import com.example.socap.model.Message;
import com.example.socap.model.MessageDetails;
import com.example.socap.model.Notif;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("ResourceType")
public class Constant {


    public static Resources getStrRes(Context context) {
        return context.getResources();
    }

    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }


    public static List<User> getFriendsData(Context ctx)  {
        List<User> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.app_social_people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.app_social_people_photos);
        for (int i = 0; i < s_arr.length; i++) {
            User fr = new User(i, s_arr[i], drw_arr.getResourceId(i, -1));
            items.add(fr);
        }
        return items;
    }

    public static List<Friend_photos> getFriendsAlbumData(Context ctx)  {
        List<Friend_photos> items = new ArrayList<>();
        String album_name[] = ctx.getResources().getStringArray(R.array.app_social_friend_photo_album_name);
        TypedArray photo = ctx.getResources().obtainTypedArray(R.array.app_social_friend_photo_album_photo);
        for (int i = 0; i <album_name.length; i++) {
            items.add(new Friend_photos(i, album_name[i], photo.getResourceId(i, -1), (5+i) ));
        }
        return items;
    }

    public static List<News> getRandomFeed(Context ctx)  {
        List<News> items = new ArrayList<>();
        List<User> users = getFriendsData(ctx);
        String rand_date[] = ctx.getResources().getStringArray(R.array.app_social_random_date);
        String rand_lorem[] = getLoremArr(ctx);
        TypedArray photo = ctx.getResources().obtainTypedArray(R.array.app_social_feed_photos);
        int friend_size = users.size()-1;
        int date_size = rand_date.length-1;
        int lorem_size = rand_lorem.length-1;
        int photo_size = photo.length();

        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            int f_i = getRandomIndex(r, 0, friend_size);
            int d_i = getRandomIndex(r, 0, date_size);
            int l_i = getRandomIndex(r, 0, lorem_size);
            int p_i = getRandomIndex(r, 0, photo_size);
            News news = new News();
            news.setFriend(users.get(f_i));
            news.setDate(rand_date[d_i]);
            boolean bool_text = r.nextBoolean();
            if(bool_text){
                news.setText(rand_lorem[l_i]);
            }
            if(!bool_text || r.nextBoolean()){
                news.setPhoto(photo.getResourceId(p_i, -1));
            }
            items.add(news);
        }
        return items;
    }


    public static List<Message> getMessageData(Context ctx)  {
        List<Message> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.app_social_people_names);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.app_social_people_photos);
        String s_cht[] = ctx.getResources().getStringArray(R.array.app_social_message_snippet);
        String s_date[] = ctx.getResources().getStringArray(R.array.app_social_message_date);
        for (int i = 0; i < 10; i++) {
            items.add(new Message(i, s_date[i], true, new User(s_arr[i+5], drw_arr.getResourceId(i+5, -1)), s_cht[i]));
        }
        return items;
    }
    public static List<MessageDetails> getMessageDetailsData(Context ctx, User user)  {
        List<MessageDetails> items = new ArrayList<>();
        String s_date[] = ctx.getResources().getStringArray(R.array.app_social_message_details_date);
        String s_content[] = ctx.getResources().getStringArray(R.array.app_social_message_details_content);

        items.add(new MessageDetails(0, s_date[0], user, s_content[0], false));
        items.add(new MessageDetails(1, s_date[1], user, s_content[1], true));
        items.add(new MessageDetails(2, s_date[2], user, s_content[2], false));

        return items;
    }

    public static List<Notif> getNotifData(Context ctx)  {
        List<Notif> items = new ArrayList<>();
        List<User> users = getFriendsData(ctx);
        String content_arr[] = ctx.getResources().getStringArray(R.array.app_social_notif_text);
        String s_date[] = ctx.getResources().getStringArray(R.array.app_social_notif_date);
        for (int i = 0; i < content_arr.length; i++) {
            items.add(new Notif(i, s_date[i], users.get(i), content_arr[i]));
        }
        return items;
    }




    private static int getRandomIndex(Random r, int min, int max){
        return r.nextInt(max - min) + min;
    }

    private static String[] getLoremArr(Context ctx){
        String rand_lorem[] = new String[4];
        rand_lorem[0] = ctx.getString(R.string.app_social_lorem_ipsum);
        rand_lorem[1] = ctx.getString(R.string.app_social_short_lorem_ipsum);
        rand_lorem[2] = ctx.getString(R.string.app_social_long_lorem_ipsum);
        rand_lorem[3] = ctx.getString(R.string.app_social_middle_lorem_ipsum);
        return  rand_lorem;
    }

}

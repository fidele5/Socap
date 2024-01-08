package com.example.socap.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import template.social.widget.CircleTransform;

import com.example.socap.R;
import com.example.socap.model.News;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private List<News> items = new ArrayList<>();

    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView photo;
        public TextView text_name;
        public ImageView bt_more;
        public ImageView photo_content;
        public ImageView bt_like;
        public ImageView bt_comment;
        public ImageView bt_share;
        public TextView text_content;
        public TextView text_date;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            photo = (ImageView) v.findViewById(R.id.photo);
            text_name = (TextView) v.findViewById(R.id.text_name);
            //bt_more = (ImageView) v.findViewById(R.id.bt_more);
            photo_content = (ImageView) v.findViewById(R.id.photo_content);
//            bt_like = (ImageView) v.findViewById(R.id.bt_like);
//            bt_comment = (ImageView) v.findViewById(R.id.bt_comment);
//            bt_share = (ImageView) v.findViewById(R.id.bt_share);
            text_content = (TextView) v.findViewById(R.id.text_content);
            text_date = (TextView) v.findViewById(R.id.text_date);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeedListAdapter(Context ctx, List<News> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_social_item_list_feed, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News p = items.get(position);
        //Picasso.with(ctx).load(p.getFriend().getPhoto()).resize(80, 80).transform(new CircleTransform()).into(holder.photo);
        holder.text_name.setText(p.getFriend().getName());
        // content photo
//        if (p.getPhoto() != -1) {
//            holder.photo_content.setVisibility(View.VISIBLE);
//            holder.photo_content.setImageResource(p.getPhoto());
//        }
        // content text
        if (p.getText() != null) {
            holder.text_content.setVisibility(View.VISIBLE);
            holder.text_content.setText(p.getText());
        }

        holder.text_date.setText(p.getDate());
//        holder.bt_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                Snackbar.make(view, "More Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        holder.bt_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Like Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.bt_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Comment Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        holder.bt_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Share Clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

        // optimize padding
        if (position == 0) {
            holder.lyt_parent.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(3));
        } else if (position == getItemCount() - 1) {
            holder.lyt_parent.setPadding(dpToPx(8), dpToPx(3), dpToPx(8), dpToPx(8));
        } else {
            holder.lyt_parent.setPadding(dpToPx(8), dpToPx(3), dpToPx(8), dpToPx(3));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
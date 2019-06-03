package com.mrnovacrm.adapter;

/**
 * Created by MITRAYAVSP2 on 4/11/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.RoundedImageView;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.NavDrawerItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private final String USERTYPE;
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    int[] menuicons;

    RelativeLayout rel;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data, int[] menuicons) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.menuicons=menuicons;
        HashMap<String, String> hashMapValues = SharedDB.getUserDetails(context);
        USERTYPE=hashMapValues.get(SharedDB.USERTYPE);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.image.setImageResource(menuicons[position]);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RoundedImageView image;
        RelativeLayout rel;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image=(RoundedImageView)itemView.findViewById(R.id.image);
        }
    }
}
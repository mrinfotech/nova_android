package com.mrnovacrm.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mrnovacrm.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prasad on 3/9/2018.
 */

@SuppressLint("ViewHolder")
public class SpinnerItemsAdapternew extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> values;
    ArrayList<HashMap<String,String>> hashMapArrayList;
    /*
     * Override the constructor for ArrayAdapter the only variable objects,
     * because it is the list of objects to display.
     */
    public SpinnerItemsAdapternew(Activity context, int textViewResourceId,
                               ArrayList<String> values,ArrayList<HashMap<String,String>> hashMapArrayList) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.hashMapArrayList=hashMapArrayList;
    }

    public int getCount() {
       // return values.size();
        return hashMapArrayList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    /* Overriding the getView method */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater
                .inflate(R.layout.layout_spinneritemswhite, null, true);
        TextView txtTitle = (TextView) rowView
                .findViewById(R.id.spinnersubitemslist);
        View viewlinear = (View) rowView
                .findViewById(R.id.view);
        viewlinear.setVisibility(View.GONE);

        String branch_name=hashMapArrayList.get(position).get("branch_name");
        TextView spinnercompanyname = (TextView) rowView
                .findViewById(R.id.spinnercompanyname);
        spinnercompanyname.setVisibility(View.GONE);

        String company=hashMapArrayList.get(position).get("company");
        String role_name=hashMapArrayList.get(position).get("role_name");
       // txtTitle.setText(values.get(position));
        txtTitle.setText(role_name+","+branch_name+","+company);
        txtTitle.setTextColor(context.getResources().getColor(R.color.white));
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater
                .inflate(R.layout.layout_spinneritemswhite, null, true);
        TextView txtTitle = (TextView) rowView
                .findViewById(R.id.spinnersubitemslist);
        TextView spinnercompanyname = (TextView) rowView
                .findViewById(R.id.spinnercompanyname);
        View viewlinear = (View) rowView
                .findViewById(R.id.view);

        spinnercompanyname.setVisibility(View.VISIBLE);
        viewlinear.setVisibility(View.VISIBLE);

        String branch_name=hashMapArrayList.get(position).get("branch_name");
        String company=hashMapArrayList.get(position).get("company");
        String role_name=hashMapArrayList.get(position).get("role_name");

        // txtTitle.setText(values.get(position));
        txtTitle.setText(role_name+", "+branch_name);
        spinnercompanyname.setText(company);
//        txtTitle.setText(values.get(position));
        txtTitle.setTextColor(context.getResources().getColor(R.color.black));
        spinnercompanyname.setTextColor(context.getResources().getColor(R.color.black));

        return rowView;
    }
}

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

/**
 * Created by prasad on 3/9/2018.
 */

@SuppressLint("ViewHolder")
public class SpinnerItemsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> values;

    /*
     * Override the constructor for ArrayAdapter the only variable objects,
     * because it is the list of objects to display.
     */
    public SpinnerItemsAdapter(Activity context, int textViewResourceId,
                               ArrayList<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public long getItemId(int position) {
        return position;
    }

    /* Overriding the getView method */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater
                .inflate(R.layout.layout_spinneritems, null, true);
        TextView txtTitle = (TextView) rowView
                .findViewById(R.id.spinnersubitemslist);
        txtTitle.setText(values.get(position));
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater
                .inflate(R.layout.layout_spinneritems, null, true);
        TextView txtTitle = (TextView) rowView
                .findViewById(R.id.spinnersubitemslist);
        txtTitle.setText(values.get(position));
        return rowView;
    }
}

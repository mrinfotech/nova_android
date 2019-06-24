package com.mrnovacrm.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mrnovacrm.R;

import java.util.ArrayList;

/**
 * Created by harish on 6/24/2019.
 */

@SuppressLint("ViewHolder")
public class CompanyAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> values;

    /*
     * Override the constructor for ArrayAdapter the only variable objects,
     * because it is the list of objects to display.
     */
    public CompanyAdapter(Activity context, ArrayList<String> values) {
        super(context, 0, values);
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
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_spinneritems, null, true);

        TextView txtTitle = rowView.findViewById(R.id.spinnersubitemslist);
        txtTitle.setText(values.get(position));
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_spinneritems, null, true);

        TextView txtTitle = rowView.findViewById(R.id.spinnersubitemslist);
        txtTitle.setText(values.get(position));
        return rowView;
    }
}

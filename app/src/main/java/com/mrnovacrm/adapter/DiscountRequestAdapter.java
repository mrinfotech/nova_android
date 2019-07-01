package com.mrnovacrm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.model.DiscountRequestRecordsDTO;

import java.util.List;

public class DiscountRequestAdapter extends RecyclerView.Adapter<DiscountRequestAdapter.MyViewHolder> {

    private List<DiscountRequestRecordsDTO> discountRequestList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCompanyName, textProductName, textStatus;

        public MyViewHolder(View view) {
            super(view);

            textCompanyName = view.findViewById(R.id.textCompanyName);
            textProductName = view.findViewById(R.id.textProductName);
            textStatus = view.findViewById(R.id.textStatus);
        }
    }


    public DiscountRequestAdapter(Context context, List<DiscountRequestRecordsDTO> moviesList) {

        this.mContext = context;
        this.discountRequestList = moviesList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_discount_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DiscountRequestRecordsDTO discountRequest = discountRequestList.get(position);


        holder.textCompanyName.setText(discountRequest.getCompanyName());

        holder.textProductName.setText(discountRequest.getItemName());

        holder.textStatus.setText(discountRequest.getStatus());


    }

    @Override
    public int getItemCount() {
        return discountRequestList.size();
    }
}
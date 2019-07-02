package com.mrnovacrm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_dealer.EditDiscountRequestActivity;
import com.mrnovacrm.model.DiscountRequestRecordsDTO;

import java.util.List;

public class DiscountRequestAdapter extends RecyclerView.Adapter<DiscountRequestAdapter.MyViewHolder> {

    private List<DiscountRequestRecordsDTO> discountRequestList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textCompanyName, textProductName, textStatus, textDealer, textCoupon, textFromDate, textToDate;
        Button buttonView;

        public MyViewHolder(View view) {
            super(view);

            textCompanyName = view.findViewById(R.id.textCompanyName);
            textProductName = view.findViewById(R.id.textProductName);
            textStatus = view.findViewById(R.id.textStatus);
            textDealer = view.findViewById(R.id.textDealer);
            textCoupon = view.findViewById(R.id.textCoupon);
            textFromDate = view.findViewById(R.id.textFromDate);
            textToDate = view.findViewById(R.id.textToDate);
            buttonView = view.findViewById(R.id.buttonView);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        DiscountRequestRecordsDTO discountRequest = discountRequestList.get(position);


        holder.textCompanyName.setText(discountRequest.getCompanyName().trim());
        holder.textProductName.setText(discountRequest.getItemName().trim());
        holder.textStatus.setText(discountRequest.getStatus().trim());
        holder.textDealer.setText(discountRequest.getDealer().trim());
        holder.textCoupon.setText(discountRequest.getCoupon().trim());
        holder.textFromDate.setText(discountRequest.getStartDate().trim());
        holder.textToDate.setText(discountRequest.getEndDate().trim());

        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditDiscountRequestActivity.discountRequest = discountRequestList.get(position);
                Intent h = new Intent(mContext, EditDiscountRequestActivity.class);
                h.putExtra("EmpId", discountRequestList.get(position).getId());
                mContext.startActivity(h);

            }
        });

    }

    @Override
    public int getItemCount() {
        return discountRequestList.size();
    }
}
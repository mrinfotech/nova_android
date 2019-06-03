package com.mrnovacrm.b2b_dispatch_dept;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DailyOrders;
import com.mrnovacrm.model.DailyOrdersList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/20/2018.
 */

public class PackedOrderPlacedFragment extends Fragment {

    private RecyclerView recyclerview;
    LinearLayout header;
   // TextView text_nodata;
    RelativeLayout imgrel;
    ImageView imageview;
    private String PRIMARYID;
    private String BRANCHID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sellerplacedorders, container, false);

        recyclerview = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);
        header = rootView.findViewById(R.id.header);
     //   text_nodata = rootView.findViewById(R.id.text_nodata);

        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        if(SharedDB.isLoggedIn(getActivity()))
        {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID=values.get(SharedDB.PRIMARYID);
            BRANCHID=values.get(SharedDB.BRANCHID);
        }
        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(getActivity());
        if(isConnectedToInternet) {
            getOrdersList("", "");
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void getOrdersList(String fromdate, String todate) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DailyOrders> mService = mApiService.getPlacedOrderDetails(fromdate, todate, "",BRANCHID);

        mService.enqueue(new Callback<DailyOrders>() {
            @Override
            public void onResponse(@NonNull Call<DailyOrders> call, @NonNull Response<DailyOrders> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    DailyOrders mOrderObject = response.body();
                    assert mOrderObject != null;
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        List<DailyOrdersList> ordersList = mOrderObject.getDailyOrdersList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {

                                recyclerview.setVisibility(View.VISIBLE);
                                header.setVisibility(View.VISIBLE);
                                //text_nodata.setVisibility(View.GONE);
                                imgrel.setVisibility(View.GONE);

                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String order_id = ordersList.get(i).getOrder_id();
                                    String orderedon = ordersList.get(i).getOrderedon();
                                    String store = ordersList.get(i).getStore();
                                    String order_value = ordersList.get(i).getOrder_value();
                                    hashMap.put("id", id);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("orderedon", orderedon);
                                    hashMap.put("store", store);
                                    hashMap.put("order_value", order_value);
                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
                            } else {
                                recyclerview.setVisibility(View.GONE);
                                header.setVisibility(View.GONE);
                              //  text_nodata.setVisibility(View.VISIBLE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        recyclerview.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                       // text_nodata.setVisibility(View.VISIBLE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DailyOrders> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
//            text_nodata.setVisibility(View.GONE);
            imgrel.setVisibility(View.GONE);
            SellerPlaceOrdersRecyclerAdapter adapter = new SellerPlaceOrdersRecyclerAdapter(getActivity(), hashmapList);
            recyclerview.setAdapter(adapter);
        } else {
            recyclerview.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
         //   text_nodata.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    public class SellerPlaceOrdersRecyclerAdapter extends RecyclerView.Adapter<SellerPlaceOrdersRecyclerAdapter.MyViewHolder> {
        ArrayList<HashMap<String, String>> hashmapList;
        Context context;

        public SellerPlaceOrdersRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> hashmapList) {
            this.context = context;
            this.hashmapList = hashmapList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ordersadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String orderId = hashmapList.get(position).get("order_id");
            String orderedon = hashmapList.get(position).get("orderedon");
            holder.orderid.setText(orderId);
            holder.orderdate.setText(orderedon);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderid, orderdate;

            public MyViewHolder(View itemView) {
                super(itemView);
                orderid = itemView.findViewById(R.id.orderid);
                orderdate = itemView.findViewById(R.id.orderdate);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet)
                {
                    try{
                        String id = hashmapList.get(getAdapterPosition()).get("id");
                        String orderId = hashmapList.get(getAdapterPosition()).get("order_id");
                        Intent intent = new Intent(getActivity(), PackedOrderDetailsActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                    }catch(Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        getOrdersList("", "");
    }
}
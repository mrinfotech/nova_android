package com.mrnovacrm.b2b_delivery_dept;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DeliverStoresListDTO;
import com.mrnovacrm.model.SellerDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 20-03-2018.
 */

public class DeliveryOrderListFragment extends Fragment {
    private RecyclerView recyclerview;
    //TextView text_nodata;
    RelativeLayout headerrel;
    RelativeLayout imgrel;
    ImageView imageview;
    private String PRIMARYID;
    private String BRANCHID="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_orders_list, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);
       // text_nodata = rootView.findViewById(R.id.text_nodata);

        headerrel=rootView.findViewById(R.id.headerrel);
        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        return rootView;
    }

    private void getDeliveryStoreOrderList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<SellerDTO> mService = mApiService.getDealers(BRANCHID);
        mService.enqueue(new Callback<SellerDTO>() {
            @Override
            public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
                dialog.dismiss();
                try {
                    Log.e("response",""+response);
                    SellerDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<DeliverStoresListDTO> sellersList = mOrderObject.getStoresListDTO();
                        Log.e("sellersList", "" + sellersList.size());
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {

                                headerrel.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.VISIBLE);
                              //  text_nodata.setVisibility(View.GONE);
                                imgrel.setVisibility(View.GONE);

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < sellersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = sellersList.get(i).getId();
                                    String order_id = sellersList.get(i).getOrder_id();
                                    Log.e("OrderId"," :" +order_id);
                                    String name = sellersList.get(i).getName();
                                    String mobile = sellersList.get(i).getMobile();
                                    String latitude = sellersList.get(i).getLatitude();
                                    String longitude = sellersList.get(i).getLongitude();
                                    String address = sellersList.get(i).getAddress();
                                    hashMap.put("id", id);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("name", name);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("latitude", latitude);
                                    hashMap.put("longitude", longitude);
                                    hashMap.put("address", address);
                                    hashmapList.add(hashMap);
                                }
                                showOrdersList(hashmapList);
                            } else {
                                headerrel.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.GONE);
                          //      text_nodata.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                        headerrel.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.GONE);
                        imgrel.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SellerDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);

            DeliveryOrderListAdapter adapter = new DeliveryOrderListAdapter(getActivity(), hashmapList);
            recyclerview.setAdapter(adapter);
        } else {
            imgrel.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }

    public class DeliveryOrderListAdapter extends RecyclerView.Adapter<DeliveryOrderListAdapter.MyViewHolder> {
        Context context;
        ArrayList<HashMap<String, String>> hashmapList;

        public DeliveryOrderListAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList) {
            this.context = context;
            hashmapList = mhashmapList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_deliveredorders_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String order_id = hashmapList.get(position).get("order_id");
            holder.orderid.setText(order_id);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderid;

            public MyViewHolder(View itemView) {
                super(itemView);
                orderid = itemView.findViewById(R.id.orderid);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                String id = hashmapList.get(getAdapterPosition()).get("id");
                String order_id = hashmapList.get(getAdapterPosition()).get("order_id");
                String latitude = hashmapList.get(getAdapterPosition()).get("latitude");
                String longitude = hashmapList.get(getAdapterPosition()).get("longitude");
                String address = hashmapList.get(getAdapterPosition()).get("address");
                Intent intent = new Intent(getActivity(), DeliveryOrderListDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", address);
                startActivity(intent);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getDeliveryStoreOrderList();
    }
}

package com.mrnovacrm.b2b_dispatch_dept;

//public class DispatchRejectedHistoryDetailsActivity {
//}


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DispatchRejectedHistoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    String ID,fromval,orderId;
    RecyclerView recyclerView;
    private String LATITUDE, LONGITUDE, ADDRESS, ORDERID;
    LinearLayout order_linear, item_linear;
    // RelativeLayout bottom;
    TextView text_nodata, order_id;
    private Dialog rejectalertDialog;
    private String REASON="";
    EditText proof_path;
    private String base64;
    private String selectedFilePath;
    private TransparentProgressDialog dialog;
    private static final int FileSelectId = 3;
    private int STORAGE_PERMISSION_CODE = 23;
    private HashMap<String, String> values;
    private String PRIMARYID;
    private String SHORTFORM;
    String STATUSVALUE;
    public static Activity mainfinish;
    LinearLayout remarkslinear;
    TextView remarks_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
//        setTheme(R.style.AppTheme);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTitle("Order Details");
//        Bundle bundle = getIntent().getExtras();
//        ID = bundle.getString("id");
//        fromval = bundle.getString("fromval");
//        orderId = bundle.getString("orderId");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setContentView(R.layout.activity_delivery_order_details);
//        setTitle("Order Details");

        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("id");
        fromval = bundle.getString("fromval");
        orderId = bundle.getString("orderId");


        setContentView(R.layout.layout_dispatchpackhistorydetailsactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_linear = findViewById(R.id.order_linear);
        item_linear = findViewById(R.id.item_linear);
        text_nodata = findViewById(R.id.text_nodata);
        order_id = findViewById(R.id.order_id);
        TextView qtytxt = findViewById(R.id.qtytxt);
        qtytxt.setText("Packed Qty");


        TextView packqtytxt = findViewById(R.id.packqtytxt);

        order_id.setText(orderId);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        if(fromval.equals("partialrejected"))
        {
            STATUSVALUE="1";
        }else{
            STATUSVALUE="2";
        }
        packqtytxt.setText("Rejected Qty");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        remarkslinear=findViewById(R.id.remarkslinear);
        remarks_text=findViewById(R.id.remarks_text);



        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(DispatchRejectedHistoryDetailsActivity.this);
        if(isConnectedToInternet)
        {
            try{
                getOrderListDetails();
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DispatchRejectedHistoryDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getDispatchRejectedHistoryDetails(ID,STATUSVALUE);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    //   Log.e("mOrderObject", "" + mOrderObject);
                    String status = mOrderObject.getStatus();
                    String REMARKS=mOrderObject.getRemarks();

                    if(!REMARKS.equals(""))
                    {
                        remarkslinear.setVisibility(View.VISIBLE);
                        remarks_text.setText(REMARKS);
                    }else{
                        remarkslinear.setVisibility(View.GONE);
                    }



                    if (Integer.parseInt(status) == 1) {
                        List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String id = productsList.get(i).getId();
                                    String mrp = productsList.get(i).getMrp();
                                    String qty = productsList.get(i).getQty();
                                    String amount = productsList.get(i).getAmount();
                                    String total_cost = productsList.get(i).getTotal_cost();
                                    String itemname = productsList.get(i).getItemname();
                                    String pack_type = productsList.get(i).getPack_type();
                                    String packed_qty = productsList.get(i).getPacked_qty();
                                    String balance_qty = productsList.get(i).getBalance_qty();
                                    String action_status = productsList.get(i).getAction_status();
                                    String discount = productsList.get(i).getDiscount();
                                    String total_price = productsList.get(i).getTotal_price();
                                    String margin = productsList.get(i).getMargin();
                                    String rem_qty = productsList.get(i).getRem_qty();
                                    String balance_descr = productsList.get(i).getBalance_descr();
                                    String rejection_point = productsList.get(i).getRejection_point();
                                    String delivered_qty = productsList.get(i).getDelivered_qty();


                                    hashMap.put("id", id);
                                    hashMap.put("mrp", mrp);
                                    hashMap.put("qty", qty);
                                    hashMap.put("amount", amount);
                                    hashMap.put("total_cost", total_cost);
                                    hashMap.put("itemname", itemname);
                                    hashMap.put("pack_type", pack_type);
                                    hashMap.put("packed_qty", packed_qty);
                                    hashMap.put("balance_qty", balance_qty);
                                    hashMap.put("action_status", action_status);
                                    hashMap.put("discount", discount);
                                    hashMap.put("total_price", total_price);
                                    hashMap.put("margin", margin);
                                    hashMap.put("rem_qty", rem_qty);
                                    hashMap.put("balance_descr", balance_descr);
                                    hashMap.put("rejection_point", rejection_point);
                                    hashMap.put("delivered_qty", delivered_qty);
                                    hashmapList.add(hashMap);
                                }
                                showDetailsList(hashmapList);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                // Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDetailsList(ArrayList<HashMap<String,String>> hashMapArrayList) {
        DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getApplicationContext(),hashMapArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String,String>> hashMapArrayList;

        public DeliveryOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String,String>> hashMapArrayList) {
            this.mContext = mContext;
            this.hashMapArrayList=hashMapArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_dispatchpackhistorydetailsadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String itemname=hashMapArrayList.get(position).get("itemname");
            String pack_type=hashMapArrayList.get(position).get("pack_type");
            String packed_qty=hashMapArrayList.get(position).get("packed_qty");
            String qty=hashMapArrayList.get(position).get("qty");
            String balance_descr=hashMapArrayList.get(position).get("balance_descr");
            String rejection_point=hashMapArrayList.get(position).get("rejection_point");
            String balance_qty=hashMapArrayList.get(position).get("balance_qty");
            String delivered_qty=hashMapArrayList.get(position).get("delivered_qty");


            holder.itemnametxt.setText(itemname);
            holder.packtypetxt.setText(pack_type);
//            holder.qtytxt.setText(qty);
//            holder.packqtytxt.setText(packed_qty);

            holder.qtytxt.setText(packed_qty);
            holder.packqtytxt.setText(delivered_qty);

//            if(!fromval.equals("packed"))
//            {
                holder.rejectionlinear.setVisibility(View.VISIBLE);
                holder.descriptiontxt.setText(balance_descr);
                holder.reasontxt.setText(rejection_point);

              //  if(balance_qty.equals(qty))
               // {
               //     holder.desclinear.setVisibility(View.VISIBLE);
               // }else{
              //      holder.desclinear.setVisibility(View.GONE);
             //   }
             //   holder.packqtytxt.setText(balance_qty);

//
//            }else{
             //   holder.rejectionlinear.setVisibility(View.GONE);
             //   holder.packqtytxt.setText(packed_qty);
            //}
        }

        @Override
        public int getItemCount() {
            return hashMapArrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView itemnametxt, packtypetxt,qtytxt,packqtytxt,descriptiontxt,reasontxt;
            LinearLayout rejectionlinear,desclinear;

            MyViewHolder(View view) {
                super(view);
                itemnametxt = view.findViewById(R.id.itemnametxt);
                packtypetxt = view.findViewById(R.id.packtypetxt);
                qtytxt = view.findViewById(R.id.qtytxt);
                packqtytxt = view.findViewById(R.id.packqtytxt);
                rejectionlinear = view.findViewById(R.id.rejectionlinear);
                reasontxt = view.findViewById(R.id.reasontxt);
                descriptiontxt = view.findViewById(R.id.descriptiontxt);
                desclinear = view.findViewById(R.id.desclinear);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }
    }
}
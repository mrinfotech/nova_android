package com.mrnovacrm.b2b_delivery_dept;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GPSTracker;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DelItemsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 6/9/2018.
 */

public class DeliveryOrderProcessDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> values;
    private String PRIMARYID,droute_id,storeid;
    RecyclerView recyclerView;
    private String pkey;
    private RelativeLayout btmrel;
   Button finishtxt,totalcostbtn;
   int TOTAL_PROCESSED=0;
   int TOTAL_RECORDS=0;
   GlobalShare globalShare;
   Context mContext;
    public static final int RequestPermissionCode = 1;
    public static final int REQUEST_LOCATION = 10001;
    private static final int LocationRequestCode = 1;
    private GPSTracker gps;
    private double locationLatitude1;
    private double locationLongitue1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Order Details");

        mContext=this;

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_deliveryprocessdetailsactivity);

        globalShare=(GlobalShare)getApplicationContext();

        Bundle bundle=getIntent().getExtras();
        String storename=bundle.getString("storename");
        pkey=bundle.getString("pkey");
        droute_id=bundle.getString("droute_id");
        storeid=bundle.getString("storeid");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        TextView storenametxt=findViewById(R.id.storenametxt);
        storenametxt.setText(storename);

        btmrel=findViewById(R.id.btmrel);
        totalcostbtn=findViewById(R.id.totalcostbtn);
        finishtxt=findViewById(R.id.finishtxt);

        finishtxt.setOnClickListener(DeliveryOrderProcessDetailsActivity.this);

        requestPermission();
        firstGPSPOINT();
        getDeliveryOrderDetailsList();
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

    private void getDeliveryOrderDetailsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliveryOrderProcessDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getDeliveryBoyProcessDetails(PRIMARYID,pkey);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Order mOrderObject = response.body();
                String status = mOrderObject.getStatus();
                try {
                    if (Integer.parseInt(status) == 1) {


                        List<OrdersListDTO> orderListDTOS = mOrderObject.getOrdersListDTOS();
                        if(orderListDTOS!=null)
                        {
                            if(orderListDTOS.size()>0)
                            {
                                btmrel.setVisibility(View.VISIBLE);
                                String totalCost=mOrderObject.getTotal_cost();
                                String total_processed=mOrderObject.getTotal_processed();
                                String total_records=mOrderObject.getTotal_records();
                                try{
                                    TOTAL_PROCESSED=Integer.parseInt(total_processed);
                                    TOTAL_RECORDS=Integer.parseInt(total_records);
                                }catch (Exception e)
                                {
                                }

                                totalcostbtn.setText("Total Cost : Rs."+totalCost);

                                ArrayList<HashMap<String,String>> recordshashmaplist=new ArrayList<HashMap<String,String>>();
                                HashMap<String,ArrayList<HashMap<String,String>>> itemsDataHashMap=new HashMap<String,ArrayList<HashMap<String,String>>>();
                                HashMap<String,String[]> item_statusHashMap=new HashMap<String,String[]>();
                                for(int i=0;i<orderListDTOS.size();i++)
                                {
                                    HashMap<String,String> recordshashMap=new HashMap<>();
                                    String id=orderListDTOS.get(i).getId();
                                    String name=orderListDTOS.get(i).getName();
                                    String order_id=orderListDTOS.get(i).getOrder_id();
                                    String order_value=orderListDTOS.get(i).getOrder_value();

                                    recordshashMap.put("id",id);
                                    recordshashMap.put("name",name);
                                    recordshashMap.put("order_id",order_id);
                                    recordshashMap.put("order_value",order_value);
                                    recordshashmaplist.add(recordshashMap);

                                     List<DelItemsDTO> delItemsList = orderListDTOS.get(i).getDelItemsDTOList();

                                     ArrayList<HashMap<String,String>> itemsHashMapList=new ArrayList<HashMap<String,String>>();
                                     if(delItemsList!=null)
                                     {
                                         if(delItemsList.size()>0)
                                         {
                                            String[] item_statusValueArray = new String[delItemsList.size()];
                                             for(int j=0;j<delItemsList.size();j++)
                                             {
                                                 HashMap<String,String> itemsHashMap=new HashMap<String,String>();
                                                 String pb_id=delItemsList.get(j).getPb_id();
                                                 String bag_name=delItemsList.get(j).getBag_name();
                                                 String barcode=delItemsList.get(j).getBarcode();
                                                 String status1=delItemsList.get(j).getStatus();
                                                 String barcode_img=delItemsList.get(j).getBarcode_img();

                                                 itemsHashMap.put("pb_id",pb_id);
                                                 itemsHashMap.put("bag_name",bag_name);
                                                 itemsHashMap.put("barcode",barcode);
                                                 itemsHashMap.put("status1",status1);
                                                 itemsHashMap.put("barcode_img",barcode_img);

                                                 itemsHashMapList.add(itemsHashMap);
                                                 item_statusValueArray[j]=status1;
                                             }

                                             item_statusHashMap.put(id,item_statusValueArray);
                                         }
                                     }
                                    itemsDataHashMap.put(id,itemsHashMapList);
                                }
                                detailsList(recordshashmaplist,itemsDataHashMap,item_statusHashMap);
                            }else{
                                btmrel.setVisibility(View.GONE);
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
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void detailsList(ArrayList<HashMap<String,String>> recordshashmaplist,HashMap<String,
            ArrayList<HashMap<String,String>>> itemsDataHashMap,HashMap<String,String[]> item_statusHashMap)
    {
        DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getApplicationContext(),
                recordshashmaplist,itemsDataHashMap,item_statusHashMap);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        if(TOTAL_RECORDS!=0 & TOTAL_PROCESSED!=0)
        {
            if(TOTAL_RECORDS==TOTAL_PROCESSED)
            {
                processWithRetrofit(droute_id,storeid,"3");
            }else{
                Toast.makeText(getApplicationContext(),"Plase process all items",Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(getApplicationContext(),"Plase process all items",Toast.LENGTH_SHORT).show();
        }
    }

    private void processWithRetrofit(String droute_id, String storeid, String storestatus) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.setDeliveryRouteProcess(droute_id,String.valueOf(locationLatitude1),
                String.valueOf(locationLongitue1),
                storestatus,storeid,pkey);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response",""+response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        globalShare.setDeliverymenuselectpos("3");
                        if(DeliveryMenuScreenActivity.mainfinish!=null)
                        {
                            DeliveryMenuScreenActivity.mainfinish.finish();
                        }
                        Intent intent=new Intent(getApplicationContext(),DeliveryMenuScreenActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;

        ArrayList<HashMap<String,String>> recordshashmaplist;
        HashMap<String,ArrayList<HashMap<String,String>>> itemsDataHashMap;
        HashMap<String,String[]> item_statusHashMap;

        public DeliveryOrderDetailsAdapter(Context mContext,ArrayList<HashMap<String,String>> recordshashmaplist,
                                           HashMap<String,ArrayList<HashMap<String,String>>> itemsDataHashMap,
                                           HashMap<String,String[]> item_statusHashMap) {
            this.mContext = mContext;
            this.recordshashmaplist=recordshashmaplist;
            this.itemsDataHashMap=itemsDataHashMap;
            this.item_statusHashMap=item_statusHashMap;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_deliverorderprocessdetailsadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            String id=recordshashmaplist.get(position).get("id");
            String name=recordshashmaplist.get(position).get("name");
            String order_id=recordshashmaplist.get(position).get("order_id");
            String order_value=recordshashmaplist.get(position).get("order_value");

            holder.OrderID.setText(order_id);
            holder.ordercosttxt.setText(order_value);

            if(itemsDataHashMap.containsKey(id))
            {
                ArrayList<HashMap<String, String>> itemsDataHashMapList = itemsDataHashMap.get(id);
                if(itemsDataHashMapList!=null)
                {
                    if(itemsDataHashMapList.size()>0)
                    {
                        String IDVAL=recordshashmaplist.get(position).get("id");
                        if(item_statusHashMap.containsKey(id))
                        {
                            String[] item_statusValueArray=item_statusHashMap.get(id);
                            holder.ordersrecyclerview.setHasFixedSize(true);
                            holder.ordersrecyclerview.setVisibility(View.VISIBLE);
                            holder.ordersrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                            OrderDetailsAdapter adapter = new OrderDetailsAdapter(mContext,itemsDataHashMapList,
                                    item_statusValueArray,IDVAL);
                            holder.ordersrecyclerview.setAdapter(adapter);
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return recordshashmaplist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            RecyclerView ordersrecyclerview;
            TextView OrderID,ordercosttxt;

            MyViewHolder(View view) {
                super(view);

              OrderID=view.findViewById(R.id.OrderID);
              ordercosttxt=view.findViewById(R.id.ordercosttxt);

             ordersrecyclerview=view.findViewById(R.id.ordersrecyclerview);
             view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {

            }
        }

        public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
            private Context mContext;
            int mExpandedPosition = -1;
            ArrayList<HashMap<String, String>> hashmap_List;
            String latitude;
            String longitude;
            String address;
            ArrayList<HashMap<String, String>> itemsDataHashMapList;
            String[] item_statusValueArray;
            String idval;

            public OrderDetailsAdapter(Context mContext,ArrayList<HashMap<String, String>> itemsDataHashMapList,
                                       String[] item_statusValueArray,String idval) {
                this.mContext = mContext;
                this.itemsDataHashMapList=itemsDataHashMapList;
                this.item_statusValueArray=item_statusValueArray;
                this.idval=idval;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.deliveritemprocessadapter, parent, false);
                return new MyViewHolder(itemView);
            }
            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {

                String pb_id=itemsDataHashMapList.get(position).get("pb_id");
                String bag_name=itemsDataHashMapList.get(position).get("bag_name");
                String barcode=itemsDataHashMapList.get(position).get("barcode");
                String status1=itemsDataHashMapList.get(position).get("status1");
                holder.item_name.setText(bag_name);

                holder.indexReference=position;

                if(item_statusValueArray[holder.indexReference].equals("0"))
                {
                    holder.deliverlinear.setVisibility(View.GONE);
                    holder.deliverlinear.setVisibility(View.VISIBLE);
                    holder.statustxt.setVisibility(View.GONE);
                }else if(item_statusValueArray[holder.indexReference].equals("1")){
                    holder.statustxt.setText("Delivered");
                    holder.statustxt.setVisibility(View.VISIBLE);
                    holder.deliverlinear.setVisibility(View.GONE);
                    holder.deliverlinear.setVisibility(View.GONE);
                }

                    holder.btn_deliver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String pb_id=itemsDataHashMapList.get(position).get("pb_id");
                    sendPickedItem(pb_id,"1",idval,PRIMARYID,
                                "","",position,holder,item_statusValueArray);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return itemsDataHashMapList.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                ImageView store_location;
                TextView statustxt,item_name;
                LinearLayout deliverlinear;
                Button btn_deliver;
                int indexReference;
                MyViewHolder(View view) {
                    super(view);

                    statustxt=view.findViewById(R.id.statustxt);
                    deliverlinear=view.findViewById(R.id.deliverlinear);
                    item_name=view.findViewById(R.id.item_name);

                    btn_deliver=view.findViewById(R.id.btn_deliver);
                    view.setOnClickListener(this);
                }
                @Override
                public void onClick(View view) {

                }
            }
            private void sendPickedItem(final String pb_id,final String action,final String orderid,final String USER,
                                        final String description,final String reason,
                                        final int position,final MyViewHolder holder,final String[] item_statusValueArray) {
                final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliveryOrderProcessDetailsActivity.this);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<Login> mService=null;
              //  Call<Login> mService = mApiService.deliveryboydeliver_orderstatus(pb_id,action,orderid,USER,"","");

                mService.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                        Login mLoginObject = response.body();
                        Log.e("response", " :" + response);
                        dialog.dismiss();
                        try {
                            String status = mLoginObject.getStatus();
                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), mLoginObject.getMessage(), Toast.LENGTH_SHORT).show();
                                item_statusValueArray[position]="1";
                                holder.statustxt.setText("Delivered");
                                holder.statustxt.setVisibility(View.VISIBLE);
                                holder.deliverlinear.setVisibility(View.GONE);
                                holder.deliverlinear.setVisibility(View.GONE);

                                try{
                                    TOTAL_PROCESSED=Integer.parseInt(mLoginObject.getTotal_processed());
                                    TOTAL_RECORDS=Integer.parseInt(mLoginObject.getTotal_records());
                                }catch (Exception e)
                                {
                                }
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                        Log.e("Throwable", " :" + t.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestPermissionCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("onActivityResult", "Called");
//                progressbar.setVisibility(View.VISIBLE);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void firstGPSPOINT()
    {
        mContext = DeliveryOrderProcessDetailsActivity.this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            //	Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, this);
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                locationLatitude1=latitude;
                locationLongitue1=longitude;
                try {
                    // Getting address from found locations.
                    Geocoder geocoder;

                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    // you can get more details other than this . like country code,
                    // state code, etc.
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps = new GPSTracker(mContext,this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                    //Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
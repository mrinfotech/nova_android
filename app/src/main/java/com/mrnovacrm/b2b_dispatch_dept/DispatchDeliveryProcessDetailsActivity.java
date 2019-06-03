package com.mrnovacrm.b2b_dispatch_dept;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.activity.FilePath;
import com.mrnovacrm.b2b_dealer.TrackDeliveryDetailsActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GPSTracker;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DelItemsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.SellersOrdersListDTO;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;
/**
 * Created by prasad on 6/9/2018.
 */

public class DispatchDeliveryProcessDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private HashMap<String, String> values;
    private String PRIMARYID;
    RecyclerView recyclerView;
    String dealername;
    //private RelativeLayout btmrel;
  //  Button finishtxt,totalcostbtn;
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
    Dialog rejectalertDialog;
    private String REASON;
    private   EditText proof_path;
    private Dialog alertDialog;
    View layout;
    private TransparentProgressDialog dialog;
    private TextView submittxt, txt_nodata;
    EditText reason_edtxt;
    private ImageView closeicon;
    private String base64;
    private int STORAGE_PERMISSION_CODE = 23;
    private String selectedFilePath;
    private static final int FileSelectId = 3;
    private String ID;
    private TextView storenametxt;
    RelativeLayout btmrelative;
    public static Activity mainfinish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        mainfinish=this;
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
        ID=bundle.getString("id");
        dealername=bundle.getString("orderId");
//        Log.e("ID",ID);
//        Log.e("dealername",dealername);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        storenametxt=findViewById(R.id.storenametxt);
        btmrelative=findViewById(R.id.btmrelative);
     //   storenametxt.setText(dealername);

    //    btmrel=findViewById(R.id.btmrel);
     //   totalcostbtn=findViewById(R.id.totalcostbtn);
     //   finishtxt=findViewById(R.id.finishtxt);
        btmrelative.setOnClickListener(DispatchDeliveryProcessDetailsActivity.this);
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(DispatchDeliveryProcessDetailsActivity.this);
        if(isConnectedToInternet)
        {
            try{
                getDeliveryOrderDetailsList();
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                recallmethod();
            // globalShare.setDispatchmenuselectpos("3");
//            if(DispatchMenuScreenActivity.mainfinish!=null)
//            {
//                DispatchMenuScreenActivity.mainfinish.finish();
//                System.exit(0);
//                Intent intent=new Intent(getApplicationContext(),DispatchMenuScreenActivity.class);
//                startActivity(intent);
//            }else{
//                Intent intent=new Intent(getApplicationContext(),DispatchMenuScreenActivity.class);
//                startActivity(intent);
//            }
       //     finish();
              return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        recallmethod();
        super.onBackPressed();
    }

    public void recallmethod()
    {
//        if(DispatchMenuScreenActivity.mainfinish!=null)
//        {
//            DispatchMenuScreenActivity.mainfinish.finish();
//        }
//        globalShare.setDispatchmenuselectpos("3");
//        Intent intent=new Intent(getApplicationContext(),DispatchMenuScreenActivity.class);
//        startActivity(intent);
//        finish();

        if(DispatchMenuScreenActivity.mainfinish!=null)
        {
            DispatchMenuScreenActivity.mainfinish.finish();
        }
        globalShare.setDispatchmenuselectpos("3");
        Intent intent=new Intent(getApplicationContext(),DispatchMenuScreenActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDeliveryOrderDetailsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DispatchDeliveryProcessDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getDeliveryBoyProcessDetails(PRIMARYID,ID);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {

                        List<OrdersListDTO> orderListDTOS = mOrderObject.getOrdersListDTOS();
                        if(orderListDTOS!=null)
                        {
                            if(orderListDTOS.size()>0)
                            {
                                //btmrel.setVisibility(View.VISIBLE);
                                String totalCost=mOrderObject.getTotal_cost();
                                String total_processed=mOrderObject.getTotal_processed();
                                String total_records=mOrderObject.getTotal_records();
                                try{
                                    TOTAL_PROCESSED=Integer.parseInt(total_processed);
                                    TOTAL_RECORDS=Integer.parseInt(total_records);
                                }catch (Exception e)
                                {
                                }
                                //totalcostbtn.setText("Total Cost : Rs."+totalCost);
                                ArrayList<HashMap<String,String>> recordshashmaplist=new ArrayList<HashMap<String,String>>();
                                HashMap<String,ArrayList<HashMap<String,String>>> itemsDataHashMap=new HashMap<String,ArrayList<HashMap<String,String>>>();
                                HashMap<String,String[]> item_statusHashMap=new HashMap<String,String[]>();
                                HashMap<String,String[]> item_balqtyHashMap=new HashMap<String,String[]>();
                                HashMap<String,String[]> item_packqtyHashMap=new HashMap<String,String[]>();

                                for(int i=0;i<orderListDTOS.size();i++)
                                {
                                    HashMap<String,String> recordshashMap=new HashMap<>();
                                    String id=orderListDTOS.get(i).getId();
                                    String name=orderListDTOS.get(i).getName();
                                    String order_id=orderListDTOS.get(i).getOrder_id();
                                    String order_value=orderListDTOS.get(i).getOrder_value();
                                    String remarks=orderListDTOS.get(i).getRemarks();
                                    storenametxt.setText(name);

                                    recordshashMap.put("id",id);
                                    recordshashMap.put("name",name);
                                    recordshashMap.put("order_id",order_id);
                                    recordshashMap.put("order_value",order_value);
                                    recordshashMap.put("remarks",remarks);
                                    recordshashmaplist.add(recordshashMap);

                                    List<DelItemsDTO> delItemsList = orderListDTOS.get(i).getDelItemsDTOList();

                                    ArrayList<HashMap<String,String>> itemsHashMapList=new ArrayList<HashMap<String,String>>();
                                    if(delItemsList!=null)
                                    {
                                        if(delItemsList.size()>0)
                                        {
                                            String[] item_statusValueArray = new String[delItemsList.size()];
                                            String[] item_balqtyValueArray = new String[delItemsList.size()];
                                            String[] item_packqtyValueArray = new String[delItemsList.size()];

                                            for(int j=0;j<delItemsList.size();j++)
                                            {
                                                HashMap<String,String> itemsHashMap=new HashMap<String,String>();
                                                String id1=delItemsList.get(j).getId();
                                                String order_item_id=delItemsList.get(j).getOrder_item_id();
                                                String packed_qty=delItemsList.get(j).getPacked_qty();
                                                String status2=delItemsList.get(j).getStatus();
                                                String balance_qty=delItemsList.get(j).getBalance_qty();
                                                String itemname=delItemsList.get(j).getItemname();
                                                String pack_type=delItemsList.get(j).getPack_type();

                                                itemsHashMap.put("id",id1);
                                                itemsHashMap.put("order_item_id",order_item_id);
                                                itemsHashMap.put("packed_qty",packed_qty);
                                                itemsHashMap.put("status",status2);
                                                itemsHashMap.put("balance_qty",balance_qty);
                                                itemsHashMap.put("itemname",itemname);
                                                itemsHashMap.put("pack_type",pack_type);

                                                if (status2.equals("1") || status2.equals("2")) {
                                                    item_packqtyValueArray[j] =balance_qty;
                                                }else{
                                                    item_packqtyValueArray[j] =packed_qty;
                                                }
                                                itemsHashMapList.add(itemsHashMap);
                                                item_statusValueArray[j]=status2;
                                                item_balqtyValueArray[j]=balance_qty;
                                              //  item_packqtyValueArray[j]=packed_qty;
                                            }
                                            item_statusHashMap.put(id,item_statusValueArray);
                                            item_balqtyHashMap.put(id,item_balqtyValueArray);
                                            item_packqtyHashMap.put(id,item_packqtyValueArray);
                                        }
                                    }
                                    itemsDataHashMap.put(id,itemsHashMapList);
                                }
                                detailsList(recordshashmaplist,itemsDataHashMap,item_statusHashMap,
                                        item_balqtyHashMap,item_packqtyHashMap);
                            }else{
                                //btmrel.setVisibility(View.GONE);
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
                //Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void detailsList(ArrayList<HashMap<String,String>> recordshashmaplist,HashMap<String,
            ArrayList<HashMap<String,String>>> itemsDataHashMap,HashMap<String,String[]> item_statusHashMap,
                            HashMap<String,String[]> item_balitemsHashMap,HashMap<String,String[]> item_packqtyitemsHashMap)
    {
        DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getApplicationContext(),
                recordshashmaplist,itemsDataHashMap,item_statusHashMap,item_balitemsHashMap,item_packqtyitemsHashMap);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btmrelative:
                Intent intent=new Intent(getApplicationContext(), TrackDeliveryDetailsActivity.class);
                intent.putExtra("id",ID);
                intent.putExtra("orderId",dealername);
                startActivity(intent);
                break;
             default:
                 break;
        }


//
//        if(TOTAL_RECORDS!=0 & TOTAL_PROCESSED!=0)
//        {
//            if(TOTAL_RECORDS==TOTAL_PROCESSED)
//            {
//             //   processWithRetrofit(droute_id,storeid,"3");
//            }else{
//                Toast.makeText(getApplicationContext(),"Plase process all items",Toast.LENGTH_SHORT).show();
//            }
//        }else
//        {
//            Toast.makeText(getApplicationContext(),"Plase process all items",Toast.LENGTH_SHORT).show();
//        }
    }

    private void processWithRetrofit(String droute_id, String storeid, String storestatus) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.setDeliveryRouteProcess(droute_id,String.valueOf(locationLatitude1),
                String.valueOf(locationLongitue1),
                storestatus,storeid,ID);
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
        HashMap<String,String[]> item_balitemsHashMap;
        HashMap<String,String[]> item_packqtyHashMap;

        public DeliveryOrderDetailsAdapter(Context mContext,ArrayList<HashMap<String,String>> recordshashmaplist,
                                           HashMap<String,ArrayList<HashMap<String,String>>> itemsDataHashMap,
                                           HashMap<String,String[]> item_statusHashMap,
                                           HashMap<String,String[]> item_balitemsHashMap,
                                           HashMap<String,String[]> item_packqtyHashMap
                                           ) {
            this.mContext = mContext;
            this.recordshashmaplist=recordshashmaplist;
            this.itemsDataHashMap=itemsDataHashMap;
            this.item_statusHashMap=item_statusHashMap;
            this.item_balitemsHashMap=item_balitemsHashMap;
            this.item_packqtyHashMap=item_packqtyHashMap;
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
            String remarks=recordshashmaplist.get(position).get("remarks");

            holder.OrderID.setText(order_id);
            holder.ordercosttxt.setText(order_value);
            if(!remarks.equals(""))
            {
                holder.remarkslinear.setVisibility(View.VISIBLE);
                holder.remarks_text.setText(remarks);
            }else{
                holder.remarkslinear.setVisibility(View.GONE);
            }

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
                            String[] item_balitemsArray=item_balitemsHashMap.get(id);
                            String[] item_packqtyitemsArray=item_packqtyHashMap.get(id);

                            holder.ordersrecyclerview.setHasFixedSize(true);
                            holder.ordersrecyclerview.setVisibility(View.VISIBLE);
                            holder.ordersrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                            OrderDetailsAdapter adapter = new OrderDetailsAdapter(mContext,itemsDataHashMapList,
                                    item_statusValueArray,IDVAL,item_balitemsArray,item_packqtyitemsArray);
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
            LinearLayout remarkslinear;
            TextView remarks_text;
            MyViewHolder(View view) {
                super(view);
                OrderID=view.findViewById(R.id.OrderID);
                ordercosttxt=view.findViewById(R.id.ordercosttxt);
                remarkslinear=view.findViewById(R.id.remarkslinear);
                remarks_text=view.findViewById(R.id.remarks_text);
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
            String[] item_balitemsArray;
            String[] item_packqtyitemsArray;

            public OrderDetailsAdapter(Context mContext,ArrayList<HashMap<String, String>> itemsDataHashMapList,
                                       String[] item_statusValueArray,String idva,
                                       String[] item_balitemsArray,
                                       String[] item_packqtyitemsArray
                                       ) {
                this.mContext = mContext;
                this.itemsDataHashMapList=itemsDataHashMapList;
                this.item_statusValueArray=item_statusValueArray;
                this.idval=idval;
                this.item_balitemsArray=item_balitemsArray;
                this.item_packqtyitemsArray=item_packqtyitemsArray;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.deliveritemprocessadapter, parent, false);
                return new MyViewHolder(itemView);
            }
            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {

                String id=itemsDataHashMapList.get(position).get("id");
                String order_item_id=itemsDataHashMapList.get(position).get("order_item_id");
                String itemname=itemsDataHashMapList.get(position).get("itemname");
                String pack_type=itemsDataHashMapList.get(position).get("pack_type");
                String balance_qty=itemsDataHashMapList.get(position).get("balance_qty");
                String packed_qty=itemsDataHashMapList.get(position).get("packed_qty");

                holder.indexReference=position;
                holder.packedtypetext.setText("Pack type : "+pack_type);
                holder.indexReference=position;

                if (item_statusValueArray[holder.indexReference].equals("1")
                        || item_statusValueArray[holder.indexReference].equals("2")) {
                    holder.pickerbtn.setVisibility(View.GONE);
                    holder.rejectbtn.setVisibility(View.GONE);
                    holder.picklinear.setVisibility(View.GONE);
                    holder.rejectlnr.setVisibility(View.GONE);

                    holder.qtyedittext.setFocusable(false);
                    holder.qtyedittext.setFocusableInTouchMode(false);
                    holder.qtyedittext.setClickable(false);

                    holder.text_picked.setVisibility(View.VISIBLE);
                    holder.pickedliner.setVisibility(View.VISIBLE);

                    if(item_statusValueArray[holder.indexReference].equals("1"))
                    {
                        holder.text_picked.setText("Delivered");
                    }else{
                        holder.text_picked.setText("Rejected");
                    }
                } else {
                    holder.picklinear.setVisibility(View.VISIBLE);
                    holder.pickerbtn.setVisibility(View.VISIBLE);
                    holder.rejectlnr.setVisibility(View.VISIBLE);
                    holder.rejectbtn.setVisibility(View.VISIBLE);
                    holder.text_picked.setVisibility(View.GONE);
                    holder.pickedliner.setVisibility(View.GONE);
                    holder.qtyedittext.setFocusable(true);
                    holder.qtyedittext.setFocusableInTouchMode(true);
                    holder.qtyedittext.setClickable(true);
                }

                if(item_statusValueArray[holder.indexReference].equals("2"))
                {
                    holder.qtyedittext.setText(packed_qty);
                }else{
                    holder.qtyedittext.setText(item_packqtyitemsArray[holder.indexReference]);
                }

                holder.itemnametxt.setText(itemname);
                holder.qtytxt.setText(packed_qty);

                holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final String itemId = itemsDataHashMapList.get(position).get("id");
                            String qty = itemsDataHashMapList.get(position).get("packed_qty");
                            String qtyedittext = holder.qtyedittext.getText().toString().trim();
                            int qtyval = Integer.parseInt(qty);
                            int editqtyval = Integer.parseInt(qtyedittext);
                            if(editqtyval>0) {
                                if (qtyval < editqtyval) {
                                    Toast.makeText(getApplicationContext(), "Pack qty exceeds the qty", Toast.LENGTH_SHORT).show();
                                } else {
                                    getRejectPoints(position, holder.qtyedittext.getText().toString().trim(), holder, item_statusValueArray, "fromreject", itemId);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                        }
                    }
                });

                holder.qtyedittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //  valueList[pos] = s.toString()+"~"+data.get(pos).get("attrId");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        item_packqtyitemsArray[holder.indexReference] = s.toString();
                    }
                });

                holder.pickerbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final String itemId = itemsDataHashMapList.get(position).get("id");
                            String qty = itemsDataHashMapList.get(position).get("packed_qty");
                            String qtyedittext = holder.qtyedittext.getText().toString().trim();
                            int qtyval=Integer.parseInt(qty);
                            int editqtyval=Integer.parseInt(qtyedittext);

                            if(editqtyval>0) {
                                if (qtyval == editqtyval) {
                                    sendPickedItem("1", ID, PRIMARYID, "picker", itemId,
                                            holder.qtyedittext.getText().toString(), position, holder, "", item_statusValueArray, "");
                                } else {
                                    if (qtyval < editqtyval) {
                                        Toast.makeText(getApplicationContext(), "Pack qty exceeds the qty", Toast.LENGTH_SHORT).show();
                                    } else {
//                                approvePopup(position, itemId, holder.qtyedittext.getText().toString(),
//                                        holder,item_statusValueArray);
                                        getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, item_statusValueArray, "frompicker", itemId);
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                        }
                    }
                });


//                if(item_statusValueArray[holder.indexReference].equals("0"))
//                {
//                    holder.deliverlinear.setVisibility(View.GONE);
//                    holder.deliverlinear.setVisibility(View.VISIBLE);
//                    holder.statustxt.setVisibility(View.GONE);
//                }else if(item_statusValueArray[holder.indexReference].equals("1")){
//                    holder.statustxt.setText("Delivered");
//                    holder.statustxt.setVisibility(View.VISIBLE);
//                    holder.deliverlinear.setVisibility(View.GONE);
//                    holder.deliverlinear.setVisibility(View.GONE);
//                }
//
//                holder.btn_deliver.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String pb_id=itemsDataHashMapList.get(position).get("pb_id");
//                        sendPickedItem(pb_id,"1",idval,PRIMARYID,
//                                "","",position,holder,item_statusValueArray);
//                    }
//                });
            }

            @Override
            public int getItemCount() {
                return itemsDataHashMapList.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//                ImageView store_location;
//                TextView statustxt,item_name;
//                LinearLayout deliverlinear;
//                Button btn_deliver;

                TextView itemnametxt, qtytxt, text_picked,text_rejected,packedtypetext;
                Button pickerbtn, rejectbtn;
                EditText qtyedittext;
                LinearLayout pickedliner,picklinear,rejectlnr;

                int indexReference;
                MyViewHolder(View view) {
                    super(view);

//                    statustxt=view.findViewById(R.id.statustxt);
//                    deliverlinear=view.findViewById(R.id.deliverlinear);
//                    item_name=view.findViewById(R.id.item_name);
//
//                    btn_deliver=view.findViewById(R.id.btn_deliver);

                    itemnametxt = view.findViewById(R.id.itemnametxt);
                    qtytxt = view.findViewById(R.id.qtytxt);
                    pickerbtn = view.findViewById(R.id.pickerbtn);
                    rejectbtn = view.findViewById(R.id.rejectbtn);
                    qtyedittext = view.findViewById(R.id.qtyedittext);
                    text_picked = view.findViewById(R.id.text_picked);
                    text_rejected = view.findViewById(R.id.text_rejected);
                    pickedliner = view.findViewById(R.id.pickedliner);
                    picklinear = view.findViewById(R.id.picklinear);
                    rejectlnr = view.findViewById(R.id.rejectlnr);

                    packedtypetext = view.findViewById(R.id.packedtypetext);

                    view.setOnClickListener(this);
                }
                @Override
                public void onClick(View view) {

                }
            }

            private void getRejectPoints(final int position, final String quantity, final MyViewHolder holder,
                                         final String[] item_statusValueArray,final String fromval,final String itemId) {
                final TransparentProgressDialog dialog = new TransparentProgressDialog(DispatchDeliveryProcessDetailsActivity.this);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<SellerDTO> mService = mApiService.getRejectPoints();

                mService.enqueue(new Callback<SellerDTO>() {
                    @Override
                    public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
                        dialog.dismiss();
                        Log.e("response", "" + response);

                        SellerDTO mOrderObject = response.body();
                        String status = mOrderObject.getStatus();
                        Log.e("ordersstatus", "" + status);
                        try {
                            if (Integer.parseInt(status) == 1) {
                                List<SellersOrdersListDTO> sellersList = mOrderObject.getSellersOrdersListDTO();
                                if (sellersList != null) {
                                    if (sellersList.size() > 0) {
                                        ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                        for (int i = 0; i < sellersList.size(); i++) {
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            String id = sellersList.get(i).getId();
                                            String rejectpoint = sellersList.get(i).getRej_point();
                                            hashMap.put("id", id);
                                            hashMap.put("rejectpoint", rejectpoint);

                                            Log.e("id", id);
                                            Log.e("rejectpoint", rejectpoint);
                                            hashmapList.add(hashMap);
                                        }
                                        if(fromval.equals("fromreject"))
                                        {
                                            rejectPopup(itemId,hashmapList, position, quantity, holder,item_statusValueArray);
                                        }else{
                                            approvePopup(position, itemId, holder.qtyedittext.getText().toString(),
                                                    holder,item_statusValueArray,hashmapList);
                                        }

                                    } else {
                                    }
                                }
                            } else {
                                String message = mOrderObject.getMessage();
                            }
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onFailure(Call<SellerDTO> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void rejectPopup(final String itemId,ArrayList<HashMap<String, String>> list, final int position,
                                    final String qty, final MyViewHolder holder,final String[] item_statusValueArray) {
                /** Used for Show Disclaimer Pop up screen */
                final EditText rejectorder_txt;

                rejectalertDialog = new Dialog(DispatchDeliveryProcessDetailsActivity.this);
                rejectalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                rejectalertDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent);
                LayoutInflater inflater = getLayoutInflater();
                View rejectlayout = inflater.inflate(R.layout.reject_popup, null);
                TextView reason_cancel = rejectlayout.findViewById(R.id.reason_reject);
                reason_cancel.setText("Reason for reject");
                rejectalertDialog.setContentView(rejectlayout);
                rejectalertDialog.setCancelable(true);
                if (!rejectalertDialog.isShowing()) {
                    rejectalertDialog.show();
                }
                rejectorder_txt = rejectlayout.findViewById(R.id.rejectorder_txt);

                RadioGroup rgp = rejectlayout.findViewById(R.id.radiogroup);
                RadioGroup.LayoutParams rprms;
                for (int i = 0; i < list.size(); i++) {

                    String id = list.get(i).get("id");
                    String rejectpoint = list.get(i).get("rejectpoint");
                    Log.e("id", " :" + id);
                    Log.e("rejectpoint", " :" + rejectpoint);
                    RadioButton rdbtn = new RadioButton(mContext);
                    rdbtn.setId(Integer.parseInt(id));
                    rdbtn.setText(rejectpoint);
                    rdbtn.setTextColor(Color.BLACK);
//                rdbtn.setTextColor(getResources().getColor(R.color.black));
                    rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rgp.addView(rdbtn, rprms);
                }
                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.e("CheckedId", " :" + checkedId);
                        REASON = String.valueOf(checkedId);
                    }
                });

                proof_path = rejectlayout.findViewById(R.id.proof_path);
                Button attach_proof = rejectlayout.findViewById(R.id.attach_proof);
                attach_proof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectImage();
                    }
                });

                ImageView closeicon = rejectlayout.findViewById(R.id.closeicon);
                closeicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rejectalertDialog.dismiss();
                    }
                });
                TextView submittxt = rejectlayout.findViewById(R.id.submittxt);
                submittxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedFilePath != null && rejectorder_txt.getText().toString() != null) {
                            dialog = new TransparentProgressDialog(DispatchDeliveryProcessDetailsActivity.this);
                            dialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //creating new thread to handle Http Operations
                                        rejectItem(itemId, qty, rejectorder_txt.getText().toString().trim(), holder,item_statusValueArray,position,REASON);
                                    } catch (OutOfMemoryError e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(getApplicationContext(), "Attachment is mandatory", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void approvePopup(final int position, final String itemId, String reasonval, final MyViewHolder holder,
                                     final String[] item_statusValueArray,ArrayList<HashMap<String, String>> hashmapList) {
                /** Used for Show Disclaimer Pop up screen */
                alertDialog = new Dialog(DispatchDeliveryProcessDetailsActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent);
                LayoutInflater inflater = getLayoutInflater();
                layout = inflater.inflate(R.layout.cancelorder_popup, null);
                TextView reason_cancel = layout.findViewById(R.id.reason_cancel);
                reason_cancel.setText("Reason for balance items");
                alertDialog.setContentView(layout);
                alertDialog.setCancelable(true);
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }

                RadioGroup rgp = layout.findViewById(R.id.radiogroup);
                RadioGroup.LayoutParams rprms;
                for (int i = 0; i < hashmapList.size(); i++) {

                    String id = hashmapList.get(i).get("id");
                    String rejectpoint = hashmapList.get(i).get("rejectpoint");
                    Log.e("id", " :" + id);
                    Log.e("rejectpoint", " :" + rejectpoint);
                    RadioButton rdbtn = new RadioButton(mContext);
                    rdbtn.setId(Integer.parseInt(id));
                    rdbtn.setText(rejectpoint);
                    rdbtn.setTextColor(Color.BLACK);
                    rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rgp.addView(rdbtn, rprms);
                }
                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.e("CheckedId", " :" + checkedId);
                        REASON = String.valueOf(checkedId);
                    }
                });

                submittxt = layout.findViewById(R.id.submittxt);
                reason_edtxt = layout.findViewById(R.id.cancelorder_txt);
                reasonval = reason_edtxt.getText().toString();
                closeicon = layout.findViewById(R.id.closeicon);
                closeicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });
                submittxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                        String desc = reason_edtxt.getText().toString();
                        sendPickedItem("1", ID, PRIMARYID, "picker", itemId,
                                holder.qtyedittext.getText().toString(), position, holder, desc,item_statusValueArray,REASON);
                    }
                });
            }


            int rejectItem(String item, String qty, String description, final MyViewHolder holder,
                           final String[] item_statusValueArray,final int position,String REASONVAL) {
                int serverResponseCode = 0;

                final HttpURLConnection connection;
                DataOutputStream dataOutputStream;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File selectedFile = new File(selectedFilePath);

                String[] parts = selectedFilePath.split("/");
                final String fileName = parts[parts.length - 1];

                if (!selectedFile.isFile()) {
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                        }
                    });
                    return 0;
                } else {

                    String SERVER_URL = SharedDB.URL + "deliveryboy/so_delivery";
                    try {
                        FileInputStream fileInputStream = new FileInputStream(selectedFile);
                        URL url = new URL(SERVER_URL);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);//Allow Inputs
                        connection.setDoOutput(true);//Allow Outputs
                        connection.setUseCaches(false);//Don't use a cached Copy
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Connection", "Keep-Alive");
                        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                        connection.setRequestProperty(
                                "Content-Type", "multipart/form-data;boundary=" + boundary);
                        connection.setRequestProperty("rej_img", selectedFilePath);

                        //creating new dataoutputstream
                        dataOutputStream = new DataOutputStream(connection.getOutputStream());
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"action\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes("2");
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    /*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"seller\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ID);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*/

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"order\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(ID);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(PRIMARYID);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(item);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"delivered_qty\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(qty);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reason\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(REASONVAL);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(description);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rej_img\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(selectedFilePath);
                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rej_img\";filename=\""
                                + fileName + "\"" + lineEnd);
                        dataOutputStream.writeBytes(lineEnd);

                        //returns no. of bytes present in fileInputStream
                        bytesAvailable = fileInputStream.available();
                        //selecting the buffer size as minimum of available bytes or 1 MB
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        //setting the buffer as byte array of size of bufferSize
                        buffer = new byte[bufferSize];

                        //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                        while (bytesRead > 0) {
                            try {
                                //write the bytes read from inputstream
                                dataOutputStream.write(buffer, 0, bufferSize);
                            } catch (OutOfMemoryError e) {
                                Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                            }
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        }

                        dataOutputStream.writeBytes(lineEnd);
                        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        try {
                            serverResponseCode = connection.getResponseCode();
                        } catch (OutOfMemoryError e) {
                            Toast.makeText(getApplicationContext(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                        }
                        String serverResponseMessage = connection.getResponseMessage();

                        Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);


                        //response code of 200 indicates the server status OK
                        if (serverResponseCode == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        BufferedReader rd = new BufferedReader(new InputStreamReader(connection
                                                .getInputStream()));
                                        String line;
                                        while ((line = rd.readLine()) != null) {
                                            sb.append(line);
                                        }
                                        rd.close();
                                    } catch (IOException ioex) {
                                    }
                                    Log.e("response", "" + sb.toString());

                                    String response = sb.toString();

                                    int statusValue = 0;
                                    String messageValue = "";
                                    try {
                                        JSONObject res_object = new JSONObject(response);

                                        if (res_object.has("status")) {
                                            statusValue = res_object.getInt("status");
                                            Log.e("statusValue", "" + statusValue);
                                        }
                                        if (res_object.has("message")) {
                                            messageValue = res_object.getString("message");
                                            Log.e("messageValue", messageValue);
                                        }

                                        if (statusValue == 1) {
                                            holder.pickerbtn.setVisibility(View.GONE);
                                            holder.rejectbtn.setVisibility(View.GONE);
                                            holder.picklinear.setVisibility(View.GONE);
                                            holder.rejectlnr.setVisibility(View.GONE);
                                            holder.qtyedittext.setFocusable(false);
                                            holder.qtyedittext.setFocusableInTouchMode(false);
                                            holder.qtyedittext.setClickable(false);

                                            holder.text_picked.setVisibility(View.VISIBLE);
                                            holder.pickedliner.setVisibility(View.VISIBLE);
                                            holder.text_picked.setText("Rejected");

                                            item_statusValueArray[position]="2";

                                            Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        //closing the input and output streams
                        fileInputStream.close();
                        dataOutputStream.flush();
                        dataOutputStream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    dialog.dismiss();
                    rejectalertDialog.dismiss();
                    return serverResponseCode;
                }
            }

            private void sendPickedItem(String action, String seller, String primary_key, String role,
                                        String item,final String qty, final int position, final MyViewHolder holder,
                                        String description,
                                        final String[] item_statusValueArray,String REASONVAL) {

                final TransparentProgressDialog dialog = new TransparentProgressDialog(DispatchDeliveryProcessDetailsActivity.this);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
//            Call<Login> mService = mApiService.picker_orderstatus(action, seller, primary_key, role,
//                    item, qty, description,REASONVAL,INVOICEIDVAL);


//                Call<Login> mService = mApiService.pack_dispatcherorderstatus(action,ID,item,qty,REASONVAL,description,
//                        PRIMARYID);

//                @Field("action") String action,@Field("user") String user,
//                @Field("id") String id,@Field("reason") String reason,
//                @Field("description") String description, @Field("order") String order

//                Log.e("action",action);
//                Log.e("PRIMARYID",PRIMARYID);
//                Log.e("item",item);
//                Log.e("REASONVAL",REASONVAL);
//                Log.e("description",description);
//                Log.e("ID",ID);
//                Log.e("qty",qty);


                Call<Login> mService = mApiService.deliveryboydeliver_orderstatus(action,PRIMARYID,item,
                        REASONVAL,description,ID,qty);
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

                                holder.pickerbtn.setVisibility(View.GONE);
                                holder.rejectbtn.setVisibility(View.GONE);
                                holder.picklinear.setVisibility(View.GONE);
                                holder.rejectlnr.setVisibility(View.GONE);
                                holder.qtyedittext.setFocusable(false);
                                holder.qtyedittext.setFocusableInTouchMode(false);
                                holder.qtyedittext.setClickable(false);

                                holder.text_picked.setVisibility(View.VISIBLE);
                                holder.pickedliner.setVisibility(View.VISIBLE);
                                holder.text_picked.setText("Delivered");

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

    public void selectImage() {
//        if (isReadStorageAllowed()) {
//            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            pickIntent.setType("image/*");
//            startActivityForResult(pickIntent, 3);
//            return;
//        }
//        requestStoragePermission();
//        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("image/*");
//        startActivityForResult(pickIntent, 3);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if (resultCode == RESULT_OK && null != data) {
                    onCaptureImageResult(data);
                }
                break;

            case FileSelectId:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //no data present
                        return;
                    }
                    try {
                        Uri selectedFileUri = data.getData();
                        selectedFilePath = FilePath.getPath(getApplicationContext(), selectedFileUri);
                        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
                        proof_path.setText(filename);
                        Log.e(TAG, "Selected File Path:" + selectedFilePath);
                    } catch (Exception e) {

                    }
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
//        try {
//            Uri selectedFileUri = data.getData();
//            selectedFilePath = FilePath.getPath(getApplicationContext(), selectedFileUri);
//            String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
//            proof_path.setText(filename);
//            Log.e(TAG, "Selected File Path:" + selectedFilePath);
//        }catch (Exception e)
//        {
//
//        }

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byte_arr = bytes.toByteArray();
        base64 = Base64
                .encodeToString(byte_arr, Base64.DEFAULT);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        String strFileName = destination.getName();
        proof_path.setText(strFileName);
        selectedFilePath = destination.getPath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());

            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(DispatchDeliveryProcessDetailsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(DispatchDeliveryProcessDetailsActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(getActivity(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                // Toast.makeText(getActivity(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
}
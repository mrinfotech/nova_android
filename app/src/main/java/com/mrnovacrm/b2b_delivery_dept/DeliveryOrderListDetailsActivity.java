package com.mrnovacrm.b2b_delivery_dept;

import android.Manifest;
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
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
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
 * Created by android on 16-03-2018.
 */

public class DeliveryOrderListDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    Button map_location;
    private String ID, LATITUDE, LONGITUDE, ADDRESS, ORDERID;
    LinearLayout order_linear, item_linear;
    RelativeLayout bottom;
    TextView text_nodata, order_id, orderValue, address_txt;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery_order_details);
        map_location = findViewById(R.id.map_location);
        order_linear = findViewById(R.id.order_linear);
        item_linear = findViewById(R.id.item_linear);
        text_nodata = findViewById(R.id.text_nodata);
        order_id = findViewById(R.id.order_id);
        orderValue = findViewById(R.id.order_value);
        address_txt = findViewById(R.id.address_txt);
        bottom = findViewById(R.id.bottom);
        map_location.setOnClickListener(this);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        setTitle("Order Details");
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("id");
        LATITUDE = bundle.getString("latitude");
        ORDERID = bundle.getString("order_id");
        LONGITUDE = bundle.getString("longitude");
        ADDRESS = bundle.getString("address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address_txt.setText(ADDRESS);
        order_id.setText(ORDERID);

        TextView qtytxt=findViewById(R.id.qtytxt);
        qtytxt.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getDeliveryOrderDetailsList(ID);

        requestStoragePermission();
    }

    private void getDeliveryOrderDetailsList(String orderID) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliveryOrderListDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("OrderID"," :"+orderID);
        Call<Order> mService = mApiService.getDeliveryBoyPackDetails(orderID,"R");

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Order mOrderObject = response.body();
                String status = mOrderObject.getStatus();
                Log.e("ordersstatus", "" + status);
                try {
                    if (Integer.parseInt(status) == 1) {
                        String bag_count = mOrderObject.getBag_count();
                        String case_count = mOrderObject.getCase_count();
                        String tin_count = mOrderObject.getTin_count();
                        String order_id = mOrderObject.getOrder_id();
                        String order_value = mOrderObject.getOrder_value();
                        orderValue.setText(order_value);

                        List<OrdersListDTO> ordersListDTOS=mOrderObject.getOrdersListDTOS();

                        if(ordersListDTOS!=null)
                        {
                            if(ordersListDTOS.size()>0)
                            {
                                String[] item_statusValueArray = new String[ordersListDTOS.size()];
                                ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String,String>>();
                                for(int i=0;i<ordersListDTOS.size();i++)
                                {
                                    HashMap<String,String> hashMap=new HashMap<>();
                                    String pb_id=ordersListDTOS.get(i).getPb_id();
                                    String bag_name=ordersListDTOS.get(i).getBag_name();
                                    String statusval=ordersListDTOS.get(i).getStatus();
                                    hashMap.put("pb_id",pb_id);
                                    hashMap.put("bag_name",bag_name);
                                    hashMap.put("statusval",statusval);
                                    hashMapArrayList.add(hashMap);
                                    item_statusValueArray[i]=statusval;
                                }
                                showDetailsList(order_id, bag_count, case_count, tin_count, order_value, LATITUDE, LONGITUDE,hashMapArrayList,item_statusValueArray);
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

    public void showDetailsList(String orderid, String bagcount, String casecount, String tincount, String value,
                                String lat, String lon,ArrayList<HashMap<String,String>> hashMapArrayList,
                                String[] item_statusValueArray) {
//        text_nodata.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
//        order_linear.setVisibility(View.VISIBLE);
//        item_linear.setVisibility(View.VISIBLE);
//        bottom.setVisibility(View.VISIBLE);
        DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getApplicationContext(), orderid,
                bagcount, casecount, tincount, value, lat, lon,hashMapArrayList,item_statusValueArray);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_location:
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=17.741420,83.328133&daddr=17.548446,82.856670"));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<String> arrayList = new ArrayList<>();
        String morderId, mBagcount, mCasecount, mTincount, mValue, mLat, mLon;
        ArrayList<HashMap<String,String>> hashMapArrayList;
        String[] item_statusValueArray;

        public DeliveryOrderDetailsAdapter(Context mContext, String orderid, String bagcount, String casecount,
                                           String tincount, String value, String lat,
                                           String lon,ArrayList<HashMap<String,String>> hashMapArrayList,
                                           String[] item_statusValueArray) {
            this.mContext = mContext;
            arrayList.add("Bag");
            arrayList.add("Tin");
            arrayList.add("Case");
            morderId = orderid;
            mBagcount = bagcount;
            mCasecount = casecount;
            mTincount = tincount;
            mValue = value;
            mLat = lat;
            mLon = lon;
            this.hashMapArrayList=hashMapArrayList;
            this.item_statusValueArray=item_statusValueArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.delivery_orderdetails_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.btn_pick.setText("Receive");

            String bagname=hashMapArrayList.get(position).get("bag_name");
            String statusval=hashMapArrayList.get(position).get("statusval");
            holder.item_name.setText(bagname);
            holder.indexReference=position;

//            if (position == 0) {
//                holder.item_name.setText(arrayList.get(0));
//                holder.item_qty.setText(mBagcount);
//            } else if (position == 1) {
//                holder.item_name.setText(arrayList.get(1));
//                holder.item_qty.setText(mTincount);
//            } else if (position == 2) {
//                holder.item_name.setText(arrayList.get(2));
//                holder.item_qty.setText(mCasecount);
//            }

            if(item_statusValueArray[holder.indexReference].equals("0"))
            {
                holder.statustxt.setVisibility(View.GONE);
                holder.btn_pick.setVisibility(View.VISIBLE);
                holder.btn_reject.setVisibility(View.VISIBLE);
            }else if(item_statusValueArray[holder.indexReference].equals("1")){
                holder.statustxt.setText("Received");
                holder.statustxt.setVisibility(View.VISIBLE);
                holder.btn_pick.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
            }else if(item_statusValueArray[holder.indexReference].equals("2")){
                holder.statustxt.setText("Rejected");
                holder.statustxt.setVisibility(View.VISIBLE);
                holder.btn_pick.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
            }
            holder.item_qty.setVisibility(View.GONE);
            holder.deliverlinear.setVisibility(View.GONE);

            holder.btn_pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pb_id=hashMapArrayList.get(position).get("pb_id");
                    sendPickedItem(pb_id,"1",ID,PRIMARYID,
                            "","",
                            position,holder,item_statusValueArray);
                }
            });
            holder.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pb_id=hashMapArrayList.get(position).get("pb_id");
                    getRejectPoints(pb_id,"2",ID,PRIMARYID,
                    "","",
                    position,holder,item_statusValueArray);
                }
            });
        }

        @Override
        public int getItemCount() {
            return hashMapArrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView item_name, item_qty,statustxt;
            Button btn_pick, btn_reject;
            LinearLayout deliverlinear;
            int indexReference;

            MyViewHolder(View view) {
                super(view);
                item_name = view.findViewById(R.id.item_name);
                item_qty = view.findViewById(R.id.item_qty);
                btn_pick = view.findViewById(R.id.btn_pick);
                btn_reject = view.findViewById(R.id.btn_reject);
                statustxt = view.findViewById(R.id.statustxt);
                deliverlinear = view.findViewById(R.id.deliverlinear);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }

       private void sendPickedItem(final String pb_id,final String action,final String orderid,final String USER,
                                       final String description,final String reason,
                                       final int position,final MyViewHolder holder,final String[] item_statusValueArray) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliveryOrderListDetailsActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Login> mService = mApiService.deliveryboy_orderstatus(pb_id,action,orderid,USER,"","");
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
                            holder.statustxt.setText("Received");
                            holder.statustxt.setVisibility(View.VISIBLE);
                            holder.btn_pick.setVisibility(View.GONE);
                            holder.btn_reject.setVisibility(View.GONE);
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
        //(pb_id,"1",ID,PRIMARYID,"","",position,holder,item_statusValueArray);
       // private void getRejectPoints(final int position,final MyViewHolder holder,final String itemId,final String[] item_statusValueArray) {

        private void getRejectPoints(final String pb_id,final String action,final String orderid,final String USER,
                                     final String description,final String reason,
                                     final int position,final MyViewHolder holder,final String[] item_statusValueArray) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliveryOrderListDetailsActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<SellerDTO> mService = mApiService.getDeliveryRejectPoints("delivery");

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
                                        hashmapList.add(hashMap);
                                    }
                                    rejectPopup(pb_id,action,orderid,USER,description,reason,
                                    position,holder,item_statusValueArray,hashmapList);
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

        public void rejectPopup(final String pb_id,final String action,final String orderid,final String USER,
                                final String description,String reason,
                                final int position,final MyViewHolder holder,final String[] item_statusValueArray,
                                ArrayList<HashMap<String,String>> list) {
            /** Used for Show Disclaimer Pop up screen */

            final EditText rejectorder_txt;
          //  final String itemId = list.get(position).get("id");

            rejectalertDialog = new Dialog(DeliveryOrderListDetailsActivity.this);
            rejectalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            rejectalertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            View rejectlayout = inflater.inflate(R.layout.rej_popup, null);
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
                        dialog = new TransparentProgressDialog(DeliveryOrderListDetailsActivity.this);
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    rejectItem(pb_id,action,orderid,USER,rejectorder_txt.getText().toString().trim(),REASON,
                                    position,holder,item_statusValueArray);
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

//        int rejectItem(String item,String description, final MyViewHolder holder,
//                       final int position,String REASONVAL,final String[] item_statusValueArray) {



//        int rejectItem(String item,String description, final MyViewHolder holder,
//                       final int position,String REASONVAL,final String[] item_statusValueArray,String action) {


        int rejectItem(String pb_id,final String action,String orderid,String USER,String description,String reason,
                       final int position,final MyViewHolder holder,final String[] item_statusValueArray){
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
                String SERVER_URL = SharedDB.URL + "deliveryboy/do_action";
                Log.e("SERVER_URL", SERVER_URL);
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


                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(pb_id);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"action\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(action);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"order\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(orderid);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(PRIMARYID);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(description);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reason\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(reason);
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
//                                        if(action.equals("1")){
//                                            item_statusValueArray[position]="1";
//                                            holder.statustxt.setText("Received");
//                                            holder.statustxt.setVisibility(View.VISIBLE);
//                                            holder.btn_pick.setVisibility(View.GONE);
//                                            holder.btn_reject.setVisibility(View.GONE);
//                                        }

                                        item_statusValueArray[position]="2";
                                        holder.statustxt.setText("Rejected");
                                        holder.statustxt.setVisibility(View.VISIBLE);
                                        holder.btn_pick.setVisibility(View.GONE);
                                        holder.btn_reject.setVisibility(View.GONE);


//                                        holder.pickerbtn.setVisibility(View.GONE);
//                                        holder.rejectbtn.setVisibility(View.GONE);
//                                        holder.picklinear.setVisibility(View.GONE);
//                                        holder.rejectlnr.setVisibility(View.GONE);
//                                        holder.qtyedittext.setFocusable(false);
//                                        holder.qtyedittext.setFocusableInTouchMode(false);
//                                        holder.qtyedittext.setClickable(false);
//
//                                        holder.text_picked.setVisibility(View.VISIBLE);
//                                        holder.pickedliner.setVisibility(View.VISIBLE);
//                                        holder.text_picked.setText("Rejected");
//
//                                        item_statusValueArray[position]="2";
//
//                                        if (res_object.has("total_processed")) {
//                                            TOTALPROCESSED = res_object.getString("total_processed");
//                                        }
//
//                                        if (res_object.has("total_records")) {
//                                            TOTALRECORDS = res_object.getString("total_records");
//                                        }
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
    }



    public void selectImage() {
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
        selectedFilePath=destination.getPath();
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(DeliveryOrderListDetailsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(DeliveryOrderListDetailsActivity.this,
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
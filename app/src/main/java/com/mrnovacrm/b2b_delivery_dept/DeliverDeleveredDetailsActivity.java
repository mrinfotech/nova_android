package com.mrnovacrm.b2b_delivery_dept;

//public class DeliverDeleveredDetailsActivity {
//}

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class DeliverDeleveredDetailsActivity extends AppCompatActivity implements View.OnClickListener {
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
    String fromval;
    String STATUSVALUE;

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

        LinearLayout collinear=findViewById(R.id.collinear);
        collinear.setVisibility(View.GONE);


        TextView storetxt=findViewById(R.id.storetxt);
        storetxt.setVisibility(View.GONE);
        address_txt.setVisibility(View.GONE);
        map_location.setVisibility(View.GONE);


        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }
        setTitle("Order Details");
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("id");
        fromval = bundle.getString("fromval");
        STATUSVALUE = bundle.getString("status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_id.setText(ORDERID);

        LinearLayout order_linear=findViewById(R.id.order_linear);
        order_linear.setVisibility(View.GONE);

        TextView qtytxt=findViewById(R.id.qtytxt);
        qtytxt.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getDeliveryOrderDetailsList(ID);

    }

    private void getDeliveryOrderDetailsList(String orderID) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliverDeleveredDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("OrderID"," :"+orderID);
        Call<Order> mService = mApiService.getPackReceivedDetails(orderID,STATUSVALUE);

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

            String bagname=hashMapArrayList.get(position).get("bag_name");
            holder.item_name.setText(bagname);
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

                item_qty.setVisibility(View.GONE);
                btn_pick.setVisibility(View.GONE);
                btn_reject.setVisibility(View.GONE);
                statustxt.setVisibility(View.GONE);
                deliverlinear.setVisibility(View.GONE);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }

        private void sendPickedItem(final String pb_id,final String action,final String orderid,final String USER,
                                    final String description,final String reason,
                                    final int position,final MyViewHolder holder,final String[] item_statusValueArray) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(DeliverDeleveredDetailsActivity.this);
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






//        int rejectItem(String item,String description, final MyViewHolder holder,
//                       final int position,String REASONVAL,final String[] item_statusValueArray,String action) {



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


}
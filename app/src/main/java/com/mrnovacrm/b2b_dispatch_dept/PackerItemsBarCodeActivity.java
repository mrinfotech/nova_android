package com.mrnovacrm.b2b_dispatch_dept;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BarcodesDTO;
import com.mrnovacrm.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackerItemsBarCodeActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private String id;
    private boolean isConnectedToInternet;
    String ORDERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setTitle("Packed Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_packer_items_bar_code);

        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id");
        ORDERID=bundle.getString("ORDERID");

        TextView orderID=findViewById(R.id.orderID);
        orderID.setText(ORDERID);

        recyclerview = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);

        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(PackerItemsBarCodeActivity.this);
        if (isConnectedToInternet) {
            getOrderListDetails();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(
                            R.string.networkerror), Toast.LENGTH_SHORT).show();
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getOrderListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(PackerItemsBarCodeActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getBarcodes(id);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    Log.e("mOrderObject", "" + mOrderObject);
                    String status = mOrderObject.getStatus();

                    if (Integer.parseInt(status) == 1) {
                        List<BarcodesDTO> productsList = mOrderObject.getBarcodesListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                String[] remQtyValueArray = new String[productsList.size()];
                                String[] checkboxValueArray = new String[productsList.size()];
                                String[] idValueArray = new String[productsList.size()];

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String pb_id = productsList.get(i).getPb_id();
                                    String bag_name = productsList.get(i).getBag_name();
                                    String barcode= productsList.get(i).getBarcode();
                                    String pack_type = productsList.get(i).getPack_type();
                                    String barcode_img = productsList.get(i).getBarcode_img();

                                    hashMap.put("pb_id", pb_id);
                                    hashMap.put("bag_name", bag_name);
                                    hashMap.put("barcode", barcode);
                                    hashMap.put("pack_type", pack_type);
                                    hashMap.put("barcode_img", barcode_img);
                                    hashmapList.add(hashMap);
                                }
                                showProductsData(hashmapList);
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
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showProductsData(ArrayList<HashMap<String, String>> hashmapList) {
            ViewPagerProductsListAdapter adapter = new ViewPagerProductsListAdapter(this, hashmapList);
            recyclerview.setAdapter(adapter);// set adapter on recyclerview
     }

    public class ViewPagerProductsListAdapter extends RecyclerView.Adapter<ViewPagerProductsListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> hashmapList;
        public ViewPagerProductsListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.hashmapList = hashmapList;
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_barcodescardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            String pb_id = hashmapList.get(position).get("pb_id");
            String bag_name = hashmapList.get(position).get("bag_name");
            String barcode = hashmapList.get(position).get("barcode");
            String pack_type = hashmapList.get(position).get("pack_type");
            String barcode_img = hashmapList.get(position).get("barcode_img");


            Picasso.with(mContext)
                    .load(barcode_img)
                    .placeholder(R.drawable.loading)
                    .into(holder.product_image);

            holder.product_name.setText(bag_name);
            holder.product_mrp.setText(barcode);
            holder.product_margin.setText(pack_type);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView cart_img, product_image;
            LinearLayout linear_cost;
            EditText qtyedittext;
            TextView product_name, product_mrp, product_pay, product_discount, product_margin, product_instock;
            Button bt_btn;
            int indexReference;

            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_margin = view.findViewById(R.id.product_margin);
                product_image = view.findViewById(R.id.product_image);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }


    }
}

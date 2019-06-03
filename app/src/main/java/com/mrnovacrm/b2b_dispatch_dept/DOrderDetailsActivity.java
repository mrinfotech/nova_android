package com.mrnovacrm.b2b_dispatch_dept;

//public class DOrderDetailsActivity {
//}

//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.b2b_dealer.ProductImageActivity;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.Login;
//import com.nova.model.Order;
//import com.nova.model.OrdersListDTO;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
///**
// * Created by android on 07-03-2018.
// */
//
//public class DOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
//
//    RecyclerView recyclerView;
//    Button btn_cancel;
//    private Dialog alertDialog;
//    private View layout;
//    private TextView submittxt;
//    private ImageView closeicon;
//    EditText cancelorder_txt;
//    String ID, ORDERID, reason;
//    TextView items, units, savings, s_charge, pay, text_nodata;
//    RelativeLayout rel_footer;
//    private GlobalShare globalShare;
//    private TextView subtotalpayval;
//    private String STATUSVALUE = "";
//    private String SCREENFROM = "";
//    private String SHORTFORM;
//    private TextView paymenttype,creditdate;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.fragment_cancelorder);
//
//        if (getIntent().getExtras() != null) {
//            ID = getIntent().getExtras().getString("id");
//            ORDERID = getIntent().getExtras().getString("orderId");
//            STATUSVALUE = getIntent().getExtras().getString("status");
//            SCREENFROM = getIntent().getExtras().getString("SCREENFROM");
//        }
//
//        globalShare = (GlobalShare) getApplicationContext();
//        View includedLayout = findViewById(R.id.include_actionbar);
//        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
//        actionbarheadertxt.setText("OrderID: " + ORDERID);
//        ImageView backimg = includedLayout.findViewById(R.id.backimg);
//        backimg.setOnClickListener(DOrderDetailsActivity.this);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        items = findViewById(R.id.items);
//        units = findViewById(R.id.units);
//        savings = findViewById(R.id.savings);
//        s_charge = findViewById(R.id.s_charge);
//        pay = findViewById(R.id.pay);
//        subtotalpayval = findViewById(R.id.subtotalpayval);
//        rel_footer = findViewById(R.id.rel_footer);
//        text_nodata = findViewById(R.id.text_nodata);
//
//        paymenttype = findViewById(R.id.paymenttype);
//        creditdate = findViewById(R.id.creditdate);
//
//
//        btn_cancel = findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(DOrderDetailsActivity.this);
//
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//        }
//
//
//
//
////        if(STATUSVALUE.equals("Ordered"))
////        {
////            btn_cancel.setVisibility(View.VISIBLE);
////        }else{
////            btn_cancel.setVisibility(View.GONE);
////        }
//        getOrderListDetails();
//
//    }
//
//    private void getOrderListDetails() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(DOrderDetailsActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        Call<Order> mService = null;
//        if(SCREENFROM.equals("Delivered"))
//        {
//            mService = mApiService.getOrderDetailss(ID,"delivered");
//        }else  if(SCREENFROM.equals("Rejected"))
//        {
//            if(SHORTFORM.equals("ADMIN"))
//            {
//                mService = mApiService.getOrderDetailss(ID, "");
//            }else {
//                mService = mApiService.getOrderDetailss(ID, "rejected");
//            }
//        }else {
//            mService = mApiService.getOrderDetails(ID);
//        }
//
//        mService.enqueue(new Callback<Order>() {
//            @Override
//            public void onResponse(Call<Order> call, Response<Order> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//                try {
//                    Order mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//
//                    Log.e("status",status);
//
//                    if (Integer.parseInt(status) == 1) {
//
//                        String payment_type_val=mOrderObject.getPayment_type();
//                        String creditdateval=mOrderObject.getCredit_date();
//
//                        paymenttype.setText(payment_type_val);
//                        creditdate.setText(creditdateval);
//
//                        String order_status = mOrderObject.getOrder_status();
//
//                        if(SCREENFROM.equals("Rejected"))
//                        {
//                            btn_cancel.setVisibility(View.GONE);
//                        }else {
//                            if (order_status.equals("Ordered")) {
//                                if (SHORTFORM.equals("SE")) {
//                                    btn_cancel.setVisibility(View.GONE);
//                                } else {
//                                    btn_cancel.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                btn_cancel.setVisibility(View.GONE);
//                            }
//                        }
//
//                        String total_rows = mOrderObject.getTotal_rows();
//                        items.setText(total_rows);
//                        String total_units = mOrderObject.getTotal_units();
//                        units.setText(total_units);
//                        String total_savings = mOrderObject.getTotal_savings();
//                        savings.setText(getResources().getString(R.string.Rs) + " " + total_savings);
//                        String total_sur_charge = mOrderObject.getTotal_sur_charge();
//
//
//                        String total_pay = mOrderObject.getTotal_pay();
//                        pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);
//                        String sub_total = mOrderObject.getSub_total();
//                        subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_total);
////
//                        String delivery_charges = mOrderObject.getDelivery_charges();
//                        double charges = Double.parseDouble(delivery_charges) + Double.parseDouble(total_sur_charge);
//                        s_charge.setText(String.valueOf(charges));
//
//                        recyclerView.setVisibility(View.VISIBLE);
//                        List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
//                        if (productsList != null) {
//                            if (productsList.size() > 0) {
//                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
//                                for (int i = 0; i < productsList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();
//
//                                    String id = productsList.get(i).getId();
//                                    String itemid = productsList.get(i).getItemid();
//                                    String sellerid = productsList.get(i).getSellerid();
//                                    String orderid = productsList.get(i).getOrderid();
//                                    String mrp = productsList.get(i).getMrp();
//                                    String qty = productsList.get(i).getQty();
//                                    String service_charge = productsList.get(i).getService_charge();
//                                    String amount = productsList.get(i).getAmount();
//                                    String created_on = productsList.get(i).getCreated_on();
//                                    String seller = productsList.get(i).getSeller();
//                                    String itemname = productsList.get(i).getItemname();
//                                    String brand = productsList.get(i).getBrand();
//                                    String sellingprice = productsList.get(i).getSellingprice();
//                                    String total_price = productsList.get(i).getTotal_price();
//                                    String margin = productsList.get(i).getMargin();
//                                    String discount = productsList.get(i).getDiscount();
//                                    List<String> images = productsList.get(i).getImagesList();
//
//                                    hashMap.put("id", id);
//                                    hashMap.put("itemid", itemid);
//                                    hashMap.put("sellerid", sellerid);
//                                    hashMap.put("orderid", orderid);
//                                    hashMap.put("mrp", mrp);
//                                    hashMap.put("qty", qty);
//                                    hashMap.put("service_charge", service_charge);
//                                    hashMap.put("amount", amount);
//                                    hashMap.put("created_on", created_on);
//                                    hashMap.put("seller", seller);
//                                    hashMap.put("itemname", itemname);
//                                    hashMap.put("brand", brand);
//                                    hashMap.put("sellingprice", sellingprice);
//                                    hashMap.put("total_price", total_price);
//                                    hashMap.put("margin", margin);
//                                    hashMap.put("discount", discount);
//
//                                    hashMapImages.put(id, images);
//                                    hashmapImagesList.add(hashMapImages);
//
//                                    hashmapList.add(hashMap);
//                                }
//                                showOrdersData(hashmapList, hashmapImagesList);
//                            } else {
//                                // nodata_found_txt.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.GONE);
//                        rel_footer.setVisibility(View.GONE);
//                        text_nodata.setVisibility(View.VISIBLE);
//                        text_nodata.setText("Pending confirmation from dealer");
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Order> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
//        if (hashmapList.size() > 0) {
//            recyclerView.setVisibility(View.VISIBLE);
//            CancelOrderAdapter adapter = new CancelOrderAdapter(getApplicationContext(), hashmapList, hashmapImagesList);
//            recyclerView.setAdapter(adapter);
//            rel_footer.setVisibility(View.VISIBLE);
//            text_nodata.setVisibility(View.GONE);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//            rel_footer.setVisibility(View.GONE);
//            text_nodata.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void showCancelOrderPopup() {
//        /** Used for Show Disclaimer Pop up screen */
//        alertDialog = new Dialog(DOrderDetailsActivity.this);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
//        LayoutInflater inflater = this.getLayoutInflater();
//        layout = inflater.inflate(R.layout.cancelorder_popup, null);
//        alertDialog.setContentView(layout);
//        alertDialog.setCancelable(true);
//        if (!alertDialog.isShowing()) {
//            alertDialog.show();
//        }
//        submittxt = layout.findViewById(R.id.submittxt);
//        cancelorder_txt = layout.findViewById(R.id.cancelorder_txt);
//        reason = cancelorder_txt.getText().toString();
//        closeicon = layout.findViewById(R.id.closeicon);
//        closeicon.setOnClickListener(DOrderDetailsActivity.this);
//        submittxt.setOnClickListener(DOrderDetailsActivity.this);
//
//        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
//        linear_edittext.setVisibility(View.VISIBLE);
//
//        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
//        radioGroup.setVisibility(View.GONE);
//    }
//
//    private void cancelOrder(String reason) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(DOrderDetailsActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Log.e("ID", "123 :" + ID);
//        Call<Login> mService = mApiService.cancelOrder(ID, reason);
//
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//
//                Login mObject = response.body();
//                Log.e("mOrderObject", "" + mObject);
//
//                String status = mObject.getStatus();
//                Log.e("status", "" + status);
//                String message = mObject.getMessage();
//
//                try {
//                    if (Integer.parseInt(status) == 1) {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.backimg:
//                finish();
//                break;
//            case R.id.btn_cancel:
//                showCancelOrderPopup();
//                break;
//            case R.id.closeicon:
//                if (alertDialog != null) {
//                    alertDialog.dismiss();
//                }
//                break;
//            case R.id.submittxt:
//                if (alertDialog != null) {
//                    alertDialog.dismiss();
//                }
//                reason = cancelorder_txt.getText().toString();
//                cancelOrder(reason);
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> ordersDetailsList = new ArrayList<HashMap<String, String>>();
//        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
//
//        public CancelOrderAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
//            ordersDetailsList = hashmapList;
//            this.mContext = mContext;
//            this.hashmapImagesList = hashmapImagesList;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.cancelorder_cardview, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, final int position) {
//            String id = ordersDetailsList.get(position).get("id");
//            String itemid = ordersDetailsList.get(position).get("itemid");
//            String sellerid = ordersDetailsList.get(position).get("sellerid");
//            String orderid = ordersDetailsList.get(position).get("orderid");
//            String mrp = ordersDetailsList.get(position).get("mrp");
//            String qty = ordersDetailsList.get(position).get("qty");
//            String service_charge = ordersDetailsList.get(position).get("service_charge");
//            String amount = ordersDetailsList.get(position).get("amount");
//            String created_on = ordersDetailsList.get(position).get("created_on");
//            String seller = ordersDetailsList.get(position).get("seller");
//            String itemname = ordersDetailsList.get(position).get("itemname");
//            String brand = ordersDetailsList.get(position).get("brand");
//            String sellingprice = ordersDetailsList.get(position).get("sellingprice");
//            String total_price = ordersDetailsList.get(position).get("total_price");
//            String margin = ordersDetailsList.get(position).get("margin");
//            String discount = ordersDetailsList.get(position).get("discount");
//
//            holder.product_name.setText(itemname);
//            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);
//            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + amount);
//            holder.qty.setText(qty);
//            holder.total_cost.setText(mContext.getResources().getString(R.string.Rs) + " " + total_price);
//            holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
//            holder.product_margin.setText(mContext.getResources().getString(R.string.Rs) + " " + margin);
//
//            List<String> imagesList = hashmapImagesList.get(position).get(id);
//            if (imagesList != null) {
//                if (imagesList.size() > 0) {
//                    Picasso.with(mContext)
//                            .load(imagesList.get(0))
//                            .placeholder(R.drawable.loading)
//                            .into(holder.product_image);
//                } else {
//                    holder.product_image.setImageResource(R.drawable.noimage);
//                }
//            } else {
//                holder.product_image.setImageResource(R.drawable.noimage);
//            }
//
//            holder.product_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String id = ordersDetailsList.get(position).get("id");
//                    List<String> imagesList = hashmapImagesList.get(position).get(id);
//
//                    Log.e("imagesList in Adapter", "" + imagesList);
//                    if (imagesList != null) {
//                        if (imagesList.size() > 0) {
//                            String itemname = ordersDetailsList.get(position).get("itemname");
//                            globalShare.setImagesList(imagesList);
//                            Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
//                            intent.putExtra("itemname", itemname);
//                            startActivity(intent);
//                        } else {
//                        }
//                    } else {
//                    }
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return ordersDetailsList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            ImageView product_image;
//            EditText qtyedittext;
//            TextView product_name, product_mrp, product_pay, product_discount, product_margin, product_instock, qty, total_cost;
//
//            MyViewHolder(View view) {
//                super(view);
//                product_name = view.findViewById(R.id.product_name);
//                product_mrp = view.findViewById(R.id.product_mrp);
//                product_pay = view.findViewById(R.id.product_pay);
//                product_discount = view.findViewById(R.id.product_discount);
//                product_margin = view.findViewById(R.id.product_margin);
//                product_instock = view.findViewById(R.id.product_instock);
//
//                qtyedittext = view.findViewById(R.id.qtyedittext);
//                product_image = view.findViewById(R.id.product_image);
//                qty = view.findViewById(R.id.qty);
//                total_cost = view.findViewById(R.id.total_cost);
//
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        }
//    }
//
//}


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String DELIVERY_ADDRESS = "";
    private String USERNAME = "";
    private String MOBILE = "";
    private String SHORTFORM = "";
    private double LATITUDE_VALUE, LANGITUDE_VALUE;
    String addressid="";

    TextView noofitemstxt,totalamounttxt,order_numtxt,mobilenumbertxt,deliveryaddresstxt,
            totalamounbt_txt,deliverychargestxt,deliverybytxt,estimatedtime,servicechargestxt;
    RecyclerView recyclerView;
    //  Button donebtn;
    Context context;
    private String order_key="";
    GlobalShare globalShare;
    public static Activity mainfinish;
    private Button btn_cancel;
    private TextView subtotalamounbt_txt;
    LinearLayout creditdatelinear,chequenumberlinear,chequedatelinear;
    TextView paymenttype_txt,creditdate_txt,chequenumber_txt,chequedate_txt,ordertakenby_txt;

    String ID, ORDERID, reason;
    private String STATUSVALUE = "";
    private String SCREENFROM = "";

    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private ImageView closeicon;
    EditText cancelorder_txt;
    RelativeLayout ordersummaryrel,errorrel;
    TextView text_nodata;

    LinearLayout remarkslinear;
    TextView remarks_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_ordersummarydetails);
        context=this;

        globalShare=(GlobalShare)getApplicationContext();
        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        actionTextView.setText("Order details");
        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(DOrderDetailsActivity.this);

//        String orderId = getIntent().getStringExtra("orderId");
//        order_key = getIntent().getStringExtra("order_key");

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERID = getIntent().getExtras().getString("orderId");
            STATUSVALUE = getIntent().getExtras().getString("status");
            SCREENFROM = getIntent().getExtras().getString("SCREENFROM");
        }

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
            USERNAME = values.get(SharedDB.USERNAME);
            MOBILE = values.get(SharedDB.MOBILE);
            SHORTFORM = values.get(SharedDB.SHORTFORM);

            String LAT = values.get(SharedDB.LATITUDE);
            String LNG = values.get(SharedDB.LONGITIDE);
            LANGITUDE_VALUE = Double.parseDouble(LNG);
            LATITUDE_VALUE = Double.parseDouble(LAT);
        }

        deliverybytxt=findViewById(R.id.deliverybytxt);
        estimatedtime=findViewById(R.id.estimatedtime);
        noofitemstxt=findViewById(R.id.noofitemstxt);
        totalamounttxt=findViewById(R.id.totalamounttxt);
        order_numtxt=findViewById(R.id.order_numtxt);
        mobilenumbertxt=findViewById(R.id.mobilenumbertxt);
        deliveryaddresstxt=findViewById(R.id.deliveryaddresstxt);
        servicechargestxt=findViewById(R.id.servicechargestxt);
        ordersummaryrel=findViewById(R.id.ordersummaryrel);
        errorrel=findViewById(R.id.errorrel);
        text_nodata=findViewById(R.id.text_nodata);

        remarkslinear=findViewById(R.id.remarkslinear);
        remarks_text=findViewById(R.id.remarks_text);

        recyclerView=findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        totalamounbt_txt=findViewById(R.id.totalamounbt_txt);
        deliverychargestxt=findViewById(R.id.deliverychargestxt);
        subtotalamounbt_txt=findViewById(R.id.subtotalamounbt_txt);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(DOrderDetailsActivity.this);

        order_numtxt.setText("Order No. "+ORDERID);

        paymenttype_txt=findViewById(R.id.paymenttype_txt);
        creditdatelinear=findViewById(R.id.creditdatelinear);
        creditdate_txt=findViewById(R.id.creditdate_txt);
        chequenumberlinear=findViewById(R.id.chequenumberlinear);
        chequenumber_txt=findViewById(R.id.chequenumber_txt);
        chequedatelinear=findViewById(R.id.chequedatelinear);
        chequedate_txt=findViewById(R.id.chequedate_txt);
        ordertakenby_txt=findViewById(R.id.ordertakenby_txt);

        ordersummaryrel.setVisibility(View.GONE);

        getOrderListDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backimg:
                finish();
                break;
            case R.id.btn_cancel:
                showCancelOrderPopup();
                break;
            case R.id.closeicon:
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                break;
            case R.id.submittxt:
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                reason = cancelorder_txt.getText().toString();
                cancelOrder(reason);
                break;

            default:
                break;
        }
    }

    public void showCancelOrderPopup() {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(DOrderDetailsActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = this.getLayoutInflater();
        layout = inflater.inflate(R.layout.cancelorder_popup, null);
        alertDialog.setContentView(layout);
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        submittxt = layout.findViewById(R.id.submittxt);
        cancelorder_txt = layout.findViewById(R.id.cancelorder_txt);
        reason = cancelorder_txt.getText().toString();
        closeicon = layout.findViewById(R.id.closeicon);
        closeicon.setOnClickListener(DOrderDetailsActivity.this);
        submittxt.setOnClickListener(DOrderDetailsActivity.this);

        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
        linear_edittext.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
        radioGroup.setVisibility(View.GONE);
    }

    private void cancelOrder(String reason) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("ID", "123 :" + ID);
        Call<Login> mService = mApiService.cancelOrder(ID, reason);

        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Login mObject = response.body();
                Log.e("mOrderObject", "" + mObject);

                String status = mObject.getStatus();
                Log.e("status", "" + status);
                String message = mObject.getMessage();

                try {
                    if (Integer.parseInt(status) == 1) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
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

    private void getOrderListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        //  Call<Order> mService = mApiService.getOrderDetails(order_key);

        Call<Order> mService = null;
        if(SCREENFROM.equals("Delivered"))
        {
            mService = mApiService.getDeliverOrderDetailss(ID,"delivered",SHORTFORM);
        }
        else  if(SCREENFROM.equals("Rejected"))
        {
                mService = mApiService.getDeliverOrderDetailss(ID, "rejected",SHORTFORM);
        }else {
            mService = mApiService.getOrderDetails(ID,SHORTFORM);
        }

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();



                    if (Integer.parseInt(status) == 1) {
                        String total_rows = mOrderObject.getTotal_rows();
                        String total_units = mOrderObject.getTotal_units();
                        String total_savings = mOrderObject.getTotal_savings();
                        String total_sur_charge = mOrderObject.getTotal_sur_charge();
                        String total_pay = mOrderObject.getTotal_pay();
                        String store_address=mOrderObject.getStore_address();
                        String delivery_date=mOrderObject.getDelivery_date();
                        String delivery_time=mOrderObject.getDelivery_time();
                        String delivery_charges=mOrderObject.getDelivery_charges();
                        String delivery_subtotal=mOrderObject.getSub_total();
                        String mobile_number=mOrderObject.getMobile();
                        String order_status=mOrderObject.getOrder_status();

                        String payment_type=mOrderObject.getPayment_type();
                        String credit_date=mOrderObject.getCredit_date();
                        String reference_no=mOrderObject.getReference_no();
                        String takenby_name=mOrderObject.getTakenby_name();
                        String REMARKS=mOrderObject.getRemarks();

                        if(!REMARKS.equals(""))
                        {
                            remarkslinear.setVisibility(View.VISIBLE);
                            remarks_text.setText(REMARKS);
                        }else{
                            remarkslinear.setVisibility(View.GONE);
                        }

                        errorrel.setVisibility(View.GONE);
                        if(payment_type.equals("credit"))
                        {
                            creditdatelinear.setVisibility(View.VISIBLE);
                            creditdate_txt.setText(credit_date);
                            chequenumberlinear.setVisibility(View.GONE);
                            chequedatelinear.setVisibility(View.GONE);
                        }else{
                            creditdatelinear.setVisibility(View.GONE);
                            chequedate_txt.setText(credit_date);
                            chequenumber_txt.setText(reference_no);
                            chequenumberlinear.setVisibility(View.VISIBLE);
                            chequedatelinear.setVisibility(View.VISIBLE);
                        }

                        paymenttype_txt.setText(payment_type);
                        ordertakenby_txt.setText(takenby_name);

                        if(SCREENFROM.equals("Rejected"))
                        {
                            btn_cancel.setVisibility(View.GONE);
                        }else {
                            if (order_status.equals("Ordered")) {
                                if (SHORTFORM.equals("SE")) {
                                    btn_cancel.setVisibility(View.GONE);
                                } else {
                                    btn_cancel.setVisibility(View.VISIBLE);
                                }
                            } else {
                                btn_cancel.setVisibility(View.GONE);
                            }
                        }

                        mobilenumbertxt.setText(mobile_number);
                        noofitemstxt.setText(total_units);
                        totalamounttxt.setText(total_pay);
                        deliveryaddresstxt.setText(store_address);
                        deliverybytxt.setText(delivery_date);
                        estimatedtime.setText(delivery_time);

                        totalamounbt_txt.setText(total_pay+" INR");

                        subtotalamounbt_txt.setText(delivery_subtotal+" INR");

                        if(!delivery_charges.equals("0"))
                        {
                            deliverychargestxt.setText(delivery_charges+" INR");
                        }else{
                            deliverychargestxt.setText(delivery_charges);
                        }

                        if(total_sur_charge.equals("0.0"))
                        {
                            servicechargestxt.setText("Free");
                        }else{
                            servicechargestxt.setText(total_sur_charge+" INR");
                        }

                        List<OrdersListDTO> productsList;
                        if(SCREENFROM.equals("Delivered"))
                        {
                            productsList = mOrderObject.getOrdersListDTOS();
                        }else{
                            productsList = mOrderObject.getRejectordersListDTOS();
                        }
                    //    List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {

                                ordersummaryrel.setVisibility(View.VISIBLE);
                                errorrel.setVisibility(View.GONE);


                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                    String id = productsList.get(i).getId();
                                    String itemid = productsList.get(i).getItemid();
                                    String sellerid = productsList.get(i).getSellerid();
                                    String orderid = productsList.get(i).getOrderid();
                                    String mrp = productsList.get(i).getMrp();
                                    String qty = productsList.get(i).getQty();
                                    String service_charge = productsList.get(i).getService_charge();
                                    String amount = productsList.get(i).getAmount();
                                    String created_on = productsList.get(i).getCreated_on();
                                    String seller = productsList.get(i).getSeller();
                                    String itemname = productsList.get(i).getItemname();
                                    String brand = productsList.get(i).getBrand();
                                    String sellingprice = productsList.get(i).getSellingprice();
                                    String total_price = productsList.get(i).getTotal_price();
                                    String margin = productsList.get(i).getMargin();
                                    String discount = productsList.get(i).getDiscount();
                                    String picked_qty = productsList.get(i).getPicked_qty();

                                    List<String> images=productsList.get(i).getImagesList();

                                    hashMap.put("id", id);
                                    hashMap.put("itemid", itemid);
                                    hashMap.put("sellerid", sellerid);
                                    hashMap.put("orderid", orderid);
                                    hashMap.put("mrp", mrp);
                                    hashMap.put("qty", qty);
                                    hashMap.put("service_charge", service_charge);
                                    hashMap.put("amount", amount);
                                    hashMap.put("created_on", created_on);
                                    hashMap.put("seller", seller);
                                    hashMap.put("itemname", itemname);
                                    hashMap.put("brand", brand);
                                    hashMap.put("sellingprice", sellingprice);
                                    hashMap.put("total_price", total_price);
                                    hashMap.put("margin", margin);
                                    hashMap.put("discount", discount);
                                    hashMap.put("picked_qty", picked_qty);

                                    hashMapImages.put(id,images);
                                    hashmapImagesList.add(hashMapImages);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList,hashmapImagesList);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                                ordersummaryrel.setVisibility(View.GONE);
                                errorrel.setVisibility(View.VISIBLE);
                                text_nodata.setText("Pending confirmation from dealer");
                            }
                        }
                    } else {
                        errorrel.setVisibility(View.VISIBLE);
                        text_nodata.setText("Pending confirmation from dealer");
                        ordersummaryrel.setVisibility(View.GONE);
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

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList,ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
        CartListAdapter adapter = new CartListAdapter(getApplicationContext(), hashmapList,hashmapImagesList);
        recyclerView.setAdapter(adapter);
    }

    public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersDetailsList;
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        public CartListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
            this.mContext = mContext;
            this.ordersDetailsList = hashmapList;
            this.hashmapImagesList=hashmapImagesList;
        }

        @Override
        public CartListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ordersummary_listadapter, parent, false);
            return new CartListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CartListAdapter.MyViewHolder holder, final int position) {
            String id = ordersDetailsList.get(position).get("id");
            String itemid = ordersDetailsList.get(position).get("itemid");
            String sellerid = ordersDetailsList.get(position).get("sellerid");
            String orderid = ordersDetailsList.get(position).get("orderid");
            String mrp = ordersDetailsList.get(position).get("mrp");
            String qty = ordersDetailsList.get(position).get("qty");
            String service_charge = ordersDetailsList.get(position).get("service_charge");
            String amount = ordersDetailsList.get(position).get("amount");
            String created_on = ordersDetailsList.get(position).get("created_on");
            String seller = ordersDetailsList.get(position).get("seller");
            String itemname = ordersDetailsList.get(position).get("itemname");
            String brand = ordersDetailsList.get(position).get("brand");
            String sellingprice = ordersDetailsList.get(position).get("sellingprice");
            String total_price = ordersDetailsList.get(position).get("total_price");
            String margin = ordersDetailsList.get(position).get("margin");
            String discount = ordersDetailsList.get(position).get("discount");
            String picked_qty = ordersDetailsList.get(position).get("picked_qty");

            holder.product_name.setText(itemname);
            if(SCREENFROM.equals("Delivered"))
            {
                holder.productcal.setText(amount+" X "+picked_qty);
            }else{
                holder.productcal.setText(amount+" X "+qty);
            }

            holder.totlproductcnt.setText(total_price+" INR");
        }

        @Override
        public int getItemCount() {
            return ordersDetailsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            EditText qtyedittext;
            ImageView product_image, cart_img;
            TextView product_name, productcal, totlproductcnt;

            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                productcal = view.findViewById(R.id.productcal);
                totlproductcnt = view.findViewById(R.id.totlproductcnt);
                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }
    }
}

package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminOrdersTabActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.SellersOrdersListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinanceOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String DELIVERY_ADDRESS = "";
    private String USERNAME = "";
    private String MOBILE = "";
    private String SHORTFORM = "";
    private double LATITUDE_VALUE, LANGITUDE_VALUE;
    String addressid = "";

    TextView ordertakenbytitle, noofitemstxt, totalamounttxt, order_numtxt, mobilenumbertxt, deliveryaddresstxt,
            totalamounbt_txt, deliverychargestxt, deliverybytxt, estimatedtime, servicechargestxt;
    RecyclerView recyclerView;
    //  Button donebtn;
    Context context;
    private String order_key = "";
    GlobalShare globalShare;
    public static Activity mainfinish;
    private Button btn_cancel, btn_approved;
    private TextView subtotalamounbt_txt;
    LinearLayout creditdatelinear, chequenumberlinear, chequedatelinear;
    TextView paymenttype_txt, creditdate_txt, chequenumber_txt, chequedate_txt, ordertakenby_txt;

    String ID, ORDERID, reason;
    private String STATUSVALUE = "";
    private String SCREENFROM = "";

    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private ImageView closeicon;
    EditText cancelorder_txt;
    RelativeLayout btmrel;
    Context mContext;

    private String REASONVAL = "";
    private Dialog rejectalertDialog;

    LinearLayout remarkslinear;
    TextView remarks_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_financeordersummarydetails);
        context = this;
        mContext = this;

        globalShare = (GlobalShare) getApplicationContext();
        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        actionTextView.setText("Order details");
        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(FinanceOrderDetailsActivity.this);

//        String orderId = getIntent().getStringExtra("orderId");
//        order_key = getIntent().getStringExtra("order_key");

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERID = getIntent().getExtras().getString("orderId");
            STATUSVALUE = getIntent().getExtras().getString("status");
            SCREENFROM = getIntent().getExtras().getString("SCREENFROM");
        }

        //  Log.e("STATUSVALUE",STATUSVALUE);
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

        ordertakenbytitle = findViewById(R.id.ordertakenbytitle);
        deliverybytxt = findViewById(R.id.deliverybytxt);
        estimatedtime = findViewById(R.id.estimatedtime);
        noofitemstxt = findViewById(R.id.noofitemstxt);
        totalamounttxt = findViewById(R.id.totalamounttxt);
        order_numtxt = findViewById(R.id.order_numtxt);
        mobilenumbertxt = findViewById(R.id.mobilenumbertxt);
        deliveryaddresstxt = findViewById(R.id.deliveryaddresstxt);
        servicechargestxt = findViewById(R.id.servicechargestxt);

        remarkslinear=findViewById(R.id.remarkslinear);
        remarks_text=findViewById(R.id.remarks_text);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        totalamounbt_txt = findViewById(R.id.totalamounbt_txt);
        deliverychargestxt = findViewById(R.id.deliverychargestxt);
        subtotalamounbt_txt = findViewById(R.id.subtotalamounbt_txt);

        btmrel = findViewById(R.id.btmrel);

        btn_approved = findViewById(R.id.btn_approved);
        btn_approved.setOnClickListener(FinanceOrderDetailsActivity.this);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(FinanceOrderDetailsActivity.this);
        //   order_numtxt.setText("Order No. "+ORDERID);
        paymenttype_txt = findViewById(R.id.paymenttype_txt);
        creditdatelinear = findViewById(R.id.creditdatelinear);
        creditdate_txt = findViewById(R.id.creditdate_txt);
        chequenumberlinear = findViewById(R.id.chequenumberlinear);
        chequenumber_txt = findViewById(R.id.chequenumber_txt);
        chequedatelinear = findViewById(R.id.chequedatelinear);
        chequedate_txt = findViewById(R.id.chequedate_txt);
        ordertakenby_txt = findViewById(R.id.ordertakenby_txt);

        if (SCREENFROM.equals("Transferred")) {
            ordertakenbytitle.setText("Order transferred by");

        } else {
            ordertakenbytitle.setText("Order taken by");
        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if (isConnectedToInternet) {
            getOrderListDetails();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            case R.id.btn_cancel:
                getRejectPoints();
                break;
            case R.id.btn_approved:
                REASONVAL = "";
                submitRejectionPoints("1");
                break;
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
            default:
                break;
        }
    }

    public void showCancelOrderPopup() {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(FinanceOrderDetailsActivity.this);
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
        closeicon.setOnClickListener(FinanceOrderDetailsActivity.this);
        submittxt.setOnClickListener(FinanceOrderDetailsActivity.this);

        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
        linear_edittext.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
        radioGroup.setVisibility(View.GONE);
    }

    private void cancelOrder(String reason) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceOrderDetailsActivity.this);
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
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        //  Call<Order> mService = mApiService.getOrderDetails(order_key);
        Call<Order> mService = null;
        if (SCREENFROM.equals("Transferred")) {
            mService = mApiService.getTransferOrderDetails(ID, SHORTFORM);
        } else {
            mService = mApiService.getOrderDetails(ID, SHORTFORM);
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
                        String store_address = mOrderObject.getStore_address();
                        String delivery_date = mOrderObject.getDelivery_date();
                        String delivery_time = mOrderObject.getDelivery_time();
                        String delivery_charges = mOrderObject.getDelivery_charges();
                        String delivery_subtotal = mOrderObject.getSub_total();
                        String mobile_number = mOrderObject.getMobile();
                        String order_status = mOrderObject.getOrder_status();
                        String order_id = mOrderObject.getOrder_id();

                        String payment_type = mOrderObject.getPayment_type();
                        String credit_date = mOrderObject.getCredit_date();
                        String reference_no = mOrderObject.getReference_no();
                        String takenby_name = mOrderObject.getTakenby_name();
                        String fa_status = mOrderObject.getFa_status();
                        String admin_status = mOrderObject.getAdmin_status();
                        String parent_order = mOrderObject.getParent_order();
                        String REMARKS=mOrderObject.getRemarks();

                        if(!REMARKS.equals(""))
                        {
                            remarkslinear.setVisibility(View.VISIBLE);
                            remarks_text.setText(REMARKS);
                        }else{
                            remarkslinear.setVisibility(View.GONE);
                        }


                        if (payment_type.equals("credit")) {
                            creditdatelinear.setVisibility(View.VISIBLE);
                            creditdate_txt.setText(credit_date);
                            chequenumberlinear.setVisibility(View.GONE);
                            chequedatelinear.setVisibility(View.GONE);
                        } else {
                            creditdatelinear.setVisibility(View.GONE);
                            chequedate_txt.setText(credit_date);
                            chequenumber_txt.setText(reference_no);
                            chequenumberlinear.setVisibility(View.VISIBLE);
                            chequedatelinear.setVisibility(View.VISIBLE);
                        }
                        order_numtxt.setText("Order No. " + order_id);
                        paymenttype_txt.setText(payment_type);
                        ordertakenby_txt.setText(takenby_name);

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


//                        if(SHORTFORM.equals("SA"))
//                        {
//                          //  btmrel.setVisibility(View.GONE);
//                        }else

                        if (SHORTFORM.equals("FM")) {
                            if (!fa_status.equals("0") || !admin_status.equals("0")) {
                                btmrel.setVisibility(View.GONE);
                            } else if (SCREENFROM.equals("Approved")) {
                                btmrel.setVisibility(View.GONE);
                            } else {
                                if (order_status.equals("Ordered")) {
                                    btmrel.setVisibility(View.VISIBLE);
                                } else {
                                    btmrel.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (order_status.equals("Ordered")) {
                                btmrel.setVisibility(View.VISIBLE);
                                //btn_cancel.setVisibility(View.VISIBLE);
                            } else {
                                btmrel.setVisibility(View.GONE);
                                // btn_cancel.setVisibility(View.GONE);
                            }
                        }

                        if (SCREENFROM.equals("Rejected")) {
                            btmrel.setVisibility(View.GONE);
                        }

                        if (SCREENFROM.equals("Transferred")) {
                            btmrel.setVisibility(View.GONE);
                        }

                        mobilenumbertxt.setText(mobile_number);
                        noofitemstxt.setText(total_units);
                        totalamounttxt.setText(total_pay);
                        deliveryaddresstxt.setText(store_address);
                        deliverybytxt.setText(delivery_date);
                        estimatedtime.setText(delivery_time);

                        totalamounbt_txt.setText(total_pay + " INR");

                        subtotalamounbt_txt.setText(delivery_subtotal + " INR");

                        if (!delivery_charges.equals("0")) {
                            deliverychargestxt.setText(delivery_charges + " INR");
                        } else {
                            deliverychargestxt.setText(delivery_charges);
                        }

                        if (total_sur_charge.equals("0.0")) {
                            servicechargestxt.setText("Free");
                        } else {
                            servicechargestxt.setText(total_sur_charge + " INR");
                        }

                        List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
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
                                    List<String> images = productsList.get(i).getImagesList();

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

                                    hashMapImages.put(id, images);
                                    hashmapImagesList.add(hashMapImages);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList, hashmapImagesList);
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

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
        CartListAdapter adapter = new CartListAdapter(getApplicationContext(), hashmapList, hashmapImagesList);
        recyclerView.setAdapter(adapter);
    }

    public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersDetailsList;
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;

        public CartListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
            this.mContext = mContext;
            this.ordersDetailsList = hashmapList;
            this.hashmapImagesList = hashmapImagesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ordersummary_listadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
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

            holder.product_name.setText(itemname);
            holder.productcal.setText(amount + " X " + qty);
            holder.totlproductcnt.setText(total_price + " INR");
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


    private void getRejectPoints() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<SellerDTO> mService = mApiService.getFinanceRejectPoints(SHORTFORM);

        mService.enqueue(new Callback<SellerDTO>() {
            @Override
            public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    SellerDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
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
                                rejectPopup(hashmapList);
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

    public void rejectPopup(ArrayList<HashMap<String, String>> list) {
        /** Used for Show Disclaimer Pop up screen */

        REASONVAL = "";
        rejectalertDialog = new Dialog(FinanceOrderDetailsActivity.this);
        rejectalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rejectalertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        View rejectlayout = inflater.inflate(R.layout.layout_financerejpopup, null);
        TextView reason_cancel = rejectlayout.findViewById(R.id.reason_reject);
        reason_cancel.setText("Reason for reject");
        rejectalertDialog.setContentView(rejectlayout);
        rejectalertDialog.setCancelable(true);
        if (!rejectalertDialog.isShowing()) {
            rejectalertDialog.show();
        }
        EditText rejectorder_txt = rejectlayout.findViewById(R.id.rejectorder_txt);
        rejectorder_txt.setVisibility(View.GONE);

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
                REASONVAL = String.valueOf(checkedId);
            }
        });

        EditText proof_path = rejectlayout.findViewById(R.id.proof_path);
        proof_path.setVisibility(View.GONE);
        Button attach_proof = rejectlayout.findViewById(R.id.attach_proof);
        attach_proof.setVisibility(View.GONE);

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
                if (REASONVAL.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select reason", Toast.LENGTH_SHORT).show();
                } else {
                    submitRejectionPoints("2");
                }
            }
        });
    }

    private void submitRejectionPoints(String status) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;

        if (SHORTFORM.equals("FM")) {
            mService = mApiService.submitRejectPoints(PRIMARYID, SHORTFORM, "", status, ID, REASONVAL);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dialog.dismiss();
                    Log.e("response", "" + response);
                    try {
                        Login mOrderObject = response.body();
                        String status = mOrderObject.getStatus();
                        if (Integer.parseInt(status) == 1) {
                            String message = mOrderObject.getMessage();

                            if (SHORTFORM.equals("FM")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Log.e("STATUSVALUE", STATUSVALUE);


                                if (STATUSVALUE.equals("notification")) {
                                    finish();
                                } else {
                                    if (FinanceOrdersTabActivity.mainfinish != null) {
                                        FinanceOrdersTabActivity.mainfinish.finish();
                                    }
                                    Intent intent = new Intent(getApplicationContext(), FinanceOrdersTabActivity.class);
                                    startActivity(intent);
                                }
                            } else if (SHORTFORM.equals("ADMIN")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                if (AdminOrdersTabActivity.mainfinish != null) {
                                    AdminOrdersTabActivity.mainfinish.finish();
                                }
                                Intent intent = new Intent(getApplicationContext(), AdminOrdersTabActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        } else {
                            String message = mOrderObject.getMessage();
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
        } else if (SHORTFORM.equals("ADMIN") || SHORTFORM.equals("SA")) {
            mService = mApiService.submitAdminRejectPoints(PRIMARYID, SHORTFORM, "", status, ID, REASONVAL);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dialog.dismiss();
                    Log.e("response", "" + response);
                    try {
                        Login mOrderObject = response.body();
                        String status = mOrderObject.getStatus();
                        if (Integer.parseInt(status) == 1) {
                            String message = mOrderObject.getMessage();
                            if (SHORTFORM.equals("FM")) {
//                                globalShare.setFinancemenuselectpos("7");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                if (FinanceOrdersTabActivity.mainfinish != null) {
                                    FinanceOrdersTabActivity.mainfinish.finish();
                                }
                                Intent intent = new Intent(getApplicationContext(), FinanceOrdersTabActivity.class);
                                startActivity(intent);
                            } else if (SHORTFORM.equals("ADMIN")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                if (AdminOrdersTabActivity.mainfinish != null) {
                                    AdminOrdersTabActivity.mainfinish.finish();
                                }
                                Intent intent = new Intent(getApplicationContext(), AdminOrdersTabActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        } else {
                            String message = mOrderObject.getMessage();
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

    }
}

//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
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
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.nova.R;
//import com.nova.b2b_admin.AdminOrdersTabActivity;
//import com.nova.b2b_dealer.ProductImageActivity;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.Login;
//import com.nova.model.Order;
//import com.nova.model.OrdersListDTO;
//import com.nova.model.SellerDTO;
//import com.nova.model.SellersOrdersListDTO;
//import com.squareup.picasso.Picasso;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
///**
// * Created by android on 07-03-2018.
// */
//
//public class FinanceOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
//
//    RecyclerView recyclerView;
//    Button btn_cancel,btn_approved;
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
//    LinearLayout btnlinear;
//    Context mContext;
//    private String REASONVAL="";
//    private Dialog rejectalertDialog;
//    String PRIMARYID="";
//    private String SHORTFORM;
//    private String SCREENFROM;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext=this;
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.layout_financeorderdetails);
//        if (getIntent().getExtras() != null) {
//            ID = getIntent().getExtras().getString("id");
//            ORDERID = getIntent().getExtras().getString("orderId");
//            STATUSVALUE = getIntent().getExtras().getString("status");
//            SCREENFROM = getIntent().getExtras().getString("SCREENFROM");
//        }
//
//        if(SharedDB.isLoggedIn(getApplicationContext()))
//        {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID=values.get(SharedDB.PRIMARYID);
//            SHORTFORM=values.get(SharedDB.SHORTFORM);
//
//        }
//        globalShare = (GlobalShare) getApplicationContext();
//        View includedLayout = findViewById(R.id.include_actionbar);
//        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
//        actionbarheadertxt.setText("OrderID: " + ORDERID);
//        ImageView backimg = includedLayout.findViewById(R.id.backimg);
//        backimg.setOnClickListener(FinanceOrderDetailsActivity.this);
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
//        btnlinear=findViewById(R.id.btnlinear);
//        btn_approved=findViewById(R.id.btn_approved);
//
//        btn_cancel = findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(FinanceOrderDetailsActivity.this);
//        btn_approved.setOnClickListener(FinanceOrderDetailsActivity.this);
//
//        getOrderListDetails();
//    }
//
//    private void getOrderListDetails() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceOrderDetailsActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Order> mService = mApiService.getOrderDetails(ID);
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
//                    if (Integer.parseInt(status) == 1) {
//                        String order_status = mOrderObject.getOrder_status();
//
//                        if(SHORTFORM.equals("SA"))
//                        {
//                            btnlinear.setVisibility(View.GONE);
//                        }else if(SHORTFORM.equals("FM")) {
//                            if(SCREENFROM.equals("Approved"))
//                            {
//                                btnlinear.setVisibility(View.GONE);
//                            }else{
//                                if (order_status.equals("Ordered")) {
//                                    btnlinear.setVisibility(View.VISIBLE);
//                                } else {
//                                    btnlinear.setVisibility(View.GONE);
//                                }
//                            }
//                        }else {
//                            if (order_status.equals("Ordered")) {
//                                btnlinear.setVisibility(View.VISIBLE);
//                                //btn_cancel.setVisibility(View.VISIBLE);
//                            } else {
//                                btnlinear.setVisibility(View.GONE);
//                                // btn_cancel.setVisibility(View.GONE);
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
//                        String total_pay = mOrderObject.getTotal_pay();
//                        pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);
//                        String sub_total = mOrderObject.getSub_total();
//                        subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_total);
//
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
//        alertDialog = new Dialog(FinanceOrderDetailsActivity.this);
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
//        closeicon.setOnClickListener(FinanceOrderDetailsActivity.this);
//        submittxt.setOnClickListener(FinanceOrderDetailsActivity.this);
//
//        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
//        linear_edittext.setVisibility(View.GONE);
//
//        ArrayList<HashMap<String, String>> hashmaplist=new ArrayList<HashMap<String, String>>();
//
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        String id = "1";
//        String rejectpoint = "No Credit";
//        hashMap.put("id", id);
//        hashMap.put("rejectpoint", rejectpoint);
//        hashmaplist.add(hashMap);
//
//        String id1 = "2";
//        String nostock = "No Stock";
//        hashMap.put("id", id1);
//        hashMap.put("rejectpoint", nostock);
//        hashmaplist.add(hashMap);
//
//        RadioGroup rgp = findViewById(R.id.radiogroup);
//        RadioGroup.LayoutParams rprms;
//
//
//            RadioButton rdbtn = new RadioButton(mContext);
//            rdbtn.setId(Integer.parseInt(id));
//            rdbtn.setText(rejectpoint);
//            rdbtn.setTextColor(Color.BLACK);
//            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            rgp.addView(rdbtn, rprms);
//
//
//        RadioButton rdbtn1 = new RadioButton(mContext);
//        rdbtn1.setId(Integer.parseInt(id1));
//        rdbtn1.setText(nostock);
//        rdbtn1.setTextColor(Color.BLACK);
//        rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rgp.addView(rdbtn1, rprms);
//
//
//
//
//
////        for (int i = 0; i < hashmaplist.size(); i++) {
////
////            String _id = hashmaplist.get(i).get("id");
////            String _rejectpoint = hashmaplist.get(i).get("rejectpoint");
////            RadioButton rdbtn = new RadioButton(mContext);
////            rdbtn.setId(Integer.parseInt(_id));
////            rdbtn.setText(_rejectpoint);
////            rdbtn.setTextColor(Color.BLACK);
////
////            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////            rgp.addView(rdbtn, rprms);
////        }
////        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            public void onCheckedChanged(RadioGroup group, int checkedId) {
////                Log.e("CheckedId", " :" + checkedId);
////                REASONVAL = String.valueOf(checkedId);
////            }
////        });
//    }
//
//    private void cancelOrder(String reason) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceOrderDetailsActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Log.e("ID", "123 :" + ID);
//        Call<Login> mService = mApiService.cancelOrder(ID, reason);
//
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                dialog.dismiss();
//                try {
//                    Log.e("response", "" + response);
//                    Login mObject = response.body();
//                    Log.e("mOrderObject", "" + mObject);
//
//                    String status = mObject.getStatus();
//                    Log.e("status", "" + status);
//                    String message = mObject.getMessage();
//                    if (Integer.parseInt(status) == 1) {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.backimg:
//                finish();
//                break;
//            case R.id.btn_cancel:
//                 getRejectPoints();
//                break;
//
////            case R.id.closeicon:
////                if (alertDialog != null) {
////                    alertDialog.dismiss();
////                }
////                break;
////            case R.id.submittxt:
////                if (alertDialog != null) {
////                    alertDialog.dismiss();
////                }
////               // reason = cancelorder_txt.getText().toString();
////                cancelOrder(REASONVAL);
////                break;
//            case R.id.btn_approved:
//                REASONVAL="";
//                submitRejectionPoints("1");
//                break;
//           default:
//                break;
//        }
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
//    private void getRejectPoints() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        Call<SellerDTO> mService = mApiService.getFinanceRejectPoints(SHORTFORM);
//
//        mService.enqueue(new Callback<SellerDTO>() {
//            @Override
//            public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//                try {
//                    SellerDTO mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//                    Log.e("ordersstatus", "" + status);
//                    if (Integer.parseInt(status) == 1) {
//                        List<SellersOrdersListDTO> sellersList = mOrderObject.getSellersOrdersListDTO();
//                        if (sellersList != null) {
//                            if (sellersList.size() > 0) {
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                for (int i = 0; i < sellersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String id = sellersList.get(i).getId();
//                                    String rejectpoint = sellersList.get(i).getRej_point();
//                                    hashMap.put("id", id);
//                                    hashMap.put("rejectpoint", rejectpoint);
//
//                                    Log.e("id", id);
//                                    Log.e("rejectpoint", rejectpoint);
//                                    hashmapList.add(hashMap);
//                                }
//                                    rejectPopup(hashmapList);
//                            } else {
//                            }
//                        }
//                    } else {
//                        String message = mOrderObject.getMessage();
//                    }
//                } catch (Exception e) {
//                }
//            }
//            @Override
//            public void onFailure(Call<SellerDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void rejectPopup(ArrayList<HashMap<String, String>> list) {
//        /** Used for Show Disclaimer Pop up screen */
//
//        REASONVAL="";
//        rejectalertDialog = new Dialog(FinanceOrderDetailsActivity.this);
//        rejectalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        rejectalertDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
//        LayoutInflater inflater = getLayoutInflater();
//        View rejectlayout = inflater.inflate(R.layout.layout_financerejpopup, null);
//        TextView reason_cancel = rejectlayout.findViewById(R.id.reason_reject);
//        reason_cancel.setText("Reason for reject");
//        rejectalertDialog.setContentView(rejectlayout);
//        rejectalertDialog.setCancelable(true);
//        if (!rejectalertDialog.isShowing()) {
//            rejectalertDialog.show();
//        }
//        EditText rejectorder_txt = rejectlayout.findViewById(R.id.rejectorder_txt);
//        rejectorder_txt.setVisibility(View.GONE);
//
//        RadioGroup rgp = rejectlayout.findViewById(R.id.radiogroup);
//        RadioGroup.LayoutParams rprms;
//        for (int i = 0; i < list.size(); i++) {
//
//            String id = list.get(i).get("id");
//            String rejectpoint = list.get(i).get("rejectpoint");
//            Log.e("id", " :" + id);
//            Log.e("rejectpoint", " :" + rejectpoint);
//            RadioButton rdbtn = new RadioButton(mContext);
//            rdbtn.setId(Integer.parseInt(id));
//            rdbtn.setText(rejectpoint);
//            rdbtn.setTextColor(Color.BLACK);
////                rdbtn.setTextColor(getResources().getColor(R.color.black));
//            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            rgp.addView(rdbtn, rprms);
//        }
//
//        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("CheckedId", " :" + checkedId);
//                REASONVAL = String.valueOf(checkedId);
//            }
//        });
//
//        EditText proof_path = rejectlayout.findViewById(R.id.proof_path);
//        proof_path.setVisibility(View.GONE);
//        Button attach_proof = rejectlayout.findViewById(R.id.attach_proof);
//        attach_proof.setVisibility(View.GONE);
//
//        ImageView closeicon = rejectlayout.findViewById(R.id.closeicon);
//        closeicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rejectalertDialog.dismiss();
//            }
//        });
//        TextView submittxt = rejectlayout.findViewById(R.id.submittxt);
//        submittxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(REASONVAL.equals(""))
//                {
//                    Toast.makeText(getApplicationContext(),"Please select reason",Toast.LENGTH_SHORT).show();
//                }else{
//                    submitRejectionPoints("2");
//                }
//            }
//        });
//    }
//
//    private void submitRejectionPoints(String status) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService=null;
//
//        if(SHORTFORM.equals("FM")) {
//           mService = mApiService.submitRejectPoints(PRIMARYID, SHORTFORM, "", status, ID, REASONVAL);
//            mService.enqueue(new Callback<Login>() {
//                @Override
//                public void onResponse(Call<Login> call, Response<Login> response) {
//                    dialog.dismiss();
//                    Log.e("response", "" + response);
//                    try {
//                        Login mOrderObject = response.body();
//                        String status = mOrderObject.getStatus();
//                        if (Integer.parseInt(status) == 1) {
//                            String message = mOrderObject.getMessage();
//
//                            if(SHORTFORM.equals("FM")) {
//                               // globalShare.setFinancemenuselectpos("7");
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                            /*    if (FinanceDeptMenuScreenActivity.mainfinish != null) {
//                                    FinanceDeptMenuScreenActivity.mainfinish.finish();
//                                }
//                                Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                startActivity(intent);*/
//                            if(FinanceOrdersTabActivity.mainfinish!=null)
//                            {
//                                FinanceOrdersTabActivity.mainfinish.finish();
//                            }
//                            Intent intent=new Intent(getApplicationContext(),FinanceOrdersTabActivity.class);
//                            startActivity(intent);
//                            }else if(SHORTFORM.equals("ADMIN")) {
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                if (AdminOrdersTabActivity.mainfinish != null) {
//                                    AdminOrdersTabActivity.mainfinish.finish();
//                                }
//                                Intent intent = new Intent(getApplicationContext(), AdminOrdersTabActivity.class);
//                                startActivity(intent);
//                            }
//                            finish();
//                        } else {
//                            String message = mOrderObject.getMessage();
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//                @Override
//                public void onFailure(Call<Login> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }else if(SHORTFORM.equals("ADMIN")) {
//            mService = mApiService.submitAdminRejectPoints(PRIMARYID, SHORTFORM, "", status, ID, REASONVAL);
//            mService.enqueue(new Callback<Login>() {
//                @Override
//                public void onResponse(Call<Login> call, Response<Login> response) {
//                    dialog.dismiss();
//                    Log.e("response", "" + response);
//                    try {
//                        Login mOrderObject = response.body();
//                        String status = mOrderObject.getStatus();
//                        if (Integer.parseInt(status) == 1) {
//                            String message = mOrderObject.getMessage();
//
//                            if(SHORTFORM.equals("FM")) {
//                                globalShare.setFinancemenuselectpos("7");
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                                if (FinanceDeptMenuScreenActivity.mainfinish != null) {
////                                    FinanceDeptMenuScreenActivity.mainfinish.finish();
////                                }
////                                Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
////                                startActivity(intent);
//
//                                if(FinanceOrdersTabActivity.mainfinish!=null)
//                                {
//                                    FinanceOrdersTabActivity.mainfinish.finish();
//                                }
//                                Intent intent=new Intent(getApplicationContext(),FinanceOrdersTabActivity.class);
//                                startActivity(intent);
//                            }else if(SHORTFORM.equals("ADMIN")) {
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                if (AdminOrdersTabActivity.mainfinish != null) {
//                                    AdminOrdersTabActivity.mainfinish.finish();
//                                }
//                                Intent intent = new Intent(getApplicationContext(), AdminOrdersTabActivity.class);
//                                startActivity(intent);
//                            }
//                            finish();
//                        } else {
//                            String message = mOrderObject.getMessage();
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//                @Override
//                public void onFailure(Call<Login> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//    }
//}

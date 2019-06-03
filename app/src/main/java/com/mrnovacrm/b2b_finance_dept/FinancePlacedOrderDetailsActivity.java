package com.mrnovacrm.b2b_finance_dept;

//public class FinancePlacedOrderDetailsActivity {
//}

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.b2b_admin.AdminOrdersTabActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchDetailsDTO;
import com.mrnovacrm.model.BranchesDTO;
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

public class FinancePlacedOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String DELIVERY_ADDRESS = "";
    private String USERNAME = "";
    private String MOBILE = "";
    private String SHORTFORM = "";
    private String COMPANY = "";
    private String BRANCHID = "";
    private double LATITUDE_VALUE, LANGITUDE_VALUE;
    String addressid = "";

    TextView ordertakenbytitle,noofitemstxt, totalamounttxt, order_numtxt, mobilenumbertxt, deliveryaddresstxt,
            totalamounbt_txt, deliverychargestxt, deliverybytxt, estimatedtime, servicechargestxt;
    RecyclerView recyclerView, transfer_recyclerview;
    //  Button donebtn;
    Context context;
    private String order_key = "";
    GlobalShare globalShare;
    public static Activity mainfinish;
    private Button btn_cancel, btn_approved, btn_transferred;
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
    ArrayList<HashMap<String, String>> transferredHashMapList = new ArrayList<HashMap<String, String>>();
    private Dialog transferAlertDialog;
    private CheckBox allcheckbox;

    String[] transferItemValueArray;
    String[] transferIdsArray;
    String[] transferQtyValueArray;
    String[] transferCheckboxValueArray;

    String[] _transferItemValueArray;
    String[] _transferIdsArray;
    String[] _transferQtyValueArray;
    String[] _transferCheckboxValueArray;

    private int smoothscrollpositon;
    HashMap<String, String> qtyHashmap = new HashMap<>();
    private TransferredListAdapter transferredListAdapter;

    boolean allItemsChecked;

    ArrayList<String> branchNamesList = new ArrayList<>();
    ArrayList<String> branchIdsList = new ArrayList<>();
    Spinner transferto_spinner;
    private String BRANCH_ID="";
    private String forward_by="";
    LinearLayout remarkslinear;
    TextView remarks_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_financeplacedordersummarydetails);
        context = this;
        mContext = this;

        globalShare = (GlobalShare) getApplicationContext();
        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        actionTextView.setText("Order details");
        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(FinancePlacedOrderDetailsActivity.this);

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
            BRANCHID = values.get(SharedDB.BRANCHID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            COMPANY = values.get(SharedDB.COMPANY);

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
        btn_approved.setOnClickListener(FinancePlacedOrderDetailsActivity.this);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(FinancePlacedOrderDetailsActivity.this);

        btn_transferred = findViewById(R.id.btn_transferred);
        btn_transferred.setOnClickListener(FinancePlacedOrderDetailsActivity.this);

        //   order_numtxt.setText("Order No. "+ORDERID);

        paymenttype_txt = findViewById(R.id.paymenttype_txt);
        creditdatelinear = findViewById(R.id.creditdatelinear);
        creditdate_txt = findViewById(R.id.creditdate_txt);
        chequenumberlinear = findViewById(R.id.chequenumberlinear);
        chequenumber_txt = findViewById(R.id.chequenumber_txt);
        chequedatelinear = findViewById(R.id.chequedatelinear);
        chequedate_txt = findViewById(R.id.chequedate_txt);
        ordertakenby_txt = findViewById(R.id.ordertakenby_txt);

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
                if(!forward_by.equals(""))
                {
                    if(!forward_by.equals("0"))
                    {
                        getRejectPoints();
                    }else{
                        Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_approved:
                if(!forward_by.equals(""))
                {
                    if(!forward_by.equals("0"))
                    {
                        REASONVAL = "";
                        submitRejectionPoints("1");
                    }else{
                        Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_transferred:
                if(!forward_by.equals(""))
                {
                    if(!forward_by.equals("0"))
                    {
                        if (transferredHashMapList != null) {
                            if (transferredHashMapList.size() > 0) {
                                boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(FinancePlacedOrderDetailsActivity.this);
                                if(isConnectedToInternet)
                                {
                                    showTransferredPopup();
                                }else{
                                    Toast.makeText(getApplicationContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Pending approval from sales executive",Toast.LENGTH_SHORT).show();
                }
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
        alertDialog = new Dialog(FinancePlacedOrderDetailsActivity.this);
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
        closeicon.setOnClickListener(FinancePlacedOrderDetailsActivity.this);
        submittxt.setOnClickListener(FinancePlacedOrderDetailsActivity.this);

        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
        linear_edittext.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
        radioGroup.setVisibility(View.GONE);
    }

    private void cancelOrder(String reason) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinancePlacedOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("ID", "123 :" + ID);
        Call<Login> mService = mApiService.cancelOrder(ID, reason);

        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Login mObject = response.body();
                    Log.e("mOrderObject", "" + mObject);

                    String status = mObject.getStatus();
                    Log.e("status", "" + status);
                    String message = mObject.getMessage();
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
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinancePlacedOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        //  Call<Order> mService = mApiService.getOrderDetails(order_key);

        Call<Order> mService = mApiService.getOrderDetails(ID,SHORTFORM);
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



                        forward_by = mOrderObject.getForward_by();
                        if(parent_order.equals("0"))
                        {
                            ordertakenbytitle.setText("Order taken by");
                        }else{
                            ordertakenbytitle.setText("Order transferred by");
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

                                transferItemValueArray = new String[productsList.size()];
                                transferQtyValueArray = new String[productsList.size()];
                                transferCheckboxValueArray = new String[productsList.size()];
                                transferIdsArray = new String[productsList.size()];

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                String[] remQtyValueArray = new String[productsList.size()];
                                String[] checkboxValueArray = new String[productsList.size()];
                                String[] idValueArray = new String[productsList.size()];
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
                                    String single_qty = productsList.get(i).getSingle_qty();

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
                                    hashMap.put("single_qty", single_qty);

                                    remQtyValueArray[i] = qty;
                                    checkboxValueArray[i] = "0";
                                    idValueArray[i] = id;

                                    transferIdsArray[i] = id;
                                    transferItemValueArray[i] = itemname;
                                    transferQtyValueArray[i] = single_qty;
                                    transferCheckboxValueArray[i] = "0";
                                    transferredHashMapList.add(hashMap);
                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
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

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        CartListAdapter adapter = new CartListAdapter(getApplicationContext(), hashmapList);
        recyclerView.setAdapter(adapter);
    }

    public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersDetailsList;

        public CartListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            this.ordersDetailsList = hashmapList;
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
        rejectalertDialog = new Dialog(FinancePlacedOrderDetailsActivity.this);
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

    public void showTransferredPopup() {
        transferAlertDialog = new Dialog(FinancePlacedOrderDetailsActivity.this);
        transferAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        transferAlertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        View transferlayout = inflater.inflate(R.layout.layout_transferredorderpopup, null);
        TextView reason_cancel = transferlayout.findViewById(R.id.reason_reject);
        reason_cancel.setText("Transfer order items");
        transferAlertDialog.setContentView(transferlayout);
        transferAlertDialog.setCancelable(true);
        if (!transferAlertDialog.isShowing()) {
            transferAlertDialog.show();
        }

        allcheckbox = transferlayout.findViewById(R.id.allcheckbox);
        transferto_spinner = transferlayout.findViewById(R.id.transferto_spinner);

        transfer_recyclerview = transferlayout.findViewById(R.id.transfer_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        transfer_recyclerview.setHasFixedSize(true);
        transfer_recyclerview.setLayoutManager(mLayoutManager);

        ImageView closeicon = transferlayout.findViewById(R.id.closeicon);
        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferAlertDialog.dismiss();
            }
        });

        TextView submittxt = transferlayout.findViewById(R.id.submittxt);
        submittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> selectedHashMap=new HashMap<String,String>();
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < _transferQtyValueArray.length; i++) {
                    String remqty = _transferQtyValueArray[i];
                    String checkqty = _transferCheckboxValueArray[i];
                   String valqty = _transferIdsArray[i];
                    if (checkqty.equals("1")) {
                        selectedHashMap.put(valqty,remqty);
                        sb1.append(valqty);
                        sb1.append("~");
                        sb1.append(remqty);
                        sb1.append(",");
                    }
                }
                if (sb1 == null || sb1.toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "No Item Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(" sb1.toString()", sb1.toString());
                    if(BRANCH_ID.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please select branch",Toast.LENGTH_SHORT).show();
                    }else {
                        if (selectedHashMap != null) {
                            if (selectedHashMap.size() > 0) {
                                submittransferorder(selectedHashMap);
                            }
                        }
                    }
                }
            }
        });

        allcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                selectdata(isChecked);
            }
        });
        transferredListAdapter = new TransferredListAdapter(getApplicationContext(), transferItemValueArray, transferQtyValueArray,
                transferCheckboxValueArray, transferIdsArray);
        transfer_recyclerview.setAdapter(transferredListAdapter);

        hinttranferBraches();
      //  getBranches();


        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(FinancePlacedOrderDetailsActivity.this);
        if(isConnectedToInternet)
        {
            getBranches();
        }else{
            Toast.makeText(getApplicationContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
        }

    }

    public void hinttranferBraches() {
        branchNamesList.clear();
        branchNamesList.add("Select Transfer branch");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, branchNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transferto_spinner.setAdapter(spinnerClass);
        transferto_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    BRANCH_ID = "";
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void loadBranches()
    {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, branchNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transferto_spinner.setAdapter(spinnerClass);
        transferto_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    BRANCH_ID = "";
                } else {
                    BRANCH_ID=branchIdsList.get(position-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void getBranches()
    {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<BranchesDTO> mService = null;
        mService = mApiService.getPlacedOrderBraches(COMPANY,BRANCHID);
        mService.enqueue(new Callback<BranchesDTO>() {
            @Override
            public void onResponse(Call<BranchesDTO> call, retrofit2.Response<BranchesDTO> response) {
                Log.e("response", "" + response);
                BranchesDTO branchesDTO = response.body();
                dialog.dismiss();
                try {
                    String status = branchesDTO.getStatus();
                    if (status.equals("1")) {
                        List<BranchDetailsDTO> branchDetailsDTOS = branchesDTO.getBranchDetailsDTOS();

                        if (branchDetailsDTOS != null) {
                            if (branchDetailsDTOS.size() > 0) {
                                branchNamesList.clear();
                                branchNamesList.add("Select transfer branch");
                                branchIdsList.clear();
                                ArrayList<HashMap<String, String>> hashMapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < branchDetailsDTOS.size(); i++) {
                                    HashMap<String, String> hashmap = new HashMap<>();
                                    String id = branchDetailsDTOS.get(i).getId();
                                    String name = branchDetailsDTOS.get(i).getName();
                                    String color = branchDetailsDTOS.get(i).getColor();
                                    String company = branchDetailsDTOS.get(i).getCompany();

                                    hashmap.put("id", id);
                                    hashmap.put("name", name);
                                    hashmap.put("color", color);
                                    hashmap.put("company", company);

                                    branchNamesList.add(name);
                                    branchIdsList.add(id);

                                    hashMapList.add(hashmap);
                                }
                                loadBranches();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<BranchesDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submittransferorder(HashMap<String,String> selectedHashMap)
    {
        Gson gson = new Gson();
        String gsonhashmap = gson.toJson(selectedHashMap);
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinancePlacedOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.submitTransferOrder(PRIMARYID,ID,BRANCH_ID,gsonhashmap);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                Login mLoginObject = response.body();
                Log.e("response", " :" + response);
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    String message = mLoginObject.getMessage();
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        if (FinanceOrdersTabActivity.mainfinish != null) {
                            FinanceOrdersTabActivity.mainfinish.finish();
                        }
                        transferAlertDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), FinanceOrdersTabActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectdata(boolean isChecked) {
        Log.e("allItemsChecked",""+allItemsChecked);
        if (isChecked) {
            allItemsChecked=true;
            if (_transferCheckboxValueArray != null) {
                if (_transferCheckboxValueArray.length > 0) {
                    for (int i = 0; i < _transferCheckboxValueArray.length; i++) {
                        _transferCheckboxValueArray[i] = "1";
                        String transferID = _transferIdsArray[i];
                        String qtyval = _transferQtyValueArray[i];
                        qtyHashmap.put(transferID, qtyval);
                    }
                    transfer_recyclerview.setAdapter(null);
                    transferredListAdapter.notifyDataSetChanged();

                    transferredListAdapter = new TransferredListAdapter(getApplicationContext(), _transferItemValueArray,
                            _transferQtyValueArray,
                            _transferCheckboxValueArray, _transferIdsArray);
                    transfer_recyclerview.setAdapter(transferredListAdapter);
                }
            }
        } else {
            if (allItemsChecked == false) {
            } else {
                if (_transferCheckboxValueArray != null) {
                    if (_transferCheckboxValueArray.length > 0) {
                        for (int i = 0; i < _transferCheckboxValueArray.length; i++) {
                            try {
                                _transferCheckboxValueArray[i] = "0";
                                String transferID = _transferIdsArray[i];
                                String qtyval = _transferQtyValueArray[i];
                                if (qtyHashmap != null) {
                                    if (qtyHashmap.size() > 0) {
                                        if (qtyHashmap.containsKey(transferID)) {
                                            qtyHashmap.remove(transferID);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                        transfer_recyclerview.setAdapter(null);
                        transferredListAdapter.notifyDataSetChanged();
                        transferredListAdapter = new TransferredListAdapter(getApplicationContext(), transferItemValueArray, transferQtyValueArray,
                                transferCheckboxValueArray, transferIdsArray);
                        transfer_recyclerview.setAdapter(transferredListAdapter);
                    }
                }
            }
        }
    }

    public class TransferredListAdapter extends RecyclerView.Adapter<TransferredListAdapter.MyViewHolder> {
        private Context mContext;
        String[] ordersDetailsList;
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        String[] transfer_QtyValueArray;
        String[] transfer_CheckboxValueArray;
        String[] transfer_IDValueArray;

        public TransferredListAdapter(Context mContext, String[] ordersDetailsList, String[] transfer_QtyValueArray,
                                      String[] transfer_CheckboxValueArray, String[] transfer_IDValueArray) {
            this.mContext = mContext;
            this.ordersDetailsList = ordersDetailsList;
            this.transfer_QtyValueArray = transfer_QtyValueArray;
            this.transfer_CheckboxValueArray = transfer_CheckboxValueArray;
            this.transfer_IDValueArray = transfer_IDValueArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_transferredlistadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            //  String id = ordersDetailsList.get(position).get("id");
            String itemname = ordersDetailsList[position];
            String single_qty = transfer_QtyValueArray[position];

            holder.product_name.setText(itemname);
            holder.qtyedittext.setText(single_qty);

            holder.indexReference = position;

            _transferIdsArray = transfer_IDValueArray;
            _transferItemValueArray = ordersDetailsList;
            _transferCheckboxValueArray = transfer_CheckboxValueArray;
            _transferQtyValueArray = transfer_QtyValueArray;

            if (transfer_CheckboxValueArray[holder.indexReference].equals("1")) {
                holder.itemcheckbox.setChecked(true);
            } else if (transfer_CheckboxValueArray[holder.indexReference].equals("0")) {
                holder.itemcheckbox.setChecked(false);
            }

            holder.itemcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    smoothscrollpositon = holder.indexReference;
                    if (isChecked) {
                        transfer_CheckboxValueArray[holder.indexReference] = "1";
                        String qtyval = holder.qtyedittext.getText().toString().trim();

                        if (qtyval != null || !qtyval.equals("")) {
                            qtyHashmap.put(transfer_IDValueArray[holder.indexReference], qtyval);
                        }
                    } else {
                        qtyHashmap.remove(transfer_IDValueArray[holder.indexReference]);
                        transfer_CheckboxValueArray[holder.indexReference] = "0";

                        if (allcheckbox.isChecked()) {
                            allItemsChecked = false;
                        }
                        allcheckbox.setChecked(false);
                    }
                }
            });

            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String qtyval = holder.qtyedittext.getText().toString().trim();
                    if (qtyval != null || !qtyval.equals("")) {
                        if (holder.itemcheckbox.isChecked()) {
                            String id = transfer_IDValueArray[holder.indexReference];
                            qtyHashmap.put(id, qtyval);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    transfer_QtyValueArray[holder.indexReference] = editable.toString();
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersDetailsList.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CheckBox itemcheckbox;
            EditText qtyedittext;
            ImageView product_image, cart_img;
            TextView product_name, productcal, totlproductcnt;
            int indexReference;

            MyViewHolder(View view) {
                super(view);
                itemcheckbox = view.findViewById(R.id.itemcheckbox);
                product_name = view.findViewById(R.id.product_name);
                qtyedittext = view.findViewById(R.id.qtyedittext);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }
    }

}
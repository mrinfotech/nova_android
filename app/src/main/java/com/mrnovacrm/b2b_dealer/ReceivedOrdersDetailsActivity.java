package com.mrnovacrm.b2b_dealer;

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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.mrnovacrm.activity.FilePath;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.SellersOrdersListDTO;
import com.squareup.picasso.Picasso;

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
 * Created by android on 07-03-2018.
 */

public class ReceivedOrdersDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView, issuerecyclerview;
    Button btn_cancel;
    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private ImageView closeicon;
    EditText cancelorder_txt;
    String ID, ORDERID, reason;
    TextView items, units, savings, s_charge, pay, text_nodata;
    RelativeLayout rel_footer;
    private GlobalShare globalShare;
    private TextView subtotalpayval;
    private String STATUSVALUE = "";
    private Dialog rejectalertDialog;
    EditText proof_path;
    private String REASON = "";
    private String base64;
    private String selectedFilePath;
    private TransparentProgressDialog dialog;
    private static final int FileSelectId = 3;
    //  private String SERVER_URL = SharedDB.URL + "stores/do_action";
    private String SERVER_URL = SharedDB.URL + "dealerorders/do_action";
    private String PRIMARYID;
    private String SHORTFORM;
    private int STORAGE_PERMISSION_CODE = 23;
    TextView paymenttype;
    TextView creditdate, issueitemstxt;
    private EditText reason_edtxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_receivedorderdetails);
        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERID = getIntent().getExtras().getString("orderId");
            STATUSVALUE = getIntent().getExtras().getString("status");
        }
        globalShare = (GlobalShare) getApplicationContext();
        View includedLayout = findViewById(R.id.include_actionbar);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        actionbarheadertxt.setText("OrderID: " + ORDERID);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        backimg.setOnClickListener(ReceivedOrdersDetailsActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        items = findViewById(R.id.items);
        units = findViewById(R.id.units);
        savings = findViewById(R.id.savings);
        s_charge = findViewById(R.id.s_charge);
        pay = findViewById(R.id.pay);
        subtotalpayval = findViewById(R.id.subtotalpayval);
        rel_footer = findViewById(R.id.rel_footer);
        text_nodata = findViewById(R.id.text_nodata);
        btn_cancel = findViewById(R.id.btn_cancel);
        paymenttype = findViewById(R.id.paymenttype);
        creditdate = findViewById(R.id.creditdate);

        issuerecyclerview = (RecyclerView) findViewById(R.id.issuerecyclerview);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        issuerecyclerview.setHasFixedSize(true);
        issuerecyclerview.setLayoutManager(mLayoutManager1);

        issueitemstxt = findViewById(R.id.issueitemstxt);

        btn_cancel.setVisibility(View.GONE);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        requestStoragePermission();
        getOrderListDetails();
    }

    private void getOrderListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(ReceivedOrdersDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getReceivedOrderFinalDetails(ID,SHORTFORM);
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
                        String total_items = mOrderObject.getTotal_items();
                        items.setText(total_items);

                        String total_units = mOrderObject.getTotal_units();
                        units.setText(total_units);
                        String total_savings = mOrderObject.getTotal_savings();
                        savings.setText(getResources().getString(R.string.Rs) + " " + total_savings);
                        String total_sur_charge = mOrderObject.getTotal_sur_charge();

                        String total_pay = mOrderObject.getTotal_pay();
                        pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);
                        String sub_total = mOrderObject.getSub_total();
                        subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_total);

                        String delivery_charges = mOrderObject.getDelivery_charges();
                        double charges = Double.parseDouble(delivery_charges) + Double.parseDouble(total_sur_charge);

                        String paymenttypeval = mOrderObject.getPayment_type();
                        String credit_dateval = mOrderObject.getCredit_date();
                        paymenttype.setText(paymenttypeval);
                        creditdate.setText(credit_dateval);

                        s_charge.setText(String.valueOf(charges));

                        recyclerView.setVisibility(View.VISIBLE);
                        List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                String[] item_statusValueArray = new String[productsList.size()];
                                String[] userValueArray = new String[productsList.size()];
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
                                    String action_status = productsList.get(i).getAction_status();
                                    String picked_qty = productsList.get(i).getPicked_qty();
                                    String delivered_qty = productsList.get(i).getDelivered_qty();


                                    List<String> images = productsList.get(i).getImagesList();
                                    item_statusValueArray[i] = action_status;
                                    userValueArray[i] = delivered_qty;

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
                                    hashMap.put("action_status", action_status);
                                    hashMap.put("picked_qty", picked_qty);
                                    hashMap.put("delivered_qty", delivered_qty);

                                    hashMapImages.put(id, images);
                                    hashmapImagesList.add(hashMapImages);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList, hashmapImagesList, item_statusValueArray, userValueArray);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                            }
                        }

                        List<OrdersListDTO> rejectordersListDTOS = mOrderObject.getRejectordersListDTOS();
                        if (rejectordersListDTOS != null) {
                            if (rejectordersListDTOS.size() > 0) {
                                String[] item_statusValueArray = new String[rejectordersListDTOS.size()];
                                String[] userValueArray = new String[rejectordersListDTOS.size()];

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                for (int i = 0; i < rejectordersListDTOS.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                    String id = rejectordersListDTOS.get(i).getId();
                                    String itemid = rejectordersListDTOS.get(i).getItemid();
                                    String sellerid = rejectordersListDTOS.get(i).getSellerid();
                                    String orderid = rejectordersListDTOS.get(i).getOrderid();
                                    String mrp = rejectordersListDTOS.get(i).getMrp();
                                    String qty = rejectordersListDTOS.get(i).getQty();
                                    String service_charge = rejectordersListDTOS.get(i).getService_charge();
                                    String amount = rejectordersListDTOS.get(i).getAmount();
                                    String created_on = rejectordersListDTOS.get(i).getCreated_on();
                                    String seller = rejectordersListDTOS.get(i).getSeller();
                                    String itemname = rejectordersListDTOS.get(i).getItemname();
                                    String brand = rejectordersListDTOS.get(i).getBrand();
                                    String sellingprice = rejectordersListDTOS.get(i).getSellingprice();
                                    String total_price = rejectordersListDTOS.get(i).getTotal_price();
                                    String margin = rejectordersListDTOS.get(i).getMargin();
                                    String discount = rejectordersListDTOS.get(i).getDiscount();
                                    String action_status = rejectordersListDTOS.get(i).getAction_status();
                                    String picked_qty = productsList.get(i).getPicked_qty();

                                    List<String> images = rejectordersListDTOS.get(i).getImagesList();
                                    item_statusValueArray[i] = action_status;
                                    userValueArray[i] = qty;

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
                                    hashMap.put("action_status", action_status);
                                    hashMap.put("picked_qty", picked_qty);

                                    hashMapImages.put(id, images);
                                    hashmapImagesList.add(hashMapImages);

                                    hashmapList.add(hashMap);
                                }
                                showRejectedOrdersData(hashmapList, hashmapImagesList, item_statusValueArray, userValueArray);
                            } else {
                                issuerecyclerview.setVisibility(View.GONE);
                                issueitemstxt.setText(View.GONE);
                            }
                        } else {
                            issuerecyclerview.setVisibility(View.GONE);
                            issueitemstxt.setText(View.GONE);
                        }


                    } else {
                        recyclerView.setVisibility(View.GONE);
                        rel_footer.setVisibility(View.GONE);
                        text_nodata.setVisibility(View.VISIBLE);
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

    public void showRejectedOrdersData(ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String,
            List<String>>> hashmapImagesList, String[] item_statusValueArray, String[] userValueArray) {
        if (hashmapList.size() > 0) {
            issueitemstxt.setVisibility(View.VISIBLE);
            issuerecyclerview.setVisibility(View.VISIBLE);
            IssueOrderAdapter adapter = new IssueOrderAdapter(getApplicationContext(), hashmapList, hashmapImagesList,
                    item_statusValueArray, userValueArray);
            issuerecyclerview.setAdapter(adapter);
        } else {
            issueitemstxt.setVisibility(View.GONE);
            issuerecyclerview.setVisibility(View.GONE);
        }
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList, ArrayList<HashMap<String,
            List<String>>> hashmapImagesList, String[] item_statusValueArray, String[] userValueArray) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            CancelOrderAdapter adapter = new CancelOrderAdapter(getApplicationContext(), hashmapList, hashmapImagesList,
                    item_statusValueArray, userValueArray);
            recyclerView.setAdapter(adapter);
            rel_footer.setVisibility(View.VISIBLE);
            text_nodata.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            rel_footer.setVisibility(View.GONE);
            text_nodata.setVisibility(View.VISIBLE);
        }
    }

    public void showCancelOrderPopup() {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(ReceivedOrdersDetailsActivity.this);
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
        closeicon.setOnClickListener(ReceivedOrdersDetailsActivity.this);
        submittxt.setOnClickListener(ReceivedOrdersDetailsActivity.this);

        LinearLayout linear_edittext = layout.findViewById(R.id.linear_edittext);
        linear_edittext.setVisibility(View.VISIBLE);

        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
        radioGroup.setVisibility(View.GONE);
    }

    private void cancelOrder(String reason) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(ReceivedOrdersDetailsActivity.this);
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
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

    public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersDetailsList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        String[] item_statusValueArray;
        String[] userValueArray;

        public CancelOrderAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                                  ArrayList<HashMap<String, List<String>>> hashmapImagesList,
                                  String[] item_statusValueArray, String[] userValueArray) {
            ordersDetailsList = hashmapList;
            this.mContext = mContext;
            this.hashmapImagesList = hashmapImagesList;
            this.item_statusValueArray = item_statusValueArray;
            this.userValueArray = userValueArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.receiveorder_cardview, parent, false);
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receiveorder_cardviewenew, parent, false);
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
            String action_status = ordersDetailsList.get(position).get("action_status");
            String picked_qty = ordersDetailsList.get(position).get("picked_qty");
            String delivered_qty = ordersDetailsList.get(position).get("delivered_qty");

            holder.product_name.setText(itemname);
            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);
            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + amount);
            //holder.qty.setText(qty);
            holder.total_cost.setText(mContext.getResources().getString(R.string.Rs) + " " + total_price);
            holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
            holder.product_margin.setText(mContext.getResources().getString(R.string.Rs) + " " + margin);
            holder.qtyedittext.setText(qty);

            holder.orderedqtytxt.setText(qty);
            holder.indexReference = position;

            List<String> imagesList = hashmapImagesList.get(position).get(id);
            if (imagesList != null) {
                if (imagesList.size() > 0) {
                    Picasso.with(mContext)
                            .load(imagesList.get(0))
                            .placeholder(R.drawable.loading)
                            .into(holder.product_image);
                } else {
                    holder.product_image.setImageResource(R.drawable.noimage);
                }
            } else {
                holder.product_image.setImageResource(R.drawable.noimage);
            }

            holder.product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = ordersDetailsList.get(position).get("id");
                    List<String> imagesList = hashmapImagesList.get(position).get(id);

                    if (imagesList != null) {

                        if (imagesList.size() > 1) {
                            List<String> productimagesList = null;
                            ArrayList<String> imglist = new ArrayList<>();
                            for (int i = 1; i < imagesList.size(); i++) {
                                // productimagesList.add(imagesList.get(i));
                                imglist.add(imagesList.get(i));
                            }
                            productimagesList = imglist;
                            if (productimagesList != null) {
                                if (productimagesList.size() > 0) {
                                    String itemname = ordersDetailsList.get(position).get("itemname");
                                    // globalShare.setImagesList(imagesList);
                                    globalShare.setImagesList(productimagesList);
                                    Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
                                    intent.putExtra("itemname", itemname);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            //    Toast.makeText(getActivity(),"No images found",Toast.LENGTH_SHORT).show();
                        }

//                        if (imagesList.size() > 0) {
//                            String itemname = ordersDetailsList.get(position).get("itemname");
//                            globalShare.setImagesList(imagesList);
//                            Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
//                            intent.putExtra("itemname", itemname);
//                            startActivity(intent);
//                        } else {
//                        }
                    } else {
                    }
                }
            });

            if (item_statusValueArray[holder.indexReference].equals("1")
                    || item_statusValueArray[holder.indexReference].equals("2")) {
                holder.receive_btn.setVisibility(View.GONE);
                holder.reject_btn.setVisibility(View.GONE);
                holder.receivelinear.setVisibility(View.GONE);
                holder.rejectlinear.setVisibility(View.GONE);
                holder.statustxt.setVisibility(View.VISIBLE);

                if (item_statusValueArray[holder.indexReference].equals("1")) {
                    holder.statustxt.setText("Received");
                } else {
                    holder.statustxt.setText("Rejected");
                }
                holder.qtyedittext.setClickable(false);
                holder.qtyedittext.setCursorVisible(false);

                holder.qtylinear.setVisibility(View.GONE);
                holder.totalqtylinear.setVisibility(View.VISIBLE);

                holder.qty.setText(picked_qty);
            } else {

                holder.qtylinear.setVisibility(View.VISIBLE);
                holder.totalqtylinear.setVisibility(View.GONE);

                holder.qtyedittext.setClickable(true);
                holder.qtyedittext.setCursorVisible(true);

                holder.receive_btn.setVisibility(View.VISIBLE);
                holder.reject_btn.setVisibility(View.VISIBLE);
                holder.receivelinear.setVisibility(View.VISIBLE);
                holder.rejectlinear.setVisibility(View.VISIBLE);
                holder.statustxt.setVisibility(View.GONE);
            }
            holder.qtyedittext.setText(userValueArray[holder.indexReference]);

            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //  valueList[pos] = s.toString()+"~"+data.get(pos).get("attrId");
                }
                @Override
                public void afterTextChanged(Editable s) {
                    userValueArray[holder.indexReference] = s.toString();

//                    final String itemId = ordersDetailsList.get(position).get("id");
//                    String qty = ordersDetailsList.get(position).get("qty");
//                    String qtyedittext = holder.qtyedittext.getText().toString().trim();
//                    int qtyval=Integer.parseInt(qty);
//                    int editqtyval=Integer.parseInt(qtyedittext);
//
//                    if(editqtyval>0) {
//                        if (qtyval == editqtyval) {
//                            sendPickedItem("1", ID, PRIMARYID, "DEALER", itemId,
//                                    holder.qtyedittext.getText().toString(), position, holder, "", item_statusValueArray, "");
//                        } else {
//                            if (qtyval < editqtyval) {
//                                Toast.makeText(getApplicationContext(), "Pick qty exceeds the qty", Toast.LENGTH_SHORT).show();
//                            } else {
////                                approvePopup(position, itemId, holder.qtyedittext.getText().toString(),
////                                        holder,item_statusValueArray);
//                                getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, item_statusValueArray, "frompicker", itemId);
//                            }
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

            holder.reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final String itemId = ordersDetailsList.get(position).get("id");
                        String qty = ordersDetailsList.get(position).get("qty");
                        getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, item_statusValueArray,
                                "fromreject", itemId);
                    } catch (Exception e) {
                    }
                }
            });

            holder.receive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final String itemId = ordersDetailsList.get(position).get("id");
                        String delivered_qty = ordersDetailsList.get(position).get("delivered_qty");

                        String qtyedittext = holder.qtyedittext.getText().toString().trim();
                        if(!qtyedittext.equals(""))
                        {
                            long qtyval=Long.parseLong(delivered_qty);
                            long editqtyval=Long.parseLong(qtyedittext);
                            if(editqtyval>0) {
                                if (qtyval == editqtyval) {
                                    sendPickedItem("1", ID, PRIMARYID, SHORTFORM, itemId,
                                            holder.qtyedittext.getText().toString(), position, holder, "", item_statusValueArray, "");
                                } else {
                                    if (qtyval < editqtyval) {
                                        Toast.makeText(getApplicationContext(), "Receive qty exceeds the qty", Toast.LENGTH_SHORT).show();
                                    } else {
                                        getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, item_statusValueArray, "fromreceive", itemId);
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersDetailsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView product_image;
            EditText qtyedittext;
            TextView product_name, product_mrp, product_pay, product_discount, product_margin,
                    product_instock, qty, total_cost, statustxt,orderedqtytxt;
            int indexReference;
            LinearLayout rejectlinear, receivelinear,totalqtylinear,qtylinear;
            Button reject_btn, receive_btn;


            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_pay = view.findViewById(R.id.product_pay);
                product_discount = view.findViewById(R.id.product_discount);
                product_margin = view.findViewById(R.id.product_margin);
                product_instock = view.findViewById(R.id.product_instock);

                qtyedittext = view.findViewById(R.id.qtyedittext);
                product_image = view.findViewById(R.id.product_image);
                qty = view.findViewById(R.id.qty);
                total_cost = view.findViewById(R.id.total_cost);

                statustxt = view.findViewById(R.id.statustxt);
                rejectlinear = view.findViewById(R.id.rejectlinear);
                reject_btn = view.findViewById(R.id.reject_btn);

                receivelinear = view.findViewById(R.id.receivelinear);
                receive_btn = view.findViewById(R.id.receive_btn);

                qtylinear = view.findViewById(R.id.qtylinear);
                totalqtylinear = view.findViewById(R.id.totalqtylinear);
                orderedqtytxt = view.findViewById(R.id.orderedqtytxt);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }


        private void sendPickedItem(String action, String seller, String primary_key, String role,
                                    String item, String qty, final int position, final MyViewHolder holder,
                                    String description,
                                    final String[] item_statusValueArray, String REASONVAL) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(ReceivedOrdersDetailsActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

            Log.e("action",action);
            Log.e("primary_key",ID);
            Log.e("item",item);
            Log.e("qty",qty);
            Log.e("description",description);
            Log.e("reason",REASONVAL);
            Log.e("user",PRIMARYID);

            Call<Login> mService = mApiService.stores_orderstatus(action, ID, SHORTFORM,
                    item, qty, description, REASONVAL,PRIMARYID);
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
                            item_statusValueArray[position] = "1";

                            holder.receive_btn.setVisibility(View.GONE);
                            holder.rejectlinear.setVisibility(View.GONE);
                            holder.receivelinear.setVisibility(View.GONE);
                            holder.receive_btn.setVisibility(View.GONE);
                            holder.statustxt.setVisibility(View.VISIBLE);
                            holder.statustxt.setText("Received");

                            holder.qtyedittext.setCursorVisible(false);
                            holder.qtyedittext.setClickable(false);
                            holder.qtyedittext.setFocusable(false);
                            holder.qtyedittext.setBackground(null);

//                      } else {
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

        private void getRejectPoints(final int position, final String quantity, final MyViewHolder holder,
                                     final String[] item_statusValueArray, final String fromval, final String itemId) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(ReceivedOrdersDetailsActivity.this);
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

                                        hashmapList.add(hashMap);
                                    }
                                    if (fromval.equals("fromreject")) {
                                        rejectPopup(hashmapList, position, quantity, holder, item_statusValueArray,fromval);
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


        public void approvePopup(final int position, final String itemId, String reasonval, final MyViewHolder holder,
                                 final String[] item_statusValueArray,ArrayList<HashMap<String, String>> hashmapList) {
            /** Used for Show Disclaimer Pop up screen */
            alertDialog = new Dialog(ReceivedOrdersDetailsActivity.this);
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
//                    sendPickedItem("1", ID, PRIMARYID, "picker", itemId,
//                            holder.qtyedittext.getText().toString(), position, holder, desc,item_statusValueArray,REASON);

                    sendPickedItem("1", ID, PRIMARYID, "picker", itemId,
                            holder.qtyedittext.getText().toString(), position, holder, desc, item_statusValueArray,
                            REASON);
                }
            });
        }

        public void rejectPopup(ArrayList<HashMap<String, String>> list, final int position,
                                final String qty, final MyViewHolder holder, final String[] item_statusValueArray,
                                final String fromval) {
            Log.e("fromval",fromval);
            /** Used for Show Disclaimer Pop up screen */
            final EditText rejectorder_txt;
            final String itemId = ordersDetailsList.get(position).get("id");

            rejectalertDialog = new Dialog(ReceivedOrdersDetailsActivity.this);
            rejectalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            rejectalertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            View rejectlayout = inflater.inflate(R.layout.reject_popup, null);
            TextView reason_cancel = rejectlayout.findViewById(R.id.reason_reject);

            if(fromval.equals("fromreject"))
            {
                reason_cancel.setText("Reason for reject");
            }else{
                reason_cancel.setText("Reason for balance items");
            }

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
                        dialog = new TransparentProgressDialog(ReceivedOrdersDetailsActivity.this);
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    rejectItem(itemId, qty, rejectorder_txt.getText().toString().trim(), holder, item_statusValueArray, position, REASON,fromval);
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

        int rejectItem(String item, String qty, String description, final MyViewHolder holder,
                       final String[] item_statusValueArray, final int position, String REASONVAL,final String fromval) {
            int serverResponseCode = 0;

            String actionstaus;
            if(fromval.equals("fromreject"))
            {
                actionstaus="2";
            }else{
                actionstaus="1";
            }

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
                Log.e("actionstaus", actionstaus);
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

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"action\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(actionstaus);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    /*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"seller\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ID);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*/

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"primary_key\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(ID);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(SHORTFORM);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"item\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(item);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"qty\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(qty);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(PRIMARYID);
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
//                                        holder.receivelinear.setVisibility(View.GONE);
//                                        holder.receive_btn.setVisibility(View.GONE);
//                                        holder.rejectlinear.setVisibility(View.GONE);
//                                        holder.reject_btn.setVisibility(View.GONE);
//
//                                        holder.statustxt.setVisibility(View.VISIBLE);
//                                        holder.statustxt.setText("Rejected");
//
//                                        item_statusValueArray[position]="2";

                                        getOrderListDetails();

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

        if (ActivityCompat.shouldShowRequestPermissionRationale(ReceivedOrdersDetailsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(ReceivedOrdersDetailsActivity.this,
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

    public class IssueOrderAdapter extends RecyclerView.Adapter<IssueOrderAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> ordersDetailsList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        String[] item_statusValueArray;
        String[] userValueArray;

        public IssueOrderAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                                 ArrayList<HashMap<String, List<String>>> hashmapImagesList,
                                 String[] item_statusValueArray, String[] userValueArray) {
            ordersDetailsList = hashmapList;
            this.mContext = mContext;
            this.hashmapImagesList = hashmapImagesList;
            this.item_statusValueArray = item_statusValueArray;
            this.userValueArray = userValueArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receiveorder_cardview, parent, false);
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
            String action_status = ordersDetailsList.get(position).get("action_status");

            holder.product_name.setText(itemname);
            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);
            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + amount);
            holder.qty.setText(qty);
            holder.total_cost.setText(mContext.getResources().getString(R.string.Rs) + " " + total_price);
            holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
            holder.product_margin.setText(mContext.getResources().getString(R.string.Rs) + " " + margin);

            holder.indexReference = position;

            List<String> imagesList = hashmapImagesList.get(position).get(id);
            if (imagesList != null) {
                if (imagesList.size() > 0) {
                    Picasso.with(mContext)
                            .load(imagesList.get(0))
                            .placeholder(R.drawable.loading)
                            .into(holder.product_image);
                } else {
                    holder.product_image.setImageResource(R.drawable.noimage);
                }
            } else {
                holder.product_image.setImageResource(R.drawable.noimage);
            }

            holder.product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = ordersDetailsList.get(position).get("id");
                    List<String> imagesList = hashmapImagesList.get(position).get(id);

                    Log.e("imagesList in Adapter", "" + imagesList);
                    if (imagesList != null) {
                        if (imagesList.size() > 1) {
                            List<String> productimagesList = null;
                            ArrayList<String> imglist = new ArrayList<>();
                            for (int i = 1; i < imagesList.size(); i++) {
                                // productimagesList.add(imagesList.get(i));
                                imglist.add(imagesList.get(i));
                            }
                            productimagesList = imglist;
                            if (productimagesList != null) {
                                if (productimagesList.size() > 0) {
                                    String itemname = ordersDetailsList.get(position).get("itemname");
                                    // globalShare.setImagesList(imagesList);
                                    globalShare.setImagesList(productimagesList);
                                    Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
                                    intent.putExtra("itemname", itemname);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            //    Toast.makeText(getActivity(),"No images found",Toast.LENGTH_SHORT).show();
                        }


//                        if (imagesList.size() > 0) {
//                            String itemname = ordersDetailsList.get(position).get("itemname");
//                            globalShare.setImagesList(imagesList);
//                            Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
//                            intent.putExtra("itemname", itemname);
//                            startActivity(intent);
//                        } else {
//                        }
                    } else {
                    }
                }
            });

            if (item_statusValueArray[holder.indexReference].equals("1")
                    || item_statusValueArray[holder.indexReference].equals("2")) {
                holder.receive_btn.setVisibility(View.GONE);
                holder.reject_btn.setVisibility(View.GONE);
                holder.receivelinear.setVisibility(View.GONE);
                holder.rejectlinear.setVisibility(View.GONE);
                holder.statustxt.setVisibility(View.VISIBLE);

//                if (item_statusValueArray[holder.indexReference].equals("1")) {
//                    holder.statustxt.setText("Received");
//                } else {
                    holder.statustxt.setText("Rejected");
               // }

            } else {
                holder.receive_btn.setVisibility(View.VISIBLE);
                holder.reject_btn.setVisibility(View.VISIBLE);
                holder.receivelinear.setVisibility(View.VISIBLE);
                holder.rejectlinear.setVisibility(View.VISIBLE);
                holder.statustxt.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return ordersDetailsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView product_image;
            EditText qtyedittext;
            TextView product_name, product_mrp, product_pay, product_discount, product_margin,
                    product_instock, qty, total_cost, statustxt;
            int indexReference;
            LinearLayout rejectlinear, receivelinear;
            Button reject_btn, receive_btn;


            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_pay = view.findViewById(R.id.product_pay);
                product_discount = view.findViewById(R.id.product_discount);
                product_margin = view.findViewById(R.id.product_margin);
                product_instock = view.findViewById(R.id.product_instock);

                qtyedittext = view.findViewById(R.id.qtyedittext);
                product_image = view.findViewById(R.id.product_image);
                qty = view.findViewById(R.id.qty);
                total_cost = view.findViewById(R.id.total_cost);

                statustxt = view.findViewById(R.id.statustxt);
                rejectlinear = view.findViewById(R.id.rejectlinear);
                reject_btn = view.findViewById(R.id.reject_btn);

                receivelinear = view.findViewById(R.id.receivelinear);
                receive_btn = view.findViewById(R.id.receive_btn);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {

            }
        }
    }
}

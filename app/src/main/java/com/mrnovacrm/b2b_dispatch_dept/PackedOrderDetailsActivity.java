package com.mrnovacrm.b2b_dispatch_dept;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.FilePath;
import com.mrnovacrm.constants.CheckNetWork;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by prasad on 3/20/2018.
 */

public class PackedOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerview;
    String ID, ORDERIDVAL;
    TextView orderID;
    //  TextView orderID, total_bags, total_tins, total_cases;
//    RadioButton type_bag, type_tin, type_case;
//    RadioGroup radioGroup;
    //int selectedId;
    String packtype = "bag";
    //Button submit_btn, finish_btn;
    private String PRIMARYID = "";
    ArrayList<String> checkedSelection = new ArrayList<String>();
    ArrayList<String> qtySelection = new ArrayList<String>();
    HashMap<String, String> qtyHashmap = new HashMap<>();
    String packedQuantity;
    boolean isenbled = true;
    String[] _remQtyValueArray;
    String[] _checkboxValueArray;
    String[] _idValueArray;
    int smoothscrollpositon = 0;
    String remainingquantityValue = "0";
    String remainingquantity_Value = "0";
    String INVOICEIDVAL = "";
    Dialog rejectalertDialog;
    Context mContext;
    private EditText proof_path;
    private TransparentProgressDialog dialog;
    private String selectedFilePath;
    private static final int FileSelectId = 3;
    private String REASON;
    private int STORAGE_PERMISSION_CODE = 23;
    private String base64;

    private TextView submittxt, txt_nodata;
    EditText reason_edtxt;
    private ImageView closeicon;
    public static Activity mainfinish;

    LinearLayout remarkslinear;
    TextView remarks_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        mainfinish = this;
        mContext = this;
        setTitle("Packed Order Details");
        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERIDVAL = getIntent().getExtras().getString("orderId");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_placedorderdetails);

        orderID = findViewById(R.id.orderID);

        orderID.setText(ORDERIDVAL);
        recyclerview = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);
        remarkslinear=findViewById(R.id.remarkslinear);
        remarks_text=findViewById(R.id.remarks_text);


//        submit_btn = findViewById(R.id.submit_btn);
//        submit_btn.setOnClickListener(this);
//
//        finish_btn = findViewById(R.id.finish_btn);
//        finish_btn.setOnClickListener(this);
//        submit_btn.setVisibility(View.GONE);

        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(mContext);
        if (isConnectedToInternet) {
            try {
                getOrderListDetails();
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
        final TransparentProgressDialog dialog = new TransparentProgressDialog(PackedOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getDispatcherOrderDetails(ID);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    //   Log.e("mOrderObject", "" + mOrderObject);
                    String status = mOrderObject.getStatus();

                    if (Integer.parseInt(status) == 1) {
                        recyclerview.setVisibility(View.VISIBLE);

                        String total_rows = mOrderObject.getTotal_rows();
                        String total_units = mOrderObject.getTotal_units();
                        String total_savings = mOrderObject.getTotal_savings();
                        String total_processed = mOrderObject.getTotal_processed();

                        String REMARKS=mOrderObject.getRemarks();

                        if(!REMARKS.equals(""))
                        {
                            remarkslinear.setVisibility(View.VISIBLE);
                            remarks_text.setText(REMARKS);
                        }else{
                            remarkslinear.setVisibility(View.GONE);
                        }

                        List<OrdersListDTO> productsList = mOrderObject.getOrdersListDTOS();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                String[] remQtyValueArray = new String[productsList.size()];
                                String[] checkboxValueArray = new String[productsList.size()];
                                String[] idValueArray = new String[productsList.size()];
                                String[] actionstatusValueArray = new String[productsList.size()];
                                String[] userValueArray = new String[productsList.size()];

                                _remQtyValueArray = new String[productsList.size()];
                                _checkboxValueArray = new String[productsList.size()];
                                _idValueArray = new String[productsList.size()];

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String id = productsList.get(i).getId();
                                    String orderid = productsList.get(i).getOrderid();
                                    String qty = productsList.get(i).getQty();
                                    String itemname = productsList.get(i).getItemname();
                                    String pack_type = productsList.get(i).getPack_type();
                                    String packed_qty = productsList.get(i).getPacked_qty();
                                    String balanced_qty = productsList.get(i).getBalanced_qty();
                                    String action_status = productsList.get(i).getAction_status();
                                    String rem_qty = productsList.get(i).getRem_qty();

                                    String mfg_date = productsList.get(i).getMfg_date();
                                    String exp_date = productsList.get(i).getExp_date();
                                    String batch_no = productsList.get(i).getBatch_no();

//                                    String created_on = productsList.get(i).getCreated_on();
//                                    String seller = productsList.get(i).getSeller();
//                                    String brand = productsList.get(i).getBrand();
//                                    String sellingprice = productsList.get(i).getSellingprice();
//                                    String total_price = productsList.get(i).getTotal_price();
//                                    String margin = productsList.get(i).getMargin();

                                    if (qty.equals(packed_qty)) {
                                        isenbled = false;
                                    } else {
                                        isenbled = true;
                                    }
                                    remQtyValueArray[i] = rem_qty;
                                    checkboxValueArray[i] = "0";
                                    idValueArray[i] = id;
                                    actionstatusValueArray[i] = action_status;

                                    if (action_status.equals("1") || action_status.equals("2")) {
                                        userValueArray[i] = packed_qty;
                                    } else {
                                        userValueArray[i] = qty;
                                    }

                                    hashMap.put("id", id);
                                    hashMap.put("orderid", orderid);
                                    hashMap.put("qty", qty);
                                    hashMap.put("itemname", itemname);
                                    hashMap.put("pack_type", pack_type);
                                    hashMap.put("packed_qty", packed_qty);
                                    hashMap.put("balanced_qty", balanced_qty);
                                    hashMap.put("action_status", action_status);
                                    hashMap.put("rem_qty", rem_qty);

                                    hashMap.put("mfg_date", mfg_date);
                                    hashMap.put("exp_date", exp_date);
                                    hashMap.put("batch_no", batch_no);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList, remQtyValueArray, checkboxValueArray, idValueArray, actionstatusValueArray,
                                        userValueArray);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerview.setVisibility(View.GONE);

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

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList, String[] remQtyValueArray,
                               String[] checkboxValueArray, String[] idValueArray, String[] actionstatusValueArray,
                               String[] userValueArray) {

        for (int i = 0; i < remQtyValueArray.length; i++) {
            String reminingqty = remQtyValueArray[i];
            if (!reminingqty.equals("0")) {
                remainingquantityValue = "1";
            }
        }
//        if (remainingquantityValue.equals("1")) {
//            submit_btn.setVisibility(View.VISIBLE);
//            finish_btn.setVisibility(View.GONE);
//        } else {
//            submit_btn.setVisibility(View.GONE);
//            finish_btn.setVisibility(View.VISIBLE);
//        }
        recyclerview.setVisibility(View.VISIBLE);
        recyclerview.smoothScrollToPosition(smoothscrollpositon);
        RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), hashmapList, remQtyValueArray,
                checkboxValueArray, idValueArray, actionstatusValueArray, userValueArray);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.submit_btn:
                submitPackedList(PRIMARYID, packtype);
                break;
            case R.id.finish_btn:
//               Intent intent=new Intent(getApplicationContext(),PackerItemsBarCodeActivity.class);
//               intent.putExtra("id",ID);
//               intent.putExtra("ORDERID",ORDERID);
//               startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    public String removelastchar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void submitPackedList(String packer, String packtype) {
        StringBuilder sb1 = new StringBuilder();

        for (int i = 0; i < _remQtyValueArray.length; i++) {
            String remqty = _remQtyValueArray[i];
            String checkqty = _checkboxValueArray[i];
            String valqty = _idValueArray[i];
            //  Log.e("remqty,checkqty,valqty",remqty+"   "+checkqty+"  "+valqty);
            if (checkqty.equals("1")) {
                sb1.append(valqty);
                sb1.append("~");
                sb1.append(remqty);
                sb1.append(",");
            }
        }

        if (sb1 == null || sb1.toString().equals("")) {
            Toast.makeText(getApplicationContext(), "No Item Selected", Toast.LENGTH_SHORT).show();
        } else {
            String selectedData = removelastchar(sb1.toString());

            final TransparentProgressDialog dialog = new TransparentProgressDialog(PackedOrderDetailsActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

            Call<Login> mService = mApiService.submitPackedList(packer, packtype, selectedData.toString(), ID);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    Login mLoginObject = response.body();
                    Log.e("response", " :" + response);
                    dialog.dismiss();
                    try {
                        String status = mLoginObject.getStatus();
                        if (status.equals("1")) {
                            qtyHashmap = new HashMap<String, String>();
                            remainingquantityValue = "0";
                            getOrderListDetails();
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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
        Context context;
        ArrayList<HashMap<String, String>> ordersDetailsList = new ArrayList<HashMap<String, String>>();
        String[] remQtyValueArray;
        String[] checkboxValueArray;
        String[] idValueArray;
        String[] actionstatusValueArray;
        String[] userValueArray;

        private Dialog alertDialog;
        View layout;

        public RecyclerAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                               String[] remQtyValueArray,
                               String[] checkboxValueArray, String[] idValueArray, String[] actionstatusValueArray,
                               String[] userValueArray) {
            this.context = context;
            ordersDetailsList = hashmapList;
            this.remQtyValueArray = remQtyValueArray;
            this.checkboxValueArray = checkboxValueArray;
            this.idValueArray = idValueArray;
            this.actionstatusValueArray = actionstatusValueArray;
            this.userValueArray = userValueArray;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_packedordersadapater, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerAdapter.MyViewHolder holder, final int position) {

            final String id = ordersDetailsList.get(position).get("id");
            String item_name = ordersDetailsList.get(position).get("itemname");
            final String qty = ordersDetailsList.get(position).get("qty");
            String packed_qty = ordersDetailsList.get(position).get("packed_qty");
            String rem_qty = ordersDetailsList.get(position).get("rem_qty");
            String packtypeval = ordersDetailsList.get(position).get("pack_type");

            String batch_no = ordersDetailsList.get(position).get("batch_no");
            String mfg_date = ordersDetailsList.get(position).get("mfg_date");
            String exp_date = ordersDetailsList.get(position).get("exp_date");


            holder.batchnumberedittext.setText(batch_no);
            holder.mfgdateedittext.setText(mfg_date);
            holder.expdateedittext.setText(exp_date);

            holder.packedtypetext.setText("Pack type : " + packtypeval);

            holder.indexReference = position;

            if (actionstatusValueArray[holder.indexReference].equals("1")
                    || actionstatusValueArray[holder.indexReference].equals("2")) {
                holder.pickerbtn.setVisibility(View.GONE);
                holder.rejectbtn.setVisibility(View.GONE);
                holder.picklinear.setVisibility(View.GONE);
                holder.rejectlnr.setVisibility(View.GONE);

                holder.qtyedittext.setFocusable(false);
                holder.qtyedittext.setFocusableInTouchMode(false);
                holder.qtyedittext.setClickable(false);

                holder.text_picked.setVisibility(View.VISIBLE);
                holder.pickedliner.setVisibility(View.VISIBLE);

                if (actionstatusValueArray[holder.indexReference].equals("1")) {
                    holder.text_picked.setText("Packed");
                } else {
                    holder.text_picked.setText("Rejected");
                }

                holder.batchnumberedittext.setFocusable(false);
                holder.batchnumberedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                holder.batchnumberedittext.setClickable(false);
                holder.batchnumberedittext.setCursorVisible(false);
                holder.batchnumberedittext.setEnabled(false);

                holder.mfgdateedittext.setFocusable(false);
                holder.mfgdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                holder.mfgdateedittext.setClickable(false);
                holder.mfgdateedittext.setCursorVisible(false);
                holder.mfgdateedittext.setEnabled(false);

                holder.expdateedittext.setFocusable(false);
                holder.expdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                holder.expdateedittext.setClickable(false);
                holder.expdateedittext.setCursorVisible(false);
                holder.expdateedittext.setEnabled(false);

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

                holder.batchnumberedittext.setFocusable(true);
                holder.batchnumberedittext.setFocusableInTouchMode(true);

                holder.mfgdateedittext.setFocusable(true);
                holder.mfgdateedittext.setFocusableInTouchMode(true);

                holder.expdateedittext.setFocusable(true);
                holder.expdateedittext.setFocusableInTouchMode(true);
            }

            if (actionstatusValueArray[holder.indexReference].equals("2")) {
                holder.qtyedittext.setText(qty);
            } else {
                holder.qtyedittext.setText(userValueArray[holder.indexReference]);
            }

            holder.itemnametxt.setText(item_name);
            holder.qtytxt.setText(qty);

            holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String batchNumber = holder.batchnumberedittext.getText().toString();
                        String mfgDate = holder.mfgdateedittext.getText().toString();
                        String expDate = holder.expdateedittext.getText().toString();

                        final String itemId = ordersDetailsList.get(position).get("id");
                        String qty = ordersDetailsList.get(position).get("qty");
                        String qtyedittext = holder.qtyedittext.getText().toString().trim();
                        int qtyval = Integer.parseInt(qty);
                        int editqtyval = Integer.parseInt(qtyedittext);
                        if (editqtyval > 0) {
                            if (qtyval < editqtyval) {
                                Toast.makeText(getApplicationContext(), "Pack qty exceeds the qty", Toast.LENGTH_SHORT).show();
                            } else {
                                getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, actionstatusValueArray,
                                        "fromreject", itemId,batchNumber,mfgDate,expDate);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
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
                    userValueArray[holder.indexReference] = s.toString();
                }
            });

            holder.pickerbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final String itemId = ordersDetailsList.get(position).get("id");
                        String qty = ordersDetailsList.get(position).get("qty");
                        String qtyedittext = holder.qtyedittext.getText().toString().trim();
                        int qtyval = Integer.parseInt(qty);
                        int editqtyval = Integer.parseInt(qtyedittext);

                        String batchNumber = holder.batchnumberedittext.getText().toString();
                        String mfgDate = holder.mfgdateedittext.getText().toString();
                        String expDate = holder.expdateedittext.getText().toString();

                        if (editqtyval > 0) {
                            if (qtyval == editqtyval) {
                                sendPickedItem("1", ID, PRIMARYID, "picker", itemId,
                                        holder.qtyedittext.getText().toString(), position, holder, "", actionstatusValueArray,
                                        "",batchNumber,mfgDate,expDate);
                            } else {
                                if (qtyval < editqtyval) {
                                    Toast.makeText(getApplicationContext(), "Pack qty exceeds the qty", Toast.LENGTH_SHORT).show();
                                } else {
//                                approvePopup(position, itemId, holder.qtyedittext.getText().toString(),
//                                        holder,item_statusValueArray);
                                    getRejectPoints(position, holder.qtyedittext.getText().toString(), holder, actionstatusValueArray,
                                            "frompicker", itemId,batchNumber,mfgDate,expDate);
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Value must not be 0", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
            });
            int mfgdateclick = 1;

            holder.mfgdateedittext.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int mYear, mMonth, mDay, mHour, mMinute;
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(PackedOrderDetailsActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        //Log.e("date",""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        holder.mfgdateedittext.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        //    mfgdateclick=2;
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                    return true;
                }
            });

            holder.expdateedittext.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int mYear, mMonth, mDay, mHour, mMinute;
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog1 = new DatePickerDialog(PackedOrderDetailsActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        holder.expdateedittext.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog1.show();
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersDetailsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int indexReference;
            TextView itemnametxt, qtytxt, text_picked, text_rejected, packedtypetext;
            Button pickerbtn, rejectbtn;
            EditText qtyedittext;
            LinearLayout pickedliner, picklinear, rejectlnr;
            EditText mfgdateedittext, expdateedittext, batchnumberedittext;

            public MyViewHolder(View view) {
                super(view);

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
                batchnumberedittext = view.findViewById(R.id.batchnumberedittext);

                mfgdateedittext = view.findViewById(R.id.mfgdateedittext);
                expdateedittext = view.findViewById(R.id.expdateedittext);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }

        private void getRejectPoints(final int position, final String quantity, final MyViewHolder holder,
                                     final String[] item_statusValueArray, final String fromval, final String itemId,
                                     final String batchNumber,final String mfgDate,final String expDate) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(PackedOrderDetailsActivity.this);
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
                                    if (fromval.equals("fromreject")) {
                                        rejectPopup(itemId, hashmapList, position, quantity, holder, item_statusValueArray,
                                                batchNumber,mfgDate,expDate);
                                    } else {
                                        approvePopup(position, itemId, holder.qtyedittext.getText().toString(),
                                                holder, item_statusValueArray, hashmapList,batchNumber,mfgDate,expDate);
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

        public void rejectPopup(final String itemId, ArrayList<HashMap<String, String>> list, final int position,
                                final String qty, final MyViewHolder holder, final String[] item_statusValueArray,
                                final String batchNumber,final String mfgDate,final String expDate) {
            /** Used for Show Disclaimer Pop up screen */
            final EditText rejectorder_txt;

            rejectalertDialog = new Dialog(PackedOrderDetailsActivity.this);
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
                        dialog = new TransparentProgressDialog(PackedOrderDetailsActivity.this);
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //creating new thread to handle Http Operations
                                    rejectItem(itemId, qty, rejectorder_txt.getText().toString().trim(), holder, item_statusValueArray,
                                            position, REASON,batchNumber,mfgDate,expDate);
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
                                 final String[] item_statusValueArray, ArrayList<HashMap<String, String>> hashmapList,
                                 final String batchNumber,final String mfgDate,final String expDate) {
            /** Used for Show Disclaimer Pop up screen */
            alertDialog = new Dialog(PackedOrderDetailsActivity.this);
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
                            holder.qtyedittext.getText().toString(), position, holder, desc, item_statusValueArray, REASON,batchNumber,mfgDate,expDate);
                }
            });
        }

//        int rejectItem(String item, String qty, String description, final MyViewHolder holder,
//                       final String[] item_statusValueArray,final int position,String REASONVAL) {
//            int serverResponseCode = 0;
//
//            final HttpURLConnection connection;
//            DataOutputStream dataOutputStream;
//            String lineEnd = "\r\n";
//            String twoHyphens = "--";
//            String boundary = "*****";
//
//            int bytesRead, bytesAvailable, bufferSize;
//            byte[] buffer;
//            int maxBufferSize = 1 * 1024 * 1024;
//            File selectedFile = new File(selectedFilePath);
//
//            String[] parts = selectedFilePath.split("/");
//            final String fileName = parts[parts.length - 1];
//
//            if (!selectedFile.isFile()) {
//                dialog.dismiss();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
//                    }
//                });
//                return 0;
//            } else {
//                try {
//                    Log.e("rej_img",selectedFilePath);
//                    Log.e("action","2");
//                    Log.e("user",PRIMARYID);
//                    Log.e("order",ID);
//                    Log.e("item",item);
//                    Log.e("qty",qty);
//                    Log.e("reason",REASONVAL);
//                    Log.e("description",description);
//                    Log.e("rej_img",selectedFilePath);
//                    Log.e("rej_img",selectedFilePath);
//
//
//                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
//                    String SERVER_URL = SharedDB.URL + "packers/pack_seller";
//                    URL url = new URL(SERVER_URL);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);//Allow Inputs
//                    connection.setDoOutput(true);//Allow Outputs
//                    connection.setUseCaches(false);//Don't use a cached Copy
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Connection", "Keep-Alive");
//                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                    connection.setRequestProperty(
//                            "Content-Type", "multipart/form-data;boundary=" + boundary);
//                    connection.setRequestProperty("rej_img", selectedFilePath);
//
//                    //creating new dataoutputstream
//                    dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"action\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes("2");
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
////                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"seller\"" + lineEnd);
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes(IDVAL);
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(PRIMARYID);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);  dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"primary_key\"" + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"order\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(ID);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
////                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes("picker");
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
////                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"invoice\"" + lineEnd);
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes(INVOICEIDVAL);
////                    dataOutputStream.writeBytes(lineEnd);
////                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"item\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(item);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"qty\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(qty);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"reason\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(REASONVAL);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(description);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rej_img\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(selectedFilePath);
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"rej_img\";filename=\""
//                            + fileName + "\"" + lineEnd);
//                    dataOutputStream.writeBytes(lineEnd);
//
//                    //returns no. of bytes present in fileInputStream
//                    bytesAvailable = fileInputStream.available();
//                    //selecting the buffer size as minimum of available bytes or 1 MB
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    //setting the buffer as byte array of size of bufferSize
//                    buffer = new byte[bufferSize];
//
//                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
//                    while (bytesRead > 0) {
//                        try {
//                            //write the bytes read from inputstream
//                            dataOutputStream.write(buffer, 0, bufferSize);
//                        } catch (OutOfMemoryError e) {
//                            Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
//                        }
//                        bytesAvailable = fileInputStream.available();
//                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                    }
//
//                    dataOutputStream.writeBytes(lineEnd);
//                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                    try {
//                        serverResponseCode = connection.getResponseCode();
//                    } catch (OutOfMemoryError e) {
//                        Toast.makeText(getApplicationContext(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
//                    }
//                    String serverResponseMessage = connection.getResponseMessage();
//                    Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
//                    //response code of 200 indicates the server status OK
//                    if (serverResponseCode == 200) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                StringBuilder sb = new StringBuilder();
//                                try {
//                                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection
//                                            .getInputStream()));
//                                    String line;
//                                    while ((line = rd.readLine()) != null) {
//                                        sb.append(line);
//                                    }
//                                    rd.close();
//                                } catch (IOException ioex) {
//                                }
//                                Log.e("response", "" + sb.toString());
//
//                                String response = sb.toString();
//
//                                int statusValue = 0;
//                                String messageValue = "";
//                                try {
//                                    JSONObject res_object = new JSONObject(response);
//                                    if (res_object.has("status")) {
//                                        statusValue = res_object.getInt("status");
//                                        Log.e("statusValue", "" + statusValue);
//                                    }
//                                    if (res_object.has("message")) {
//                                        messageValue = res_object.getString("message");
//                                        Log.e("messageValue", messageValue);
//                                    }
//                                    if (statusValue == 1) {
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
//                                        Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//                    //closing the input and output streams
//                    fileInputStream.close();
//                    dataOutputStream.flush();
//                    dataOutputStream.close();
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                dialog.dismiss();
//                rejectalertDialog.dismiss();
//                return serverResponseCode;
//            }
//        }


        int rejectItem(String item, String qty, String description, final MyViewHolder holder,
                       final String[] item_statusValueArray, final int position, String REASONVAL,
                       final String batchNumber,final String mfgDate,final String expDate) {
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

                String SERVER_URL = SharedDB.URL + "packers/pack_seller";

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


                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"batch_no\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(batchNumber);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"exp_date\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(expDate);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"mfg_date\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(mfgDate);
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

                                        item_statusValueArray[position] = "2";

                                        Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();

                                        holder.batchnumberedittext.setFocusable(false);
                                        holder.batchnumberedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                        holder.batchnumberedittext.setClickable(false);
                                        holder.batchnumberedittext.setCursorVisible(false);
                                        holder.batchnumberedittext.setEnabled(false);

                                        holder.mfgdateedittext.setFocusable(false);
                                        holder.mfgdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                        holder.mfgdateedittext.setClickable(false);
                                        holder.mfgdateedittext.setCursorVisible(false);
                                        holder.mfgdateedittext.setEnabled(false);

                                        holder.expdateedittext.setFocusable(false);
                                        holder.expdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                        holder.expdateedittext.setClickable(false);
                                        holder.expdateedittext.setCursorVisible(false);
                                        holder.expdateedittext.setEnabled(false);






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
                                    String item, final String qty, final int position, final MyViewHolder holder,
                                    String description,
                                    final String[] item_statusValueArray, String REASONVAL,String batchNumber,String mfgDate,
                                    String expDate) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(PackedOrderDetailsActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//            Call<Login> mService = mApiService.picker_orderstatus(action, seller, primary_key, role,
//                    item, qty, description,REASONVAL,INVOICEIDVAL);


            Call<Login> mService = mApiService.pack_dispatcherorderstatus(action, ID, item, qty, REASONVAL, description,
                    PRIMARYID, batchNumber, expDate,mfgDate);
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

                            holder.pickerbtn.setVisibility(View.GONE);
                            holder.rejectbtn.setVisibility(View.GONE);
                            holder.picklinear.setVisibility(View.GONE);
                            holder.rejectlnr.setVisibility(View.GONE);
                            holder.qtyedittext.setFocusable(false);
                            holder.qtyedittext.setFocusableInTouchMode(false);
                            holder.qtyedittext.setClickable(false);

                            holder.text_picked.setVisibility(View.VISIBLE);
                            holder.pickedliner.setVisibility(View.VISIBLE);
                            holder.text_picked.setText("Packed");

                            holder.batchnumberedittext.setFocusable(false);
                            holder.batchnumberedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                            holder.batchnumberedittext.setClickable(false);
                            holder.batchnumberedittext.setCursorVisible(false);
                            holder.batchnumberedittext.setEnabled(false);

                            holder.mfgdateedittext.setFocusable(false);
                            holder.mfgdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                            holder.mfgdateedittext.setClickable(false);
                            holder.mfgdateedittext.setCursorVisible(false);
                            holder.mfgdateedittext.setEnabled(false);

                            holder.expdateedittext.setFocusable(false);
                            holder.expdateedittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                            holder.expdateedittext.setClickable(false);
                            holder.expdateedittext.setCursorVisible(false);
                            holder.expdateedittext.setEnabled(false);

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

        if (ActivityCompat.shouldShowRequestPermissionRationale(PackedOrderDetailsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(PackedOrderDetailsActivity.this,
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
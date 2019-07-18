package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.CartDetailsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Product;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 01-03-2018.
 */
public class CartListActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    Button btn_continue, btn_checkout;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    Context context;
    RelativeLayout rel_footer;
    TextView items, units, savings, s_charge, pay, subtotalpayval;
    public static Activity mainfinish;
    private GlobalShare globalShare;
    private String ADDRESSID = "";
    private String SHORTFORM = "";
    int smoothscrollpositon = 0;
    RelativeLayout imgrel;
    ImageView imageview;
    private String MOBILEOTPVAL;
    private Dialog otpalertDialog;
    private View otplayout;
    private EditText otpmobilenumber;
    private Button popup_submit;
    private String mobileOTP;
    ArrayList<String> companyidlist = new ArrayList<>();
    ArrayList<String> companynameslist = new ArrayList<>();
    String COMPANYIDVAL = "";
    String COMPANYNAMEVAL = "";
    HashMap<String, String> qtyHashmap = new HashMap<>();
    String HIDDENTOTALPAY;
    String BALANCE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cart_recyclerview);
        context = this;
        mainfinish = this;
        globalShare = (GlobalShare) getApplicationContext();
        recyclerView = findViewById(R.id.cart_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        items = findViewById(R.id.items);
        units = findViewById(R.id.units);
        savings = findViewById(R.id.savings);
        s_charge = findViewById(R.id.s_charge);
        pay = findViewById(R.id.pay);

        rel_footer = findViewById(R.id.rel_footer);
        // text_nodata = findViewById(R.id.text_nodata);

        imgrel = findViewById(R.id.imgrel);
        imageview = findViewById(R.id.imageview);


        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        btn_checkout = findViewById(R.id.btn_checkout);
        btn_checkout.setOnClickListener(this);

        subtotalpayval = findViewById(R.id.subtotalpayval);

        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(CartListActivity.this);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            ADDRESSID = values.get(SharedDB.ADDRESSID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if (isConnectedToInternet) {
            getCartList();
        } else {
            Toast.makeText(getApplicationContext(), R.string.networkerror,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            case R.id.btn_continue:
                if (ProductListActivity.mainfinish != null) {
                    ProductListActivity.mainfinish.finish();
                }
                if (ProductListActivityNew.mainfinish != null) {
                    ProductListActivityNew.mainfinish.finish();
                }

                if (ProductCategoriesActivity.mainfinish != null) {
                    ProductCategoriesActivity.mainfinish.finish();
                }

//                if(SHORTFORM.equals("SE"))
//                {
//                    globalShare.setSalesmenuselectpos("1");
//                    if(SalesMenuScreenActivity.mainfinish!=null)
//                    {
//                        SalesMenuScreenActivity.mainfinish.finish();
//                    }
//                    Intent intent = new Intent(getApplicationContext(), SalesMenuScreenActivity.class);
//                    startActivity(intent);
//                }else {

                globalShare.setDeliverymenuselectpos("1");
                if (DealerMenuScreenActivity.mainfinish != null) {
                    DealerMenuScreenActivity.mainfinish.finish();
                }
                if (DealerScreenActivity.mainfinish != null) {
                    DealerScreenActivity.mainfinish.finish();
                }
                Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
                startActivity(intent);
                //  }
                finish();
                break;
            case R.id.btn_checkout:
//                try{
//                    if (SHORTFORM.equals("SE")) {
//                        if (SharedDB.isDealerExists(getApplicationContext())) {
//                            HashMap<String, String> vals = SharedDB.getDealerDetails(getApplicationContext());
//                            String DEALERMOBILE = vals.get(SharedDB.DEALERMOBILE);
//                            String DEALERID = vals.get(SharedDB.DEALERID);
//                            loginProcessWithRetrofit(DEALERMOBILE, DEALERID);
//                        }
//                    } else {
//                        Intent checkoutIntent = new Intent(getApplicationContext(), UserPaymentActivity.class);
//                        checkoutIntent.putExtra("dealer_id", PRIMARYID);
//                        startActivity(checkoutIntent);
//                    }
//                }catch (Exception e)
//                {
//                }

////                String hidden_total_pay;
////                String balance;
//
//
//                Log.e("HIDDENTOTALPAY",HIDDENTOTALPAY);
//                Log.e("BALANCE",BALANCE);
                try{
                    if(HIDDENTOTALPAY.equals("") || BALANCE.equals(""))
                    {
                    }else{
                        if(Float.parseFloat((BALANCE))>=Float.parseFloat((HIDDENTOTALPAY)))
                        {
                            if (SHORTFORM.equals("SE")) {
                                if (SharedDB.isDealerExists(getApplicationContext())) {
                                    HashMap<String, String> vals = SharedDB.getDealerDetails(getApplicationContext());
                                    String DEALERMOBILE = vals.get(SharedDB.DEALERMOBILE);
                                    String DEALERID = vals.get(SharedDB.DEALERID);
//                                    loginProcessWithRetrofit(DEALERMOBILE, DEALERID);
                                    showOTPPopup(DEALERID);

//                                    Intent checkoutIntent = new Intent(getApplicationContext(), UserPaymentActivity.class);
//                                    checkoutIntent.putExtra("dealer_id", DEALERID);
//                                    startActivity(checkoutIntent);
                                }
                            } else {
                                Intent checkoutIntent = new Intent(getApplicationContext(), UserPaymentActivity.class);
                                checkoutIntent.putExtra("dealer_id", PRIMARYID);
                                startActivity(checkoutIntent);
                            }
                        }else{
                            // Please contact finance admin (or) reduce some items from your cart "
                            Toast.makeText(getApplicationContext(),"Your credit limit exceed. Please contact finance admin",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e)
                {
                }
                break;
            default:
                break;
        }
    }

    public void showOTPPopup(final String dealer_id) {
        //	if(!globalShare.getSelectuserotp().equals("local")) {
        /** Used for Show Disclaimer Pop up screen */
        otpalertDialog = new Dialog(CartListActivity.this);
        otpalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpalertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        otplayout = inflater.inflate(R.layout.loginotppopup, null);
        otpalertDialog.setContentView(otplayout);
        otpalertDialog.setCancelable(false);
        if (!otpalertDialog.isShowing()) {
            otpalertDialog.show();
        }

        ImageView cancel = (ImageView) otplayout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                otpalertDialog.dismiss();
            }
        });

        otpmobilenumber = (EditText) otplayout.findViewById(R.id.otpmobilenumber);
        otpmobilenumber.setHintTextColor(getResources().getColor(R.color.darkgrey));

        popup_submit = (Button) otplayout.findViewById(R.id.popup_submit);
        popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileOTP = otpmobilenumber.getText().toString().trim();
                if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                        || mobileOTP.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
//                    if (mobileOTP.equals(MOBILEOTPVAL)) {
                    if (mobileOTP.equals("123456")) {
                        otpalertDialog.dismiss();
                        Intent intent = new Intent(
                                getApplicationContext(),
                                UserPaymentActivity.class);
                        intent.putExtra("dealer_id", dealer_id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        otpmobilenumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private boolean isConnectedToInternet;

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(getApplicationContext());
                    hideKeyboard();
                    if (isConnectedToInternet) {
                        mobileOTP = otpmobilenumber.getText().toString();
                        if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                                || mobileOTP.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mobileOTP.equals(MOBILEOTPVAL)) {
                                otpalertDialog.dismiss();
                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        UserPaymentActivity.class);
                                startActivity(intent);
                                otpalertDialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Check Your Internet Connection",
                                Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
        //}
    }

    private void loginProcessWithRetrofit(final String mobilenumber, final String dealer_id) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.authenticate(mobilenumber);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    MOBILEOTPVAL = mLoginObject.getOtp();
                   // Log.e("MOBILEOTPVAL", MOBILEOTPVAL);
                    showOTPPopup(dealer_id);
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
              //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCartList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("ID", " :" + PRIMARYID);
        //  Call<Product> mService = mApiService.getCartList(PRIMARYID);

        Call<Product> mService = null;
       final HashMap<String, Integer> companyhashMap = new HashMap<String, Integer>();
        if (SHORTFORM.equals("SE")) {
            if (SharedDB.isDealerExists(getApplicationContext())) {
                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
                String DEALERID = VALS.get(SharedDB.DEALERID);
                mService = mApiService.getCartList(DEALERID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                            Log.e("response", "" + response);
                            Product mProductObject = response.body();
//                            Log.e("mProductObject", "" + mProductObject);
                            String status = mProductObject.getStatus();
                            Log.e("status", "" + status);
                            if (Integer.parseInt(status) == 1) {
                                String total_rows = mProductObject.getTotal_rows();
                                items.setText(total_rows);
                                String total_units = mProductObject.getTotal_units();
                                units.setText(total_units);
                                String total_savings = mProductObject.getTotal_savings();
                                savings.setText(getResources().getString(R.string.Rs) + " " + total_savings);
                                String total_sur_charge = mProductObject.getTotal_sur_charge();
                                s_charge.setText(getResources().getString(R.string.Rs) + " " + total_sur_charge);
                                String total_pay = mProductObject.getTotal_pay();
                                pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);

                                String sub_pay = mProductObject.getSub_pay();
                                subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_pay);

                                HIDDENTOTALPAY = mProductObject.getHidden_total_pay();
                                BALANCE = mProductObject.getBalance();
                                List<CartDetailsDTO> cartDetailsDTOS = mProductObject.getCartDetailsDTOList();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                if (cartDetailsDTOS != null) {
                                    if (cartDetailsDTOS.size() > 0) {

                                        recyclerView.smoothScrollToPosition(0);
                                        imgrel.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);

                                        String[] item_cartListValueArray = new String[cartDetailsDTOS.size()];
                                        String[] item_balqtyValueArray = new String[cartDetailsDTOS.size()];
                                        String[] item_idValueArray = new String[cartDetailsDTOS.size()];

                                        ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                        for (int i = 0; i < cartDetailsDTOS.size(); i++) {
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                            String id = cartDetailsDTOS.get(i).getId();
                                            String user_id = cartDetailsDTOS.get(i).getUser_id();
                                            String item_id = cartDetailsDTOS.get(i).getItem_id();
                                            String sellerid = cartDetailsDTOS.get(i).getSellerid();
                                            String product_count = cartDetailsDTOS.get(i).getProduct_count();
                                            String modifiedon = cartDetailsDTOS.get(i).getModifiedon();
                                            String seller = cartDetailsDTOS.get(i).getSeller();
                                            String itemname = cartDetailsDTOS.get(i).getItemname();
                                            String brand = cartDetailsDTOS.get(i).getBrand();
                                            String pay = cartDetailsDTOS.get(i).getPay();
                                            String mrp = cartDetailsDTOS.get(i).getMrp();
                                            String discount = cartDetailsDTOS.get(i).getDiscount();
                                            String sellingprice = cartDetailsDTOS.get(i).getSellingprice();
                                            String total_price = cartDetailsDTOS.get(i).getTotal_price();
                                            String bal_qty = cartDetailsDTOS.get(i).getBal_qty();
                                            String service_percent = cartDetailsDTOS.get(i).getService_percent();
                                            String company_id = cartDetailsDTOS.get(i).getCompany_id();
                                            String company = cartDetailsDTOS.get(i).getCompany();
                                            String pack_qty = cartDetailsDTOS.get(i).getPack_qty();
                                            String pack_type = cartDetailsDTOS.get(i).getPack_type();
                                            String caseprice = cartDetailsDTOS.get(i).getCaseprice();
                                            String company_mrp = cartDetailsDTOS.get(i).getCompany_mrp();

                                            Log.e("company_mrp",company_mrp);

                                            if(i==0)
                                            {
                                                companyhashMap.put(company_id, i);
                                            }else{
                                                if (companyhashMap.containsKey(company_id))
                                                {

                                                }else{
                                                    companyhashMap.put(company_id, i);
                                                }
                                            }
                                            item_idValueArray[i]=item_id;
  //                                          item_idValueArray[i]=id;
                                            List<String> images = cartDetailsDTOS.get(i).getImagesList();
                                            item_balqtyValueArray[i] = bal_qty;

                                            hashMap.put("company_id", company_id);
                                            hashMap.put("company", company);
                                            hashMap.put("id", id);
                                            hashMap.put("user_id", user_id);
                                            hashMap.put("item_id", item_id);
                                            hashMap.put("sellerid", sellerid);
                                            hashMap.put("product_count", product_count);
                                            hashMap.put("modifiedon", modifiedon);
                                            hashMap.put("seller", seller);
                                            hashMap.put("itemname", itemname);
                                            hashMap.put("brand", brand);
                                            hashMap.put("pay", pay);
                                            hashMap.put("mrp", mrp);
                                            hashMap.put("sellingprice", sellingprice);
                                            hashMap.put("total_price", total_price);
                                            hashMap.put("discount", discount);
                                            hashMap.put("bal_qty", bal_qty);
                                            hashMap.put("service_percent", service_percent);
                                            hashMap.put("pack_qty", pack_qty);
                                            hashMap.put("pack_type", pack_type);
                                            hashMap.put("caseprice", caseprice);
                                            hashMap.put("company_mrp", company_mrp);

                                            hashmapList.add(hashMap);
                                            item_cartListValueArray[i] = product_count;
                                            hashMapImages.put(id, images);
                                            hashmapImagesList.add(hashMapImages);
                                        }
                                        showCartData(hashmapList, hashmapImagesList, item_cartListValueArray,
                                                item_balqtyValueArray, companyhashMap,item_idValueArray);
                                    } else {
                                        // text_nodata.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                        imgrel.setVisibility(View.VISIBLE);
                                        imageview.setImageResource(R.drawable.yourcartempty);
                                    }
                                }
                            } else {
                                String message = mProductObject.getMessage();
                                //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                // text_nodata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.yourcartempty);
                                rel_footer.setVisibility(View.GONE);

                                if(SHORTFORM.equals("SE"))
                                {
                                    SharedPreferences dealerpreferences = getSharedPreferences(
                                            SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor dealereditor = dealerpreferences.edit();
                                    dealereditor.clear();
                                    dealereditor.commit();
                                    SharedDB.dealerclearAuthentication(CartListActivity.this);
                                }


                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                       // Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mService = mApiService.getCartList(PRIMARYID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                            Log.e("response", "" + response);
                            Product mProductObject = response.body();
                            Log.e("mProductObject", "" + mProductObject);
                            String status = mProductObject.getStatus();
                            Log.e("status", "" + status);
                            if (Integer.parseInt(status) == 1) {

                                String total_rows = mProductObject.getTotal_rows();
                                items.setText(total_rows);
                                String total_units = mProductObject.getTotal_units();
                                units.setText(total_units);
                                String total_savings = mProductObject.getTotal_savings();
                                savings.setText(getResources().getString(R.string.Rs) + " " + total_savings);
                                String total_sur_charge = mProductObject.getTotal_sur_charge();
                                s_charge.setText(getResources().getString(R.string.Rs) + " " + total_sur_charge);
                                String total_pay = mProductObject.getTotal_pay();
                                pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);

                                String sub_pay = mProductObject.getSub_pay();
                                subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_pay);

                                HIDDENTOTALPAY = mProductObject.getHidden_total_pay();
                                BALANCE = mProductObject.getBalance();

                                List<CartDetailsDTO> cartDetailsDTOS = mProductObject.getCartDetailsDTOList();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                if (cartDetailsDTOS != null) {
                                    if (cartDetailsDTOS.size() > 0) {

                                        recyclerView.smoothScrollToPosition(0);
                                        imgrel.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);


                                        String[] item_cartListValueArray = new String[cartDetailsDTOS.size()];
                                        String[] item_balqtyValueArray = new String[cartDetailsDTOS.size()];
                                        String[] item_idValueArray = new String[cartDetailsDTOS.size()];

                                        ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                        for (int i = 0; i < cartDetailsDTOS.size(); i++) {
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                            String company_id = cartDetailsDTOS.get(i).getCompany_id();
                                            String company = cartDetailsDTOS.get(i).getCompany();
                                            String id = cartDetailsDTOS.get(i).getId();
                                            String user_id = cartDetailsDTOS.get(i).getUser_id();
                                            String item_id = cartDetailsDTOS.get(i).getItem_id();
                                            String sellerid = cartDetailsDTOS.get(i).getSellerid();
                                            String product_count = cartDetailsDTOS.get(i).getProduct_count();
                                            String modifiedon = cartDetailsDTOS.get(i).getModifiedon();
                                            String seller = cartDetailsDTOS.get(i).getSeller();
                                            String itemname = cartDetailsDTOS.get(i).getItemname();
                                            String brand = cartDetailsDTOS.get(i).getBrand();
                                            String pay = cartDetailsDTOS.get(i).getPay();
                                            String mrp = cartDetailsDTOS.get(i).getMrp();
                                            String discount = cartDetailsDTOS.get(i).getDiscount();
                                            String sellingprice = cartDetailsDTOS.get(i).getSellingprice();
                                            String total_price = cartDetailsDTOS.get(i).getTotal_price();
                                            String bal_qty = cartDetailsDTOS.get(i).getBal_qty();
                                            String service_percent = cartDetailsDTOS.get(i).getService_percent();
                                            String pack_qty = cartDetailsDTOS.get(i).getPack_qty();
                                            String pack_type = cartDetailsDTOS.get(i).getPack_type();
                                            String caseprice = cartDetailsDTOS.get(i).getCaseprice();
                                            String company_mrp = cartDetailsDTOS.get(i).getCompany_mrp();

                                            if(i==0)
                                            {
                                                companyhashMap.put(company_id, i);
                                            }else{
                                                if (companyhashMap.containsKey(company_id))
                                                {

                                                }else{
                                                    companyhashMap.put(company_id, i);
                                                }
                                            }
//                                            item_idValueArray[i]=id;
                                            item_idValueArray[i]=item_id;
                                            List<String> images = cartDetailsDTOS.get(i).getImagesList();

                                            item_balqtyValueArray[i] = bal_qty;

                                            hashMap.put("company_id", company_id);
                                            hashMap.put("company", company);
                                            hashMap.put("id", id);
                                            hashMap.put("user_id", user_id);
                                            hashMap.put("item_id", item_id);
                                            hashMap.put("sellerid", sellerid);
                                            hashMap.put("product_count", product_count);
                                            hashMap.put("modifiedon", modifiedon);
                                            hashMap.put("seller", seller);
                                            hashMap.put("itemname", itemname);
                                            hashMap.put("brand", brand);
                                            hashMap.put("pay", pay);
                                            hashMap.put("mrp", mrp);
                                            hashMap.put("sellingprice", sellingprice);
                                            hashMap.put("total_price", total_price);
                                            hashMap.put("discount", discount);
                                            hashMap.put("bal_qty", bal_qty);
                                            hashMap.put("service_percent", service_percent);
                                            hashMap.put("pack_qty", pack_qty);
                                            hashMap.put("pack_type", pack_type);
                                            hashMap.put("caseprice", caseprice);
                                            hashMap.put("company_mrp", company_mrp);
                                            hashmapList.add(hashMap);

                                            item_cartListValueArray[i] = product_count;

                                            hashMapImages.put(id, images);
                                            hashmapImagesList.add(hashMapImages);
                                        }
                                        showCartData(hashmapList, hashmapImagesList, item_cartListValueArray,
                                                item_balqtyValueArray, companyhashMap,item_idValueArray);
                                    } else {
                                        // text_nodata.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                        imgrel.setVisibility(View.VISIBLE);
                                        imageview.setImageResource(R.drawable.yourcartempty);

                                        if(SHORTFORM.equals("SE"))
                                        {
                                            SharedPreferences dealerpreferences = getSharedPreferences(
                                                    SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor dealereditor = dealerpreferences.edit();
                                            dealereditor.clear();
                                            dealereditor.commit();
                                            SharedDB.dealerclearAuthentication(CartListActivity.this);
                                        }
                                    }
                                }
                            } else {
                                String message = mProductObject.getMessage();
                                //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                // text_nodata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.yourcartempty);
                                rel_footer.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                      //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            mService = mApiService.getCartList(PRIMARYID);
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response", "" + response);
                        Product mProductObject = response.body();
                        Log.e("mProductObject", "" + mProductObject);
                        String status = mProductObject.getStatus();
                        Log.e("status", "" + status);
                        if (Integer.parseInt(status) == 1) {

                            String total_rows = mProductObject.getTotal_rows();
                            items.setText(total_rows);
                            String total_units = mProductObject.getTotal_units();
                            units.setText(total_units);
                            String total_savings = mProductObject.getTotal_savings();
                            savings.setText(getResources().getString(R.string.Rs) + " " + total_savings);
                            String total_sur_charge = mProductObject.getTotal_sur_charge();
                            s_charge.setText(getResources().getString(R.string.Rs) + " " + total_sur_charge);
                            String total_pay = mProductObject.getTotal_pay();
                            pay.setText(getResources().getString(R.string.Rs) + " " + total_pay);

                            String sub_pay = mProductObject.getSub_pay();
                            subtotalpayval.setText(getResources().getString(R.string.Rs) + " " + sub_pay);

                            HIDDENTOTALPAY = mProductObject.getHidden_total_pay();
                            BALANCE = mProductObject.getBalance();

                            List<CartDetailsDTO> cartDetailsDTOS = mProductObject.getCartDetailsDTOList();
                            ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                            if (cartDetailsDTOS != null) {
                                if (cartDetailsDTOS.size() > 0) {

                                    recyclerView.smoothScrollToPosition(0);
                                    imgrel.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);


                                    String[] item_cartListValueArray = new String[cartDetailsDTOS.size()];
                                    String[] item_balqtyValueArray = new String[cartDetailsDTOS.size()];
                                    String[] item_idValueArray = new String[cartDetailsDTOS.size()];

                                    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < cartDetailsDTOS.size(); i++) {
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                        String company_id = cartDetailsDTOS.get(i).getCompany_id();
                                        String company = cartDetailsDTOS.get(i).getCompany();

                                        String id = cartDetailsDTOS.get(i).getId();
                                        String user_id = cartDetailsDTOS.get(i).getUser_id();
                                        String item_id = cartDetailsDTOS.get(i).getItem_id();
                                        String sellerid = cartDetailsDTOS.get(i).getSellerid();
                                        String product_count = cartDetailsDTOS.get(i).getProduct_count();
                                        String modifiedon = cartDetailsDTOS.get(i).getModifiedon();
                                        String seller = cartDetailsDTOS.get(i).getSeller();
                                        String itemname = cartDetailsDTOS.get(i).getItemname();
                                        String brand = cartDetailsDTOS.get(i).getBrand();
                                        String pay = cartDetailsDTOS.get(i).getPay();
                                        String mrp = cartDetailsDTOS.get(i).getMrp();
                                        String discount = cartDetailsDTOS.get(i).getDiscount();
                                        String sellingprice = cartDetailsDTOS.get(i).getSellingprice();
                                        String total_price = cartDetailsDTOS.get(i).getTotal_price();
                                        String bal_qty = cartDetailsDTOS.get(i).getBal_qty();
                                        String service_percent = cartDetailsDTOS.get(i).getService_percent();
                                        String pack_qty = cartDetailsDTOS.get(i).getPack_qty();
                                        String pack_type = cartDetailsDTOS.get(i).getPack_type();
                                        String caseprice = cartDetailsDTOS.get(i).getCaseprice();
                                        String company_mrp = cartDetailsDTOS.get(i).getCompany_mrp();

                                        List<String> images = cartDetailsDTOS.get(i).getImagesList();
                                        if(i==0)
                                        {
                                            companyhashMap.put(company_id, i);
                                        }else{
                                            if (companyhashMap.containsKey(company_id))
                                            {

                                            }else{
                                                companyhashMap.put(company_id, i);
                                            }
                                        }
                                        item_idValueArray[i]=item_id;

//                                        if (companyhashMap.containsKey(company_id)) {
//
//                                        } else {
//                                            companyhashMap.put(company_id, i);
//                                        }

                                        item_balqtyValueArray[i] = bal_qty;

                                        hashMap.put("company_id", company_id);
                                        hashMap.put("company", company);
                                        hashMap.put("id", id);
                                        hashMap.put("user_id", user_id);
                                        hashMap.put("item_id", item_id);
                                        hashMap.put("sellerid", sellerid);
                                        hashMap.put("product_count", product_count);
                                        hashMap.put("modifiedon", modifiedon);
                                        hashMap.put("seller", seller);
                                        hashMap.put("itemname", itemname);
                                        hashMap.put("brand", brand);
                                        hashMap.put("pay", pay);
                                        hashMap.put("mrp", mrp);
                                        hashMap.put("sellingprice", sellingprice);
                                        hashMap.put("total_price", total_price);
                                        hashMap.put("discount", discount);
                                        hashMap.put("bal_qty", bal_qty);
                                        hashMap.put("service_percent", service_percent);
                                        hashMap.put("pack_qty", pack_qty);
                                        hashMap.put("pack_type", pack_type);
                                        hashMap.put("caseprice", caseprice);
                                        hashMap.put("company_mrp", company_mrp);
                                        hashmapList.add(hashMap);

                                        item_cartListValueArray[i] = product_count;
                                        hashMapImages.put(id, images);
                                        hashmapImagesList.add(hashMapImages);
                                    }
                                    Log.e("companyhashMap",""+companyhashMap);

                                    showCartData(hashmapList, hashmapImagesList, item_cartListValueArray, item_balqtyValueArray,
                                            companyhashMap,item_idValueArray);
                                } else {
                                    // text_nodata.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    imgrel.setVisibility(View.VISIBLE);
                                    imageview.setImageResource(R.drawable.yourcartempty);
                                }
                            }
                        } else {
                            String message = mProductObject.getMessage();
                            //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            // text_nodata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            imgrel.setVisibility(View.VISIBLE);
                            imageview.setImageResource(R.drawable.yourcartempty);
                            rel_footer.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                 //   Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showCartData(ArrayList<HashMap<String, String>> hashmapList,
                             ArrayList<HashMap<String, List<String>>> hashmapImagesList, String[] item_cartListValueArray,
                             String[] item_balqtyValueArray, HashMap<String, Integer> companyhashMap,String[] item_idValueArray) {
        if (hashmapList.size() > 0) {
            rel_footer.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Log.e("smoothscrollpositon", "" + smoothscrollpositon);
            if (smoothscrollpositon >= 0) {
                recyclerView.scrollToPosition(smoothscrollpositon);
                // recyclerView.smoothScrollToPosition(smoothscrollpositon);
            }
            CartListAdapter adapter = new CartListAdapter(this, hashmapList, hashmapImagesList,
                    item_cartListValueArray, item_balqtyValueArray, companyhashMap,item_idValueArray);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            // text_nodata.setVisibility(View.VISIBLE);
            rel_footer.setVisibility(View.GONE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.yourcartempty);
        }
    }

    public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> hashmapList;
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        String[] item_cartListValueArray;
        String[] item_balqtyValueArray;
        HashMap<String, Integer> companyhashMap;
        String[] item_idValueArray;

        public CartListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                               ArrayList<HashMap<String, List<String>>> hashmapImagesList, String[] item_cartListValueArray,
                               String[] item_balqtyValueArray, HashMap<String, Integer> companyhashMap,
                               String[] item_idValueArray) {
            this.mContext = mContext;
            this.hashmapList = hashmapList;
            this.hashmapImagesList = hashmapImagesList;
            this.item_cartListValueArray = item_cartListValueArray;
            this.item_balqtyValueArray = item_balqtyValueArray;
            this.companyhashMap = companyhashMap;
            this.item_idValueArray=item_idValueArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_cartlist_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String id = hashmapList.get(position).get("id");
            String user_id = hashmapList.get(position).get("user_id");
            String item_id = hashmapList.get(position).get("item_id");
            String sellerid = hashmapList.get(position).get("sellerid");
            String product_count = hashmapList.get(position).get("product_count");
            String modifiedon = hashmapList.get(position).get("modifiedon");
            String seller = hashmapList.get(position).get("seller");
            String itemname = hashmapList.get(position).get("itemname");
            String brand = hashmapList.get(position).get("brand");
            String pay = hashmapList.get(position).get("pay");
            String mrp = hashmapList.get(position).get("mrp");
            String sellingprice = hashmapList.get(position).get("sellingprice");
            String total_price = hashmapList.get(position).get("total_price");
            String discount = hashmapList.get(position).get("discount");
            String bal_qty = hashmapList.get(position).get("bal_qty");
            String service_percent = hashmapList.get(position).get("service_percent");
            String company_id = hashmapList.get(position).get("company_id");
            String company = hashmapList.get(position).get("company");
            String pack_qty = hashmapList.get(position).get("pack_qty");
            String pack_type = hashmapList.get(position).get("pack_type");
            String caseprice = hashmapList.get(position).get("caseprice");
            String company_mrp = hashmapList.get(position).get("company_mrp");

//            Log.e("company_mrp***********",company_mrp);
            holder.mrp_txt.setText(mContext.getResources().getString(R.string.Rs) + " " + company_mrp);
            if(caseprice!=null)
            {
                if(!caseprice.equals(""))
                {
                    holder.caseprice_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + caseprice);
                }
            }

            if (companyhashMap.size() > 0) {
                if (companyhashMap.size() == 1) {
                    if (position == 0) {
                        holder.titlelinear.setVisibility(View.VISIBLE);
                        holder.titletxt.setText(company);
                    } else {
                        holder.titlelinear.setVisibility(View.GONE);
                    }
                } else if (companyhashMap.size() == 2) {

                    if(companyhashMap.containsKey(company_id))
                    {
                        int pos=companyhashMap.get(company_id);
                        if(position==pos)
                        {
                            holder.titlelinear.setVisibility(View.VISIBLE);
                            holder.titletxt.setText(company);
                        }else{
                            holder.titlelinear.setVisibility(View.GONE);
                        }
                    }
                }else{
                    holder.titlelinear.setVisibility(View.GONE);
                }
            } else {
                holder.titlelinear.setVisibility(View.GONE);
            }

            holder.packtype_txt.setText(pack_type);
            holder.packqtytxt.setText(pack_qty+" PCs");

            holder.indexReference = position;
            holder.product_name.setText(itemname);
            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);
            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + pay);
            holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
            holder.product_margin.setText(mContext.getResources().getString(R.string.Rs) + " " + sellingprice);
            holder.totaltxt.setText(mContext.getResources().getString(R.string.Rs) + " " + total_price);

            // holder.product_instock.setText(bal_qty+" In Stock");
            //   holder.product_instock.setText(item_balqtyValueArray[holder.indexReference]+" In Stock");
            holder.product_instock.setVisibility(View.GONE);
            holder.qtyedittext.setText(item_cartListValueArray[holder.indexReference]);
            try {
                int balqty_val = Integer.parseInt(item_balqtyValueArray[holder.indexReference]);
                int qty_value = Integer.parseInt(item_cartListValueArray[holder.indexReference]);
                if (qty_value > balqty_val) {
                    holder.product_instock.setTextColor(getResources().getColor(R.color.appbackground));
                } else {
                    holder.product_instock.setTextColor(getResources().getColor(R.color.black));
                }
            } catch (Exception e) {
            }

            holder.indexReference = position;
            smoothscrollpositon = holder.indexReference;
            holder.qtyedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    try {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            // Your action on done
                            String qty = hashmapList.get(position).get("product_count");
                            String bal_qty = hashmapList.get(position).get("bal_qty");
                            DealerMenuScreenActivity.updateCartCountNotification(qty);
                            DealerScreenActivity.updateCartCountNotification(qty);
                            hideKeyboard();
                            String count = holder.qtyedittext.getText().toString().trim();
                            if (!count.equals("")) {
                                if (!qty.equals("")) {
                                    if (Long.parseLong(count) == 0) {
                                        Toast.makeText(getApplicationContext(), "Value should be greaterthan 0", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //  if (!qty.equals("") && Integer.parseInt(bal_qty)<=Integer.parseInt(count)) {
                                        String id = hashmapList.get(position).get("id");
                                        String item_id = hashmapList.get(position).get("item_id");
                                        String sellerid = hashmapList.get(position).get("sellerid");
                                        addtocart(item_id, sellerid, count, item_cartListValueArray, position, holder);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter value", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    } catch (Exception e) {
                    }
                    return false;
                }
            });

            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //  valueList[pos] = s.toString()+"~"+data.get(pos).get("attrId");
//                    String item_id = hashmapList.get(position).get("item_id");
//                    String qtyval=holder.qtyedittext.getText().toString().trim();
//                    if(qtyval!=null || !qtyval.equals("")) {
//                        qtyHashmap.put(item_id, qtyval);
//                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    // uservals.add(holder.indexReference,s.toString());

                    item_cartListValueArray[holder.indexReference] = s.toString();
                    String id = item_idValueArray[holder.indexReference];
                    if(s.toString()!=null || !s.toString().equals(""))
                    {
                        qtyHashmap.put(id, s.toString());
                    }
                }
            });



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
                    String id = hashmapList.get(position).get("id");
                    List<String> imagesList = hashmapImagesList.get(position).get(id);
                    Log.e("imagesList in Adapter", "" + imagesList);
                    if (imagesList != null) {
                        if (imagesList.size() > 1) {
                            String itemname = hashmapList.get(position).get("itemname");

                            List<String> productimagesList = null;
                            ArrayList<String> imglist = new ArrayList<>();
                            for (int i = 1; i < imagesList.size(); i++) {
                                // productimagesList.add(imagesList.get(i));
                                imglist.add(imagesList.get(i));
                            }
                            productimagesList = imglist;
                            if (productimagesList != null) {
                                if (productimagesList.size() > 0) {
                                    globalShare.setImagesList(productimagesList);
                                    Intent intent = new Intent(getApplicationContext(), ProductImageActivity.class);
                                    intent.putExtra("itemname", itemname);
                                    startActivity(intent);
                                }
                            }
                        } else {
                           // Toast.makeText(getApplicationContext(),"No images found",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    }
                }
            });

            holder.cart_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String item_id = hashmapList.get(position).get("item_id");
                    String sellerid = hashmapList.get(position).get("sellerid");
                    removefromcart(item_id, sellerid, position, holder);
                }
            });
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            EditText qtyedittext;
            ImageView product_image, cart_img;
            TextView product_name, product_mrp, product_pay, product_discount,
                    product_margin, product_instock, sctxt, totaltxt, titletxt,packtype_txt,packqtytxt,caseprice_pay,mrp_txt;
            int indexReference;
            LinearLayout titlelinear;

            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_pay = view.findViewById(R.id.product_pay);
                product_discount = view.findViewById(R.id.product_discount);
                product_margin = view.findViewById(R.id.product_margin);
                product_instock = view.findViewById(R.id.product_instock);
                sctxt = view.findViewById(R.id.sctxt);
                totaltxt = view.findViewById(R.id.totaltxt);

                qtyedittext = view.findViewById(R.id.qtyedittext);
                product_image = view.findViewById(R.id.product_image);
                cart_img = view.findViewById(R.id.cart_img);

                titlelinear = view.findViewById(R.id.titlelinear);
                titletxt = view.findViewById(R.id.titletxt);
                packtype_txt = view.findViewById(R.id.packtype_txt);
                packqtytxt = view.findViewById(R.id.packqtytxt);
                caseprice_pay = view.findViewById(R.id.caseprice_pay);
                mrp_txt = view.findViewById(R.id.mrp_txt);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }

        private void addtocart(String item_id, String sellerid, String qty, final String[] item_cartListValueArray,
                               final int position, final MyViewHolder holder) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

            HashMap<String, String> vals = SharedDB.getDealerDetails(getApplicationContext());
            String DEALERID = vals.get(SharedDB.DEALERID);

            HashMap<String,String> editQtyHashMap=new HashMap<>();

            if(qtyHashmap!=null) {
                if(qtyHashmap.size()>0) {
                    //Get the set
                    Set set = (Set) qtyHashmap.entrySet();
                    //Create iterator on Set
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) iterator.next();
                        // Get Key
                        String keyValue = (String) mapEntry.getKey();
                        //Get Value
                        String value = (String) mapEntry.getValue();

                        if (!value.equals("")) {
                            editQtyHashMap.put(keyValue, value);
                        }
                    }
                }
            }
            Gson gson = new Gson();
            String gsonhashmap=gson.toJson(editQtyHashMap);
            Call<Product> mService = null;

            Log.e("PRIMARYID", PRIMARYID);
            Log.e("item_id", item_id);
            Log.e("DEALERID", PRIMARYID);
            Log.e("qty", qty);
            Log.e("SHORTFORM", SHORTFORM);
            Log.e("gsonhashmap", gsonhashmap);

            if (SHORTFORM.equals("SE")) {
                mService = mApiService.productaddtocart(PRIMARYID, item_id, DEALERID, qty, SHORTFORM,gsonhashmap);
            } else {
                mService = mApiService.productaddtocart(PRIMARYID, item_id, PRIMARYID, qty, SHORTFORM,gsonhashmap);
            }
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response", "" + response);
                        Product mProductObject = response.body();

                        String status = mProductObject.getStatus();
                        Log.e("status", "" + status);
                        String message = mProductObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (Integer.parseInt(status) == 1) {
                            item_cartListValueArray[position] = holder.qtyedittext.getText().toString();
                            qtyHashmap=new HashMap<>();
                            String itemscount = mProductObject.getItems_count();
                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                            DealerScreenActivity.updateCartCountNotification(itemscount);
                            ProductListActivity.updateCartCountNotification(itemscount);
                            //  BarCodeDetailsActivity.updateCartCountNotification(itemscount);
                            ProductListActivityNew.updateCartCountNotification(itemscount);
                            ProductCategoriesActivity.updateCartCountNotification(itemscount);

                            smoothscrollpositon = position;
                            getCartList();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                  //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void removefromcart(String item_id, String sellerid, final int position, final MyViewHolder holder) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Product> mService = null;
            if (SHORTFORM.equals("SE")) {
                if (SharedDB.isDealerExists(getApplicationContext())) {
                    HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
                    String DEALERID = VALS.get(SharedDB.DEALERID);
                    mService = mApiService.removecart(DEALERID, item_id, sellerid);
                } else {
                    mService = mApiService.removecart(PRIMARYID, item_id, sellerid);
                }
            } else {
                mService = mApiService.removecart(PRIMARYID, item_id, sellerid);
            }
//            Call<Product> mService = mApiService.removecart(PRIMARYID, item_id, sellerid);
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response", "" + response);
                        Product mProductObject = response.body();

                        String status = mProductObject.getStatus();
                        Log.e("status", "" + status);
                        String message = mProductObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (Integer.parseInt(status) == 1) {

                            if (position > 0) {
                                smoothscrollpositon = position - 1;
                            }
                            String itemscount = mProductObject.getItems_count();
                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                            DealerScreenActivity.updateCartCountNotification(itemscount);
                            ProductListActivity.updateCartCountNotification(itemscount);
                            // BarCodeDetailsActivity.updateCartCountNotification(itemscount);
                            ProductListActivityNew.updateCartCountNotification(itemscount);
                            ProductCategoriesActivity.updateCartCountNotification(itemscount);
                            getCartList();
                        }
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                 //   Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String SHORTFORM = "";
    private String DELIVERY_ADDRESS = "";
    private String USERNAME = "";
    private String MOBILE = "";
    private double LATITUDE_VALUE, LANGITUDE_VALUE;
    String addressid="";

    TextView noofitemstxt,totalamounttxt,order_numtxt,mobilenumbertxt,deliveryaddresstxt,
            totalamounbt_txt,deliverychargestxt,deliverybytxt,estimatedtime,servicechargestxt;
    RecyclerView recyclerView;
    Button donebtn;
    Context context;
    private String order_key="";
    GlobalShare globalShare;
    public static Activity mainfinish;
    private Button cancelbtn;
    private TextView subtotalamounbt_txt;
    LinearLayout creditdatelinear,chequenumberlinear,chequedatelinear,remarkslinear;
    TextView paymenttype_txt,creditdate_txt,chequenumber_txt,chequedate_txt,ordertakenby_txt,remarks_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_summary);
        context=this;

        globalShare=(GlobalShare)getApplicationContext();
        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        actionTextView.setText("Order Summary");
        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(OrderSummaryActivity.this);

        if(UserPaymentActivity.mainfinish!=null)
        {
            UserPaymentActivity.mainfinish.finish();
        }
        String orderId = getIntent().getStringExtra("orderId");
        order_key = getIntent().getStringExtra("order_key");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
            USERNAME = values.get(SharedDB.USERNAME);
            MOBILE = values.get(SharedDB.MOBILE);

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

        recyclerView=findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        totalamounbt_txt=findViewById(R.id.totalamounbt_txt);
        deliverychargestxt=findViewById(R.id.deliverychargestxt);
        subtotalamounbt_txt=findViewById(R.id.subtotalamounbt_txt);
        donebtn=findViewById(R.id.donebtn);
        donebtn.setOnClickListener(OrderSummaryActivity.this);

        cancelbtn=findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(OrderSummaryActivity.this);

        order_numtxt.setText("Order No. "+orderId);

        paymenttype_txt=findViewById(R.id.paymenttype_txt);
        creditdatelinear=findViewById(R.id.creditdatelinear);
        creditdate_txt=findViewById(R.id.creditdate_txt);
        chequenumberlinear=findViewById(R.id.chequenumberlinear);
        chequenumber_txt=findViewById(R.id.chequenumber_txt);
        chequedatelinear=findViewById(R.id.chequedatelinear);
        chequedate_txt=findViewById(R.id.chequedate_txt);
        ordertakenby_txt=findViewById(R.id.ordertakenby_txt);
        remarkslinear=findViewById(R.id.remarkslinear);
        remarks_text=findViewById(R.id.remarks_text);
        getOrderListDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backimg:

                if(globalShare.getInvoicesList()!=null)
                {
                    if(globalShare.getInvoicesList().size()>0)
                    {
                        if(globalShare.getInvoicesList().size()==1)
                        {
                            globalShare.setDeliverymenuselectpos("1");
                            if(DealerMenuScreenActivity.mainfinish!=null)
                            {
                                DealerMenuScreenActivity.mainfinish.finish();
                            }
                            if(ProductCategoriesActivity.mainfinish!=null)
                            {
                                ProductCategoriesActivity.mainfinish.finish();
                            }
                            Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
                            startActivity(intent);
                            OrderSummaryActivity.mainfinish.finish();
                        }else{
                            if(ProductCategoriesActivity.mainfinish!=null)
                            {
                                ProductCategoriesActivity.mainfinish.finish();
                            }
                            finish();
                        }
                    }
                }
                break;
            case R.id.donebtn:
                globalShare.setDealerMenuSelectedPos("1");
                if(DealerMenuScreenActivity.mainfinish!=null)
                {
                    DealerMenuScreenActivity.mainfinish.finish();
                }

                if(ProductCategoriesActivity.mainfinish!=null)
                {
                    ProductCategoriesActivity.mainfinish.finish();
                }

                if(CheckoutInvoiceReportActivity.mainfinish!=null)
                {
                    CheckoutInvoiceReportActivity.mainfinish.finish();
                }

                Intent intent1 = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
                startActivity(intent1);
                OrderSummaryActivity.mainfinish.finish();
                break;
            case R.id.cancelbtn:

                if(DealerMenuScreenActivity.mainfinish!=null)
                {
                    DealerMenuScreenActivity.mainfinish.finish();
                }

                if(ProductCategoriesActivity.mainfinish!=null)
                {
                    ProductCategoriesActivity.mainfinish.finish();
                }

                if(CheckoutInvoiceReportActivity.mainfinish!=null)
                {
                    CheckoutInvoiceReportActivity.mainfinish.finish();
                }
                System.exit(0);
                finish();
                break;
            default:
                break;
        }
    }

    private void getOrderListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(OrderSummaryActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Order> mService = mApiService.getOrderDetails(order_key,SHORTFORM);
        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
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

                    String payment_type=mOrderObject.getPayment_type();
                    String credit_date=mOrderObject.getCredit_date();
                    String reference_no=mOrderObject.getReference_no();
                    String takenby_name=mOrderObject.getTakenby_name();
                    String REMARKS=mOrderObject.getRemarks();

                    if (Integer.parseInt(status) == 1) {
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

                                    hashMapImages.put(id,images);
                                    hashmapImagesList.add(hashMapImages);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList,hashmapImagesList);
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
            holder.productcal.setText(amount+" X "+qty);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(globalShare.getInvoicesList()!=null)
        {
            if(globalShare.getInvoicesList().size()>0)
            {
                if(globalShare.getInvoicesList().size()==1)
                {
                    globalShare.setDeliverymenuselectpos("1");
                    if(DealerMenuScreenActivity.mainfinish!=null)
                    {
                        DealerMenuScreenActivity.mainfinish.finish();
                    }
                    if(ProductCategoriesActivity.mainfinish!=null)
                    {
                        ProductCategoriesActivity.mainfinish.finish();
                    }
                    Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
                    startActivity(intent);
                    OrderSummaryActivity.mainfinish.finish();
                }else{
                    if(ProductCategoriesActivity.mainfinish!=null)
                    {
                        ProductCategoriesActivity.mainfinish.finish();
                    }
                    finish();
                }
            }
        }
    }
}
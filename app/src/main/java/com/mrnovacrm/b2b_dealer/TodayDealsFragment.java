package com.mrnovacrm.b2b_dealer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SlidingImage_Adapter;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;
import com.mrnovacrm.model.Product;
import com.mrnovacrm.model.ProductDetails;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayDealsFragment extends Fragment {
    RecyclerView recyclerView;
    private static AutoScrollViewPager mPager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private HashMap<String, String> values=new HashMap<String, String>();
    private String PRIMARYID = "";
    private String USERTYPE = "";
    GlobalShare globalShare;
  //  private TextView nodata_found_txt;
    //LinearLayout imgrel;
    RelativeLayout imgrel;
    ImageView imageview;
    private String SHORTFORM;

    private Dialog dealersalertDialog;
    private View dealerlayout;
    RecyclerView dealerrecyclerView;
    private String BRANCHID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TodayDealsFragment", "TodayDealsFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todaydeals, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //imgrel=rootView.findViewById(R.id.imgrel);
        //nodata_found_txt=rootView.findViewById(R.id.nodata_text);

        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        mPager = rootView.findViewById(R.id.pager);
        indicator = rootView.findViewById(R.id.indicator);
        mPager.startAutoScroll();
        mPager.setInterval(3000);
        mPager.setCycle(true);
        mPager.setStopScrollWhenTouch(true);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), null, getFragmentManager()));
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }
      //  getProductsList();
        return rootView;
    }

    private void getProductsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Product> mService = mApiService.getOffersList(PRIMARYID,"today");
        mService.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Product mProductObject = response.body();
                Log.e("mProductObject", "" + mProductObject);

                String status = mProductObject.getStatus();
                Log.e("status", "" + status);
                try {
                    if (Integer.parseInt(status) == 1) {
                        List<ProductDetails> productsList = mProductObject.getProductDetails();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                imgrel.setVisibility(View.GONE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                    String id = productsList.get(i).getId();
                                    String brand = productsList.get(i).getBrand();
                                    String itemname = productsList.get(i).getItemname();
                                    String productid = productsList.get(i).getProductid();
                                    String mrp = productsList.get(i).getMrp();
                                    String pay = productsList.get(i).getPay();
                                    String suppier_name = productsList.get(i).getSuppier_name();
                                    String sellerid = productsList.get(i).getSellerid();
                                    String qty = productsList.get(i).getQty();
                                    String margin = productsList.get(i).getMargin();
                                    String discount = productsList.get(i).getDiscount();
                                    String cart = productsList.get(i).getCart();
                                    String bal_qty = productsList.get(i).getBal_qty();
                                    String is_bt = productsList.get(i).getIs_bt();
                                    String pack_qty = productsList.get(i).getPack_qty();
                                    String case_rate= productsList.get(i).getCase_rate();


                                    List<String> images=productsList.get(i).getImagesList();
                                    hashMap.put("id", id);
                                    hashMap.put("brand", brand);
                                    hashMap.put("itemname", itemname);
                                    hashMap.put("productid", productid);
                                    hashMap.put("mrp", mrp);
                                    hashMap.put("pay", pay);
                                    hashMap.put("suppier_name", suppier_name);
                                    hashMap.put("sellerid", sellerid);
                                    hashMap.put("qty", qty);
                                    hashMap.put("margin", margin);
                                    hashMap.put("discount", discount);
                                    hashMap.put("cart", cart);
                                    hashMap.put("bal_qty", bal_qty);
                                    hashMap.put("is_bt", is_bt);
                                    hashMap.put("pack_qty", pack_qty);
                                    hashMap.put("case_rate", case_rate);
                                    hashmapList.add(hashMap);
                                    hashMapImages.put(id,images);


                                    hashmapImagesList.add(hashMapImages);
                                }
                                showProductsData(hashmapList,hashmapImagesList);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        recyclerView.setVisibility(View.GONE);
                      //  nodata_found_txt.setVisibility(View.VISIBLE);

                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noitemsfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showProductsData(ArrayList<HashMap<String, String>> hashmapList,ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
        if (hashmapList.size() > 0) {
            ViewPagerProductsListAdapter adapter = new ViewPagerProductsListAdapter(getActivity(), hashmapList,hashmapImagesList);
            recyclerView.setAdapter(adapter);// set adapter on recyclerview
            //  nodata_text.setVisibility(View.GONE);
        } else {
            // nodata_text.setVisibility(View.VISIBLE);
        }
    }

    public class ViewPagerProductsListAdapter extends RecyclerView.Adapter<ViewPagerProductsListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> productsList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        public ViewPagerProductsListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,ArrayList<HashMap<String, List<String>>> hashmapImagesList) {
            productsList = hashmapList;
            this.mContext = mContext;
            this.hashmapImagesList=hashmapImagesList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todaydeals_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            String id = productsList.get(position).get("id");
            String brand = productsList.get(position).get("brand");
            String itemname = productsList.get(position).get("itemname");

            String mrp = productsList.get(position).get("mrp");
            String pay = productsList.get(position).get("pay");
            String suppier_name = productsList.get(position).get("suppier_name");

            String qty = productsList.get(position).get("qty");
            String bal_qty = productsList.get(position).get("bal_qty");
            String margin = productsList.get(position).get("margin");
            String discount = productsList.get(position).get("discount");
            String cart = productsList.get(position).get("cart");
            String is_bt = productsList.get(position).get("is_bt");
            String pack_qty = productsList.get(position).get("pack_qty");
            String case_rate = productsList.get(position).get("case_rate");

            if(is_bt.equals("1"))
            {
                holder.bt_btn.setVisibility(View.VISIBLE);
            }else{
                holder.bt_btn.setVisibility(View.GONE);
            }

            List<String> imagesList = hashmapImagesList.get(position).get(id);
            if(imagesList!=null)
            {
                if(imagesList.size()>0)
                {
                    Picasso.with(mContext)
                            .load(imagesList.get(0))
                            .placeholder(R.drawable.loading)
                            .into(holder.product_image);
                }else{
                    holder.product_image.setImageResource(R.drawable.noimage);
                }
            }else{
                holder.product_image.setImageResource(R.drawable.noimage);
            }

            holder.packqtytxt.setText(pack_qty);
            holder.caserate_txt.setText(case_rate);

            holder.product_name.setText(itemname);
            holder.product_mrp.setText(mrp);
            holder.product_discount.setText(discount);
            holder.product_margin.setText(margin);
            holder.product_pay.setText(pay);
           // holder.product_instock.setText(bal_qty + " In Stock");
            holder.product_instock.setVisibility(View.GONE);

            if(cart.equals("0"))
            {
                holder.qtyedittext.setText("");
            }else{
                holder.qtyedittext.setText(cart);
            }

            holder.qtyedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String count = holder.qtyedittext.getText().toString();
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Your action on done
                        String qty = productsList.get(position).get("qty");
                        //if (!qty.equals("") && Integer.parseInt(count) <= Integer.parseInt(qty)) {
                        if (!qty.equals("")) {
                            String id = productsList.get(position).get("id");
                            String sellerid = productsList.get(position).get("sellerid");

                            if(SHORTFORM.equals("SE"))
                            {
                                if(SharedDB.isDealerExists(getActivity()))
                                {
                                    HashMap<String, String> vals = SharedDB.getDealerDetails(getActivity());
                                    String DEALERID=vals.get(SharedDB.DEALERID);
                                    addtocart(id, DEALERID, count);
                                }else{
                                    loadDealers(id, sellerid, count);
                                }
                            }else{
                                addtocart(id, sellerid, count);
                            }
                          //  addtocart(id, sellerid, count);
                        } else {
                            Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                }
            });
            holder.product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = productsList.get(position).get("id");
                    List<String> imagesList = hashmapImagesList.get(position).get(id);

                    Log.e("imagesList in Adapter",""+imagesList);
                    if(imagesList!=null)
                    {
                        if(imagesList.size()>0)
                        {
                            String itemname = productsList.get(position).get("itemname");
                            globalShare.setImagesList(imagesList);
                            Intent intent = new Intent(getActivity(), ProductImageActivity.class);
                            intent.putExtra("itemname",itemname);
                            startActivity(intent);
                        }else{
                        }
                    }else{
                    }
                }
            });
            holder.bt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), BTProductDetailsActivity.class);
//                    startActivity(intent);
                    String id = productsList.get(position).get("id");
                    List<String> imagesList = hashmapImagesList.get(position).get(id);
                    if(imagesList!=null)
                    {
                        if(imagesList.size()>0)
                        {
                            String itemname = productsList.get(position).get("itemname");
                            globalShare.setImagesList(imagesList);
                            Intent intent = new Intent(getActivity(), BTProductDetailsActivity.class);
                            intent.putExtra("itemname",itemname);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }else{
                        }
                    }else{
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return productsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView cart_img, product_image;
            LinearLayout linear_cost;
            EditText qtyedittext;
            TextView product_name, product_mrp, product_pay, product_discount, product_margin,
                    product_instock,packqtytxt,caserate_txt;
            Button bt_btn;

            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_pay = view.findViewById(R.id.product_pay);
                product_discount = view.findViewById(R.id.product_discount);
                product_margin = view.findViewById(R.id.product_margin);
                product_instock = view.findViewById(R.id.product_instock);
                bt_btn = view.findViewById(R.id.bt_btn);

                qtyedittext = view.findViewById(R.id.qtyedittext);
                product_image = view.findViewById(R.id.product_image);
                cart_img = view.findViewById(R.id.cart_img);
                linear_cost = view.findViewById(R.id.linear_cost);

                packqtytxt = view.findViewById(R.id.packqtytxt);
                caserate_txt = view.findViewById(R.id.caserate_txt);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }

        public void loadDealers(final String id_val,final String sellerid_val,final String count_val) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<EmployeeDTO> mService = null;
            mService = mApiService.getDealersList(BRANCHID);
            mService.enqueue(new Callback<EmployeeDTO>() {
                @Override
                public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
                    dialog.dismiss();
                    Log.e("response", "" + response);
                    try {
                        EmployeeDTO mOrderObject = response.body();
                        String status = mOrderObject.getStatus();
                        Log.e("ordersstatus", "" + status);
                        if (Integer.parseInt(status) == 1) {
                            List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
                            if (ordersList != null) {
                                if (ordersList.size() > 0) {
                                    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                    //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                    for (int i = 0; i < ordersList.size(); i++) {
                                        HashMap<String, String> hashMap = new HashMap<String, String>();

                                        String id = ordersList.get(i).getId();
                                        String name = ordersList.get(i).getName();
                                        String first_name = ordersList.get(i).getFirst_name();
                                        String last_name = ordersList.get(i).getLast_name();
                                        String empid = ordersList.get(i).getEmp_id();
                                        String branch = ordersList.get(i).getBranch();
                                        String mobile = ordersList.get(i).getMobile();
                                        String email = ordersList.get(i).getEmail();
                                        String dob = ordersList.get(i).getDob();
                                        String address = ordersList.get(i).getAddress();
                                        String role_name = ordersList.get(i).getRole_name();
                                        String branch_name = ordersList.get(i).getBranch_name();

                                        hashMap.put("id", id);
                                        hashMap.put("name", name);
                                        hashMap.put("first_name", first_name);
                                        hashMap.put("last_name", last_name);
                                        hashMap.put("empid", empid);
                                        hashMap.put("branch", branch);
                                        hashMap.put("mobile", mobile);
                                        hashMap.put("email", email);
                                        hashMap.put("dob", dob);
                                        hashMap.put("address", address);
                                        hashMap.put("role_name", role_name);
                                        hashMap.put("branch_name", branch_name);

                                        hashmapList.add(hashMap);
                                    }
                                    showDealers(hashmapList,id_val,sellerid_val,count_val);
                                } else {
                                    Toast.makeText(getActivity(), "No dealers found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No dealers found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<EmployeeDTO> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void showDealers(ArrayList<HashMap<String, String>> hashmapList,String id_val,String sellerid_val,String count_val) {
            dealersalertDialog = new Dialog(getActivity());
            dealersalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dealersalertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            dealerlayout = inflater.inflate(R.layout.layout_dealerslist, null);
            dealersalertDialog.setContentView(dealerlayout);
            dealersalertDialog.setCancelable(true);
            if (!dealersalertDialog.isShowing()) {
                dealersalertDialog.show();
            }

            dealerrecyclerView = dealerlayout.findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            dealerrecyclerView.setHasFixedSize(true);
            dealerrecyclerView.setLayoutManager(mLayoutManager);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), hashmapList,id_val,sellerid_val,count_val);
            dealerrecyclerView.setAdapter(adapter);
        }

        public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
            private Context mContext;
            ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();
            String id_val;
            String sellerid_val;
            String count_val;

            public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                                       String id_val,String sellerid_val,String count_val) {
                this.mContext = mContext;
                employeesList = hashmapList;

                this.id_val=id_val;
                this.sellerid_val=sellerid_val;
                this.count_val=count_val;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_emplist_cardview, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, final int position) {

                String id = employeesList.get(position).get("id");
                String name = employeesList.get(position).get("name");
                String first_name = employeesList.get(position).get("first_name");
                String last_name = employeesList.get(position).get("last_name");
                String empid = employeesList.get(position).get("empid");
                String branch = employeesList.get(position).get("branch");
                String mobile = employeesList.get(position).get("mobile");
                String email = employeesList.get(position).get("email");
                String dob = employeesList.get(position).get("dob");
                String address = employeesList.get(position).get("address");
                String role_name = employeesList.get(position).get("role_name");
                String branch_name = employeesList.get(position).get("branch_name");

                holder.emp_id.setText(empid);
                holder.emp_name.setText(name);
                holder.emp_mobile.setText(mobile);
                holder.emp_location.setText(address);
                holder.emp_branch.setText(branch_name);
                holder.deleteimg.setVisibility(View.GONE);
                holder.editimg.setVisibility(View.GONE);
            }

            @Override
            public int getItemCount() {
                return employeesList.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView emp_id, emp_name, emp_mobile, emp_location, emp_branch;
                ImageView deleteimg, editimg;

                MyViewHolder(View view) {
                    super(view);
                    emp_id = view.findViewById(R.id.emp_id);
                    emp_name = view.findViewById(R.id.emp_name);
                    emp_mobile = view.findViewById(R.id.emp_mobile);
                    emp_location = view.findViewById(R.id.emp_location);
                    emp_branch = view.findViewById(R.id.emp_branch);
                    deleteimg = view.findViewById(R.id.deleteimg);
                    editimg = view.findViewById(R.id.editimg);
                    view.setOnClickListener(this);
                }

                @Override
                public void onClick(View view) {

                    String mobile = employeesList.get(getAdapterPosition()).get("mobile");
                    String dealer_id = employeesList.get(getAdapterPosition()).get("id");
                    String name = employeesList.get(getAdapterPosition()).get("name");

                    SharedDB.dealerSahred(getActivity(), dealer_id,name,mobile);

                    dealersalertDialog.dismiss();

                    boolean isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(getActivity());
                    if(isConnectedToInternet) {
                        addtocart(id_val, dealer_id, count_val);
                    }else{
                        Toast.makeText(getActivity(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        private void addtocart(String id, String sellerid, String qty) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            //Call<Product> mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty);
            Call<Product> mService=null;

            if(SHORTFORM.equals("DEALER"))
            {
                mService = mApiService.addtocart(PRIMARYID, id, PRIMARYID, qty,SHORTFORM);
            }else{
                mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty,SHORTFORM);
            }

            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    Log.e("response", "" + response);
                    Product mProductObject = response.body();

                    String status = mProductObject.getStatus();
                    Log.e("status", "" + status);
                    String message = mProductObject.getMessage();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    try {
                        if (Integer.parseInt(status) == 1) {
                            String itemscount = mProductObject.getItems_count();
                            getProductsList();
                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                            ProductListActivity.updateCartCountNotification(itemscount);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet) {
            getProductsList();
        }else{
            Toast.makeText(getActivity(),R.string.networkerror,Toast.LENGTH_SHORT).show();
        }
    }
}
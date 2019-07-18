package com.mrnovacrm.b2b_dealer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mrnovacrm.R;
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
 * Created by android on 17-03-2018.
 */

public class ViewPagerProductsListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView dealerrecyclerView;
    String subcatId;
    String categoryID,ItemId;
    //TextView nodata_text;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String BRANCHID = "";
    private String SHORTFORM = "";
    private String FROMVAL = "";
    GlobalShare globalShare;
    RelativeLayout imgrel;
    ImageView imageview;
    private Dialog dealersalertDialog;
    private View dealerlayout;
    SearchView inputSearch;
    SearchView searchView;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ViewPagerProductsListAdapter.RecyclerViewAdapterDealer delaeradapter;
    ArrayAdapter<String> adapter;
    HashMap<String, String> qtyHashmap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_productslist, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        // nodata_text = rootView.findViewById(R.id.nodata_text);

        imgrel = rootView.findViewById(R.id.imgrel);
        imageview = rootView.findViewById(R.id.imageview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle args = getArguments();
        if (args != null) {
            subcatId = args.getString("subcatId", null);
            categoryID = args.getString("categoryID", null);
            ItemId = args.getString("itemId", null);
            FROMVAL = args.getString("from", null);
        }

        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            BRANCHID = values.get(SharedDB.BRANCHID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        globalShare = (GlobalShare) getActivity().getApplicationContext();
        // getProductsList();
        return rootView;
    }

    private void getProductsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Product> mService = null;
        if (SHORTFORM.equals("SE")) {
            if (SharedDB.isDealerExists(getActivity())) {
                HashMap<String, String> VALS = SharedDB.getDealerDetails(getActivity());
                String DEALERID = VALS.get(SharedDB.DEALERID);

                if(FROMVAL.equals("search"))
                {
                    mService = mApiService.getSearchProductsList(subcatId, DEALERID, BRANCHID, categoryID,ItemId);
                }else{
                    mService = mApiService.getProductsList(subcatId, DEALERID, BRANCHID, categoryID);
                }
            } else {
                if(FROMVAL.equals("search"))
                {
                    mService = mApiService.getSearchProductsList(subcatId, PRIMARYID, BRANCHID, categoryID,ItemId);
                }else {
                    mService = mApiService.getProductsList(subcatId, PRIMARYID, BRANCHID, categoryID);
                }
            }
        } else {
            if(FROMVAL.equals("search"))
            {
                mService = mApiService.getSearchProductsList(subcatId, PRIMARYID, BRANCHID, categoryID,ItemId);
            }else{
                mService = mApiService.getProductsList(subcatId, PRIMARYID, BRANCHID, categoryID);
            }
        }

        mService.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                    try {
                    Log.e("response", "" + response);
                    Product mProductObject = response.body();
                    String status = mProductObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<ProductDetails> productsList = mProductObject.getProductDetails();
                        if (productsList != null) {
                            if (productsList.size() > 0) {
                                imgrel.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                String[] userValueArray = new String[productsList.size()];
                                String[] useridValueArray = new String[productsList.size()];
                                String[] item_balqtyValueArray = new String[productsList.size()];
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                    String id = productsList.get(i).getId();
                                    String brand = productsList.get(i).getBrand();
                                    String itemname = productsList.get(i).getItemname();
                                    String productid = productsList.get(i).getProductid();
                                    String company_mrp = productsList.get(i).getCompany_mrp();
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
                                    String branch_price_id = productsList.get(i).getBranch_price_id();
                                    String pack_qty = productsList.get(i).getPack_qty();
                                    String case_rate = productsList.get(i).getCase_rate();
                                    String caseprice = productsList.get(i).getCaseprice();

                                    userValueArray[i] = cart;
                                    item_balqtyValueArray[i] = bal_qty;
                                    useridValueArray[i]=id;

                                    List<String> images = productsList.get(i).getImagesList();
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
                                    hashMap.put("company_mrp", company_mrp);
                                    hashMap.put("caseprice", caseprice);

                                    hashmapList.add(hashMap);
                                    hashMapImages.put(id, images);
                                    hashmapImagesList.add(hashMapImages);
                                }
                                showProductsData(hashmapList, hashmapImagesList, userValueArray, item_balqtyValueArray,useridValueArray);
                            } else {
                                imgrel.setVisibility(View.VISIBLE);

                                //imageview.setImageResource(R.drawable.noitemsfound);
                                imageview.setImageResource(R.drawable.noitems);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noitemsfound);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showProductsData(ArrayList<HashMap<String, String>> hashmapList,
                                 ArrayList<HashMap<String, List<String>>> hashmapImagesList, String[] userValueArray,
                                 String[] item_balqtyValueArray,String[] useridValueArray) {
        if (hashmapList.size() > 0) {
            ViewPagerProductsListAdapter adapter = new ViewPagerProductsListAdapter(getActivity(), hashmapList,
                    hashmapImagesList, userValueArray, item_balqtyValueArray,useridValueArray);
            recyclerView.setAdapter(adapter);// set adapter on recyclerview
            imgrel.setVisibility(View.GONE);
//            recyclerView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    return false;
//                }
//            });
        } else {
            imgrel.setVisibility(View.VISIBLE);
            //imageview.setImageResource(R.drawable.noitemsfound);
            imageview.setImageResource(R.drawable.noitems);
        }
    }

    public class ViewPagerProductsListAdapter extends RecyclerView.Adapter<ViewPagerProductsListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> productsList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
        String values[];
        String[] item_balqtyValueArray;
        int doneposition = -1;
        String[] useridValueArray;

        public ViewPagerProductsListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                                            ArrayList<HashMap<String, List<String>>> hashmapImagesList, String values[],
                                            String[] item_balqtyValueArray,String[] useridValueArray) {
            productsList = hashmapList;
            this.mContext = mContext;
            this.hashmapImagesList = hashmapImagesList;
            this.values = values;
            this.item_balqtyValueArray = item_balqtyValueArray;
            this.useridValueArray=useridValueArray;
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
            String company_mrp = productsList.get(position).get("company_mrp");
            String caseprice = productsList.get(position).get("caseprice");

            if(caseprice!=null)
            {
                if(!caseprice.equals(""))
                {
                    holder.caseprice_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + caseprice);
                }
            }
            holder.indexReference = position;

            if (is_bt.equals("1")) {
                holder.bt_btn.setVisibility(View.VISIBLE);
            } else {
                holder.bt_btn.setVisibility(View.GONE);
            }

            List<String> imagesList = hashmapImagesList.get(position).get(id);
            try {
                if (imagesList != null) {
                    if (imagesList.size() > 0) {
                        Picasso.with(mContext)
                                .load(imagesList.get(0))
                                .placeholder(R.drawable.loading)
                                .into(holder.product_image);

                        //     holder.product_image.setImageResource(R.drawable.thumbnail);

                    } else {
                        holder.product_image.setImageResource(R.drawable.noimage);
                    }
                } else {
                    holder.product_image.setImageResource(R.drawable.noimage);
                }
            } catch (Exception e) {
            }

            holder.product_name.setText(itemname);
            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);

            //holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
            holder.product_discount.setText(margin);

            holder.product_margin.setText(margin);
            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + pay);

            //holder.product_instock.setText(bal_qty + " In Stock");
            holder.packqtytxt.setText(pack_qty + " PCs");
            holder.caserate_txt.setText(case_rate);
            holder.company_mrptxt.setText(mContext.getResources().getString(R.string.Rs) + " " + company_mrp);
            holder.product_instock.setVisibility(View.GONE);
            holder.casetxt.setText(case_rate + " Qty ");
            try {
                int balqty_val = Integer.parseInt(item_balqtyValueArray[holder.indexReference]);
                int qty_value = Integer.parseInt(values[holder.indexReference]);
                if (qty_value > balqty_val) {
                    holder.product_instock.setTextColor(getResources().getColor(R.color.appbackground));
                } else {
                    holder.product_instock.setTextColor(getResources().getColor(R.color.black));
                }
            } catch (Exception e) {
            }

//            if (!cart.equals("0")) {
//                holder.qtyedittext.setText(cart);
//            }

            if (values[holder.indexReference].equals("0")) {
                holder.qtyedittext.setText("");
            } else {
                holder.qtyedittext.setText(values[holder.indexReference]);
            }

            holder.qtyedittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                    }else{
                     //  hideKeyboard();
                       // Check if no view has focus:
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(holder.qtyedittext.getWindowToken(), 0);
                    }
               }
            });

 //            holder.qtyedittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus)
//                    {
//                    }else{
//                        try{
//                            if(doneposition!=position)
//                            {
//                                String count = holder.qtyedittext.getText().toString();
//                                if (!count.equals("")) {
//                                    String qty = productsList.get(position).get("qty");
//                                    if (!qty.equals("")) {
//                                        if (Long.parseLong(count) == 0) {
//                                            Toast.makeText(getActivity(), "Value should be greaterthan 0", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            String id = productsList.get(position).get("id");
//                                            String sellerid = productsList.get(position).get("sellerid");
//                                            if (SHORTFORM.equals("SE")) {
//                                                if (SharedDB.isDealerExists(getActivity())) {
//                                                    HashMap<String, String> vals = SharedDB.getDealerDetails(getActivity());
//                                                    String DEALERID = vals.get(SharedDB.DEALERID);
//                                                    addtocart(id, DEALERID, count);
//                                                } else {
//                                                    // loadDealers(id, sellerid, count);
//                                                    Intent intent = new Intent(getActivity(), DealersDataActivity.class);
//                                                    intent.putExtra("id", id);
//                                                    intent.putExtra("sellerid", sellerid);
//                                                    intent.putExtra("count", count);
//                                                    startActivity(intent);
//                                                }
//                                            } else {
//                                                addtocart(id, sellerid, count);
//                                            }
//                                        }
//                                    } else {
//                                        Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }else{
//                                //      doneposition=-1;
//                            }
//                        }catch (Exception e)
//                        {
//                        }
//                    }
//                }
//            });

            holder.qtyedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    try {
                        String count = holder.qtyedittext.getText().toString();
                        if (!count.equals("")) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                // Your action on done
                                String qty = productsList.get(position).get("qty");
                                if (!qty.equals("")) {
                                    if (Long.parseLong(count) == 0) {
                                        Toast.makeText(getActivity(), "Value should be greaterthan 0", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String id = productsList.get(position).get("id");
                                        String sellerid = productsList.get(position).get("sellerid");
                                        //Log.e("qtyHashmap", "" + qtyHashmap);
                                        if (SHORTFORM.equals("SE")) {
                                            if (SharedDB.isDealerExists(getActivity())) {
                                                HashMap<String, String> vals = SharedDB.getDealerDetails(getActivity());
                                                String DEALERID = vals.get(SharedDB.DEALERID);
                                                addtocart(id, DEALERID, count);
                                            } else {
                                                // loadDealers(id, sellerid, count);
                                                Intent intent = new Intent(getActivity(), DealersDataActivity.class);
                                                intent.putExtra("id", id);
                                                intent.putExtra("sellerid", sellerid);
                                                intent.putExtra("count", count);
                                                startActivity(intent);
                                            }
                                        } else {
                                            Log.e("qtyHashmap", "" + qtyHashmap);
//                                            qtyHashmap = new HashMap<String, String>();
                                            doneposition = position;
                                            addtocart(id, sellerid, count);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enter value", Toast.LENGTH_SHORT).show();
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
//                    String id = productsList.get(position).get("id");
//                    String qtyval = holder.qtyedittext.getText().toString().trim();
//                    if (qtyval != null || !qtyval.equals("")) {
//                        qtyHashmap.put(id, qtyval);
//                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    // uservals.add(holder.indexReference,s.toString());
                    values[holder.indexReference] = s.toString();
                    String id = useridValueArray[holder.indexReference];
                    if(s.toString()!=null || !s.toString().equals(""))
                    {
                        qtyHashmap.put(id, s.toString());
                    }
                }
            });

            holder.product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String id = productsList.get(position).get("id");
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
                                        String itemname = productsList.get(position).get("itemname");
                                       // globalShare.setImagesList(imagesList);
                                        globalShare.setImagesList(productimagesList);
                                        Intent intent = new Intent(getActivity(), ProductImageActivity.class);
                                        intent.putExtra("itemname", itemname);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                            //    Toast.makeText(getActivity(),"No images found",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }

                    }catch (Exception e)
                    {

                    }
                }
            });

            holder.bt_btn.setVisibility(View.GONE);
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
                    product_instock, packqtytxt, caserate_txt, company_mrptxt, casetxt,caseprice_pay;
            Button bt_btn;
            int indexReference;

            MyViewHolder(View view) {
                super(view);
                product_name = view.findViewById(R.id.product_name);
                product_mrp = view.findViewById(R.id.product_mrp);
                product_pay = view.findViewById(R.id.product_pay);
                product_discount = view.findViewById(R.id.product_discount);
                product_margin = view.findViewById(R.id.product_margin);
                product_instock = view.findViewById(R.id.product_instock);
                bt_btn = view.findViewById(R.id.bt_btn);

                packqtytxt = view.findViewById(R.id.packqtytxt);
                caserate_txt = view.findViewById(R.id.caserate_txt);
                company_mrptxt = view.findViewById(R.id.company_mrptxt);


                qtyedittext = view.findViewById(R.id.qtyedittext);
                product_image = view.findViewById(R.id.product_image);

                cart_img = view.findViewById(R.id.cart_img);
                linear_cost = view.findViewById(R.id.linear_cost);
                casetxt = view.findViewById(R.id.casetxt);
                caseprice_pay = view.findViewById(R.id.caseprice_pay);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }


        public void loadDealers(final String id_val, final String sellerid_val, final String count_val) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<EmployeeDTO> mService = null;
            mService = mApiService.getDealersLists(BRANCHID, SHORTFORM, PRIMARYID);
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
                                    showDealers(hashmapList, id_val, sellerid_val, count_val);
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

        public void showDealers(final ArrayList<HashMap<String, String>> hashmapList, final String id_val,
                                final String sellerid_val, final String count_val) {
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
            bindDealers(hashmapList, id_val, sellerid_val, count_val);

            searchView = dealerlayout.findViewById(R.id.inputSearch);


            searchView.setQueryHint("Search..");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String txt) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String txt) {
                    // TODO Auto-generated method stub
                    //delaeradapter.getFilter().filter(txt);
                    Log.e("txt", txt);
                    return false;
                }
            });

//           // if (inputSearch != null) {
//              //  android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
//
//                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                    @Override
//                    public boolean onClose() {
//                        //some operation
//                        return true;
//                    }
//                });
//
//                EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//                searchPlate.setHint("Search");
//                View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
//                searchPlateView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//                // use this method for search process
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        // use this method when query submitted
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//
//                        Log.e("newText",newText);
//
//                        // use this method for auto complete search process
//                        filteremployeehashmapList = new ArrayList<HashMap<String, String>>();
//                        if (hashmapList != null) {
//                            if (hashmapList.size() > 0) {
//                                for (int i = 0; i < hashmapList.size(); i++) {
//                                    String empId = hashmapList.get(i).get("empid").toLowerCase();
//                                    String empName = hashmapList.get(i).get("name").toLowerCase();
//
//                                    Log.e("newText",newText);
//                                    Log.e("empId",empId);
//                                    Log.e("empName",empName);
//
//                                    if (empId.contains(newText) || empName.contains(newText)) {
//                                        HashMap<String, String> hashMap = new HashMap<String, String>();
//                                        String id = hashmapList.get(i).get("id");
//                                        String empid = hashmapList.get(i).get("empid");
//                                        String name = hashmapList.get(i).get("name");
//                                        String role = hashmapList.get(i).get("role");
//                                        String address = hashmapList.get(i).get("address");
//
//                                        hashMap.put("id", id);
//                                        hashMap.put("empid", empid);
//                                        hashMap.put("name", name);
//                                        hashMap.put("role", role);
//                                        hashMap.put("address", address);
//
//                                        filteremployeehashmapList.add(hashMap);
//                                        bindDealers(filteremployeehashmapList,id_val,sellerid_val,count_val);
//                                    }
//                                }
//                            }
//                        }
//                        return false;
//                    }
//                });
//                SearchManager searchManager = (SearchManager)getActivity().getSystemService(SEARCH_SERVICE);
//                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//         //   }
        }

        public void bindDealers(final ArrayList<HashMap<String, String>> hashmapList, final String id_val,
                                final String sellerid_val, final String count_val) {
            delaeradapter = new RecyclerViewAdapterDealer(getActivity(), hashmapList, id_val, sellerid_val, count_val);
            dealerrecyclerView.setAdapter(delaeradapter);
        }

        public class RecyclerViewAdapterDealer extends RecyclerView.Adapter<RecyclerViewAdapterDealer.MyViewHolder> {
            private Context mContext;
            ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();
            String id_val;
            String sellerid_val;
            String count_val;

            public RecyclerViewAdapterDealer(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
                                             String id_val, String sellerid_val, String count_val) {
                this.mContext = mContext;
                employeesList = hashmapList;

                this.id_val = id_val;
                this.sellerid_val = sellerid_val;
                this.count_val = count_val;
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

                    SharedDB.dealerSahred(getActivity(), dealer_id, name, mobile);
                    dealersalertDialog.dismiss();
                    addtocart(id_val, dealer_id, count_val);
                }
            }
        }

        private void addtocart(String id, String sellerid, String qty) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Product> mService = null;
            Gson gson = new Gson();

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
            String gsonhashmap = gson.toJson(editQtyHashMap);

            Log.e("PRIMARYID", PRIMARYID);
            Log.e("id", id);
            Log.e("sellerid", PRIMARYID);
            Log.e("qty", qty);
            Log.e("SHORTFORM", SHORTFORM);
            Log.e("gsonhashmap", gsonhashmap);
//            Log.e("qtyHashmap", "" + qtyHashmap);
//            Log.e("editQtyHashMap", "" + editQtyHashMap);

            if (SHORTFORM.equals("DEALER")) {
                mService = mApiService.productaddtocart(PRIMARYID, id, PRIMARYID, qty, SHORTFORM, gsonhashmap);
            } else {
                mService = mApiService.productaddtocart(PRIMARYID, id, sellerid, qty, SHORTFORM, gsonhashmap);
            }
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response", "" + response);
                        Product mProductObject = response.body();
                        String status = mProductObject.getStatus();
                        String message = mProductObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(status) == 1) {
                            String itemscount = mProductObject.getItems_count();
//                            getProductsList();
                            qtyHashmap = new HashMap<>();
                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                            DealerScreenActivity.updateCartCountNotification(itemscount);
                            ProductListActivity.updateCartCountNotification(itemscount);
                            ProductListActivityNew.updateCartCountNotification(itemscount);
                            ProductCategoriesActivity.updateCartCountNotification(itemscount);

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    //    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
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
        try {
            if (globalShare.getBtfrom() != null) {
                if (globalShare.getBtfrom().equals("bt")) {
                    boolean isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(getActivity());
                    if (isConnectedToInternet) {

                        getProductsList();
                        globalShare.setBtfrom("other");
                    } else {
                        Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    qtyHashmap = new HashMap<String, String>();
                    getProductsList();
                }
            } else {
                getProductsList();
            }
        } catch (Exception e) {

        }
    }
}


//package com.nova.b2b_dealer;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.SearchView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.constants.CheckNetWork;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.EmployeeDTO;
//import com.nova.model.EmployeeListDTO;
//import com.nova.model.Product;
//import com.nova.model.ProductDetails;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by android on 17-03-2018.
// */
//
//public class ViewPagerProductsListFragment extends Fragment {
//
//    RecyclerView recyclerView;
//    RecyclerView dealerrecyclerView;
//    String subcatId;
//    String categoryID;
//    //TextView nodata_text;
//    private HashMap<String, String> values;
//    private String PRIMARYID = "";
//    private String USERTYPE = "";
//    private String BRANCHID = "";
//    private String SHORTFORM = "";
//    GlobalShare globalShare;
//    RelativeLayout imgrel;
//    ImageView imageview;
//    private Dialog dealersalertDialog;
//    private View dealerlayout;
//    SearchView inputSearch;
//    SearchView searchView;
//    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
//    ViewPagerProductsListAdapter.RecyclerViewAdapterDealer delaeradapter;
//    ArrayAdapter<String> adapter;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_all_productslist, container, false);
//        recyclerView = rootView.findViewById(R.id.recyclerView);
//        // nodata_text = rootView.findViewById(R.id.nodata_text);
//
//        imgrel = rootView.findViewById(R.id.imgrel);
//        imageview = rootView.findViewById(R.id.imageview);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Bundle args = getArguments();
//        if (args != null) {
//            subcatId = args.getString("subcatId", null);
//            categoryID = args.getString("categoryID", null);
//        }
//        if (SharedDB.isLoggedIn(getActivity())) {
//            values = SharedDB.getUserDetails(getActivity());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            USERTYPE = values.get(SharedDB.USERTYPE);
//            BRANCHID = values.get(SharedDB.BRANCHID);
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//        }
//        globalShare = (GlobalShare) getActivity().getApplicationContext();
//        // getProductsList();
//        return rootView;
//    }
//
//    private void getProductsList() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        Call<Product> mService=null;
//        if(SHORTFORM.equals("SE"))
//        {
//            if(SharedDB.isDealerExists(getActivity()))
//            {
//                HashMap<String, String> VALS = SharedDB.getDealerDetails(getActivity());
//                String DEALERID=VALS.get(SharedDB.DEALERID);
//                mService = mApiService.getProductsList(subcatId, DEALERID,BRANCHID,categoryID);
//            }else{
//                mService = mApiService.getProductsList(subcatId, PRIMARYID,BRANCHID,categoryID);
//            }
//        }else{
//            mService = mApiService.getProductsList(subcatId, PRIMARYID,BRANCHID,categoryID);
//        }
//            mService.enqueue(new Callback<Product>() {
//                @Override
//                public void onResponse(Call<Product> call, Response<Product> response) {
//                    dialog.dismiss();
//                    Log.e("response", "" + response);
//                    Product mProductObject = response.body();
//                    String status = mProductObject.getStatus();
//                    try {
//                        if (Integer.parseInt(status) == 1) {
//                            List<ProductDetails> productsList = mProductObject.getProductDetails();
//                            if (productsList != null) {
//                                if (productsList.size() > 0) {
//
//                                    imgrel.setVisibility(View.GONE);
//                                    recyclerView.setVisibility(View.VISIBLE);
//
//                                    //  nodata_found_txt.setVisibility(View.INVISIBLE);
//                                    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                    ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
//                                    String[] userValueArray = new String[productsList.size()];
//                                    String[] item_balqtyValueArray = new String[productsList.size()];
//                                    for (int i = 0; i < productsList.size(); i++) {
//                                        HashMap<String, String> hashMap = new HashMap<String, String>();
//                                        HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();
//
//                                        String id = productsList.get(i).getId();
//                                        String brand = productsList.get(i).getBrand();
//                                        String itemname = productsList.get(i).getItemname();
//                                        String productid = productsList.get(i).getProductid();
//                                        String company_mrp = productsList.get(i).getCompany_mrp();
//                                        String mrp = productsList.get(i).getMrp();
//                                        String pay = productsList.get(i).getPay();
//                                        String suppier_name = productsList.get(i).getSuppier_name();
//                                        String sellerid = productsList.get(i).getSellerid();
//                                        String qty = productsList.get(i).getQty();
//                                        String margin = productsList.get(i).getMargin();
//                                        String discount = productsList.get(i).getDiscount();
//                                        String cart = productsList.get(i).getCart();
//                                        String bal_qty = productsList.get(i).getBal_qty();
//                                        String is_bt = productsList.get(i).getIs_bt();
//                                        String branch_price_id = productsList.get(i).getBranch_price_id();
//                                        String pack_qty = productsList.get(i).getPack_qty();
//                                        String case_rate= productsList.get(i).getCase_rate();
//
//                                        userValueArray[i] = cart;
//                                        item_balqtyValueArray[i] = bal_qty;
//
//                                        List<String> images = productsList.get(i).getImagesList();
//
//
//                                        hashMap.put("id", id);
//                                        hashMap.put("brand", brand);
//                                        hashMap.put("itemname", itemname);
//                                        hashMap.put("productid", productid);
//                                        hashMap.put("mrp", mrp);
//                                        hashMap.put("pay", pay);
//                                        hashMap.put("suppier_name", suppier_name);
//                                        hashMap.put("sellerid", sellerid);
//                                        hashMap.put("qty", qty);
//                                        hashMap.put("margin", margin);
//                                        hashMap.put("discount", discount);
//                                        hashMap.put("cart", cart);
//                                        hashMap.put("bal_qty", bal_qty);
//                                        hashMap.put("is_bt", is_bt);
//                                        hashMap.put("pack_qty", pack_qty);
//                                        hashMap.put("case_rate", case_rate);
//                                        hashMap.put("company_mrp", company_mrp);
//
//                                        hashmapList.add(hashMap);
//                                        hashMapImages.put(id, images);
//                                        hashmapImagesList.add(hashMapImages);
//
//                                    }
//                                    showProductsData(hashmapList, hashmapImagesList, userValueArray, item_balqtyValueArray);
//                                } else {
//                                    imgrel.setVisibility(View.VISIBLE);
//                                    imageview.setImageResource(R.drawable.noitemsfound);
//                                    recyclerView.setVisibility(View.GONE);
//                                }
//                            }
//                        } else {
//                            imgrel.setVisibility(View.VISIBLE);
//                            imageview.setImageResource(R.drawable.noitemsfound);
//                            recyclerView.setVisibility(View.GONE);
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Product> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                  //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//    }
//
//    public void showProductsData(ArrayList<HashMap<String, String>> hashmapList,
//                                 ArrayList<HashMap<String, List<String>>> hashmapImagesList, String[] userValueArray,
//                                 String[] item_balqtyValueArray) {
//        if (hashmapList.size() > 0) {
//            ViewPagerProductsListAdapter adapter = new ViewPagerProductsListAdapter(getActivity(), hashmapList,
//                    hashmapImagesList, userValueArray, item_balqtyValueArray);
//            recyclerView.setAdapter(adapter);// set adapter on recyclerview
//            imgrel.setVisibility(View.GONE);
//        } else {
//            imgrel.setVisibility(View.VISIBLE);
//            imageview.setImageResource(R.drawable.noitemsfound);
//        }
//    }
//
//    public class ViewPagerProductsListAdapter extends RecyclerView.Adapter<ViewPagerProductsListAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> productsList = new ArrayList<HashMap<String, String>>();
//        ArrayList<HashMap<String, List<String>>> hashmapImagesList;
//        String values[];
//        String[] item_balqtyValueArray;
//        int doneposition=-1;
//        public ViewPagerProductsListAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
//                                            ArrayList<HashMap<String, List<String>>> hashmapImagesList, String values[], String[] item_balqtyValueArray) {
//            productsList = hashmapList;
//            this.mContext = mContext;
//            this.hashmapImagesList = hashmapImagesList;
//            this.values = values;
//            this.item_balqtyValueArray = item_balqtyValueArray;
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.todaydeals_cardview, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//            String id = productsList.get(position).get("id");
//            String brand = productsList.get(position).get("brand");
//            String itemname = productsList.get(position).get("itemname");
//            String mrp = productsList.get(position).get("mrp");
//            String pay = productsList.get(position).get("pay");
//            String suppier_name = productsList.get(position).get("suppier_name");
//            String qty = productsList.get(position).get("qty");
//            String bal_qty = productsList.get(position).get("bal_qty");
//            String margin = productsList.get(position).get("margin");
//            String discount = productsList.get(position).get("discount");
//            String cart = productsList.get(position).get("cart");
//            String is_bt = productsList.get(position).get("is_bt");
//            String pack_qty = productsList.get(position).get("pack_qty");
//            String case_rate = productsList.get(position).get("case_rate");
//            String company_mrp = productsList.get(position).get("company_mrp");
//
//            holder.indexReference = position;
//
//            if (is_bt.equals("1")) {
//                holder.bt_btn.setVisibility(View.VISIBLE);
//            } else {
//                holder.bt_btn.setVisibility(View.GONE);
//            }
//
//            List<String> imagesList = hashmapImagesList.get(position).get(id);
//            try {
//                if (imagesList != null) {
//                    if (imagesList.size() > 0) {
//
//                        Picasso.with(mContext)
//                                .load(imagesList.get(0))
//                                .placeholder(R.drawable.loading)
//                                .into(holder.product_image);
//
//                   //     holder.product_image.setImageResource(R.drawable.thumbnail);
//
//                    } else {
//                        holder.product_image.setImageResource(R.drawable.noimage);
//                    }
//                } else {
//                    holder.product_image.setImageResource(R.drawable.noimage);
//                }
//            } catch (Exception e) {
//
//            }
//
//            holder.product_name.setText(itemname);
//            holder.product_mrp.setText(mContext.getResources().getString(R.string.Rs) + " " + mrp);
//
//            //holder.product_discount.setText(mContext.getResources().getString(R.string.Rs) + " " + discount);
//            holder.product_discount.setText(margin);
//
//            holder.product_margin.setText(margin);
//            holder.product_pay.setText(mContext.getResources().getString(R.string.Rs) + " " + pay);
//
//            //holder.product_instock.setText(bal_qty + " In Stock");
//            holder.packqtytxt.setText(pack_qty+" PCs");
//            holder.caserate_txt.setText(case_rate);
//            holder.company_mrptxt.setText(mContext.getResources().getString(R.string.Rs) + " " + company_mrp);
//            holder.product_instock.setVisibility(View.GONE);
//
//            holder.casetxt.setText(case_rate+" Qty ");
//           try {
//                int balqty_val = Integer.parseInt(item_balqtyValueArray[holder.indexReference]);
//                int qty_value = Integer.parseInt(values[holder.indexReference]);
//                if (qty_value > balqty_val) {
//                    holder.product_instock.setTextColor(getResources().getColor(R.color.appbackground));
//                } else {
//                    holder.product_instock.setTextColor(getResources().getColor(R.color.black));
//                }
//            } catch (Exception e) {
//            }
//
////            if (!cart.equals("0")) {
////                holder.qtyedittext.setText(cart);
////            }
//
//            if (values[holder.indexReference].equals("0")) {
//                holder.qtyedittext.setText("");
//            } else {
//                holder.qtyedittext.setText(values[holder.indexReference]);
//            }
//
//            holder.qtyedittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus)
//                    {
//                    }else{
//                        try{
//                            if(doneposition!=position)
//                            {
//                             String count = holder.qtyedittext.getText().toString();
//                            if (!count.equals("")) {
//                                String qty = productsList.get(position).get("qty");
//                                if (!qty.equals("")) {
//                                    if (Long.parseLong(count) == 0) {
//                                        Toast.makeText(getActivity(), "Value should be greaterthan 0", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        String id = productsList.get(position).get("id");
//                                        String sellerid = productsList.get(position).get("sellerid");
//                                        if (SHORTFORM.equals("SE")) {
//                                            if (SharedDB.isDealerExists(getActivity())) {
//                                                HashMap<String, String> vals = SharedDB.getDealerDetails(getActivity());
//                                                String DEALERID = vals.get(SharedDB.DEALERID);
//                                                addtocart(id, DEALERID, count);
//                                            } else {
//                                                // loadDealers(id, sellerid, count);
//                                                Intent intent = new Intent(getActivity(), DealersDataActivity.class);
//                                                intent.putExtra("id", id);
//                                                intent.putExtra("sellerid", sellerid);
//                                                intent.putExtra("count", count);
//                                                startActivity(intent);
//                                            }
//                                        } else {
//                                            addtocart(id, sellerid, count);
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
//                                }
//                             }
//                            }else{
//                          //      doneposition=-1;
//                            }
//                        }catch (Exception e)
//                        {
//                        }
//                    }
//                }
//            });
//
//            holder.qtyedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    try{
//                        String count = holder.qtyedittext.getText().toString();
//                        if (!count.equals("")) {
//                            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                                // Your action on done
//                                String qty = productsList.get(position).get("qty");
//                                if (!qty.equals("")) {
//                                    if (Long.parseLong(count) == 0) {
//                                        Toast.makeText(getActivity(), "Value should be greaterthan 0", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        String id = productsList.get(position).get("id");
//                                        String sellerid = productsList.get(position).get("sellerid");
//
//                                        if(SHORTFORM.equals("SE"))
//                                        {
//                                            if(SharedDB.isDealerExists(getActivity()))
//                                            {
//                                                HashMap<String, String> vals = SharedDB.getDealerDetails(getActivity());
//                                                String DEALERID=vals.get(SharedDB.DEALERID);
//                                                addtocart(id, DEALERID, count);
//                                            }else{
//                                                // loadDealers(id, sellerid, count);
//                                                Intent intent=new Intent(getActivity(),DealersDataActivity.class);
//                                                intent.putExtra("id",id);
//                                                intent.putExtra("sellerid",sellerid);
//                                                intent.putExtra("count",count);
//                                                startActivity(intent);
//                                            }
//                                        }else{
//                                            doneposition=position;
//                                            addtocart(id, sellerid, count);
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_SHORT).show();
//                                }
//                                return true;
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), "Please enter value", Toast.LENGTH_SHORT).show();
//                        }
//                    }catch (Exception e)
//                    {
//
//                    }
//                    return false;
//                }
//            });
//
//            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    //  valueList[pos] = s.toString()+"~"+data.get(pos).get("attrId");
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    // uservals.add(holder.indexReference,s.toString());
//                    values[holder.indexReference] = s.toString();
//                }
//            });
//
//            holder.product_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String id = productsList.get(position).get("id");
//                    List<String> imagesList = hashmapImagesList.get(position).get(id);
//                    if (imagesList != null) {
//                        if (imagesList.size() > 0) {
//                            String itemname = productsList.get(position).get("itemname");
//                            globalShare.setImagesList(imagesList);
//                            Intent intent = new Intent(getActivity(), ProductImageActivity.class);
//                            intent.putExtra("itemname", itemname);
//                            startActivity(intent);
//                        } else {
//                        }
//                    } else {
//                    }
//                }
//            });
//
//            holder.bt_btn.setVisibility(View.GONE);
//        }
//
//        @Override
//        public int getItemCount() {
//            return productsList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            ImageView cart_img, product_image;
//            LinearLayout linear_cost;
//            EditText qtyedittext;
//            TextView product_name, product_mrp, product_pay, product_discount, product_margin,
//                    product_instock,packqtytxt,caserate_txt,company_mrptxt,casetxt;
//            Button bt_btn;
//            int indexReference;
//
//            MyViewHolder(View view) {
//                super(view);
//                product_name = view.findViewById(R.id.product_name);
//                product_mrp = view.findViewById(R.id.product_mrp);
//                product_pay = view.findViewById(R.id.product_pay);
//                product_discount = view.findViewById(R.id.product_discount);
//                product_margin = view.findViewById(R.id.product_margin);
//                product_instock = view.findViewById(R.id.product_instock);
//                bt_btn = view.findViewById(R.id.bt_btn);
//
//                packqtytxt = view.findViewById(R.id.packqtytxt);
//                caserate_txt = view.findViewById(R.id.caserate_txt);
//                company_mrptxt = view.findViewById(R.id.company_mrptxt);
//
//
//                qtyedittext = view.findViewById(R.id.qtyedittext);
//                product_image = view.findViewById(R.id.product_image);
//
//                cart_img = view.findViewById(R.id.cart_img);
//                linear_cost = view.findViewById(R.id.linear_cost);
//                casetxt = view.findViewById(R.id.casetxt);
//
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        }
//
//
//        public void loadDealers(final String id_val,final String sellerid_val,final String count_val) {
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//            Call<EmployeeDTO> mService = null;
//            mService = mApiService.getDealersLists(BRANCHID,SHORTFORM,PRIMARYID);
//            mService.enqueue(new Callback<EmployeeDTO>() {
//                @Override
//                public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
//                    dialog.dismiss();
//                    Log.e("response", "" + response);
//                    try {
//                        EmployeeDTO mOrderObject = response.body();
//                        String status = mOrderObject.getStatus();
//                        Log.e("ordersstatus", "" + status);
//                        if (Integer.parseInt(status) == 1) {
//                            List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
//                            if (ordersList != null) {
//                                if (ordersList.size() > 0) {
//                                    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                    //  nodata_found_txt.setVisibility(View.INVISIBLE);
//                                    for (int i = 0; i < ordersList.size(); i++) {
//                                        HashMap<String, String> hashMap = new HashMap<String, String>();
//
//                                        String id = ordersList.get(i).getId();
//                                        String name = ordersList.get(i).getName();
//                                        String first_name = ordersList.get(i).getFirst_name();
//                                        String last_name = ordersList.get(i).getLast_name();
//                                        String empid = ordersList.get(i).getEmp_id();
//                                        String branch = ordersList.get(i).getBranch();
//                                        String mobile = ordersList.get(i).getMobile();
//                                        String email = ordersList.get(i).getEmail();
//                                        String dob = ordersList.get(i).getDob();
//                                        String address = ordersList.get(i).getAddress();
//                                        String role_name = ordersList.get(i).getRole_name();
//                                        String branch_name = ordersList.get(i).getBranch_name();
//
//                                        hashMap.put("id", id);
//                                        hashMap.put("name", name);
//                                        hashMap.put("first_name", first_name);
//                                        hashMap.put("last_name", last_name);
//                                        hashMap.put("empid", empid);
//                                        hashMap.put("branch", branch);
//                                        hashMap.put("mobile", mobile);
//                                        hashMap.put("email", email);
//                                        hashMap.put("dob", dob);
//                                        hashMap.put("address", address);
//                                        hashMap.put("role_name", role_name);
//                                        hashMap.put("branch_name", branch_name);
//
//                                        hashmapList.add(hashMap);
//                                    }
//                                    showDealers(hashmapList,id_val,sellerid_val,count_val);
//                                } else {
//                                    Toast.makeText(getActivity(), "No dealers found", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), "No dealers found", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<EmployeeDTO> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        public void showDealers(final ArrayList<HashMap<String, String>> hashmapList,final String id_val,
//                                final String sellerid_val,final String count_val) {
//            dealersalertDialog = new Dialog(getActivity());
//            dealersalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dealersalertDialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent);
//            LayoutInflater inflater = getLayoutInflater();
//            dealerlayout = inflater.inflate(R.layout.layout_dealerslist, null);
//            dealersalertDialog.setContentView(dealerlayout);
//            dealersalertDialog.setCancelable(true);
//            if (!dealersalertDialog.isShowing()) {
//                dealersalertDialog.show();
//            }
//
//            dealerrecyclerView = dealerlayout.findViewById(R.id.recyclerView);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//            dealerrecyclerView.setHasFixedSize(true);
//            dealerrecyclerView.setLayoutManager(mLayoutManager);
//            bindDealers(hashmapList,id_val,sellerid_val,count_val);
//
//            searchView = dealerlayout.findViewById(R.id.inputSearch);
//
//
//            searchView.setQueryHint("Search..");
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//                @Override
//                public boolean onQueryTextSubmit(String txt) {
//                    // TODO Auto-generated method stub
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String txt) {
//                    // TODO Auto-generated method stub
//                    //delaeradapter.getFilter().filter(txt);
//                    Log.e("txt",txt);
//
//
//
//
//
//                    return false;
//                }
//            });
//
//
//
////           // if (inputSearch != null) {
////              //  android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
////
////                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
////                    @Override
////                    public boolean onClose() {
////                        //some operation
////                        return true;
////                    }
////                });
////
////                EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
////                searchPlate.setHint("Search");
////                View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
////                searchPlateView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
////                // use this method for search process
////                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                    @Override
////                    public boolean onQueryTextSubmit(String query) {
////                        // use this method when query submitted
////                        return false;
////                    }
////
////                    @Override
////                    public boolean onQueryTextChange(String newText) {
////
////                        Log.e("newText",newText);
////
////                        // use this method for auto complete search process
////                        filteremployeehashmapList = new ArrayList<HashMap<String, String>>();
////                        if (hashmapList != null) {
////                            if (hashmapList.size() > 0) {
////                                for (int i = 0; i < hashmapList.size(); i++) {
////                                    String empId = hashmapList.get(i).get("empid").toLowerCase();
////                                    String empName = hashmapList.get(i).get("name").toLowerCase();
////
////                                    Log.e("newText",newText);
////                                    Log.e("empId",empId);
////                                    Log.e("empName",empName);
////
////                                    if (empId.contains(newText) || empName.contains(newText)) {
////                                        HashMap<String, String> hashMap = new HashMap<String, String>();
////                                        String id = hashmapList.get(i).get("id");
////                                        String empid = hashmapList.get(i).get("empid");
////                                        String name = hashmapList.get(i).get("name");
////                                        String role = hashmapList.get(i).get("role");
////                                        String address = hashmapList.get(i).get("address");
////
////                                        hashMap.put("id", id);
////                                        hashMap.put("empid", empid);
////                                        hashMap.put("name", name);
////                                        hashMap.put("role", role);
////                                        hashMap.put("address", address);
////
////                                        filteremployeehashmapList.add(hashMap);
////                                        bindDealers(filteremployeehashmapList,id_val,sellerid_val,count_val);
////                                    }
////                                }
////                            }
////                        }
////                        return false;
////                    }
////                });
////                SearchManager searchManager = (SearchManager)getActivity().getSystemService(SEARCH_SERVICE);
////                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
////         //   }
//        }
//
//        public void bindDealers(final ArrayList<HashMap<String, String>> hashmapList,final String id_val,
//                                final String sellerid_val,final String count_val)
//        {
//            delaeradapter = new RecyclerViewAdapterDealer(getActivity(), hashmapList,id_val,sellerid_val,count_val);
//            dealerrecyclerView.setAdapter(delaeradapter);
//        }
//
//        public class RecyclerViewAdapterDealer extends RecyclerView.Adapter<RecyclerViewAdapterDealer.MyViewHolder> {
//            private Context mContext;
//            ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();
//            String id_val;
//            String sellerid_val;
//            String count_val;
//
//            public RecyclerViewAdapterDealer(Context mContext, ArrayList<HashMap<String, String>> hashmapList,
//                                       String id_val,String sellerid_val,String count_val) {
//                this.mContext = mContext;
//                employeesList = hashmapList;
//
//                this.id_val=id_val;
//                this.sellerid_val=sellerid_val;
//                this.count_val=count_val;
//            }
//
//            @Override
//            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.layout_emplist_cardview, parent, false);
//                return new MyViewHolder(itemView);
//            }
//
//            @Override
//            public void onBindViewHolder(MyViewHolder holder, final int position) {
//
//                String id = employeesList.get(position).get("id");
//                String name = employeesList.get(position).get("name");
//                String first_name = employeesList.get(position).get("first_name");
//                String last_name = employeesList.get(position).get("last_name");
//                String empid = employeesList.get(position).get("empid");
//                String branch = employeesList.get(position).get("branch");
//                String mobile = employeesList.get(position).get("mobile");
//                String email = employeesList.get(position).get("email");
//                String dob = employeesList.get(position).get("dob");
//                String address = employeesList.get(position).get("address");
//                String role_name = employeesList.get(position).get("role_name");
//                String branch_name = employeesList.get(position).get("branch_name");
//
//                holder.emp_id.setText(empid);
//                holder.emp_name.setText(name);
//                holder.emp_mobile.setText(mobile);
//                holder.emp_location.setText(address);
//                holder.emp_branch.setText(branch_name);
//                holder.deleteimg.setVisibility(View.GONE);
//                holder.editimg.setVisibility(View.GONE);
//            }
//
//            @Override
//            public int getItemCount() {
//                return employeesList.size();
//            }
//
//            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//                TextView emp_id, emp_name, emp_mobile, emp_location, emp_branch;
//                ImageView deleteimg, editimg;
//
//                MyViewHolder(View view) {
//                    super(view);
//                    emp_id = view.findViewById(R.id.emp_id);
//                    emp_name = view.findViewById(R.id.emp_name);
//                    emp_mobile = view.findViewById(R.id.emp_mobile);
//                    emp_location = view.findViewById(R.id.emp_location);
//                    emp_branch = view.findViewById(R.id.emp_branch);
//                    deleteimg = view.findViewById(R.id.deleteimg);
//                    editimg = view.findViewById(R.id.editimg);
//                    view.setOnClickListener(this);
//                }
//
//                @Override
//                public void onClick(View view) {
//
//                    String mobile = employeesList.get(getAdapterPosition()).get("mobile");
//                    String dealer_id = employeesList.get(getAdapterPosition()).get("id");
//                    String name = employeesList.get(getAdapterPosition()).get("name");
//
//                    SharedDB.dealerSahred(getActivity(), dealer_id,name,mobile);
//                    dealersalertDialog.dismiss();
//                    addtocart(id_val,dealer_id,count_val);
//                }
//            }
//        }
//
//        private void addtocart(String id, String sellerid, String qty) {
//
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//            Call<Product> mService=null;
//
//            if(SHORTFORM.equals("DEALER"))
//            {
//                mService = mApiService.addtocart(PRIMARYID, id, PRIMARYID, qty,SHORTFORM);
//            }else{
//                mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty,SHORTFORM);
//            }
//            mService.enqueue(new Callback<Product>() {
//                @Override
//                public void onResponse(Call<Product> call, Response<Product> response) {
//                    dialog.dismiss();
//                    try {
//                        Log.e("response", "" + response);
//                        Product mProductObject = response.body();
//                        String status = mProductObject.getStatus();
//                        String message = mProductObject.getMessage();
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        if (Integer.parseInt(status) == 1) {
//                            String itemscount = mProductObject.getItems_count();
////                            getProductsList();
//
//                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
//                            ProductListActivity.updateCartCountNotification(itemscount);
//                            ProductListActivityNew.updateCartCountNotification(itemscount);
//                            ProductCategoriesActivity.updateCartCountNotification(itemscount);
//
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Product> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                //    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void hideKeyboard() {
//        // Check if no view has focus:
//        View view = getActivity().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager inputManager = (InputMethodManager) getActivity()
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            if (globalShare.getBtfrom() != null) {
//                if (globalShare.getBtfrom().equals("bt")) {
//                    boolean isConnectedToInternet = CheckNetWork
//                            .isConnectedToInternet(getActivity());
//                    if(isConnectedToInternet) {
//                        getProductsList();
//                        globalShare.setBtfrom("other");
//                    }else {
//                        Toast.makeText(getActivity(),R.string.networkerror,Toast.LENGTH_SHORT);
//                    }
//                } else {
//
//                    getProductsList();
//                }
//            } else {
//                getProductsList();
//            }
//        } catch (Exception e) {
//
//        }
//    }
//}
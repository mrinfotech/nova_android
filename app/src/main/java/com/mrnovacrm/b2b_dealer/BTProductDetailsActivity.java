package com.mrnovacrm.b2b_dealer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.ProductImageAdapter;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BTDataModelDTO;
import com.mrnovacrm.model.Product;
import com.mrnovacrm.model.ProductDetails;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 02-04-2018.
 */

public class BTProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageView product_image1, product_image2, product_image3;
    ViewPager mPager;
    String prices[] = {"20", "20", "20"};
    GlobalShare globalShare;
    private List<String> imagesList;
    String id;
    EditText qtyedittext;
    TextView item_name,product_info,product_id,product_mrp,product_pay,product_yourdiscount,product_discount,product_margin,product_instock;
    Context mContext;
    private HashMap<String, String> values;
    String PRIMARYID;
    String USERTYPE;
    String SHORTFORM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_product_details);
        mPager = findViewById(R.id.pager);
        mContext=this;
        globalShare = (GlobalShare) getApplicationContext();
        Gallery gallery = (Gallery) findViewById(R.id.mygallery);
        gallery.setOnItemClickListener(BTProductDetailsActivity.this);

        if (globalShare.getImagesList() != null) {
            imagesList = globalShare.getImagesList();
            if (imagesList.size() > 0) {
                mPager.setAdapter(new ProductImageAdapter(getApplicationContext(), imagesList));
                gallery.setAdapter(new ImageAdapter(this,imagesList,0));
            }
        }

        item_name=findViewById(R.id.item_name);
        product_info=findViewById(R.id.product_info);
        product_id=findViewById(R.id.product_id);
        product_mrp=findViewById(R.id.product_mrp);
        product_pay=findViewById(R.id.product_pay);
        product_yourdiscount=findViewById(R.id.product_yourdiscount);
        product_discount=findViewById(R.id.product_discount);
        product_margin=findViewById(R.id.product_margin);
        product_instock=findViewById(R.id.product_instock);
        qtyedittext=findViewById(R.id.qtyedittext);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        Bundle bundle=getIntent().getExtras();
        String itemname=bundle.getString("itemname");
        id=bundle.getString("id");
        item_name.setText(itemname);

        addHeaders();
      //  addData();

        getProductsList();
    }

    private void getProductsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Product> mService = mApiService.getBTProductsList(id, PRIMARYID);

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
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                ArrayList<HashMap<String,String>> bulkPriceHashMapList=new ArrayList<HashMap<String,String>>();
                                for (int i = 0; i < productsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, List<String>> hashMapImages = new HashMap<String, List<String>>();

                                    final String id = productsList.get(i).getId();
                                    String brand = productsList.get(i).getBrand();
                                    String itemname = productsList.get(i).getItemname();
                                    String productid = productsList.get(i).getProductid();
                                    String mrp = productsList.get(i).getMrp();
                                    String pay = productsList.get(i).getPay();
                                    String suppier_name = productsList.get(i).getSuppier_name();
                                    final String sellerid = productsList.get(i).getSellerid();
                                    final String qty = productsList.get(i).getQty();
                                    String margin = productsList.get(i).getMargin();
                                    String discount = productsList.get(i).getDiscount();
                                    String cart = productsList.get(i).getCart();
                                    String bal_qty = productsList.get(i).getBal_qty();
                                    String is_bt = productsList.get(i).getIs_bt();

                                    item_name.setText(itemname);
                                    product_info.setText(itemname);
                                    product_id.setText(productid);
                                    product_mrp.setText(mrp);
                                    product_pay.setText(pay);
                                    product_yourdiscount.setText(discount);
                                    product_discount.setText(discount);
                                    product_margin.setText(margin);
                                    product_instock.setText(qty+" In Stock");
                                    if(!cart.equals("0"))
                                    {
                                        qtyedittext.setText(cart);
                                    }
                                    qtyedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                        @Override
                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                            String count = qtyedittext.getText().toString();
                                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                if(!count.equals(""))
                                                {
                                                    // Your action on done
                                                   // if (!qty.equals("") && Integer.parseInt(count) <= Integer.parseInt(qty)) {
                                                    if (!qty.equals("")) {
                                                        addtocart(id, sellerid, count);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Value exceeding In Stock", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                return true;
                                            }
                                            return false;
                                        }
                                    });

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
                                    hashmapList.add(hashMap);
                                    hashMapImages.put(id,images);
                                    hashmapImagesList.add(hashMapImages);

                                    List<BTDataModelDTO> btDataModelDTOList=productsList.get(i).getBtDataModelDTOList();
                                    if(btDataModelDTOList!=null)
                                    {
                                        if(btDataModelDTOList.size()>0)
                                        {
                                            for(int j=0;j<btDataModelDTOList.size();j++)
                                            {
                                                String bt_id=btDataModelDTOList.get(j).getBt_id();
                                                String bt_qty=btDataModelDTOList.get(j).getBt_qty();
                                                String bt_price=btDataModelDTOList.get(j).getBt_price();
                                                String save=btDataModelDTOList.get(j).getSave();

                                                HashMap<String,String> hashMap1=new HashMap<String,String>();
                                                hashMap1.put("bt_id",bt_id);
                                                hashMap1.put("bt_qty",bt_qty);
                                                hashMap1.put("bt_price",bt_price);
                                                hashMap1.put("save",save);
                                                bulkPriceHashMapList.add(hashMap1);
                                            }
                                        }
                                    }
                                }
                                //showProductsData(hashmapList,hashmapImagesList);
                                addData(bulkPriceHashMapList);
                            } else {
                                // nodata_found_txt.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addtocart(String id, String sellerid, String qty) {
//        Log.e("PRIMARYID", PRIMARYID);
//        Log.e("id", id);
//        Log.e("sellerid", sellerid);
//        Log.e("qty", qty);
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Product> mService=null;

        if(SHORTFORM.equals("DEALER"))
        {
            mService = mApiService.addtocart(PRIMARYID, id, PRIMARYID, qty,SHORTFORM);
        }else{
            mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty,SHORTFORM);
        }
       // Call<Product> mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty);
        mService.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                Product mProductObject = response.body();

                String status = mProductObject.getStatus();
                Log.e("status", "" + status);
                String message = mProductObject.getMessage();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                try {
                    if (Integer.parseInt(status) == 1) {
                        String itemscount = mProductObject.getItems_count();
                        DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                        DealerScreenActivity.updateCartCountNotification(itemscount);
                        ProductListActivity.updateCartCountNotification(itemscount);
                        globalShare.setBtfrom("bt");
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setBackgroundResource(R.drawable.row_border);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setBackgroundResource(R.drawable.row_border);
//        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Quantity", Color.BLACK, Typeface.BOLD, Color.WHITE));
        tr.addView(getTextView(0, "Price",Color.BLACK, Typeface.BOLD, Color.WHITE));
        tr.addView(getTextView(0, "You Save",Color.BLACK, Typeface.BOLD, Color.WHITE));
        tl.addView(tr, getTblLayoutParams());
        Log.e("Added","Headers");
    }

    /**
     * This function add the data to the table
     **/
    public void addData(ArrayList<HashMap<String,String>> bulkPriceHashMapList) {

        int numCompanies = prices.length;
        TableLayout tl = findViewById(R.id.table);

        for (int i = 0; i < bulkPriceHashMapList.size(); i++) {
            String bt_qty=bulkPriceHashMapList.get(i).get("bt_qty");
            String bt_price=bulkPriceHashMapList.get(i).get("bt_price");
            String save=bulkPriceHashMapList.get(i).get("save");

            TableRow tr = new TableRow(this);
            tr.addView(getTextView(i + 1, bt_qty, Color.BLACK, Typeface.NORMAL, Color.WHITE));
            tr.addView(getTextView(i + bulkPriceHashMapList.size(),bt_price, Color.BLACK, Typeface.NORMAL, Color.WHITE));
            tr.addView(getTextView(i + bulkPriceHashMapList.size(), save, Color.BLACK, Typeface.NORMAL, Color.WHITE));
            tl.addView(tr, getTblLayoutParams());
            Log.e("Added","Data");
        }

//        int numCompanies = prices.length;
//        TableLayout tl = findViewById(R.id.table);
//        for (int i = 0; i < numCompanies; i++) {
//            TableRow tr = new TableRow(this);
//            tr.addView(getTextView(i + 1, prices[i], Color.BLACK, Typeface.NORMAL, Color.WHITE));
//            tr.addView(getTextView(i + numCompanies, prices[i], Color.BLACK, Typeface.NORMAL, Color.WHITE));
//            tr.addView(getTextView(i + numCompanies, prices[i], Color.BLACK, Typeface.NORMAL, Color.WHITE));
//            tl.addView(tr, getTblLayoutParams());
//            Log.e("Added","Data");
//        }
    }


    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.product_image1:
//                product_image1.setBackgroundResource(R.drawable.imageview_selected_border);
//                product_image2.setBackgroundResource(R.drawable.imageview_border);
//                product_image3.setBackgroundResource(R.drawable.imageview_border);
//                mPager.setCurrentItem(0);
//                break;
//            case R.id.product_image2:
//                product_image1.setBackgroundResource(R.drawable.imageview_border);
//                product_image2.setBackgroundResource(R.drawable.imageview_selected_border);
//                product_image3.setBackgroundResource(R.drawable.imageview_border);
//                mPager.setCurrentItem(1);
//                break;
//            case R.id.product_image3:
//                product_image1.setBackgroundResource(R.drawable.imageview_border);
//                product_image2.setBackgroundResource(R.drawable.imageview_border);
//                product_image3.setBackgroundResource(R.drawable.imageview_selected_border);
//                mPager.setCurrentItem(2);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mPager.setCurrentItem(i);
    }

    public class ImageAdapter extends BaseAdapter
    {
        Context ctx;
        int itemBackground;
        List<String> imagesList;
        int pos;
        public ImageAdapter(Context ctx,List<String> imagesList,int pos)
        {
            this.ctx = ctx;
            this.imagesList=imagesList;
            this.pos=pos;
        }
        public int getCount()
        {
            return imagesList.size();
        }
        public Object getItem(int position)
        {
            return position;
        }
        public long getItemId(int position)
        {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView=new ImageView(ctx);
            //imageView.setImageResource(imageIDs[position]);
            Picasso.with(ctx)
                    .load(imagesList.get(position))
                    .placeholder(R.drawable.loading)
                    .into(imageView);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(250,300));
            imageView.setPadding(0,50,50,0);
            return imageView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

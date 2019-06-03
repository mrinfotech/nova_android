package com.mrnovacrm.b2b_dealer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.CountDrawable;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Product;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 15-03-2018.
 */

public class ProductListActivityNew extends AppCompatActivity {

    CountDrawable badge;
  //  private ViewPager mPager;
  //  private ScreenSlidePagerAdapter mPagerAdapter;
  //  private static int NUM_PAGES = 5;
  //  List<String> subcatIdList = new ArrayList<>();
  //  List<String> subcatNameList = new ArrayList<>();
  //  int childPosition;
   // String groupName;
   // private TabLayout tabLayout;
    private static ProductListActivityNew mInstance = null;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String SHORTFORM = "";
    private static String itemscount = "";
    public static Activity mainfinish;
    private String BRANCHCONTACT = "";
    public static final int RequestPermissionCode = 1;
    private final int CALL_REQUEST = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mInstance = this;
        mainfinish=this;

        setTheme(R.style.AppTheme);
        setContentView(R.layout.productlistactivity_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        String subcatId=bundle.getString("subcatId");
        String searchText=bundle.getString("searchText");
        String itemId=bundle.getString("itemId");
        setTitle(searchText);


//        if (getIntent() != null) {
//          catNameList = getIntent().getStringArrayListExtra("subcatName");
//            childPosition = getIntent().getIntExtra("childPosition", 0);
//            groupName = getIntent  subcatIdList = getIntent().getStringArrayListExtra("subcatId");
//            sub().getStringExtra("groupName");

      //  }
//
//        NUM_PAGES = subcatIdList.size();
//        mPager = findViewById(R.id.pager);
//        mPager.setPageTransformer(true, new DepthPageTransformer());
//        tabLayout = findViewById(R.id.tabs);

//        setupViewPager();
//        tabLayout.setupWithViewPager(mPager);

       // mPager.setCurrentItem(childPosition);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
        }
//        for(int i=0; i < tabLayout.getTabCount(); i++) {
//            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0, 0, 8, 0);
//            tab.requestLayout();
//        }

        requestPermission();
        getCartCount();

        Fragment fragment=new ViewPagerProductsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        Bundle args = new Bundle();
        args.putString("subcatId", subcatId);
        args.putString("categoryID", "");
        args.putString("itemId", itemId);
        args.putString("from", "search");

        fragment.setArguments(args);
        fragmentTransaction.commit();
    }

//    private void setupViewPager() {
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
//    }

    public static void updateCartCountNotification(String count) {
        itemscount = count;
        ProductListActivityNew instance = ProductListActivityNew.getInstance();
        if (instance != null) {
            instance.getCartCount(count);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setupViewPager();
//        tabLayout.setupWithViewPager(mPager);
//        if (mPager != null) {
//            int pos = mPager.getCurrentItem();
//            mPager.setCurrentItem(pos);
//        }
        Log.e("onResume", "Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "Called");
    }

    private void getCartCount() {
        if(SHORTFORM.equals("SE")) {
            if(SharedDB.isDealerExists(getApplicationContext())) {
                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
                String DEALERID = VALS.get(SharedDB.DEALERID);
                final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<Product> mService = mApiService.getCartCount(DEALERID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                            Product mProductObject = response.body();
                            if (mProductObject != null) {
                                itemscount = mProductObject.getItems_count();
                                updateCartCountNotification(itemscount);
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
            }else{
                final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<Product> mService = mApiService.getCartCount(PRIMARYID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                            Product mProductObject = response.body();
                            if (mProductObject != null) {
                                itemscount = mProductObject.getItems_count();
                                updateCartCountNotification(itemscount);
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
        }else{
            final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Product> mService = mApiService.getCartCount(PRIMARYID);
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Product mProductObject = response.body();
                        if (mProductObject != null) {
                            itemscount = mProductObject.getItems_count();
                            updateCartCountNotification(itemscount);
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
    }

    private static synchronized ProductListActivityNew getInstance() {
        return mInstance;
    }

    public void getCartCount(final String count) {
        badge.setCount(String.valueOf(count));
    }

//    public class DepthPageTransformer implements ViewPager.PageTransformer {
//        private static final float MIN_SCALE = 0.75f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            } else if (position <= 0) { // [-1,0]
//                // Use the default slide transition when moving to the left page
//                view.setAlpha(1);
//                view.setTranslationX(0);
//                view.setScaleX(1);
//                view.setScaleY(1);
//
//            } else if (position <= 1) { // (0,1]
//                // Fade the page out.
//                view.setAlpha(1 - position);
//
//                // Counteract the default slide transition
//                view.setTranslationX(pageWidth * -position);
//
//                // Scale the page down (between MIN_SCALE and 1)
//                float scaleFactor = MIN_SCALE
//                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
//        }
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(getApplicationContext());
        }

        badge.setCount(itemscount);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
                startActivity(intent);
                return true;
            case R.id.phone:
                callPhoneNumber();
                return true;


//            case R.id.barcode:
//                Intent barcode_intent = new Intent(getApplicationContext(), BarCodeScannerActivity.class);
//                startActivity(barcode_intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//        @Override
//        public Fragment getItem(int position) {
//            ViewPagerProductsListFragment fragment = new ViewPagerProductsListFragment();
//            Bundle args = new Bundle();
//            String ID = subcatIdList.get(position);
//            args.putString("subcatId", ID);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//
//        @Override
//        public int getItemPosition(@NonNull Object object) {
//            return POSITION_NONE;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return subcatNameList.get(position);
//        }
//    }

    public void callPhoneNumber() {
        if (!BRANCHCONTACT.equals("")) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(ProductListActivityNew.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                        return;
                    }
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + BRANCHCONTACT));
                startActivity(callIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Branch contact not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE},
                    RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean PhonePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    } else {
                    }
                }
                break;
        }
    }

}

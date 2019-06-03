package com.mrnovacrm.b2b_dealer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.AutoCompleteAdapter;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.SearchItemsDTO;
import com.mrnovacrm.model.SearchRecordsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 2/27/2018.
 */
public class ProductsListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout linear_location;
    int PLACESFRAGMENT = 123;
    TextView locality;
    int banner_position;
    AutoCompleteTextView autocompletetextview;
    private String PRIMARYID="";
    private String USERTYPE="";
    private String COMPANY="";
    private String searchText="";
    private boolean isConnectedToInternet;
    HashMap<String, String> itemsValueshashMap=new HashMap<String, String>();

    public ProductsListFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            banner_position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_productlist, container, false);
        Log.e("onCreate", "ProductsListFragment");


        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet) {
            getItems();
        }

        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(banner_position);
        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        linear_location = rootView.findViewById(R.id.linear_location);
      //  linear_location.setOnClickListener(ProductsListFragment.this);
        locality = rootView.findViewById(R.id.locality);

        LinearLayout branch_linear=rootView.findViewById(R.id.branch_linear);
        branch_linear.setVisibility(View.VISIBLE);

        LinearLayout location_linear=rootView.findViewById(R.id.location_linear);
        location_linear.setVisibility(View.GONE);

        TextView branch=rootView.findViewById(R.id.branch);

        autocompletetextview=rootView.findViewById(R.id.autocompletetextview);
        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            COMPANY = values.get(SharedDB.COMPANY);
            String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
            branch.setText(BRANCHNAME);
        }
        return rootView;
    }

    public void getItems()
    {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<SearchItemsDTO> mService = mApiService.searchItemsList(COMPANY);
        mService.enqueue(new Callback<SearchItemsDTO>() {
            @Override
            public void onResponse(Call<SearchItemsDTO> call, Response<SearchItemsDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                SearchItemsDTO mSearchItemsDTO = response.body();
                Log.e("mProductObject", "" + mSearchItemsDTO);

                String status = mSearchItemsDTO.getStatus();
                Log.e("status", "" + status);
                try {
                    if (Integer.parseInt(status) == 1) {
                        List<SearchRecordsDTO> itemsList = mSearchItemsDTO.getSearchRecordsDTOS();
                        if (itemsList != null) {
                            if (itemsList.size() > 0) {
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<HashMap<String, List<String>>> hashmapImagesList = new ArrayList<HashMap<String, List<String>>>();
                                HashMap<String, String> itemshashMap = new HashMap<String, String>();
                                ArrayList<String> categoryNamesList=new ArrayList<>();

                                for (int i = 0; i < itemsList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String id = itemsList.get(i).getId();
                                    String parentid = itemsList.get(i).getParentid();
                                    String parent_cateogortyitemname = itemsList.get(i).getParent_cateogorty();
                                    String categoryname = itemsList.get(i).getCategoryname();

                                    hashMap.put("id", id);
                                    hashMap.put("parentid", parentid);
                                    hashMap.put("parent_cateogortyitemname", parent_cateogortyitemname);
                                    hashMap.put("categoryname", categoryname);
                                    hashmapList.add(hashMap);

                                    itemshashMap.put(categoryname,id);
                                    categoryNamesList.add(categoryname);
                                }
                                loadItems(hashmapList,itemshashMap,categoryNamesList);
                            } else {
                            }
                        }
                    }else{

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SearchItemsDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadItems(ArrayList<HashMap<String, String>> hashmapList, final HashMap<String, String> itemshashMap, ArrayList<String> categoryNamesList)
    {
        itemsValueshashMap=itemshashMap;
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
                R.layout.searchautocomplete, R.id.searchtextview, categoryNamesList);
        autocompletetextview.setAdapter(adapter);
        autocompletetextview.setThreshold(1);
        autocompletetextview.setOnTouchListener(ProductsListFragment.this);
        autocompletetextview.setOnItemClickListener(ProductsListFragment.this);
        autocompletetextview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard();
                        searchText = autocompletetextview.getText().toString();
                        if (searchText == null || autocompletetextview.equals("")) {
                            Toast.makeText(getActivity(), "Please Select Item", Toast.LENGTH_SHORT).show();
                        } else {
                            if(itemshashMap.containsKey(searchText))
                            {
                                String subcatId=itemshashMap.get(searchText);
                                Intent intent=new Intent(getActivity(),ProductListActivityNew.class);
                                intent.putExtra("subcatId",subcatId);
                                intent.putExtra("searchText",searchText);
                                startActivity(intent);

//                                Toast.makeText(getActivity(), "itemid "+itemid, Toast.LENGTH_SHORT).show();
//
//                                ViewPagerProductsListFragment fragment = new ViewPagerProductsListFragment();
//                                Bundle args = new Bundle();
//                                args.putString("subcatId", itemid);
//                                fragment.setArguments(args);

//                                Fragment fragment=new ViewPagerProductsListFragment();
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.container_body, fragment);
//                                Bundle args = new Bundle();
//                                args.putString("subcatId", itemid);
//                                fragment.setArguments(args);
//                                fragmentTransaction.commit();
                            }
                         }
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchText = parent.getItemAtPosition(position).toString();
        hideKeyboard();
        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if (isConnectedToInternet) {
            searchText = autocompletetextview.getText().toString();
            if (searchText == null || searchText.equals("")) {
                Toast.makeText(getActivity(), "Please select item", Toast.LENGTH_SHORT).show();
            } else {
                if(itemsValueshashMap.containsKey(searchText))
                {
                    String subcatId=itemsValueshashMap.get(searchText);
                    Intent intent=new Intent(getActivity(),ProductListActivityNew.class);
                    intent.putExtra("subcatId",subcatId);
                    intent.putExtra("searchText",searchText);
                    startActivity(intent);

//                    Intent intent=new Intent(getActivity(),SearchItemsActivity.class);
//                    intent.putExtra("itemid",itemid);
//                    startActivity(intent);
                   // Toast.makeText(getActivity(), "itemid "+itemid, Toast.LENGTH_SHORT).show();

//                    ViewPagerProductsListFragment fragment = new ViewPagerProductsListFragment();
//                    Bundle args = new Bundle();
//                    args.putString("subcatId", itemid);
//                    fragment.setArguments(args);

//                    Fragment fragment=new ViewPagerProductsListFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container_body, fragment);
//                    Bundle args = new Bundle();
//                    args.putString("subcatId", itemid);
//                    fragment.setArguments(args);
//                    fragmentTransaction.commit();
                }
            }
        } else {
            Toast.makeText(getActivity(),
                    R.string.networkerror,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
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

    private void setupViewPager(ViewPager viewPager) {
        Log.e("setupViewPager", "setupViewPager");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TodayDealsFragment(), getString(R.string.todaydeals));
        adapter.addFragment(new OfferDealsFragment(), getString(R.string.offerdeals));
        adapter.addFragment(new HotDealsFragment(), getString(R.string.hotdeals));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_location:
           //     callPlaceAutocompleteActivityIntent();
                break;
            default:
                break;
        }
    }

    private void callPlaceAutocompleteActivityIntent() {
//        try {
//            Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                            .build(getActivity());
//            startActivityForResult(intent, PLACESFRAGMENT);
//        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACESFRAGMENT) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
//                Log.e("TAG", "Place:" + place.toString());
//                LatLng latLng = place.getLatLng();
//                double LAT = latLng.latitude;
//                double LNG = latLng.longitude;
//                String LOCALITY;
//                try {
//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
//
//                    addresses = geocoder.getFromLocation(LAT, LNG, 1);
//                    LOCALITY = addresses.get(0).getSubLocality();
//                    locality.setText(LOCALITY);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
//                Log.e("TAG", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//            }
//        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            Log.e("ViewPagerAdapter", "ViewPagerAdapter");
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("getItem", "getItem");
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
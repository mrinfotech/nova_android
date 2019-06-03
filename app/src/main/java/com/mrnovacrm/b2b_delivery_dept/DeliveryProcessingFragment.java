package com.mrnovacrm.b2b_delivery_dept;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.GPSTracker;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DeliveryDTO;
import com.mrnovacrm.model.DeliveryOrdersListDTO;
import com.mrnovacrm.model.DeliveryRecordsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.TransportDTO;
import com.mrnovacrm.model.TransportList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 26-04-2018.
 */

public class DeliveryProcessingFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    private Dialog alertDialog;
    View layout;
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;

    private TransparentProgressDialog dialog;
    private double source_LAT, source_LNG;
    public static final int RequestPermissionCode = 1;
    public static final int REQUEST_LOCATION = 10001;
    private static final int LocationRequestCode = 1;
    //TextView text_nodata;
    LinearLayout linear_header;
    Button btn_startprocessing;
    private TextView submittxt;
    private ImageView closeicon;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    private RecyclerView processrecyclerview;
    HashMap<String, String> processHashmap = new HashMap<>();
    HashMap<String, String> editprocessHashmap = new HashMap<>();
    Context mContext;
    private GPSTracker gps;
    private double locationLatitude1;
    private double locationLongitue1;
    private HashMap<String, String> values = new HashMap<String, String>();
    private String USERID = "";
    private RelativeLayout btmrel;
    private String is_define = "";

    int STATUSCOUNT;
    int TOTALRECORDS;
    String routeValue = "start";

    ArrayList<HashMap<String, String>> editroutehashmapList = new ArrayList<HashMap<String, String>>();
    private String DROUTEID = "";
    RelativeLayout imgrel;
    ImageView imageview;
    private String PRIMARYID;
    private String BRANCHID;


    Button transportbtn;
    EditText tranportname;
    private Dialog transportAlertDialog;
    Spinner transport_spinner;
    static EditText date;
    EditText lpnumber, vehiclenumber, drivernameedittxt, paid, topay,
            phonenumberedittext, torouteedittext, fromrouteedittext, drivermobileedittxt;
    Button save_btn;
    ArrayList<String> transportNamesList = new ArrayList<>();
    ArrayList<String> transportIDsList = new ArrayList<>();
    String TRANSPORT_ID = "";
    LinearLayout drivernamelinear, drivermobilelinear, vehiclnumberlinear, lpnumberlinear;
    private Dialog otherCircleAlertDialog;
    private String TRANSPORT_NAME="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deliveryorder_details, container, false);

        mContext = getActivity();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        //  text_nodata = rootView.findViewById(R.id.text_nodata);
        imgrel = rootView.findViewById(R.id.imgrel);
        imageview = rootView.findViewById(R.id.imageview);


        btn_startprocessing = rootView.findViewById(R.id.btn_startprocessing);

        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linear_header = rootView.findViewById(R.id.linear_header);
        linear_header.setVisibility(View.GONE);

        btmrel = rootView.findViewById(R.id.btm);

        dialog = new TransparentProgressDialog(mContext);

        values = SharedDB.getUserDetails(getActivity());
        USERID = values.get(SharedDB.PRIMARYID);
        BRANCHID = values.get(SharedDB.BRANCHID);
        Log.e("USERID", USERID);
        locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
        requestPermission();

        btn_startprocessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_define.equals("0")) {
                    btmrel.setVisibility(View.VISIBLE);
                    if (hashmapList != null) {
                        if (hashmapList.size() > 0) {
                            if (hashmapList.size() == 1) {
                                final Dialog otherCircleAlertDialog = new Dialog(getActivity());
                                String store_id = hashmapList.get(0).get("id");
                                String searchstring = store_id + "~1";
                                submitRouteOrder(searchstring, otherCircleAlertDialog, "single",
                                        "","","","","",
                                        "","","","","");
                            } else {
                                showStartProcessPopup("start","","","","","",
                                        "","","","","");
                            }
                        }
                    }
                } else if (is_define.equals("1")) {

                    if (routeValue.equals("editroute")) {
                        if (STATUSCOUNT == TOTALRECORDS) {
                            endprocessWithRetrofit();
                        } else {
                            if (editroutehashmapList != null) {
                                if (editroutehashmapList.size() > 0) {
                                    showStartProcessPopup("edit","","","","","",
                                            "","","","","");
                                }
                            }
                        }
                    }
                }
            }
        });

        firstGPSPOINT();
        //getDistanceInfo();
        getDistanceFromMap();
        //  new DistanceMatrixApiAsyncTask().execute();
        return rootView;
    }

    private void endprocessWithRetrofit() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.setDeliveryRouteProcess(DROUTEID, String.valueOf(locationLatitude1),
                String.valueOf(locationLongitue1),
                "4", "", "");
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        Fragment fragment = new DeliveryProcessingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();
                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDeliveryStoreOrderList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<DeliveryDTO> mService = mApiService.getBranchesList(USERID, BRANCHID);
        //   Call<DeliveryDTO> mService = mApiService.getDeliveryboyStoreList();
        mService.enqueue(new Callback<DeliveryDTO>() {
            @Override
            public void onResponse(Call<DeliveryDTO> call, Response<DeliveryDTO> response) {
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    DeliveryDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {

                        DROUTEID = mOrderObject.getDroute_id();
                        is_define = mOrderObject.getIs_define();
                        if (is_define.equals("0")) {
                            btmrel.setVisibility(View.VISIBLE);
                            btn_startprocessing.setText("Start Processing");
                        } else if (is_define.equals("1")) {
                            btmrel.setVisibility(View.VISIBLE);
                            btn_startprocessing.setText("Edit route");
                            routeValue = "editroute";
                        }
                        String status_count = mOrderObject.getStatus_count();
                        String total_records = mOrderObject.getTotal_records();
                        STATUSCOUNT = Integer.parseInt(status_count);
                        TOTALRECORDS = Integer.parseInt(total_records);

                        hashmapList = new ArrayList<HashMap<String, String>>();

                        editroutehashmapList = new ArrayList<HashMap<String, String>>();

                        List<DeliveryRecordsDTO> sellersList = mOrderObject.getDeliveryDTO();
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {
                                btmrel.setVisibility(View.VISIBLE);
                                HashMap<String, ArrayList<HashMap<String, String>>> storeOrders = new HashMap<String, ArrayList<HashMap<String, String>>>();
                                String[] itemStatusArray = new String[sellersList.size()];

                                for (int i = 0; i < sellersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    HashMap<String, String> editroutehashmap = new HashMap<>();

                                    String droute_id = sellersList.get(i).getDroute_id();
                                    String pkey = sellersList.get(i).getPkey();
                                    String statusval = sellersList.get(i).getStatus();
                                    String id = sellersList.get(i).getId();
                                    String route_order = sellersList.get(i).getRoute_order();
                                    String name = sellersList.get(i).getName();
                                    String latitude = sellersList.get(i).getLatitude();
                                    String longitude = sellersList.get(i).getLongitude();
                                    String address = sellersList.get(i).getAddress();
                                    String mobile = sellersList.get(i).getMobile();

                                    ArrayList<HashMap<String, String>> ordersMapList = new ArrayList<HashMap<String, String>>();
                                    /*  Accessing Orderlist of the store */
                                    List<DeliveryOrdersListDTO> deliveryOrdersListDTO = sellersList.get(i).getDeliveryOrdersListDTO();
                                    if (deliveryOrdersListDTO != null) {
                                        if (deliveryOrdersListDTO.size() > 0) {
                                            for (int j = 0; j < deliveryOrdersListDTO.size(); j++) {
                                                HashMap<String, String> ordersMap = new HashMap<String, String>();
                                                ordersMap.put("id", deliveryOrdersListDTO.get(j).getId());
                                                ordersMap.put("order_id", deliveryOrdersListDTO.get(j).getOrder_id());
                                                ordersMap.put("order_value", deliveryOrdersListDTO.get(j).getOrder_value());
                                                ordersMapList.add(ordersMap);
                                            }
                                        }
                                    }
                                    storeOrders.put(id, ordersMapList);
                                    hashMap.put("droute_id", droute_id);
                                    hashMap.put("pkey", pkey);
                                    hashMap.put("status", statusval);
                                    hashMap.put("route_order", route_order);
                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("latitude", latitude);
                                    hashMap.put("longitude", longitude);
                                    hashMap.put("address", address);
                                    hashMap.put("mobile", mobile);
                                    hashmapList.add(hashMap);

                                    itemStatusArray[i] = statusval;

                                    if (statusval.equals("0")) {
                                        editroutehashmap.put("droute_id", droute_id);
                                        editroutehashmap.put("pkey", pkey);
                                        editroutehashmap.put("status", statusval);
                                        editroutehashmap.put("route_order", route_order);
                                        editroutehashmap.put("id", id);
                                        editroutehashmap.put("name", name);
                                        editroutehashmap.put("latitude", latitude);
                                        editroutehashmap.put("longitude", longitude);
                                        editroutehashmap.put("address", address);
                                        editroutehashmap.put("mobile", mobile);
                                        editroutehashmapList.add(editroutehashmap);

                                        Log.e("editrouha size", "" + editroutehashmapList.size());
                                    }
                                }

                                if (editroutehashmapList != null) {
                                    if (editroutehashmapList.size() > 0) {
                                    } else {
                                        btmrel.setVisibility(View.VISIBLE);
                                        btn_startprocessing.setText("END");
                                    }
                                }
                                showOrdersList(hashmapList, storeOrders, itemStatusArray);
                            } else {
                                btmrel.setVisibility(View.GONE);
                            }
                        } else {
                            btmrel.setVisibility(View.GONE);
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                        recyclerView.setVisibility(View.GONE);
                        // text_nodata.setVisibility(View.VISIBLE);

                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DeliveryDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList, HashMap<String,
            ArrayList<HashMap<String, String>>> storeOrders, String[] itemStatusArray) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //  text_nodata.setVisibility(View.GONE);
            imgrel.setVisibility(View.GONE);
            DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getActivity(), hashmapList,
                    storeOrders, itemStatusArray);
            recyclerView.setAdapter(adapter);
        } else {
            //   text_nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestPermissionCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }




    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btn_startprocessing) {
//            showStartProcessPopup();
//        }
    }

    class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.e("onLocationChanged", "Location :" + location);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            source_LAT = latitude;
            source_LNG = longitude;
            if (dialog != null) {
                dialog.dismiss();
            }
//            getCompleteAddressString(latitude, longitude);
//            progressbar.setVisibility(View.GONE);
            locationManager.removeUpdates(myLocationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }





    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> hashmapList;
        HashMap<String, ArrayList<HashMap<String, String>>> storeOrders;
        String[] itemStatusArray;

        public DeliveryOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList,
                                           HashMap<String, ArrayList<HashMap<String, String>>> storeOrders,
                                           String[] itemStatusArray) {
            this.mContext = mContext;
            this.hashmapList = mhashmapList;
            this.storeOrders = storeOrders;
            this.itemStatusArray = itemStatusArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deliverydetails_newcardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            String id = hashmapList.get(position).get("id");
            String name = hashmapList.get(position).get("name");
            String mobile = hashmapList.get(position).get("mobile");
            final String latitude = hashmapList.get(position).get("latitude");
            final String longitude = hashmapList.get(position).get("longitude");
            final String address = hashmapList.get(position).get("address");
            final String status = hashmapList.get(position).get("status");
            final String droute_id = hashmapList.get(position).get("droute_id");
            final String pkey = hashmapList.get(position).get("pkey");

            holder.indexheaderpos = position;

            holder.store_name.setText(name);
            holder.store_contact.setText(mobile);
            holder.btnlnr.setVisibility(View.GONE);

            ArrayList<HashMap<String, String>> ordersList = storeOrders.get(id);

            if (is_define.equals("0")) {
                if (holder.indexheaderpos == 0) {
                }
            } else if (is_define.equals("1")) {
                Log.e("itemStatu", "" + itemStatusArray[holder.indexheaderpos]);
                if (itemStatusArray[holder.indexheaderpos].equals("3")) {
                    holder.btnlnr.setVisibility(View.VISIBLE);
                    holder.deliveredbtn.setVisibility(View.VISIBLE);
                    holder.unloadbtn.setVisibility(View.GONE);
                    holder.startbtn.setVisibility(View.GONE);

                } else if (itemStatusArray[holder.indexheaderpos].equals("1") || itemStatusArray[holder.indexheaderpos].equals("2")) {
                    holder.btnlnr.setVisibility(View.VISIBLE);
                    holder.deliveredbtn.setVisibility(View.GONE);
                    holder.unloadbtn.setVisibility(View.VISIBLE);
                    holder.startbtn.setVisibility(View.GONE);
                } else {
                    holder.btnlnr.setVisibility(View.VISIBLE);
                    holder.unloadbtn.setVisibility(View.GONE);
                    holder.startbtn.setVisibility(View.VISIBLE);
                    holder.deliveredbtn.setVisibility(View.GONE);
                    holder.startbtn.setBackground(getResources().getDrawable(R.drawable.lightgreybtnborder));
                }
            }
            holder.startbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String droute_id = hashmapList.get(position).get("droute_id");
                    String storeid = hashmapList.get(position).get("id");
                    String pkey = hashmapList.get(position).get("pkey");
                    String storename = hashmapList.get(position).get("name");
                    if (status.equals("0")) {
                        processWithRetrofit(droute_id, storeid, "1", holder, itemStatusArray, position, pkey, storename, "start");
                    }
                }
            });

            holder.unloadbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String storename = hashmapList.get(position).get("name");
                    String droute_id = hashmapList.get(position).get("droute_id");
                    String storeid = hashmapList.get(position).get("id");
                    String pkey = hashmapList.get(position).get("pkey");

                    processWithRetrofit(droute_id, storeid, "2", holder, itemStatusArray, position, pkey, storename, "unload");
                }
            });
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView store_location;
            TextView order_num, store_name, store_contact;
            RecyclerView ordersrecyclerview;
            Button startbtn, unloadbtn, deliveredbtn;
            LinearLayout btnlnr;
            int indexheaderpos;

            MyViewHolder(View view) {
                super(view);
//                store_location = view.findViewById(R.id.store_location);
//                order_num = view.findViewById(R.id.order_num);
                store_name = view.findViewById(R.id.store_name);
                store_contact = view.findViewById(R.id.store_contact);
                ordersrecyclerview = view.findViewById(R.id.ordersrecyclerview);
                btnlnr = view.findViewById(R.id.btnlnr);

                startbtn = view.findViewById(R.id.startbtn);
                unloadbtn = view.findViewById(R.id.unloadbtn);
                deliveredbtn = view.findViewById(R.id.deliveredbtn);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }

        private void processWithRetrofit(final String droute_id, final String storeid, String storestatus, final MyViewHolder holder,
                                         final String[] itemStatusArray, final int pos, final String pkey, final String storename,
                                         final String fromval) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

            Log.e("droute_id", droute_id);
            Log.e("locationLatitude1", "" + locationLatitude1);
            Log.e("locationLongitue1", "" + locationLongitue1);
            Log.e("storestatus", storestatus);
            Log.e("storeid", storeid);
            Log.e("pkey", pkey);

            Call<Login> mService = mApiService.setDeliveryRouteProcess(droute_id, String.valueOf(locationLatitude1),
                    String.valueOf(locationLongitue1),
                    storestatus, storeid, pkey);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Log.e("response", "" + response);
                    Login mLoginObject = response.body();
                    dialog.dismiss();
                    try {
                        String status = mLoginObject.getStatus();
                        if (status.equals("1")) {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            if (fromval.equals("unload")) {
                                Intent intent = new Intent(getActivity(), DeliveryOrderProcessDetailsActivity.class);
                                intent.putExtra("storename", storename);
                                intent.putExtra("pkey", pkey);
                                intent.putExtra("droute_id", droute_id);
                                intent.putExtra("storeid", storeid);
                                startActivity(intent);
                            } else {
                                holder.btnlnr.setVisibility(View.VISIBLE);
                                holder.unloadbtn.setVisibility(View.VISIBLE);
                                holder.startbtn.setVisibility(View.GONE);
                                holder.deliveredbtn.setVisibility(View.GONE);
                                itemStatusArray[holder.indexheaderpos] = "1";
                            }
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void showaddresspopup(final String deslatitude, final String destlongitude, String addressval) {
            /** Used for Show Disclaimer Pop up screen */
            alertDialog = new Dialog(getActivity());
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            layout = inflater.inflate(R.layout.layout_addresspopup, null);
            alertDialog.setContentView(layout);
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            TextView viewonmaptxt, address_txt;
            ImageView closeicon;
            viewonmaptxt = layout.findViewById(R.id.viewonmaptxt);
            address_txt = layout.findViewById(R.id.address_txt);
            address_txt.setText(addressval);
            closeicon = layout.findViewById(R.id.closeicon);
            closeicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            viewonmaptxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + source_LAT + "," + source_LNG + "&daddr=" + deslatitude + "," + destlongitude));
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            });
        }

        public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
            private Context mContext;
            int mExpandedPosition = -1;
            ArrayList<HashMap<String, String>> hashmap_List;
            String latitude;
            String longitude;
            String address;

            public OrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList, String latitude, String longitude, String address) {
                this.mContext = mContext;
                this.hashmap_List = hashmapList;
                this.latitude = latitude;
                this.longitude = longitude;
                this.address = address;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_orderslistadapter, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {

                String id = hashmap_List.get(position).get("id");
                String order_id = hashmap_List.get(position).get("order_id");
                String order_value = hashmap_List.get(position).get("order_value");

                holder.orderid_txt.setText(order_id);
                holder.orderval_txt.setText(order_value);
            }

            @Override
            public int getItemCount() {
                return hashmap_List.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                ImageView store_location;
                TextView orderid_txt, orderval_txt;

                MyViewHolder(View view) {
                    super(view);
                    orderid_txt = view.findViewById(R.id.orderid_txt);
                    orderval_txt = view.findViewById(R.id.orderval_txt);
                    view.setOnClickListener(this);
                }

                @Override
                public void onClick(View view) {

                }
            }
        }
    }

    public void showStartProcessPopup(final String fromval,
                                      final String LRNUMBER, final String ESTDATE,final String PAID_VAL, final String TO_PAY,
                                      final String MOBILE_NUMBER, final String FROM_ROUTE,
                                      final String TO_ROUTE,final String VEHICLE_NUMBER,final String DRIVER_M0BILE,
                                      final String DRIVER_NAME) {
        /** Used for Show Disclaimer Pop up screen */

        processHashmap = new HashMap<>();
        editprocessHashmap = new HashMap<>();
         otherCircleAlertDialog = new Dialog(getActivity());
        otherCircleAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otherCircleAlertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        View othterCirclelayout = inflater.inflate(R.layout.layout_startprocesspopup, null);
        otherCircleAlertDialog.setContentView(othterCirclelayout);
        otherCircleAlertDialog.setCancelable(true);
        if (!otherCircleAlertDialog.isShowing()) {
            otherCircleAlertDialog.show();
        }

        submittxt = othterCirclelayout.findViewById(R.id.submittxt);
        closeicon = othterCirclelayout.findViewById(R.id.closeicon);
        tranportname = othterCirclelayout.findViewById(R.id.tranportname);
        transportbtn = othterCirclelayout.findViewById(R.id.transportbtn);

        if(!TRANSPORT_NAME.equals(""))
        {
            tranportname.setText(TRANSPORT_NAME);
        }

        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherCircleAlertDialog.dismiss();
            }
        });
        submittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("processHashmap", "" + processHashmap);
                Log.e("editprocessHashmap", "" + editprocessHashmap);
                if (fromval.equals("edit")) {
                    if (hashmapList.size() == editprocessHashmap.size()) {
                        StringBuilder sb = new StringBuilder();
                        try {
                            for (HashMap.Entry<String, String> e : editprocessHashmap.entrySet()) {
                                Object key = e.getKey();
                                Object value = e.getValue();
                                sb.append(key);
                                sb.append("~");
                                sb.append(value);
                                sb.append(",");
                            }
                            sb.setLength(sb.length() - 1);
                            editsubmitRouteOrder(sb.toString(), otherCircleAlertDialog, "multiple");
                        } catch (Exception e) {
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please define route order", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String TRANSPORT = tranportname.getText().toString().trim();
                    if (TRANSPORT.equals("")) {
                        Toast.makeText(getActivity(), "Please select transport", Toast.LENGTH_SHORT).show();
                    }else{
                    if (hashmapList.size() == processHashmap.size()) {
                        StringBuilder sb = new StringBuilder();
                        try {
                            for (HashMap.Entry<String, String> e : processHashmap.entrySet()) {
                                Object key = e.getKey();
                                Object value = e.getValue();
                                sb.append(key);
                                sb.append("~");
                                sb.append(value);
                                sb.append(",");
                            }
                            sb.setLength(sb.length() - 1);
                            submitRouteOrder(sb.toString(), otherCircleAlertDialog, "multiple",
                                    LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                    TO_ROUTE,VEHICLE_NUMBER,DRIVER_M0BILE,DRIVER_NAME);

                        } catch (Exception e) {
                        }
                    }
                  }
                }
            }
        });
        processrecyclerview = othterCirclelayout.findViewById(R.id.processrecyclerview);
        processrecyclerview.setHasFixedSize(true);
        processrecyclerview.setVisibility(View.VISIBLE);
        processrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        showStartProcessOrdersList(fromval);

        transportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransportPopup(fromval);
            }
        });
    }

    public void showTransportPopup(final String fromval) {
        transportAlertDialog = new Dialog(getActivity());
        transportAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        transportAlertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        View transportlayout = inflater.inflate(R.layout.layout_managetransport, null);
        transportAlertDialog.setContentView(transportlayout);
        transportAlertDialog.setCancelable(true);
        if (!transportAlertDialog.isShowing()) {
            transportAlertDialog.show();
        }

        transport_spinner = transportlayout.findViewById(R.id.transport_spinner);
        lpnumber = transportlayout.findViewById(R.id.lpnumber);
        vehiclenumber = transportlayout.findViewById(R.id.vehiclenumber);
        drivernameedittxt = transportlayout.findViewById(R.id.drivernameedittxt);
        drivermobileedittxt = transportlayout.findViewById(R.id.drivermobileedittxt);
        date = transportlayout.findViewById(R.id.date);
        paid = transportlayout.findViewById(R.id.paid);
        topay = transportlayout.findViewById(R.id.topay);
        phonenumberedittext = transportlayout.findViewById(R.id.phonenumberedittext);
        save_btn = transportlayout.findViewById(R.id.save_btn);
        ImageView closeimg = transportlayout.findViewById(R.id.closeimg);

        drivernamelinear = transportlayout.findViewById(R.id.drivernamelinear);
        vehiclnumberlinear = transportlayout.findViewById(R.id.vehiclnumberlinear);
        lpnumberlinear = transportlayout.findViewById(R.id.lpnumberlinear);
        drivermobilelinear = transportlayout.findViewById(R.id.drivermobilelinear);
        fromrouteedittext = transportlayout.findViewById(R.id.fromrouteedittext);
        torouteedittext = transportlayout.findViewById(R.id.torouteedittext);

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

//        drivernamelinear.setVisibility(View.GONE);
//        drivermobilelinear.setVisibility(View.GONE);
//        vehiclnumberlinear.setVisibility(View.GONE);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LRNUMBER = lpnumber.getText().toString().trim();
                String ESTDATE = date.getText().toString().trim();
                String PAID_VAL = paid.getText().toString().trim();
                String TO_PAY = topay.getText().toString().trim();
                String MOBILE_NUMBER = phonenumberedittext.getText().toString().trim();
                String VEHICLE_NUMBER = vehiclenumber.getText().toString().trim();
                String DRIVER_NAME = drivernameedittxt.getText().toString().trim();
                String DRIVER_M0BILE = drivermobileedittxt.getText().toString().trim();
                String FROM_ROUTE = fromrouteedittext.getText().toString().trim();
                String TO_ROUTE = torouteedittext.getText().toString().trim();

                if (!TRANSPORT_ID.equals("")) {
                    if (TRANSPORT_ID.equals("0")) {
                        if (ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
                                || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
                                || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
                                || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
                                || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
                                || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")
                                || VEHICLE_NUMBER == null || "".equalsIgnoreCase(VEHICLE_NUMBER) || VEHICLE_NUMBER.equals("")
                                || DRIVER_NAME == null || "".equalsIgnoreCase(DRIVER_NAME) || DRIVER_NAME.equals("")
                                || DRIVER_M0BILE == null || "".equalsIgnoreCase(DRIVER_M0BILE) || DRIVER_M0BILE.equals("")) {
                            tranportname.setText("");
                            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                        } else {
                            tranportname.setText("");
                            if(otherCircleAlertDialog!=null)
                            {
                                otherCircleAlertDialog.dismiss();
                            }
                            transportAlertDialog.dismiss();
                            showStartProcessPopup(fromval,LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                    TO_ROUTE,VEHICLE_NUMBER,DRIVER_M0BILE,DRIVER_NAME);
                        }
                    }else{
                        if (LRNUMBER == null || "".equalsIgnoreCase(LRNUMBER) || LRNUMBER.equals("")
                                || ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
                                || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
                                || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
                                || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
                                || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
                                || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")) {
                            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                        } else {
                            tranportname.setText("");
                            if(otherCircleAlertDialog!=null)
                            {
                                otherCircleAlertDialog.dismiss();
                            }
                            transportAlertDialog.dismiss();
                            showStartProcessPopup(fromval,LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                    TO_ROUTE,VEHICLE_NUMBER,DRIVER_M0BILE,DRIVER_NAME);
                        }
                    }

//                    if (LRNUMBER == null || "".equalsIgnoreCase(LRNUMBER) || LRNUMBER.equals("")
//                            || ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
//                            || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
//                            || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
//                            || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
//                            || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
//                            || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")) {
//                        tranportname.setText("");
//                        Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (TRANSPORT_ID.equals("0")) {
//                            if (VEHICLE_NUMBER == null || "".equalsIgnoreCase(VEHICLE_NUMBER) || VEHICLE_NUMBER.equals("")
//                                    || DRIVER_NAME == null || "".equalsIgnoreCase(DRIVER_NAME) || DRIVER_NAME.equals("")
//                                    || DRIVER_M0BILE == null || "".equalsIgnoreCase(DRIVER_M0BILE) || DRIVER_M0BILE.equals("")
//                                    ) {
//                                Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                            }else{
//                                tranportname.setText("");
//                                if(otherCircleAlertDialog!=null)
//                                {
//                                    otherCircleAlertDialog.dismiss();
//                                }
//                                transportAlertDialog.dismiss();
//                                showStartProcessPopup(fromval,LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
//                                        TO_ROUTE,VEHICLE_NUMBER,DRIVER_M0BILE,DRIVER_NAME);
//
//                            }
//                        } else {
//                            tranportname.setText("");
//                            if(otherCircleAlertDialog!=null)
//                            {
//                                otherCircleAlertDialog.dismiss();
//                            }
//                            transportAlertDialog.dismiss();
//                            showStartProcessPopup(fromval,LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
//                                    TO_ROUTE,VEHICLE_NUMBER,DRIVER_M0BILE,DRIVER_NAME);
//                        }
//                    }
                } else {
                    tranportname.setText("");
                    Toast.makeText(getActivity(), "Plase select transport", Toast.LENGTH_SHORT).show();
                }
            }
        });
        closeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportAlertDialog.dismiss();
            }
        });
        hintTransport();
        getTrasportsList();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String type;
            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        date.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    public void submitDetailsWithRetorfit(final String ROUTE_ID,final String LRNUMBER,final String ESTDATE,
                                          final String PAID_VAL,
                                          final String TO_PAY,
                                          final String MOBILE_NUMBER, final String FROM_ROUTE,
                                          final String TO_ROUTE,final String VEHICLE_NUMBER,final String DRIVER_M0BILE,
                                          final String DRIVER_NAME) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        String TRANSPORTTYPE="";
        if(TRANSPORT_ID.equals("0"))
        {
            TRANSPORTTYPE="private";
        }else{
            TRANSPORTTYPE="reg";
        }

        Call<Login> mService = mApiService.addTransportDetails("0",ROUTE_ID,VEHICLE_NUMBER,DRIVER_M0BILE,
                DRIVER_NAME,LRNUMBER,TRANSPORT_ID,BRANCHID,TRANSPORTTYPE,FROM_ROUTE,TO_ROUTE,ESTDATE,
                USERID,TO_PAY,PAID_VAL,MOBILE_NUMBER);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        Fragment fragment = new DeliveryProcessingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();

                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hintTransport() {
        transportNamesList.clear();
        transportNamesList.add("Select Transport");
        transportNamesList.add("Private");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, transportNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                    TRANSPORT_ID = "0";
                    drivernamelinear.setVisibility(View.VISIBLE);
                    drivermobilelinear.setVisibility(View.VISIBLE);
                    vehiclnumberlinear.setVisibility(View.VISIBLE);
                    lpnumberlinear.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void getTrasportsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<TransportDTO> mService = mApiService.getTransportsList(BRANCHID);
        mService.enqueue(new Callback<TransportDTO>() {
            @Override
            public void onResponse(Call<TransportDTO> call, Response<TransportDTO> response) {
                Log.e("response", "" + response);
                TransportDTO mtransportObject = response.body();
                dialog.dismiss();
                try {
                    String status = mtransportObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {

                        List<TransportList> transportDTOLIST = mtransportObject.getTransportDTOS();
                        if (transportDTOLIST != null) {
                            if (transportDTOLIST.size() > 0) {
                                ArrayList<String> transport_NamesList = new ArrayList<>();
                                ArrayList<String> transport_IdsList = new ArrayList<>();

                                transport_NamesList.add("Select Transport");
                                ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                                for (int i = 0; i < transportDTOLIST.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    String id = transportDTOLIST.get(i).getId();
                                    String name = transportDTOLIST.get(i).getName();
                                    hashMap.put("id", id);
                                    hashMap.put("name", name);

                                    transport_NamesList.add(name);
                                    transport_IdsList.add(id);
                                    hashMapArrayList.add(hashMap);
                                }
                                transport_NamesList.add("Private");
                                transport_IdsList.add("0");
                                getTransportNames(hashMapArrayList, transport_NamesList, transport_IdsList);
                            }
                        }
                    } else {
                        //   String message = mtransportObject.getMessage();
                        // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TransportDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTransportNames(ArrayList<HashMap<String, String>> hashMapArrayList,
                                  final ArrayList<String> transport_NamesList, final ArrayList<String> transport_IdsList) {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, transport_NamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                    TRANSPORT_ID = transport_IdsList.get(position - 1);
                    TRANSPORT_NAME=transport_NamesList.get(position);
                    Log.e("TRANSPORT_ID", TRANSPORT_ID);

                    if (TRANSPORT_ID.equals("0")) {
                        drivernamelinear.setVisibility(View.VISIBLE);
                        drivermobilelinear.setVisibility(View.VISIBLE);
                        vehiclnumberlinear.setVisibility(View.VISIBLE);
                        lpnumberlinear.setVisibility(View.GONE);
                    } else {
                        drivernamelinear.setVisibility(View.GONE);
                        drivermobilelinear.setVisibility(View.GONE);
                        vehiclnumberlinear.setVisibility(View.GONE);
                        lpnumberlinear.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    public void submitRouteOrder(String routeorder, final Dialog otherCircleAlertDialog, final String select,
                                 final String LRNUMBER, final String ESTDATE,final String PAID_VAL, final String TO_PAY,
                                 final String MOBILE_NUMBER, final String FROM_ROUTE,
                                 final String TO_ROUTE,final String VEHICLE_NUMBER,final String DRIVER_M0BILE,final String DRIVER_NAME) {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.submitDefineRoute(USERID, String.valueOf(locationLatitude1),
                String.valueOf(locationLongitue1), routeorder);
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
//                        getOrderListDetails();
                        if (!select.equals("single")) {
                            otherCircleAlertDialog.dismiss();
                        }
                        String routeid=mLoginObject.getRoute_id();

                        if (!select.equals("single")) {
                            submitDetailsWithRetorfit(routeid, LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                    TO_ROUTE, VEHICLE_NUMBER, DRIVER_M0BILE, DRIVER_NAME);
                        }else{
                        Fragment fragment = new DeliveryProcessingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
//

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editsubmitRouteOrder(String routeorder, final Dialog otherCircleAlertDialog, final String select) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.editsubmitDefineRoute(USERID, routeorder);
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
//                        getOrderListDetails();
                        if (!select.equals("single")) {
                            otherCircleAlertDialog.dismiss();
                        }
                        Fragment fragment = new DeliveryProcessingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showStartProcessOrdersList(String fromval) {
        if (fromval.equals("edit")) {
            StartProcessOrderDetailsAdapter adapter = new StartProcessOrderDetailsAdapter(getActivity(), editroutehashmapList, fromval);
            processrecyclerview.setAdapter(adapter);
        } else {
            StartProcessOrderDetailsAdapter adapter = new StartProcessOrderDetailsAdapter(getActivity(), hashmapList, fromval);
            processrecyclerview.setAdapter(adapter);
        }
    }

    public class StartProcessOrderDetailsAdapter extends RecyclerView.Adapter<StartProcessOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> hashmapList;
        String fromval;

        public StartProcessOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList, String fromval) {
            this.mContext = mContext;
            this.hashmapList = mhashmapList;
            this.fromval = fromval;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deliveryprocess_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            String name = hashmapList.get(position).get("name");
            final String store_id = hashmapList.get(position).get("id");

            holder.store_name.setText(name);

            if (fromval.equals("edit")) {
                String route_order = hashmapList.get(position).get("route_order");
                holder.qtyedittext.setText(route_order);
            }

            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String store_id = hashmapList.get(position).get("id");
                    String pkey = hashmapList.get(position).get("pkey");
                    String qty = holder.qtyedittext.getText().toString().trim();
                    if (qty == null || qty.equals("")) {
                        if (processHashmap.containsKey(store_id)) {
                            processHashmap.remove(store_id);
                        }

                        if (editprocessHashmap.containsKey(pkey)) {
                            editprocessHashmap.remove(pkey);
                        }
                    } else {
                        processHashmap.put(store_id, holder.qtyedittext.getText().toString());
                        editprocessHashmap.put(pkey, holder.qtyedittext.getText().toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView store_name;
            EditText qtyedittext;

            MyViewHolder(View view) {
                super(view);
                store_name = view.findViewById(R.id.store_name);
                qtyedittext = view.findViewById(R.id.qtyedittext);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }
    }

    public void firstGPSPOINT() {
        mContext = getActivity();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            //	Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, getActivity());
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                locationLatitude1 = latitude;
                locationLongitue1 = longitude;
                try {
                    // Getting address from found locations.
                    Geocoder geocoder;

                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    // you can get more details other than this . like country code,
                    // state code, etc.
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps = new GPSTracker(mContext, getActivity());
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                    //Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void getDistanceFromMap() {
        Log.e("getDistanceFromMap", "yes");
        JSONObject mreq = new JSONObject();
        JsonObjectRequest movieReq = new JsonObjectRequest("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=17.738639,83.323809&destinations=17.928554,83.424811", mreq,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MAP Response", response.toString());
                        try {
                            String status = "";
                            if (response.has("status")) {
                                status = response.getString("status");
                                Log.e("status***", status);
                            }
                            if (status.equals("OK")) {
                                JSONArray destaddressarray = response.getJSONArray("destination_addresses");
                                JSONArray originaddressarray = response.getJSONArray("origin_addresses");

                                JSONArray rows = response.getJSONArray("rows");
                                if (rows.length() > 0) {
                                    for (int i = 0; i < rows.length(); i++) {
                                        JSONObject jsonObject = rows.getJSONObject(i);
                                        JSONArray jsonArrayElements = jsonObject.getJSONArray("elements");

                                        for (int j = 0; j < jsonArrayElements.length(); j++) {
                                            JSONObject json = jsonArrayElements.getJSONObject(j);
                                            if (json.has("status")) {
                                                String status1 = json.getString("status");
                                                Log.e("status1^^^^^^^^^^", status1);
                                            }

                                            if (json.has("distance")) {
                                                JSONObject json_distance = json.getJSONObject("distance");
                                                String distance = json_distance.getString("text");
                                                Log.e("distance^^^^^^^^^^", distance);
                                            }
                                            if (json.has("duration")) {
                                                JSONObject json_duration = json.getJSONObject("duration");
                                                String duration = json_duration.getString("text");
                                                Log.e("duration^^^^^^^^^^", duration);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("OPENTAB", "Error: " + error.getMessage());
                Log.e("OPENTAB", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        GlobalShare.getInstance().addToRequestQueue(movieReq);
    }

    public class DistanceMatrixApiAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=&origins=17.738639,83.323809&destinations=17.928554,83.424811");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sr = new StringBuilder();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sr.append(line);
                    }
                    String jsonStr = sr.toString();
                    Log.e("jsonstring", jsonStr);
                    parseJSON(jsonStr);
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void parseJSON(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                Log.e("jsonObject", "" + jsonObject);
                JSONArray jsonArray = jsonObject.getJSONArray("destination_addresses");
                JSONArray originjsonArray = jsonObject.getJSONArray("origin_addresses");
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDeliveryStoreOrderList();
    }
}


//package com.nova.b2b_delivery_dept;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.nova.R;
//import com.nova.constants.GPSTracker;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.DeliveryDTO;
//import com.nova.model.DeliveryOrdersListDTO;
//import com.nova.model.DeliveryRecordsDTO;
//import com.nova.model.Login;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by android on 26-04-2018.
// */
//
//public class DeliveryProcessingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
//
//    RecyclerView recyclerView;
//    private Dialog alertDialog;
//    View layout;
//    private LocationManager locationManager;
//    private MyLocationListener myLocationListener;
//    private GoogleApiClient googleApiClient;
//    private LocationRequest locationRequest;
//    LocationSettingsRequest.Builder locationSettingsRequest;
//    private PendingResult<LocationSettingsResult> pendingResult;
//    private TransparentProgressDialog dialog;
//    private double source_LAT, source_LNG;
//    public static final int RequestPermissionCode = 1;
//    public static final int REQUEST_LOCATION = 10001;
//    private static final int LocationRequestCode = 1;
//    //TextView text_nodata;
//    LinearLayout linear_header;
//    Button btn_startprocessing;
//    private TextView submittxt;
//    private ImageView closeicon;
//    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//    private RecyclerView processrecyclerview;
//    HashMap<String, String> processHashmap = new HashMap<>();
//    HashMap<String, String> editprocessHashmap = new HashMap<>();
//    Context mContext;
//    private GPSTracker gps;
//    private double locationLatitude1;
//    private double locationLongitue1;
//    private HashMap<String, String> values = new HashMap<String, String>();
//    private String USERID = "";
//    private RelativeLayout btmrel;
//    private String is_define = "";
//
//    int STATUSCOUNT;
//    int TOTALRECORDS;
//    String routeValue = "start";
//
//    ArrayList<HashMap<String, String>> editroutehashmapList = new ArrayList<HashMap<String, String>>();
//    private String DROUTEID="";
//    RelativeLayout imgrel;
//    ImageView imageview;
//    private String PRIMARYID;
//    private String BRANCHID;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_deliveryorder_details, container, false);
//
//        mContext = getActivity();
//
//        recyclerView = rootView.findViewById(R.id.recyclerView);
//      //  text_nodata = rootView.findViewById(R.id.text_nodata);
//        imgrel=rootView.findViewById(R.id.imgrel);
//        imageview=rootView.findViewById(R.id.imageview);
//
//
//        btn_startprocessing = rootView.findViewById(R.id.btn_startprocessing);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setVisibility(View.VISIBLE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        linear_header = rootView.findViewById(R.id.linear_header);
//        linear_header.setVisibility(View.GONE);
//
//        btmrel = rootView.findViewById(R.id.btm);
//
//        dialog = new TransparentProgressDialog(mContext);
//
//        values = SharedDB.getUserDetails(getActivity());
//        USERID = values.get(SharedDB.PRIMARYID);
//        BRANCHID = values.get(SharedDB.BRANCHID);
//        Log.e("USERID", USERID);
//        locationManager = (LocationManager) getActivity()
//                .getSystemService(Context.LOCATION_SERVICE);
//        myLocationListener = new MyLocationListener();
//        requestPermission();
//
//        btn_startprocessing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (is_define.equals("0")) {
//                    btmrel.setVisibility(View.VISIBLE);
//                    if (hashmapList != null) {
//                        if (hashmapList.size() > 0) {
//                            if (hashmapList.size() == 1) {
//                                final Dialog otherCircleAlertDialog = new Dialog(getActivity());
//                                String store_id = hashmapList.get(0).get("id");
//                                String searchstring = store_id + "~1";
//                                submitRouteOrder(searchstring, otherCircleAlertDialog, "single");
//                            } else {
//                                showStartProcessPopup("start");
//                            }
//                        }
//                    }
//                } else if (is_define.equals("1")) {
//
//                    if (routeValue.equals("editroute")) {
//                        if (STATUSCOUNT == TOTALRECORDS) {
//                            endprocessWithRetrofit();
//                        } else {
//                            if (editroutehashmapList != null) {
//                                if (editroutehashmapList.size() > 0) {
//                                    showStartProcessPopup("edit");
//                                }
//                            }
//                        }
//                    }
////                    btmrel.setVisibility(View.VISIBLE);
////                    if(hashmapList!=null)
////                    {
////                        if(hashmapList.size()>0)
////                        {
////                            if(hashmapList.size()==1)
////                            {
////                                final Dialog otherCircleAlertDialog = new Dialog(getActivity());
////                                String store_id = hashmapList.get(0).get("id");
////                                String searchstring=store_id+"~1";
////                                submitRouteOrder(searchstring,otherCircleAlertDialog,"single");
////                            }else{
////                                showStartProcessPopup("edit");
////                            }
////                        }
////                    }
//                }
//            }
//        });
//
//        firstGPSPOINT();
//        //getDistanceInfo();
//        getDistanceFromMap();
//        //  new DistanceMatrixApiAsyncTask().execute();
//        return rootView;
//    }
//
//    private void endprocessWithRetrofit() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        Call<Login> mService = mApiService.setDeliveryRouteProcess(DROUTEID, String.valueOf(locationLatitude1),
//                String.valueOf(locationLongitue1),
//                "4", "", "");
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                Log.e("response", "" + response);
//                Login mLoginObject = response.body();
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    if (status.equals("1")) {
//                        String message = mLoginObject.getMessage();
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//
//                        Fragment fragment = new DeliveryProcessingFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.container_body, fragment);
//                        fragmentTransaction.commit();
//                    } else {
//                        String message = mLoginObject.getMessage();
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getDeliveryStoreOrderList() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//
//
//        Call<DeliveryDTO> mService = mApiService.getBranchesList(USERID,BRANCHID);
//        //   Call<DeliveryDTO> mService = mApiService.getDeliveryboyStoreList();
//        mService.enqueue(new Callback<DeliveryDTO>() {
//            @Override
//            public void onResponse(Call<DeliveryDTO> call, Response<DeliveryDTO> response) {
//                dialog.dismiss();
//                try {
//                    Log.e("response", "" + response);
//                    DeliveryDTO mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//                    if (Integer.parseInt(status) == 1) {
//
//                        DROUTEID = mOrderObject.getDroute_id();
//                        is_define = mOrderObject.getIs_define();
//                        if (is_define.equals("0")) {
//                            btmrel.setVisibility(View.VISIBLE);
//                            btn_startprocessing.setText("Start Processing");
//                        } else if (is_define.equals("1")) {
//                            btmrel.setVisibility(View.VISIBLE);
//                            btn_startprocessing.setText("Edit route");
//                            routeValue = "editroute";
//                        }
//                        String status_count = mOrderObject.getStatus_count();
//                        String total_records = mOrderObject.getTotal_records();
//                        STATUSCOUNT = Integer.parseInt(status_count);
//                        TOTALRECORDS = Integer.parseInt(total_records);
//
////                        if(STATUSCOUNT==TOTALRECORDS)
////                        {
////                            routeValue="end";
////                            btmrel.setVisibility(View.VISIBLE);
////                            btn_startprocessing.setText("END");
////                        }
//
//
//                        hashmapList = new ArrayList<HashMap<String, String>>();
//
//                        editroutehashmapList = new ArrayList<HashMap<String, String>>();
//
//                        List<DeliveryRecordsDTO> sellersList = mOrderObject.getDeliveryDTO();
//                        if (sellersList != null) {
//                            if (sellersList.size() > 0) {
//                                btmrel.setVisibility(View.VISIBLE);
//                                HashMap<String, ArrayList<HashMap<String, String>>> storeOrders = new HashMap<String, ArrayList<HashMap<String, String>>>();
//                                String[] itemStatusArray = new String[sellersList.size()];
//
//                                for (int i = 0; i < sellersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    HashMap<String, String> editroutehashmap = new HashMap<>();
//
//                                    String droute_id = sellersList.get(i).getDroute_id();
//                                    String pkey = sellersList.get(i).getPkey();
//                                    String statusval = sellersList.get(i).getStatus();
//                                    String id = sellersList.get(i).getId();
//                                    String route_order = sellersList.get(i).getRoute_order();
//                                    String name = sellersList.get(i).getName();
//                                    String latitude = sellersList.get(i).getLatitude();
//                                    String longitude = sellersList.get(i).getLongitude();
//                                    String address = sellersList.get(i).getAddress();
//                                    String mobile = sellersList.get(i).getMobile();
//
//                                    ArrayList<HashMap<String, String>> ordersMapList = new ArrayList<HashMap<String, String>>();
//                                    /*  Accessing Orderlist of the store */
//                                    List<DeliveryOrdersListDTO> deliveryOrdersListDTO = sellersList.get(i).getDeliveryOrdersListDTO();
//                                    if (deliveryOrdersListDTO != null) {
//                                        if (deliveryOrdersListDTO.size() > 0) {
//                                            for (int j = 0; j < deliveryOrdersListDTO.size(); j++) {
//                                                HashMap<String, String> ordersMap = new HashMap<String, String>();
//                                                ordersMap.put("id", deliveryOrdersListDTO.get(j).getId());
//                                                ordersMap.put("order_id", deliveryOrdersListDTO.get(j).getOrder_id());
//                                                ordersMap.put("order_value", deliveryOrdersListDTO.get(j).getOrder_value());
//                                                ordersMapList.add(ordersMap);
//                                            }
//                                        }
//                                    }
//                                    storeOrders.put(id, ordersMapList);
//                                    hashMap.put("droute_id", droute_id);
//                                    hashMap.put("pkey", pkey);
//                                    hashMap.put("status", statusval);
//                                    hashMap.put("route_order", route_order);
//                                    hashMap.put("id", id);
//                                    hashMap.put("name", name);
//                                    hashMap.put("latitude", latitude);
//                                    hashMap.put("longitude", longitude);
//                                    hashMap.put("address", address);
//                                    hashMap.put("mobile", mobile);
//                                    hashmapList.add(hashMap);
//
//                                    itemStatusArray[i] = statusval;
//
//                                    if (statusval.equals("0")) {
//                                        editroutehashmap.put("droute_id", droute_id);
//                                        editroutehashmap.put("pkey", pkey);
//                                        editroutehashmap.put("status", statusval);
//                                        editroutehashmap.put("route_order", route_order);
//                                        editroutehashmap.put("id", id);
//                                        editroutehashmap.put("name", name);
//                                        editroutehashmap.put("latitude", latitude);
//                                        editroutehashmap.put("longitude", longitude);
//                                        editroutehashmap.put("address", address);
//                                        editroutehashmap.put("mobile", mobile);
//                                        editroutehashmapList.add(editroutehashmap);
//
//                                        Log.e("editrouha size",""+editroutehashmapList.size());
//                                    }
//                                }
//
//                                if (editroutehashmapList != null) {
//                                    if (editroutehashmapList.size() > 0) {
//                                    } else {
//                                        btmrel.setVisibility(View.VISIBLE);
//                                        btn_startprocessing.setText("END");
//                                    }
//                                }
//                                showOrdersList(hashmapList, storeOrders, itemStatusArray);
//                            } else {
//                                btmrel.setVisibility(View.GONE);
//                            }
//                        } else {
//                            btmrel.setVisibility(View.GONE);
//                        }
//                    } else {
//                        String message = mOrderObject.getMessage();
//                        recyclerView.setVisibility(View.GONE);
//                       // text_nodata.setVisibility(View.VISIBLE);
//
//                        imgrel.setVisibility(View.VISIBLE);
//                        imageview.setImageResource(R.drawable.noordersfound);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeliveryDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList, HashMap<String,
//            ArrayList<HashMap<String, String>>> storeOrders, String[] itemStatusArray) {
//        if (hashmapList.size() > 0) {
//            recyclerView.setVisibility(View.VISIBLE);
//          //  text_nodata.setVisibility(View.GONE);
//            imgrel.setVisibility(View.GONE);
//            DeliveryOrderDetailsAdapter adapter = new DeliveryOrderDetailsAdapter(getActivity(), hashmapList,
//                    storeOrders, itemStatusArray);
//            recyclerView.setAdapter(adapter);
//        } else {
//         //   text_nodata.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//            imgrel.setVisibility(View.VISIBLE);
//            imageview.setImageResource(R.drawable.noordersfound);
//        }
//    }
//
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION},
//                    RequestPermissionCode);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_LOCATION) {
//            if (resultCode == Activity.RESULT_OK) {
//                Log.e("onActivityResult", "Called");
////                progressbar.setVisibility(View.VISIBLE);
//                getLocation();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//    }
//
//    public void mEnableGps() {
//        Log.e("mEnableGps", "Called");
//        googleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API).addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        googleApiClient.connect();
//        mLocationSetting();
//    }
//
//    void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // to handle the case where the user grants the permission. See the documentation
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        dialog.show();
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
////        if (v.getId() == R.id.btn_startprocessing) {
////            showStartProcessPopup();
////        }
//    }
//
//    class MyLocationListener implements android.location.LocationListener {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.e("onLocationChanged", "Location :" + location);
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            source_LAT = latitude;
//            source_LNG = longitude;
//            if (dialog != null) {
//                dialog.dismiss();
//            }
////            getCompleteAddressString(latitude, longitude);
////            progressbar.setVisibility(View.GONE);
//            locationManager.removeUpdates(myLocationListener);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }
//
//    public void mLocationSetting() {
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1 * 1000);
//        locationRequest.setFastestInterval(1 * 1000);
//
//        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        mResult();
//
//
//    }
//
//    public void mResult() {
//        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
//        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//                Status status = locationSettingsResult.getStatus();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        try {
//                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
//                        } catch (Exception e) {
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        //  Toast.makeText(getActivity(),"SETTINGS_CHANGE_UNAVAILABLE CALLED",Toast.LENGTH_SHORT).show();
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//    }
//
//    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> hashmapList;
//        HashMap<String, ArrayList<HashMap<String, String>>> storeOrders;
//        String[] itemStatusArray;
//
//        public DeliveryOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList,
//                                           HashMap<String, ArrayList<HashMap<String, String>>> storeOrders,
//                                           String[] itemStatusArray) {
//            this.mContext = mContext;
//            this.hashmapList = mhashmapList;
//            this.storeOrders = storeOrders;
//            this.itemStatusArray = itemStatusArray;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.deliverydetails_newcardview, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//            String id = hashmapList.get(position).get("id");
//            String name = hashmapList.get(position).get("name");
//            String mobile = hashmapList.get(position).get("mobile");
//            final String latitude = hashmapList.get(position).get("latitude");
//            final String longitude = hashmapList.get(position).get("longitude");
//            final String address = hashmapList.get(position).get("address");
//            final String status = hashmapList.get(position).get("status");
//            final String droute_id = hashmapList.get(position).get("droute_id");
//            final String pkey = hashmapList.get(position).get("pkey");
//
//            holder.indexheaderpos = position;
//
//            holder.store_name.setText(name);
//            holder.store_contact.setText(mobile);
//            holder.btnlnr.setVisibility(View.GONE);
//
//            ArrayList<HashMap<String, String>> ordersList = storeOrders.get(id);
//
////            if(ordersList!=null)
////            {
////                if(ordersList.size()>0)
////                {
////                    holder.ordersrecyclerview.setHasFixedSize(true);
////                    holder.ordersrecyclerview.setVisibility(View.VISIBLE);
////                    holder.ordersrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
////                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(mContext, ordersList,latitude,longitude,address);
////                    holder.ordersrecyclerview.setAdapter(adapter);
////                }
////            }
//
//            if (is_define.equals("0")) {
//                if (holder.indexheaderpos == 0) {
//                }
//            } else if (is_define.equals("1")) {
//                Log.e("itemStatu",""+itemStatusArray[holder.indexheaderpos]);
//                if (itemStatusArray[holder.indexheaderpos].equals("3")) {
//                    holder.btnlnr.setVisibility(View.VISIBLE);
//                    holder.deliveredbtn.setVisibility(View.VISIBLE);
//                    holder.unloadbtn.setVisibility(View.GONE);
//                    holder.startbtn.setVisibility(View.GONE);
//
//                }else  if (itemStatusArray[holder.indexheaderpos].equals("1") || itemStatusArray[holder.indexheaderpos].equals("2")) {
//                    holder.btnlnr.setVisibility(View.VISIBLE);
//                    holder.deliveredbtn.setVisibility(View.GONE);
//                    holder.unloadbtn.setVisibility(View.VISIBLE);
//                    holder.startbtn.setVisibility(View.GONE);
//                }else{
//                    holder.btnlnr.setVisibility(View.VISIBLE);
//                    holder.unloadbtn.setVisibility(View.GONE);
//                    holder.startbtn.setVisibility(View.VISIBLE);
//                    holder.deliveredbtn.setVisibility(View.GONE);
//                    holder.startbtn.setBackground(getResources().getDrawable(R.drawable.lightgreybtnborder));
//                }
//
////                Log.e("itemStatu",""+itemStatusArray[holder.indexheaderpos]);
////
////                if (itemStatusArray[holder.indexheaderpos].equals("3")) {
////                    holder.deliveredbtn.setVisibility(View.VISIBLE);
////                    holder.unloadbtn.setVisibility(View.GONE);
////                    holder.startbtn.setVisibility(View.GONE);
////
////
////
////                } else if (itemStatusArray[holder.indexheaderpos].equals("1") || itemStatusArray[holder.indexheaderpos].equals("2")) {
//////                    holder.btnlnr.setVisibility(View.VISIBLE);
//////
//////                    holder.deliveredbtn.setVisibility(View.VISIBLE);
//////                    holder.unloadbtn.setVisibility(View.GONE);
//////                    holder.startbtn.setVisibility(View.GONE);
////
////                    holder.btnlnr.setVisibility(View.VISIBLE);
////                    holder.deliveredbtn.setVisibility(View.GONE);
////                    holder.unloadbtn.setVisibility(View.VISIBLE);
////                    holder.startbtn.setVisibility(View.GONE);
////                }
//////                else if (itemStatusArray[holder.indexheaderpos].equals("1")) {
//////                    holder.btnlnr.setVisibility(View.VISIBLE);
//////                    holder.deliveredbtn.setVisibility(View.GONE);
//////                    holder.unloadbtn.setVisibility(View.VISIBLE);
//////                    holder.startbtn.setVisibility(View.GONE);
//////                }
////                else if (itemStatusArray[holder.indexheaderpos].equals("0") & holder.indexheaderpos == 0) {
////                    holder.btnlnr.setVisibility(View.VISIBLE);
////                    holder.unloadbtn.setVisibility(View.GONE);
////                    holder.startbtn.setVisibility(View.VISIBLE);
////                    holder.deliveredbtn.setVisibility(View.GONE);
////                    holder.startbtn.setBackground(getResources().getDrawable(R.drawable.lightgreybtnborder));
////                }
//            }
//
//            holder.startbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String droute_id = hashmapList.get(position).get("droute_id");
//                    String storeid = hashmapList.get(position).get("id");
//                    String pkey = hashmapList.get(position).get("pkey");
//                    String storename = hashmapList.get(position).get("name");
//
//                    if (status.equals("0")) {
////                        holder.btnlnr.setVisibility(View.VISIBLE);
////                        holder.unloadbtn.setVisibility(View.VISIBLE);
////                        holder.startbtn.setVisibility(View.GONE);
//                        processWithRetrofit(droute_id, storeid, "1", holder, itemStatusArray, position, pkey, storename, "start");
//                    }
//                }
//            });
//
//            holder.unloadbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    String name = hashmapList.get(position).get("name");
////                    String pkey = hashmapList.get(position).get("pkey");
////                    String droute_id = hashmapList.get(position).get("droute_id");
////                    String storeid = hashmapList.get(position).get("id");
////                    Intent intent=new Intent(getActivity(),DeliveryOrderProcessDetailsActivity.class);
////                    intent.putExtra("storename",name);
////                    intent.putExtra("pkey",pkey);
////                    intent.putExtra("droute_id",droute_id);
////                    intent.putExtra("storeid",storeid);
////                    startActivity(intent);
//
//                    String storename = hashmapList.get(position).get("name");
//                    String droute_id = hashmapList.get(position).get("droute_id");
//                    String storeid = hashmapList.get(position).get("id");
//                    String pkey = hashmapList.get(position).get("pkey");
//
//                    processWithRetrofit(droute_id, storeid, "2", holder, itemStatusArray, position, pkey, storename, "unload");
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return hashmapList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            ImageView store_location;
//            TextView order_num, store_name, store_contact;
//            RecyclerView ordersrecyclerview;
//            Button startbtn, unloadbtn, deliveredbtn;
//            LinearLayout btnlnr;
//            int indexheaderpos;
//
//            MyViewHolder(View view) {
//                super(view);
////                store_location = view.findViewById(R.id.store_location);
////                order_num = view.findViewById(R.id.order_num);
//                store_name = view.findViewById(R.id.store_name);
//                store_contact = view.findViewById(R.id.store_contact);
//                ordersrecyclerview = view.findViewById(R.id.ordersrecyclerview);
//                btnlnr = view.findViewById(R.id.btnlnr);
//
//                startbtn = view.findViewById(R.id.startbtn);
//                unloadbtn = view.findViewById(R.id.unloadbtn);
//                deliveredbtn = view.findViewById(R.id.deliveredbtn);
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//            }
//        }
//
//        private void processWithRetrofit(final String droute_id, final String storeid, String storestatus, final MyViewHolder holder,
//                                         final String[] itemStatusArray, final int pos, final String pkey, final String storename,
//                                         final String fromval) {
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//            Log.e("droute_id",droute_id);
//            Log.e("locationLatitude1",""+locationLatitude1);
//            Log.e("locationLongitue1",""+locationLongitue1);
//            Log.e("storestatus",storestatus);
//            Log.e("storeid",storeid);
//            Log.e("pkey",pkey);
//
//               Call<Login> mService = mApiService.setDeliveryRouteProcess(droute_id, String.valueOf(locationLatitude1),
//                    String.valueOf(locationLongitue1),
//                    storestatus, storeid, pkey);
//            mService.enqueue(new Callback<Login>() {
//                @Override
//                public void onResponse(Call<Login> call, Response<Login> response) {
//                    Log.e("response", "" + response);
//                    Login mLoginObject = response.body();
//                    dialog.dismiss();
//                    try {
//                        String status = mLoginObject.getStatus();
//                        if (status.equals("1")) {
//                            String message = mLoginObject.getMessage();
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                            if (fromval.equals("unload")) {
//                                Intent intent = new Intent(getActivity(), DeliveryOrderProcessDetailsActivity.class);
//                                intent.putExtra("storename", storename);
//                                intent.putExtra("pkey", pkey);
//                                intent.putExtra("droute_id", droute_id);
//                                intent.putExtra("storeid", storeid);
//                                startActivity(intent);
//                            } else {
//                                holder.btnlnr.setVisibility(View.VISIBLE);
//                                holder.unloadbtn.setVisibility(View.VISIBLE);
//                                holder.startbtn.setVisibility(View.GONE);
//                                holder.deliveredbtn.setVisibility(View.GONE);
//                                itemStatusArray[holder.indexheaderpos] = "1";
//                            }
//                        } else {
//                            String message = mLoginObject.getMessage();
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Login> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        public void showaddresspopup(final String deslatitude, final String destlongitude, String addressval) {
//            /** Used for Show Disclaimer Pop up screen */
//            alertDialog = new Dialog(getActivity());
//            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            alertDialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent);
//            LayoutInflater inflater = getLayoutInflater();
//            layout = inflater.inflate(R.layout.layout_addresspopup, null);
//            alertDialog.setContentView(layout);
//            alertDialog.setCancelable(true);
//            if (!alertDialog.isShowing()) {
//                alertDialog.show();
//            }
//            TextView viewonmaptxt, address_txt;
//            ImageView closeicon;
//            viewonmaptxt = layout.findViewById(R.id.viewonmaptxt);
//            address_txt = layout.findViewById(R.id.address_txt);
//            address_txt.setText(addressval);
//            closeicon = layout.findViewById(R.id.closeicon);
//            closeicon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                }
//            });
//            viewonmaptxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://maps.google.com/maps?saddr=" + source_LAT + "," + source_LNG + "&daddr=" + deslatitude + "," + destlongitude));
//                    startActivity(intent);
//                    alertDialog.dismiss();
//                }
//            });
//        }
//
//        public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
//            private Context mContext;
//            int mExpandedPosition = -1;
//            ArrayList<HashMap<String, String>> hashmap_List;
//            String latitude;
//            String longitude;
//            String address;
//
//            public OrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList, String latitude, String longitude, String address) {
//                this.mContext = mContext;
//                this.hashmap_List = hashmapList;
//                this.latitude = latitude;
//                this.longitude = longitude;
//                this.address = address;
//            }
//
//            @Override
//            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.layout_orderslistadapter, parent, false);
//                return new MyViewHolder(itemView);
//            }
//
//            @Override
//            public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//                String id = hashmap_List.get(position).get("id");
//                String order_id = hashmap_List.get(position).get("order_id");
//                String order_value = hashmap_List.get(position).get("order_value");
//
//                holder.orderid_txt.setText(order_id);
//                holder.orderval_txt.setText(order_value);
//            }
//
//            @Override
//            public int getItemCount() {
//                return hashmap_List.size();
//            }
//
//            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//                ImageView store_location;
//                TextView orderid_txt, orderval_txt;
//
//                MyViewHolder(View view) {
//                    super(view);
//                    orderid_txt = view.findViewById(R.id.orderid_txt);
//                    orderval_txt = view.findViewById(R.id.orderval_txt);
//                    view.setOnClickListener(this);
//                }
//
//                @Override
//                public void onClick(View view) {
////                    int pos=getAdapterPosition();
////                    String idval=hashmap_List.get(pos).get("id");
////                    String order_id=hashmap_List.get(pos).get("order_id");
////                    Intent intent = new Intent(getContext(), DeliveryDetailsActivity.class);
////                    intent.putExtra("id", idval);
////                    intent.putExtra("latitude", latitude);
////                    intent.putExtra("longitude", longitude);
////                    intent.putExtra("order_id", order_id);
////                    intent.putExtra("address", address);
////                    startActivity(intent);
//                }
//            }
//        }
//    }
//
//    public void showStartProcessPopup(final String fromval) {
//        /** Used for Show Disclaimer Pop up screen */
//
//        processHashmap = new HashMap<>();
//        editprocessHashmap = new HashMap<>();
//        final Dialog otherCircleAlertDialog = new Dialog(getActivity());
//        otherCircleAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        otherCircleAlertDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
//        LayoutInflater inflater = getLayoutInflater();
//        View othterCirclelayout = inflater.inflate(R.layout.layout_startprocesspopup, null);
//        otherCircleAlertDialog.setContentView(othterCirclelayout);
//        otherCircleAlertDialog.setCancelable(true);
//        if (!otherCircleAlertDialog.isShowing()) {
//            otherCircleAlertDialog.show();
//        }
//
//        submittxt = othterCirclelayout.findViewById(R.id.submittxt);
//        closeicon = othterCirclelayout.findViewById(R.id.closeicon);
//        closeicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                otherCircleAlertDialog.dismiss();
//            }
//        });
//        submittxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("processHashmap", "" + processHashmap);
//                Log.e("editprocessHashmap", "" + editprocessHashmap);
//
//
//                if (fromval.equals("edit")) {
//                    if (hashmapList.size() == editprocessHashmap.size()) {
//                        StringBuilder sb = new StringBuilder();
//                        try {
//                            for (HashMap.Entry<String, String> e : editprocessHashmap.entrySet()) {
//                                Object key = e.getKey();
//                                Object value = e.getValue();
//                                sb.append(key);
//                                sb.append("~");
//                                sb.append(value);
//                                sb.append(",");
//                            }
//                            sb.setLength(sb.length() - 1);
//                            editsubmitRouteOrder(sb.toString(), otherCircleAlertDialog, "multiple");
//                        } catch (Exception e) {
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Please define route order", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                    if (hashmapList.size() == processHashmap.size()) {
//                        StringBuilder sb = new StringBuilder();
//                        try {
//                            for (HashMap.Entry<String, String> e : processHashmap.entrySet()) {
//                                Object key = e.getKey();
//                                Object value = e.getValue();
//                                sb.append(key);
//                                sb.append("~");
//                                sb.append(value);
//                                sb.append(",");
//                            }
//                            sb.setLength(sb.length() - 1);
//                            submitRouteOrder(sb.toString(), otherCircleAlertDialog, "multiple");
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//
//                //                if(hashmapList.size()==processHashmap.size()) {
////                    if(fromval.equals("edit"))
////                    {
////
////
////
////                    }else{
////                    StringBuilder sb = new StringBuilder();
////                    try {
////                        for (HashMap.Entry<String, String> e : processHashmap.entrySet()) {
////                            Object key = e.getKey();
////                            Object value = e.getValue();
////                            sb.append(key);
////                            sb.append("~");
////                            sb.append(value);
////                            sb.append(",");
////                        }
////                        sb.setLength(sb.length() - 1);
////                       submitRouteOrder(sb.toString(),otherCircleAlertDialog,"multiple");
////                    } catch (Exception e) {
////                    }
////                    }
////                }else{
////                    Toast.makeText(getActivity(),"Please define route order",Toast.LENGTH_SHORT).show();
////                }
//
////                if(hashmapList.size()==processHashmap.size())
////                {
////                    StringBuilder sb = new StringBuilder();
////                    try {
////                        for (HashMap.Entry<String, String> e : processHashmap.entrySet()) {
////                            Object key = e.getKey();
////                            Object value = e.getValue();
////                            sb.append(key);
////                            sb.append("~");
////                            sb.append(value);
////                            sb.append(",");
////                        }
////                        sb.setLength(sb.length() - 1);
////                       submitRouteOrder(sb.toString(),otherCircleAlertDialog,"multiple");
////                    } catch (Exception e) {
////                    }
////                }else{
////                    Toast.makeText(getActivity(),"Please define route order",Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//        processrecyclerview = othterCirclelayout.findViewById(R.id.processrecyclerview);
//        processrecyclerview.setHasFixedSize(true);
//        processrecyclerview.setVisibility(View.VISIBLE);
//        processrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        showStartProcessOrdersList(fromval);
//    }
//
//    public void submitRouteOrder(String routeorder, final Dialog otherCircleAlertDialog, final String select) {
//        Log.e("USERID",USERID);
//        Log.e("lat",String.valueOf(locationLatitude1));
//        Log.e("lng",String.valueOf(locationLongitue1));
//        Log.e("routeorder",routeorder);
//
//
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = mApiService.submitDefineRoute(USERID, String.valueOf(locationLatitude1),
//                String.valueOf(locationLongitue1), routeorder);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
//                Login mLoginObject = response.body();
//                Log.e("response", " :" + response);
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    String message = mLoginObject.getMessage();
//                    if (status.equals("1")) {
////                        getOrderListDetails();
//                        if (!select.equals("single")) {
//                            otherCircleAlertDialog.dismiss();
//                        }
//                        Fragment fragment = new DeliveryProcessingFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.container_body, fragment);
//                        fragmentTransaction.commit();
//
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Log.e("Throwable", " :" + t.getMessage());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void editsubmitRouteOrder(String routeorder, final Dialog otherCircleAlertDialog, final String select) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = mApiService.editsubmitDefineRoute(USERID,routeorder);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
//                Login mLoginObject = response.body();
//                Log.e("response", " :" + response);
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    String message = mLoginObject.getMessage();
//                    if (status.equals("1")) {
////                        getOrderListDetails();
//                        if (!select.equals("single")) {
//                            otherCircleAlertDialog.dismiss();
//                        }
//                        Fragment fragment = new DeliveryProcessingFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.container_body, fragment);
//                        fragmentTransaction.commit();
//
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Log.e("Throwable", " :" + t.getMessage());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showStartProcessOrdersList(String fromval) {
//        if(fromval.equals("edit"))
//        {
//            StartProcessOrderDetailsAdapter adapter = new StartProcessOrderDetailsAdapter(getActivity(), editroutehashmapList, fromval);
//            processrecyclerview.setAdapter(adapter);
//        }else{
//            StartProcessOrderDetailsAdapter adapter = new StartProcessOrderDetailsAdapter(getActivity(), hashmapList, fromval);
//            processrecyclerview.setAdapter(adapter);
//        }
//    }
//
//    public class StartProcessOrderDetailsAdapter extends RecyclerView.Adapter<StartProcessOrderDetailsAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> hashmapList;
//        String fromval;
//
//        public StartProcessOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList, String fromval) {
//            this.mContext = mContext;
//            this.hashmapList = mhashmapList;
//            this.fromval = fromval;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.deliveryprocess_cardview, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//            String name = hashmapList.get(position).get("name");
//            final String store_id = hashmapList.get(position).get("id");
//
//            holder.store_name.setText(name);
//
//            if (fromval.equals("edit")) {
//                String route_order = hashmapList.get(position).get("route_order");
//                holder.qtyedittext.setText(route_order);
//            }
//
//            holder.qtyedittext.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                    String qty=holder.qtyedittext.getText().toString().trim();
////                    if(qty==null || qty.equals(""))
////                    {
////                        if(processHashmap.containsKey(store_id))
////                        {
////                            processHashmap.remove(store_id);
////                        }
////                    }else{
////                        processHashmap.put(store_id, holder.qtyedittext.getText().toString());
////                    }
//                    //    Log.e("processHashmap",""+processHashmap);
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    String store_id = hashmapList.get(position).get("id");
//                    String pkey = hashmapList.get(position).get("pkey");
//                    String qty = holder.qtyedittext.getText().toString().trim();
//                    if (qty == null || qty.equals("")) {
//                        if (processHashmap.containsKey(store_id)) {
//                            processHashmap.remove(store_id);
//                        }
//
//                        if (editprocessHashmap.containsKey(pkey)) {
//                            editprocessHashmap.remove(pkey);
//                        }
//                    } else {
//                        processHashmap.put(store_id, holder.qtyedittext.getText().toString());
//                        editprocessHashmap.put(pkey, holder.qtyedittext.getText().toString());
//                    }
//
////                    String qtyval=holder.qtyedittext.getText().toString().trim();
////                    if(qtyval!=null || !qtyval.equals("")) {
////                        if(holder.item_checkbox.isChecked())
////                        {
////                            qtyHashmap.put(id, qtyval);
////                        }
////                    }
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return hashmapList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView store_name;
//            EditText qtyedittext;
//
//            MyViewHolder(View view) {
//                super(view);
//                store_name = view.findViewById(R.id.store_name);
//                qtyedittext = view.findViewById(R.id.qtyedittext);
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
////                String id = hashmapList.get(getAdapterPosition()).get("id");
////                String latitude = hashmapList.get(getAdapterPosition()).get("latitude");
////                String longitude = hashmapList.get(getAdapterPosition()).get("longitude");
////                String address = hashmapList.get(getAdapterPosition()).get("address");
////                Intent intent = new Intent(getContext(), DeliveryDetailsActivity.class);
////                intent.putExtra("id", id);
////                intent.putExtra("latitude", latitude);
////                intent.putExtra("longitude", longitude);
////                intent.putExtra("address", address);
////                startActivity(intent);
//            }
//        }
//    }
//
//    public void firstGPSPOINT() {
//        mContext = getActivity();
//        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//        } else {
//            //	Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
//            gps = new GPSTracker(mContext, getActivity());
//            if (gps.canGetLocation()) {
//
//                double latitude = gps.getLatitude();
//                double longitude = gps.getLongitude();
//                locationLatitude1 = latitude;
//                locationLongitue1 = longitude;
//                try {
//                    // Getting address from found locations.
//                    Geocoder geocoder;
//
//                    List<Address> addresses;
//                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
//
//                    // you can get more details other than this . like country code,
//                    // state code, etc.
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                gps.showSettingsAlert();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    gps = new GPSTracker(mContext, getActivity());
//                    if (gps.canGetLocation()) {
//                        double latitude = gps.getLatitude();
//                        double longitude = gps.getLongitude();
//                    } else {
//                        gps.showSettingsAlert();
//                    }
//                } else {
//                    //Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//        }
//    }
//
//    public void getDistanceFromMap() {
//        Log.e("getDistanceFromMap", "yes");
//        JSONObject mreq = new JSONObject();
//        JsonObjectRequest movieReq = new JsonObjectRequest("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=17.738639,83.323809&destinations=17.928554,83.424811", mreq,
//                new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("MAP Response", response.toString());
//                        try {
//                            String status = "";
//                            if (response.has("status")) {
//                                status = response.getString("status");
//                                Log.e("status***", status);
//                            }
//                            if (status.equals("OK")) {
//                                JSONArray destaddressarray = response.getJSONArray("destination_addresses");
//                                JSONArray originaddressarray = response.getJSONArray("origin_addresses");
//
//                                JSONArray rows = response.getJSONArray("rows");
//                                if (rows.length() > 0) {
//                                    for (int i = 0; i < rows.length(); i++) {
//                                        JSONObject jsonObject = rows.getJSONObject(i);
//                                        JSONArray jsonArrayElements = jsonObject.getJSONArray("elements");
//
//                                        for (int j = 0; j < jsonArrayElements.length(); j++) {
//                                            JSONObject json = jsonArrayElements.getJSONObject(j);
//                                            if (json.has("status")) {
//                                                String status1 = json.getString("status");
//                                                Log.e("status1^^^^^^^^^^", status1);
//                                            }
//
//                                            if (json.has("distance")) {
//                                                JSONObject json_distance = json.getJSONObject("distance");
//                                                String distance = json_distance.getString("text");
//                                                Log.e("distance^^^^^^^^^^", distance);
//                                            }
//                                            if (json.has("duration")) {
//                                                JSONObject json_duration = json.getJSONObject("duration");
//                                                String duration = json_duration.getString("text");
//                                                Log.e("duration^^^^^^^^^^", duration);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
////                            JSONArray destaddressarray = response.getJSONArray("destination_addresses");
////                            JSONArray originaddressarray = response.getJSONArray("origin_addresses");
////                            JSONArray rows = response.getJSONArray("rows");
////                            if (rows.length() > 0) {
////                                for (int i = 0; i < rows.length(); i++) {
////                                    JSONObject jsonObject = rows.getJSONObject(i);
////                                    JSONArray jsonArraysubject = jsonObject.getJSONArray("elements");
////                                    if (jsonArraysubject != null) {
////                                        if (jsonArraysubject.length() > 0) {
////                                            for (int j = 0; j < jsonArraysubject.length(); j++) {
////                                                JSONObject distanceObject = rows.getJSONObject(i);
////                                                JSONObject destobj = distanceObject.getJSONObject("distance");
////                                                if(destobj.has("text"))
////                                                {
////                                                    String distance=destobj.getString("text");
////                                                    Log.e("distance",distance);
////                                                }
////                                                JSONObject durobj = distanceObject.getJSONObject("duration");
////                                                if(durobj.has("text"))
////                                                {
////                                                    String duriation=destobj.getString("text");
////                                                    Log.e("duriation",duriation);
////                                                }
////                                            }
////                                        }
////                                    }
////                                }
////                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("OPENTAB", "Error: " + error.getMessage());
//                Log.e("OPENTAB", "Error: " + error.getMessage());
//            }
//        });
//        // Adding request to request queue
//        GlobalShare.getInstance().addToRequestQueue(movieReq);
//    }
//
//    public class DistanceMatrixApiAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=&origins=17.738639,83.323809&destinations=17.928554,83.424811");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    StringBuilder sr = new StringBuilder();
//                    String line = "";
//                    while ((line = br.readLine()) != null) {
//                        sr.append(line);
//                    }
//                    String jsonStr = sr.toString();
//                    Log.e("jsonstring", jsonStr);
//                    parseJSON(jsonStr);
//                }
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//
//        public void parseJSON(String json) {
//            try {
//                JSONObject jsonObject = new JSONObject(json);
//                Log.e("jsonObject", "" + jsonObject);
//                JSONArray jsonArray = jsonObject.getJSONArray("destination_addresses");
//                JSONArray originjsonArray = jsonObject.getJSONArray("origin_addresses");
//            } catch (Exception e) {
//            }
//        }
//    }
//
//
////    private void getDistanceInfo() {
////        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY
////        Map<String, String> mapQuery = new HashMap<>();
////        mapQuery.put("units", "metric");
////        mapQuery.put("origins", "17.738639,83.323809");
////        mapQuery.put("destinations", "17.928554,83.424811");
////        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);
////
////        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
////        call.enqueue(new Callback<DistanceResponse>() {
////            @Override
////            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
////                if (response.body() != null &&
////                        response.body().getRows() != null &&
////                        response.body().getRows().size() > 0 &&
////                        response.body().getRows().get(0) != null &&
////                        response.body().getRows().get(0).getElements() != null &&
////                        response.body().getRows().get(0).getElements().size() > 0 &&
////                        response.body().getRows().get(0).getElements().get(0) != null &&
////                        response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
////                        response.body().getRows().get(0).getElements().get(0).getDuration() != null) {
////
////                    Element element = response.body().getRows().get(0).getElements().get(0);
////                    //showTravelDistance(element.getDistance().getText() + "\n" + element.getDuration().getText());
////                    Toast.makeText(getActivity(), element.getDistance().getText() + "\n" + element.getDuration().getText(), Toast.LENGTH_LONG).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<DistanceResponse> call, Throwable t) {
////            }
////        });
////    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getDeliveryStoreOrderList();
//    }
//}
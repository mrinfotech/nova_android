package com.mrnovacrm.b2b_dispatch_dept;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.b2b_delivery_dept.DeliveryOrderProcessDetailsActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DeliveryDTO;
import com.mrnovacrm.model.DeliveryOrdersListDTO;
import com.mrnovacrm.model.DeliveryRecordsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.TransportDTO;
import com.mrnovacrm.model.TransportList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by android on 26-04-2018.
 */

public class DispatchDeliveryProcessingFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    private Dialog alertDialog;
    View layout;
    //  private LocationManager locationManager;
    //   private MyLocationListener myLocationListener;
    private TransparentProgressDialog dialog;
    private double source_LAT, source_LNG;
    public static final int RequestPermissionCode = 1;
    public static final int REQUEST_LOCATION = 10001;
    private static final int LocationRequestCode = 1;
    //TextView text_nodata;
    LinearLayout linear_header;
    //  Button btn_startprocessing;
    private TextView submittxt;
    private ImageView closeicon;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    private RecyclerView processrecyclerview;
    HashMap<String, String> processHashmap = new HashMap<>();
    HashMap<String, String> editprocessHashmap = new HashMap<>();
    Context mContext;
    private double locationLatitude1;
    private double locationLongitue1;
    private HashMap<String, String> values = new HashMap<String, String>();
    private String USERID = "";
    // private RelativeLayout btmrel;
    private String is_define = "";
    int STATUSCOUNT;
    int TOTALRECORDS;
    String routeValue = "start";
    // ArrayList<HashMap<String, String>> editroutehashmapList = new ArrayList<HashMap<String, String>>();
    private String DROUTEID = "";
    RelativeLayout imgrel;
    ImageView imageview;
    //   private String PRIMARYID;
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
    private String TRANSPORT_NAME = "";
    private DeliveryOrderDetailsAdapter adapter;

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
        //  btn_startprocessing = rootView.findViewById(R.id.btn_startprocessing);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linear_header = rootView.findViewById(R.id.linear_header);
        linear_header.setVisibility(View.GONE);
        //btmrel = rootView.findViewById(R.id.btm);
        dialog = new TransparentProgressDialog(mContext);
        values = SharedDB.getUserDetails(getActivity());
        USERID = values.get(SharedDB.PRIMARYID);
        BRANCHID = values.get(SharedDB.BRANCHID);
        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(getActivity());
        if(isConnectedToInternet) {
            getDeliveryStoreOrderList();
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        return rootView;
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

                        String status_count = mOrderObject.getStatus_count();
                        String total_records = mOrderObject.getTotal_records();
                        STATUSCOUNT = Integer.parseInt(status_count);
                        TOTALRECORDS = Integer.parseInt(total_records);
                        hashmapList = new ArrayList<HashMap<String, String>>();

                        List<DeliveryRecordsDTO> sellersList = mOrderObject.getDeliveryDTO();
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {
                                // btmrel.setVisibility(View.VISIBLE);
                                HashMap<String, ArrayList<HashMap<String, String>>> storeOrders = new HashMap<String, ArrayList<HashMap<String, String>>>();

                                HashMap<String, String[]> checkboxHashmap = new HashMap<>();
                                HashMap<String, String[]> idHashmap = new HashMap<>();
                                HashMap<String, String[]> transportIdHashmap = new HashMap<>();
                                HashMap<String, String[]> isDefineHashmap = new HashMap<>();
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
                                    String total_orders = sellersList.get(i).getTotal_orders();
                                    String route_define = sellersList.get(i).getRoute_define();

                                    ArrayList<HashMap<String, String>> ordersMapList = new ArrayList<HashMap<String, String>>();
                                    /*  Accessing Orderlist of the store */
                                    List<DeliveryOrdersListDTO> deliveryOrdersListDTO = sellersList.get(i).getDeliveryOrdersListDTO();

                                    if (deliveryOrdersListDTO != null) {
                                        if (deliveryOrdersListDTO.size() > 0) {
                                            String[] checkboxValueArray = new String[deliveryOrdersListDTO.size()];
                                            String[] idValueArray = new String[deliveryOrdersListDTO.size()];
                                            String[] transportidValueArray = new String[deliveryOrdersListDTO.size()];
                                            String[] isdefineValueArray = new String[deliveryOrdersListDTO.size()];

                                            for (int j = 0; j < deliveryOrdersListDTO.size(); j++) {
                                                HashMap<String, String> ordersMap = new HashMap<String, String>();
                                                ordersMap.put("id", deliveryOrdersListDTO.get(j).getId());
                                                ordersMap.put("order_id", deliveryOrdersListDTO.get(j).getOrder_id());
                                                ordersMap.put("order_value", deliveryOrdersListDTO.get(j).getOrder_value());
                                                ordersMap.put("transport_name", deliveryOrdersListDTO.get(j).getTransport_name());
                                                ordersMap.put("transport_id", deliveryOrdersListDTO.get(j).getTransport_id());
                                                ordersMap.put("is_define", deliveryOrdersListDTO.get(j).getIs_define());

                                                if(deliveryOrdersListDTO.get(j).getIs_define().equals("1"))
                                                {
                                                    checkboxValueArray[j] = "2";
                                                }else{
                                                    checkboxValueArray[j] = deliveryOrdersListDTO.get(j).getIs_define();
                                                }
                                                idValueArray[j] = deliveryOrdersListDTO.get(j).getId();
                                                isdefineValueArray[j]=deliveryOrdersListDTO.get(j).getIs_define();
                                                transportidValueArray[j]=deliveryOrdersListDTO.get(j).getTransport_id();
                                                ordersMapList.add(ordersMap);
                                            }
                                            checkboxHashmap.put(id, checkboxValueArray);
                                            idHashmap.put(id, idValueArray);
                                            isDefineHashmap.put(id,isdefineValueArray);
                                            transportIdHashmap.put(id,transportidValueArray);
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
                                    hashMap.put("total_orders", total_orders);
                                    hashMap.put("route_define", route_define);
                                    hashmapList.add(hashMap);
                                    itemStatusArray[i] = statusval;
                                }

                                showOrdersList(hashmapList, storeOrders, itemStatusArray, checkboxHashmap,
                                        idHashmap,isDefineHashmap,transportIdHashmap);
                                //   showOrdersList(hashmapList, storeOrders, itemStatusArray);
                            } else {
                                //   btmrel.setVisibility(View.GONE);
                            }
                        } else {
                            //  btmrel.setVisibility(View.GONE);
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
                //   Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList, HashMap<String,
            ArrayList<HashMap<String, String>>> storeOrders, String[] itemStatusArray,
                               HashMap<String, String[]> checkboxHashmap, HashMap<String, String[]> idHashmap,
                               HashMap<String, String[]> isDefineHashmap,HashMap<String, String[]> transportIdHashmap) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            adapter = new DeliveryOrderDetailsAdapter(getActivity(), hashmapList,
                    storeOrders, itemStatusArray, checkboxHashmap, idHashmap,isDefineHashmap,transportIdHashmap);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }


    @Override
    public void onClick(View v) {
    }

    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> hashmapList;
        HashMap<String, ArrayList<HashMap<String, String>>> storeOrders;
        String[] itemStatusArray;

        HashMap<String, String[]> checkboxHashmap;
        HashMap<String, String[]> idHashmap;
        HashMap<String, String[]> isDefineHashmap;
        HashMap<String, String[]> transportIdHashmap;

        int smoothscrollpositon = 0;

        HashMap<String, String[]> _checkboxHashmap = new HashMap<String, String[]>();
        HashMap<String, String[]> _idHashmap = new HashMap<String, String[]>();
        HashMap<String, String[]> _transportidHashmap = new HashMap<String, String[]>();

        public DeliveryOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList,
                                           HashMap<String, ArrayList<HashMap<String, String>>> storeOrders,
                                           String[] itemStatusArray,
                                           HashMap<String, String[]> checkboxHashmap,
                                           HashMap<String, String[]> idHashmap,
                                           HashMap<String, String[]> isDefineHashmap,
                                           HashMap<String, String[]> transportIdHashmap
        ) {
            this.mContext = mContext;
            this.hashmapList = mhashmapList;
            this.storeOrders = storeOrders;
            this.itemStatusArray = itemStatusArray;

            this.checkboxHashmap = checkboxHashmap;
            this.idHashmap = idHashmap;
            this.isDefineHashmap = isDefineHashmap;
            this.transportIdHashmap=transportIdHashmap;
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

            String total_orders = hashmapList.get(position).get("total_orders");
            String route_define = hashmapList.get(position).get("route_define");

            if(total_orders.equals(route_define))
            {
                holder.tranportbtn.setVisibility(View.GONE);
                holder.transportbtnlnr.setVisibility(View.GONE);
            }else{
                holder.tranportbtn.setVisibility(View.VISIBLE);
                holder.transportbtnlnr.setVisibility(View.VISIBLE);
            }
            holder.indexheaderpos = position;

            holder.store_name.setText(name);
            holder.store_contact.setText(mobile);
            holder.store_address.setText(address);
            //    holder.btnlnr.setVisibility(View.GONE);

            ArrayList<HashMap<String, String>> ordersList = storeOrders.get(id);
            if (ordersList != null) {
                if (ordersList.size() > 0) {
                    String[] checkboxValueArray = checkboxHashmap.get(id);
                    String[] idValueArray = idHashmap.get(id);
                    String[] isDefineValueArray = isDefineHashmap.get(id);
                    String[] transportIdValueArray=transportIdHashmap.get(id);

                    _checkboxHashmap.put(id, checkboxValueArray);
                    _idHashmap.put(id, idValueArray);
                    _transportidHashmap.put(id,transportIdValueArray);

                    holder.transportbtnlnr.setVisibility(View.VISIBLE);
                    holder.orderlinear.setVisibility(View.VISIBLE);
                    holder.ordersrecyclerview.setHasFixedSize(true);
                    holder.ordersrecyclerview.setVisibility(View.VISIBLE);
                    holder.ordersrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(mContext, id, ordersList, latitude, longitude, address,
                            checkboxValueArray, idValueArray,isDefineValueArray,total_orders,route_define,transportIdValueArray);
                    holder.ordersrecyclerview.setAdapter(adapter);
                }
            }

            holder.tranportbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dealerid = hashmapList.get(position).get("id");
                    String[] checkboxValueArray = _checkboxHashmap.get(dealerid);
                    String[] idValueArray = _idHashmap.get(dealerid);
                    String[] tranportidValueArray = _transportidHashmap.get(dealerid);
                    ArrayList<String> tranport_IdsList=new ArrayList<>();
                    StringBuilder sb1 = new StringBuilder();
                    for (int i = 0; i < checkboxValueArray.length; i++) {
                        String checkqty = checkboxValueArray[i];
                        String valqty = idValueArray[i];
                        if (checkqty.equals("1")) {
                            sb1.append(valqty);
                            sb1.append(",");
                            String transportid=tranportidValueArray[i];
                            if(!transportid.equals("0"))
                            {
                               // tranportIdsList.add(transportid);
                                if(tranport_IdsList!=null)
                                {
                                    if(tranport_IdsList.size()>0)
                                    {
                                        if(!tranport_IdsList.contains(transportid))
                                        {
                                            tranport_IdsList.add(transportid);
                                        }
                                    }else{
                                        tranport_IdsList.add(transportid);
                                    }
                                }else{
                                    tranport_IdsList.add(transportid);
                                }
                            }
                        }
                    }

                    if (sb1 == null || sb1.toString().equals("")) {
                        Toast.makeText(getActivity(), "Please select orders", Toast.LENGTH_SHORT).show();
                    }else{
                        String selectedOrders = removelastchar(sb1.toString());
                        String tranport_idval="0";

                        if(tranport_IdsList!=null)
                        {
                            if(tranport_IdsList.size()>0)
                            {
                                if(tranport_IdsList.size()==1)
                                {
                                    tranport_idval=tranport_IdsList.get(0);
                                }else{
                                    tranport_idval="0";
                                }
                            }else{
                                tranport_idval="0";
                            }
                        }else{
                            tranport_idval="0";
                        }
                         showTransportPopup(dealerid, selectedOrders,tranport_idval);
                    }
                }
            });

            holder.store_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String id = hashmapList.get(position).get("id");
//                    String name = hashmapList.get(position).get("name");
//                    Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
//                    intent.putExtra("dealerid",id);
//                    intent.putExtra("dealername",name);
//                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView order_num, store_name, store_contact,store_address;
            RecyclerView ordersrecyclerview;
            Button tranportbtn;
            LinearLayout orderlinear, transportbtnlnr;
            int indexheaderpos;

            MyViewHolder(View view) {
                super(view);
//                order_num = view.findViewById(R.id.order_num);
                store_name = view.findViewById(R.id.store_name);
                store_contact = view.findViewById(R.id.store_contact);
                ordersrecyclerview = view.findViewById(R.id.ordersrecyclerview);
                //  btnlnr = view.findViewById(R.id.btnlnr);
                orderlinear = view.findViewById(R.id.orderlinear);
                transportbtnlnr = view.findViewById(R.id.transportbtnlnr);

//                startbtn = view.findViewById(R.id.startbtn);
//                unloadbtn = view.findViewById(R.id.unloadbtn);
//                deliveredbtn = view.findViewById(R.id.deliveredbtn);
                tranportbtn = view.findViewById(R.id.tranportbtn);
                store_address = view.findViewById(R.id.store_address);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }

        public String removelastchar(String str) {
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }

        public void showTransportPopup(final String dealerid, final String selectedorders, final String tranport_idval) {

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

            lpnumber.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            vehiclenumber.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            drivernameedittxt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            drivermobileedittxt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            date.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            paid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            topay.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            phonenumberedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            fromrouteedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            torouteedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

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
                                Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                            } else {
                                submitRouteOrder(dealerid, selectedorders, transportAlertDialog, "single", LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                        TO_ROUTE, VEHICLE_NUMBER, DRIVER_M0BILE, DRIVER_NAME);
                            }
                        } else {
                            if (LRNUMBER == null || "".equalsIgnoreCase(LRNUMBER) || LRNUMBER.equals("")
                                    || ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
                                    || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
                                    || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
                                    || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
                                    || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
                                    || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")) {
                                Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                            } else {
                                if(isValidPhoneNumber(MOBILE_NUMBER))
                                {
                                    if (otherCircleAlertDialog != null) {
                                        otherCircleAlertDialog.dismiss();
                                    }
                                    transportAlertDialog.dismiss();
                                    submitRouteOrder(dealerid, selectedorders, transportAlertDialog, "single", LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
                                            TO_ROUTE, VEHICLE_NUMBER, DRIVER_M0BILE, DRIVER_NAME);
                                }else{
                                    Toast.makeText(getActivity(),"Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
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
            boolean isConnectedToInternet = CheckNetWork
                    .isConnectedToInternet(getActivity());
            if(isConnectedToInternet)
            {
                try{
                    getTrasportsList(tranport_idval);
                }catch(Exception e)
                {
                }
            }else{
                //  Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
//        private boolean isValidMobile(String phone) {
//            return android.util.Patterns.PHONE.matcher(phone).matches();
//        }

        private void processWithRetrofit(final String droute_id, final String storeid, String storestatus, final MyViewHolder holder,
                                         final String[] itemStatusArray, final int pos, final String pkey, final String storename,
                                         final String fromval) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

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
//                                holder.btnlnr.setVisibility(View.VISIBLE);
//                                holder.unloadbtn.setVisibility(View.VISIBLE);
//                                holder.startbtn.setVisibility(View.GONE);
//                                holder.deliveredbtn.setVisibility(View.GONE);
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

        public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
            private Context mContext;
            int mExpandedPosition = -1;
            ArrayList<HashMap<String, String>> hashmap_List;
            String latitude;
            String longitude;
            String address;
            String[] checkboxValueArray;
            String[] idValueArray;
            String[] isDefineValueArray;
            String dealerid;
            String total_orders,route_define;
            String[] transportIdValueArray;

            public OrderDetailsAdapter(Context mContext, String dealerid, ArrayList<HashMap<String, String>> hashmapList, String latitude,
                                       String longitude, String address, String[] checkboxValueArray, String[] idValueArray,String[] isDefineValueArray,
                                       String total_orders,String route_define,String[] transportIdValueArray) {
                this.mContext = mContext;
                this.dealerid = dealerid;
                this.hashmap_List = hashmapList;
                this.latitude = latitude;
                this.longitude = longitude;
                this.address = address;
                this.checkboxValueArray = checkboxValueArray;
                this.idValueArray = idValueArray;
                this.isDefineValueArray=isDefineValueArray;
                this.total_orders=total_orders;
                this.route_define=route_define;
                this.transportIdValueArray=transportIdValueArray;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_deliveryitemsorderslistadapter, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {

                holder.indexReference = position;

                _checkboxHashmap.put(dealerid, checkboxValueArray);
                _idHashmap.put(dealerid, idValueArray);
                _transportidHashmap.put(dealerid,transportIdValueArray);

                String id = hashmap_List.get(position).get("id");
                String order_id = hashmap_List.get(position).get("order_id");
                String order_value = hashmap_List.get(position).get("order_value");
                String is_define = hashmap_List.get(position).get("is_define");
                String transport_name = hashmap_List.get(position).get("transport_name");
                String transport_id = hashmap_List.get(position).get("transport_id");

                if(isDefineValueArray[holder.indexReference].equals("0"))
                {
                    //     holder.transportnametxt.setVisibility(View.GONE);
                    holder.item_checkbox.setVisibility(View.VISIBLE);
                    holder.item_checkbox.setClickable(true);
                    holder.item_checkbox.setChecked(false);
                }else{
                    //   holder.transportnametxt.setText(transport_name);
                    //     holder.transportnametxt.setVisibility(View.VISIBLE);
                    holder.item_checkbox.setChecked(true);
                    holder.item_checkbox.setVisibility(View.VISIBLE);
                    holder.item_checkbox.setClickable(false);
                }

                holder.transportnametxt.setText(transport_name);
                //  holder.orderid_txt.setText(order_id);
                holder.item_checkbox.setText(order_id);
                holder.orderval_txt.setText(order_value);

                if (checkboxValueArray[holder.indexReference].equals("1") || checkboxValueArray[holder.indexReference].equals("2")) {
                    holder.item_checkbox.setChecked(true);
                } else if (checkboxValueArray[holder.indexReference].equals("0")) {
                    holder.item_checkbox.setChecked(false);
                }
                smoothscrollpositon = holder.indexReference;

                holder.item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        smoothscrollpositon = holder.indexReference;
                        if (isChecked) {
                            checkboxValueArray[holder.indexReference] = "1";
//                            String qtyval = holder.qtyedittext.getText().toString().trim();
//                            if (qtyval != null || !qtyval.equals("")) {
//                                qtyHashmap.put(idValueArray[holder.indexReference], qtyval);
//                            }
                        } else {
                            //qtyHashmap.remove(idValueArray[holder.indexReference]);
                            checkboxValueArray[holder.indexReference] = "0";
                        }
                    }
                });


//                holder.orderlineartxt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String id = hashmap_List.get(position).get("id");
//                        String order_id = hashmap_List.get(position).get("order_id");
//
//                        if(total_orders.equals(route_define))
//                        {
//                            Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
//                            intent.putExtra("id",id);
//                            intent.putExtra("orderId",order_id);
//                            startActivity(intent);
//                        }else{
//                            Toast.makeText(getActivity(),"Please define transport",Toast.LENGTH_SHORT).show();
//                        }
//                   }
//                });

                holder.viewlinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isConnectedToInternet = CheckNetWork
                                .isConnectedToInternet(getActivity());
                        if(isConnectedToInternet)
                        {
                            try{
                                String id = hashmap_List.get(position).get("id");
                                String order_id = hashmap_List.get(position).get("order_id");

                                if(isDefineValueArray[holder.indexReference].equals("0"))
                                {
                                    Toast.makeText(getActivity(),"Please define transport",Toast.LENGTH_SHORT).show();
                                }else{
                                    Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("orderId",order_id);
                                    startActivity(intent);
                                }
//                                if(total_orders.equals(route_define))
//                                {
//                                    Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
//                                    intent.putExtra("id",id);
//                                    intent.putExtra("orderId",order_id);
//                                    startActivity(intent);
//                                }else{
//                                    Toast.makeText(getActivity(),"Please define transport",Toast.LENGTH_SHORT).show();
//                                }
                            }catch(Exception e)
                            {
                            }
                        }else{
                            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return hashmap_List.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView orderid_txt, orderval_txt, transportnametxt;
                CheckBox item_checkbox;
                int indexReference;
                LinearLayout orderlineartxt,viewlinear;
                MyViewHolder(View view) {
                    super(view);
                    orderid_txt = view.findViewById(R.id.orderid_txt);
                    orderval_txt = view.findViewById(R.id.orderval_txt);
                    item_checkbox = view.findViewById(R.id.checkbox);
                    transportnametxt = view.findViewById(R.id.transportnametxt);
                    orderlineartxt = view.findViewById(R.id.orderlineartxt);
                    viewlinear = view.findViewById(R.id.viewlinear);
                    view.setOnClickListener(this);
                }
                @Override
                public void onClick(View view) {

                }
            }
        }
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

    public void getTrasportsList(final String selecttranportid) {
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

                                int tranportselectpos=0;
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
                                    if(!selecttranportid.equals("0"))
                                    {
                                        if(id.equals(selecttranportid))
                                        {
                                            tranportselectpos=i+1;
                                        }
                                    }
                                }
                                transport_NamesList.add("Private");
                                transport_IdsList.add("0");
                                getTransportNames(hashMapArrayList, transport_NamesList, transport_IdsList,tranportselectpos);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TransportDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTransportNames(ArrayList<HashMap<String, String>> hashMapArrayList,
                                  final ArrayList<String> transport_NamesList, final ArrayList<String> transport_IdsList,
                                  final int tranportselectpos) {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, transport_NamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setSelection(tranportselectpos);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                    TRANSPORT_ID = transport_IdsList.get(position - 1);
                    TRANSPORT_NAME = transport_NamesList.get(position);
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


    public void submitRouteOrder(String dealerid, String orderitems, final Dialog transportAlertDialog, final String select,
                                 final String LRNUMBER, final String ESTDATE, final String PAID_VAL, final String TO_PAY,
                                 final String MOBILE_NUMBER, final String FROM_ROUTE,
                                 final String TO_ROUTE, final String VEHICLE_NUMBER, final String DRIVER_M0BILE, final String DRIVER_NAME) {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        String transporttype;
        if (TRANSPORT_ID.equals("0")) {
            transporttype = "private";
        } else {
            transporttype = "reg";
        }

        Call<Login> mService = mApiService.submitDisptachTransport(USERID, dealerid, orderitems, VEHICLE_NUMBER, DRIVER_M0BILE,
                DRIVER_NAME, LRNUMBER, TRANSPORT_ID, BRANCHID, transporttype,TO_PAY, PAID_VAL,FROM_ROUTE, TO_ROUTE, ESTDATE, MOBILE_NUMBER);
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
                        if (transportAlertDialog != null) {
                            transportAlertDialog.dismiss();
                        }
                        recyclerView.setAdapter(null);
                        adapter.notifyDataSetChanged();
                        getDeliveryStoreOrderList();
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

    @Override
    public void onResume() {
        super.onResume();
    }
    /**
     * Used for PhoneNumber Pattern and Length
     */
    public boolean isValidPhoneNumber(String phonenumber) {
        String pattern = "[6-9]{1}[0-9]{9}";
        if (phonenumber.matches(pattern)) {
            return true;
        }
        return false;
    }
}



























































































//package com.mrnovacrm.b2b_dispatch_dept;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.InputFilter;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.mrnovacrm.R;
//import com.mrnovacrm.adapter.SpinnerItemsAdapter;
//import com.mrnovacrm.b2b_delivery_dept.DeliveryOrderProcessDetailsActivity;
//import com.mrnovacrm.constants.CheckNetWork;
//import com.mrnovacrm.constants.RetrofitAPI;
//import com.mrnovacrm.constants.TransparentProgressDialog;
//import com.mrnovacrm.db.SharedDB;
//import com.mrnovacrm.model.DeliveryDTO;
//import com.mrnovacrm.model.DeliveryOrdersListDTO;
//import com.mrnovacrm.model.DeliveryRecordsDTO;
//import com.mrnovacrm.model.Login;
//import com.mrnovacrm.model.TransportDTO;
//import com.mrnovacrm.model.TransportList;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by android on 26-04-2018.
// */
//
//public class DispatchDeliveryProcessingFragment extends Fragment implements View.OnClickListener {
//
//    RecyclerView recyclerView;
//    private Dialog alertDialog;
//    View layout;
//    //  private LocationManager locationManager;
//    //   private MyLocationListener myLocationListener;
//    private TransparentProgressDialog dialog;
//    private double source_LAT, source_LNG;
//    public static final int RequestPermissionCode = 1;
//    public static final int REQUEST_LOCATION = 10001;
//    private static final int LocationRequestCode = 1;
//    //TextView text_nodata;
//    LinearLayout linear_header;
//    //  Button btn_startprocessing;
//    private TextView submittxt;
//    private ImageView closeicon;
//    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//    private RecyclerView processrecyclerview;
//    HashMap<String, String> processHashmap = new HashMap<>();
//    HashMap<String, String> editprocessHashmap = new HashMap<>();
//    Context mContext;
//    private double locationLatitude1;
//    private double locationLongitue1;
//    private HashMap<String, String> values = new HashMap<String, String>();
//    private String USERID = "";
//    // private RelativeLayout btmrel;
//    private String is_define = "";
//
//    int STATUSCOUNT;
//    int TOTALRECORDS;
//    String routeValue = "start";
//
//   // ArrayList<HashMap<String, String>> editroutehashmapList = new ArrayList<HashMap<String, String>>();
//    private String DROUTEID = "";
//    RelativeLayout imgrel;
//    ImageView imageview;
//    //   private String PRIMARYID;
//    private String BRANCHID;
//
//    Button transportbtn;
//    EditText tranportname;
//    private Dialog transportAlertDialog;
//    Spinner transport_spinner;
//    static EditText date;
//    EditText lpnumber, vehiclenumber, drivernameedittxt, paid, topay,
//            phonenumberedittext, torouteedittext, fromrouteedittext, drivermobileedittxt;
//    Button save_btn;
//    ArrayList<String> transportNamesList = new ArrayList<>();
//    ArrayList<String> transportIDsList = new ArrayList<>();
//    String TRANSPORT_ID = "";
//    LinearLayout drivernamelinear, drivermobilelinear, vehiclnumberlinear, lpnumberlinear;
//    private Dialog otherCircleAlertDialog;
//    private String TRANSPORT_NAME = "";
//    private DeliveryOrderDetailsAdapter adapter;
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
//        //  text_nodata = rootView.findViewById(R.id.text_nodata);
//        imgrel = rootView.findViewById(R.id.imgrel);
//        imageview = rootView.findViewById(R.id.imageview);
//
//
//        //  btn_startprocessing = rootView.findViewById(R.id.btn_startprocessing);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setVisibility(View.VISIBLE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        linear_header = rootView.findViewById(R.id.linear_header);
//        linear_header.setVisibility(View.GONE);
//
//        //btmrel = rootView.findViewById(R.id.btm);
//
//        dialog = new TransparentProgressDialog(mContext);
//
//        values = SharedDB.getUserDetails(getActivity());
//        USERID = values.get(SharedDB.PRIMARYID);
//        BRANCHID = values.get(SharedDB.BRANCHID);
//
//        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(getActivity());
//        if(isConnectedToInternet) {
//            getDeliveryStoreOrderList();
//        }else{
//            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//        }
//        return rootView;
//    }
//
//
//    private void getDeliveryStoreOrderList() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        Call<DeliveryDTO> mService = mApiService.getBranchesList(USERID, BRANCHID);
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
//
//                        String status_count = mOrderObject.getStatus_count();
//                        String total_records = mOrderObject.getTotal_records();
//                        STATUSCOUNT = Integer.parseInt(status_count);
//                        TOTALRECORDS = Integer.parseInt(total_records);
//                        hashmapList = new ArrayList<HashMap<String, String>>();
//
//                        List<DeliveryRecordsDTO> sellersList = mOrderObject.getDeliveryDTO();
//                        if (sellersList != null) {
//                            if (sellersList.size() > 0) {
//                                // btmrel.setVisibility(View.VISIBLE);
//                                HashMap<String, ArrayList<HashMap<String, String>>> storeOrders = new HashMap<String, ArrayList<HashMap<String, String>>>();
//
//                                HashMap<String, String[]> checkboxHashmap = new HashMap<>();
//                                HashMap<String, String[]> idHashmap = new HashMap<>();
//                                HashMap<String, String[]> isDefineHashmap = new HashMap<>();
//                                String[] itemStatusArray = new String[sellersList.size()];
//
//                                for (int i = 0; i < sellersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    HashMap<String, String> editroutehashmap = new HashMap<>();
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
//                                    String total_orders = sellersList.get(i).getTotal_orders();
//                                    String route_define = sellersList.get(i).getRoute_define();
//
//                                    ArrayList<HashMap<String, String>> ordersMapList = new ArrayList<HashMap<String, String>>();
//
//                                    /*  Accessing Orderlist of the store */
//                                    List<DeliveryOrdersListDTO> deliveryOrdersListDTO = sellersList.get(i).getDeliveryOrdersListDTO();
//
//                                    if (deliveryOrdersListDTO != null) {
//                                        if (deliveryOrdersListDTO.size() > 0) {
//                                            String[] checkboxValueArray = new String[deliveryOrdersListDTO.size()];
//                                            String[] idValueArray = new String[deliveryOrdersListDTO.size()];
//                                            String[] isdefineValueArray = new String[deliveryOrdersListDTO.size()];
//
//                                            for (int j = 0; j < deliveryOrdersListDTO.size(); j++) {
//                                                HashMap<String, String> ordersMap = new HashMap<String, String>();
//                                                ordersMap.put("id", deliveryOrdersListDTO.get(j).getId());
//                                                ordersMap.put("order_id", deliveryOrdersListDTO.get(j).getOrder_id());
//                                                ordersMap.put("order_value", deliveryOrdersListDTO.get(j).getOrder_value());
//                                                ordersMap.put("transport_name", deliveryOrdersListDTO.get(j).getTransport_name());
//                                                ordersMap.put("is_define", deliveryOrdersListDTO.get(j).getIs_define());
//
//                                                if(deliveryOrdersListDTO.get(j).getIs_define().equals("1"))
//                                                {
//                                                    checkboxValueArray[j] = "2";
//                                                }else{
//                                                    checkboxValueArray[j] = deliveryOrdersListDTO.get(j).getIs_define();
//                                                }
//
//                                                idValueArray[j] = deliveryOrdersListDTO.get(j).getId();
//                                                isdefineValueArray[j]=deliveryOrdersListDTO.get(j).getIs_define();
//
//                                                ordersMapList.add(ordersMap);
//                                            }
//                                            checkboxHashmap.put(id, checkboxValueArray);
//                                            idHashmap.put(id, idValueArray);
//                                            isDefineHashmap.put(id,isdefineValueArray);
//                                        }
//                                    }
//
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
//                                    hashMap.put("total_orders", total_orders);
//                                    hashMap.put("route_define", route_define);
//                                    hashmapList.add(hashMap);
//                                    itemStatusArray[i] = statusval;
//                                }
//
//                                showOrdersList(hashmapList, storeOrders, itemStatusArray, checkboxHashmap,
//                                        idHashmap,isDefineHashmap);
//                               //   showOrdersList(hashmapList, storeOrders, itemStatusArray);
//                            } else {
//                                //   btmrel.setVisibility(View.GONE);
//                            }
//                        } else {
//                            //  btmrel.setVisibility(View.GONE);
//                        }
//                    } else {
//                        String message = mOrderObject.getMessage();
//                        recyclerView.setVisibility(View.GONE);
//                        // text_nodata.setVisibility(View.VISIBLE);
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
//             //   Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList, HashMap<String,
//            ArrayList<HashMap<String, String>>> storeOrders, String[] itemStatusArray,
//                               HashMap<String, String[]> checkboxHashmap, HashMap<String, String[]> idHashmap,
//                               HashMap<String, String[]> isDefineHashmap) {
//        if (hashmapList.size() > 0) {
//            recyclerView.setVisibility(View.VISIBLE);
//            imgrel.setVisibility(View.GONE);
//            adapter = new DeliveryOrderDetailsAdapter(getActivity(), hashmapList,
//                    storeOrders, itemStatusArray, checkboxHashmap, idHashmap,isDefineHashmap);
//            recyclerView.setAdapter(adapter);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//            imgrel.setVisibility(View.VISIBLE);
//            imageview.setImageResource(R.drawable.noordersfound);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_LOCATION) {
//            if (resultCode == Activity.RESULT_OK) {
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//            }
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//    }
//
//    public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> hashmapList;
//        HashMap<String, ArrayList<HashMap<String, String>>> storeOrders;
//        String[] itemStatusArray;
//
//        HashMap<String, String[]> checkboxHashmap;
//        HashMap<String, String[]> idHashmap;
//        HashMap<String, String[]> isDefineHashmap;
//
//        int smoothscrollpositon = 0;
//
//        HashMap<String, String[]> _checkboxHashmap = new HashMap<String, String[]>();
//        HashMap<String, String[]> _idHashmap = new HashMap<String, String[]>();
//
//        public DeliveryOrderDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList,
//                                           HashMap<String, ArrayList<HashMap<String, String>>> storeOrders,
//                                           String[] itemStatusArray,
//                                           HashMap<String, String[]> checkboxHashmap,
//                                           HashMap<String, String[]> idHashmap,
//                                           HashMap<String, String[]> isDefineHashmap
//        ) {
//            this.mContext = mContext;
//            this.hashmapList = mhashmapList;
//            this.storeOrders = storeOrders;
//            this.itemStatusArray = itemStatusArray;
//
//            this.checkboxHashmap = checkboxHashmap;
//            this.idHashmap = idHashmap;
//            this.isDefineHashmap = isDefineHashmap;
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
//            String total_orders = hashmapList.get(position).get("total_orders");
//            String route_define = hashmapList.get(position).get("route_define");
//
//            if(total_orders.equals(route_define))
//            {
//                holder.tranportbtn.setVisibility(View.GONE);
//                holder.transportbtnlnr.setVisibility(View.GONE);
//            }else{
//                holder.tranportbtn.setVisibility(View.VISIBLE);
//                holder.transportbtnlnr.setVisibility(View.VISIBLE);
//            }
//            holder.indexheaderpos = position;
//
//            holder.store_name.setText(name);
//            holder.store_contact.setText(mobile);
//            holder.store_address.setText(address);
//            //    holder.btnlnr.setVisibility(View.GONE);
//
//            ArrayList<HashMap<String, String>> ordersList = storeOrders.get(id);
//            if (ordersList != null) {
//                if (ordersList.size() > 0) {
//                    String[] checkboxValueArray = checkboxHashmap.get(id);
//                    String[] idValueArray = idHashmap.get(id);
//                    String[] isDefineValueArray = isDefineHashmap.get(id);
//
//                    _checkboxHashmap.put(id, checkboxValueArray);
//                    _idHashmap.put(id, idValueArray);
//
//                    holder.transportbtnlnr.setVisibility(View.VISIBLE);
//                    holder.orderlinear.setVisibility(View.VISIBLE);
//                    holder.ordersrecyclerview.setHasFixedSize(true);
//                    holder.ordersrecyclerview.setVisibility(View.VISIBLE);
//                    holder.ordersrecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(mContext, id, ordersList, latitude, longitude, address,
//                            checkboxValueArray, idValueArray,isDefineValueArray,total_orders,route_define);
//                    holder.ordersrecyclerview.setAdapter(adapter);
//                }
//            }
//
//            holder.tranportbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String dealerid = hashmapList.get(position).get("id");
//                    String[] checkboxValueArray = _checkboxHashmap.get(dealerid);
//                    String[] idValueArray = _idHashmap.get(dealerid);
//
//                    StringBuilder sb1 = new StringBuilder();
//                    for (int i = 0; i < checkboxValueArray.length; i++) {
//                        String checkqty = checkboxValueArray[i];
//                        String valqty = idValueArray[i];
//                        if (checkqty.equals("1")) {
//                            sb1.append(valqty);
//                            sb1.append(",");
//                        }
//                    }
//                    if (sb1 == null || sb1.toString().equals("")) {
//                        Toast.makeText(getActivity(), "Please select orders", Toast.LENGTH_SHORT).show();
//                    } else {
//                        String selectedOrders = removelastchar(sb1.toString());
//                        showTransportPopup(dealerid, selectedOrders);
//                        //   Log.e("selectedOrders",selectedOrders);
//                    }
//                }
//            });
//
//            holder.store_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    String id = hashmapList.get(position).get("id");
////                    String name = hashmapList.get(position).get("name");
////                    Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
////                    intent.putExtra("dealerid",id);
////                    intent.putExtra("dealername",name);
////                    startActivity(intent);
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
//            TextView order_num, store_name, store_contact,store_address;
//            RecyclerView ordersrecyclerview;
//            Button tranportbtn;
//            LinearLayout orderlinear, transportbtnlnr;
//            int indexheaderpos;
//
//            MyViewHolder(View view) {
//                super(view);
////                order_num = view.findViewById(R.id.order_num);
//                store_name = view.findViewById(R.id.store_name);
//                store_contact = view.findViewById(R.id.store_contact);
//                ordersrecyclerview = view.findViewById(R.id.ordersrecyclerview);
//                //  btnlnr = view.findViewById(R.id.btnlnr);
//                orderlinear = view.findViewById(R.id.orderlinear);
//                transportbtnlnr = view.findViewById(R.id.transportbtnlnr);
//
////                startbtn = view.findViewById(R.id.startbtn);
////                unloadbtn = view.findViewById(R.id.unloadbtn);
////                deliveredbtn = view.findViewById(R.id.deliveredbtn);
//                tranportbtn = view.findViewById(R.id.tranportbtn);
//                store_address = view.findViewById(R.id.store_address);
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//            }
//        }
//
//        public String removelastchar(String str) {
//            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
//                str = str.substring(0, str.length() - 1);
//            }
//            return str;
//        }
//
//        public void showTransportPopup(final String dealerid, final String selectedorders) {
//            transportAlertDialog = new Dialog(getActivity());
//            transportAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            transportAlertDialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent);
//            LayoutInflater inflater = getLayoutInflater();
//            View transportlayout = inflater.inflate(R.layout.layout_managetransport, null);
//            transportAlertDialog.setContentView(transportlayout);
//            transportAlertDialog.setCancelable(true);
//            if (!transportAlertDialog.isShowing()) {
//                transportAlertDialog.show();
//            }
//
//            transport_spinner = transportlayout.findViewById(R.id.transport_spinner);
//            lpnumber = transportlayout.findViewById(R.id.lpnumber);
//            vehiclenumber = transportlayout.findViewById(R.id.vehiclenumber);
//            drivernameedittxt = transportlayout.findViewById(R.id.drivernameedittxt);
//            drivermobileedittxt = transportlayout.findViewById(R.id.drivermobileedittxt);
//            date = transportlayout.findViewById(R.id.date);
//            paid = transportlayout.findViewById(R.id.paid);
//            topay = transportlayout.findViewById(R.id.topay);
//            phonenumberedittext = transportlayout.findViewById(R.id.phonenumberedittext);
//            save_btn = transportlayout.findViewById(R.id.save_btn);
//            ImageView closeimg = transportlayout.findViewById(R.id.closeimg);
//
//            drivernamelinear = transportlayout.findViewById(R.id.drivernamelinear);
//            vehiclnumberlinear = transportlayout.findViewById(R.id.vehiclnumberlinear);
//            lpnumberlinear = transportlayout.findViewById(R.id.lpnumberlinear);
//            drivermobilelinear = transportlayout.findViewById(R.id.drivermobilelinear);
//            fromrouteedittext = transportlayout.findViewById(R.id.fromrouteedittext);
//            torouteedittext = transportlayout.findViewById(R.id.torouteedittext);
//
//            lpnumber.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            vehiclenumber.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            drivernameedittxt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            drivermobileedittxt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            date.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            paid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            topay.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            phonenumberedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            fromrouteedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//            torouteedittext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//
//            date.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("DateType", "fromDate");
//                        DialogFragment fromfragment = new DatePickerFragment();
//                        fromfragment.setArguments(bundle);
//                        fromfragment.show(getFragmentManager(), "Date Picker");
//                    }
//                    return true;
//                }
//            });
//
//            save_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String LRNUMBER = lpnumber.getText().toString().trim();
//                    String ESTDATE = date.getText().toString().trim();
//                    String PAID_VAL = paid.getText().toString().trim();
//                    String TO_PAY = topay.getText().toString().trim();
//                    String MOBILE_NUMBER = phonenumberedittext.getText().toString().trim();
//                    String VEHICLE_NUMBER = vehiclenumber.getText().toString().trim();
//                    String DRIVER_NAME = drivernameedittxt.getText().toString().trim();
//                    String DRIVER_M0BILE = drivermobileedittxt.getText().toString().trim();
//                    String FROM_ROUTE = fromrouteedittext.getText().toString().trim();
//                    String TO_ROUTE = torouteedittext.getText().toString().trim();
//
//                    if (!TRANSPORT_ID.equals("")) {
//                        if (TRANSPORT_ID.equals("0")) {
//                            if (ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
//                                    || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
//                                    || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
//                                    || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
//                                    || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
//                                    || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")
//                                    || VEHICLE_NUMBER == null || "".equalsIgnoreCase(VEHICLE_NUMBER) || VEHICLE_NUMBER.equals("")
//                                    || DRIVER_NAME == null || "".equalsIgnoreCase(DRIVER_NAME) || DRIVER_NAME.equals("")
//                                    || DRIVER_M0BILE == null || "".equalsIgnoreCase(DRIVER_M0BILE) || DRIVER_M0BILE.equals("")) {
//                                Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                            } else {
//                                submitRouteOrder(dealerid, selectedorders, transportAlertDialog, "single", LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
//                                        TO_ROUTE, VEHICLE_NUMBER, DRIVER_M0BILE, DRIVER_NAME);
//                            }
//                        } else {
//                            if (LRNUMBER == null || "".equalsIgnoreCase(LRNUMBER) || LRNUMBER.equals("")
//                                    || ESTDATE == null || "".equalsIgnoreCase(ESTDATE) || ESTDATE.equals("")
//                                    || PAID_VAL == null || "".equalsIgnoreCase(PAID_VAL) || PAID_VAL.equals("")
//                                    || TO_PAY == null || "".equalsIgnoreCase(TO_PAY) || TO_PAY.equals("")
//                                    || MOBILE_NUMBER == null || "".equalsIgnoreCase(MOBILE_NUMBER) || MOBILE_NUMBER.equals("")
//                                    || FROM_ROUTE == null || "".equalsIgnoreCase(FROM_ROUTE) || FROM_ROUTE.equals("")
//                                    || TO_ROUTE == null || "".equalsIgnoreCase(TO_ROUTE) || TO_ROUTE.equals("")) {
//                                Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if(isValidPhoneNumber(MOBILE_NUMBER))
//                                {
//                                    if (otherCircleAlertDialog != null) {
//                                        otherCircleAlertDialog.dismiss();
//                                    }
//                                    transportAlertDialog.dismiss();
//                                    submitRouteOrder(dealerid, selectedorders, transportAlertDialog, "single", LRNUMBER, ESTDATE, PAID_VAL, TO_PAY, MOBILE_NUMBER, FROM_ROUTE,
//                                            TO_ROUTE, VEHICLE_NUMBER, DRIVER_M0BILE, DRIVER_NAME);
//                                }else{
//                                   Toast.makeText(getActivity(),"Please enter valid mobile number", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    } else {
//                        tranportname.setText("");
//                        Toast.makeText(getActivity(), "Plase select transport", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            closeimg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    transportAlertDialog.dismiss();
//                }
//            });
//            hintTransport();
//            boolean isConnectedToInternet = CheckNetWork
//                    .isConnectedToInternet(getActivity());
//            if(isConnectedToInternet)
//            {
//                try{
//                    getTrasportsList();
//                }catch(Exception e)
//                {
//                }
//            }else{
//              //  Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//            }
//        }
////        private boolean isValidMobile(String phone) {
////            return android.util.Patterns.PHONE.matcher(phone).matches();
////        }
//
//        private void processWithRetrofit(final String droute_id, final String storeid, String storestatus, final MyViewHolder holder,
//                                         final String[] itemStatusArray, final int pos, final String pkey, final String storename,
//                                         final String fromval) {
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//            Call<Login> mService = mApiService.setDeliveryRouteProcess(droute_id, String.valueOf(locationLatitude1),
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
////                                holder.btnlnr.setVisibility(View.VISIBLE);
////                                holder.unloadbtn.setVisibility(View.VISIBLE);
////                                holder.startbtn.setVisibility(View.GONE);
////                                holder.deliveredbtn.setVisibility(View.GONE);
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
//        public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
//            private Context mContext;
//            int mExpandedPosition = -1;
//            ArrayList<HashMap<String, String>> hashmap_List;
//            String latitude;
//            String longitude;
//            String address;
//            String[] checkboxValueArray;
//            String[] idValueArray;
//            String[] isDefineValueArray;
//            String dealerid;
//            String total_orders,route_define;
//
//            public OrderDetailsAdapter(Context mContext, String dealerid, ArrayList<HashMap<String, String>> hashmapList, String latitude,
//                                       String longitude, String address
//                    , String[] checkboxValueArray, String[] idValueArray,String[] isDefineValueArray,
//                                       String total_orders,String route_define
//            ) {
//                this.mContext = mContext;
//                this.dealerid = dealerid;
//                this.hashmap_List = hashmapList;
//                this.latitude = latitude;
//                this.longitude = longitude;
//                this.address = address;
//                this.checkboxValueArray = checkboxValueArray;
//                this.idValueArray = idValueArray;
//                this.isDefineValueArray=isDefineValueArray;
//                this.total_orders=total_orders;
//                this.route_define=route_define;
//            }
//
//            @Override
//            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.layout_deliveryitemsorderslistadapter, parent, false);
//                return new MyViewHolder(itemView);
//            }
//
//            @Override
//            public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//                holder.indexReference = position;
//
//                _checkboxHashmap.put(dealerid, checkboxValueArray);
//                _idHashmap.put(dealerid, idValueArray);
//
//                String id = hashmap_List.get(position).get("id");
//                String order_id = hashmap_List.get(position).get("order_id");
//                String order_value = hashmap_List.get(position).get("order_value");
//                String is_define = hashmap_List.get(position).get("is_define");
//                String transport_name = hashmap_List.get(position).get("transport_name");
//
//                if(isDefineValueArray[holder.indexReference].equals("0"))
//                {
//               //     holder.transportnametxt.setVisibility(View.GONE);
//                    holder.item_checkbox.setVisibility(View.VISIBLE);
//                    holder.item_checkbox.setClickable(true);
//                    holder.item_checkbox.setChecked(false);
//                }else{
//                 //   holder.transportnametxt.setText(transport_name);
//               //     holder.transportnametxt.setVisibility(View.VISIBLE);
//                    holder.item_checkbox.setChecked(true);
//                    holder.item_checkbox.setVisibility(View.VISIBLE);
//                    holder.item_checkbox.setClickable(false);
//                }
//
//                holder.transportnametxt.setText(transport_name);
//              //  holder.orderid_txt.setText(order_id);
//                holder.item_checkbox.setText(order_id);
//
//                holder.orderval_txt.setText(order_value);
//
//                if (checkboxValueArray[holder.indexReference].equals("1") || checkboxValueArray[holder.indexReference].equals("2")) {
//                    holder.item_checkbox.setChecked(true);
//                } else if (checkboxValueArray[holder.indexReference].equals("0")) {
//                    holder.item_checkbox.setChecked(false);
//                }
//                smoothscrollpositon = holder.indexReference;
//
//                holder.item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        smoothscrollpositon = holder.indexReference;
//                        if (isChecked) {
//                            checkboxValueArray[holder.indexReference] = "1";
////                            String qtyval = holder.qtyedittext.getText().toString().trim();
////                            if (qtyval != null || !qtyval.equals("")) {
////                                qtyHashmap.put(idValueArray[holder.indexReference], qtyval);
////                            }
//                        } else {
//                            //qtyHashmap.remove(idValueArray[holder.indexReference]);
//                            checkboxValueArray[holder.indexReference] = "0";
//                        }
//                    }
//                });
//
//
////                holder.orderlineartxt.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        String id = hashmap_List.get(position).get("id");
////                        String order_id = hashmap_List.get(position).get("order_id");
////
////                        if(total_orders.equals(route_define))
////                        {
////                            Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
////                            intent.putExtra("id",id);
////                            intent.putExtra("orderId",order_id);
////                            startActivity(intent);
////                        }else{
////                            Toast.makeText(getActivity(),"Please define transport",Toast.LENGTH_SHORT).show();
////                        }
////                   }
////                });
//
//                holder.viewlinear.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean isConnectedToInternet = CheckNetWork
//                                .isConnectedToInternet(getActivity());
//                        if(isConnectedToInternet)
//                        {
//                            try{
//                                String id = hashmap_List.get(position).get("id");
//                                String order_id = hashmap_List.get(position).get("order_id");
//                                if(total_orders.equals(route_define))
//                                {
//                                    Intent intent=new Intent(getActivity(),DispatchDeliveryProcessDetailsActivity.class);
//                                    intent.putExtra("id",id);
//                                    intent.putExtra("orderId",order_id);
//                                    startActivity(intent);
//                                }else{
//                                    Toast.makeText(getActivity(),"Please define transport",Toast.LENGTH_SHORT).show();
//                                }
//                            }catch(Exception e)
//                            {
//                            }
//                        }else{
//                            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public int getItemCount() {
//                return hashmap_List.size();
//            }
//
//            class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//                TextView orderid_txt, orderval_txt, transportnametxt;
//                CheckBox item_checkbox;
//                int indexReference;
//                LinearLayout orderlineartxt,viewlinear;
//                MyViewHolder(View view) {
//                    super(view);
//                    orderid_txt = view.findViewById(R.id.orderid_txt);
//                    orderval_txt = view.findViewById(R.id.orderval_txt);
//                    item_checkbox = view.findViewById(R.id.checkbox);
//                    transportnametxt = view.findViewById(R.id.transportnametxt);
//                    orderlineartxt = view.findViewById(R.id.orderlineartxt);
//                    viewlinear = view.findViewById(R.id.viewlinear);
//                    view.setOnClickListener(this);
//                }
//                @Override
//                public void onClick(View view) {
//
//                }
//            }
//        }
//    }
//
//
//    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            //Use the current date as the default date in the date picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//            String type;
//            if (getArguments() != null) {
//                type = getArguments().getString("DateType");
//                if (type.equals("fromDate")) {
//                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
//                }
//            }
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        private DatePickerDialog.OnDateSetListener from_dateListener =
//                new DatePickerDialog.OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        int month = monthOfYear + 1;
//                        date.setText(dayOfMonth + "-" + month + "-" + year);
//                    }
//                };
//
//        @Override
//        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//        }
//    }
//
//    public void hintTransport() {
//        transportNamesList.clear();
//        transportNamesList.add("Select Transport");
//        transportNamesList.add("Private");
//        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
//                R.layout.layout_spinneritems, transportNamesList);
//        spinnerClass
//                .setDropDownViewResource(R.layout.layout_spinneritems);
//        transport_spinner.setAdapter(spinnerClass);
//        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                // TODO Auto-generated method stub
//                if (position == 0) {
//                    TRANSPORT_ID = "";
//                } else {
//                    TRANSPORT_ID = "0";
//                    drivernamelinear.setVisibility(View.VISIBLE);
//                    drivermobilelinear.setVisibility(View.VISIBLE);
//                    vehiclnumberlinear.setVisibility(View.VISIBLE);
//                    lpnumberlinear.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//    public void getTrasportsList() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<TransportDTO> mService = mApiService.getTransportsList(BRANCHID);
//        mService.enqueue(new Callback<TransportDTO>() {
//            @Override
//            public void onResponse(Call<TransportDTO> call, Response<TransportDTO> response) {
//                Log.e("response", "" + response);
//                TransportDTO mtransportObject = response.body();
//                dialog.dismiss();
//                try {
//                    String status = mtransportObject.getStatus();
//                    Log.e("status", "" + status);
//                    if (status.equals("1")) {
//
//                        List<TransportList> transportDTOLIST = mtransportObject.getTransportDTOS();
//                        if (transportDTOLIST != null) {
//                            if (transportDTOLIST.size() > 0) {
//                                ArrayList<String> transport_NamesList = new ArrayList<>();
//                                ArrayList<String> transport_IdsList = new ArrayList<>();
//
//                                transport_NamesList.add("Select Transport");
//                                ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
//                                for (int i = 0; i < transportDTOLIST.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<>();
//                                    String id = transportDTOLIST.get(i).getId();
//                                    String name = transportDTOLIST.get(i).getName();
//                                    hashMap.put("id", id);
//                                    hashMap.put("name", name);
//
//                                    transport_NamesList.add(name);
//                                    transport_IdsList.add(id);
//                                    hashMapArrayList.add(hashMap);
//                                }
//                                transport_NamesList.add("Private");
//                                transport_IdsList.add("0");
//                                getTransportNames(hashMapArrayList, transport_NamesList, transport_IdsList);
//                            }
//                        }
//                    } else {
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TransportDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//              //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getTransportNames(ArrayList<HashMap<String, String>> hashMapArrayList,
//                                  final ArrayList<String> transport_NamesList, final ArrayList<String> transport_IdsList) {
//        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
//                R.layout.layout_spinneritems, transport_NamesList);
//        spinnerClass
//                .setDropDownViewResource(R.layout.layout_spinneritems);
//        transport_spinner.setAdapter(spinnerClass);
//        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                // TODO Auto-generated method stub
//                if (position == 0) {
//                    TRANSPORT_ID = "";
//                } else {
//                    TRANSPORT_ID = transport_IdsList.get(position - 1);
//                    TRANSPORT_NAME = transport_NamesList.get(position);
//                    Log.e("TRANSPORT_ID", TRANSPORT_ID);
//
//                    if (TRANSPORT_ID.equals("0")) {
//                        drivernamelinear.setVisibility(View.VISIBLE);
//                        drivermobilelinear.setVisibility(View.VISIBLE);
//                        vehiclnumberlinear.setVisibility(View.VISIBLE);
//                        lpnumberlinear.setVisibility(View.GONE);
//                    } else {
//                        drivernamelinear.setVisibility(View.GONE);
//                        drivermobilelinear.setVisibility(View.GONE);
//                        vehiclnumberlinear.setVisibility(View.GONE);
//                        lpnumberlinear.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//
//    public void submitRouteOrder(String dealerid, String orderitems, final Dialog transportAlertDialog, final String select,
//                                 final String LRNUMBER, final String ESTDATE, final String PAID_VAL, final String TO_PAY,
//                                 final String MOBILE_NUMBER, final String FROM_ROUTE,
//                                 final String TO_ROUTE, final String VEHICLE_NUMBER, final String DRIVER_M0BILE, final String DRIVER_NAME) {
//
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//        String transporttype;
//        if (TRANSPORT_ID.equals("0")) {
//            transporttype = "private";
//        } else {
//            transporttype = "reg";
//        }
//        Log.e("PRIMARYID",USERID);
//        Log.e("dealerid",dealerid);
//        Log.e("orderitems",orderitems);
//        Log.e("VEHICLE_NUMBER",VEHICLE_NUMBER);
//        Log.e("DRIVER_M0BILE",DRIVER_M0BILE);
//        Log.e("DRIVER_NAME",DRIVER_NAME);
//        Log.e("LRNUMBER",LRNUMBER);
//        Log.e("TRANSPORT_ID",TRANSPORT_ID);
//        Log.e("BRANCHID",BRANCHID);
//        Log.e("transporttype",transporttype);
//        Log.e("PAID_VAL",PAID_VAL);
//        Log.e("TO_PAY",TO_PAY);
//        Log.e("FROM_ROUTE",FROM_ROUTE);
//        Log.e("TO_ROUTE",TO_ROUTE);
//        Log.e("ESTDATE",ESTDATE);
//        Log.e("MOBILE_NUMBER",MOBILE_NUMBER);
//
//        Call<Login> mService = mApiService.submitDisptachTransport(USERID, dealerid, orderitems, VEHICLE_NUMBER, DRIVER_M0BILE,
//                DRIVER_NAME, LRNUMBER, TRANSPORT_ID, BRANCHID, transporttype,TO_PAY, PAID_VAL,FROM_ROUTE, TO_ROUTE, ESTDATE, MOBILE_NUMBER);
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
//                        if (transportAlertDialog != null) {
//                            transportAlertDialog.dismiss();
//                        }
//                        recyclerView.setAdapter(null);
//                        adapter.notifyDataSetChanged();
//                        getDeliveryStoreOrderList();
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
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//
//    /**
//     * Used for PhoneNumber Pattern and Length
//     */
//    public boolean isValidPhoneNumber(String phonenumber) {
//        String pattern = "[6-9]{1}[0-9]{9}";
//        if (phonenumber.matches(pattern)) {
//            return true;
//        }
//        return false;
//    }
//}

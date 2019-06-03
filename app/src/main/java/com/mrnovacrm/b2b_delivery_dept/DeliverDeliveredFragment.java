package com.mrnovacrm.b2b_delivery_dept;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_dispatch_dept.DOrderDetailsActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DailyOrders;
import com.mrnovacrm.model.DailyOrdersList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/20/2018.
 */

public class DeliverDeliveredFragment extends Fragment implements View.OnTouchListener {

    private RecyclerView recyclerview;
    LinearLayout header;
 //   TextView text_nodata;
    LinearLayout linearlnr;
    public static EditText edtxt_fromdate, edtxt_todate;
    private SimpleDateFormat dfDate;
    private String PRIMARYID,BRANCHID;
    RelativeLayout imgrel;
    ImageView imageview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sellerplacedorders, container, false);

        recyclerview = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);
        header = rootView.findViewById(R.id.header);

//        text_nodata = rootView.findViewById(R.id.text_nodata);
        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        linearlnr=rootView.findViewById(R.id.linearlnr);
        linearlnr.setVisibility(View.VISIBLE);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        ImageView search = rootView.findViewById(R.id.search);

        edtxt_fromdate.setOnTouchListener(DeliverDeliveredFragment.this);
        edtxt_todate.setOnTouchListener(DeliverDeliveredFragment.this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getPlacedList();
                String FROMDATE=edtxt_fromdate.getText().toString().trim();
                String TODATE=edtxt_todate.getText().toString().trim();

              //  dfDate = new SimpleDateFormat("yyyy-MM-dd");

                 dfDate = new SimpleDateFormat("dd-MM-yyyy");
                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
                {
                    Toast.makeText(getActivity(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            getOrdersList(FROMDATE,TODATE);
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                            getOrdersList(FROMDATE,TODATE);
                        } else {
                            Toast.makeText(getActivity(),
                                    "From Date Should be above To Date", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });


        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(getActivity());
        if(isConnectedToInternet) {
            getOrdersList("", "");
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void getOrdersList(String fromdate, String todate) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DailyOrders> mService = mApiService.deliveryBoyDeliverHistory(fromdate, todate, "1",PRIMARYID,BRANCHID);
        mService.enqueue(new Callback<DailyOrders>() {
            @Override
            public void onResponse(@NonNull Call<DailyOrders> call, @NonNull Response<DailyOrders> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    DailyOrders mOrderObject = response.body();
                    assert mOrderObject != null;
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        List<DailyOrdersList> ordersList = mOrderObject.getDailyOrdersList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String order_id = ordersList.get(i).getOrder_id();
                                    String orderedon = ordersList.get(i).getOrderedon();

                                    hashMap.put("id", id);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("orderedon", orderedon);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
                            } else {
                                recyclerview.setVisibility(View.GONE);
                                header.setVisibility(View.GONE);
                              //  text_nodata.setVisibility(View.VISIBLE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        recyclerview.setVisibility(View.GONE);
                        header.setVisibility(View.GONE);
                       // text_nodata.setVisibility(View.VISIBLE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DailyOrders> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
           //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            SellerPlaceOrdersRecyclerAdapter adapter = new SellerPlaceOrdersRecyclerAdapter(getActivity(), hashmapList);
            recyclerview.setAdapter(adapter);
        } else {
            recyclerview.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            //text_nodata.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            tofragment.show(getFragmentManager(), "Date Picker");
        }
        return true;
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

            //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);

                } else if (type.equals("toDate")) {
                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_fromdate.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_todate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    }

    public class SellerPlaceOrdersRecyclerAdapter extends RecyclerView.Adapter<SellerPlaceOrdersRecyclerAdapter.MyViewHolder> {
        ArrayList<HashMap<String, String>> hashmapList;
        Context context;

        public SellerPlaceOrdersRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> hashmapList) {
            this.context = context;
            this.hashmapList = hashmapList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ordersadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String orderId = hashmapList.get(position).get("order_id");
            String orderedon = hashmapList.get(position).get("orderedon");
            holder.orderid.setText(orderId);
            holder.orderdate.setText(orderedon);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderid, orderdate;

            public MyViewHolder(View itemView) {
                super(itemView);
                orderid = itemView.findViewById(R.id.orderid);
                orderdate = itemView.findViewById(R.id.orderdate);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                String id = hashmapList.get(getAdapterPosition()).get("id");
//                String orderId = hashmapList.get(getAdapterPosition()).get("order_id");
//                Intent intent = new Intent(getActivity(), DeliverDeleveredDetailsActivity.class);
//                intent.putExtra("id",id);
//                intent.putExtra("fromval", "packed");
//                intent.putExtra("status", "1");
//                startActivity(intent);


                String id = hashmapList.get(getAdapterPosition()).get("id");
                String orderId = hashmapList.get(getAdapterPosition()).get("order_id");

                Intent intent = new Intent(getActivity(), DOrderDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("orderId", orderId);
                intent.putExtra("status", "Delivered");
                intent.putExtra("SCREENFROM","Delivered");
                startActivity(intent);
            }
        }
    }
}
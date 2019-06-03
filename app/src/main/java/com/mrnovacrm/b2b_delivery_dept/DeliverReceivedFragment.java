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
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DeliverStoresListDTO;
import com.mrnovacrm.model.SellerDTO;

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
 * Created by android on 20-03-2018.
 */

public class DeliverReceivedFragment extends Fragment implements View.OnTouchListener {
    private RecyclerView recyclerview;
    //TextView text_nodata;
    RelativeLayout headerrel;
    public static EditText edtxt_fromdate, edtxt_todate;
    private SimpleDateFormat dfDate;
    private String PRIMARYID;
    RelativeLayout imgrel;
    ImageView imageview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_orders_list, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(mLayoutManager);
       // text_nodata = rootView.findViewById(R.id.text_nodata);

        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        headerrel=rootView.findViewById(R.id.headerrel);

        LinearLayout linearlnr=rootView.findViewById(R.id.linearlnr);
        linearlnr.setVisibility(View.VISIBLE);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        ImageView search = rootView.findViewById(R.id.search);

        edtxt_fromdate.setOnTouchListener(DeliverReceivedFragment.this);
        edtxt_todate.setOnTouchListener(DeliverReceivedFragment.this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getPlacedList();
                String FROMDATE=edtxt_fromdate.getText().toString().trim();
                String TODATE=edtxt_todate.getText().toString().trim();

                dfDate = new SimpleDateFormat("yyyy-MM-dd");
                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
                {
                    Toast.makeText(getActivity(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            getDeliveryStoreOrderList(FROMDATE,TODATE);
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                            getDeliveryStoreOrderList(FROMDATE,TODATE);
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

        getDeliveryStoreOrderList("","");
        return rootView;
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

    private void getDeliveryStoreOrderList(String FROMDATE,String TODATE) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<SellerDTO> mService = mApiService.getDeliveryPickupHistory(FROMDATE,TODATE,"1",PRIMARYID);
        mService.enqueue(new Callback<SellerDTO>() {
            @Override
            public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    SellerDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<DeliverStoresListDTO> sellersList = mOrderObject.getStoresListDTO();
                        Log.e("sellersList", "" + sellersList.size());
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {
                                headerrel.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.VISIBLE);
                                imgrel.setVisibility(View.GONE);

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < sellersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = sellersList.get(i).getId();
                                    String order_id = sellersList.get(i).getOrder_id();
                                    Log.e("OrderId"," :" +order_id);
                                    String name = sellersList.get(i).getName();
                                    String mobile = sellersList.get(i).getMobile();
                                    String latitude = sellersList.get(i).getLatitude();
                                    String longitude = sellersList.get(i).getLongitude();
                                    String address = sellersList.get(i).getAddress();
                                    hashMap.put("id", id);
                                    hashMap.put("order_id", order_id);
                                    hashMap.put("name", name);
                                    hashMap.put("mobile", mobile);
                                    hashmapList.add(hashMap);
                                }
                                showOrdersList(hashmapList);
                            } else {
                                headerrel.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                        headerrel.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.GONE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SellerDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersList(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            DeliveryOrderListAdapter adapter = new DeliveryOrderListAdapter(getActivity(), hashmapList);
            recyclerview.setAdapter(adapter);
        } else {
            recyclerview.setVisibility(View.GONE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
        }
    }

    public class DeliveryOrderListAdapter extends RecyclerView.Adapter<DeliveryOrderListAdapter.MyViewHolder> {
        Context context;
        ArrayList<HashMap<String, String>> hashmapList;

        public DeliveryOrderListAdapter(Context mContext, ArrayList<HashMap<String, String>> mhashmapList) {
            this.context = context;
            hashmapList = mhashmapList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_deliveredorders_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String order_id = hashmapList.get(position).get("order_id");
            holder.orderid.setText(order_id);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderid;

            public MyViewHolder(View itemView) {
                super(itemView);
                orderid = itemView.findViewById(R.id.orderid);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                String id = hashmapList.get(getAdapterPosition()).get("id");
                String order_id = hashmapList.get(getAdapterPosition()).get("order_id");
                String latitude = hashmapList.get(getAdapterPosition()).get("latitude");
                String longitude = hashmapList.get(getAdapterPosition()).get("longitude");
                String address = hashmapList.get(getAdapterPosition()).get("address");
                Intent intent = new Intent(getActivity(), DeliverReceivedListDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("status","1");

                startActivity(intent);
            }
        }
    }
}

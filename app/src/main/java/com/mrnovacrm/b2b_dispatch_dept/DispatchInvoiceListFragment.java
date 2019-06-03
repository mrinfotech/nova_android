package com.mrnovacrm.b2b_dispatch_dept;

//public class DispatchInvoiceListFragment extends Fragment{
//}

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.b2b_dealer.InvoiceDetailsActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;
import java.io.File;
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
 * Created by prasad on 2/27/2018.
 */
public class DispatchInvoiceListFragment extends Fragment implements View.OnTouchListener {
    private RecyclerView recyclerView;
    public static EditText edtxt_fromdate, edtxt_todate;
    private HashMap<String, String> values;

    ImageView viewreport_btn;
    RelativeLayout linear_header;
    RelativeLayout bottom;
    TextView text_nodata;
    RelativeLayout imgrel;
    ImageView imageview;
    private String PRIMARYID = "";
    private String SHORTFORM,BRANCHID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_orders, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        // edtxt_fromdate.setOnClickListener(OrderListFragment.this);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);
        //edtxt_todate.setOnClickListener(OrderListFragment.this);

        edtxt_fromdate.setOnTouchListener(DispatchInvoiceListFragment.this);
        edtxt_todate.setOnTouchListener(DispatchInvoiceListFragment.this);
        linear_header = rootView.findViewById(R.id.linear_header);
        bottom = rootView.findViewById(R.id.bottom);

        // text_nodata = rootView.findViewById(R.id.text_nodata);
        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        viewreport_btn = rootView.findViewById(R.id.search);
        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }
        viewreport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FROMDATE=edtxt_fromdate.getText().toString().trim();
                String TODATE=edtxt_todate.getText().toString().trim();

               // SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
                {
                    Toast.makeText(getActivity(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        boolean isConnectedToInternet = CheckNetWork
                                .isConnectedToInternet(getActivity());
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            if(isConnectedToInternet)
                            {
                                getOrdersList();
                            }else {
                                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                            }
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                            if(isConnectedToInternet)
                            {
                                getOrdersList();
                            }else {
                                Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                            }
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

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            getOrdersList();
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        deleteInvoices();

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
    @Override
    public void onResume() {
        super.onResume();
    }

    private void getOrdersList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        String from_date = edtxt_fromdate.getText().toString();
        String to_date = edtxt_todate.getText().toString();
       // Call<Order> mService = mApiService.getDeliveryBoyInvoiceList(PRIMARYID, from_date, to_date);
        Call<Order> mService = mApiService.getDeliveryBoyMasterInvoiceList(PRIMARYID, from_date, to_date,SHORTFORM,
                BRANCHID);
        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                Order mOrderObject = response.body();
                try {
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);

                    if (Integer.parseInt(status) == 1) {
                        List<OrdersListDTO> ordersList = mOrderObject.getOrdersListDTOS();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String orderId = ordersList.get(i).getOrder_id();
                                    String orderedOn = ordersList.get(i).getOrderedon();
                                    String status1 = ordersList.get(i).getStatus();
                                    String dealer_name = ordersList.get(i).getDealer_name();
                                    String dealer_contact = ordersList.get(i).getDealer_contact();
                                    String dealer_code = ordersList.get(i).getDealer_code();
                                    String takenby_name = ordersList.get(i).getTakenby_name();
                                    String takenby_branch = ordersList.get(i).getTakenby_branch();
                                    String takenby_contact = ordersList.get(i).getTakenby_contact();
                                    String payment_type = ordersList.get(i).getPayment_type();
                                    String credit_date = ordersList.get(i).getCredit_date();
                                    String dealer_id = ordersList.get(i).getDealer_id();
                                    String count = ordersList.get(i).getCount();


                                    hashMap.put("id", id);
                                    hashMap.put("orderId", orderId);
                                    hashMap.put("orderedOn", orderedOn);
                                    hashMap.put("status", status1);
                                    hashMap.put("dealer_name", dealer_name);
                                    hashMap.put("dealer_contact", dealer_contact);
                                    hashMap.put("dealer_code", dealer_code);
                                    hashMap.put("takenby_name", takenby_name);
                                    hashMap.put("takenby_branch", takenby_branch);
                                    hashMap.put("takenby_contact", takenby_contact);
                                    hashMap.put("payment_type", payment_type);
                                    hashMap.put("credit_date", credit_date);
                                    hashMap.put("dealer_id", dealer_id);
                                    hashMap.put("count", count);
                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                linear_header.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                                //text_nodata.setVisibility(View.VISIBLE);

                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        linear_header.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                        // text_nodata.setVisibility(View.VISIBLE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noinvoicefound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            linear_header.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), hashmapList);
            recyclerView.setAdapter(adapter);
        } else {
            linear_header.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            // text_nodata.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noinvoicefound);
        }
        bottom.setVisibility(View.GONE);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;

        ArrayList<HashMap<String, String>> hashmapList;
        public RecyclerViewAdapter(Context mContext,ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            this.hashmapList=hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.invoicereport_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String orderId = hashmapList.get(position).get("orderId");
            holder.orderidtxt.setText(orderId);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderidtxt;
            MyViewHolder(View view) {
                super(view);
                orderidtxt=view.findViewById(R.id.orderidtxt);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet)
                {
                    try{
                      //  String count = hashmapList.get(pos).get("count");
//                        if(count.equals("0"))
//                        {
                            String id = hashmapList.get(pos).get("id");
                            String orderId = hashmapList.get(pos).get("orderId");

                            Intent intent = new Intent(getActivity(), InvoiceDetailsActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("invoice_id",orderId);
                            startActivity(intent);
                     //   }
                    }catch(Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void deleteInvoices()
    {
        try{
            File dir = new File(Environment.getExternalStorageDirectory()+"/.Nova");
            if (dir.exists()) {
                if (dir.isDirectory())
                {
                    String[] children = dir.list();

                    if(children!=null)
                    {
                        if(children.length>0)
                        {
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                    }
                }
            }
        }catch (Exception e)
        {
        }
    }
}

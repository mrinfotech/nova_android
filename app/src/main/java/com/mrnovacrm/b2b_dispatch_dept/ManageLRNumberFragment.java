package com.mrnovacrm.b2b_dispatch_dept;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.LRDetailsDTO;
import com.mrnovacrm.model.Order;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageLRNumberFragment extends Fragment implements View.OnTouchListener{

    private RecyclerView recyclerView;
    public static EditText edtxt_fromdate, edtxt_todate;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    ImageView viewreport_btn;
    RelativeLayout linear_header;
    RelativeLayout bottom;
    TextView text_nodata;
    RelativeLayout imgrel;
    ImageView imageview;
    private String SHORTFORM,BRANCHID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_managelrnumber, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(ManageLRNumberFragment.this);
        edtxt_todate.setOnTouchListener(ManageLRNumberFragment.this);


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
        Call<Order> mService = mApiService.getEmpLRDetailsList(PRIMARYID, from_date, to_date,SHORTFORM,
                BRANCHID);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                Order mOrderObject = response.body();
                String status = mOrderObject.getStatus();
                Log.e("ordersstatus", "" + status);
                try {
                    if (Integer.parseInt(status) == 1) {
                        List<LRDetailsDTO> ordersList = mOrderObject.getLrDetailsDTOList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {

                                showOrdersData(ordersList);
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

    public void showOrdersData(List<LRDetailsDTO> ordersList) {
        if (ordersList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            linear_header.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.GONE);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), ordersList);
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

        List<LRDetailsDTO> ordersList;
        public RecyclerViewAdapter(Context mContext,List<LRDetailsDTO> ordersList) {
            this.mContext = mContext;
            this.ordersList=ordersList;
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_lrnumberadapter, parent, false);
            return new RecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder,final int position) {

//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
//            String orderId = hashmapList.get(position).get("orderId");
           // holder.invoiceidtext.setText(orderId);

            holder.invoiceid_txt.setText(ordersList.get(position).getOrder_id());
            holder.dealer_text.setText(ordersList.get(position).getCompany_name());
            holder.fromroute_text.setText(ordersList.get(position).getFrom_route());
            holder.toroute_text.setText(ordersList.get(position).getTo_route());
            holder.vehiclenumber_text.setText(ordersList.get(position).getVechicle_number());

            holder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), UpdateLRNumberDetails.class);
                    intent.putExtra("id",ordersList.get(position).getId());
                    intent.putExtra("oid",ordersList.get(position).getOid());
                    intent.putExtra("orderId",ordersList.get(position).getOrder_id());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView invoiceidtext,invoiceid_txt,dealer_text,fromroute_text,toroute_text,vehiclenumber_text;
            Button viewbtn;
            MyViewHolder(View view) {
                super(view);
                invoiceidtext=view.findViewById(R.id.invoiceidtext);
                invoiceid_txt=view.findViewById(R.id.invoiceid_txt);
                dealer_text=view.findViewById(R.id.dealer_text);
                fromroute_text=view.findViewById(R.id.fromroute_text);
                toroute_text=view.findViewById(R.id.toroute_text);
                vehiclenumber_text=view.findViewById(R.id.vehiclenumber_text);
                viewbtn=view.findViewById(R.id.viewbtn);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();

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

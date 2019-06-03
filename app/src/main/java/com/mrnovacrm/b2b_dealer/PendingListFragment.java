package com.mrnovacrm.b2b_dealer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_finance_dept.EmployeeFullDeatailsActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.OrdersListDTO;

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
public class PendingListFragment extends Fragment implements View.OnTouchListener {
    private RecyclerView recyclerView;
    public static EditText edtxt_fromdate, edtxt_todate;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    ImageView viewreport_btn;
    RelativeLayout linear_header;
    RelativeLayout bottom;
   // TextView text_nodata;
    RelativeLayout imgrel;
    ImageView imageview;
    private String SHORTFORM;
    private String BRANCHID;

    public PendingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_orders, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        // edtxt_fromdate.setOnClickListener(OrderListFragment.this);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);
        //edtxt_todate.setOnClickListener(OrderListFragment.this);

        edtxt_fromdate.setOnTouchListener(PendingListFragment.this);
        edtxt_todate.setOnTouchListener(PendingListFragment.this);
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

                //SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
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
                            if(isConnectedToInternet) {
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
        }else {
          //  Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
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
        Call<Order> mService = mApiService.getOrdersList(PRIMARYID, from_date, to_date,"Pending",SHORTFORM,BRANCHID);

        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
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




                                    Log.e("orderId", "" + orderId);
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
                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                linear_header.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                               // text_nodata.setVisibility(View.VISIBLE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.noordersfound);
                            }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        linear_header.setVisibility(View.GONE);
                        bottom.setVisibility(View.GONE);
                      //  text_nodata.setVisibility(View.VISIBLE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.noordersfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
              //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
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
          //  text_nodata.setVisibility(View.VISIBLE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.noordersfound);
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
        ArrayList<HashMap<String, String>> ordersList = new ArrayList<HashMap<String, String>>();

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            ordersList = hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_orderslistcardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            String id = ordersList.get(position).get("id");
            String orderId = ordersList.get(position).get("orderId");
            String orderedOn = ordersList.get(position).get("orderedOn");

            String dealer_name = ordersList.get(position).get("dealer_name");
            String dealer_contact = ordersList.get(position).get("dealer_contact");
            String dealer_code = ordersList.get(position).get("dealer_code");
            String takenby_name = ordersList.get(position).get("takenby_name");
            String takenby_branch = ordersList.get(position).get("takenby_branch");
            String takenby_contact = ordersList.get(position).get("takenby_contact");
            String credit_date = ordersList.get(position).get("credit_date");
            String payment_type = ordersList.get(position).get("payment_type");

            holder.orderid.setText(orderId);
            holder.orderid.setTextColor(getResources().getColor(R.color.bluecolor));
            holder.orderedOn.setText(orderedOn);

            holder.creditdate.setText(credit_date);
            holder.paymenttype.setText(payment_type);
            holder.ordertakenby.setText(takenby_name);
            holder.dealer.setText(dealer_name);
            holder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = ordersList.get(position).get("id");
                    String orderId = ordersList.get(position).get("orderId");
                    String status = ordersList.get(position).get("status");

                    Log.e("ID***", " :" + id);
                    Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("status", status);
                    intent.putExtra("SCREENFROM","Pending");
                    startActivity(intent);
                }
            });

            if(SHORTFORM.equals("DEALER"))
            {
                holder.dealaerlinear.setVisibility(View.GONE);
            }else{
                holder.dealaerlinear.setVisibility(View.VISIBLE);
            }

            holder.dealer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dealerid=ordersList.get(position).get("dealer_id");
                    if(!dealerid.equals(""))
                    {
                        String dealer_name = ordersList.get(position).get("dealer_name");
                        Intent intent=new Intent(getActivity(), EmployeeFullDeatailsActivity.class);
                        intent.putExtra("FROMVAL","DEALER");
                        intent.putExtra("DEALERID",dealerid);
                        intent.putExtra("DEALERNAME",dealer_name);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ordersList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView orderid, orderedOn,creditdate,paymenttype,ordertakenby,dealer;;
            Button viewbtn;
            LinearLayout dealaerlinear;
            MyViewHolder(View view) {
                super(view);
                orderid = view.findViewById(R.id.orderid);
                orderedOn = view.findViewById(R.id.dateval);

                creditdate = view.findViewById(R.id.creditdate);
                paymenttype = view.findViewById(R.id.paymenttype);
                ordertakenby = view.findViewById(R.id.ordertakenby);
                dealer = view.findViewById(R.id.dealer);
                viewbtn = view.findViewById(R.id.viewbtn);
                dealaerlinear = view.findViewById(R.id.dealaerlinear);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                String id = ordersList.get(getAdapterPosition()).get("id");
//                String orderId = ordersList.get(getAdapterPosition()).get("orderId");
//                String status = ordersList.get(getAdapterPosition()).get("status");
//
//                Log.e("ID***", " :" + id);
//                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("orderId", orderId);
//                intent.putExtra("status", status);
//                startActivity(intent);
            }
        }
    }
}
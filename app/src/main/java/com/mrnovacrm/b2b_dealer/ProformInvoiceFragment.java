package com.mrnovacrm.b2b_dealer;

//import android.support.v4.app.Fragment;
//
//public class ProformInvoiceFragment extends Fragment {
//
//
//}

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mrnovacrm.db.SharedDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Satyanarayana on 3/19/2018.
 */

public class ProformInvoiceFragment extends Fragment implements View.OnTouchListener {
    RecyclerView recyclerView;
    public static EditText edtxt_fromdate, edtxt_todate;
    private HashMap<String, String> values;
    private String PRIMARYID="";
    private SimpleDateFormat dfDate;
    //TextView text_nodata;
    RelativeLayout imgrel;
    ImageView imageview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoicereport, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //  text_nodata=rootView.findViewById(R.id.text_nodata);
        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(ProformInvoiceFragment.this);
        edtxt_todate.setOnTouchListener(ProformInvoiceFragment.this);

        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        ImageView searchimg=rootView.findViewById(R.id.searchimg);
        searchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FROMDATE=edtxt_fromdate.getText().toString().trim();
                String TODATE=edtxt_todate.getText().toString().trim();
                dfDate = new SimpleDateFormat("yyyy-MM-dd");
                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
                {
                    Toast.makeText(getActivity(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            getInvoiceOrdersList();
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                            getInvoiceOrdersList();
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
        RelativeLayout bottom=rootView.findViewById(R.id.bottom);
        bottom.setVisibility(View.GONE);

        getInvoiceOrdersList();

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
                        int month = monthOfYear+1;                        edtxt_fromdate.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear+1;                        edtxt_todate.setText(dayOfMonth + "-" + month + "-" + year);
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
            String invoice_id=hashmapList.get(position).get("invoice_id");
            String order_date=hashmapList.get(position).get("order_date");

            holder.orderidtxt.setText(invoice_id);
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
                String id = hashmapList.get(pos).get("id");
                String invoice_id=hashmapList.get(pos).get("invoice_id");

//                Intent intent = new Intent(getActivity(), InvoiceDetailsActivity.class);
//                intent.putExtra("id",id);
//                intent.putExtra("invoice_id", invoice_id);
//                startActivity(intent);
            }
        }
    }

    private void getInvoiceOrdersList() {

//        String fromDate=edtxt_fromdate.getText().toString();
//        String toDate=edtxt_todate.getText().toString();
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<SellersDTO> mService = mApiService.getPickerListDateBased(PRIMARYID,"1",fromDate,toDate);
//
//        mService.enqueue(new Callback<SellersDTO>() {
//            @Override
//            public void onResponse(Call<SellersDTO> call, Response<SellersDTO> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//
//                SellersDTO mOrderObject = response.body();
//                String status = mOrderObject.getStatus();
//                Log.e("ordersstatus", "" + status);
//                try {
//                    if (Integer.parseInt(status) == 1) {
//                        List<SellersRecordsDTOList> pickersList = mOrderObject.getSellersRecordsDTOLists();
//                        if (pickersList != null) {
//                            if (pickersList.size() > 0) {
//                                recyclerView.setVisibility(View.VISIBLE);
//                                imgrel.setVisibility(View.GONE);
//
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                for (int i = 0; i < pickersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//
//                                    String invoice_id = pickersList.get(i).getInvoice_id();
//                                    String order_date= pickersList.get(i).getOrder_date();
//                                    String is_processed = pickersList.get(i).getIs_processed();
//                                    String id = pickersList.get(i).getId();
//
//                                    hashMap.put("invoice_id", invoice_id);
//                                    hashMap.put("order_date", order_date);
//                                    hashMap.put("is_processed", is_processed);
//                                    hashMap.put("id", id);
//                                    hashmapList.add(hashMap);
//                                }
//                                showSellersList(hashmapList);
//                            } else {
//                            }
//                        }
//                    } else {
//                        String message=mOrderObject.getMessage();
////                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//                        recyclerView.setVisibility(View.GONE);
//                        imgrel.setVisibility(View.VISIBLE);
//                        imageview.setImageResource(R.drawable.noinvoicefound);
//                    }
//                } catch (Exception e) {
//                }
//            }
//            @Override
//            public void onFailure(Call<SellersDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    public void showSellersList(ArrayList<HashMap<String, String>> hashmapList)
    {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),hashmapList);
        recyclerView.setAdapter(adapter);
    }
}


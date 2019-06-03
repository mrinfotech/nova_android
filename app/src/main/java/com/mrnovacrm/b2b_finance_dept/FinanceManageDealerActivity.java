package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SwitchRecyclerAdapter;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;
import com.mrnovacrm.model.StoresDTO;
import com.mrnovacrm.model.SwitchItemDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/20/2018.
 */
public class FinanceManageDealerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txt_nodata;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    String SHORTFROM = "";
    String SHORTFORMVAL = "";
    String PRIMARYID = "";

    String TITLE = "";
    Context context;
    RecyclerViewAdapter adapter;
    public static Activity mainfinish;
    GlobalShare globalShare;
    private Dialog otpalertDialog;
    private View otplayout;
    private EditText otpmobilenumber;
    private Button popup_submit;
    private String mobileOTP = "";
    private String MOBILEOTPVAL = "";


    private String BRANCHID;
    private String id;
    private String sellerid;
    private String count;
    private String SHORTFORM;

    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private ImageView closeicon;
    EditText reason_edtxt;
    String reason;

    private List<SwitchItemDTO> mDataList = null;
    private SwitchRecyclerAdapter mAdapter;

    HashMap<String,String> statusHashMap=new HashMap<String,String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalShare = (GlobalShare) getApplicationContext();
        setTheme(R.style.AppTheme);
        context = this;
        mainfinish = this;

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            SHORTFORMVAL = values.get(SharedDB.SHORTFORM);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        setContentView(R.layout.layout_managestores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Manage Dealers");
//
        recyclerView = findViewById(R.id.managestoresrecyclerview);
        txt_nodata = findViewById(R.id.txt_nodata);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        getEmployeeList("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);

        if (searchItem != null) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //some operation
                    return true;
                }
            });

            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                    filteremployeehashmapList = new ArrayList<HashMap<String, String>>();

                    if (hashmapList != null) {
                        if (hashmapList.size() > 0) {
                            String[] companystatus_array = new String[hashmapList.size()];

                            String[] dealername_array = new String[hashmapList.size()];
                            String[] dealerid_array = new String[hashmapList.size()];

                            for (int i = 0; i < hashmapList.size(); i++) {
                                String company_name = hashmapList.get(i).get("company_name");
//                                String company_name = hashmapList.get(i).get("company_name");
                                if ((company_name.toLowerCase()).contains(newText.toLowerCase())) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = hashmapList.get(i).get("id");
                                    String name = hashmapList.get(i).get("name");
                                    String empid = hashmapList.get(i).get("empid");
                                    String status = hashmapList.get(i).get("status");
                                    String companyname = hashmapList.get(i).get("company_name");

                                     String statusval=statusHashMap.get(id);

//                                    if(status.equals("1"))
//                                    {
                                        hashMap.put("id", id);
                                        hashMap.put("name", name);
                                        hashMap.put("empid", empid);
                                        hashMap.put("status", statusval);
                                        hashMap.put("company_name", companyname);
                                        companystatus_array[i] = statusval;

                                        filteremployeehashmapList.add(hashMap);
                                        showOrdersData(filteremployeehashmapList,companystatus_array);
 //                                   }
                                }
                            }
                        }
                    }
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getEmployeeList(String fromval) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService = null;

        mService = mApiService.getDealerNamesList(BRANCHID);
        mService.enqueue(new Callback<EmployeeDTO>() {
            @Override
            public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    EmployeeDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);

                                hashmapList=new ArrayList<HashMap<String, String>>();
                                statusHashMap=new HashMap<String,String>();

                                String[] companystatus_array = new String[ordersList.size()];
                                String[] dealername_array = new String[ordersList.size()];
                                String[] dealerid_array = new String[ordersList.size()];


                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String name = ordersList.get(i).getName();
                                    String empid = ordersList.get(i).getEmp_id();
                                    String company_name = ordersList.get(i).getCompany_name();
                                    String status1 = ordersList.get(i).getStatus();

//                                    if(status1.equals("1"))
//                                    {
                                        companystatus_array[i] = status1;
                                        dealername_array[i] = company_name;
                                        dealerid_array[i] = id;

                                        hashMap.put("id", id);
                                        hashMap.put("name", name);
                                        hashMap.put("empid", empid);
                                        hashMap.put("status", status1);
                                        hashMap.put("company_name", company_name);

                                        statusHashMap.put(id,status1);

                                        hashmapList.add(hashMap);
//                                    }else{
//                                    }
                                }
                                showOrdersData(hashmapList,companystatus_array);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                txt_nodata.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList,String[] companystatus_array) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
            adapter = new RecyclerViewAdapter(getApplicationContext(), hashmapList,companystatus_array);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            txt_nodata.setVisibility(View.VISIBLE);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();
        String[] companystauts_array;

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList,String[] companystauts_array) {
            this.mContext = mContext;
            employeesList = hashmapList;
            this.companystauts_array=companystauts_array;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_managestoresadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String id = employeesList.get(position).get("id");
            String name = employeesList.get(position).get("name");
            String empid = employeesList.get(position).get("empid");
            String status = employeesList.get(position).get("status");
            String company_name = employeesList.get(position).get("company_name");

            holder.indexReference = position;
            holder.storenametxt.setText(company_name);

            String stausval=statusHashMap.get(id);

            Log.e("stausval",stausval);
            if (stausval.equals("1")) {
                holder.activeswitchimageviw.setVisibility(View.VISIBLE);
                holder.inactiveswithimageview.setVisibility(View.GONE);
            } else {
                holder.activeswitchimageviw.setVisibility(View.GONE);
                holder.inactiveswithimageview.setVisibility(View.VISIBLE);
            }

            holder.activeswitchimageviw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   String id = employeesList.get(position).get("id");
//                    statusHashMap.put(id,"0");
//                    holder.activeswitchimageviw.setVisibility(View.GONE);
//                    holder.inactiveswithimageview.setVisibility(View.VISIBLE);
                    showCancelOrderPopup(position, 0, holder, employeesList);
                }
            });

            holder.inactiveswithimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = employeesList.get(position).get("id");
//                    statusHashMap.put(id,"1");
//                    companystauts_array[holder.indexReference]="1";
//
//                    holder.activeswitchimageviw.setVisibility(View.VISIBLE);
//                    holder.inactiveswithimageview.setVisibility(View.GONE);
                    updateStatusWithRetrofit(id, "1", "",
                                        holder, employeesList, position);
                }
            });



//            if (companystauts_array[holder.indexReference].equals("1")) {
//                holder.activeswitchimageviw.setVisibility(View.VISIBLE);
//                holder.inactiveswithimageview.setVisibility(View.GONE);
//            } else {
//                holder.activeswitchimageviw.setVisibility(View.GONE);
//                holder.inactiveswithimageview.setVisibility(View.VISIBLE);
//            }



//            if (status.equals("1")) {
//                holder.switchcompatid.setChecked(true);
//            } else {
//
//                holder.switchcompatid.setChecked(false);
//            }
//
//
//            holder.switchcompatid.setOnCheckedChangeListener(
//                    new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                            // TODO: handle your switch toggling logic here
//                            String storeid = employeesList.get(position).get("id");
//                            if (isChecked == true) {
//                                updateStatusWithRetrofit(storeid, "1", "",
//                                        holder, employeesList, position);
//                            } else {
//                                showCancelOrderPopup(position, 0, holder, employeesList);
//                            }
//                        }
//                    });
//
//
//            // Listen to touch events on the Switch and, when a tap or drag is starting (ACTION_DOWN),
//            // disallow the interception of touch events on the ViewParent (valid until an ACTION_UP).
//            holder.switchcompatid.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        holder.switchcompatid.getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                    // always return false since we don't care about handling tapping, flinging, etc.
//                    return false;
//                }
//            });
        }
        @Override
        public int getItemCount() {
            return employeesList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView emp_id, emp_name, emp_mobile, emp_location, emp_branch, empidtxt;
            ImageView deleteimg, editimg;
            TextView storenametxt;
            SwitchCompat switchcompatid;
            int indexReference;
            Boolean isTouched = false;
            ImageView activeswitchimageviw,inactiveswithimageview;

            MyViewHolder(View view) {
                super(view);
                storenametxt = view.findViewById(R.id.storenametxt);
                switchcompatid = view.findViewById(R.id.switchcompatid);
                activeswitchimageviw = view.findViewById(R.id.activeswitchimageviw);
                inactiveswithimageview = view.findViewById(R.id.inactiveswithimageview);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {


            }


        }

        public void showCancelOrderPopup(final int position, final int status, final MyViewHolder holder,
                                         final ArrayList<HashMap<String, String>> storeshashmapList) {
            /** Used for Show Disclaimer Pop up screen */
            alertDialog = new Dialog(FinanceManageDealerActivity.this);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            layout = inflater.inflate(R.layout.layout_managestores_popup, null);
            TextView reason_cancel = layout.findViewById(R.id.reason_cancel);
            reason_cancel.setText("Reason for reject");
            alertDialog.setContentView(layout);
            alertDialog.setCancelable(true);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            submittxt = layout.findViewById(R.id.submittxt);
            reason_edtxt = layout.findViewById(R.id.cancelorder_txt);
            reason = reason_edtxt.getText().toString();
            closeicon = layout.findViewById(R.id.closeicon);
            closeicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    getEmployeeList("cancel");
                }
            });
            submittxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    reason = reason_edtxt.getText().toString();
                    String storeid = storeshashmapList.get(position).get("id");
                    updateStatusWithRetrofit(storeid, String.valueOf(status), reason, holder, storeshashmapList, position);
                }
            });
        }

        private void updateStatusWithRetrofit(final String storeid, final String storestatus, String reason,
                                              final MyViewHolder holder,
                                              final ArrayList<HashMap<String, String>> storeshashmapList,
                                              final int pos
        ) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<StoresDTO> mService = mApiService.updateStoreStatus(PRIMARYID, storeid, storestatus, reason);
            mService.enqueue(new Callback<StoresDTO>() {
                @Override
                public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
                    StoresDTO mLoginObject = response.body();
                    dialog.dismiss();
                    Log.e("response", "" + response);
                    try {
                        String status = mLoginObject.getStatus();
                        Log.e("status", status);
                        if (status.equals("1")) {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if(storestatus.equals("1"))
                            {
                                statusHashMap.put(storeid,"1");
                                holder.activeswitchimageviw.setVisibility(View.VISIBLE);
                                holder.inactiveswithimageview.setVisibility(View.GONE);
                            }else{
                                statusHashMap.put(storeid,"0");
                                holder.activeswitchimageviw.setVisibility(View.GONE);
                                holder.inactiveswithimageview.setVisibility(View.VISIBLE);
                            }
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<StoresDTO> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }


        private void hideKeyboard() {
            // Check if no view has focus:
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}







//package com.nova.b2b_finance_dept;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.SearchManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.SwitchCompat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.adapter.SwitchRecyclerAdapter;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.EmployeeDTO;
//import com.nova.model.EmployeeListDTO;
//import com.nova.model.StoresDTO;
//import com.nova.model.SwitchItemDTO;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by prasad on 3/20/2018.
// */
//public class FinanceManageDealerActivity extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    TextView txt_nodata;
//    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
//    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//    String SHORTFROM = "";
//    String SHORTFORMVAL = "";
//    String PRIMARYID = "";
//
//    String TITLE = "";
//    Context context;
//    RecyclerViewAdapter adapter;
//    public static Activity mainfinish;
//    GlobalShare globalShare;
//    private Dialog otpalertDialog;
//    private View otplayout;
//    private EditText otpmobilenumber;
//    private Button popup_submit;
//    private String mobileOTP = "";
//    private String MOBILEOTPVAL = "";
//
//
//    private String BRANCHID;
//    private String id;
//    private String sellerid;
//    private String count;
//    private String SHORTFORM;
//
//    private Dialog alertDialog;
//    private View layout;
//    private TextView submittxt;
//    private ImageView closeicon;
//    EditText reason_edtxt;
//    String reason;
//
//    private List<SwitchItemDTO> mDataList = null;
//    private SwitchRecyclerAdapter mAdapter;
//    String[] COMPANYNamesArray;
//    String[] DEALERID_Array;
//    String[] DEALERNAME_Array;
//    String[] EMPID_Array;
//    String[] COMPANYSTATUS_Array;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        globalShare = (GlobalShare) getApplicationContext();
//        setTheme(R.style.AppTheme);
//        context = this;
//        mainfinish = this;
//
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            SHORTFORMVAL = values.get(SharedDB.SHORTFORM);
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//            BRANCHID = values.get(SharedDB.BRANCHID);
//        }
//
//        setContentView(R.layout.layout_managestores);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle("Manage Dealers");
////
//        recyclerView = findViewById(R.id.managestoresrecyclerview);
//        txt_nodata = findViewById(R.id.txt_nodata);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(mLayoutManager);
//        getEmployeeList("");
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.employee_search, menu);
//        final MenuItem searchItem = menu.findItem(R.id.search);
//
//        if (searchItem != null) {
//            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//                    //some operation
//                    return true;
//                }
//            });
//
//            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//            searchPlate.setHint("Search");
//            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
//            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
//            // use this method for search process
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    // use this method when query submitted
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    // use this method for auto complete search process
//                    filteremployeehashmapList = new ArrayList<HashMap<String, String>>();
//
//                    String[] dealerid_array = new String[hashmapList.size()];
//                    String[] dealername_array = new String[hashmapList.size()];
//                    String[] empid_array = new String[hashmapList.size()];
//                    String[] companyname_array = new String[hashmapList.size()];
//                    String[] companystauts_array = new String[hashmapList.size()];
//
//                    if(hashmapList!=null)
//                    {
//                        if(hashmapList.size()>0)
//                        {
//                            for(int i=0;i<hashmapList.size();i++)
//                            {
//                                String company_name = hashmapList.get(i).get("company_name");
//                                if ((company_name.toLowerCase()).contains(newText.toLowerCase())) {
//
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String id = hashmapList.get(i).get("id");
//                                    String name = hashmapList.get(i).get("name");
//                                    String empid = hashmapList.get(i).get("empid");
//                                    String status = hashmapList.get(i).get("status");
//                                    String companyname = hashmapList.get(i).get("company_name");
//
//
//                                    hashMap.put("id", id);
//                                    hashMap.put("name", name);
//                                    hashMap.put("empid", empid);
//                                    hashMap.put("status", status);
//                                    hashMap.put("company_name", companyname);
//
//                                    filteremployeehashmapList.add(hashMap);
//
//                                    dealerid_array[i]=id;
//                                    dealername_array[i]=name;
//                                    empid_array[i]=empid;
//                                    companyname_array[i]=companyname;
//                                    companystauts_array[i]=status;
//
//                                    showOrdersData(filteremployeehashmapList,dealerid_array,dealername_array,empid_array,
//                                            companyname_array,companystauts_array);
//                                }
//                            }
//                        }
//                    }
//
////                    if (hashmapList != null) {
////                        if (hashmapList.size() > 0) {
////                            String[] dealerstatus_array = new String[hashmapList.size()];
////                            String[] dealername_array = new String[hashmapList.size()];
////                            String[] dealerid_array = new String[hashmapList.size()];
////
////                            for (int i = 0; i < hashmapList.size(); i++) {
////                                String company_name = hashmapList.get(i).get("company_name");
////
////                                if ((company_name.toLowerCase()).contains(newText.toLowerCase())) {
////                                    HashMap<String, String> hashMap = new HashMap<String, String>();
////                                    String id = hashmapList.get(i).get("id");
////                                    String name = hashmapList.get(i).get("name");
////                                    String empid = hashmapList.get(i).get("empid");
////                                    String status = hashmapList.get(i).get("status");
////                                    String companyname = hashmapList.get(i).get("company_name");
////
////                                    companyNamesArray[i]=company_name;
////
//////                                    if(status.equals("1"))
//////                                    {
////                                        dealerstatus_array[i] = status;
////                                        hashMap.put("id", id);
////                                        hashMap.put("name", name);
////                                        hashMap.put("empid", empid);
////                                        hashMap.put("status", status);
////                                        hashMap.put("company_name", companyname);
////
////                                        filteremployeehashmapList.add(hashMap);
////                                        showOrdersData(filteremployeehashmapList,dealerstatus_array,dealername_array,dealerid_array);
////                                   // }
////                                }
////                            }
////                        }
////                    }
//                    return false;
//                }
//            });
//            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            case R.id.search:
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void getEmployeeList(String fromval) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<EmployeeDTO> mService = null;
//
//        mService = mApiService.getDealerNamesList(BRANCHID);
//        mService.enqueue(new Callback<EmployeeDTO>() {
//            @Override
//            public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//                try {
//                    EmployeeDTO mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//                    Log.e("ordersstatus", "" + status);
//                    if (Integer.parseInt(status) == 1) {
//                        List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
//                        if (ordersList != null) {
//                            if (ordersList.size() > 0) {
//                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
//
//                                hashmapList=new ArrayList<HashMap<String, String>>();
//
//                                String[] dealerid_array = new String[ordersList.size()];
//                                String[] dealername_array = new String[ordersList.size()];
//                                String[] empid_array = new String[ordersList.size()];
//                                String[] companyname_array = new String[ordersList.size()];
//                                String[] companystauts_array = new String[ordersList.size()];
//
//
//                                DEALERID_Array=new String[ordersList.size()];
//                                DEALERNAME_Array=new String[ordersList.size()];
//                                EMPID_Array=new String[ordersList.size()];
//                                COMPANYNamesArray=new String[ordersList.size()];
//                                COMPANYSTATUS_Array=new String[ordersList.size()];
//
//                                for (int i = 0; i < ordersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String id = ordersList.get(i).getId();
//                                    String name = ordersList.get(i).getName();
//                                    String empid = ordersList.get(i).getEmp_id();
//                                    String company_name = ordersList.get(i).getCompany_name();
//                                    String status1 = ordersList.get(i).getStatus();
//
//                                         dealerid_array[i]=id;
//                                         dealername_array[i]=name;
//                                         empid_array[i]=empid;
//                                         companyname_array[i]=company_name;
//                                         companystauts_array[i]=status1;
//
//                                        DEALERID_Array[i]=id;
//                                        DEALERNAME_Array[i]=name;
//                                        EMPID_Array[i]=empid;
//                                        COMPANYNamesArray[i]=company_name;
//                                        COMPANYSTATUS_Array[i]=status1;
//
//                                        hashMap.put("id", id);
//                                        hashMap.put("name", name);
//                                        hashMap.put("empid", empid);
//                                        hashMap.put("status", status1);
//                                        hashMap.put("company_name", company_name);
//
//                                        hashmapList.add(hashMap);
////                                    }else{
////                                    }
//                                }
//                                showOrdersData(hashmapList,dealerid_array,dealername_array,empid_array,
//                                        companyname_array,companystauts_array);
//                            } else {
//                                recyclerView.setVisibility(View.GONE);
//                                txt_nodata.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.GONE);
//                        txt_nodata.setVisibility(View.VISIBLE);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList,String[] dealerid_array,String[] dealername_array,String[] empid_array,
//                               String[] companyname_array,String[] companystauts_array) {
//        if (hashmapList.size() > 0) {
//            recyclerView.setVisibility(View.VISIBLE);
//            txt_nodata.setVisibility(View.GONE);
//            adapter = new RecyclerViewAdapter(getApplicationContext(),hashmapList,dealerid_array,dealername_array,
//                    empid_array,companyname_array,companystauts_array);
//            recyclerView.setAdapter(adapter);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//            txt_nodata.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
//        private Context mContext;
//
//        ArrayList<HashMap<String, String>> hashMapArrayList;
//        String[] dealerid_array;
//        String[] dealername_array;
//        String[] empid_array;
//        String[] companyname_array;
//        String[] companystauts_array;
//        public RecyclerViewAdapter(Context mContext,ArrayList<HashMap<String, String>> hashMapArrayList,String[] dealerid_array,String[] dealername_array,String[] empid_array,
//                                   String[] companyname_array,String[] companystauts_array){
//            this.mContext = mContext;
//            this.hashMapArrayList=hashMapArrayList;
//            this.dealerid_array=dealerid_array;
//            this.dealername_array=dealername_array;
//            this.empid_array=empid_array;
//            this.companyname_array=companyname_array;
//            this.companystauts_array=companystauts_array;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.layout_managestoresadapter, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//            String company_name =hashMapArrayList.get(position).get("company_name");
//            String id=hashMapArrayList.get(position).get("id");
//            holder.storenametxt.setText(company_name);
//
//            //COMPANYSTATUS_Array=companystauts_array;
//            holder.indexReference = position;
//
//            if (companystauts_array[holder.indexReference].equals("1")) {
//                holder.activeswitchimageviw.setVisibility(View.VISIBLE);
//                holder.inactiveswithimageview.setVisibility(View.GONE);
//            } else {
//                holder.activeswitchimageviw.setVisibility(View.GONE);
//                holder.inactiveswithimageview.setVisibility(View.VISIBLE);
//            }
//
//
//           holder.activeswitchimageviw.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View v) {
//                   //showCancelOrderPopup(position, 0, holder, employeesList,dealerstatus_array);
//                   companystauts_array[holder.indexReference]="0";
//                   holder.activeswitchimageviw.setVisibility(View.GONE);
//                   holder.inactiveswithimageview.setVisibility(View.VISIBLE);
//               }
//           });
//
//            holder.inactiveswithimageview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String storeid = dealerid_array[position];;
////                    updateStatusWithRetrofit(storeid, "1", "",
////                            holder, employeesList, position,dealerstatus_array);
//                    companystauts_array[holder.indexReference]="1";
//                    holder.activeswitchimageviw.setVisibility(View.VISIBLE);
//                    holder.inactiveswithimageview.setVisibility(View.GONE);
//                }
//            });
//
//
//
//
//
////                        holder.switchcompatid.setOnCheckedChangeListener(
////                    new CompoundButton.OnCheckedChangeListener() {
////                        @Override
////                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
////                            // TODO: handle your switch toggling logic here
////                            String storeid = employeesList.get(position).get("id");
////                            if (isChecked == true) {
////                                updateStatusWithRetrofit(storeid, "1", "",
////                                        holder, employeesList, position);
////                            } else {
////                              //  showCancelOrderPopup(position, 0, holder, employeesList);
////                            }
////                        }
////                    });
////            holder.switchcompatid.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
////                        holder.switchcompatid.getParent().requestDisallowInterceptTouchEvent(false);
////                    }
////                    // always return false since we don't care about handling tapping, flinging, etc.
////                    return false;
////                }
////            });
//
////
////                boolean isConnectedToInternet = CheckNetWork
////                        .isConnectedToInternet(getApplicationContext());
////                if (isConnectedToInternet) {
////                    holder.switchcompatid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                        @Override
////                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                            String storeid = dealerid_array[holder.indexReference];
////                            if (isChecked == true) {
////
//////                                updateStatusWithRetrofit(storeid, "1", "",
//////                                        holder,employeesList,position);
////
////                                dealerstatus_array[holder.indexReference]="1";
////                                Toast.makeText(getApplicationContext(),"is checked CALLED "+storeid,Toast.LENGTH_SHORT).show();
////                            } else {
////                               // showCancelOrderPopup(position, 0,holder,employeesList);
////                                dealerstatus_array[holder.indexReference]="0";
////                                Toast.makeText(getApplicationContext(),"un check CALLED "+storeid,Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    });
////                } else {
////                    Toast.makeText(getApplicationContext(), R.string.networkerror,
////                            Toast.LENGTH_SHORT).show();
////                }
//        }
//
////        @Override
////        public void onViewRecycled(@NonNull MyViewHolder holder) {
////            super.onViewRecycled(holder);
////            holder.switchcompatid.setOnCheckedChangeListener(null);
////        }
////
////        interface OnItemCheckedChangeListener {
////            /**
////             * Fired when the item check state is changed
////             */
////            void onItemCheckedChanged(int position, boolean isChecked);
////        }
//
//        @Override
//        public int getItemCount() {
//            return hashMapArrayList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView emp_id, emp_name, emp_mobile, emp_location, emp_branch, empidtxt;
//            ImageView deleteimg, editimg;
//            TextView storenametxt;
//            SwitchCompat switchcompatid;
//            int indexReference;
//            Boolean isTouched = false;
//            ImageView activeswitchimageviw,inactiveswithimageview;
//
//            MyViewHolder(View view) {
//                super(view);
//                storenametxt = view.findViewById(R.id.storenametxt);
//                switchcompatid = view.findViewById(R.id.switchcompatid);
//
//                activeswitchimageviw = view.findViewById(R.id.activeswitchimageviw);
//                inactiveswithimageview = view.findViewById(R.id.inactiveswithimageview);
//
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//
//
//            }
//
//
//        }
//
//        public void showCancelOrderPopup(final int position, final int status, final MyViewHolder holder,
//                                         final ArrayList<HashMap<String, String>> storeshashmapList,
//                                         final String[] dealerstatus_array) {
//            /** Used for Show Disclaimer Pop up screen */
//            alertDialog = new Dialog(FinanceManageDealerActivity.this);
//            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            alertDialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent);
//            LayoutInflater inflater = getLayoutInflater();
//            layout = inflater.inflate(R.layout.layout_managestores_popup, null);
//            TextView reason_cancel = layout.findViewById(R.id.reason_cancel);
//            reason_cancel.setText("Reason for reject");
//            alertDialog.setContentView(layout);
//            alertDialog.setCancelable(true);
//            if (!alertDialog.isShowing()) {
//                alertDialog.show();
//            }
//            submittxt = layout.findViewById(R.id.submittxt);
//            reason_edtxt = layout.findViewById(R.id.cancelorder_txt);
//            reason = reason_edtxt.getText().toString();
//            closeicon = layout.findViewById(R.id.closeicon);
//            closeicon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (alertDialog != null) {
//                        alertDialog.dismiss();
//                    }
//                    getEmployeeList("cancel");
//                }
//            });
//            submittxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (alertDialog != null) {
//                        alertDialog.dismiss();
//                    }
//                    reason = reason_edtxt.getText().toString();
//                    String storeid = storeshashmapList.get(position).get("id");
//                    updateStatusWithRetrofit(storeid, String.valueOf(status), reason, holder, storeshashmapList, position,
//                            dealerstatus_array);
//                }
//            });
//        }
//
//        private void updateStatusWithRetrofit(String storeid, final String storestatus, String reason,
//                                              final MyViewHolder holder,
//                                              final ArrayList<HashMap<String, String>> storeshashmapList,
//                                              final int pos,final String[] dealerstatus_array) {
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//            Call<StoresDTO> mService = mApiService.updateStoreStatus(PRIMARYID, storeid, storestatus, reason);
//            mService.enqueue(new Callback<StoresDTO>() {
//                @Override
//                public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
//                    StoresDTO mLoginObject = response.body();
//                    dialog.dismiss();
//                    Log.e("response", "" + response);
//                    try {
//                        String status = mLoginObject.getStatus();
//                        Log.e("status", status);
//
//                        if (status.equals("1")) {
//                            String message = mLoginObject.getMessage();
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                            dealerstatus_array[holder.indexReference]="0";
//
//
//
//
////                            hashmapList=new ArrayList<>();
////                            recyclerView.setAdapter(null);
////                            adapter.notifyDataSetChanged();
////                            getEmployeeList("");
//                        } else {
//
//                            String message = mLoginObject.getMessage();
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                            Intent intent=new Intent(getApplicationContext(),FinanceManageDealerActivity.class);
////                            startActivity(intent);
////                            finish();
//                            hashmapList=new ArrayList<>();
//                            recyclerView.setAdapter(null);
//                            adapter.notifyDataSetChanged();
//                            getEmployeeList("");
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<StoresDTO> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//        private void hideKeyboard() {
//            // Check if no view has focus:
//            View view = getCurrentFocus();
//            if (view != null) {
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }
//
//
//}
//
//
////FinanceManageDealerActivity
////
////import android.app.Dialog;
////import android.app.SearchManager;
////import android.content.Context;
////import android.os.Bundle;
////import android.support.annotation.Nullable;
////import android.support.v4.content.ContextCompat;
////import android.support.v4.view.MenuItemCompat;
////import android.support.v7.app.AppCompatActivity;
////import android.support.v7.widget.LinearLayoutManager;
////import android.support.v7.widget.RecyclerView;
////import android.support.v7.widget.SearchView;
////import android.support.v7.widget.SwitchCompat;
////import android.util.Log;
////import android.view.LayoutInflater;
////import android.view.Menu;
////import android.view.MenuItem;
////import android.view.View;
////import android.view.ViewGroup;
////import android.view.Window;
////import android.widget.CompoundButton;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.RelativeLayout;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.nova.R;
////import com.nova.constants.CheckNetWork;
////import com.nova.constants.GlobalShare;
////import com.nova.constants.RetrofitAPI;
////import com.nova.constants.TransparentProgressDialog;
////import com.nova.db.SharedDB;
////import com.nova.model.EmployeeDTO;
////import com.nova.model.EmployeeListDTO;
////import com.nova.model.StoresDTO;
////
////import java.util.ArrayList;
////import java.util.HashMap;
////import java.util.List;
////
////import retrofit2.Call;
////import retrofit2.Callback;
////import retrofit2.Response;
////
////public class FinanceManageDealerActivity extends AppCompatActivity {
////
////    ArrayList<HashMap<String, String>> filteremployeehashmapList;
////    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
////
////    RecyclerView managestoresrecyclerview;
////    private HashMap<String, String> values;
////    private String PRIMARYID = "";
////    private String USERTYPE = "";
////    private String BRANCHID = "";
////    private Dialog alertDialog;
////    private View layout;
////    private TextView submittxt;
////    private ImageView closeicon;
////    EditText reason_edtxt;
////    String reason;
////    StoresListAdapter adapter;
////    RelativeLayout storesbtmrel;
////    RelativeLayout imgrel;
////    ImageView imageview;
////    GlobalShare globalShare;
////    Context context;
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        context=this;
////        setTheme(R.style.AppTheme);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        setTitle("Manage Dealers");
////        setContentView(R.layout.layout_managestores);
////
////        managestoresrecyclerview = findViewById(R.id.managestoresrecyclerview);
////        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
////        managestoresrecyclerview.setHasFixedSize(true);
////        managestoresrecyclerview.setLayoutManager(mLayoutManager);
////        //txt_nodata = rootView.findViewById(R.id.txt_nodata);
////
////        imgrel=findViewById(R.id.imgrel);
////        imageview=findViewById(R.id.imageview);
////        storesbtmrel = findViewById(R.id.storesbtmrel);
////
////        if (SharedDB.isLoggedIn(getApplicationContext())) {
////            values = SharedDB.getUserDetails(getApplicationContext());
////            PRIMARYID = values.get(SharedDB.PRIMARYID);
////            USERTYPE = values.get(SharedDB.USERTYPE);
////            BRANCHID = values.get(SharedDB.BRANCHID);
////        }
////        getDealersList("");
////
////    }
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.employee_search, menu);
////        final MenuItem searchItem = menu.findItem(R.id.search);
////
////        if (searchItem != null) {
////            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
////            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
////                @Override
////                public boolean onClose() {
////                    //some operation
////                    return true;
////                }
////            });
////
////            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
////            searchPlate.setHint("Search");
////            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
////            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
////            // use this method for search process
////            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                @Override
////                public boolean onQueryTextSubmit(String query) {
////                    // use this method when query submitted
////                    return false;
////                }
////
////                @Override
////                public boolean onQueryTextChange(String newText) {
////                    // use this method for auto complete search process
////
////                    Log.e("newText",newText);
////                    filteremployeehashmapList = new ArrayList<HashMap<String, String>>();
////                    Log.e("hashmapList size",""+hashmapList.size());
////                    if (hashmapList != null) {
////                        if (hashmapList.size() > 0) {
////                            for (int i = 0; i < hashmapList.size(); i++) {
////
////                                String empId = hashmapList.get(i).get("empid");
////
////                                Log.e("empId",empId);
////
////                                if ((empId.toLowerCase()).contains(newText.toLowerCase())) {
////                                    HashMap<String, String> hashMap = new HashMap<String, String>();
////                                    String id = hashmapList.get(i).get("id");
////                                    String name = hashmapList.get(i).get("name");
////                                    String empid = hashmapList.get(i).get("empid");
////                                    String status1 = hashmapList.get(i).get("status");
////
////                                    hashMap.put("id", id);
////                                    hashMap.put("name", name);
////                                    hashMap.put("empid", empid);
////                                    hashMap.put("status", status1);
////                                    hashmapList.add(hashMap);
////
////                                    filteremployeehashmapList.add(hashMap);
////                                    showData(filteremployeehashmapList);
////                                }
//////
////////                                else{
////////                                    if((empName.toLowerCase()).contains((newText.toLowerCase()))){
////////                                        HashMap<String, String> hashMap = new HashMap<String, String>();
////////                                        String id = hashmapList.get(i).get("id");
////////                                        String name = hashmapList.get(i).get("name");
////////                                        String empid = hashmapList.get(i).get("empid");
////////                                        String status1 = hashmapList.get(i).get("status");
////////
////////                                        hashMap.put("id", id);
////////                                        hashMap.put("name", name);
////////                                        hashMap.put("empid", empid);
////////                                        hashMap.put("status", status1);
////////                                        hashmapList.add(hashMap);
////////
////////                                        filteremployeehashmapList.add(hashMap);
////////                                        showData(filteremployeehashmapList);
////////                                    }
////////                                }
////                            }
////                        }
////                   }
////                    return false;
////                }
////            });
////            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
////            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
////        }
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case android.R.id.home:
////                finish();
////                return true;
////            case R.id.search:
////
////                return true;
////            default:
////                return super.onOptionsItemSelected(item);
////        }
////    }
////
////    private void getDealersList(final String fromval) {
////        final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
////        dialog.show();
////        RetrofitAPI mApiService = SharedDB.getInterfaceService();
////        Call<EmployeeDTO> mService=null;
////        mService = mApiService.getDealerNamesList(BRANCHID);
////        mService.enqueue(new Callback<EmployeeDTO>() {
////            @Override
////            public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
////                dialog.dismiss();
////                Log.e("response", "" + response);
////                try {
////                    EmployeeDTO mOrderObject = response.body();
////                    String status = mOrderObject.getStatus();
////                    Log.e("ordersstatus", "" + status);
////                    if (Integer.parseInt(status) == 1) {
////                        List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
////                        if (ordersList != null) {
////                            if (ordersList.size() > 0) {
////                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
////
////                                for (int i = 0; i < ordersList.size(); i++) {
////                                    HashMap<String, String> hashMap = new HashMap<String, String>();
////                                    String id = ordersList.get(i).getId();
////                                    String name = ordersList.get(i).getName();
//////                                    String first_name = ordersList.get(i).getFirst_name();
//////                                    String last_name = ordersList.get(i).getLast_name();
////                                    String empid = ordersList.get(i).getEmp_id();
//////                                    String branch = ordersList.get(i).getBranch();
//////                                    String mobile = ordersList.get(i).getMobile();
//////                                    String email = ordersList.get(i).getEmail();
//////                                    String dob = ordersList.get(i).getDob();
//////                                    String address = ordersList.get(i).getAddress();
//////                                    String role_name = ordersList.get(i).getRole_name();
//////                                    String branch_name = ordersList.get(i).getBranch_name();
////                                    String status1 = ordersList.get(i).getStatus();
////
////                                    hashMap.put("id", id);
////                                    hashMap.put("name", name);
//////                                    hashMap.put("first_name", first_name);
//////                                    hashMap.put("last_name", last_name);
////                                    hashMap.put("empid", empid);
//////                                    hashMap.put("branch", branch);
//////                                    hashMap.put("mobile", mobile);
//////                                    hashMap.put("email", email);
//////                                    hashMap.put("dob", dob);
//////                                    hashMap.put("address", address);
//////                                    hashMap.put("role_name", role_name);
//////                                    hashMap.put("branch_name", branch_name);
////                                    hashMap.put("status", status1);
//////                                    items[i]=name;
//////                                    itemsstatus[i]=status1;
//////                                    itemid[i]=empid;
////
////                                    hashmapList.add(hashMap);
////                                }
////                                showData(hashmapList);
////                            } else {
////                                managestoresrecyclerview.setVisibility(View.GONE);
////                                storesbtmrel.setVisibility(View.GONE);
////                                imgrel.setVisibility(View.VISIBLE);
////                                imageview.setImageResource(R.drawable.nostoresfound);
////                            }
////                        }
////                    } else {
////                        managestoresrecyclerview.setVisibility(View.GONE);
////                        storesbtmrel.setVisibility(View.GONE);
////                        imgrel.setVisibility(View.VISIBLE);
////                        imageview.setImageResource(R.drawable.nostoresfound);
////                    }
////                } catch (Exception e) {
////                }
////            }
////
////            @Override
////            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
////                call.cancel();
////                dialog.dismiss();
////                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////
////    public void showData(ArrayList<HashMap<String, String>> storeshashmapList) {
////        if (storeshashmapList.size() > 0) {
////
////            managestoresrecyclerview.setVisibility(View.VISIBLE);
////            adapter = new StoresListAdapter(this, storeshashmapList);
////            managestoresrecyclerview.setAdapter(adapter);
////            imgrel.setVisibility(View.GONE);
////            storesbtmrel.setVisibility(View.VISIBLE);
////        } else {
////            managestoresrecyclerview.setVisibility(View.GONE);
////            imgrel.setVisibility(View.VISIBLE);
////            imageview.setImageResource(R.drawable.nostoresfound);
////            storesbtmrel.setVisibility(View.GONE);
////        }
////    }
////
////    public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.MyViewHolder> {
////        private Context mContext;
////        ArrayList<HashMap<String, String>> storeshashmapList;
////
////        public StoresListAdapter(Context mContext, ArrayList<HashMap<String, String>> storeshashmapList) {
////            this.mContext = mContext;
////            this.storeshashmapList = storeshashmapList;
////        }
////
////        @Override
////        public StoresListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            View itemView = LayoutInflater.from(parent.getContext())
////                    .inflate(R.layout.layout_managestoresadapter, parent, false);
////            return new StoresListAdapter.MyViewHolder(itemView);
////        }
////
////        @Override
////        public void onBindViewHolder(final StoresListAdapter.MyViewHolder holder, final int position) {
////            //hashMap.put("empid", empid);
////            try{
////                String storeName="";
////                String empid="";
////                String status="";
////                storeName = storeshashmapList.get(position).get("name");
////                empid = storeshashmapList.get(position).get("empid");
////                status = storeshashmapList.get(position).get("status");
////
////                String dealer_Name=storeName+" / "+empid;
////                dealer_Name=dealer_Name.trim();
////                char first = dealer_Name.charAt(0);
////                String firstchar=String.valueOf(first);
////                if(firstchar.equals("/"))
////                {
////                    holder.storenametxt.setText(empid);
////                }else{
////                    holder.storenametxt.setText(storeName+" / "+empid);
////                }
////
//////             if(itemsstatus[holder.indexRefrence].equals("1"))
//////             {
//////                 holder.switchcompatid.setChecked(true);
//////             }else{
//////                 holder.switchcompatid.setChecked(false);
//////             }
////
////            if (status.equals("1")) {
////                holder.switchcompatid.setChecked(true);
////            } else {
////                holder.switchcompatid.setChecked(false);
////               }
////                boolean isConnectedToInternet = CheckNetWork
////                        .isConnectedToInternet(getApplicationContext());
////                if (isConnectedToInternet) {
////                    holder.switchcompatid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                        @Override
////                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                            String storeid = storeshashmapList.get(position).get("id");
////                            if (isChecked == true) {
////                                updateStatusWithRetrofit(storeid, "1", "",
////                                        holder,storeshashmapList,position);
////                            } else {
////
////                                showCancelOrderPopup(position, 0,holder,storeshashmapList);
////                            }
////                        }
////                    });
////                } else {
////                    Toast.makeText(getApplicationContext(), R.string.networkerror,
////                            Toast.LENGTH_SHORT).show();
////
////                }
////            }catch (Exception e)
////            {
////            }
////        }
////
////        public void showCancelOrderPopup(final int position, final int status, final MyViewHolder holder,
////                                         final ArrayList<HashMap<String, String>> storeshashmapList) {
////            /** Used for Show Disclaimer Pop up screen */
////            alertDialog = new Dialog(getApplicationContext());
////            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////            alertDialog.getWindow().setBackgroundDrawableResource(
////                    android.R.color.transparent);
////            LayoutInflater inflater = getLayoutInflater();
////            layout = inflater.inflate(R.layout.layout_managestores_popup, null);
////            TextView reason_cancel = layout.findViewById(R.id.reason_cancel);
////            reason_cancel.setText("Reason for reject");
////            alertDialog.setContentView(layout);
////            alertDialog.setCancelable(true);
////            if (!alertDialog.isShowing()) {
////                alertDialog.show();
////            }
////            submittxt = layout.findViewById(R.id.submittxt);
////            reason_edtxt = layout.findViewById(R.id.cancelorder_txt);
////            reason = reason_edtxt.getText().toString();
////            closeicon = layout.findViewById(R.id.closeicon);
////            closeicon.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    if (alertDialog != null) {
////                        alertDialog.dismiss();
////                    }
////                    getDealersList("cancel");
////                }
////            });
////            submittxt.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    if (alertDialog != null) {
////                        alertDialog.dismiss();
////                    }
////                    reason = reason_edtxt.getText().toString();
////                    String storeid = storeshashmapList.get(position).get("id");
////                    updateStatusWithRetrofit(storeid, String.valueOf(status), reason,holder,storeshashmapList,position);
////                }
////            });
////        }
////
////        @Override
////        public int getItemCount() {
////            return storeshashmapList.size();
////        }
////
////        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
////            TextView storenametxt;
////             SwitchCompat switchcompatid;
////            int indexRefrence;
////
////            MyViewHolder(View view) {
////                super(view);
////
////                storenametxt = view.findViewById(R.id.storenametxt);
////                 switchcompatid = view.findViewById(R.id.switchcompatid);
////                view.setOnClickListener(this);
////            }
////
////            @Override
////            public void onClick(View view) {
//////                Intent intent = new Intent(getActivity(), StoreOrderDetailsActivity.class);
//////                String storeid = storeshashmapList.get(getAdapterPosition()).get("id");
//////                intent.putExtra("store", storeid);
//////                startActivity(intent);
////            }
////        }
////
////        private void updateStatusWithRetrofit(String storeid, final String storestatus, String reason,
////                                              final MyViewHolder holder,
////                                              final  ArrayList<HashMap<String, String>> storeshashmapList,
////                                              final int pos
////        ) {
////            final TransparentProgressDialog dialog = new TransparentProgressDialog(FinanceManageDealerActivity.this);
////            dialog.show();
////            RetrofitAPI mApiService = SharedDB.getInterfaceService();
////            Call<StoresDTO> mService = mApiService.updateStoreStatus(PRIMARYID, storeid, storestatus, reason);
////            mService.enqueue(new Callback<StoresDTO>() {
////                @Override
////                public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
////                    StoresDTO mLoginObject = response.body();
////                    dialog.dismiss();
////                    Log.e("response",""+response);
////                    try {
////                        String status = mLoginObject.getStatus();
////                        Log.e("status",status);
////
////                        if (status.equals("1")) {
////                            String message = mLoginObject.getMessage();
////
////                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                            if (adapter != null) {
////                                adapter.notifyDataSetChanged();
////                                //  holder.switchcompatid.setChecked(false);
////                            }
////                            storeshashmapList.remove(pos);
////                            adapter.notifyDataSetChanged();
////
////                            //    getDealersList("cancel");
////                        } else {
////                            String message = mLoginObject.getMessage();
////                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                            if (storestatus.equals("0")) {
////                                storeshashmapList.remove(holder.indexRefrence);
////                                adapter.notifyDataSetChanged();
////                                getDealersList("cancel");
////
//////                                globalShare.setFinancemenuselectpos("5");
//////
//////                                if(FinanceDeptMenuScreenActivity.mainfinish!=null)
//////                                {
//////                                    FinanceDeptMenuScreenActivity.mainfinish.finish();
//////                                }
//////                                Intent intent=new Intent(getActivity(),FinanceDeptMenuScreenActivity.class);
//////                                startActivity(intent);
////                            }
////                        }
////                    } catch (Exception e) {
////                    }
////                }
////                @Override
////                public void onFailure(Call<StoresDTO> call, Throwable t) {
////                    call.cancel();
////                    dialog.dismiss();
////                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
////                }
////            });
////        }
////    }
////}

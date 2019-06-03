package com.mrnovacrm.b2b_dealer;

//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.EmployeeDTO;
//import com.nova.model.EmployeeListDTO;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DealersDataActivity extends AppCompatActivity{
//
//    private GlobalShare globalShare;
//    private HashMap<String, String> values;
//    private String PRIMARYID = "";
//    private String USERTYPE = "";
//    private String BRANCHID = "";
//    private String SHORTFORM = "";
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        globalShare = (GlobalShare) getApplicationContext();
//        setTheme(R.style.AppTheme);
//        setTitle("Dealers List");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setContentView(R.layout.layout_dealer);
//
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            USERTYPE = values.get(SharedDB.USERTYPE);
//            BRANCHID = values.get(SharedDB.BRANCHID);
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//        }
//
//    }
//
//    public void loadDealers(final String id_val,final String sellerid_val,final String count_val) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(DealersDataActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<EmployeeDTO> mService = null;
//        mService = mApiService.getDealersLists(BRANCHID,SHORTFORM,PRIMARYID);
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
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
//                                for (int i = 0; i < ordersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//
//                                    String id = ordersList.get(i).getId();
//                                    String name = ordersList.get(i).getName();
//                                    String first_name = ordersList.get(i).getFirst_name();
//                                    String last_name = ordersList.get(i).getLast_name();
//                                    String empid = ordersList.get(i).getEmp_id();
//                                    String branch = ordersList.get(i).getBranch();
//                                    String mobile = ordersList.get(i).getMobile();
//                                    String email = ordersList.get(i).getEmail();
//                                    String dob = ordersList.get(i).getDob();
//                                    String address = ordersList.get(i).getAddress();
//                                    String role_name = ordersList.get(i).getRole_name();
//                                    String branch_name = ordersList.get(i).getBranch_name();
//
//                                    hashMap.put("id", id);
//                                    hashMap.put("name", name);
//                                    hashMap.put("first_name", first_name);
//                                    hashMap.put("last_name", last_name);
//                                    hashMap.put("empid", empid);
//                                    hashMap.put("branch", branch);
//                                    hashMap.put("mobile", mobile);
//                                    hashMap.put("email", email);
//                                    hashMap.put("dob", dob);
//                                    hashMap.put("address", address);
//                                    hashMap.put("role_name", role_name);
//                                    hashMap.put("branch_name", branch_name);
//
//                                    hashmapList.add(hashMap);
//                                }
//                                showDealers(hashmapList,id_val,sellerid_val,count_val);
//                            } else {
//                                Toast.makeText(getApplicationContext(), "No dealers found", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "No dealers found", Toast.LENGTH_SHORT).show();
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
//    public void showDealers()
//    {
//
//    }
//}


import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_finance_dept.UpdateFinanceEmployeesActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchesDTO;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/20/2018.
 */
public class DealersDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txt_nodata;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    String SHORTFROM="";
    String SHORTFORMVAL="";
    String PRIMARYID="";

    String TITLE="";
    Context context;
    RecyclerViewAdapter adapter;
    public static Activity mainfinish;
    GlobalShare globalShare;
    private Dialog otpalertDialog;
    private View otplayout;
    private EditText otpmobilenumber;
    private Button popup_submit;
    private String mobileOTP="";
    private String MOBILEOTPVAL="";


    private String BRANCHID;
    private String id;
    private String sellerid;
    private String count;
    private String SHORTFORM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalShare=(GlobalShare)getApplicationContext();
        setTheme(R.style.AppTheme);
        context=this;
        mainfinish=this;

        Bundle bundle=getIntent().getExtras();
        id= bundle.getString("id");
        sellerid= bundle.getString("sellerid");
        count= bundle.getString("count");
        TITLE= bundle.getString("TITLE");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            SHORTFORMVAL = values.get(SharedDB.SHORTFORM);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        setContentView(R.layout.employee_report_recyclerview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         setTitle("Dealers List");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            BRANCHID = values.get(SharedDB.BRANCHID);
        }
        recyclerView = findViewById(R.id.recyclerView);
        txt_nodata = findViewById(R.id.txt_nodata);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        getEmployeeList();
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
                            for (int i = 0; i < hashmapList.size(); i++) {
                                String empId = hashmapList.get(i).get("empid");
                                String company_name = hashmapList.get(i).get("company_name");
                                String address_val = hashmapList.get(i).get("address");

                                if ((empId.toLowerCase()).contains(newText.toLowerCase())) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = hashmapList.get(i).get("id");
                                    String name = hashmapList.get(i).get("name");
                                    String first_name = hashmapList.get(i).get("first_name");
                                    String last_name = hashmapList.get(i).get("last_name");
                                    String empid = hashmapList.get(i).get("empid");
                                    String branch = hashmapList.get(i).get("branch");
                                    String mobile = hashmapList.get(i).get("mobile");
                                    String email = hashmapList.get(i).get("email");
                                    String dob = hashmapList.get(i).get("dob");
                                    String address = hashmapList.get(i).get("address");
                                    String role = hashmapList.get(i).get("role_name");
                                    String branch_name = hashmapList.get(i).get("branch_name");
                                    String companyname = hashmapList.get(i).get("company_name");
                                    String wallet = hashmapList.get(i).get("wallet");

                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("first_name", first_name);
                                    hashMap.put("last_name", last_name);
                                    hashMap.put("empid", empid);
                                    hashMap.put("branch", branch);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("email", email);
                                    hashMap.put("dob", dob);
                                    hashMap.put("address", address);
                                    hashMap.put("role", role);
                                    hashMap.put("branch_name", branch_name);
                                    hashMap.put("company_name", companyname);
                                    hashMap.put("wallet", wallet);

                                    filteremployeehashmapList.add(hashMap);
                                    showOrdersData(filteremployeehashmapList);
                                }else if ((address_val.toLowerCase()).contains(newText.toLowerCase())) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = hashmapList.get(i).get("id");
                                    String name = hashmapList.get(i).get("name");
                                    String first_name = hashmapList.get(i).get("first_name");
                                    String last_name = hashmapList.get(i).get("last_name");
                                    String empid = hashmapList.get(i).get("empid");
                                    String branch = hashmapList.get(i).get("branch");
                                    String mobile = hashmapList.get(i).get("mobile");
                                    String email = hashmapList.get(i).get("email");
                                    String dob = hashmapList.get(i).get("dob");
                                    String address = hashmapList.get(i).get("address");
                                    String role = hashmapList.get(i).get("role_name");
                                    String branch_name = hashmapList.get(i).get("branch_name");
                                    String companyname = hashmapList.get(i).get("company_name");
                                    String wallet = hashmapList.get(i).get("wallet");

                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("first_name", first_name);
                                    hashMap.put("last_name", last_name);
                                    hashMap.put("empid", empid);
                                    hashMap.put("branch", branch);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("email", email);
                                    hashMap.put("dob", dob);
                                    hashMap.put("address", address);
                                    hashMap.put("role", role);
                                    hashMap.put("branch_name", branch_name);
                                    hashMap.put("company_name", companyname);
                                    hashMap.put("wallet", wallet);

                                    filteremployeehashmapList.add(hashMap);
                                    showOrdersData(filteremployeehashmapList);
                                }else{
                                    if((company_name.toLowerCase()).contains((newText.toLowerCase()))){
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        String id = hashmapList.get(i).get("id");
                                        String name = hashmapList.get(i).get("name");
                                        String first_name = hashmapList.get(i).get("first_name");
                                        String last_name = hashmapList.get(i).get("last_name");
                                        String empid = hashmapList.get(i).get("empid");
                                        String branch = hashmapList.get(i).get("branch");
                                        String mobile = hashmapList.get(i).get("mobile");
                                        String email = hashmapList.get(i).get("email");
                                        String dob = hashmapList.get(i).get("dob");
                                        String address = hashmapList.get(i).get("address");
                                        String role = hashmapList.get(i).get("role_name");
                                        String branch_name = hashmapList.get(i).get("branch_name");
                                        String companyname = hashmapList.get(i).get("company_name");
                                        String wallet = hashmapList.get(i).get("wallet");

                                        hashMap.put("id", id);
                                        hashMap.put("name", name);
                                        hashMap.put("first_name", first_name);
                                        hashMap.put("last_name", last_name);
                                        hashMap.put("empid", empid);
                                        hashMap.put("branch", branch);
                                        hashMap.put("mobile", mobile);
                                        hashMap.put("email", email);
                                        hashMap.put("dob", dob);
                                        hashMap.put("address", address);
                                        hashMap.put("role", role);
                                        hashMap.put("branch_name", branch_name);
                                        hashMap.put("company_name", companyname);
                                        hashMap.put("wallet", wallet);
                                        filteremployeehashmapList.add(hashMap);
                                        showOrdersData(filteremployeehashmapList);
                                    }
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

    private void getEmployeeList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(DealersDataActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;

        if(SHORTFORM.equals("SE"))
        {
            mService = mApiService.getDealersLists(BRANCHID,SHORTFORM,PRIMARYID);
        }else {
            mService = mApiService.getDealersLists(BRANCHID, "DEALER", PRIMARYID);
        }
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
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String id = ordersList.get(i).getId();
                                    String name = ordersList.get(i).getName();
                                    String first_name = ordersList.get(i).getFirst_name();
                                    String last_name = ordersList.get(i).getLast_name();
                                    String empid = ordersList.get(i).getEmp_id();
                                    String branch = ordersList.get(i).getBranch();
                                    String mobile = ordersList.get(i).getMobile();
                                    String email = ordersList.get(i).getEmail();
                                    String dob = ordersList.get(i).getDob();
                                    String address = ordersList.get(i).getAddress();
                                    String role_name = ordersList.get(i).getRole_name();
                                    String branch_name = ordersList.get(i).getBranch_name();
                                    String company_name = ordersList.get(i).getCompany_name();
                                    String wallet = ordersList.get(i).getWallet();
                                    hashMap.put("wallet", wallet);

                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("first_name", first_name);
                                    hashMap.put("last_name", last_name);
                                    hashMap.put("empid", empid);
                                    hashMap.put("branch", branch);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("email", email);
                                    hashMap.put("dob", dob);
                                    hashMap.put("address", address);
                                    hashMap.put("role_name", role_name);
                                    hashMap.put("branch_name", branch_name);
                                    hashMap.put("company_name", company_name);

                                    hashmapList.add(hashMap);
                                }
                                showOrdersData(hashmapList);
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
                //Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
            adapter = new RecyclerViewAdapter(getApplicationContext(), hashmapList);
            recyclerView.setAdapter(adapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            txt_nodata.setVisibility(View.VISIBLE);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            employeesList = hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_emplist_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {
//            String id = employeesList.get(position).get("id");
//            String empId = employeesList.get(position).get("empid");
//            String name = employeesList.get(position).get("name");
//            String address = employeesList.get(position).get("address");
//            String role = employeesList.get(position).get("role");

            String id = employeesList.get(position).get("id");
            String name = employeesList.get(position).get("name");
            String first_name = employeesList.get(position).get("first_name");
            String last_name = employeesList.get(position).get("last_name");
            String empid = employeesList.get(position).get("empid");
            String branch =employeesList.get(position).get("branch");
            String mobile = employeesList.get(position).get("mobile");
            String email = employeesList.get(position).get("email");
            String dob = employeesList.get(position).get("dob");
            String address = employeesList.get(position).get("address");
            String role_name = employeesList.get(position).get("role_name");
            String branch_name = employeesList.get(position).get("branch_name");
            String company_name = employeesList.get(position).get("company_name");
            String wallet= employeesList.get(position).get("wallet");


//            holder.emp_id.setText(empId);
//            holder.emp_name.setText(name);
//            holder.emp_designation.setText(role);
//            holder.emp_location.setText(address);

            holder.emp_id.setText(empid);
           // holder.emp_name.setText(name);
            holder.emp_name.setText(company_name);
            holder.emp_mobile.setText(mobile);
            holder.emp_location.setText(address);
            holder.emp_branch.setText(branch_name);
            holder.deleteimg.setVisibility(View.GONE);
            holder.editimg.setVisibility(View.GONE);

            holder.credibal_linear.setVisibility(View.VISIBLE);

            if(wallet!=null)
            {
                if(wallet.equals("NA"))
                {
                    holder.emp_creditbalance.setText(wallet);
                }else{
                    holder.emp_creditbalance.setText("Rs."+wallet+" /-");
                }
            }
        //    holder.emp_creditbalance.setText(wallet);

//            if(SHORTFORMVAL.equals("SE"))
//            {
//                if(globalShare.getDealersFrom()!=null)
//                {
//                    if(globalShare.getDealersFrom().equals("cart"))
//                    {
//                        holder.deleteimg.setVisibility(View.GONE);
//                        holder.editimg.setVisibility(View.GONE);
//                    }else{
//                        holder.deleteimg.setVisibility(View.VISIBLE);
//                        holder.editimg.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }else{
//                holder.deleteimg.setVisibility(View.VISIBLE);
//                holder.editimg.setVisibility(View.VISIBLE);
//            }


            holder.empidtxt.setText("Dealer ID");

            holder.deleteimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = employeesList.get(position).get("id");
                    String name = employeesList.get(position).get("name");
                    confirmPopup(id, position, employeesList, name);
                }
            });

            holder.editimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = employeesList.get(position).get("id");
                    String name = employeesList.get(position).get("name");
                    String first_name = employeesList.get(position).get("first_name");
                    String last_name = employeesList.get(position).get("last_name");
                    String empid = employeesList.get(position).get("empid");
                    String branch =employeesList.get(position).get("branch");
                    String mobile = employeesList.get(position).get("mobile");
                    String email = employeesList.get(position).get("email");
                    String dob = employeesList.get(position).get("dob");
                    String address = employeesList.get(position).get("address");
                    String role_name = employeesList.get(position).get("role_name");
                    String branch_name = employeesList.get(position).get("branch_name");

                    Intent intent=new Intent(getApplicationContext(),UpdateFinanceEmployeesActivity.class);
                    intent.putExtra("SHORTFROM",SHORTFROM);
                    intent.putExtra("TITLE",TITLE);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("first_name",first_name);
                    intent.putExtra("last_name",last_name);
                    intent.putExtra("empid",empid);
                    intent.putExtra("branch",branch);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("email",email);
                    intent.putExtra("dob",dob);
                    intent.putExtra("address",address);
                    intent.putExtra("role_name",role_name);
                    intent.putExtra("branch_name",branch_name);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeesList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView emp_id, emp_name, emp_mobile, emp_location,emp_branch,empidtxt,emp_creditbalance;
            ImageView deleteimg,editimg;
            LinearLayout credibal_linear;

            MyViewHolder(View view) {
                super(view);
                emp_id = view.findViewById(R.id.emp_id);
                emp_name = view.findViewById(R.id.emp_name);
                emp_mobile = view.findViewById(R.id.emp_mobile);
                emp_location = view.findViewById(R.id.emp_location);
                emp_branch = view.findViewById(R.id.emp_branch);
                deleteimg = view.findViewById(R.id.deleteimg);
                editimg = view.findViewById(R.id.editimg);
                empidtxt = view.findViewById(R.id.empidtxt);
                credibal_linear = view.findViewById(R.id.credibal_linear);
                emp_creditbalance = view.findViewById(R.id.emp_creditbalance);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                String mobile = employeesList.get(getAdapterPosition()).get("mobile");
                String dealer_id = employeesList.get(getAdapterPosition()).get("id");
                String name = employeesList.get(getAdapterPosition()).get("name");
                addtocart(dealer_id,mobile,name);


            }
        }

        private void addtocart(final String sellerid,final String mobile,final String name) {

            final TransparentProgressDialog dialog = new TransparentProgressDialog(DealersDataActivity.this);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();

            Call<Product> mService=null;

                mService = mApiService.addtocart(PRIMARYID, id, sellerid, count,SHORTFORM);
            //Call<Product> mService = mApiService.addtocart(PRIMARYID, id, sellerid, qty);
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response", "" + response);
                        Product mProductObject = response.body();
                        String status = mProductObject.getStatus();
                        String message = mProductObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (Integer.parseInt(status) == 1) {
                            String itemscount = mProductObject.getItems_count();

                            SharedDB.dealerSahred(getApplicationContext(), sellerid,name,mobile);

                            DealerMenuScreenActivity.updateCartCountNotification(itemscount);
                            ProductListActivity.updateCartCountNotification(itemscount);
                            ProductListActivityNew.updateCartCountNotification(itemscount);
                            ProductCategoriesActivity.updateCartCountNotification(itemscount);
                            finish();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }






        public void showOTPPopup(final String dealer_id) {
            //	if(!globalShare.getSelectuserotp().equals("local")) {
            /** Used for Show Disclaimer Pop up screen */
            otpalertDialog = new Dialog(DealersDataActivity.this);
            otpalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            otpalertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getLayoutInflater();
            otplayout = inflater.inflate(R.layout.loginotppopup, null);
            otpalertDialog.setContentView(otplayout);
            otpalertDialog.setCancelable(false);
            if (!otpalertDialog.isShowing()) {
                otpalertDialog.show();
            }

            ImageView cancel = (ImageView) otplayout.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    otpalertDialog.dismiss();
                }
            });

            otpmobilenumber = (EditText) otplayout.findViewById(R.id.otpmobilenumber);
            otpmobilenumber.setHintTextColor(getResources().getColor(R.color.darkgrey));

            popup_submit=(Button)otplayout.findViewById(R.id.popup_submit);
            popup_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobileOTP = otpmobilenumber.getText().toString().trim();
                    if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                            || mobileOTP.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mobileOTP.equals(MOBILEOTPVAL)) {
                            otpalertDialog.dismiss();
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    UserPaymentActivity.class);
                            intent.putExtra("dealer_id", dealer_id);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            otpmobilenumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                private boolean isConnectedToInternet;
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                            || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        isConnectedToInternet = CheckNetWork
                                .isConnectedToInternet(getApplicationContext());
                        hideKeyboard();
                        if (isConnectedToInternet) {
                            mobileOTP = otpmobilenumber.getText().toString();
                            if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                                    || mobileOTP.equals("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Please enter OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                if (mobileOTP.equals(MOBILEOTPVAL)) {
                                    otpalertDialog.dismiss();
                                    Intent intent = new Intent(
                                            getApplicationContext(),
                                            UserPaymentActivity.class);
                                    startActivity(intent);
                                    otpalertDialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Please Check Your Internet Connection",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    return false;
                }
            });
            //}
        }

        private void loginProcessWithRetrofit(final String mobilenumber,final String dealer_id){
            final TransparentProgressDialog dialog =new TransparentProgressDialog(context);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Login> mService = mApiService.authenticate(mobilenumber);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Login mLoginObject = response.body();
                    dialog.dismiss();
                    try {
                        Log.e("response",""+response);
                        MOBILEOTPVAL = mLoginObject.getOtp();
                        Log.e("MOBILEOTPVAL",MOBILEOTPVAL);
                        showOTPPopup(dealer_id);
                    }catch (Exception e)
                    {
                    }
                }
                @Override
                public void onFailure(Call<Login> call, Throwable t) {
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
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        public void confirmPopup(final String employee_id, final int position,
                                 final ArrayList<HashMap<String, String>> employeesList, String name) {
            //  deleteEmployee(employee_id,position,employeesList);
            android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(DealersDataActivity.this)
                    .setMessage("Do you want to delete ?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    deleteEmployee(employee_id, position, employeesList);
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).show();
        }

        public void deleteEmployee(String empip, final int position, final ArrayList<HashMap<String, String>> employeesList) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<BranchesDTO> mService = mApiService.deleteEmployee("trade",empip);
            mService.enqueue(new Callback<BranchesDTO>() {
                @Override
                public void onResponse(Call<BranchesDTO> call, Response<BranchesDTO> response) {
                    Log.e("response", "" + response);
                    BranchesDTO mLoginObject = response.body();
                    dialog.dismiss();
                    try {
                        String status = mLoginObject.getStatus();
                        if (status.equals("1")) {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            // employeesList.remove(position);
                            // adapter.notifyDataSetChanged();

                            employeesList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<BranchesDTO> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

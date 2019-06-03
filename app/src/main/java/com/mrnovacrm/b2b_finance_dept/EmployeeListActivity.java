package com.mrnovacrm.b2b_finance_dept;

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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/20/2018.
 */
public class EmployeeListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txt_nodata;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    String SHORTFROM="";
    String TITLE="";
    private String BRANCHID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);

        Bundle bundle=getIntent().getExtras();
        SHORTFROM= bundle.getString("SHORTFROM");
        TITLE= bundle.getString("TITLE");

        setContentView(R.layout.employee_report_recyclerview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(SHORTFROM.equals("DEALER"))
        {
            setTitle("Dealers List");
        }else {
            setTitle(TITLE);
        }

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
            searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
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
//                                String empId = hashmapList.get(i).get("empid").toLowerCase();
//                                String empName = hashmapList.get(i).get("name").toLowerCase();

                                String empId = hashmapList.get(i).get("empid");
                                String empName = hashmapList.get(i).get("name");


                                if ((empId.toLowerCase()).contains(newText.toLowerCase())) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = hashmapList.get(i).get("id");
                                    String empid = hashmapList.get(i).get("empid");
                                    String name = hashmapList.get(i).get("name");
                                    String role = hashmapList.get(i).get("role");
                                    String address = hashmapList.get(i).get("address");

                                    hashMap.put("id", id);
                                    hashMap.put("empid", empid);
                                    hashMap.put("name", name);
                                    hashMap.put("role", role);
                                    hashMap.put("address", address);

                                    filteremployeehashmapList.add(hashMap);
                                    showOrdersData(filteremployeehashmapList);
                                }else{
                                    if((empName.toLowerCase()).contains((newText.toLowerCase()))){
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        String id = hashmapList.get(i).get("id");
                                        String empid = hashmapList.get(i).get("empid");
                                        String name = hashmapList.get(i).get("name");
                                        String role = hashmapList.get(i).get("role");
                                        String address = hashmapList.get(i).get("address");

                                        hashMap.put("id", id);
                                        hashMap.put("empid", empid);
                                        hashMap.put("name", name);
                                        hashMap.put("role", role);
                                        hashMap.put("address", address);

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
        final TransparentProgressDialog dialog = new TransparentProgressDialog(EmployeeListActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;
        if(SHORTFROM.equals("DEALER"))
        {
            mService = mApiService.getDealersList(BRANCHID);
        }else{
            mService = mApiService.getemployeeList(SHORTFROM);
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
                                    String empid = ordersList.get(i).getEmp_id();
                                    String name = ordersList.get(i).getName();
                                    String address = ordersList.get(i).getAddress();
                                    String role = ordersList.get(i).getRole_name();
                                    hashMap.put("id", id);
                                    hashMap.put("empid", empid);
                                    hashMap.put("name", name);
                                    hashMap.put("role", role);
                                    hashMap.put("address", address);
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
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList) {
        if (hashmapList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), hashmapList);
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
                    .inflate(R.layout.fragment_employeelist_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String id = employeesList.get(position).get("id");
            String empId = employeesList.get(position).get("empid");
            String name = employeesList.get(position).get("name");
            String address = employeesList.get(position).get("address");
            String role = employeesList.get(position).get("role");
            holder.emp_id.setText(empId);
            holder.emp_name.setText(name);
            holder.emp_designation.setText(role);
            holder.emp_location.setText(address);
        }

        @Override
        public int getItemCount() {
            return employeesList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView emp_id, emp_name, emp_designation, emp_location;

            MyViewHolder(View view) {
                super(view);
                emp_id = view.findViewById(R.id.emp_id);
                emp_name = view.findViewById(R.id.emp_name);
                emp_designation = view.findViewById(R.id.emp_designation);
                emp_location = view.findViewById(R.id.emp_location);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), EmployeeDescriptionActivity.class);
//                startActivity(intent);
            }
        }
    }

}
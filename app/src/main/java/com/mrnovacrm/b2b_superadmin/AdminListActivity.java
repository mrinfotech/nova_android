package com.mrnovacrm.b2b_superadmin;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_finance_dept.UpdateFinanceEmployeesActivity;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchesDTO;
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
public class AdminListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txt_nodata;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    String SHORTFROM="";
    private RecyclerViewAdapter adapter;
    Context context;
    public static Activity mainfinish;
    private String BRANCHID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        context=this;
        mainfinish=this;
        Bundle bundle=getIntent().getExtras();
        SHORTFROM= bundle.getString("SHORTFROM");
        setContentView(R.layout.employee_report_recyclerview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(SHORTFROM.equals("ADMIN"))
        {
            setTitle("Admin's List");
        }else  if(SHORTFROM.equals("FM"))
        {
            setTitle("Branch Managers");
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
                                String empId = hashmapList.get(i).get("empid").toLowerCase();
                                String empName = hashmapList.get(i).get("name").toLowerCase();
                                if (empId.contains(newText) || empName.contains(newText)) {
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
        final TransparentProgressDialog dialog = new TransparentProgressDialog(AdminListActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;
        if(SHORTFROM.equals("DEALER"))
        {
            mService = mApiService.getDealersList(BRANCHID);
        }else{
          //  mService = mApiService.getemployeeList(SHORTFROM);
            mService = mApiService.getEmployeesList(SHORTFROM,BRANCHID);
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
        public void onBindViewHolder(MyViewHolder holder, final int position) {
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

            holder.emp_id.setText(empid);
            holder.emp_name.setText(name);
            holder.emp_mobile.setText(mobile);
            holder.emp_location.setText(address);
            holder.emp_branch.setText(branch_name);

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

                   if(SHORTFROM.equals("ADMIN"))
                    {
                        Intent intent=new Intent(getApplicationContext(),UpdateAdminActivity.class);

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
                    }else  if(SHORTFROM.equals("FM"))
                    {
                        Intent intent=new Intent(getApplicationContext(),UpdateFinanceEmployeesActivity.class);
                        intent.putExtra("SHORTFROM",SHORTFROM);
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
                }
            });

            holder.deleteimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = employeesList.get(position).get("id");
                    String name = employeesList.get(position).get("name");
                    confirmPopup(id, position, employeesList, name);
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeesList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView emp_id, emp_name, emp_mobile, emp_location,emp_branch;
            ImageView deleteimg,editimg;

            MyViewHolder(View view) {
                super(view);
                emp_id = view.findViewById(R.id.emp_id);
                emp_name = view.findViewById(R.id.emp_name);
                emp_mobile = view.findViewById(R.id.emp_mobile);
                emp_location = view.findViewById(R.id.emp_location);
                emp_branch = view.findViewById(R.id.emp_branch);
                deleteimg = view.findViewById(R.id.deleteimg);
                editimg = view.findViewById(R.id.editimg);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), EmployeeDescriptionActivity.class);
//                startActivity(intent);
            }


        }
        public void confirmPopup(final String employee_id, final int position,
                                 final ArrayList<HashMap<String, String>> employeesList, String name) {
            //  deleteEmployee(employee_id,position,employeesList);
            android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(AdminListActivity.this)
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
package com.mrnovacrm.b2b_superadmin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchDetailsDTO;
import com.mrnovacrm.model.BranchesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrachesListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txt_nodata;
    private TransparentProgressDialog progress;
    private String openURL;
    private ImageView backbtn;
    String ZONEID;
    GlobalShare globalShare;
    public static Activity mainfinish;
    private RecyclerViewAdapter adapter;
    private AlertDialog alertbox;
    private String USERTYPE;
    private String TITLE;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        setTitle("Branches Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        setContentView(R.layout.layout_brancheslist);
        recyclerView = findViewById(R.id.recyclerView);
        txt_nodata = findViewById(R.id.txt_nodata);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        globalShare = (GlobalShare) getApplicationContext();

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            USERTYPE = values.get(SharedDB.USERTYPE);
        }
        getEmployeeList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getEmployeeList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<BranchesDTO> mService = null;
        mService = mApiService.getBrachesList();
        mService.enqueue(new Callback<BranchesDTO>() {
            @Override
            public void onResponse(Call<BranchesDTO> call, retrofit2.Response<BranchesDTO> response) {
                Log.e("response", "" + response);
                BranchesDTO branchesDTO = response.body();
                dialog.dismiss();
                try {
                    String status = branchesDTO.getStatus();
                    if (status.equals("1")) {
                        List<BranchDetailsDTO> branchDetailsDTOS = branchesDTO.getBranchDetailsDTOS();
                        if (branchDetailsDTOS != null) {
                            if (branchDetailsDTOS.size() > 0) {

                                ArrayList<HashMap<String, String>> hashMapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < branchDetailsDTOS.size(); i++) {
                                    HashMap<String, String> hashmap = new HashMap<>();
                                    String id = branchDetailsDTOS.get(i).getId();
                                    String name = branchDetailsDTOS.get(i).getName();
                                    String email = branchDetailsDTOS.get(i).getEmail();
                                    String mobile = branchDetailsDTOS.get(i).getMobile();
                                    String address = branchDetailsDTOS.get(i).getAddress();
                                    String district_id = branchDetailsDTOS.get(i).getDistrict_id();
                                    String state_id = branchDetailsDTOS.get(i).getState_id();
                                    String country_id = branchDetailsDTOS.get(i).getCountry_id();
                                    String latitude = branchDetailsDTOS.get(i).getLatitude();
                                    String longitude = branchDetailsDTOS.get(i).getLongitude();
                                    String gst = branchDetailsDTOS.get(i).getGst();
                                    String pic = branchDetailsDTOS.get(i).getPic();
                                    String country_nm = branchDetailsDTOS.get(i).getCountry_nm();
                                    String state_nm = branchDetailsDTOS.get(i).getState_nm();
                                    String district_nm = branchDetailsDTOS.get(i).getDistrict_nm();

                                    hashmap.put("id", id);
                                    hashmap.put("name", name);
                                    hashmap.put("email", email);
                                    hashmap.put("mobile", mobile);
                                    hashmap.put("address", address);
                                    hashmap.put("district_id", district_id);
                                    hashmap.put("state_id", state_id);
                                    hashmap.put("country_id", country_id);
                                    hashmap.put("latitude", latitude);
                                    hashmap.put("longitude", longitude);
                                    hashmap.put("gst", gst);
                                    hashmap.put("pic", pic);
                                    hashmap.put("country_nm", country_nm);
                                    hashmap.put("state_nm", state_nm);
                                    hashmap.put("district_nm", district_nm);

                                    hashMapList.add(hashmap);
                                }

                                showEmployeeData(hashMapList);
                            }
                        }
                    } else {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    public void showEmployeeData(ArrayList<HashMap<String, String>> hashmapList) {
        adapter = new RecyclerViewAdapter(getApplicationContext(), hashmapList);
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context context;
        ArrayList<HashMap<String, String>> employeesList = new ArrayList<HashMap<String, String>>();

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
            this.context = mContext;
            employeesList = hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_branchesadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            String id = employeesList.get(position).get("id");
            String name = employeesList.get(position).get("name");
            String email = employeesList.get(position).get("email");
            String mobile = employeesList.get(position).get("mobile");
            String address = employeesList.get(position).get("address");
            String gst = employeesList.get(position).get("gst");
            String state_nm = employeesList.get(position).get("state_nm");
            String district_nm = employeesList.get(position).get("district_nm");
            String country_nm = employeesList.get(position).get("country_nm");

            holder.emp_name.setText(name);
            holder.emp_mobile.setText(mobile);
            holder.emp_email.setText(email);
            holder.gstid.setText(gst);
            holder.addresstext.setText(address);
            holder.emp_state.setText(state_nm);
            holder.emp_dist.setText(district_nm);

            holder.editimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id = employeesList.get(position).get("id");
                    String name = employeesList.get(position).get("name");
                    String email = employeesList.get(position).get("email");
                    String mobile = employeesList.get(position).get("mobile");
                    String address = employeesList.get(position).get("address");
                    String gst = employeesList.get(position).get("gst");
                    String state_nm = employeesList.get(position).get("state_nm");
                    String district_nm = employeesList.get(position).get("district_nm");
                    String country_nm = employeesList.get(position).get("country_nm");

                    String district_id = employeesList.get(position).get("district_id");
                    String state_id = employeesList.get(position).get("state_id");
                    String country_id = employeesList.get(position).get("country_id");

                    Intent intent = new Intent(getApplicationContext(), ManageBranchActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("address", address);
                    intent.putExtra("gst", gst);
                    intent.putExtra("state_nm", state_nm);
                    intent.putExtra("district_nm", district_nm);
                    intent.putExtra("country_nm", country_nm);

                    intent.putExtra("district_id", district_id);
                    intent.putExtra("state_id", state_id);
                    intent.putExtra("country_id", country_id);

                    startActivity(intent);


                    //                    ArrayList<HashMap<String, String>> circlesList = circleListHashMapList.get(position).get(uniq_id);
//                    globalShare.setCirclesList(circlesList);
//
//                    ArrayList<HashMap<String, String>> zonesList = zoneListHashMapList.get(position).get(uniq_id);
//                    globalShare.setZonesList(zonesList);
//
//                    if(TITLE.equals("Operational Incharge List")) {
//                        Intent intent = new Intent(getApplicationContext(), UpdateOperationalInchargeActivity.class);
//                        intent.putExtra("uniq_id", uniq_id);
//                        intent.putExtra("empname", name);
//                        intent.putExtra("employee_id", employee_id);
//                        intent.putExtra("emp_mobile", emp_mobile);
//                        intent.putExtra("emp_email", emp_email);
//                        intent.putExtra("title", TITLE);
//                        startActivity(intent);
//                    }else{
//                        String zone_id = employeesList.get(position).get("zone_id");
//                        Intent intent = new Intent(getApplicationContext(), UpdateAMOHActivity.class);
//                        intent.putExtra("uniq_id", uniq_id);
//                        intent.putExtra("empname", name);
//                        intent.putExtra("employee_id", employee_id);
//                        intent.putExtra("emp_mobile", emp_mobile);
//                        intent.putExtra("emp_email", emp_email);
//                        intent.putExtra("title", TITLE);
//                        intent.putExtra("zone_id", zone_id);
//                        startActivity(intent);
//                    }
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
            TextView emp_name, emp_mobile, emp_email, emp_dist, gstid, circleheadtxt, emp_state,
                    zoneheadtxt, addresstext;
            ImageView deleteimg, editimg;
            LinearLayout zonelinear, circlelinear;

            MyViewHolder(View view) {
                super(view);
                emp_name = view.findViewById(R.id.emp_name);
                emp_mobile = view.findViewById(R.id.emp_mobile);
                emp_email = view.findViewById(R.id.emp_email);

                deleteimg = view.findViewById(R.id.deleteimg);
                editimg = view.findViewById(R.id.editimg);
                gstid = view.findViewById(R.id.gstid);

                zonelinear = view.findViewById(R.id.zonelinear);
                zoneheadtxt = view.findViewById(R.id.zoneheadtxt);
                emp_state = view.findViewById(R.id.emp_state);

                circlelinear = view.findViewById(R.id.circlelinear);
                circleheadtxt = view.findViewById(R.id.circleheadtxt);
                emp_dist = view.findViewById(R.id.emp_dist);
                addresstext = view.findViewById(R.id.addresstext);

                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }

        public void confirmPopup(final String employee_id, final int position,
                                 final ArrayList<HashMap<String, String>> employeesList, String name) {
            //  deleteEmployee(employee_id,position,employeesList);
            android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(BrachesListActivity.this)
                    .setMessage("Do you want to delete Branch " + name + "?")
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
            final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<BranchesDTO> mService = mApiService.deleteBranch(empip);
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
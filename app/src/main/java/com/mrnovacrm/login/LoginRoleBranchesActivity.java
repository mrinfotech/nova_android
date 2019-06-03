package com.mrnovacrm.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.RolesModelDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRoleBranchesActivity extends AppCompatActivity{

    Context context;
    private GlobalShare globalShare;
    public static Activity mainfinish;
    RecyclerView recyclerView;
    String PRIMARYID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mainfinish = this;
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_rolebrach);

        View includedLayout = findViewById(R.id.include_actionbar);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        backimg.setVisibility(View.GONE);
        actionbarheadertxt.setText("Branches");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        globalShare = (GlobalShare) getApplicationContext();

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        loadBranchesData();
    }

    public void loadBranchesData() {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.getEmpRolesList(PRIMARYID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("Response", " :" + response);
                Login mLoginObject = response.body();
                Log.e("mLoginObject", " :" + mLoginObject);
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    Log.e("Status", " :" + status);
                    String message = mLoginObject.getMessage();
                    if (status.equals("1")) {
                        try{

                            String role_count=mLoginObject.getRole_count();
                            String branch_count=mLoginObject.getBranch_count();

                            List<RolesModelDTO> rolesDTOLis=mLoginObject.getRolesDTOLis();
                            if(rolesDTOLis!=null)
                            {
                                if(rolesDTOLis.size()>0)
                                {
                                    loadData(rolesDTOLis,role_count,branch_count);
                                }
                            }else{
                               // globalShare.setRolesList(rolesDTOLis);
                            }
                        }catch (Exception e)
                        {
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
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

    public void loadData(List<RolesModelDTO> rolesList,String rolecount,String branchcount) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, rolesList,rolecount,branchcount);
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<String> menuNameList;
        List<RolesModelDTO> hashMapList;
        String rolecountval;
        String branchcountval;

        public RecyclerViewAdapter(Context mContext, List<RolesModelDTO> hashMapList,String rolecountval,String branchcountval) {
            this.mContext = mContext;
            this.hashMapList = hashMapList;
            this.rolecountval=rolecountval;
            this.branchcountval=branchcountval;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rolebranchadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            String id = hashMapList.get(position).getId();
            String company_id = hashMapList.get(position).getCompany_id();
            String company = hashMapList.get(position).getCompany();
            String branch_id = hashMapList.get(position).getBranch_id();
            String branch_name = hashMapList.get(position).getBranch_name();
            String short_form = hashMapList.get(position).getShort_form();
            String role_id = hashMapList.get(position).getRole_id();
            String role_name = hashMapList.get(position).getRole_name();

            holder.text_count.setText(branch_name);
            holder.text_company.setText(company);
            holder.dashimag.setImageResource(R.drawable.menu_managebranch);
            try {
                holder.linearcardview.setBackgroundColor(getResources().getColor(R.color.appbackground));
            } catch (Exception e) {
            }
            holder.text_role.setText(role_name);
        }

        @Override
        public int getItemCount() {
            return hashMapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView text_count,text_company,text_role;
            LinearLayout linearcardview;
            ImageView dashimag;

            MyViewHolder(View view) {
                super(view);
                linearcardview = view.findViewById(R.id.linearcardview);
                text_count = view.findViewById(R.id.text_count);
                text_role = view.findViewById(R.id.text_role);
                text_company = view.findViewById(R.id.text_company);
                dashimag = view.findViewById(R.id.dashimag);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                String id = hashMapList.get(pos).getId();
                String company_id = hashMapList.get(pos).getCompany_id();
                String company = hashMapList.get(pos).getCompany();
                String branch_id = hashMapList.get(pos).getBranch_id();
                String branch_name = hashMapList.get(pos).getBranch_name();
                String short_form = hashMapList.get(pos).getShort_form();
                String role_id = hashMapList.get(pos).getRole_id();
                String role_name = hashMapList.get(pos).getRole_name();
                String branch_contact = hashMapList.get(pos).getBranch_contact();

                HashMap<String, String>  values = SharedDB.getUserDetails(getApplicationContext());

                String ADDRESSID = values.get(SharedDB.ADDRESSID);
               // String USERTYPE = values.get(SharedDB.USERTYPE);
               // String SHORTFORM = values.get(SharedDB.SHORTFORM);
                String USERNAME = values.get(SharedDB.USERNAME);
                String MOBILE = values.get(SharedDB.MOBILE);
                String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                //String COMPANY = values.get(SharedDB.COMPANY);
                String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
               // String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                //String BRANCHID = values.get(SharedDB.BRANCHID);
                String PINCODE = values.get(SharedDB.PINCODE);
                String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
               // String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
               // String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                String IMAGEURL = values.get(SharedDB.IMAGEURL);

                double lat = 0.0;
                double lon = 0.0;

                SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, role_name, "",
                        "", lat, lon, PRIMARYID, PINCODE, USERNAME, IMAGEURL, ADDRESSID, branch_id,
                        branch_name, short_form,company_id,branch_contact,COMPANIESLIST,
                        branchcountval,rolecountval,id);

                SharedPreferences multisharedpreference = getSharedPreferences(
                        SharedDB.MULTIPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = multisharedpreference.edit();
                editor.clear();
                editor.commit();
                SharedDB.clearMultiroleAuthentication(LoginRoleBranchesActivity.this);
                globalShare.setLoginselectedfromval("multiplescreen");

                if (short_form.equals("ADMIN")) {
                    globalShare.setAdminmenuselectpos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            AdminMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("FM")) {
                    globalShare.setFinancemenuselectpos("1");
                    Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("SE")) {

                    globalShare.setDealerMenuSelectedPos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            DealerMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("DB")) {
                    globalShare.setDeliverymenuselectpos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            DeliveryMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("PACKER")) {
                    globalShare.setDispatchmenuselectpos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            DispatchMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("DEALER")) {
                    globalShare.setDealerMenuSelectedPos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            DealerMenuScreenActivity.class);
                    startActivity(intent);
                } else if (short_form.equals("SA")) {
                    globalShare.setSuperadminmenuselectpos("1");
                    Intent intent = new Intent(getApplicationContext(),
                            SuperAdminMenuScrenActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }
    }
}
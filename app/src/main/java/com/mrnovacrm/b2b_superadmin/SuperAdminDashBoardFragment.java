package com.mrnovacrm.b2b_superadmin;

//public class SuperAdminDashBoardFragment {
//}


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class SuperAdminDashBoardFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> menuNameList = new ArrayList<String>();
    ArrayList<String> menuColorList = new ArrayList<String>();
    private TransparentProgressDialog progress;
    private String openURL = "";
    private int newsfeedstatus;
    int Pending, Open, Escalated, In_Progress, Closed, Not_Related, Reopen, Other_Department;
    String message;
    GlobalShare globalShare;
    Context mContext;
    ArrayList<String> branchNamesList = new ArrayList<>();
    ArrayList<String> branchIdsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dashboard, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mContext = getActivity();
        globalShare = (GlobalShare) getActivity().getApplicationContext();

        menuNameList.clear();
        menuColorList.clear();

        menuNameList.add("Vizianagram");
        menuNameList.add("Vizag");
        menuNameList.add("Srikakulam");
        menuNameList.add("Vijayawada");
        menuNameList.add("Hyderabad");

        menuColorList.add("#5867c4");
        menuColorList.add("#00c4dc");
        menuColorList.add("#a146d1");
        menuColorList.add("#b21878");
        menuColorList.add("#f9a20c");

        int[] menuicons = {R.drawable.menu_orders,
                R.drawable.menu_orders, R.drawable.menu_orders, R.drawable.menu_orders, R.drawable.menu_orders
        };

        loadBranchesData();
        return rootView;
    }

    public void loadBranchesData() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<BranchesDTO> mService = null;
        mService = mApiService.getBraches();
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

                                branchNamesList.clear();
                                branchNamesList.add("Select Branch");
                                branchIdsList.clear();
                                ArrayList<HashMap<String, String>> hashMapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < branchDetailsDTOS.size(); i++) {
                                    HashMap<String, String> hashmap = new HashMap<>();
                                    String id = branchDetailsDTOS.get(i).getId();
                                    String name = branchDetailsDTOS.get(i).getName();
                                    String color = branchDetailsDTOS.get(i).getColor();
                                    String company = branchDetailsDTOS.get(i).getCompany();

                                    hashmap.put("id", id);
                                    hashmap.put("name", name);
                                    hashmap.put("color", color);
                                    hashmap.put("company", company);

                                    branchNamesList.add(name);
                                    branchIdsList.add(id);

                                    hashMapList.add(hashmap);
                                }
                                loadData(hashMapList);
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
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(ArrayList<HashMap<String, String>> hashMapList) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), hashMapList);
        recyclerView.setAdapter(adapter);
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<String> menuNameList;


        ArrayList<HashMap<String, String>> hashMapList;

        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashMapList) {
            this.mContext = mContext;
            this.hashMapList = hashMapList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_superadmindashboardadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            String id = hashMapList.get(position).get("id");
            String branchname = hashMapList.get(position).get("name");
            String color = hashMapList.get(position).get("color");
            String company = hashMapList.get(position).get("company");

            holder.text_count.setText(branchname);
            holder.text_company.setText(company);
            holder.dashimag.setImageResource(R.drawable.menu_orders);
            try {
                holder.linearcardview.setBackgroundColor(Color.parseColor(color));
            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            return hashMapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView text_count,text_company;
            LinearLayout linearcardview;
            ImageView dashimag;

            MyViewHolder(View view) {
                super(view);
                linearcardview = view.findViewById(R.id.linearcardview);
                text_count = view.findViewById(R.id.text_count);
                text_company = view.findViewById(R.id.text_company);
                dashimag = view.findViewById(R.id.dashimag);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                String id = hashMapList.get(pos).get("id");
                String branchname = hashMapList.get(pos).get("name");

                Intent intent = new Intent(getActivity(), SuperAdminDashBoardActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("branchname",branchname);
                startActivity(intent);


//                if (Status.equals("Orders")) {
//                    Intent intent=new Intent(getActivity(),AdminOrdersTabActivity.class);
//                    startActivity(intent);
//                }else if (Status.equals("Payments")) {
//                }else if (Status.equals("Dispatchers")) {
//                    Intent intent=new Intent(getActivity(),EmployeeListActivity.class);
//                    intent.putExtra("title",Status);
//                    startActivity(intent);
//                }else if (Status.equals("Delivery Boys")) {
//                    Intent intent=new Intent(getActivity(),EmployeeListActivity.class);
//                    intent.putExtra("title",Status);
//                    startActivity(intent);
//                }else if (Status.equals("Sales Executives")) {
//                    Intent intent=new Intent(getActivity(),EmployeeListActivity.class);
//                    intent.putExtra("title",Status);
//                    startActivity(intent);
//                }
            }
        }
    }
}

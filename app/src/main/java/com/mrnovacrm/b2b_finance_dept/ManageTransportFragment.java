package com.mrnovacrm.b2b_finance_dept;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchesDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.TransportDTO;
import com.mrnovacrm.model.TransportList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTransportFragment extends Fragment implements View.OnClickListener {

    EditText addtransporttxt;
    Button submit_btn;
    TextView transporttxt;
    ScrollView scrollview;
    RecyclerView recyclerView;
    Context mContext;
    private String PRIMARYID;
    private String BRANCHID;
    private RecyclerViewAdapter adapter;
    String TRANSPORT_ID="";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      //   View rootView = inflater.inflate(R.layout.layout_managetransport, container, false);
         View rootView = inflater.inflate(R.layout.layout_manage_transport, container, false);
        mContext=getActivity();
        addtransporttxt=rootView.findViewById(R.id.addtransporttxt);
        submit_btn=rootView.findViewById(R.id.submit_btn);
        transporttxt=rootView.findViewById(R.id.transporttxt);
        scrollview=rootView.findViewById(R.id.scrollview);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            BRANCHID=values.get(SharedDB.BRANCHID);
        }


        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            try{
                getTrasportsList();
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        submit_btn.setOnClickListener(this);
         return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_btn:
                String transportName=addtransporttxt.getText().toString().trim();
                if(transportName.equals(""))
                {
                    Toast.makeText(getActivity(),"Please enter transport name",Toast.LENGTH_SHORT).show();
                }else {
                    submitrasport(transportName);
                }
                break;
            default:
                break;
        }
    }

    public void submitrasport(String transportName)
    {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService=null;
        if(TRANSPORT_ID.equals(""))
        {
            mService = mApiService.addTransport(transportName,BRANCHID,PRIMARYID);
        }else{
            mService = mApiService.updateTransport(transportName,BRANCHID,PRIMARYID,TRANSPORT_ID);
        }
       // Call<Login> mService = mApiService.addTransport(transportName,BRANCHID,PRIMARYID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        TRANSPORT_ID="";
                        addtransporttxt.setText("");
                        getTrasportsList();
                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTrasportsList()
    {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<TransportDTO> mService = mApiService.getTransportsList(BRANCHID);
        mService.enqueue(new Callback<TransportDTO>() {
            @Override
            public void onResponse(Call<TransportDTO> call, Response<TransportDTO> response) {
                Log.e("response", "" + response);
                TransportDTO mtransportObject = response.body();
                dialog.dismiss();
                try {
                    String status = mtransportObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {

                        List<TransportList> transportDTOLIST = mtransportObject.getTransportDTOS();
                        if(transportDTOLIST!=null)
                        {
                            if(transportDTOLIST.size()>0)
                            {
                                ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<>();
                                for(int i=0;i<transportDTOLIST.size();i++)
                                {
                                    HashMap<String,String> hashMap=new HashMap<>();
                                    String id=transportDTOLIST.get(i).getId();
                                    String name=transportDTOLIST.get(i).getName();
                                    hashMap.put("id",id);
                                    hashMap.put("name",name);
                                    hashMapArrayList.add(hashMap);
                                }
                                getTransportNames(hashMapArrayList);
                            }
                        }
                    } else {
                        String message = mtransportObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TransportDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTransportNames(ArrayList<HashMap<String,String>> hashMapArrayList)
    {
        adapter = new RecyclerViewAdapter(getActivity(), hashMapArrayList);
        recyclerView.setAdapter(adapter);
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
                    .inflate(R.layout.layout_transport_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {

            String id = employeesList.get(position).get("id");
            String name = employeesList.get(position).get("name");
////            holder.emp_id.setText(id);
//            holder.emp_name.setText(name);
//
            holder.transport_name.setText(name);
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
                    TRANSPORT_ID=id;
                    addtransporttxt.setText(name);
                }
            });
        }

        @Override
        public int getItemCount() {
            return employeesList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView transport_name;
            ImageView deleteimg,editimg;

            MyViewHolder(View view) {
                super(view);
                transport_name = view.findViewById(R.id.transport_name);
                deleteimg = view.findViewById(R.id.deleteimg);
                editimg = view.findViewById(R.id.editimg);
                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
            }
        }
        public void confirmPopup(final String transport_id, final int position,
                                 final ArrayList<HashMap<String, String>> employeesList, String name) {
            //  deleteEmployee(employee_id,position,employeesList);
            android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setMessage("Do you want to delete ?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    deleteTransport(transport_id, position, employeesList);
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).show();
        }

        public void deleteTransport(String transportid, final int position, final ArrayList<HashMap<String, String>> employeesList) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<BranchesDTO> mService = mApiService.deleteTransport(BRANCHID,PRIMARYID,transportid);
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            employeesList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

        public void updateTransport(String transportid, final int position, final ArrayList<HashMap<String, String>> employeesList) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<BranchesDTO> mService = mApiService.deleteTransport(BRANCHID,PRIMARYID,transportid);
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            employeesList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    }
}
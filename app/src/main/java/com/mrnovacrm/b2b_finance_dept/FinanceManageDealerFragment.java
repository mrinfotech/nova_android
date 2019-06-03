package com.mrnovacrm.b2b_finance_dept;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;
import com.mrnovacrm.model.StoresDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by prasad on 3/7/2018.
 */

public class FinanceManageDealerFragment extends Fragment {
    RecyclerView managestoresrecyclerview;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String BRANCHID = "";
    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private ImageView closeicon;
    EditText reason_edtxt;
    String reason;
    StoresListAdapter adapter;
    RelativeLayout storesbtmrel;
    RelativeLayout imgrel;
    ImageView imageview;
    GlobalShare globalShare;
    private ArrayList<HashMap<String, String>> filteremployeehashmapList;
    ArrayList<HashMap<String, String>> hashmapsList = new ArrayList<HashMap<String, String>>();
    public FinanceManageDealerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_managestores, container, false);
        managestoresrecyclerview = rootView.findViewById(R.id.managestoresrecyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        managestoresrecyclerview.setHasFixedSize(true);
        managestoresrecyclerview.setLayoutManager(mLayoutManager);
        //txt_nodata = rootView.findViewById(R.id.txt_nodata);

        imgrel=rootView.findViewById(R.id.imgrel);
        imageview=rootView.findViewById(R.id.imageview);
        globalShare=(GlobalShare)getActivity().getApplicationContext();

        storesbtmrel = rootView.findViewById(R.id.storesbtmrel);

        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        setHasOptionsMenu(true);
//        if(globalShare.getDealershashmapList()!=null)
//        {
//            if(globalShare.getDealershashmapList().size()>0)
//            {
//                globalShare.getDealershashmapList();
//                showData(globalShare.getDealershashmapList());
//            }else{
//                getDealersList("");
//            }
//        }else{
            getDealersList("");
        //}

        return rootView;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.employee_search, menu);
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
            searchPlateView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
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

                    if (hashmapsList != null) {
                        if (hashmapsList.size() > 0) {
                            for (int i = 0; i < hashmapsList.size(); i++) {

                                String empId = hashmapsList.get(i).get("empid");
                                String empName = hashmapsList.get(i).get("name");

                                if ((empId.toLowerCase()).contains(newText.toLowerCase())) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = hashmapsList.get(i).get("id");
                                    String name = hashmapsList.get(i).get("name");
                                    String empid = hashmapsList.get(i).get("empid");
                                    String status1 = hashmapsList.get(i).get("status");

                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
                                    hashMap.put("empid", empid);
                                    hashMap.put("status", status1);
                                    hashmapsList.add(hashMap);

                                    filteremployeehashmapList.add(hashMap);
                                    showData(filteremployeehashmapList);
                                }else{
                                    if((empName.toLowerCase()).contains((newText.toLowerCase()))){
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        String id = hashmapsList.get(i).get("id");
                                        String name = hashmapsList.get(i).get("name");
                                        String empid = hashmapsList.get(i).get("empid");
                                        String status1 = hashmapsList.get(i).get("status");

                                        hashMap.put("id", id);
                                        hashMap.put("name", name);
                                        hashMap.put("empid", empid);
                                        hashMap.put("status", status1);
                                        hashmapsList.add(hashMap);

                                        filteremployeehashmapList.add(hashMap);
                                        showData(filteremployeehashmapList);
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager)getActivity().getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void getDealersList(final String fromval) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;
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

                                ArrayList<HashMap<String, String>> hashmapList=new ArrayList<>();
                                String[] items = new String[hashmapList.size()];
                                String[] itemsstatus = new String[hashmapList.size()];
                                String[] itemid = new String[hashmapList.size()];


                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String name = ordersList.get(i).getName();
//                                    String first_name = ordersList.get(i).getFirst_name();
//                                    String last_name = ordersList.get(i).getLast_name();
                                    String empid = ordersList.get(i).getEmp_id();
//                                    String branch = ordersList.get(i).getBranch();
//                                    String mobile = ordersList.get(i).getMobile();
//                                    String email = ordersList.get(i).getEmail();
//                                    String dob = ordersList.get(i).getDob();
//                                    String address = ordersList.get(i).getAddress();
//                                    String role_name = ordersList.get(i).getRole_name();
//                                    String branch_name = ordersList.get(i).getBranch_name();
                                    String status1 = ordersList.get(i).getStatus();

                                    hashMap.put("id", id);
                                    hashMap.put("name", name);
//                                    hashMap.put("first_name", first_name);
//                                    hashMap.put("last_name", last_name);
                                    hashMap.put("empid", empid);
//                                    hashMap.put("branch", branch);
//                                    hashMap.put("mobile", mobile);
//                                    hashMap.put("email", email);
//                                    hashMap.put("dob", dob);
//                                    hashMap.put("address", address);
//                                    hashMap.put("role_name", role_name);
//                                    hashMap.put("branch_name", branch_name);
                                    hashMap.put("status", status1);
//                                    items[i]=name;
//                                    itemsstatus[i]=status1;
//                                    itemid[i]=empid;

                                    hashmapsList.add(hashMap);

                                    hashmapList.add(hashMap);
                                }
                                showData(hashmapList);
                            } else {
                                managestoresrecyclerview.setVisibility(View.GONE);
                                storesbtmrel.setVisibility(View.GONE);
                                imgrel.setVisibility(View.VISIBLE);
                                imageview.setImageResource(R.drawable.nostoresfound);
                            }
                        }
                    } else {
                        managestoresrecyclerview.setVisibility(View.GONE);
                        storesbtmrel.setVisibility(View.GONE);
                        imgrel.setVisibility(View.VISIBLE);
                        imageview.setImageResource(R.drawable.nostoresfound);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showData(ArrayList<HashMap<String, String>> storeshashmapList) {
        if (storeshashmapList.size() > 0) {

            managestoresrecyclerview.setVisibility(View.VISIBLE);
            adapter = new StoresListAdapter(getActivity(), storeshashmapList);
            managestoresrecyclerview.setAdapter(adapter);
            imgrel.setVisibility(View.GONE);
            storesbtmrel.setVisibility(View.VISIBLE);
        } else {
            managestoresrecyclerview.setVisibility(View.GONE);
            imgrel.setVisibility(View.VISIBLE);
            imageview.setImageResource(R.drawable.nostoresfound);
            storesbtmrel.setVisibility(View.GONE);
        }
    }

    public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<HashMap<String, String>> storeshashmapList;

        public StoresListAdapter(Context mContext, ArrayList<HashMap<String, String>> storeshashmapList) {
            this.mContext = mContext;
            this.storeshashmapList = storeshashmapList;
        }

        @Override
        public StoresListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_managestoresadapter, parent, false);
            return new StoresListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final StoresListAdapter.MyViewHolder holder, final int position) {
            //hashMap.put("empid", empid);
            try{
                String storeName="";
                String empid="";
                String status="";
                storeName = storeshashmapList.get(position).get("name");
                empid = storeshashmapList.get(position).get("empid");
                status = storeshashmapList.get(position).get("status");

                String dealer_Name=storeName+" / "+empid;
                dealer_Name=dealer_Name.trim();
                char first = dealer_Name.charAt(0);
                String firstchar=String.valueOf(first);
                if(firstchar.equals("/"))
                {
                    holder.storenametxt.setText(empid);
                }else{
                    holder.storenametxt.setText(storeName+" / "+empid);
                }

//             if(itemsstatus[holder.indexRefrence].equals("1"))
//             {
//                 holder.switchcompatid.setChecked(true);
//             }else{
//                 holder.switchcompatid.setChecked(false);
//             }

//            if (status.equals("1")) {
//                holder.switchcompatid.setChecked(true);
//            } else {
//                holder.switchcompatid.setChecked(false);
//               }
//                boolean isConnectedToInternet = CheckNetWork
//                        .isConnectedToInternet(getActivity());
//                if (isConnectedToInternet) {
//                    holder.switchcompatid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            String storeid = storeshashmapList.get(position).get("id");
//                            if (isChecked == true) {
//                                updateStatusWithRetrofit(storeid, "1", "",
//                                        holder,storeshashmapList,position);
//                            } else {
//
//                                showCancelOrderPopup(position, 0,holder,storeshashmapList);
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(getActivity(), R.string.networkerror,
//                            Toast.LENGTH_SHORT).show();
//
//                }
            }catch (Exception e)
            {
            }
        }

        public void showCancelOrderPopup(final int position, final int status, final MyViewHolder holder,
                                         final ArrayList<HashMap<String, String>> storeshashmapList) {
            /** Used for Show Disclaimer Pop up screen */
            alertDialog = new Dialog(getActivity());
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
            LayoutInflater inflater = getActivity().getLayoutInflater();
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
                    getDealersList("cancel");
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
                    updateStatusWithRetrofit(storeid, String.valueOf(status), reason,holder,storeshashmapList,position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return storeshashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView storenametxt;
            //   SwitchCompat switchcompatid;
            int indexRefrence;

            MyViewHolder(View view) {
                super(view);

                storenametxt = view.findViewById(R.id.storenametxt);
                //   switchcompatid = view.findViewById(R.id.switchcompatid);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), StoreOrderDetailsActivity.class);
//                String storeid = storeshashmapList.get(getAdapterPosition()).get("id");
//                intent.putExtra("store", storeid);
//                startActivity(intent);
            }
        }

        private void updateStatusWithRetrofit(String storeid, final String storestatus, String reason,
                                              final MyViewHolder holder,
                                              final  ArrayList<HashMap<String, String>> storeshashmapList,
                                              final int pos
        ) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<StoresDTO> mService = mApiService.updateStoreStatus(PRIMARYID, storeid, storestatus, reason);
            mService.enqueue(new Callback<StoresDTO>() {
                @Override
                public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
                    StoresDTO mLoginObject = response.body();
                    dialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        String status = mLoginObject.getStatus();
                        Log.e("status",status);

                        if (status.equals("1")) {
                            String message = mLoginObject.getMessage();

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                                //  holder.switchcompatid.setChecked(false);
                            }
                            storeshashmapList.remove(pos);
                            adapter.notifyDataSetChanged();

                            //    getDealersList("cancel");
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            if (storestatus.equals("0")) {
                                storeshashmapList.remove(holder.indexRefrence);
                                adapter.notifyDataSetChanged();
                                getDealersList("cancel");

//                                globalShare.setFinancemenuselectpos("5");
//
//                                if(FinanceDeptMenuScreenActivity.mainfinish!=null)
//                                {
//                                    FinanceDeptMenuScreenActivity.mainfinish.finish();
//                                }
//                                Intent intent=new Intent(getActivity(),FinanceDeptMenuScreenActivity.class);
//                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onFailure(Call<StoresDTO> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.EmployeeDTO;
//import com.nova.model.EmployeeListDTO;
//import com.nova.model.StoresDTO;
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
// * Created by prasad on 3/7/2018.
// */
//
//public class FinanceManageDealerFragment extends Fragment {
//    RecyclerView managestoresrecyclerview;
//    private HashMap<String, String> values;
//    private String PRIMARYID = "";
//    private String USERTYPE = "";
//    private String BRANCHID = "";
//    private Dialog alertDialog;
//    private View layout;
//    private TextView submittxt;
//    private ImageView closeicon;
//    EditText reason_edtxt;
//    String reason;
//    StoresListAdapter adapter;
//    RelativeLayout storesbtmrel;
//    RelativeLayout imgrel;
//    ImageView imageview;
//    GlobalShare globalShare;
//
//    public FinanceManageDealerFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.layout_managestores, container, false);
//        managestoresrecyclerview = rootView.findViewById(R.id.managestoresrecyclerview);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        managestoresrecyclerview.setHasFixedSize(true);
//        managestoresrecyclerview.setLayoutManager(mLayoutManager);
//        //txt_nodata = rootView.findViewById(R.id.txt_nodata);
//
//        imgrel=rootView.findViewById(R.id.imgrel);
//        imageview=rootView.findViewById(R.id.imageview);
//        globalShare=(GlobalShare)getActivity().getApplicationContext();
//
//        storesbtmrel = rootView.findViewById(R.id.storesbtmrel);
//
//        if (SharedDB.isLoggedIn(getActivity())) {
//            values = SharedDB.getUserDetails(getActivity());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            USERTYPE = values.get(SharedDB.USERTYPE);
//            BRANCHID = values.get(SharedDB.BRANCHID);
//        }
//        getDealersList("");
//        return rootView;
//    }
//
//
////    public void getStoresList() {
////        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
////        dialog.show();
////        RetrofitAPI mApiService = SharedDB.getInterfaceService();
////        Call<StoresDTO> mService = mApiService.storesList("list");
////        mService.enqueue(new Callback<StoresDTO>() {
////            @Override
////            public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
////                StoresDTO mstoresObject = response.body();
////                dialog.dismiss();
////                try {
////                    String status = mstoresObject.getStatus();
////                    Log.e("status", "" + status);
////                    if (status.equals("1")) {
////
////                        List<StoresDTOList> storesDTOList = mstoresObject.getStoresDTOList();
////                        if (storesDTOList != null) {
////                            if (storesDTOList.size() > 0) {
////                                storesbtmrel.setVisibility(View.VISIBLE);
////                                imgrel.setVisibility(View.GONE);
////
////                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
////                                for (int i = 0; i < storesDTOList.size(); i++) {
////                                    HashMap<String, String> hashMap = new HashMap<String, String>();
////                                    String id = storesDTOList.get(i).getId();
////                                    String name = storesDTOList.get(i).getName();
////                                    String store_status = storesDTOList.get(i).getStore_status();
////                                    hashMap.put("id", id);
////                                    hashMap.put("name", name);
////                                    hashMap.put("store_status", store_status);
////                                    hashmapList.add(hashMap);
////                                }
////                                Log.e("hashmapList", "" + hashmapList);
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
////            public void onFailure(Call<StoresDTO> call, Throwable t) {
////                call.cancel();
////                dialog.dismiss();
////                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
//
//    private void getDealersList(final String fromval) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<EmployeeDTO> mService=null;
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
//                                ArrayList<HashMap<String, String>> hashmapList=new ArrayList<>();
//                                String[] items = new String[hashmapList.size()];
//                                String[] itemsstatus = new String[hashmapList.size()];
//                                String[] itemid = new String[hashmapList.size()];
//
//
//                                for (int i = 0; i < ordersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String id = ordersList.get(i).getId();
//                                    String name = ordersList.get(i).getName();
////                                    String first_name = ordersList.get(i).getFirst_name();
////                                    String last_name = ordersList.get(i).getLast_name();
//                                    String empid = ordersList.get(i).getEmp_id();
////                                    String branch = ordersList.get(i).getBranch();
////                                    String mobile = ordersList.get(i).getMobile();
////                                    String email = ordersList.get(i).getEmail();
////                                    String dob = ordersList.get(i).getDob();
////                                    String address = ordersList.get(i).getAddress();
////                                    String role_name = ordersList.get(i).getRole_name();
////                                    String branch_name = ordersList.get(i).getBranch_name();
//                                    String status1 = ordersList.get(i).getStatus();
//
//                                    hashMap.put("id", id);
//                                    hashMap.put("name", name);
////                                    hashMap.put("first_name", first_name);
////                                    hashMap.put("last_name", last_name);
//                                    hashMap.put("empid", empid);
////                                    hashMap.put("branch", branch);
////                                    hashMap.put("mobile", mobile);
////                                    hashMap.put("email", email);
////                                    hashMap.put("dob", dob);
////                                    hashMap.put("address", address);
////                                    hashMap.put("role_name", role_name);
////                                    hashMap.put("branch_name", branch_name);
//                                    hashMap.put("status", status1);
//
////                                    items[i]=name;
////                                    itemsstatus[i]=status1;
////                                    itemid[i]=empid;
//
//                                    hashmapList.add(hashMap);
//                                }
//                                showData(hashmapList,fromval,items,itemsstatus,itemid);
//                            } else {
//                                managestoresrecyclerview.setVisibility(View.GONE);
//                                storesbtmrel.setVisibility(View.GONE);
//                                imgrel.setVisibility(View.VISIBLE);
//                                imageview.setImageResource(R.drawable.nostoresfound);
//                            }
//                        }
//                    } else {
//                        managestoresrecyclerview.setVisibility(View.GONE);
//                                storesbtmrel.setVisibility(View.GONE);
//                                imgrel.setVisibility(View.VISIBLE);
//                                imageview.setImageResource(R.drawable.nostoresfound);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showData(ArrayList<HashMap<String, String>> storeshashmapList,String fromval,
//                         String[] items,String[] itemsstatus,String[] itemid) {
//        if (storeshashmapList.size() > 0) {
//
//            managestoresrecyclerview.setVisibility(View.VISIBLE);
//            adapter = new StoresListAdapter(getActivity(), storeshashmapList,items,itemsstatus,itemid);
//            managestoresrecyclerview.setAdapter(adapter);
//            imgrel.setVisibility(View.GONE);
//            storesbtmrel.setVisibility(View.VISIBLE);
//        } else {
//            managestoresrecyclerview.setVisibility(View.GONE);
//            imgrel.setVisibility(View.VISIBLE);
//            imageview.setImageResource(R.drawable.nostoresfound);
//            storesbtmrel.setVisibility(View.GONE);
//        }
//    }
//
//    public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> storeshashmapList;
//        String[] items;
//        String[] itemsstatus;
//        String[] itemid;
//
//        public StoresListAdapter(Context mContext, ArrayList<HashMap<String, String>> storeshashmapList,
//                                 String[] items,String[] itemsstatus,String[] itemid) {
//            this.mContext = mContext;
//            this.storeshashmapList = storeshashmapList;
//            this.items=items;
//            this.itemsstatus=itemsstatus;
//            this.itemid=itemid;
//        }
//
//        @Override
//        public StoresListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.layout_managestoresadapter, parent, false);
//            return new StoresListAdapter.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final StoresListAdapter.MyViewHolder holder, final int position) {
//            //hashMap.put("empid", empid);
//            try{
//                String storeName="";
//                String empid="";
//                String status="";
//                storeName = storeshashmapList.get(position).get("name");
//                empid = storeshashmapList.get(position).get("empid");
//                status = storeshashmapList.get(position).get("status");
//
//                String dealer_Name=storeName+" / "+empid;
//                dealer_Name=dealer_Name.trim();
//                char first = dealer_Name.charAt(0);
//                String firstchar=String.valueOf(first);
//                if(firstchar.equals("/"))
//                {
//                    holder.storenametxt.setText(empid);
//                }else{
//                    holder.storenametxt.setText(storeName+" / "+empid);
//                }
//
////             if(itemsstatus[holder.indexRefrence].equals("1"))
////             {
////                 holder.switchcompatid.setChecked(true);
////             }else{
////                 holder.switchcompatid.setChecked(false);
////             }
//
////            if (status.equals("1")) {
////                holder.switchcompatid.setChecked(true);
////            } else {
////                holder.switchcompatid.setChecked(false);
////               }
////                boolean isConnectedToInternet = CheckNetWork
////                        .isConnectedToInternet(getActivity());
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
////                    Toast.makeText(getActivity(), R.string.networkerror,
////                            Toast.LENGTH_SHORT).show();
////
////                }
//            }catch (Exception e)
//            {
//            }
//        }
//
//        public void showCancelOrderPopup(final int position, final int status, final MyViewHolder holder,
//                                         final ArrayList<HashMap<String, String>> storeshashmapList) {
//            /** Used for Show Disclaimer Pop up screen */
//            alertDialog = new Dialog(getActivity());
//            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            alertDialog.getWindow().setBackgroundDrawableResource(
//                    android.R.color.transparent);
//            LayoutInflater inflater = getActivity().getLayoutInflater();
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
//                    getDealersList("cancel");
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
//                    updateStatusWithRetrofit(storeid, String.valueOf(status), reason,holder,storeshashmapList,position);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return storeshashmapList.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView storenametxt;
//         //   SwitchCompat switchcompatid;
//            int indexRefrence;
//
//            MyViewHolder(View view) {
//                super(view);
//
//                storenametxt = view.findViewById(R.id.storenametxt);
//             //   switchcompatid = view.findViewById(R.id.switchcompatid);
//                view.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(getActivity(), StoreOrderDetailsActivity.class);
////                String storeid = storeshashmapList.get(getAdapterPosition()).get("id");
////                intent.putExtra("store", storeid);
////                startActivity(intent);
//            }
//        }
//
//        private void updateStatusWithRetrofit(String storeid, final String storestatus, String reason,
//                                              final MyViewHolder holder,
//                                              final  ArrayList<HashMap<String, String>> storeshashmapList,
//                                              final int pos
//                                              ) {
//            final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
//            dialog.show();
//            RetrofitAPI mApiService = SharedDB.getInterfaceService();
//            Call<StoresDTO> mService = mApiService.updateStoreStatus(PRIMARYID, storeid, storestatus, reason);
//            mService.enqueue(new Callback<StoresDTO>() {
//                @Override
//                public void onResponse(Call<StoresDTO> call, Response<StoresDTO> response) {
//                    StoresDTO mLoginObject = response.body();
//                    dialog.dismiss();
//                    Log.e("response",""+response);
//                    try {
//                        String status = mLoginObject.getStatus();
//                        Log.e("status",status);
//
//                        if (status.equals("1")) {
//                            String message = mLoginObject.getMessage();
//
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                            if (adapter != null) {
//                                adapter.notifyDataSetChanged();
//                              //  holder.switchcompatid.setChecked(false);
//                            }
//                            storeshashmapList.remove(pos);
//                            adapter.notifyDataSetChanged();
//
//                        //    getDealersList("cancel");
//                        } else {
//                            String message = mLoginObject.getMessage();
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                            if (storestatus.equals("0")) {
//                                storeshashmapList.remove(holder.indexRefrence);
//                                adapter.notifyDataSetChanged();
//                                getDealersList("cancel");
//
////                                globalShare.setFinancemenuselectpos("5");
////
////                                if(FinanceDeptMenuScreenActivity.mainfinish!=null)
////                                {
////                                    FinanceDeptMenuScreenActivity.mainfinish.finish();
////                                }
////                                Intent intent=new Intent(getActivity(),FinanceDeptMenuScreenActivity.class);
////                                startActivity(intent);
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//                @Override
//                public void onFailure(Call<StoresDTO> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//}
package com.mrnovacrm.b2b_dealer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.Login;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsHomeFragment extends Fragment implements View.OnClickListener {
    GlobalShare globalShare;
    String PRIMARYID,COMPANY,SHORTFORM;
    LinearLayout agritechlinear, agrisciencelinear,productlnr;
    public ProductsHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        globalShare = (GlobalShare) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.layout_producthome, container, false);

        productlnr = rootView.findViewById(R.id.productlnr);
        agritechlinear = rootView.findViewById(R.id.agritechlinear);
        agrisciencelinear = rootView.findViewById(R.id.agrisciencelinear);
        agritechlinear.setOnClickListener(ProductsHomeFragment.this);
        agrisciencelinear.setOnClickListener(ProductsHomeFragment.this);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            COMPANY = values.get(SharedDB.COMPANY);
            SHORTFORM = values.get(SharedDB.SHORTFORM);

            if(SHORTFORM.equals("SE")) {
                if (COMPANY.equals("2")) {
                    agritechlinear.setVisibility(View.VISIBLE);
                    agrisciencelinear.setVisibility(View.GONE);
                  //  //productlnr.setWeightSum(1);
                } else {
                    agritechlinear.setVisibility(View.GONE);
                    agrisciencelinear.setVisibility(View.VISIBLE);
                  //  //productlnr.setWeightSum(1);
                }
            }else {
                try {
                    if (Integer.parseInt(COMPANIESLIST) == 0) {
                        if (COMPANY.equals("2")) {
                            agritechlinear.setVisibility(View.VISIBLE);
                            agrisciencelinear.setVisibility(View.GONE);
                            //productlnr.setWeightSum(1);
                        } else {
                            agritechlinear.setVisibility(View.GONE);
                            agrisciencelinear.setVisibility(View.VISIBLE);
                            //productlnr.setWeightSum(1);
                        }
                    } else if (Integer.parseInt(COMPANIESLIST) == 1) {
                        if (globalShare.getCompaniesList() != null) {
                            if (globalShare.getCompaniesList().size() > 0) {
                                for (int i = 0; i < globalShare.getCompaniesList().size(); i++) {
                                    String company = globalShare.getCompaniesList().get(i).getCompany();
                                    String company_id = globalShare.getCompaniesList().get(i).getCompany_id();
                                    if (globalShare.getCompaniesList().size() == 1) {
                                        if (company_id.equals("2")) {
                                            agritechlinear.setVisibility(View.VISIBLE);
                                            agrisciencelinear.setVisibility(View.GONE);
                                            //productlnr.setWeightSum(1);
                                        } else {
                                            agritechlinear.setVisibility(View.GONE);
                                            agrisciencelinear.setVisibility(View.VISIBLE);
                                            //productlnr.setWeightSum(1);
                                        }
                                    } else {
                                        agritechlinear.setVisibility(View.VISIBLE);
                                        agrisciencelinear.setVisibility(View.VISIBLE);
                                        productlnr.setWeightSum(2);
                                    }
                                }
                            }
                        } else {
                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getActivity());
                            if (isConnectedToInternet) {
                                getCompaniesList();
                            }
                        }
                    } else {
                        if (globalShare.getCompaniesList() != null) {
                            if (globalShare.getCompaniesList().size() > 0) {
                                for (int i = 0; i < globalShare.getCompaniesList().size(); i++) {
                                    String company = globalShare.getCompaniesList().get(i).getCompany();
                                    String company_id = globalShare.getCompaniesList().get(i).getCompany_id();
                                    if (globalShare.getCompaniesList().size() == 1) {
                                        if (company_id.equals("2")) {
                                            agritechlinear.setVisibility(View.VISIBLE);
                                            agrisciencelinear.setVisibility(View.GONE);
                                            //productlnr.setWeightSum(1);
                                        } else {
                                            agritechlinear.setVisibility(View.GONE);
                                            agrisciencelinear.setVisibility(View.VISIBLE);
                                            //productlnr.setWeightSum(1);
                                        }
                                    } else {
                                        agritechlinear.setVisibility(View.VISIBLE);
                                        agrisciencelinear.setVisibility(View.VISIBLE);
                                        productlnr.setWeightSum(2);
                                    }
                                }
                            }
                        } else {
                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getActivity());
                            if (isConnectedToInternet) {
                                getCompaniesList();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agrisciencelinear:
                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet)
                {
                    try{
                        Intent intent = new Intent(getActivity(), ProductCategoriesActivity.class);
                        intent.putExtra("title", "Agriscience Products");
                        intent.putExtra("id", "1");
                        startActivity(intent);
                    }catch(Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.agritechlinear:
                boolean isConnectedToInternet1 = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet1)
                {
                    try{
                        Intent intent1 = new Intent(getActivity(), ProductCategoriesActivity.class);
                        intent1.putExtra("title", "Agritech Products");
                        intent1.putExtra("id", "2");
                        startActivity(intent1);
                    }catch(Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void getCompaniesList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService=null;
        mService = mApiService.getDealerCompaniesList(PRIMARYID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                try {
                    Login mProductObject = response.body();
                    if (mProductObject != null) {
                        String status=mProductObject.getStatus();
                        if(status.equals("1"))
                        {
                            List<ContactsModelDTO> companiesList = mProductObject.getCompaniesDTOList();
                            if(companiesList!=null)
                            {
                                if(companiesList.size()>0)
                                {
                                    globalShare.setCompaniesList(companiesList);
                                    HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
                                    String ADDRESSID = values.get(SharedDB.ADDRESSID);
                                    String USERTYPE = values.get(SharedDB.USERTYPE);
                                    String SHORTFORM = values.get(SharedDB.SHORTFORM);
                                    String USERNAME = values.get(SharedDB.USERNAME);
                                    String MOBILE = values.get(SharedDB.MOBILE);
                                    String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                                    String COMPANY = values.get(SharedDB.COMPANY);
                                    String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                                    String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                                    String BRANCHID = values.get(SharedDB.BRANCHID);
                                    String PINCODE = values.get(SharedDB.PINCODE);
                                    String LATITUDE = values.get(SharedDB.LATITUDE);
                                    String LONGITIDE = values.get(SharedDB.LONGITIDE);
                                    String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                                    String IMAGEURL = values.get(SharedDB.IMAGEURL);
                                    String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                                    String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                                    String ROLEPKEY = values.get(SharedDB.ROLEPKEY);

                                    SharedDB.loginSahred(getActivity(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
                                            "", Double.parseDouble(LATITUDE), Double.parseDouble(LONGITIDE), PRIMARYID, PINCODE,
                                            USERNAME, IMAGEURL, ADDRESSID, BRANCHID,
                                            BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,
                                            String.valueOf(companiesList.size()),BRANCHCOUNT,ROLECOUNT,ROLEPKEY);

                                    for(int i=0;i<companiesList.size();i++)
                                    {
                                        String company=companiesList.get(i).getCompany();
                                        String company_id=companiesList.get(i).getCompany_id();
                                        if(companiesList.size()==1)
                                        {
                                            if(company_id.equals("2"))
                                            {
                                                agritechlinear.setVisibility(View.VISIBLE);
                                                agrisciencelinear.setVisibility(View.GONE);
                                                //productlnr.setWeightSum(1);
                                            }else{
                                                agritechlinear.setVisibility(View.GONE);
                                                agrisciencelinear.setVisibility(View.VISIBLE);
                                                //productlnr.setWeightSum(1);
                                            }
                                        }else{
                                            agritechlinear.setVisibility(View.VISIBLE);
                                            agrisciencelinear.setVisibility(View.VISIBLE);
                                            productlnr.setWeightSum(2);
                                        }
                                    }
                                }
                            }
                        }else{
                        }
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
              //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(),"onresume called",Toast.LENGTH_SHORT).show();
//        if(SHORTFORM.equals("SE")) {
//            if (COMPANY.equals("2")) {
//                agritechlinear.setVisibility(View.VISIBLE);
//                agrisciencelinear.setVisibility(View.GONE);
//                //  //productlnr.setWeightSum(1);
//            } else {
//                agritechlinear.setVisibility(View.GONE);
//                agrisciencelinear.setVisibility(View.VISIBLE);
//                //  //productlnr.setWeightSum(1);
//            }
//        }

    }
}
package com.mrnovacrm.b2b_dealer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.b2b_finance_dept.FinanceEmployeesListActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.EmpDetailsDTO;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.RecordsDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerRegistrationFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    GlobalShare globalShare;
    Context mContext;
    LinearLayout companydetails_downarrow, contacts_downarrow, accountsdetailsdownarrow, deposits_downarrow,
            licence_downarrow, personal_downarrow;

    EditText dealername_txt, dno_txt, streetname_txt, landmark_txt, towncity_txt, pincode_txt, gstinnumber_txt, pannumber_txt,
            brach_txt, name_txt, marketingofccontactnumber_txt, emailaddress_txt, headquater_txt,
            dealercontactno_txt, dealerwhatsapp_txt, alternativecontact_txt, dealeremailid_txt,
            bankaccountnumber_txt, bankname_txt, branch_txt, ifsc_txt,
            amount_txt,
            fertilizers_txt, pesticideslicense_txt, otherlicence_txt,
            ownername_txt, ownwerdesingation_txt, ownercontactnumber_txt, keypersonname_txt, keyperson_contacttxt,
            keypersondesignation_txt;

    static EditText date_txt, validupto_txt, pesticideslicensevalidupto_txt,seedlicensevalidupto_txt;

    Spinner country_spinner, state_spinner, district_spinner;

    RelativeLayout companydetailsrealtive, contactdetailsrelative, accountdetailsrelative, depositrealitve,
            licencedetailsrelative, personaldetailsrelative;

    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();

    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();

    ArrayList<String> distNamesList = new ArrayList<>();
    ArrayList<String> distIdsList = new ArrayList<>();

    String COUNTRY_ID = "";
    String STATE_ID = "";
    String DISTRICT_ID = "";
    Button submitbtn;

    boolean isCompanyDetailsSelected;
    boolean isContactDetailsSelected;
    boolean isAccountDetailsSelected;
    boolean isDepositDetailsSelected;
    boolean isLicenceDetailsSelected;
    boolean isPersonalDetailsSelected;

    ImageView companyimage,contactimage,accountimagview,depositimage,licenceimage,personalimage;
    private String PRIMARYID,COMPANY,BRANCHID,BRANCHNAME;
    EditText seedlicense_txt;
    private String USER_UNICODE="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.activity_employee_registration, container, false);
        View rootView = inflater.inflate(R.layout.layout_dealerreg, container, false);
        mContext = getActivity();
        globalShare = (GlobalShare) getActivity().getApplicationContext();

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            COMPANY = values.get(SharedDB.COMPANY);
            BRANCHID = values.get(SharedDB.BRANCHID);
            BRANCHNAME = values.get(SharedDB.BRANCHNAME);
        }

        companydetails_downarrow = rootView.findViewById(R.id.companydetails_downarrow);

        dealername_txt = rootView.findViewById(R.id.dealername_txt);
        dno_txt = rootView.findViewById(R.id.dno_txt);
        streetname_txt = rootView.findViewById(R.id.streetname_txt);
        landmark_txt = rootView.findViewById(R.id.landmark_txt);
        country_spinner = rootView.findViewById(R.id.country_spinner);
        state_spinner = rootView.findViewById(R.id.state_spinner);
        district_spinner = rootView.findViewById(R.id.district_spinner);
        towncity_txt = rootView.findViewById(R.id.towncity_txt);
        pincode_txt = rootView.findViewById(R.id.pincode_txt);
        gstinnumber_txt = rootView.findViewById(R.id.gstinnumber_txt);
        pannumber_txt = rootView.findViewById(R.id.pannumber_txt);
        brach_txt = rootView.findViewById(R.id.brach_txt);
        brach_txt.setText(BRANCHNAME);
        name_txt = rootView.findViewById(R.id.name_txt);
        marketingofccontactnumber_txt = rootView.findViewById(R.id.marketingofccontactnumber_txt);
        emailaddress_txt = rootView.findViewById(R.id.emailaddress_txt);
        headquater_txt = rootView.findViewById(R.id.headquater_txt);

        dealername_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        dno_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        streetname_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        landmark_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        towncity_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pincode_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        gstinnumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pannumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        name_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        marketingofccontactnumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        headquater_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        contacts_downarrow = rootView.findViewById(R.id.contacts_downarrow);
        dealercontactno_txt = rootView.findViewById(R.id.dealercontactno_txt);
        dealerwhatsapp_txt = rootView.findViewById(R.id.dealerwhatsapp_txt);
        alternativecontact_txt = rootView.findViewById(R.id.alternativecontact_txt);
        dealeremailid_txt = rootView.findViewById(R.id.dealeremailid_txt);


        dealercontactno_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        dealerwhatsapp_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        alternativecontact_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        accountsdetailsdownarrow = rootView.findViewById(R.id.accountsdetailsdownarrow);
        bankaccountnumber_txt = rootView.findViewById(R.id.bankaccountnumber_txt);
        bankname_txt = rootView.findViewById(R.id.bankname_txt);
        branch_txt = rootView.findViewById(R.id.branch_txt);
        ifsc_txt = rootView.findViewById(R.id.ifsc_txt);

        bankaccountnumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        bankname_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        branch_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ifsc_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        deposits_downarrow = rootView.findViewById(R.id.deposits_downarrow);
        amount_txt = rootView.findViewById(R.id.amount_txt);
        date_txt = rootView.findViewById(R.id.date_txt);

        licence_downarrow = rootView.findViewById(R.id.licence_downarrow);
        fertilizers_txt = rootView.findViewById(R.id.fertilizers_txt);
        validupto_txt = rootView.findViewById(R.id.validupto_txt);
        pesticideslicense_txt = rootView.findViewById(R.id.pesticideslicense_txt);
        pesticideslicensevalidupto_txt = rootView.findViewById(R.id.pesticideslicensevalidupto_txt);
        seedlicense_txt = rootView.findViewById(R.id.seedlicense_txt);
        seedlicensevalidupto_txt = rootView.findViewById(R.id.seedlicensevalidupto_txt);
        otherlicence_txt = rootView.findViewById(R.id.otherlicence_txt);

        fertilizers_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        validupto_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pesticideslicense_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pesticideslicensevalidupto_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        seedlicense_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        seedlicensevalidupto_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        otherlicence_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        personal_downarrow = rootView.findViewById(R.id.personal_downarrow);
        ownername_txt = rootView.findViewById(R.id.ownername_txt);
        ownwerdesingation_txt = rootView.findViewById(R.id.ownwerdesingation_txt);
        ownercontactnumber_txt = rootView.findViewById(R.id.ownercontactnumber_txt);
        keypersonname_txt = rootView.findViewById(R.id.keypersonname_txt);
        keyperson_contacttxt = rootView.findViewById(R.id.keyperson_contacttxt);
        keypersondesignation_txt = rootView.findViewById(R.id.keypersondesignation_txt);
        submitbtn = rootView.findViewById(R.id.submitbtn);

        ownername_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ownwerdesingation_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ownercontactnumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        keypersonname_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        keyperson_contacttxt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        keypersondesignation_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        submitbtn.setOnClickListener(DealerRegistrationFragment.this);
        companydetails_downarrow.setOnClickListener(DealerRegistrationFragment.this);
        contacts_downarrow.setOnClickListener(DealerRegistrationFragment.this);
        accountsdetailsdownarrow.setOnClickListener(DealerRegistrationFragment.this);
        deposits_downarrow.setOnClickListener(DealerRegistrationFragment.this);
        licence_downarrow.setOnClickListener(DealerRegistrationFragment.this);
        personal_downarrow.setOnClickListener(DealerRegistrationFragment.this);

        companydetailsrealtive = rootView.findViewById(R.id.companydetailsrealtive);
        contactdetailsrelative = rootView.findViewById(R.id.contactdetailsrelative);
        accountdetailsrelative = rootView.findViewById(R.id.accountdetailsrelative);
        depositrealitve = rootView.findViewById(R.id.depositrealitve);
        licencedetailsrelative = rootView.findViewById(R.id.licencedetailsrelative);
        personaldetailsrelative = rootView.findViewById(R.id.personaldetailsrelative);
        keypersondesignation_txt = rootView.findViewById(R.id.keypersondesignation_txt);


        companyimage = rootView.findViewById(R.id.companyimage);
        contactimage = rootView.findViewById(R.id.contactimage);
        accountimagview = rootView.findViewById(R.id.accountimagview);
        depositimage = rootView.findViewById(R.id.depositimage);
        licenceimage = rootView.findViewById(R.id.licenceimage);
        personalimage = rootView.findViewById(R.id.personalimage);


        companydetailsrealtive.setVisibility(View.VISIBLE);
        contactdetailsrelative.setVisibility(View.GONE);
        accountdetailsrelative.setVisibility(View.GONE);
        depositrealitve.setVisibility(View.GONE);
        licencedetailsrelative.setVisibility(View.GONE);
        personaldetailsrelative.setVisibility(View.GONE);

        date_txt.setOnTouchListener(DealerRegistrationFragment.this);
        validupto_txt.setOnTouchListener(DealerRegistrationFragment.this);
        pesticideslicensevalidupto_txt.setOnTouchListener(DealerRegistrationFragment.this);
        seedlicensevalidupto_txt.setOnTouchListener(DealerRegistrationFragment.this);

        hintCountries();
        hintStates();
        hintDist();

        isCompanyDetailsSelected = true;
        isContactDetailsSelected = false;
        isAccountDetailsSelected = false;
        isDepositDetailsSelected = false;
        isLicenceDetailsSelected = false;
        isPersonalDetailsSelected = false;

        companyimage.setImageResource(R.drawable.uparrows);
        contactimage.setImageResource(R.drawable.downarrows);
        accountimagview.setImageResource(R.drawable.downarrows);
        depositimage.setImageResource(R.drawable.downarrows);
        licenceimage.setImageResource(R.drawable.downarrows);
        personalimage.setImageResource(R.drawable.downarrows);

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            getUserUniqID();
            loadGraphicsData("country", "");
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
      //  loadLicenceDetails();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.companydetails_downarrow:

                if (isCompanyDetailsSelected) {
                    isCompanyDetailsSelected = false;
                    companydetailsrealtive.setVisibility(View.GONE);
                    companyimage.setImageResource(R.drawable.downarrows);
                } else {
                    isCompanyDetailsSelected = true;
                    companydetailsrealtive.setVisibility(View.VISIBLE);
                    companyimage.setImageResource(R.drawable.uparrows);
                }

                contactimage.setImageResource(R.drawable.downarrows);
                accountimagview.setImageResource(R.drawable.downarrows);
                depositimage.setImageResource(R.drawable.downarrows);
                licenceimage.setImageResource(R.drawable.downarrows);
                personalimage.setImageResource(R.drawable.downarrows);

                isContactDetailsSelected = false;
                isAccountDetailsSelected = false;
                isDepositDetailsSelected = false;
                isLicenceDetailsSelected = false;
                isPersonalDetailsSelected = false;

                contactdetailsrelative.setVisibility(View.GONE);
                accountdetailsrelative.setVisibility(View.GONE);
                depositrealitve.setVisibility(View.GONE);
                licencedetailsrelative.setVisibility(View.GONE);
                personaldetailsrelative.setVisibility(View.GONE);
                break;

            case R.id.contacts_downarrow:
                if (isContactDetailsSelected) {
                    isContactDetailsSelected = false;
                    contactdetailsrelative.setVisibility(View.GONE);
                    contactimage.setImageResource(R.drawable.downarrows);
                } else {
                    isContactDetailsSelected = true;
                    contactdetailsrelative.setVisibility(View.VISIBLE);
                    contactimage.setImageResource(R.drawable.uparrows);
                }

                companyimage.setImageResource(R.drawable.downarrows);
                accountimagview.setImageResource(R.drawable.downarrows);
                depositimage.setImageResource(R.drawable.downarrows);
                licenceimage.setImageResource(R.drawable.downarrows);
                personalimage.setImageResource(R.drawable.downarrows);

                isCompanyDetailsSelected = false;
                isAccountDetailsSelected = false;
                isDepositDetailsSelected = false;
                isLicenceDetailsSelected = false;
                isPersonalDetailsSelected = false;

                companydetailsrealtive.setVisibility(View.GONE);
                accountdetailsrelative.setVisibility(View.GONE);
                depositrealitve.setVisibility(View.GONE);
                licencedetailsrelative.setVisibility(View.GONE);
                personaldetailsrelative.setVisibility(View.GONE);
                break;

            case R.id.accountsdetailsdownarrow:
                if (isAccountDetailsSelected) {
                    isAccountDetailsSelected = false;
                    accountdetailsrelative.setVisibility(View.GONE);
                    accountimagview.setImageResource(R.drawable.downarrows);
                } else {
                    isAccountDetailsSelected = true;
                    accountdetailsrelative.setVisibility(View.VISIBLE);
                    accountimagview.setImageResource(R.drawable.uparrows);
                }

                companyimage.setImageResource(R.drawable.downarrows);
                contactimage.setImageResource(R.drawable.downarrows);
                depositimage.setImageResource(R.drawable.downarrows);
                licenceimage.setImageResource(R.drawable.downarrows);
                personalimage.setImageResource(R.drawable.downarrows);

                isCompanyDetailsSelected = false;
                isContactDetailsSelected = false;
                isDepositDetailsSelected = false;
                isLicenceDetailsSelected = false;
                isPersonalDetailsSelected = false;

                companydetailsrealtive.setVisibility(View.GONE);
                contactdetailsrelative.setVisibility(View.GONE);
                depositrealitve.setVisibility(View.GONE);
                licencedetailsrelative.setVisibility(View.GONE);
                personaldetailsrelative.setVisibility(View.GONE);
                break;

            case R.id.deposits_downarrow:

                if (isDepositDetailsSelected) {
                    isDepositDetailsSelected = false;
                    depositrealitve.setVisibility(View.GONE);
                    depositimage.setImageResource(R.drawable.downarrows);
                } else {
                    isDepositDetailsSelected = true;
                    depositrealitve.setVisibility(View.VISIBLE);
                    depositimage.setImageResource(R.drawable.uparrows);
                }

                companyimage.setImageResource(R.drawable.downarrows);
                contactimage.setImageResource(R.drawable.downarrows);
                accountimagview.setImageResource(R.drawable.downarrows);
                licenceimage.setImageResource(R.drawable.downarrows);
                personalimage.setImageResource(R.drawable.downarrows);

                companydetailsrealtive.setVisibility(View.GONE);
                contactdetailsrelative.setVisibility(View.GONE);
                accountdetailsrelative.setVisibility(View.GONE);
                licencedetailsrelative.setVisibility(View.GONE);
                personaldetailsrelative.setVisibility(View.GONE);
                break;

            case R.id.licence_downarrow:
                if (isLicenceDetailsSelected) {
                    isLicenceDetailsSelected = false;
                    licencedetailsrelative.setVisibility(View.GONE);
                    licenceimage.setImageResource(R.drawable.downarrows);
                } else {
                    isLicenceDetailsSelected = true;
                    licencedetailsrelative.setVisibility(View.VISIBLE);
                    licenceimage.setImageResource(R.drawable.uparrows);
                }
                companyimage.setImageResource(R.drawable.downarrows);
                contactimage.setImageResource(R.drawable.downarrows);
                accountimagview.setImageResource(R.drawable.downarrows);
                depositimage.setImageResource(R.drawable.downarrows);
                personalimage.setImageResource(R.drawable.downarrows);

                isCompanyDetailsSelected = false;
                isContactDetailsSelected = false;
                isAccountDetailsSelected = false;
                isDepositDetailsSelected = false;
                isPersonalDetailsSelected = false;

                companydetailsrealtive.setVisibility(View.GONE);
                contactdetailsrelative.setVisibility(View.GONE);
                accountdetailsrelative.setVisibility(View.GONE);
                depositrealitve.setVisibility(View.GONE);
                personaldetailsrelative.setVisibility(View.GONE);
                break;

            case R.id.personal_downarrow:

                if (isPersonalDetailsSelected) {
                    isPersonalDetailsSelected = false;
                    personaldetailsrelative.setVisibility(View.GONE);
                    personalimage.setImageResource(R.drawable.downarrows);
                } else {
                    isPersonalDetailsSelected = true;
                    personaldetailsrelative.setVisibility(View.VISIBLE);
                    personalimage.setImageResource(R.drawable.uparrows);
                }
                companyimage.setImageResource(R.drawable.downarrows);
                contactimage.setImageResource(R.drawable.downarrows);
                accountimagview.setImageResource(R.drawable.downarrows);
                depositimage.setImageResource(R.drawable.downarrows);
                licenceimage.setImageResource(R.drawable.downarrows);

                isCompanyDetailsSelected = false;
                isContactDetailsSelected = false;
                isAccountDetailsSelected = false;
                isDepositDetailsSelected = false;
                isLicenceDetailsSelected = false;

                companydetailsrealtive.setVisibility(View.GONE);
                contactdetailsrelative.setVisibility(View.GONE);
                accountdetailsrelative.setVisibility(View.GONE);
                depositrealitve.setVisibility(View.GONE);
                licencedetailsrelative.setVisibility(View.GONE);
                break;
            case R.id.submitbtn:
                checkValidCredentials();
                break;
            default:
                break;
        }
    }

    public void hintCountries() {
        countryNamesList.clear();
        countryNamesList.add("Select Country");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    COUNTRY_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintStates() {
        stateNamesList.clear();
        stateNamesList.add("Select State");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintDist() {
        distNamesList.clear();
        distNamesList.add("Select District");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        district_spinner.setAdapter(spinnerClass);
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    DISTRICT_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void loadGraphicsData(final String type, final String id) {
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            try{
                final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<DemoGraphicsDTO> mService = null;
                if (type.equals("country")) {
                    mService = mApiService.getCountries();
                } else if (type.equals("state")) {
                    mService = mApiService.getStates(id);
                } else if (type.equals("dist")) {
                    mService = mApiService.getDistricts(id);
                }
                mService.enqueue(new Callback<DemoGraphicsDTO>() {
                    @Override
                    public void onResponse(Call<DemoGraphicsDTO> call, Response<DemoGraphicsDTO> response) {
                        Log.e("response", "" + response);
                        DemoGraphicsDTO mLoginObject = response.body();
                        dialog.dismiss();
                        try {
                            String status = mLoginObject.getStatus();
                            if (status.equals("1")) {
                                List<RecordsDTO> demograRecordsDTOS = mLoginObject.getDemograRecordsDTOS();
                                if (demograRecordsDTOS != null) {
                                    if (demograRecordsDTOS.size() > 0) {
                                        if (type.equals("country")) {
                                            countryNamesList.clear();
                                            countryNamesList.add("Select Country");
                                            countryIdsList.clear();
                                        } else if (type.equals("state")) {
                                            stateNamesList.clear();
                                            stateNamesList.add("Select State");
                                            stateIdsList.clear();
                                        } else if (type.equals("dist")) {
                                            distNamesList.clear();
                                            distNamesList.add("Select District");
                                            distIdsList.clear();
                                        }

                                        for (int i = 0; i < demograRecordsDTOS.size(); i++) {
                                            String id = demograRecordsDTOS.get(i).getId();
                                            String name = demograRecordsDTOS.get(i).getName();
                                            if (type.equals("country")) {
                                                countryNamesList.add(name);
                                                countryIdsList.add(id);
                                            }
                                            if (type.equals("state")) {
                                                stateNamesList.add(name);
                                                stateIdsList.add(id);
                                            }
                                            if (type.equals("dist")) {
                                                distNamesList.add(name);
                                                distIdsList.add(id);
                                            }
                                        }

                                        if (type.equals("country")) {
                                            showCountriesData();
                                        } else if (type.equals("state")) {
                                            showStatesData();
                                        } else if (type.equals("dist")) {
                                            showDistData();
                                        }
                                    }
                                }
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<DemoGraphicsDTO> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                        Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserUniqID() {
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            try{
//                final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//                dialog.show();
                RetrofitAPI mApiService = SharedDB.getInterfaceService();
                Call<EmployeeDTO> mService = null;
                 mService = mApiService.getEmpUniqueCode(PRIMARYID);
                mService.enqueue(new Callback<EmployeeDTO>() {
                    @Override
                    public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
                        Log.e("response", "" + response);
                        EmployeeDTO empDTOObject = response.body();
                      //  dialog.dismiss();
                        try {
                            String status = empDTOObject.getStatus();
                            if (status.equals("1")) {
                               USER_UNICODE= empDTOObject.getUni_code();
                                name_txt.setText(USER_UNICODE);
                            } else {
                               USER_UNICODE="";
                                name_txt.setText("");
                            }
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onFailure(Call<EmployeeDTO> call, Throwable t) {
                        call.cancel();
                  //      dialog.dismiss();
                       // Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(Exception e)
            {
            }
        }else{
         //   Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }


    public void showCountriesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),

                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    COUNTRY_ID = "";
                    hintStates();
                    hintDist();
                } else {
                    COUNTRY_ID = countryIdsList.get(position - 1);
                    loadGraphicsData("state", COUNTRY_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showStatesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                } else {
                    STATE_ID = stateIdsList.get(position - 1);
                    loadGraphicsData("dist", STATE_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showDistData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        district_spinner.setAdapter(spinnerClass);
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    DISTRICT_ID = "";
                } else {
                    DISTRICT_ID = distIdsList.get(position - 1);
                    //loadGraphicsData("dist", STATE_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        switch (v.getId()) {
//            case R.id.date_txt:
//
//                break;
//            case R.id.validupto_txt:
//
//                break;
//            case R.id.pesticideslicensevalidupto_txt:
//
//                break;
//            default:
//                break;
//        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date_txt) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.validupto_txt) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "validupto");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.pesticideslicensevalidupto_txt) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "licenceupto");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.seedlicensevalidupto_txt) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "seedlicenceupto");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        }
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String type;
            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
                } else if (type.equals("validupto")) {
                    return new DatePickerDialog(getActivity(), valid_dateListener, year, month, day);
                } else if (type.equals("licenceupto")) {
                    return new DatePickerDialog(getActivity(), pesticidesvalid_dateListener, year, month, day);
                } else if (type.equals("seedlicenceupto")) {
                    return new DatePickerDialog(getActivity(), seedlicencevalid_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        date_txt.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        private DatePickerDialog.OnDateSetListener valid_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        validupto_txt.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };

        private DatePickerDialog.OnDateSetListener pesticidesvalid_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        pesticideslicensevalidupto_txt.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        private DatePickerDialog.OnDateSetListener seedlicencevalid_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        seedlicensevalidupto_txt.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    public void checkValidCredentials() {
        String DEALERNAME = dealername_txt.getText().toString().trim();
        String DNO = dno_txt.getText().toString().trim();
        String STREETNAME = streetname_txt.getText().toString().trim();
        String LANDMARK = landmark_txt.getText().toString().trim();
        String TOWNCITY = towncity_txt.getText().toString().trim();
        String PINCODE = pincode_txt.getText().toString().trim();
        String GSTINNUMBER = gstinnumber_txt.getText().toString().trim();
        String PAN = pannumber_txt.getText().toString().trim();
        String NAME = name_txt.getText().toString().trim();
        String MARKETINGOFCCONTACT = marketingofccontactnumber_txt.getText().toString().trim();
        String EMAILADDRESS = emailaddress_txt.getText().toString().trim();
        String HEADQUATER = headquater_txt.getText().toString().trim();
        String DEALER_CONTACT = dealercontactno_txt.getText().toString().trim();
        String DEALER_WHATSAPP = dealerwhatsapp_txt.getText().toString().trim();
        String ALTERNATIVE_CONTACT = alternativecontact_txt.getText().toString().trim();
        String DEALER_EMAILID = dealeremailid_txt.getText().toString().trim();
        String BANKACCOUT_NUMBER = bankaccountnumber_txt.getText().toString().trim();
        String BANKNAME = bankname_txt.getText().toString().trim();
        String BRANCH = branch_txt.getText().toString().trim();
        String IFSC = ifsc_txt.getText().toString().trim();

        String AMOUNT = amount_txt.getText().toString().trim();
        String DEPOSIDATE=date_txt.getText().toString().trim();
        String FERTILIZERS = fertilizers_txt.getText().toString().trim();
        String FERTILIZERSUPTO = validupto_txt.getText().toString().trim();
        String PRESTICIDESLICENCE = pesticideslicense_txt.getText().toString().trim();
        String PRESTICIDESLICENCEUPTO = pesticideslicensevalidupto_txt.getText().toString().trim();
        String OTHERLICENCE = otherlicence_txt.getText().toString().trim();
        String OWNER_NAME = ownername_txt.getText().toString().trim();
        String OWNER_DESIGNATION = ownwerdesingation_txt.getText().toString().trim();
        String OWNER_CONTACT = ownercontactnumber_txt.getText().toString().trim();
        String KEYPERSON_NAME = keypersonname_txt.getText().toString().trim();
        String KEYPERSON_CONTACT = keyperson_contacttxt.getText().toString().trim();
        String KEYPERSON_DESIGNATION = keypersondesignation_txt.getText().toString().trim();

        String SEEDLICENCE = seedlicense_txt.getText().toString().trim();
        String SEEDLICENCE_VALIDUPTO = seedlicensevalidupto_txt.getText().toString().trim();


//        Log.e("DEALERNAME",DEALERNAME);
//        Log.e("DNO",DNO);
//        Log.e("STREETNAME",STREETNAME);
//        Log.e("LANDMARK",LANDMARK);
//        Log.e("TOWNCITY",TOWNCITY);
//        Log.e("PINCODE",PINCODE);
//        Log.e("GSTINNUMBER",GSTINNUMBER);
//        Log.e("PAN",PAN);
//        Log.e("NAME",NAME);
//        Log.e("MARKETINGOFCCONTACT",MARKETINGOFCCONTACT);
//        Log.e("EMAILADDRESS",EMAILADDRESS);
//        Log.e("HEADQUATER",HEADQUATER);
//        Log.e("DEALER_CONTACT",DEALER_CONTACT);
//        Log.e("DEALER_WHATSAPP",DEALER_WHATSAPP);
//        Log.e("ALTERNATIVE_CONTACT",ALTERNATIVE_CONTACT);
//        Log.e("DEALER_EMAILID",DEALER_EMAILID);
//        Log.e("BANKACCOUT_NUMBER",BANKACCOUT_NUMBER);
//        Log.e("BANKNAME",BANKNAME);
//        Log.e("BRANCH",BRANCH);
//        Log.e("IFSC",IFSC);
//        Log.e("AMOUNT",AMOUNT);
//        Log.e("FERTILIZERS",FERTILIZERS);
//        Log.e("FERTILIZERSUPTO",FERTILIZERSUPTO);
//        Log.e("PRESTICIDESLICENCE",PRESTICIDESLICENCE);
//        Log.e("PRESTICIDESLICENCEUPTO",PRESTICIDESLICENCEUPTO);
//        Log.e("OTHERLICENCE",OTHERLICENCE);
//        Log.e("OWNER_NAME",OWNER_NAME);
//        Log.e("OWNER_DESIGNATION",OWNER_DESIGNATION);
//        Log.e("OWNER_CONTACT",OWNER_CONTACT);
//        Log.e("KEYPERSON_NAME",KEYPERSON_NAME);
//        Log.e("KEYPERSON_CONTACT",KEYPERSON_CONTACT);
//        Log.e("KEYPERSON_DESIGNATION",KEYPERSON_DESIGNATION);
//        Log.e("SEEDLICENCE",SEEDLICENCE);
//        Log.e("SEEDLICENCE_VALIDUPTO",SEEDLICENCE_VALIDUPTO);
//        Log.e("COUNTRY_ID",COUNTRY_ID);
//        Log.e("STATE_ID",STATE_ID);
//        Log.e("DISTRICT_ID",DISTRICT_ID);

        if (DEALERNAME == null || "".equalsIgnoreCase(DEALERNAME) || DEALERNAME.equals("")
                || DNO == null || "".equalsIgnoreCase(DNO) || DNO.equals("")
                || STREETNAME == null || "".equalsIgnoreCase(STREETNAME) || STREETNAME.equals("")
                || LANDMARK == null || "".equalsIgnoreCase(LANDMARK) || LANDMARK.equals("")
                || TOWNCITY == null || "".equalsIgnoreCase(TOWNCITY) || TOWNCITY.equals("")
                || PINCODE == null || "".equalsIgnoreCase(PINCODE) || PINCODE.equals("")
                || GSTINNUMBER == null || "".equalsIgnoreCase(GSTINNUMBER) || GSTINNUMBER.equals("")
                || PAN == null || "".equalsIgnoreCase(PAN) || PAN.equals("")
                || NAME == null || "".equalsIgnoreCase(NAME) || NAME.equals("")
                || MARKETINGOFCCONTACT == null || "".equalsIgnoreCase(MARKETINGOFCCONTACT) || MARKETINGOFCCONTACT.equals("")
                || EMAILADDRESS == null || "".equalsIgnoreCase(EMAILADDRESS) || EMAILADDRESS.equals("")
                || HEADQUATER == null || "".equalsIgnoreCase(HEADQUATER) || HEADQUATER.equals("")
                || DEALER_CONTACT == null || "".equalsIgnoreCase(DEALER_CONTACT) || DEALER_CONTACT.equals("")
                || DEALER_WHATSAPP == null || "".equalsIgnoreCase(DEALER_WHATSAPP) || DEALER_WHATSAPP.equals("")
                || ALTERNATIVE_CONTACT == null || "".equalsIgnoreCase(ALTERNATIVE_CONTACT) || ALTERNATIVE_CONTACT.equals("")
                || DEALER_EMAILID == null || "".equalsIgnoreCase(DEALER_EMAILID) || DEALER_EMAILID.equals("")
                || BANKACCOUT_NUMBER == null || "".equalsIgnoreCase(BANKACCOUT_NUMBER) || BANKACCOUT_NUMBER.equals("")
                || BANKNAME == null || "".equalsIgnoreCase(BANKNAME) || BANKNAME.equals("")
                || BRANCH == null || "".equalsIgnoreCase(BRANCH) || BRANCH.equals("")
                || IFSC == null || "".equalsIgnoreCase(IFSC) || IFSC.equals("")
                || AMOUNT == null || "".equalsIgnoreCase(AMOUNT) || AMOUNT.equals("")
                || FERTILIZERS == null || "".equalsIgnoreCase(FERTILIZERS) || FERTILIZERS.equals("")
                || FERTILIZERSUPTO == null || "".equalsIgnoreCase(FERTILIZERSUPTO) || FERTILIZERSUPTO.equals("")
                || PRESTICIDESLICENCE == null || "".equalsIgnoreCase(PRESTICIDESLICENCE) || PRESTICIDESLICENCE.equals("")
                || PRESTICIDESLICENCEUPTO == null || "".equalsIgnoreCase(PRESTICIDESLICENCEUPTO) || PRESTICIDESLICENCEUPTO.equals("")
                || OTHERLICENCE == null || "".equalsIgnoreCase(OTHERLICENCE) || OTHERLICENCE.equals("")
                || OWNER_NAME == null || "".equalsIgnoreCase(OWNER_NAME) || OWNER_NAME.equals("")
                || OWNER_DESIGNATION == null || "".equalsIgnoreCase(OWNER_DESIGNATION) || OWNER_DESIGNATION.equals("")
                || OWNER_CONTACT == null || "".equalsIgnoreCase(OWNER_CONTACT) || OWNER_CONTACT.equals("")
                || KEYPERSON_NAME == null || "".equalsIgnoreCase(KEYPERSON_NAME) || KEYPERSON_NAME.equals("")
                || KEYPERSON_CONTACT == null || "".equalsIgnoreCase(KEYPERSON_CONTACT) || KEYPERSON_CONTACT.equals("")
                || KEYPERSON_DESIGNATION == null || "".equalsIgnoreCase(KEYPERSON_DESIGNATION) || KEYPERSON_DESIGNATION.equals("")
                || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID) || COUNTRY_ID.equals("")
                || STATE_ID == null || "".equalsIgnoreCase(STATE_ID) || STATE_ID.equals("")
                || DISTRICT_ID == null || "".equalsIgnoreCase(DISTRICT_ID) || DISTRICT_ID.equals("")
                || SEEDLICENCE == null || "".equalsIgnoreCase(SEEDLICENCE) || SEEDLICENCE.equals("")
                || SEEDLICENCE_VALIDUPTO == null || "".equalsIgnoreCase(SEEDLICENCE_VALIDUPTO) || SEEDLICENCE_VALIDUPTO.equals("")
                ) {
            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {

            String GSTINPattern = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$";
            if (GSTINNUMBER.matches(GSTINPattern)) {
                Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                Matcher matcher = pattern.matcher(PAN);
                // Check if pattern matches
                if (matcher.matches()) {

                    boolean isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(getActivity());
                    if(isConnectedToInternet)
                    {
                        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
                        dialog.show();
                        RetrofitAPI mApiService = SharedDB.getInterfaceService();
                        Call<Login> mService = null;
                        mService = mApiService.addDealerFealds(OWNER_NAME,DEALERNAME,KEYPERSON_NAME,"",COUNTRY_ID,
                                STATE_ID,TOWNCITY,
                                "",PAN,GSTINNUMBER,"",BANKNAME,BANKACCOUT_NUMBER,IFSC,"","",
                                KEYPERSON_CONTACT,"",NAME,"",OWNER_CONTACT,
                                DEALER_EMAILID,PRIMARYID,"DEALER",COMPANY,BRANCHID,"",
                                DNO,OWNER_DESIGNATION,DEALER_CONTACT,ALTERNATIVE_CONTACT,DEALER_WHATSAPP,STREETNAME,
                                LANDMARK,DISTRICT_ID,BRANCH,KEYPERSON_DESIGNATION,
                                PRESTICIDESLICENCE,PRESTICIDESLICENCEUPTO,FERTILIZERS,FERTILIZERSUPTO,
                                SEEDLICENCE,SEEDLICENCE_VALIDUPTO,OTHERLICENCE,DEPOSIDATE,AMOUNT);
                        mService.enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                Log.e("response", "" + response);
                                Login mLoginObject = response.body();
                                dialog.dismiss();
                                try {
                                    String status = mLoginObject.getStatus();
                                    if (status.equals("1")) {
                                        String message = mLoginObject.getMessage();
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                        Fragment fragment=new DealerRegistrationFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.container_body, fragment);
                                        fragmentTransaction.commit();
                                    } else {                            String message = mLoginObject.getMessage();
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                call.cancel();
                                dialog.dismiss();
                           //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please enter valid gstin number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadLicenceDetails()
    {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;
                    mService = mApiService.getLicenses();
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();

                        List<EmpDetailsDTO> licencedetails = mLoginObject.getEmpDetailsDTOS();
                        if(licencedetails!=null)
                        {
                            if(licencedetails.size()>0)
                            {
                                for(int i=0;i<licencedetails.size();i++)
                                {
                                   String id=licencedetails.get(i).getId();
                                   String name=licencedetails.get(i).getName();



                                }
                            }
                        }
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

    public void showLicencedetails()
    {

        EditText editText = new EditText(getActivity());
        // Add an ID to it
       // editText.setId(View.generateViewId());
        // Get the Hint text for EditText field which will be presented to the
        // user in the TextInputLayout
      //  editText.setHint(field.getHint());
        // Set color of the hint text inside the EditText field
        editText.setHintTextColor(getResources().getColor(android.R.color.white));
        // Set the font size of the text that the user will enter
        editText.setTextSize(16);
        // Set the color of the text inside the EditText field
        editText.setTextColor(getResources().getColor(android.R.color.white));
        // Define layout params for the EditTExt field
        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // Set editText layout parameters to the editText field
        editText.setLayoutParams(editTextParams);


        TextInputLayout textInputLayout = new TextInputLayout(getActivity());
       // textInputLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(textInputLayoutParams);

        // Then you add editText into a textInputLayout
        textInputLayout.addView(editText, editTextParams);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_superadmin, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
                intent.putExtra("SHORTFROM","DEALER");
                intent.putExtra("TITLE","Dealers List");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
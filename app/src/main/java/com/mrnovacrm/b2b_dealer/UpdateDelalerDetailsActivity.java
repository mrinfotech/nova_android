package com.mrnovacrm.b2b_dealer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.mrnovacrm.b2b_admin.EmpListActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.EmpDetailsDTO;
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

public class UpdateDelalerDetailsActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

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

    static EditText date_txt, validupto_txt, pesticideslicensevalidupto_txt, seedlicensevalidupto_txt;

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

    ImageView companyimage, contactimage, accountimagview, depositimage, licenceimage, personalimage;
    private String PRIMARYID, COMPANY, BRANCHID, BRANCHNAME;
    EditText seedlicense_txt;

    Context context, mContext;
    GlobalShare globalShare;
    private String IDVAL;
    String SHORTFROM = "";
    String TITLE = "";

    String state_idval = "";
    String district_idval = "";
    String country_idval = "";
    int COUNTRYSELECTEDPOS = 0;
    int STATESELECTEDPOS = 0;
    int DISTSELECTEDPOS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setTitle("Update Dealer");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        mContext = this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_dealerreg);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            COMPANY = values.get(SharedDB.COMPANY);
            BRANCHID = values.get(SharedDB.BRANCHID);
            BRANCHNAME = values.get(SharedDB.BRANCHNAME);
        }

        Bundle bundle = getIntent().getExtras();
        IDVAL = bundle.getString("id");
        SHORTFROM = bundle.getString("SHORTFROM");
        TITLE = bundle.getString("TITLE");

        companydetails_downarrow = findViewById(R.id.companydetails_downarrow);

        dealername_txt = findViewById(R.id.dealername_txt);
        dno_txt = findViewById(R.id.dno_txt);
        streetname_txt = findViewById(R.id.streetname_txt);
        landmark_txt = findViewById(R.id.landmark_txt);
        country_spinner = findViewById(R.id.country_spinner);
        state_spinner = findViewById(R.id.state_spinner);
        district_spinner = findViewById(R.id.district_spinner);
        towncity_txt = findViewById(R.id.towncity_txt);
        pincode_txt = findViewById(R.id.pincode_txt);
        gstinnumber_txt = findViewById(R.id.gstinnumber_txt);
        pannumber_txt = findViewById(R.id.pannumber_txt);
        brach_txt = findViewById(R.id.brach_txt);
        brach_txt.setText(BRANCHNAME);
        name_txt = findViewById(R.id.name_txt);
        marketingofccontactnumber_txt = findViewById(R.id.marketingofccontactnumber_txt);
        emailaddress_txt = findViewById(R.id.emailaddress_txt);
        headquater_txt = findViewById(R.id.headquater_txt);

        dealername_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        dno_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        streetname_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        landmark_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        towncity_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        pincode_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        gstinnumber_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        pannumber_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        name_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        marketingofccontactnumber_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        headquater_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        contacts_downarrow = findViewById(R.id.contacts_downarrow);
        dealercontactno_txt = findViewById(R.id.dealercontactno_txt);
        dealerwhatsapp_txt = findViewById(R.id.dealerwhatsapp_txt);
        alternativecontact_txt = findViewById(R.id.alternativecontact_txt);
        dealeremailid_txt = findViewById(R.id.dealeremailid_txt);


        dealercontactno_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        dealerwhatsapp_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        alternativecontact_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        accountsdetailsdownarrow = findViewById(R.id.accountsdetailsdownarrow);
        bankaccountnumber_txt = findViewById(R.id.bankaccountnumber_txt);
        bankname_txt = findViewById(R.id.bankname_txt);
        branch_txt = findViewById(R.id.branch_txt);
        ifsc_txt = findViewById(R.id.ifsc_txt);

        bankaccountnumber_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        bankname_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        branch_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ifsc_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        deposits_downarrow = findViewById(R.id.deposits_downarrow);
        amount_txt = findViewById(R.id.amount_txt);
        date_txt = findViewById(R.id.date_txt);

        licence_downarrow = findViewById(R.id.licence_downarrow);
        fertilizers_txt = findViewById(R.id.fertilizers_txt);
        validupto_txt = findViewById(R.id.validupto_txt);
        pesticideslicense_txt = findViewById(R.id.pesticideslicense_txt);
        pesticideslicensevalidupto_txt = findViewById(R.id.pesticideslicensevalidupto_txt);
        seedlicense_txt = findViewById(R.id.seedlicense_txt);
        seedlicensevalidupto_txt = findViewById(R.id.seedlicensevalidupto_txt);
        otherlicence_txt = findViewById(R.id.otherlicence_txt);

        fertilizers_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        validupto_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        pesticideslicense_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        pesticideslicensevalidupto_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        seedlicense_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        seedlicensevalidupto_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        otherlicence_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        personal_downarrow = findViewById(R.id.personal_downarrow);
        ownername_txt = findViewById(R.id.ownername_txt);
        ownwerdesingation_txt = findViewById(R.id.ownwerdesingation_txt);
        ownercontactnumber_txt = findViewById(R.id.ownercontactnumber_txt);
        keypersonname_txt = findViewById(R.id.keypersonname_txt);
        keyperson_contacttxt = findViewById(R.id.keyperson_contacttxt);
        keypersondesignation_txt = findViewById(R.id.keypersondesignation_txt);
        submitbtn = findViewById(R.id.submitbtn);

        ownername_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ownwerdesingation_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ownercontactnumber_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        keypersonname_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        keyperson_contacttxt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        keypersondesignation_txt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        submitbtn.setOnClickListener(UpdateDelalerDetailsActivity.this);
        companydetails_downarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);
        contacts_downarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);
        accountsdetailsdownarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);
        deposits_downarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);
        licence_downarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);
        personal_downarrow.setOnClickListener(UpdateDelalerDetailsActivity.this);

        companydetailsrealtive = findViewById(R.id.companydetailsrealtive);
        contactdetailsrelative = findViewById(R.id.contactdetailsrelative);
        accountdetailsrelative = findViewById(R.id.accountdetailsrelative);
        depositrealitve = findViewById(R.id.depositrealitve);
        licencedetailsrelative = findViewById(R.id.licencedetailsrelative);
        personaldetailsrelative = findViewById(R.id.personaldetailsrelative);
        keypersondesignation_txt = findViewById(R.id.keypersondesignation_txt);


        companyimage = findViewById(R.id.companyimage);
        contactimage = findViewById(R.id.contactimage);
        accountimagview = findViewById(R.id.accountimagview);
        depositimage = findViewById(R.id.depositimage);
        licenceimage = findViewById(R.id.licenceimage);
        personalimage = findViewById(R.id.personalimage);


        companydetailsrealtive.setVisibility(View.VISIBLE);
        contactdetailsrelative.setVisibility(View.GONE);
        accountdetailsrelative.setVisibility(View.GONE);
        depositrealitve.setVisibility(View.GONE);
        licencedetailsrelative.setVisibility(View.GONE);
        personaldetailsrelative.setVisibility(View.GONE);

        date_txt.setOnTouchListener(UpdateDelalerDetailsActivity.this);
        validupto_txt.setOnTouchListener(UpdateDelalerDetailsActivity.this);
        pesticideslicensevalidupto_txt.setOnTouchListener(UpdateDelalerDetailsActivity.this);
        seedlicensevalidupto_txt.setOnTouchListener(UpdateDelalerDetailsActivity.this);

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

        //   loadGraphicsData("country", "");

        getEMPList_Details();

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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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
                                        if (country_idval.equals(id)) {
                                            COUNTRYSELECTEDPOS = i + 1;
                                        }
                                    }
                                    if (type.equals("state")) {
                                        stateNamesList.add(name);
                                        stateIdsList.add(id);
                                        if (state_idval.equals(id)) {
                                            STATESELECTEDPOS = i + 1;
                                        }
                                    }
                                    if (type.equals("dist")) {
                                        distNamesList.add(name);
                                        distIdsList.add(id);
                                        if (district_idval.equals(id)) {
                                            DISTSELECTEDPOS = i + 1;
                                        }
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
                //    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showCountriesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setSelection(COUNTRYSELECTEDPOS);
        COUNTRYSELECTEDPOS = 0;
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setSelection(STATESELECTEDPOS);
        STATESELECTEDPOS = 0;
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        district_spinner.setAdapter(spinnerClass);
        district_spinner.setSelection(DISTSELECTEDPOS);
        DISTSELECTEDPOS = 0;
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
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {

            String GSTINPattern = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$";
            if (GSTINNUMBER.matches(GSTINPattern)) {
                Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                Matcher matcher = pattern.matcher(PAN);
                // Check if pattern matches
                if (matcher.matches()) {
                    final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
                    dialog.show();
                    RetrofitAPI mApiService = SharedDB.getInterfaceService();
                    Call<Login> mService = null;
                    mService = mApiService.updateDealerFealdDetails(OWNER_NAME, DEALERNAME, KEYPERSON_NAME, "", COUNTRY_ID,
                            STATE_ID, TOWNCITY,
                            "", PAN, GSTINNUMBER, "", BANKNAME, BANKACCOUT_NUMBER, IFSC, "", "",
                            KEYPERSON_CONTACT, "", PRIMARYID, "", OWNER_CONTACT,
                            DEALER_EMAILID, PRIMARYID, "DEALER", COMPANY, BRANCHID, "",
                            DNO, OWNER_DESIGNATION, DEALER_CONTACT, ALTERNATIVE_CONTACT, DEALER_WHATSAPP, STREETNAME,
                            LANDMARK, DISTRICT_ID, BRANCH, KEYPERSON_DESIGNATION,
                            PRESTICIDESLICENCE, PRESTICIDESLICENCEUPTO, FERTILIZERS, FERTILIZERSUPTO,
                            SEEDLICENCE, SEEDLICENCE_VALIDUPTO, OTHERLICENCE, IDVAL);
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
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                    if (EmpListActivity.mainfinish != null) {
                                        EmpListActivity.mainfinish.finish();
                                    }
                                    String branchidval = globalShare.getBranchidval();
                                    Intent intent = new Intent(getApplicationContext(), EmpListActivity.class);
                                    intent.putExtra("title", TITLE);
                                    intent.putExtra("branch", branchidval);
                                    intent.putExtra("shortform", SHORTFROM);
                                    startActivity(intent);

                                    finish();
                                } else {
                                    String message = mLoginObject.getMessage();
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid gstin number", Toast.LENGTH_SHORT).show();
            }


        }
    }


    private void getEMPList_Details() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;
        mService = mApiService.getDealerDetails(IDVAL);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Login mOrderObject = response.body();
                    String status = mOrderObject.getStatus();

                    Log.e("status", status);

                    if (Integer.parseInt(status) == 1) {
                        List<EmpDetailsDTO> empDetailsDTOS = mOrderObject.getEmpDetailsDTOS();

                        List<ContactsModelDTO> contactsModelDTOS = mOrderObject.getContactsModelDTOList();
                        if (contactsModelDTOS != null) {
                            if (contactsModelDTOS.size() > 0) {
                                globalShare.setContactsModelDTOS(contactsModelDTOS);

                                for (int i = 0; i < contactsModelDTOS.size(); i++) {
                                    String key_person = contactsModelDTOS.get(i).getKey_person();
                                    String ofc_contact = contactsModelDTOS.get(i).getOfc_contact();
                                    String ofc_email = contactsModelDTOS.get(i).getOfc_email();
                                    String ofc_whatsapp = contactsModelDTOS.get(i).getOfc_whatsapp();

                                    keypersonname_txt.setText(key_person);
                                    keyperson_contacttxt.setText(ofc_contact);
                                    keypersondesignation_txt.setText("");
                                }
                            }
                        }

                        if (empDetailsDTOS != null) {
                            if (empDetailsDTOS.size() > 0) {
                                globalShare.setDealerDetailsDTOS(empDetailsDTOS);

                                for (int i = 0; i < empDetailsDTOS.size(); i++) {
                                    String firstname = empDetailsDTOS.get(i).getFirst_name();
                                    String last_name = empDetailsDTOS.get(i).getLast_name();
                                    String uniq_id = empDetailsDTOS.get(i).getUniq_id();
                                    String branch = empDetailsDTOS.get(i).getBranch();
                                    String dept = empDetailsDTOS.get(i).getDept();
                                    String mobile = empDetailsDTOS.get(i).getMobile();
                                    String email = empDetailsDTOS.get(i).getEmail();
                                    String dob = empDetailsDTOS.get(i).getDob();
                                    String gender = empDetailsDTOS.get(i).getGender();
                                    String location = empDetailsDTOS.get(i).getLocation();
                                    String blood_group = empDetailsDTOS.get(i).getBlood_group();
                                    String father = empDetailsDTOS.get(i).getFather();
                                    String mother = empDetailsDTOS.get(i).getMother();
                                    String marital = empDetailsDTOS.get(i).getMarital();
                                    String expeience = empDetailsDTOS.get(i).getExpeience();
                                    String report_to = empDetailsDTOS.get(i).getReport_to();
                                    String doj = empDetailsDTOS.get(i).getDoj();
                                    String pan = empDetailsDTOS.get(i).getPan();
                                    String pf = empDetailsDTOS.get(i).getPf();
                                    String esi = empDetailsDTOS.get(i).getEsi();
                                    String bank_name = empDetailsDTOS.get(i).getBank_name();
                                    String bank_account = empDetailsDTOS.get(i).getBank_account();
                                    String ifsc = empDetailsDTOS.get(i).getIfsc();
                                    String address = empDetailsDTOS.get(i).getAddress();
                                    String addressproof1 = empDetailsDTOS.get(i).getAddressproof1();
                                    String addressproof2 = empDetailsDTOS.get(i).getAddressproof2();
                                    String role_id = empDetailsDTOS.get(i).getRole_id();
                                    String ofc_email = empDetailsDTOS.get(i).getOfc_email();
                                    String ofc_contact = empDetailsDTOS.get(i).getOfc_contact();
                                    String createdon = empDetailsDTOS.get(i).getCreatedon();
                                    String createdby = empDetailsDTOS.get(i).getCreatedby();
                                    String modifiedon = empDetailsDTOS.get(i).getModifiedon();
                                    String modifiedby = empDetailsDTOS.get(i).getModifiedby();
                                    String country = empDetailsDTOS.get(i).getCountry();
                                    String state = empDetailsDTOS.get(i).getState();
                                    String city = empDetailsDTOS.get(i).getCity();
                                    String pincode = empDetailsDTOS.get(i).getPincode();
                                    String gstin = empDetailsDTOS.get(i).getGstin();
                                    String company_name = empDetailsDTOS.get(i).getCompany_name();
                                    //  String key_person = empDetailsDTOS.get(i).getKey_person();
                                    String reg_type = empDetailsDTOS.get(i).getReg_type();
                                    //   String ofc_whatsapp = empDetailsDTOS.get(i).getOfc_whatsapp();
                                    String sales_manager = empDetailsDTOS.get(i).getSm();
                                    String am_contact = empDetailsDTOS.get(i).getAm_contact();
                                    String dealer_code = empDetailsDTOS.get(i).getDealer_code();
                                    String country_id = empDetailsDTOS.get(i).getCountry_id();
                                    String state_id = empDetailsDTOS.get(i).getState_id();
                                    String licence = empDetailsDTOS.get(i).getLicence();
                                    String area_sm = empDetailsDTOS.get(i).getSm();
                                    String designation = empDetailsDTOS.get(i).getDesignation();

                                    String fertilizer = empDetailsDTOS.get(i).getFertilizer();
                                    String fertilizer_upto = empDetailsDTOS.get(i).getFertilizer_upto();
                                    String seed = empDetailsDTOS.get(i).getSeed();
                                    String seed_upto = empDetailsDTOS.get(i).getSeed_upto();
                                    String pesticide = empDetailsDTOS.get(i).getPesticide();
                                    String pesticide_upto = empDetailsDTOS.get(i).getPesticide_upto();
                                    String other = empDetailsDTOS.get(i).getOther();
                                    String street_name = empDetailsDTOS.get(i).getStreet_name();
                                    String door_no = empDetailsDTOS.get(i).getDoor_no();
                                    String landmark = empDetailsDTOS.get(i).getLandmark();
                                    String district = empDetailsDTOS.get(i).getDistrict();
                                    String district_id = empDetailsDTOS.get(i).getDistrict_id();

                                    String company_name1 = empDetailsDTOS.get(i).getCompany_name();
                                    String contact1 = empDetailsDTOS.get(i).getContact1();
                                    String contact2 = empDetailsDTOS.get(i).getContact2();
                                    String whatsapp = empDetailsDTOS.get(i).getWhatsapp();

                                    String marketing_name = empDetailsDTOS.get(i).getMarketing_name();
                                    String marketing_contact = empDetailsDTOS.get(i).getMarketing_contact();
                                    String marketing_email = empDetailsDTOS.get(i).getMarketing_email();
                                    String marketing_hq = empDetailsDTOS.get(i).getMarketing_hq();

                                    String bank_branch = empDetailsDTOS.get(i).getBank_branch();
                                    String owner_desg = empDetailsDTOS.get(i).getOwner_desg();

                                    district_idval = district_id;


 //                                EditText dealername_txt, dno_txt, streetname_txt, landmark_txt, towncity_txt, pincode_txt,
                                    // gstinnumber_txt, pannumber_txt,
//                                        brach_txt, name_txt, marketingofccontactnumber_txt, emailaddress_txt, headquater_txt,
//                                        dealercontactno_txt, dealerwhatsapp_txt, alternativecontact_txt, dealeremailid_txt,
//                                        bankaccountnumber_txt, bankname_txt, branch_txt, ifsc_txt,
//                                        amount_txt,
//                                        fertilizers_txt, pesticideslicense_txt, otherlicence_txt,
//                                        ownername_txt, ownwerdesingation_txt, ownercontactnumber_txt, keypersonname_txt, keyperson_contacttxt,
//                                        keypersondesignation_txt;



                                    country_idval=country_id;
                                    state_idval=state_id;
                                    district_idval=district_id;

                                    dealername_txt.setText(firstname);
                                    dno_txt.setText(door_no);
                                    streetname_txt.setText(street_name);
                                    landmark_txt.setText(landmark);
                                    towncity_txt.setText(city);
                                    pincode_txt.setText(pincode);
                                    gstinnumber_txt.setText(gstin);
                                    pannumber_txt.setText(pan);
                                  //  branch_txt.setText("");
                                   name_txt.setText(marketing_name);

                                   dealercontactno_txt.setText("");
                                   dealeremailid_txt.setText("");
                                   dealerwhatsapp_txt.setText("");
                                   alternativecontact_txt.setText("");

                                   bankaccountnumber_txt.setText(bank_account);
                                   bankname_txt.setText(bank_name);
                                   branch_txt.setText(bank_branch);
                                   ifsc_txt.setText(ifsc);

                                    fertilizers_txt.setText(fertilizer);
                                    validupto_txt.setText(fertilizer_upto);

                                    pesticideslicense_txt.setText(pesticide);
                                    pesticideslicensevalidupto_txt.setText(pesticide_upto);

                                    seedlicense_txt.setText(seed);
                                    seedlicensevalidupto_txt.setText(seed_upto);

                                    otherlicence_txt.setText(other);

                                    ownername_txt.setText("");
                                    ownercontactnumber_txt.setText("");
                                    ownwerdesingation_txt.setText(owner_desg);
                                }
                                if (globalShare.getCountryid() != null) {
                                    country_idval = globalShare.getCountryid();
                                }

                                if (globalShare.getStateid() != null) {
                                    state_idval = globalShare.getStateid();
                                }
                                loadGraphicsData("country", "");
                            }
                        }

                    } else {
                        String messge = mOrderObject.getMessage();
                        Toast.makeText(getApplicationContext(), messge, Toast.LENGTH_SHORT).show();
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
}

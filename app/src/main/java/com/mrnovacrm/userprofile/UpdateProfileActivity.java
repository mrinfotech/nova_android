package com.mrnovacrm.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.DealerRegisrationDTO;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.EmpDetailsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.RecordsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    GlobalShare globalShare;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String SHORTFORM = "";
    private String USERNAME = "";

    private TransparentProgressDialog dialog;
    Spinner country_spinner,state_spinner;
    EditText dealername_txt,companyname_txt,keypersonname_txt,address_txt,towncity_txt,pincode_txt;
    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();

    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();

    String COUNTRY_ID = "";
    String STATE_ID = "";

    Context mContext;
    List<EmpDetailsDTO> empDetailsDTOS;

    EditText pannumber_txt, gstinnumber_txt, registraitontype_txt,
            bankname_txt, accountnumber_txt, ifsc_txt,licencennumber_txt;

    EditText dealerid_txt,officialemail_txt,officialcontact_txt,officialwhatsapp,areasalesmanager_txt,areamanagercontact_txt;
    List<ContactsModelDTO> contactsModelDTOS;

    EditText personalmobile_txt,personalemail_txt;

    String state_idval="";
    String district_idval="";
    String country_idval="";

    int COUNTRYSELECTEDPOS=0;
    int STATESELECTEDPOS=0;
    Button updatebtn;

    DealerRegisrationDTO dealerregdto;
    private String COMPANY_VAL="";
    private String BRANCHID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mContext=this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_updateprofile);
        View includedLayout = findViewById(R.id.include_actionbar);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        actionbarheadertxt.setText("Update Profile");

        backimg.setOnClickListener(UpdateProfileActivity.this);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            USERNAME = values.get(SharedDB.USERNAME);
            COMPANY_VAL = values.get(SharedDB.COMPANY);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        dealerregdto=new DealerRegisrationDTO();
        /* Perosonal details*/
        dealername_txt=findViewById(R.id.dealername_txt);
        companyname_txt=findViewById(R.id.companyname_txt);
        keypersonname_txt=findViewById(R.id.keypersonname_txt);
        address_txt=findViewById(R.id.address_txt);
        country_spinner=findViewById(R.id.country_spinner);
        state_spinner=findViewById(R.id.state_spinner);
        towncity_txt=findViewById(R.id.towncity_txt);
        pincode_txt=findViewById(R.id.pincode_txt);

        /* Account details*/
        pannumber_txt = findViewById(R.id.pannumber_txt);
        licencennumber_txt = findViewById(R.id.licencennumber_txt);
        gstinnumber_txt = findViewById(R.id.gstinnumber_txt);
        registraitontype_txt = findViewById(R.id.registraitontype_txt);
        bankname_txt = findViewById(R.id.bankname_txt);
        accountnumber_txt = findViewById(R.id.accountnumber_txt);
        ifsc_txt = findViewById(R.id.ifsc_txt);
        pannumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        dealerid_txt=findViewById(R.id.dealerid_txt);
        officialemail_txt=findViewById(R.id.officialemail_txt);
        officialcontact_txt=findViewById(R.id.officialcontact_txt);
        officialwhatsapp=findViewById(R.id.officialwhatsapp);
        areasalesmanager_txt=findViewById(R.id.areasalesmanager_txt);
        areamanagercontact_txt=findViewById(R.id.areamanagercontact_txt);

        personalmobile_txt=findViewById(R.id.personalmobile_txt);
        personalemail_txt=findViewById(R.id.personalemail_txt);

        updatebtn=findViewById(R.id.updatebtn);
        updatebtn.setOnClickListener(UpdateProfileActivity.this);

        if(globalShare.getCountryid()!=null)
        {
            country_idval=globalShare.getCountryid();
        }

        if(globalShare.getStateid()!=null)
        {
           state_idval= globalShare.getStateid();
        }


        hintCountries();
        hintStates();

        if(globalShare.getDealerDetailsDTOS()!=null)
        {
            empDetailsDTOS=globalShare.getDealerDetailsDTOS();
        }

        if(globalShare.getContactsModelDTOS()!=null)
        {
            contactsModelDTOS=globalShare.getContactsModelDTOS();
        }

        getEMPListDetails();
        loadGraphicsData("country","");
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.updatebtn:
                showValidations();
                break;
            case R.id.backimg:
                finish();
                break;
            default:
                break;
        }
    }

    private void getEMPListDetails() {
        if (empDetailsDTOS != null) {
            if (empDetailsDTOS.size() > 0) {
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
                    String area_sm = empDetailsDTOS.get(i).getSm();
                    String licence = empDetailsDTOS.get(i).getLicence();

                    if (SHORTFORM.equals("DEALER")) {

                        dealername_txt.setText(USERNAME);
                        companyname_txt.setText(company_name);
                        address_txt.setText(address);
                        towncity_txt.setText(city);
                        pincode_txt.setText(pincode);
                        pannumber_txt.setText(pan);
                        licencennumber_txt.setText(licence);
                        gstinnumber_txt.setText(gstin);
                        registraitontype_txt.setText(reg_type);
                        bankname_txt.setText(bank_name);
                        accountnumber_txt.setText(bank_account);
                        ifsc_txt.setText(ifsc);

                        dealerid_txt.setText(dealer_code);
                        areasalesmanager_txt.setText(sales_manager);
                        areamanagercontact_txt.setText(am_contact);

                        personalmobile_txt.setText(mobile);
                        personalemail_txt.setText(email);

                        areasalesmanager_txt.setText(area_sm);

                    } else {

                    }

                }
            }
        }

        if(contactsModelDTOS!=null)
        {
            if (contactsModelDTOS.size() > 0) {
                globalShare.setContactsModelDTOS(contactsModelDTOS);
                for (int i = 0; i < contactsModelDTOS.size(); i++) {
                    String key_person = contactsModelDTOS.get(i).getKey_person();
                    String ofc_contact = contactsModelDTOS.get(i).getOfc_contact();
                    String ofc_email = contactsModelDTOS.get(i).getOfc_email();
                    String ofc_whatsapp = contactsModelDTOS.get(i).getOfc_whatsapp();

                    keypersonname_txt.setText(key_person);
                    officialemail_txt.setText(ofc_email);
                    officialcontact_txt.setText(ofc_contact);
                    officialwhatsapp.setText(ofc_whatsapp);
                }
            }
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

    public void loadGraphicsData(final String type, final String id) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DemoGraphicsDTO> mService = null;
        if (type.equals("country")) {
            mService = mApiService.getCountries();
        } else if (type.equals("state")) {
            mService = mApiService.getStates(id);
        }else if (type.equals("dist")) {
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
                                }

                                for (int i = 0; i < demograRecordsDTOS.size(); i++) {
                                    String id = demograRecordsDTOS.get(i).getId();
                                    String name = demograRecordsDTOS.get(i).getName();
                                    if (type.equals("country")) {
                                        countryNamesList.add(name);
                                        countryIdsList.add(id);
                                        if(country_idval.equals(id))
                                        {
                                            COUNTRYSELECTEDPOS=i+1;
                                        }
                                    }

                                    if (type.equals("state")) {
                                        stateNamesList.add(name);
                                        stateIdsList.add(id);
                                        if(state_idval.equals(id))
                                        {
                                            STATESELECTEDPOS=i+1;
                                        }
                                    }
                                }
                                if (type.equals("country")) {
                                    showCountriesData();
                                }else if(type.equals("state"))
                                {
                                    showStatesData();
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
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
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
        COUNTRYSELECTEDPOS=0;

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    COUNTRY_ID = "";
                    hintStates();
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
        STATESELECTEDPOS=0;
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                } else {
                    STATE_ID =stateIdsList.get(position - 1);
                    //  loadGraphicsData("dist", STATE_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showValidations()
    {
        String DEALER_NAME=dealername_txt.getText().toString().trim();
        String COMPANY_NAME=companyname_txt.getText().toString().trim();
        String KEYPERSON_NAME=keypersonname_txt.getText().toString().trim();
        String ADDRESS_VAL=address_txt.getText().toString().trim();
        String TOWN=towncity_txt.getText().toString().trim();
        String PINCODE=pincode_txt.getText().toString().trim();

        String PAN_NUMBER = pannumber_txt.getText().toString().trim();
        String GSTIN_NUMBER = gstinnumber_txt.getText().toString().trim();
        String REGISTRATION_TYPE = registraitontype_txt.getText().toString().trim();
        String BANK_NAME = bankname_txt.getText().toString().trim();
        String ACCOUNT_NUMER = accountnumber_txt.getText().toString().trim();
        String IFSCCODE = ifsc_txt.getText().toString().trim();
        String LICENCENO = licencennumber_txt.getText().toString().trim();

        String DEALER_ID=dealerid_txt.getText().toString().trim();
        String OFFICIALEMAIL=officialemail_txt.getText().toString().trim();
        String OFFICIAL_CONTACT=officialcontact_txt.getText().toString().trim();
        String OFFICIAL_WHATSAPP=officialwhatsapp.getText().toString().trim();
        String AREA_SALESMANAGER=areasalesmanager_txt.getText().toString().trim();
        String AREA_MANAGER=areamanagercontact_txt.getText().toString().trim();
        String PERSONALMOBILE=personalmobile_txt.getText().toString();
        String PERSONALEMAIL=personalemail_txt.getText().toString();

        if (DEALER_NAME == null || "".equalsIgnoreCase(DEALER_NAME)|| DEALER_NAME.equals("")
                || COMPANY_NAME == null || "".equalsIgnoreCase(COMPANY_NAME)|| COMPANY_NAME.equals("")
                || KEYPERSON_NAME == null || "".equalsIgnoreCase(KEYPERSON_NAME) || KEYPERSON_NAME.equals("")
                || ADDRESS_VAL == null || "".equalsIgnoreCase(ADDRESS_VAL) || ADDRESS_VAL.equals("")
                || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID)|| COUNTRY_ID.equals("")
                || STATE_ID == null || "".equalsIgnoreCase(STATE_ID)|| STATE_ID.equals("")
                || TOWN == null || "".equalsIgnoreCase(TOWN)|| TOWN.equals("")
                || PINCODE == null || "".equalsIgnoreCase(PINCODE)|| PINCODE.equals("")
                || PAN_NUMBER == null || "".equalsIgnoreCase(PAN_NUMBER) || PAN_NUMBER.equals("")
                || GSTIN_NUMBER == null || "".equalsIgnoreCase(GSTIN_NUMBER) || GSTIN_NUMBER.equals("")
                || LICENCENO == null || "".equalsIgnoreCase(LICENCENO) || LICENCENO.equals("")
                || REGISTRATION_TYPE == null || "".equalsIgnoreCase(REGISTRATION_TYPE) || REGISTRATION_TYPE.equals("")
                || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
                || ACCOUNT_NUMER == null || "".equalsIgnoreCase(ACCOUNT_NUMER) || ACCOUNT_NUMER.equals("")
                || IFSCCODE == null || "".equalsIgnoreCase(IFSCCODE) || IFSCCODE.equals("")
                || DEALER_ID == null || "".equalsIgnoreCase(DEALER_ID)|| DEALER_ID.equals("")
                || OFFICIALEMAIL == null || "".equalsIgnoreCase(OFFICIALEMAIL)|| OFFICIALEMAIL.equals("")
                || OFFICIAL_CONTACT == null || "".equalsIgnoreCase(OFFICIAL_CONTACT) || OFFICIAL_CONTACT.equals("")
                || OFFICIAL_WHATSAPP == null || "".equalsIgnoreCase(OFFICIAL_WHATSAPP) || OFFICIAL_WHATSAPP.equals("")
                || AREA_SALESMANAGER == null || "".equalsIgnoreCase(AREA_SALESMANAGER)|| AREA_SALESMANAGER.equals("")
                || AREA_MANAGER == null || "".equalsIgnoreCase(AREA_MANAGER)|| AREA_MANAGER.equals("")
                || PERSONALMOBILE == null || "".equalsIgnoreCase(PERSONALMOBILE)|| PERSONALMOBILE.equals("")
                || PERSONALEMAIL == null || "".equalsIgnoreCase(PERSONALEMAIL)|| PERSONALEMAIL.equals("")
                ) {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            String pincodepattern="^[1-9][0-9]{5}$";
            if(PINCODE.matches(pincodepattern))
            {
                Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                Matcher matcher = pattern.matcher(PAN_NUMBER);
                // Check if pattern matches
                if (matcher.matches()) {
                    String GSTINPattern = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$";
                    if (GSTIN_NUMBER.matches(GSTINPattern)) {
                        submitDetailsWithRetorfit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid gstin number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Please enter valid pincode",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submitDetailsWithRetorfit() {


//        String DEALERNAME=dealerregdto.getDealerName();
//        String companyName=dealerregdto.getCompanyName();
//        String keypersonname=dealerregdto.getKeypersonname();
//        String address=dealerregdto.getAddress();
//        String country=dealerregdto.getCountry();
//        String state=dealerregdto.getState();
//        String town=dealerregdto.getTown();
//        String pincode=dealerregdto.getPincode();
//
//        String paNnumber=dealerregdto.getPANnumber();
//        String gstiNnumber=dealerregdto.getGSTINnumber();
//        String regastrtiontype=dealerregdto.getRegastrtiontype();
//        String bankName=dealerregdto.getBankName();
//        String accountnumber=dealerregdto.getAccountnumber();
//        String ifsCcode=dealerregdto.getIFSCcode();
//
//        String dealerID=dealerregdto.getDealerID();
//        String officialemail=dealerregdto.getOfficialemail();
//        String officialcontact=dealerregdto.getOfficialcontact();
//        String officialwhatsapp=dealerregdto.getOfficialwhatsapp();
//        String areaSalesManager=dealerregdto.getAreaSalesManager();
//        String areamanagercontactnumber=dealerregdto.getAreamanagercontactnumber();
//
//        String persnolmobile=dealerregdto.getPersnolmobile();
//        String persnolemail=dealerregdto.getPersnolemail();
//        String licencenumber=dealerregdto.getLicencenumber();


        String DEALER_NAME=dealername_txt.getText().toString().trim();
        String COMPANY_NAME=companyname_txt.getText().toString().trim();
        String KEYPERSON_NAME=keypersonname_txt.getText().toString().trim();
        String ADDRESS_VAL=address_txt.getText().toString().trim();
        String TOWN=towncity_txt.getText().toString().trim();
        String PINCODE=pincode_txt.getText().toString().trim();

        String PAN_NUMBER = pannumber_txt.getText().toString().trim();
        String GSTIN_NUMBER = gstinnumber_txt.getText().toString().trim();
        String REGISTRATION_TYPE = registraitontype_txt.getText().toString().trim();
        String BANK_NAME = bankname_txt.getText().toString().trim();
        String ACCOUNT_NUMER = accountnumber_txt.getText().toString().trim();
        String IFSCCODE = ifsc_txt.getText().toString().trim();
        String LICENCENO = licencennumber_txt.getText().toString().trim();

        String DEALER_ID=dealerid_txt.getText().toString().trim();
        String OFFICIALEMAIL=officialemail_txt.getText().toString().trim();
        String OFFICIAL_CONTACT=officialcontact_txt.getText().toString().trim();
        String OFFICIAL_WHATSAPP=officialwhatsapp.getText().toString().trim();
        String AREA_SALESMANAGER=areasalesmanager_txt.getText().toString().trim();
        String AREA_MANAGER=areamanagercontact_txt.getText().toString().trim();
        String PERSONALMOBILE=personalmobile_txt.getText().toString();
        String PERSONALEMAIL=personalemail_txt.getText().toString();

//        Log.e("DEALERNAME",DEALER_NAME);
//        Log.e("companyName",COMPANY_NAME);
//        Log.e("keypersonname",KEYPERSON_NAME);
//        Log.e("address",ADDRESS_VAL);
//        Log.e("country",COUNTRY_ID);
//        Log.e("state",STATE_ID);
//        Log.e("town",TOWN);
//        Log.e("pincode",PINCODE);
//        Log.e("paNnumber",PAN_NUMBER);
//        Log.e("gstiNnumber",GSTIN_NUMBER);
//        Log.e("regastrtiontype",REGISTRATION_TYPE);
//        Log.e("bankName",BANK_NAME);
//        Log.e("accountnumber",ACCOUNT_NUMER);
//        Log.e("ifsCcode",IFSCCODE);
//        Log.e("dealerID",DEALER_ID);
//        Log.e("officialemail",OFFICIALEMAIL);
//        Log.e("officialcontact",OFFICIAL_CONTACT);
//        Log.e("officialwhatsapp",OFFICIAL_WHATSAPP);
//        Log.e("areaSalesManager",PRIMARYID);
//        Log.e("areamaactnumber",AREA_MANAGER);
//        Log.e("persnolmobile",PERSONALMOBILE);
//        Log.e("persnolemail",PERSONALEMAIL);
//        Log.e("PRIMARYID",PRIMARYID);
//        Log.e("COMPANY_VAL",COMPANY_VAL);
//        Log.e("BRANCHID",BRANCHID);
//        Log.e("LICENCENO",LICENCENO);
//        Log.e("EMPLOYEE",PRIMARYID);

        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.updateDealer(
                DEALER_NAME,COMPANY_NAME,KEYPERSON_NAME,ADDRESS_VAL,COUNTRY_ID,STATE_ID,TOWN,
                PINCODE,PAN_NUMBER,GSTIN_NUMBER,REGISTRATION_TYPE,BANK_NAME,ACCOUNT_NUMER,IFSCCODE,
                DEALER_ID,OFFICIALEMAIL,
                OFFICIAL_CONTACT,OFFICIAL_WHATSAPP,PRIMARYID,AREA_MANAGER,PERSONALMOBILE,
                PERSONALEMAIL,PRIMARYID,"DEALER",COMPANY_VAL,BRANCHID,LICENCENO,PRIMARYID);
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

                        if(UserProfileActivity.mainfinish!=null)
                        {
                            UserProfileActivity.mainfinish.finish();
                        }

                        Intent intent=new Intent(getApplicationContext(),UserProfileActivity.class);
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
    }
}
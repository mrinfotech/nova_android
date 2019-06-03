package com.mrnovacrm.userprofile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.b2b_admin.EmpListActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceEmployeesListActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DealerRegisrationDTO;
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

public class UpdateEmployeeProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context context, mContext;
    GlobalShare globalShare;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String SHORTFORMVAL = "";
    private String USERNAME = "";
    DealerRegisrationDTO dealerregdto;
    private String COMPANY_VAL = "";
    private String BRANCHID = "";

    EditText employeename_txt, fathername_txt, mothername_txt, address_txt, towncity_txt, pincode_txt;
    public static EditText dobedittext, dojedittext;
    RadioGroup radioSex, maritalrgp;
    RadioButton radioMale, radioFemale, singlerb, marriedrb;
    Spinner country_spinner, state_spinner;
    Button nextbtn;
    private RadioButton radioSexButton, martialSexButton;

    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();

    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();

    String COUNTRY_ID = "";
    String STATE_ID = "";
    int COUNTRYSELECTEDPOS = 0;
    int STATESELECTEDPOS = 0;

    Button updatebtn;
    EditText pannumber_txt, pfnumber_txt, esinumber_txt,
            bankname_txt, accountnumber_txt, ifsc_txt;

    EditText empid_txt, department_txt, designation_txt, companyemail, companycontact_txt, totalexp_txt, reportingmanager_txt;
    EditText personalmobile_txt, personalemail_txt;
    private List<EmpDetailsDTO> empDetailsDTOS;
    String state_idval = "";
    // String district_idval="";
    String country_idval = "";
    private String IDVAL;
    String SHORTFROM="";
    String TITLE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mContext = this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_employeeupdateprofile);
        View includedLayout = findViewById(R.id.include_actionbar);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        actionbarheadertxt.setText("Update Employee");

        backimg.setOnClickListener(UpdateEmployeeProfileActivity.this);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            SHORTFORMVAL = values.get(SharedDB.SHORTFORM);
            USERNAME = values.get(SharedDB.USERNAME);
            COMPANY_VAL = values.get(SharedDB.COMPANY);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        Bundle bundle=getIntent().getExtras();
        IDVAL=bundle.getString("id");
        SHORTFROM= bundle.getString("SHORTFROM");
        TITLE= bundle.getString("TITLE");

        employeename_txt = findViewById(R.id.employeename_txt);
        dobedittext = findViewById(R.id.dob);
        fathername_txt = findViewById(R.id.fathername_txt);
        mothername_txt = findViewById(R.id.mothername_txt);
        address_txt = findViewById(R.id.address_txt);

        country_spinner = findViewById(R.id.country_spinner);
        state_spinner = findViewById(R.id.state_spinner);

        towncity_txt = findViewById(R.id.towncity_txt);
        pincode_txt = findViewById(R.id.pincode_txt);

        radioSex = findViewById(R.id.radioSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        maritalrgp = findViewById(R.id.maritalrgp);
        singlerb = findViewById(R.id.singlerb);
        marriedrb = findViewById(R.id.marriedrb);
        updatebtn = findViewById(R.id.updatebtn);

        updatebtn.setOnClickListener(UpdateEmployeeProfileActivity.this);

        pannumber_txt = findViewById(R.id.pannumber_txt);
        pfnumber_txt = findViewById(R.id.pfnumber_txt);
        esinumber_txt = findViewById(R.id.esinumber_txt);
        bankname_txt = findViewById(R.id.bankname_txt);
        accountnumber_txt = findViewById(R.id.accountnumber_txt);
        ifsc_txt = findViewById(R.id.ifsc_txt);

        empid_txt = findViewById(R.id.empid_txt);
        dojedittext = findViewById(R.id.date);
        department_txt = findViewById(R.id.department_txt);
        designation_txt = findViewById(R.id.designation_txt);
        companyemail = findViewById(R.id.companyemail);
        companycontact_txt = findViewById(R.id.companycontact_txt);
        totalexp_txt = findViewById(R.id.totalexp_txt);
        reportingmanager_txt = findViewById(R.id.reportingmanager_txt);

        personalmobile_txt = findViewById(R.id.personalmobile_txt);
        personalemail_txt = findViewById(R.id.personalemail_txt);

        dobedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.dob) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getSupportFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

        dojedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "toDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getSupportFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

//        if(globalShare.getCountryid()!=null)
//        {
//            country_idval=globalShare.getCountryid();
//        }
//
//        if(globalShare.getStateid()!=null)
//        {
//            state_idval= globalShare.getStateid();
//        }


        hintCountries();
        hintStates();

        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(UpdateEmployeeProfileActivity.this);
        if(isConnectedToInternet) {
            getEMPList_Details();
          }else{
        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
    }



    // loadGraphicsData("country","");
//        if(globalShare.getDealerDetailsDTOS()!=null)
//        {
//            empDetailsDTOS=globalShare.getDealerDetailsDTOS();
//            getEMPListDetails();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatebtn:
                showvalidation();
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
                    String designation = empDetailsDTOS.get(i).getDesignation();

                    if (gender.equals("Male")) {
                        radioMale.setChecked(true);
                        radioFemale.setChecked(false);
                    } else {
                        radioFemale.setChecked(true);
                        radioMale.setChecked(false);
                    }

                    if (marital.equals("Married")) {
                        marriedrb.setChecked(true);
                        singlerb.setChecked(false);
                    } else {
                        singlerb.setChecked(true);
                        marriedrb.setChecked(false);
                    }

                    employeename_txt.setText(firstname);
                    dobedittext.setText(dob);
                    fathername_txt.setText(father);
                    mothername_txt.setText(mother);
                    address_txt.setText(address);
                    towncity_txt.setText(city);
                    pannumber_txt.setText(pan);
                    pfnumber_txt.setText(pf);
                    esinumber_txt.setText(esi);
                    bankname_txt.setText(bank_name);
                    accountnumber_txt.setText(bank_account);
                    ifsc_txt.setText(ifsc);
                    empid_txt.setText(uniq_id);
                    dojedittext.setText(doj);
                    department_txt.setText(dept);
                    designation_txt.setText(designation);
                    companyemail.setText("");
                    companycontact_txt.setText("");
                    totalexp_txt.setText(expeience);
                    reportingmanager_txt.setText(report_to);
                    personalemail_txt.setText(email);
                    personalmobile_txt.setText(mobile);
                }
            }
        }
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
                }
                if (type.equals("toDate")) {
                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        dobedittext.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };

        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        dojedittext.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    public void showvalidation() {
        int selectedId = radioSex.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        String genderval = radioSexButton.getText().toString();

        int martialselectedId = maritalrgp.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        martialSexButton = (RadioButton) findViewById(martialselectedId);
        String martialval = martialSexButton.getText().toString();

        String EMPLOYEE_NAME = employeename_txt.getText().toString().trim();
        String DOB_VAL = dobedittext.getText().toString().trim();

        String FATHER_NAME = fathername_txt.getText().toString().trim();
        String MOTHER_NAME = mothername_txt.getText().toString().trim();
        String ADDRESS_VAL = address_txt.getText().toString().trim();

        String TOWN = towncity_txt.getText().toString().trim();
        String PINCODE = pincode_txt.getText().toString().trim();

        String PAN_NUMBER = pannumber_txt.getText().toString().trim();
        String PF_NUMBER = pfnumber_txt.getText().toString().trim();
        String ESI_NUMBER = esinumber_txt.getText().toString().trim();
        String BANK_NAME = bankname_txt.getText().toString().trim();
        String ACCOUNT_NUMER = accountnumber_txt.getText().toString().trim();
        String IFSCCODE = ifsc_txt.getText().toString().trim();

        String EMP_ID = empid_txt.getText().toString().trim();
        String DOJVAL = dojedittext.getText().toString().trim();
        String DEPARTMENT = department_txt.getText().toString().trim();
        String DESIGNATION = designation_txt.getText().toString().trim();
        String COMPANYEMAIL = companyemail.getText().toString().trim();
        String COMPANYCONTACT = companycontact_txt.getText().toString().trim();
        String TOTALEXP = totalexp_txt.getText().toString().trim();
        String REPORTINGMANAGER = reportingmanager_txt.getText().toString().trim();

        String PERSONALMOBILE = personalmobile_txt.getText().toString();
        String PERSONALEMAIL = personalemail_txt.getText().toString();


        if (EMPLOYEE_NAME == null || "".equalsIgnoreCase(EMPLOYEE_NAME) || EMPLOYEE_NAME.equals("")
                || DOB_VAL == null || "".equalsIgnoreCase(DOB_VAL) || DOB_VAL.equals("")
                || genderval == null || "".equalsIgnoreCase(genderval) || genderval.equals("")
                || martialval == null || "".equalsIgnoreCase(martialval) || martialval.equals("")
                || FATHER_NAME == null || "".equalsIgnoreCase(FATHER_NAME) || FATHER_NAME.equals("")
                || MOTHER_NAME == null || "".equalsIgnoreCase(MOTHER_NAME) || MOTHER_NAME.equals("")
                || ADDRESS_VAL == null || "".equalsIgnoreCase(ADDRESS_VAL) || ADDRESS_VAL.equals("")
                || TOWN == null || "".equalsIgnoreCase(TOWN) || TOWN.equals("")
                || PINCODE == null || "".equalsIgnoreCase(PINCODE) || PINCODE.equals("")
                || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID) || COUNTRY_ID.equals("")
                || STATE_ID == null || "".equalsIgnoreCase(STATE_ID) || STATE_ID.equals("")
                || PAN_NUMBER == null || "".equalsIgnoreCase(PAN_NUMBER) || PAN_NUMBER.equals("")
                || ESI_NUMBER == null || "".equalsIgnoreCase(ESI_NUMBER) || ESI_NUMBER.equals("")
                || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
                || ACCOUNT_NUMER == null || "".equalsIgnoreCase(ACCOUNT_NUMER) || ACCOUNT_NUMER.equals("")
                || IFSCCODE == null || "".equalsIgnoreCase(IFSCCODE) || IFSCCODE.equals("")
                || EMP_ID == null || "".equalsIgnoreCase(EMP_ID) || EMP_ID.equals("")
                || DOJVAL == null || "".equalsIgnoreCase(DOJVAL) || DOJVAL.equals("")
                || DEPARTMENT == null || "".equalsIgnoreCase(DEPARTMENT) || DEPARTMENT.equals("")
                || DESIGNATION == null || "".equalsIgnoreCase(DESIGNATION) || DESIGNATION.equals("")
                || COMPANYEMAIL == null || "".equalsIgnoreCase(COMPANYEMAIL) || COMPANYEMAIL.equals("")
                || COMPANYCONTACT == null || "".equalsIgnoreCase(COMPANYCONTACT) || COMPANYCONTACT.equals("")
                || TOTALEXP == null || "".equalsIgnoreCase(TOTALEXP) || TOTALEXP.equals("")
                || REPORTINGMANAGER == null || "".equalsIgnoreCase(REPORTINGMANAGER) || REPORTINGMANAGER.equals("")
                || PERSONALMOBILE == null || "".equalsIgnoreCase(PERSONALMOBILE) || PERSONALMOBILE.equals("")
                || PERSONALEMAIL == null || "".equalsIgnoreCase(PERSONALEMAIL) || PERSONALEMAIL.equals("")) {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {

            String pincodepattern = "^[1-9][0-9]{5}$";
            if (PINCODE.matches(pincodepattern)) {
                Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                Matcher matcher = pattern.matcher(PAN_NUMBER);
                // Check if pattern matches
                if (matcher.matches()) {
                    submitDetailsWithRetorfit();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid pincode", Toast.LENGTH_SHORT).show();
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
                                }
                                if (type.equals("country")) {
                                    showCountriesData();
                                } else if (type.equals("state")) {
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
        COUNTRYSELECTEDPOS = 0;
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
                    //  loadGraphicsData("dist", STATE_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void submitDetailsWithRetorfit() {
        int selectedId = radioSex.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        String genderval = radioSexButton.getText().toString();

        int martialselectedId = maritalrgp.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        martialSexButton = (RadioButton) findViewById(martialselectedId);
        String martialval = martialSexButton.getText().toString();

        String EMPLOYEE_NAME = employeename_txt.getText().toString().trim();
        String DOB_VAL = dobedittext.getText().toString().trim();

        String FATHER_NAME = fathername_txt.getText().toString().trim();
        String MOTHER_NAME = mothername_txt.getText().toString().trim();
        String ADDRESS_VAL = address_txt.getText().toString().trim();

        String TOWN = towncity_txt.getText().toString().trim();
        String PINCODE = pincode_txt.getText().toString().trim();

        String PAN_NUMBER = pannumber_txt.getText().toString().trim();
        String PF_NUMBER = pfnumber_txt.getText().toString().trim();
        String ESI_NUMBER = esinumber_txt.getText().toString().trim();
        String BANK_NAME = bankname_txt.getText().toString().trim();
        String ACCOUNT_NUMER = accountnumber_txt.getText().toString().trim();
        String IFSCCODE = ifsc_txt.getText().toString().trim();

        String EMP_ID = empid_txt.getText().toString().trim();
        String DOJVAL = dojedittext.getText().toString().trim();
        String DEPARTMENT = department_txt.getText().toString().trim();
        String DESIGNATION = designation_txt.getText().toString().trim();
        String COMPANYEMAIL = companyemail.getText().toString().trim();
        String COMPANYCONTACT = companycontact_txt.getText().toString().trim();
        String TOTALEXP = totalexp_txt.getText().toString().trim();
        String REPORTINGMANAGER = reportingmanager_txt.getText().toString().trim();

        String PERSONALMOBILE = personalmobile_txt.getText().toString();
        String PERSONALEMAIL = personalemail_txt.getText().toString();

//        Log.e("EMPLOYEE_NAME",EMPLOYEE_NAME);
//        Log.e("DOB_VAL",DOB_VAL);
//        Log.e("genderval",genderval);
//        Log.e("FATHER_NAME",FATHER_NAME);
//        Log.e("MOTHER_NAME",MOTHER_NAME);
//        Log.e("martialval",martialval);
//        Log.e("address",ADDRESS_VAL);
//        Log.e("COUNTRY_ID",COUNTRY_ID);
//        Log.e("STATE_ID",STATE_ID);
//        Log.e("TOWN",TOWN);
//        Log.e("PINCODE",PINCODE);
//        Log.e("PAN_NUMBER",PAN_NUMBER);
//        Log.e("PF_NUMBER",PF_NUMBER);
//        Log.e("ESI_NUMBER",ESI_NUMBER);
//        Log.e("BANK_NAME",BANK_NAME);
//        Log.e("ACCOUNT_NUMER",ACCOUNT_NUMER);
//        Log.e("IFSCCODE",IFSCCODE);
//        Log.e("EMP_ID",EMP_ID);
//        Log.e("DOJVAL",DOJVAL);
//        Log.e("DEPARTMENT",DEPARTMENT);
//        Log.e("DESIGNATION",DESIGNATION);
//        Log.e("COMPANYEMAIL",COMPANYEMAIL);
//        Log.e("COMPANYCONTACT",COMPANYCONTACT);
//        Log.e("TOTALEXP",TOTALEXP);
//        Log.e("REPORTINGMANAGER",REPORTINGMANAGER);
//        Log.e("PERSONALMOBILE",PERSONALMOBILE);
//        Log.e("PERSONALEMAIL",PERSONALEMAIL);
//        Log.e("PRIMARYID",PRIMARYID);
//        Log.e("SHORTFROM",SHORTFROM);
//        Log.e("COMPANY_VAL",COMPANY_VAL);
//        Log.e("BRANCHID",BRANCHID);
//        Log.e("employee",IDVAL);

        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

         Call<Login> mService = mApiService.updateEmployee(
                EMPLOYEE_NAME, DOB_VAL, genderval, FATHER_NAME, MOTHER_NAME, martialval,
                ADDRESS_VAL, COUNTRY_ID, STATE_ID, TOWN,
                PINCODE, PAN_NUMBER, PF_NUMBER, ESI_NUMBER, BANK_NAME, ACCOUNT_NUMER, IFSCCODE,
                EMP_ID, DOJVAL, DEPARTMENT, DESIGNATION, COMPANYEMAIL, COMPANYCONTACT,
                TOTALEXP, REPORTINGMANAGER, PERSONALMOBILE,
                PERSONALEMAIL, PRIMARYID, SHORTFROM, COMPANY_VAL, BRANCHID, IDVAL);

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

                        if(SHORTFORMVAL.equals("ADMIN") || SHORTFORMVAL.equals("SA"))
                        {
                            if (EmpListActivity.mainfinish != null) {
                                EmpListActivity.mainfinish.finish();
                            }
                            String branchidval=globalShare.getBranchidval();
                            Intent intent=new Intent(getApplicationContext(),EmpListActivity.class);
                            intent.putExtra("title",TITLE);
                            intent.putExtra("branch",branchidval);
                            intent.putExtra("shortform",SHORTFROM);
                            startActivity(intent);
                        }else {
                            if (FinanceEmployeesListActivity.mainfinish != null) {
                                FinanceEmployeesListActivity.mainfinish.finish();
                            }
                            Intent intent = new Intent(getApplicationContext(), FinanceEmployeesListActivity.class);
                            intent.putExtra("SHORTFROM", SHORTFROM);
                            intent.putExtra("TITLE", TITLE);
                            startActivity(intent);
                        }
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


    private void getEMPList_Details() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(UpdateEmployeeProfileActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;
            mService = mApiService.getEMPDetails(IDVAL);
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
                        empDetailsDTOS= mOrderObject.getEmpDetailsDTOS();
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

                                    globalShare.setCountryid(country_id);
                                    globalShare.setStateid(state_id);

                                    if (gender.equals("Male")) {
                                        radioMale.setChecked(true);
                                        radioFemale.setChecked(false);
                                    } else {
                                        radioFemale.setChecked(true);
                                        radioMale.setChecked(false);
                                    }

                                    if (marital.equals("Married")) {
                                        marriedrb.setChecked(true);
                                        singlerb.setChecked(false);
                                    } else {
                                        singlerb.setChecked(true);
                                        marriedrb.setChecked(false);
                                    }

                                    employeename_txt.setText(firstname);
                                    dobedittext.setText(dob);
                                    fathername_txt.setText(father);
                                    mothername_txt.setText(mother);
                                    address_txt.setText(address);
                                    towncity_txt.setText(city);
                                    pannumber_txt.setText(pan);
                                    pfnumber_txt.setText(pf);
                                    esinumber_txt.setText(esi);
                                    bankname_txt.setText(bank_name);
                                    accountnumber_txt.setText(bank_account);
                                    ifsc_txt.setText(ifsc);
                                    empid_txt.setText(uniq_id);
                                    dojedittext.setText(doj);
                                    department_txt.setText(dept);
                                    designation_txt.setText(designation);
                                    companyemail.setText(ofc_email);
                                    companycontact_txt.setText(ofc_contact);
                                    totalexp_txt.setText(expeience);
                                    reportingmanager_txt.setText(report_to);
                                    personalemail_txt.setText(email);
                                    personalmobile_txt.setText(mobile);
                                    pincode_txt.setText(pincode);
                                }

                                if (globalShare.getCountryid() != null) {
                                    country_idval = globalShare.getCountryid();
                                }

                                if (globalShare.getStateid() != null) {
                                    state_idval = globalShare.getStateid();
                                }
                                loadGraphicsData("country","");
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
}
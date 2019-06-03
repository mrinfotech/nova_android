package com.mrnovacrm.b2b_finance_dept;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.EmployeeRegistrationDTO;
import com.mrnovacrm.model.RecordsDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageEmployeeDeatailsFragment extends Fragment {

    EditText employeename_txt,fathername_txt,mothername_txt,address_txt,towncity_txt,pincode_txt;
    public static EditText date;
    RadioGroup radioSex,maritalrgp;
    RadioButton radioMale,radioFemale,singlerb,marriedrb;
    Spinner country_spinner,state_spinner;
    Button nextbtn;
    private RadioButton radioSexButton,martialSexButton;

    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();

    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();

    String COUNTRY_ID = "";
    String STATE_ID = "";
    Context mContext;
    int COUNTRYSELECTEDPOS=0;
    int STATESELECTEDPOS=0;
    private String fromval;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.layout_manageemployeedeatails, container, false);

        mContext=getActivity();
        setHasOptionsMenu(true);

        fromval = getArguments().getString("fromval");
        title = getArguments().getString("title");

        employeename_txt=rootView.findViewById(R.id.employeename_txt);
        date=rootView.findViewById(R.id.date);
        fathername_txt=rootView.findViewById(R.id.fathername_txt);
        mothername_txt=rootView.findViewById(R.id.mothername_txt);
        address_txt=rootView.findViewById(R.id.address_txt);

        country_spinner=rootView.findViewById(R.id.country_spinner);
        state_spinner=rootView.findViewById(R.id.state_spinner);

        towncity_txt=rootView.findViewById(R.id.towncity_txt);
        pincode_txt=rootView.findViewById(R.id.pincode_txt);

        radioSex=rootView.findViewById(R.id.radioSex);
        radioMale=rootView.findViewById(R.id.radioMale);
        radioFemale=rootView.findViewById(R.id.radioFemale);

        maritalrgp=rootView.findViewById(R.id.maritalrgp);
        singlerb=rootView.findViewById(R.id.singlerb);
        marriedrb=rootView.findViewById(R.id.marriedrb);

        nextbtn=rootView.findViewById(R.id.nextbtn);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getFragmentManager(), "Date Picker");
                }
                return true;
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioSex.getCheckedRadioButtonId();
                radioSexButton = (RadioButton)rootView.findViewById(selectedId);
                String genderval=radioSexButton.getText().toString();

                int martialselectedId = maritalrgp.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                martialSexButton = (RadioButton)rootView.findViewById(martialselectedId);
                String martialval=martialSexButton.getText().toString();

                String EMPLOYEE_NAME=employeename_txt.getText().toString().trim();
                String DOB_VAL=date.getText().toString().trim();

                String FATHER_NAME=fathername_txt.getText().toString().trim();
                String MOTHER_NAME=mothername_txt.getText().toString().trim();
                String ADDRESS_VAL=address_txt.getText().toString().trim();

                String TOWN=towncity_txt.getText().toString().trim();
                String PINCODE=pincode_txt.getText().toString().trim();

                if (EMPLOYEE_NAME == null || "".equalsIgnoreCase(EMPLOYEE_NAME)|| EMPLOYEE_NAME.equals("")
                        || DOB_VAL == null || "".equalsIgnoreCase(DOB_VAL)|| DOB_VAL.equals("")
                        || genderval == null || "".equalsIgnoreCase(genderval) || genderval.equals("")
                        || martialval == null || "".equalsIgnoreCase(martialval) || martialval.equals("")
                        || FATHER_NAME == null || "".equalsIgnoreCase(FATHER_NAME)|| FATHER_NAME.equals("")
                        || MOTHER_NAME == null || "".equalsIgnoreCase(MOTHER_NAME)|| MOTHER_NAME.equals("")
                        || ADDRESS_VAL == null || "".equalsIgnoreCase(ADDRESS_VAL)|| ADDRESS_VAL.equals("")
                        || TOWN == null || "".equalsIgnoreCase(TOWN)|| TOWN.equals("")
                        || PINCODE == null || "".equalsIgnoreCase(PINCODE)|| PINCODE.equals("")
                        || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID)|| COUNTRY_ID.equals("")
                        || STATE_ID == null || "".equalsIgnoreCase(STATE_ID)|| STATE_ID.equals("")) {
                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {

                    String pincodepattern="^[1-9][0-9]{5}$";
                    if(PINCODE.matches(pincodepattern))
                    {
                        EmployeeRegistrationDTO dto=new EmployeeRegistrationDTO();
                        dto.setEmployeename(EMPLOYEE_NAME);
                        dto.setDOB(DOB_VAL);
                        dto.setGender(genderval);
                        dto.setFathername(FATHER_NAME);
                        dto.setMothername(MOTHER_NAME);
                        dto.setMartialstatus(martialval);
                        dto.setAddress(ADDRESS_VAL);
                        dto.setCountry(COUNTRY_ID);
                        dto.setState(STATE_ID);
                        dto.setTown(TOWN);
                        dto.setPincode(PINCODE);

                        Intent intent=new Intent(getActivity(),ManageEmployeeAccountActivity.class);
                        intent.putExtra("empregdto",dto);
                        intent.putExtra("fromval",fromval);
                        intent.putExtra("title",title);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(),"Please enter valid pincode",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        hintCountries();
        hintStates();


        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            try{
                loadGraphicsData("country","");
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        return rootView;
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
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        date.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
                                            }
                                            if (type.equals("state")) {
                                                stateNamesList.add(name);
                                                stateIdsList.add(id);
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
                        //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_superadmin, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet)
                {
                    try{
                        Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
                        intent.putExtra("SHORTFROM",fromval);
                        intent.putExtra("TITLE",title);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
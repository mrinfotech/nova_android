package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.model.EmployeeRegistrationDTO;

import java.util.Calendar;

public class ManageEmployeeCompanyActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainfinish;
    private Button nextbtn;
    private EmployeeRegistrationDTO empregdto;
    public static EditText date;
    EditText empid_txt,department_txt,designation_txt,companyemail,companycontact_txt,totalexp_txt,reportingmanager_txt;
    private String fromval;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        setTheme(R.style.AppTheme);
      //  setTitle("Manage Dealer");
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        fromval=bundle.getString("fromval");
        title=bundle.getString("title");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.layout_manageemployeecompany);

        Intent intent = getIntent();
        empregdto = (EmployeeRegistrationDTO) intent.getSerializableExtra("empregdto");



        empid_txt=findViewById(R.id.empid_txt);
        date=findViewById(R.id.date);
        department_txt=findViewById(R.id.department_txt);
        designation_txt=findViewById(R.id.designation_txt);
        companyemail=findViewById(R.id.companyemail);
        companycontact_txt=findViewById(R.id.companycontact_txt);
        totalexp_txt=findViewById(R.id.totalexp_txt);
        reportingmanager_txt=findViewById(R.id.reportingmanager_txt);

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageEmployeeCompanyActivity.this);

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.date) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getSupportFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextbtn:
                String EMP_ID=empid_txt.getText().toString().trim();
                String DOJ=date.getText().toString().trim();
                String DEPARTMENT=department_txt.getText().toString().trim();
                String DESIGNATION=designation_txt.getText().toString().trim();
                String COMPANYEMAIL=companyemail.getText().toString().trim();
                String COMPANYCONTACT=companycontact_txt.getText().toString().trim();
                String TOTALEXP=totalexp_txt.getText().toString().trim();
                String REPORTINGMANAGER=reportingmanager_txt.getText().toString().trim();

                if (DOJ == null || "".equalsIgnoreCase(DOJ)|| DOJ.equals("")
                        || DEPARTMENT == null || "".equalsIgnoreCase(DEPARTMENT) || DEPARTMENT.equals("")
                        || DESIGNATION == null || "".equalsIgnoreCase(DESIGNATION) || DESIGNATION.equals("")
                        || COMPANYEMAIL == null || "".equalsIgnoreCase(COMPANYEMAIL)|| COMPANYEMAIL.equals("")
                        || COMPANYCONTACT == null || "".equalsIgnoreCase(COMPANYCONTACT)|| COMPANYCONTACT.equals("")
                        || TOTALEXP == null || "".equalsIgnoreCase(TOTALEXP)|| TOTALEXP.equals("")
                        || REPORTINGMANAGER == null || "".equalsIgnoreCase(REPORTINGMANAGER)|| REPORTINGMANAGER.equals("")
                        ) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    empregdto.setEmployeeID(EMP_ID);
                    empregdto.setDOJ(DOJ);
                    empregdto.setDepartment(DEPARTMENT);
                    empregdto.setDesignation(DESIGNATION);
                    empregdto.setCompanyemail(COMPANYEMAIL);
                    empregdto.setCompanycontact(COMPANYCONTACT);
                    empregdto.setTotalExperience(TOTALEXP);
                    empregdto.setReportingManager(REPORTINGMANAGER);

                   // Intent intent=new Intent(getApplicationContext(),ManageDealersPersonalcontact.class);
                    Intent intent=new Intent(getApplicationContext(),ManageEmployeePersonalcontact.class);
                    intent.putExtra("empregdto",empregdto);
                    intent.putExtra("fromval",fromval);
                    intent.putExtra("title",title);
                    startActivity(intent);
                }

//                Intent intent =new Intent(getApplicationContext(),ManageDealersPersonalcontact.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
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

}

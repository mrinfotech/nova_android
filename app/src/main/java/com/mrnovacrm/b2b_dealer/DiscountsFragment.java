package com.mrnovacrm.b2b_dealer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.AutoCompleteCompanyAdapter;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.CompaniesDTO;
import com.mrnovacrm.model.DealersDTO;
import com.mrnovacrm.model.DealersRecordListDTO;
import com.mrnovacrm.model.EmployeesDiscountsDTO;
import com.mrnovacrm.model.EmployeesRecordListDTO;
import com.mrnovacrm.model.ProductsDiscountsDTO;
import com.mrnovacrm.model.ProductsRecordListDTO;
import com.mrnovacrm.model.RecordListDTO;
import com.mrnovacrm.model.ResultsDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harish on 6/24/2019.
 */

public class DiscountsFragment extends Fragment implements View.OnTouchListener {

    AutoCompleteTextView companySpinner, dealerSpinner, productSpinner, referenceSpinner;
    EditText discountText;
    @SuppressLint("StaticFieldLeak")
    public static EditText edtxt_fromdate, edtxt_todate;
    Button buttonSubmit;
    String companyId = "";

    List<RecordListDTO> companyRecordList;
    List<DealersRecordListDTO> dealersRecordList;
    List<ProductsRecordListDTO> productsRecordList;
    List<EmployeesRecordListDTO> employeesRecordList;
    ArrayList<String> companyNames, dealerNameList, productsNameList, employeesNameList;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_disocunts, container, false);


        companySpinner = rootView.findViewById(R.id.company_spinner);
        dealerSpinner = rootView.findViewById(R.id.dealer_spinner);
        productSpinner = rootView.findViewById(R.id.product_spinner);
        referenceSpinner = rootView.findViewById(R.id.reference_spinner);
        discountText = rootView.findViewById(R.id.discount_txt);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);

        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(DiscountsFragment.this);
        edtxt_todate.setOnTouchListener(DiscountsFragment.this);

        getDiscountCompany();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (companySpinner.getText().length() > 0) {
                    if (dealerSpinner.getText().length() > 0) {
                        if (productSpinner.getText().length() > 0) {
                            if (referenceSpinner.getText().length() > 0) {
                                if (discountText.getText().length() > 0) {
                                    if (edtxt_fromdate.getText().length() > 0) {
                                        if (edtxt_todate.getText().length() > 0) {
                                            sendDiscountRequest();
                                        } else {
                                            Toast.makeText(getActivity(), "To Date", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "From Date", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    discountText.setError("Enter discount");
                                }
                            } else {
                                Toast.makeText(getActivity(), "Select Employee", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Select Products", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Select Dealer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Select Company", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    public void getDiscountCompany() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<CompaniesDTO> mService = mApiService.getDiscounts();

        mService.enqueue(new Callback<CompaniesDTO>() {
            @Override
            public void onResponse(@NonNull Call<CompaniesDTO> call, @NonNull Response<CompaniesDTO> response) {
                dialog.dismiss();
                try {
                    CompaniesDTO mOrderObject = response.body();
                    assert mOrderObject != null;
                    int status = mOrderObject.getStatus();
                    if (status == 1) {
                        companyRecordList = mOrderObject.getRecordList();
                        if (companyRecordList != null) {
                            if (companyRecordList.size() > 0) {

                                companyNames = new ArrayList<>();

                                for (int i = 0; i < companyRecordList.size(); i++) {
                                    companyNames.add(companyRecordList.get(i).getCompany());
                                }

                                /*CompanyAdapter adapter = new CompanyAdapter(getActivity(), companyNames);
                                companySpinner.setAdapter(adapter);
*/
                                /* need to implement this functionality for products, dealers, employees*/
                                AutoCompleteCompanyAdapter adapter = new AutoCompleteCompanyAdapter(getActivity(),
                                        R.layout.searchautocomplete, R.id.searchtextview, companyNames);
                                companySpinner.setAdapter(adapter);


                                companySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                                        companyId = companyRecordList.get(pos).getCompanyId();

                                        getDealersList(companyRecordList.get(pos).getCompanyId());
                                        getProductsList(companyRecordList.get(pos).getCompanyId());
                                        getEmployeesList(companyRecordList.get(pos).getCompanyId());
                                    }
                                });


                                /*companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                        if (!companySpinner.getSelectedItem().equals("Select Company")) {

                                            companyId = companyRecordList.get(pos - 1).getCompanyId();

                                            getDealersList(companyRecordList.get(pos - 1).getCompanyId());
                                            getProductsList(companyRecordList.get(pos - 1).getCompanyId());
                                            getEmployeesList(companyRecordList.get(pos - 1).getCompanyId());

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });*/


                            }
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompaniesDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDealersList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DealersDTO> mService = mApiService.getDiscountsDealers(companyId);

        mService.enqueue(new Callback<DealersDTO>() {
            @Override
            public void onResponse(@NonNull Call<DealersDTO> call, @NonNull Response<DealersDTO> response) {
                DealersDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        dealersRecordList = mstoresObject.getRecordList();


                        dealerNameList = new ArrayList<>();
                        dealerNameList.add("Select Dealer");

                        for (int i = 0; i < dealersRecordList.size(); i++) {
                            dealerNameList.add(dealersRecordList.get(i).getName());
                        }


                        /*CompanyAdapter adapter = new CompanyAdapter(getActivity(), dealerNameList);
                        dealerSpinner.setAdapter(adapter);*/

                        AutoCompleteCompanyAdapter adapter = new AutoCompleteCompanyAdapter(getActivity(),
                                R.layout.searchautocomplete, R.id.searchtextview, dealerNameList);
                        dealerSpinner.setAdapter(adapter);


                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DealersDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductsList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<ProductsDiscountsDTO> mService = mApiService.getDiscountsProducts(companyId);

        mService.enqueue(new Callback<ProductsDiscountsDTO>() {
            @Override
            public void onResponse(@NonNull Call<ProductsDiscountsDTO> call, @NonNull Response<ProductsDiscountsDTO> response) {
                ProductsDiscountsDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        productsRecordList = mstoresObject.getRecordList();


                        productsNameList = new ArrayList<>();
                        productsNameList.add("Select Products");

                        for (int i = 0; i < productsRecordList.size(); i++) {
                            productsNameList.add(productsRecordList.get(i).getItemName());
                        }


                        /*CompanyAdapter adapter = new CompanyAdapter(getActivity(), productsNameList);
                        productSpinner.setAdapter(adapter);*/

                        AutoCompleteCompanyAdapter adapter = new AutoCompleteCompanyAdapter(getActivity(),
                                R.layout.searchautocomplete, R.id.searchtextview, productsNameList);
                        productSpinner.setAdapter(adapter);

                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductsDiscountsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmployeesList(String companyId) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeesDiscountsDTO> mService = mApiService.getDiscountsEmployees(companyId);

        mService.enqueue(new Callback<EmployeesDiscountsDTO>() {
            @Override
            public void onResponse(@NonNull Call<EmployeesDiscountsDTO> call, @NonNull Response<EmployeesDiscountsDTO> response) {
                EmployeesDiscountsDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        employeesRecordList = mstoresObject.getRecordList();


                        employeesNameList = new ArrayList<>();
                        employeesNameList.add("Select Employee");

                        for (int i = 0; i < employeesRecordList.size(); i++) {
                            employeesNameList.add(employeesRecordList.get(i).getUniqId());
                        }

                        /*CompanyAdapter adapter = new CompanyAdapter(getActivity(), employeesNameList);
                        referenceSpinner.setAdapter(adapter);*/

                        AutoCompleteCompanyAdapter adapter = new AutoCompleteCompanyAdapter(getActivity(),
                                R.layout.searchautocomplete, R.id.searchtextview, employeesNameList);
                        referenceSpinner.setAdapter(adapter);

                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeesDiscountsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            assert getFragmentManager() != null;
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            assert getFragmentManager() != null;
            tofragment.show(getFragmentManager(), "Date Picker");
        }
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
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
                assert type != null;
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);

                } else if (type.equals("toDate")) {
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
                        edtxt_fromdate.setText(year + "-" + month + "-" + dayOfMonth);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_todate.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                };


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    }


    private void sendDiscountRequest() {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();


        Call<ResultsDTO> mService = mApiService.discountRequest(companyId
                , dealerSpinner.getText().toString()
                , productSpinner.getText().toString()
                , referenceSpinner.getText().toString(), SharedDB.getUserName(getActivity())
                , edtxt_fromdate.getText().toString(), edtxt_todate.getText().toString()
                , discountText.getText().toString());

        mService.enqueue(new Callback<ResultsDTO>() {
            @Override
            public void onResponse(@NonNull Call<ResultsDTO> call, @NonNull Response<ResultsDTO> response) {
                ResultsDTO mLoginObject = response.body();
                dialog.dismiss();
                try {
                    assert mLoginObject != null;
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        Toast.makeText(getActivity(), mLoginObject.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
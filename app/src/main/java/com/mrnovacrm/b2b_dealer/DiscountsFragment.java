package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.CompanyAdapter;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harish on 6/24/2019.
 */

public class DiscountsFragment extends Fragment {

    Spinner companySpinner, dealerSpinner, productSpinner, referenceSpinner;
    EditText discountText;

    List<RecordListDTO> sellersList;
    List<DealersRecordListDTO> dealersRecordList;
    List<ProductsRecordListDTO> productsRecordList;
    List<EmployeesRecordListDTO> employeesRecordList;
    ArrayList<String> companyNames, dealerNameList, productsNameList, employeesNameList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_disocunts, container, false);


        companySpinner = rootView.findViewById(R.id.company_spinner);
        dealerSpinner = rootView.findViewById(R.id.dealer_spinner);
        productSpinner = rootView.findViewById(R.id.product_spinner);
        referenceSpinner = rootView.findViewById(R.id.reference_spinner);
        discountText = rootView.findViewById(R.id.discount_txt);


        getDiscountCompany();


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
                        sellersList = mOrderObject.getRecordList();
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {

                                companyNames = new ArrayList<>();

                                companyNames.add("Select Company");

                                for (int i = 0; i < sellersList.size(); i++) {
                                    companyNames.add(sellersList.get(i).getCompany());
                                }


                                CompanyAdapter adapter = new CompanyAdapter(getActivity(), companyNames);
                                companySpinner.setAdapter(adapter);


                                companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                        if (!companySpinner.getSelectedItem().equals("Select Company")) {

                                            getDealersList(sellersList.get(pos - 1).getCompanyId());
                                            getProductsList(sellersList.get(pos - 1).getCompanyId());
                                            getEmployeesList(sellersList.get(pos - 1).getCompanyId());

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


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


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), dealerNameList);
                        dealerSpinner.setAdapter(adapter);

                        dealerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!dealerSpinner.getSelectedItem().equals("Select Dealer")) {

                                    Toast.makeText(getActivity(), dealersRecordList.get(pos - 1).getName(), Toast.LENGTH_SHORT).show();


//                                    getDealersList(sellersList.get(pos).getCompanyId());

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

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


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), productsNameList);
                        productSpinner.setAdapter(adapter);

                        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!productSpinner.getSelectedItem().equals("Select Products")) {

                                    Toast.makeText(getActivity(), productsRecordList.get(pos - 1).getItemName(), Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

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


                        CompanyAdapter adapter = new CompanyAdapter(getActivity(), employeesNameList);
                        referenceSpinner.setAdapter(adapter);

                        referenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                if (!referenceSpinner.getSelectedItem().equals("Select Employee")) {

                                    Toast.makeText(getActivity(), employeesRecordList.get(pos - 1).getUniqId(), Toast.LENGTH_SHORT).show();


//                                    getDealersList(sellersList.get(pos).getCompanyId());

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

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

}
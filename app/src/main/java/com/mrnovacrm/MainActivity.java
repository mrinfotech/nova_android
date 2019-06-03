package com.mrnovacrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_admin.AdminOrdersTabActivity;
import com.mrnovacrm.b2b_admin.AdminPaymentsActivity;
import com.mrnovacrm.b2b_admin.EmpListActivity;
import com.mrnovacrm.b2b_dealer.CartListActivity;
import com.mrnovacrm.b2b_dealer.CheckoutInvoiceReportActivity;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealersDataActivity;
import com.mrnovacrm.b2b_dealer.FinanceTransportOrderDetailsActivity;
import com.mrnovacrm.b2b_dealer.InvoiceDetailsActivity;
import com.mrnovacrm.b2b_dealer.NotificationStatementActivity;
import com.mrnovacrm.b2b_dealer.OrderDetailsActivity;
import com.mrnovacrm.b2b_dealer.ProductCategoriesActivity;
import com.mrnovacrm.b2b_dealer.ProductImageActivity;
import com.mrnovacrm.b2b_dealer.ProductListActivity;
import com.mrnovacrm.b2b_dealer.ProductListActivityNew;
import com.mrnovacrm.b2b_dealer.TrackOrderDetailsActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DOrderDetailsActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchDeliveryProcessDetailsActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchPackHistoryDetailsActivity;
import com.mrnovacrm.b2b_dispatch_dept.PackedOrderDetailsActivity;
import com.mrnovacrm.b2b_dispatch_dept.UpdateLRNumberDetails;
import com.mrnovacrm.b2b_finance_dept.EmployeeFullDeatailsActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceEmployeesListActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceOrderDetailsActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceOrdersTabActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceReportActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminDashBoardActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.userprofile.UserProfileActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    GlobalShare globalShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalShare = (GlobalShare) getApplicationContext();
        Intent intent_o = getIntent();
        String user_id = intent_o.getStringExtra("user_id");
        String date = intent_o.getStringExtra("date");
        String hal_id = intent_o.getStringExtra("hal_id");
        String M_view = intent_o.getStringExtra("M_view");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            String userType = values.get(SharedDB.SHORTFORM);

            globalShare.setLoginselectedfromval("notification");
            if (userType.equals("DEALER")) {
                closeDealermethods();

                globalShare.setDealerMenuSelectedPos("8");
                Intent intent = new Intent(MainActivity.this,
                        DealerMenuScreenActivity.class);
                startActivity(intent);
            } else if (userType.equals("FM")) {
                closeDealermethods();
                globalShare.setFinancemenuselectpos("5");
                Intent intent = new Intent(MainActivity.this,
                        FinanceDeptMenuScreenActivity.class);
                startActivity(intent);
            }
//            else if (userType.equals("PACKER")) {
//                closeDealermethods();
//                globalShare.setDispatchmenuselectpos("8");
//                Intent intent = new Intent(MainActivity.this,
//                        DispatchMenuScreenActivity.class);
//                startActivity(intent);
//            }
            else if (userType.equals("DB")) {
                globalShare.setDeliverymenuselectpos("1");
                Intent intent = new Intent(MainActivity.this,
                        DeliveryMenuScreenActivity.class);
                startActivity(intent);
            } else if (userType.equals("SE")) {
                closeDealermethods();
                globalShare.setDealerMenuSelectedPos("10");
                Intent intent = new Intent(MainActivity.this,
                        DealerMenuScreenActivity.class);
                startActivity(intent);
            }
//            else if (userType.equals("ADMIN")) {
//                closeDealermethods();
//                globalShare.setAdminmenuselectpos("5");
//                Intent intent = new Intent(MainActivity.this,
//                        AdminMenuScreenActivity.class);
//                startActivity(intent);
//            } else if (userType.equals("SA")) {
//                closeDealermethods();
//                globalShare.setSuperadminmenuselectpos("5");
//                Intent intent = new Intent(MainActivity.this,
//                        SuperAdminMenuScrenActivity.class);
//                startActivity(intent);
//            }
            finish();
        } else {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }


    public void closeDealermethods() {

        if(FinanceTransportOrderDetailsActivity.mainfinish!=null)
        {
            FinanceTransportOrderDetailsActivity.mainfinish.finish();
        }

        if(NotificationStatementActivity.mainfinish!=null)
        {
            NotificationStatementActivity.mainfinish.finish();
        }

        if (SuperAdminDashBoardActivity.mainfinish != null) {
            SuperAdminDashBoardActivity.mainfinish.finish();
        }

        if (SuperAdminMenuScrenActivity.mainfinish != null) {
            SuperAdminMenuScrenActivity.mainfinish.finish();
        }

        if (AdminPaymentsActivity.mainfinish != null) {
            AdminPaymentsActivity.mainfinish.finish();
        }

        if (EmpListActivity.mainfinish != null) {
            EmpListActivity.mainfinish.finish();
        }

        if (AdminOrdersTabActivity.mainfinish != null) {
            AdminOrdersTabActivity.mainfinish.finish();
        }

        if (AdminMenuScreenActivity.mainfinish != null) {
            AdminMenuScreenActivity.mainfinish.finish();
        }

        if (UpdateLRNumberDetails.mainfinish != null) {
            UpdateLRNumberDetails.mainfinish.finish();
        }

        if (DOrderDetailsActivity.mainfinish != null) {
            DOrderDetailsActivity.mainfinish.finish();
        }

        if (DispatchDeliveryProcessDetailsActivity.mainfinish != null) {
            DispatchDeliveryProcessDetailsActivity.mainfinish.finish();
        }

        if (DispatchPackHistoryDetailsActivity.mainfinish != null) {
            DispatchPackHistoryDetailsActivity.mainfinish.finish();
        }

        if (PackedOrderDetailsActivity.mainfinish != null) {
            PackedOrderDetailsActivity.mainfinish.finish();
        }

       if (DispatchMenuScreenActivity.mainfinish != null) {
           DispatchMenuScreenActivity.mainfinish.finish();
        }
       if (FinanceReportActivity.mainfinish != null) {
            FinanceReportActivity.mainfinish.finish();
        }

        if (FinanceEmployeesListActivity.mainfinish != null) {
            FinanceEmployeesListActivity.mainfinish.finish();
        }

        if (FinanceOrderDetailsActivity.mainfinish != null) {
            FinanceOrderDetailsActivity.mainfinish.finish();
        }

        if (FinanceOrdersTabActivity.mainfinish != null) {
            FinanceOrdersTabActivity.mainfinish.finish();
        }


        if (FinanceDeptMenuScreenActivity.mainfinish != null) {
            FinanceDeptMenuScreenActivity.mainfinish.finish();
        }


        if (UserProfileActivity.mainfinish != null) {
            UserProfileActivity.mainfinish.finish();
        }
        if (ProductCategoriesActivity.mainfinish != null) {
            ProductCategoriesActivity.mainfinish.finish();
        }
        if (ProductListActivity.mainfinish != null) {
            ProductListActivity.mainfinish.finish();
        }
        if (CartListActivity.mainfinish != null) {
            CartListActivity.mainfinish.finish();
        }
        if (DealerMenuScreenActivity.mainfinish != null) {
            DealerMenuScreenActivity.mainfinish.finish();
        }

        if (ProductListActivityNew.mainfinish != null) {
            ProductListActivityNew.mainfinish.finish();
        }

        if (CheckoutInvoiceReportActivity.mainfinish != null) {
            CheckoutInvoiceReportActivity.mainfinish.finish();
        }

        if (OrderDetailsActivity.mainfinish != null) {
            OrderDetailsActivity.mainfinish.finish();
        }

        if (FinanceEmployeesListActivity.mainfinish != null) {
            FinanceEmployeesListActivity.mainfinish.finish();
        }
        if (EmployeeFullDeatailsActivity.mainfinish != null) {
            EmployeeFullDeatailsActivity.mainfinish.finish();
        }

        if (InvoiceDetailsActivity.mainfinish != null) {
            InvoiceDetailsActivity.mainfinish.finish();
        }
        if (DealersDataActivity.mainfinish != null) {
            DealersDataActivity.mainfinish.finish();
        }

        if (ProductImageActivity.mainfinish != null) {
            ProductImageActivity.mainfinish.finish();
        }

        if (TrackOrderDetailsActivity.mainfinish != null) {
            TrackOrderDetailsActivity.mainfinish.finish();
        }





    }
}
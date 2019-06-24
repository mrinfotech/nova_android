package com.mrnovacrm.constants;

import com.mrnovacrm.model.BranchesDTO;
import com.mrnovacrm.model.CategoryDTO;
import com.mrnovacrm.model.Checkout;
import com.mrnovacrm.model.CompaniesDTO;
import com.mrnovacrm.model.DailyOrders;
import com.mrnovacrm.model.DealersDTO;
import com.mrnovacrm.model.DeliveryDTO;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeesDiscountsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.Product;
import com.mrnovacrm.model.ProductsDiscountsDTO;
import com.mrnovacrm.model.SearchItemsDTO;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.StoresDTO;
import com.mrnovacrm.model.Track;
import com.mrnovacrm.model.TransportDTO;
import com.mrnovacrm.model.WalletDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
/**
 * Created by android on 22-02-2018.
 */

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("login")
    Call<Login> checkLogin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @PUT("login/forgotpassword")
    Call<Login> getForgotPassword(@Field("username") String username);

    @FormUrlEncoded
    @POST("notification/store_token")
    Call<Login> saveFCMToken(@Field("user") String user, @Field("role") String role,
                             @Field("token") String token);

    @GET("dealercart/count")
    Call<Product> getCartCount(@Query("user") String user);

    @GET("dealeritems/search")
    Call<SearchItemsDTO> searchItemsList(@Query("company") String company);

    @GET("master/emp_roles")
    Call<Login> getEmpRolesList(@Query("user") String user);

    @GET("dealeritems/globalsearch")
    Call<SearchItemsDTO> globalsearchItemsList(@Query("category") String company);

    @GET("notification/list")
    Call<Login> getNotificationList(@Query("id") String id,@Query("branch") String branch,
                                             @Query("role") String role);

    @FormUrlEncoded
    @POST("dealercart/add")
    Call<Product> addtocart(@Field("user") String user, @Field("item") String item, @Field("sellerid") String sellerid,
                            @Field("qty") String qty,@Field("role") String role);

    @FormUrlEncoded
    @POST("dealercart/json_add")
    Call<Product> productaddtocart(@Field("user") String user, @Field("item") String item, @Field("sellerid") String sellerid,
                            @Field("qty") String qty,@Field("role") String role,@Field("json_qty") String json_qty);

    @FormUrlEncoded
    @PUT("dealercart/delete")
    Call<Product> removecart(@Field("user") String user, @Field("item") String item, @Field("sellerid") String sellerid);

    @GET("dealerorders/details")
    Call<Order> getOrderDetails(@Query("order") String order,@Query("role") String role);

    @GET("transferorders/details")
    Call<Order> getTransferOrderDetails(@Query("order") String order,@Query("role") String role);

    @GET("dealerorders/final_details")
    Call<Order> getReceivedOrderDetails(@Query("order") String order);


    @GET("dealerorders/delivery_details")
    Call<Order> getReceivedOrderFinalDetails(@Query("order") String order,@Query("role") String role);


    @GET("dealerorders/details")
    Call<Order> getOrderDetailss(@Query("order") String order,@Query("status") String status,
                                 @Query("role") String role);

    @GET("dealerorders/delivery_details")
    Call<Order> getDeliverOrderDetailss(@Query("order") String order,@Query("status") String status,
                                        @Query("role") String role);

    @GET("employee/employeedetails")
    Call<Login> getEMPDetails(@Query("user") String user);

    @GET("employee/dealerdetails")
    Call<Login> getDealerDetails(@Query("user") String user);

    @FormUrlEncoded
    @POST("dealercart/checkout")
    Call<Checkout> checkoutCart(@Field("user") String user, @Field("address") String address, @Field("flat_no") String flat_no,
                                @Field("landmark") String landmark,
                                @Field("latitude") String latitude, @Field("langitude") String langitude,
                                @Field("payment_type") String payment_type, @Field("credit_date") String credit_date,
                                @Field("createdby") String createdby,@Field("branch") String branch,
                                @Field("reference_no") String reference_no,@Field("transport") String transport,
                                @Field("remarks") String remarks);

    @GET("dealercart/list")
    Call<Product> getCartList(@Query("user") String user);


    @GET("master/dealer_companies")
    Call<Login> getDealerCompaniesList(@Query("user") String user);

    @GET("items/offers")
    Call<Product> getOffersList(@Query("user") String user,@Query("offer_type") String offer_type);

    @GET("items/btdetails")
    Call<Product> getBTProductsList(@Query("id") String id, @Query("user") String user);

    @GET("dealeritems/list")
    Call<Product> getProductsList(@Query("id") String id, @Query("user") String user,
                                  @Query("branch") String branch,@Query("category") String category);

    @GET("dealeritems/list")
    Call<Product> getSearchProductsList(@Query("id") String id, @Query("user") String user,
                                  @Query("branch") String branch,@Query("category") String category,
                                        @Query("item_id") String item_id);

    @FormUrlEncoded
    @PUT("login/changepassword")
    Call<Login> changePassword(@Field("primaryid") String primaryid, @Field("role") String role,
                               @Field("oldpassword") String oldpassword, @Field("newpassword") String newpassword);

    @GET("dealerorders/list")
    Call<Order> getOrdersList(@Query("user") String user, @Query("fromdate") String fromdate, @Query("todate") String todate,
                              @Query("status") String status,@Query("role") String role,@Query("branch") String branch);

    @GET("wallet/history")
    Call<Order> getWalletCreditList(@Query("user") String user, @Query("fromdate") String fromdate, @Query("todate") String todate,
                              @Query("status") String status,@Query("role") String role);

    @GET("sellers/cheque_list")
    Call<Order> getWalletChequeList(@Query("user") String user,@Query("fromdate") String fromdate, @Query("todate") String todate);

    @GET("sellers/cheque_details")
    Call<Order> getWalletChequeDetails(@Query("user") String user,@Query("id") String id);


    @GET("sellers/debit_orders")
    Call<WalletDTO> getWalletOrdersList(@Query("user") String user);

    @GET("sellers/seller_balance")
    Call<WalletDTO> getWalletBalance(@Query("user") String user);

    @GET("wallet/details")
    Call<Order> getWalletCreditDetails(@Query("user") String user, @Query("transaction_id") String transaction_id);

    @GET("deliveryboy/invoiceslist")
    Call<Order> getDeliveryBoyInvoiceList(@Query("user") String user, @Query("fromdate") String fromdate,
                                          @Query("todate") String todate);

    @GET("master/list")
    Call<Order> getDeliveryBoyMasterInvoiceList(@Query("user") String user, @Query("fromdate") String fromdate,
                                          @Query("todate") String todate,@Query("role") String role,
                                                @Query("branch") String branch);

    @GET("transport/emptylrno")
    Call<Order> getEmpLRDetailsList(@Query("user") String user, @Query("fromdate") String fromdate,
                                                @Query("todate") String todate,@Query("role") String role,
                                                @Query("branch") String branch);

    @GET("fa/orders")
    Call<Order> getFinanceOrdersList(@Query("user") String user, @Query("fromdate") String fromdate, @Query("todate") String todate,
                              @Query("status") String status,@Query("branch") String branch);

    @GET("transferorders/list")
    Call<Order> getFinanceTransferredOrdersList(@Query("user") String user, @Query("fromdate") String fromdate, @Query("todate") String todate,
                                     @Query("status") String status,@Query("branch") String branch);

    @GET("admin/orders")
    Call<Order> getAdminOrdersList(@Query("user") String user, @Query("fromdate") String fromdate, @Query("todate") String todate,
                                     @Query("status") String status,@Query("branch") String branch);

    @FormUrlEncoded
    @PUT("dealerorders/cancel")
    Call<Login> cancelOrder(@Field("order") String order, @Field("reason") String reason);

    @FormUrlEncoded
    @POST("dealerorders/do_action")
    Call<Login> stores_orderstatus(@Field("action") String action,@Field("primary_key") String primary_key,
                                   @Field("role") String role,
                                   @Field("item") String item, @Field("qty") String qty,
                                   @Field("description") String description,@Field("reason") String reason,
                                   @Field("user") String user);

    @GET("master/rejectionpoints")
    Call<SellerDTO> getRejectPoints();

    @GET("master/rejectionpoints")
    Call<SellerDTO> getFinanceRejectPoints(@Query("rej_for") String rej_for);

    @GET("master/discount_points")
    Call<SellerDTO> getDiscountPoints();

    @GET("Discounts/companies")
    Call<CompaniesDTO> getDiscounts();

    @GET("discounts/dealers")
    Call<DealersDTO> getDiscountsDealers(@Query("company") String company);

    @GET("discounts/products")
    Call<ProductsDiscountsDTO> getDiscountsProducts(@Query("company") String company);

    @GET("discounts/employees")
    Call<EmployeesDiscountsDTO> getDiscountsEmployees(@Query("company") String company);




    @GET("categories")
    Call<CategoryDTO> categoriesList(@Query("category") String category);

    @GET("dealerorders/track")
    Call<Track> trackOrderDetails(@Query("order") String order);

    @GET("transport/details")
    Call<Track> trackDeliveryDetails(@Query("order") String order);

    @FormUrlEncoded
    @POST("send_otp")
    Call<Login> getOTP(@Field("mobile") String mobile);

    @GET("stores/list")
    Call<StoresDTO> storesList(@Query("action") String action);

    @FormUrlEncoded
    @PUT("stores/changestatus")
    Call<StoresDTO> updateStoreStatus(@Field("primaryid") String primaryid, @Field("id") String id,
                                      @Field("store_status") String store_status, @Field("reason") String reason);

   /* @GET("packers/packreport")
    Call<DailyOrders> packerspackreport(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                        @Query("status") String status, @Query("user") String user);*/

    @GET("packers/so_packreport")
    Call<DailyOrders> packerspackreport(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                        @Query("status") String status, @Query("user") String user,
                                        @Query("branch") String branch);
    @GET("packers/so_packorders_deatails")
    Call<Order> getPackReceivedDetails(@Query("order") String order,@Query("status") String status);

    @FormUrlEncoded
    @POST("deliveryboy/do_action")
    Call<Login> deliveryboy_orderstatus(@Field("id") String id,@Field("action") String action,
                                        @Field("order") String order, @Field("user") String user,
                                        @Field("description") String description, @Field("reason") String reason);

    @GET("packers/seller_orders")
    Call<DailyOrders> getPlacedOrderDetails(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                            @Query("status") String status,@Query("branch") String branch);

//    @GET("packers/details")
//    Call<Order> getPackerOrderDetails(@Query("order") String order);

    @GET("packers/so_details")
    Call<Order> getPackerOrderDetails(@Query("order") String order);

    @GET("packers/so_packed_details")
    Call<Order> getDipachPackerOrderDetails(@Query("order") String order,@Query("action") String action,@Query("role") String role);

    //totalrejected

    @GET("deliveryboy/so_rejected_details")
    Call<Order> getDispatchRejectedHistoryDetails(@Query("order") String order,@Query("action") String action);

    @GET("packers/so2_details")
    Call<Order> getDispatcherOrderDetails(@Query("order") String order);

    @GET("packers/barcodes")
    Call<Order> getBarcodes(@Query("id") String id);

    @FormUrlEncoded
    @POST("packers/pack_seller")
    Call<Login> submitPackedList(@Field("packer") String packer, @Field("pack_type") String pack_type,
                                 @Field("items") String items, @Field("order") String order);

    @GET("Stores_orders/list")
    Call<SellerDTO> getDeliveryStoreOrderList();

    @GET("deliveryboy/dealers")
    Call<SellerDTO> getDealers(@Query("branch") String branch);

//    @GET("delivery/dboy_order_summary")
//    Call<Order> getDeliveryBoyPackDetails(@Query("order") String order,@Query("screen") String screen);


    @GET("deliveryboy/order_summary")
    Call<Order> getDeliveryBoyPackDetails(@Query("order") String order,@Query("screen") String screen);

    @GET("master/rejectionpoints")
    Call<SellerDTO> getDeliveryRejectPoints(@Query("rej_for") String rej_for);

    @GET("deliveryboy/so_pickreport")
    Call<SellerDTO> getDeliveryPickupHistory(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                             @Query("status") String status,@Query("user") String user);

    @GET("deliveryboy/so_pickreport")
    Call<Order> getDeliveryBoyPickDetails(@Query("order") String order,@Query("screen") String screen,
                                          @Query("status") String status,@Query("user") String user,
                                          @Query("token") String token);

    @FormUrlEncoded
    @POST("deliveryboy/route_process")
    Call<Login> setDeliveryRouteProcess(@Field("droute_id") String droute_id, @Field("latitude") String latitude,
                                        @Field("langitude") String langitude,
                                        @Field("status") String status,@Field("store") String store,@Field("pkey") String pkey);

    @GET("deliveryboy/dealers_list")
    Call<DeliveryDTO> getStoresList(@Query("user") String user);

    @GET("deliveryboy/dealers_list")
    Call<DeliveryDTO> getBranchesList(@Query("user") String user,@Query("branch") String branch);

    @FormUrlEncoded
    @POST("deliveryboy/so_defineroute")
    Call<Login> submitDefineRoute(@Field("emp_id") String emp_id, @Field("latitude") String latitude,
                                  @Field("longitude") String longitude, @Field("order_str") String order_str);

    @FormUrlEncoded
    @POST("deliveryboy/manage_transport")
    Call<Login> submitDisptachTransport(@Field("user") String user, @Field("seller") String seller,
                                  @Field("order_str") String order_str, @Field("vechicle_number") String vechicle_number,
                                  @Field("driver_number") String driver_number, @Field("driver_name") String driver_name,
                                  @Field("lr_no") String lr_no, @Field("transport") String transport,
                                  @Field("branch") String branch, @Field("transport_type") String transport_type,
                                  @Field("amount") String amount, @Field("paid") String paid,
                                  @Field("from_route") String from_route, @Field("to_route") String to_route,
                                  @Field("estimation_time") String estimation_time, @Field("contact") String contact);

    @FormUrlEncoded
    @POST("transport/updatelrno")
    Call<Login> updateLRnumber(@Field("user") String user, @Field("lrno") String lrno,
                                  @Field("id") String id);

    @FormUrlEncoded
    @POST("deliveryboy/editroute")
    Call<Login> editsubmitDefineRoute(@Field("emp_id") String emp_id,@Field("order_str") String order_str);

//    @GET("deliveryboy/dboy_route_summary")
//    Call<Order> getDeliveryBoyProcessDetails(@Query("user") String user,@Query("pkey") String pkey);

    @GET("deliveryboy/so_dboy_route_summary")
    Call<Order> getDeliveryBoyProcessDetails(@Query("user") String user,@Query("order") String order);

//    @FormUrlEncoded
//    @POST("deliveryboy/delivery")
//    Call<Login> deliveryboydeliver_orderstatus(@Field("id") String id,@Field("action") String action,
//                                               @Field("order") String order, @Field("user") String user,
//                                               @Field("description") String description, @Field("reason") String reason,
//                                               @Field("delivered_qty") String delivered_qty,
//                                               @Field("delivered_qty") String delivered_qty);

    @FormUrlEncoded
    @POST("deliveryboy/so_delivery")
    Call<Login> deliveryboydeliver_orderstatus(@Field("action") String action,@Field("user") String user,
                                               @Field("id") String id,@Field("reason") String reason,
                                               @Field("description") String description, @Field("order") String order,
                                               @Field("delivered_qty") String delivered_qty);

    @GET("deliveryboy/so_deliveryreport")
    Call<DailyOrders> deliveryBoyDeliverHistory(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                                @Query("status") String status,@Query("user") String user,
                                                @Query("branch") String branch);

    @GET("deliveryboy/so_reject_report")
    Call<DailyOrders> deliveryBoyRejectedHistory(@Query("fromdate") String fromdate, @Query("todate") String todate,
                                                @Query("status") String status,@Query("user") String user,
                                                @Query("branch") String branch);

    @GET("employee/list")
    Call<EmployeeDTO> employeeList();

    @FormUrlEncoded
    @POST("branches/manage")
    Call<Login> addBranches(@Field("user") String user,@Field("email") String email,
                           @Field("name") String name,@Field("mobile") String mobile, @Field("address") String address,
                           @Field("district") String district,@Field("state") String state,@Field("country") String country,
                           @Field("gst") String gst,@Field("latitude") String latitude,@Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("branches/manage")
    Call<Login> manageBranches(@Field("user") String user,@Field("email") String email,
                            @Field("name") String name,@Field("mobile") String mobile, @Field("address") String address,
                            @Field("district") String district,@Field("state") String state,@Field("country") String country,
                            @Field("gst") String gst,@Field("latitude") String latitude,@Field("longitude") String longitude,
                               @Field("branch") String branch);


    @GET("master/employee_code")
    Call<EmployeeDTO> getEmpUniqueCode(@Query("user") String user);


    @GET("demographics/countries")
    Call<DemoGraphicsDTO> getCountries();

    @GET("demographics/states")
    Call<DemoGraphicsDTO> getStates(@Query("country") String country);

    @GET("demographics/districts")
    Call<DemoGraphicsDTO> getDistricts(@Query("state") String state);

    @GET("branches/list")
    Call<BranchesDTO> getBrachesList();

    @GET("branches/shortlist")
    Call<BranchesDTO> getBraches();

    @GET("branches/shortlist")
    Call<BranchesDTO> getPlacedOrderBraches(@Query("company") String company,@Query("branch") String branch);

    @FormUrlEncoded
    @PUT("branches/delete")
    Call<BranchesDTO> deleteBranch(@Field("branch") String branch);

    @FormUrlEncoded
    @PUT("employee/delete")
    Call<BranchesDTO> deleteEmployee(@Field("role") String role,@Field("employee") String employee);

    @FormUrlEncoded
    @PUT("transport/delete")
    Call<BranchesDTO> deleteTransport(@Field("branch") String branch,@Field("user") String user,@Field("transport") String transport);


    @FormUrlEncoded
    @PUT("grades/delete")
    Call<BranchesDTO> deleteGrade(@Field("grade") String grade);


    @FormUrlEncoded
    @POST("employee/add")
    Call<Login> addEmployee(@Field("first_name") String first_name, @Field("last_name") String last_name,
                            @Field("role") String role, @Field("mobile") String mobile,
                            @Field("dob") String dob, @Field("address") String address,
                            @Field("user") String user, @Field("email") String email,
                            @Field("latitude") String latitude, @Field("langitude") String langitude);

    @FormUrlEncoded
    @POST("employee/add")
    Call<Login> addAdmin(@Field("first_name") String first_name, @Field("last_name") String last_name,
                            @Field("role") String role, @Field("mobile") String mobile,
                            @Field("dob") String dob, @Field("address") String address,
                            @Field("user") String user, @Field("email") String email,
                            @Field("latitude") String latitude, @Field("langitude") String langitude,
                            @Field("branch") String branch);

    @FormUrlEncoded
    @POST("employee/mangedealer")
    Call<Login> addDealer(@Field("first_name") String first_name,
                          @Field("company_name") String company_name,
                          @Field("key_person") String key_person, @Field("address") String address,
                          @Field("country") String country, @Field("state") String state,
                          @Field("town") String town, @Field("pincode") String pincode,
                          @Field("pan") String pan,@Field("gst") String gst,@Field("reg_type") String reg_type,
                          @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                          @Field("ifsc") String ifsc,@Field("dealer_code") String dealer_code,
                          @Field("contact_email") String contact_email,@Field("contact_personal") String contact_personal,
                          @Field("contact_whatsup") String contact_whatsup,@Field("sales_manager") String sales_manager,
                          @Field("am_contact") String am_contact,@Field("mobile") String mobile,
                          @Field("email") String email,
                          @Field("user") String user,
                          @Field("role") String role,
                          @Field("worked_for") String worked_for,
                          @Field("branch") String branch,@Field("licence") String licence
                          );

    @FormUrlEncoded
    @POST("employee/mangedealer")
    Call<Login> addDealerFealds(@Field("first_name") String first_name,
                          @Field("company_name") String company_name,
                          @Field("key_person") String key_person, @Field("address") String address,
                          @Field("country") String country, @Field("state") String state,
                          @Field("town") String town, @Field("pincode") String pincode,
                          @Field("pan") String pan,@Field("gst") String gst,@Field("reg_type") String reg_type,
                          @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                          @Field("ifsc") String ifsc,@Field("dealer_code") String dealer_code,
                          @Field("contact_email") String contact_email,@Field("contact_personal") String contact_personal,
                          @Field("contact_whatsup") String contact_whatsup,@Field("sales_manager") String sales_manager,
                          @Field("am_contact") String am_contact,@Field("mobile") String mobile,
                          @Field("email") String email,
                          @Field("user") String user,
                          @Field("role") String role,
                          @Field("worked_for") String worked_for,
                          @Field("branch") String branch,@Field("licence") String licence,
                          @Field("door_no") String door_no,@Field("owner_desg") String owner_desg,
                          @Field("contact1") String contact1,@Field("contact2") String contact2,
                          @Field("delaer_whatsapp") String delaer_whatsapp,@Field("street_name") String street_name,
                          @Field("landmark") String landmark,@Field("district") String district,
                          @Field("bank_branch") String bank_branch,@Field("designation") String designation,
                          @Field("pesticide") String pesticide,@Field("pesticide_upto") String pesticide_upto,
                          @Field("fertilizer") String fertilizer,@Field("fertilizer_upto") String fertilizer_upto,
                          @Field("seed") String seed,@Field("seed_upto") String seed_upto,
                          @Field("other") String other,
                          @Field("credit_date") String credit_date,
                          @Field("wallet") String wallet);

//    @FormUrlEncoded
//    @POST("sellers/balance_update")
//    Call<Login> addPaymentFealds(@Field("order_id") String order_id,
//                                @Field("payment_mode") String payment_mode,
//                                @Field("cheque_no") String cheque_no, @Field("cheque_status") String cheque_status,
//                                @Field("account_name") String account_name, @Field("account_number") String account_number,
//                                @Field("bank_name") String bank_name, @Field("action_by") String action_by,
//                                @Field("action_role") String action_role,@Field("deposit_date") String deposit_date);

    @FormUrlEncoded
    @POST("sellers/balance_update")
    Call<Login> addPaymentFealds(@Field("user") String user,
                                 @Field("amount") String amount,
                                 @Field("discount") String discount,
                                 @Field("discount_point_id") String discount_point_id,
                                 @Field("discount_point") String discount_point,
                                 @Field("payment_mode") String payment_mode,
                                 @Field("cheque_no") String cheque_no,
                                 @Field("deposit_date") String deposit_date,
                                 @Field("cheque_status") String cheque_status,
                                 @Field("account_name") String account_name,
                                 @Field("account_number") String account_number,
                                 @Field("bank_name") String bank_name,
                                 @Field("action_by") String action_by,
                                 @Field("transaction_no") String transaction_no,
                                 @Field("branch") String branch);

    @FormUrlEncoded
    @POST("employee/mangedealer")
    Call<Login> updateDealerFealdDetails(@Field("first_name") String first_name,
                                @Field("company_name") String company_name,
                                @Field("key_person") String key_person, @Field("address") String address,
                                @Field("country") String country, @Field("state") String state,
                                @Field("town") String town, @Field("pincode") String pincode,
                                @Field("pan") String pan,@Field("gst") String gst,@Field("reg_type") String reg_type,
                                @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                                @Field("ifsc") String ifsc,@Field("dealer_code") String dealer_code,
                                @Field("contact_email") String contact_email,@Field("contact_personal") String contact_personal,
                                @Field("contact_whatsup") String contact_whatsup,@Field("sales_manager") String sales_manager,
                                @Field("am_contact") String am_contact,@Field("mobile") String mobile,
                                @Field("email") String email,
                                @Field("user") String user,
                                @Field("role") String role,
                                @Field("worked_for") String worked_for,
                                @Field("branch") String branch,@Field("licence") String licence,
                                @Field("door_no") String door_no,@Field("owner_desg") String owner_desg,
                                @Field("contact1") String contact1,@Field("contact2") String contact2,
                                @Field("delaer_whatsapp") String delaer_whatsapp,@Field("street_name") String street_name,
                                @Field("landmark") String landmark,@Field("district") String district,
                                @Field("bank_branch") String bank_branch,@Field("designation") String designation,
                                @Field("pesticide") String pesticide,@Field("pesticide_upto") String pesticide_upto,
                                @Field("fertilizer") String fertilizer,@Field("fertilizer_upto") String fertilizer_upto,
                                @Field("seed") String seed,@Field("seed_upto") String seed_upto,
                                @Field("other") String other,
                                @Field("employee") String employee);

    @FormUrlEncoded
    @POST("employee/mangedealer")
    Call<Login> updateDealer(@Field("first_name") String first_name,
                          @Field("company_name") String company_name,
                          @Field("key_person") String key_person, @Field("address") String address,
                          @Field("country") String country, @Field("state") String state,
                          @Field("town") String town, @Field("pincode") String pincode,
                          @Field("pan") String pan,@Field("gst") String gst,@Field("reg_type") String reg_type,
                          @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                          @Field("ifsc") String ifsc,@Field("dealer_code") String dealer_code,
                          @Field("contact_email") String contact_email,@Field("contact_personal") String contact_personal,
                          @Field("contact_whatsup") String contact_whatsup,@Field("sales_manager") String sales_manager,
                          @Field("am_contact") String am_contact,@Field("mobile") String mobile,
                          @Field("email") String email,
                          @Field("user") String user,
                          @Field("role") String role,
                          @Field("worked_for") String worked_for,
                          @Field("branch") String branch,@Field("licence") String licence,
                          @Field("employee") String employee
    );

    @FormUrlEncoded
    @POST("employee/add")
    Call<Login> manageEmployee(@Field("first_name") String first_name,
                          @Field("dob") String dob,
                          @Field("gender") String gender, @Field("father") String father,
                          @Field("mother") String mother, @Field("mstatus") String mstatus,
                          @Field("address") String address, @Field("country") String country,
                          @Field("state") String state,
                          @Field("town") String town, @Field("pincode") String pincode,
                          @Field("pan") String pan,@Field("pf") String pf,@Field("esi") String esi,
                          @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                          @Field("ifsc") String ifsc,
                          @Field("emp_code") String emp_code,
                          @Field("doj") String doj,@Field("dept") String dept,
                          @Field("designation") String designation,
                          @Field("ofc_email") String ofc_email,
                          @Field("ofc_contact") String ofc_contact,
                          @Field("experience") String experience,
                          @Field("report_to") String report_to,
                          @Field("mobile") String mobile,
                          @Field("email") String email,
                          @Field("user") String user,
                          @Field("role") String role,
                          @Field("company") String company,
                          @Field("branch") String branch);

    @FormUrlEncoded
    @POST("employee/add")
    Call<Login> updateEmployee(@Field("first_name") String first_name,
                               @Field("dob") String dob,
                               @Field("gender") String gender, @Field("father") String father,
                               @Field("mother") String mother, @Field("mstatus") String mstatus,
                               @Field("address") String address, @Field("country") String country,
                               @Field("state") String state,
                               @Field("town") String town, @Field("pincode") String pincode,
                               @Field("pan") String pan,@Field("pf") String pf,@Field("esi") String esi,
                               @Field("bank_name") String bank_name,@Field("bank_accno") String bank_accno,
                               @Field("ifsc") String ifsc,
                               @Field("emp_code") String emp_code,
                               @Field("doj") String doj,@Field("dept") String dept,
                               @Field("designation") String designation,
                               @Field("ofc_email") String ofc_email,
                               @Field("ofc_contact") String ofc_contact,
                               @Field("experience") String experience,
                               @Field("report_to") String report_to,
                               @Field("mobile") String mobile,
                               @Field("email") String email,
                               @Field("user") String user,
                               @Field("role") String role,
                               @Field("company") String company,
                               @Field("branch") String branch,
                               @Field("employee") String employee);

    @FormUrlEncoded
    @POST("dealer/add")
    Call<Login> addEmployee(@Field("first_name") String first_name, @Field("last_name") String last_name,
                          @Field("role") String role, @Field("mobile") String mobile,
                          @Field("dob") String dob, @Field("address") String address,
                          @Field("user") String user, @Field("email") String email,
                          @Field("latitude") String s, @Field("langitude") String s1,
                          @Field("latitude") String latirtude, @Field("langitude") String lan1gitude,
                          @Field("latitude") String latitfude, @Field("langitude") String ladfngitude,
                          @Field("latitude") String ladfdstitude, @Field("langitude") String langdfadsitude,
                          @Field("latitude") String ldfasatitude, @Field("langitude") String langitgasude,
                          @Field("latitude") String latitude, @Field("langitude") String ladfsadfngitude,
                          @Field("latitude") String dsflatitfude, @Field("langitude") String ladsdfdsffngitude,
                         @Field("latitude") String latdsfidftdfude, @Field("langitude") String langitdfdsaude,
                          @Field("latitude") String dsfadsfadslatitude
    );

    @FormUrlEncoded
    @POST("deliveryboy/manage_transport")
    Call<Login> addTransportDetails(@Field("vehicle_id") String vehicle_id, @Field("route_id") String route_id,
                         @Field("vechicle_number") String vechicle_number, @Field("driver_number") String driver_number,
                         @Field("driver_name") String driver_name, @Field("lr_no") String lr_no,
                         @Field("tranport") String tranport, @Field("branch") String branch,
                         @Field("transport_type") String transport_type, @Field("from_route") String from_route,
                         @Field("to_route") String to_route, @Field("estimation_time") String estimation_time,
                                    @Field("user") String user,@Field("amount") String amount,@Field("paid") String paid,
                                    @Field("contact") String contact);

    @FormUrlEncoded
    @POST("transport/manage")
    Call<Login> addTransport(@Field("name") String name,@Field("branch") String branch,@Field("user") String user);

    @FormUrlEncoded
    @POST("transport/manage")
    Call<Login> updateTransport(@Field("name") String name,@Field("branch") String branch,@Field("user") String user,
                                @Field("id") String id);

    @FormUrlEncoded
    @POST("grades/manage")
    Call<Login> addGrade(@Field("grade") String grade,@Field("grade_amount") String grade_amount);

    @FormUrlEncoded
    @POST("grades/manage")
    Call<Login> updateGrade(@Field("grade") String grade,@Field("grade_amount") String grade_amount,
                                @Field("id") String id);

    @GET("transport/list")
    Call<TransportDTO> getTransportsList(@Query("branch") String branch);

    @GET("grades/list")
    Call<TransportDTO> getGradesList();

    @FormUrlEncoded
    @POST("employee/add")
    Call<Login> updateAdmin(@Field("first_name") String first_name, @Field("last_name") String last_name,
                         @Field("role") String role, @Field("mobile") String mobile,
                         @Field("dob") String dob, @Field("address") String address,
                         @Field("user") String user, @Field("email") String email,
                         @Field("latitude") String latitude, @Field("langitude") String langitude,
                         @Field("branch") String branch,@Field("employee") String employee);

    @GET("employee/list")
    Call<EmployeeDTO> getemployeeList(@Query("role") String role);

    @GET("employee/dealers")
    Call<EmployeeDTO> getDealersList(@Query("branch") String branch);

    @GET("employee/dealers")
    Call<EmployeeDTO> getDealersList(@Query("branch") String branch,@Query("user") String user,
                                     @Query("role") String role);

    @GET("employee/page_dealers")
    Call<EmployeeDTO> getDealerNamesList(@Query("branch") String branch);

    @GET("employee/dealers")
    Call<EmployeeDTO> getDealersLists(@Query("branch") String branch,@Query("role") String role,@Query("user") String user);

    @GET("employee/list")
    Call<EmployeeDTO> getEmployeesList(@Query("role") String role,@Query("branch") String branch);

    @GET("transport/list")
    Call<EmployeeDTO> getList(@Query("role") String role,@Query("branch") String branch);

    @GET("employee/licenses")
    Call<Login> getLicenses();


    @FormUrlEncoded
    @POST("fa/accept")
    Call<Login> submitRejectPoints(@Field("user") String user,@Field("role") String role,
                                   @Field("reason") String reason,
                                   @Field("status") String status, @Field("order") String order,
                                   @Field("rej_for") String rej_for);

    @FormUrlEncoded
    @POST("admin/accept")
    Call<Login> submitAdminRejectPoints(@Field("user") String user,@Field("role") String role,
                                   @Field("reason") String reason,
                                   @Field("status") String status, @Field("order") String order,
                                   @Field("rej_for") String rej_for);

   @GET("master/validate_mobile")
    Call<Login> authenticate(@Query("mobile") String mobile);

    @FormUrlEncoded
    @POST("manage_employee")
    Call<Login> submitUserProfile(@Field("user") String user, @Field("action") String action,@Field("image") String image, @Field("picName") String picName);


    @FormUrlEncoded
    @POST("master/dp")
    Call<Login> updateUserProfile(@Field("user") String user, @Field("role") String role,@Field("dp") String dp);

    @FormUrlEncoded
    @POST("pickorders/do_action")
    Call<Login> picker_orderstatus(@Field("action") String action, @Field("seller") String seller,
                                   @Field("primary_key") String primary_key, @Field("role") String role,
                                   @Field("item") String item, @Field("qty") String qty,
                                   @Field("description") String description,@Field("reason") String reason,
                                   @Field("invoice") String invoice);

    @FormUrlEncoded
    @POST("packers/pack_seller")
    Call<Login> pack_dispatcherorderstatus(@Field("action") String action, @Field("order") String order,
                                   @Field("item") String item, @Field("qty") String qty,@Field("reason") String reason,
                                   @Field("description") String description,@Field("user") String user,
                                           @Field("batch_no") String batch_no,@Field("exp_date") String exp_date,
                                           @Field("mfg_date") String mfg_date);

//    @GET("sellers/statement_details")
//    Call<Order> getWalletStatementDetails(@Query("user") String user);

    @FormUrlEncoded
    @POST("transferorders/transfer")
    Call<Login> submitTransferOrder(@Field("user") String user, @Field("order") String order,
                                 @Field("branch") String branch, @Field("item_qty") String item_qty);

    @FormUrlEncoded
    @POST("dealerorders/forward")
    Call<Login> submitForwordOrder(@Field("user") String user, @Field("order") String order);
}
package com.astrika.stywis_staff.network.services

import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.ProductAvailabilityDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface DashboardService {

    @POST(GET_DRAWER_MENU)
    fun getDrawerMenu(
        @Query("productId") productId: Long
    ): Call<ResponseBody>

    //Fetch Outlet Details
    @POST(FETCH_OUTLET_DETAILS)
    fun fetchOutletDetails(
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>


    //Get all Menu categories
    @POST(GET_ALL_MENU_CATEGORIES)
    fun getAllMenuCategories(
//        @Header("Authorization") access_token: String,
//        @Query("outletId") outletId: Long,
//        @Query("categoryType") categoryType: String
        @Body commonRequestDTO: CommonRequestDTO

    ): Call<ResponseBody>

    //Get all Dishes
    @POST(GET_ALL_DISHES)
    fun getAllDishes(
//        @Header("Authorization") access_token: String,
        /*@Query("catalogueCategoryId") productCategoryId: Long,
        @Query("outletId") outletId: Long,
        @Query("searchProduct") productName: String,
        @Query("categoryType") categoryType: String,
        @Body commonListingDTO: CommonListingDTO*/
        @Body commonRequestDTO: CommonRequestDTO
    ): Call<ResponseBody>


    //Save Dish Availability
    @POST(SAVE_DISH_AVAILABILITY)
    fun saveDishAvailability(
//        @Header("Authorization") access_token: String,
        @Query("categoryType") categoryType: String,
        @Body productAvailabilityList: ArrayList<ProductAvailabilityDTO>
    ): Call<ResponseBody>


    //Get Customization listing
    @POST(GET_CUSTOMIZATION_LISTING)
    fun getCustomizationListing(
        @Query("categoryType") categoryType: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    //Save Order Details
    @POST(GET_ORDER_DETAILS)
    fun getOrderDetails(
//        @Header("Authorization") access_token: String,
        @Query("inCustomerRequestId") inCustomerRequestId: Long
    ): Call<ResponseBody>

    //Save Order Details
    @POST(SAVE_ORDER_DETAILS)
    fun saveOrderDetails(
//        @Header("Authorization") access_token: String,
        @Body saveOrderDTO: SaveOrderDTO
    ): Call<ResponseBody>

/*
    //Get Check In Listing
    @POST(GET_CHECK_IN_LISTING)
    fun getCheckInListing(
        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>
*/


    //Get Check In Listing
    @POST(GET_CHECK_IN_LISTING)
    fun getCheckInListing(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long,
        @Query("requestTypes") requestTypes: String,
        @Query("requestStatuses") requestStatuses: ArrayList<String> // Share multiple queries with same query param name
    ): Call<ResponseBody>


    // Profile
    @POST(GET_PROFILE_DETAILS)
    fun getProfileDetails(
//        @Header("Authorization") access_token: String,
//        @Path("userId") userId: Long
        @Query("staffId") userId: Long

    ): Call<ResponseBody>

    @POST(UPDATE_PROFILE_DETAILS)
    fun updateProfileDetails(
//        @Header("Authorization") access_token: String,
        @Body userDetailsDTO: UserDetailsDTO
    ): Call<ResponseBody>


    /*


        @POST(GET_TABLE_LISTING)
        fun getTableListing(
    //        @Header("Authorization") access_token: String,
            @Body commonListingDTO: CommonListingDTO

        ): Call<ResponseBody>


        // Re Assign table
        @POST(RE_ASSIGN_TABLE_TO_USER)
        fun reAssignTable(
            @Query("tableIdOld") tableIdOld: Long,
            @Query("tableIdNew") tableIdNew: Long
        ): Call<ResponseBody>

    */
    //Change Status Approved
    @POST(CHANGE_STATUS_APPROVED)
    fun changeStatusApproved(
//        @Header("Authorization") access_token: String,
//        @Query("inCustomerRequestId") inCustomerRequestId: Long
        @Body commonRequestDTO: CommonRequestDTO
    ): Call<ResponseBody>

    //Change Status Approved
    @POST(CHANGE_DINE_IN_STATUS)
    fun changeDineInStatus(
//        @Header("Authorization") access_token: String,
        @Query("inCustomerRequestId") inCustomerRequestId: Long,
        @Query("status") status: String
    ): Call<ResponseBody>

    //Check Out
    @POST(CHECK_OUT)
    fun checkOut(
//        @Query("inCheckInCustomerRequestId") inCheckInCustomerRequestId: Long,
//        @Query("inCustomerRequestId") inCustomerRequestId: Long
        @Body commonRequestDTO: CommonRequestDTO
    ): Call<ResponseBody>

    @POST(VERIFY_GPIN_URL)
    fun verifyGPin(
        @Body gPinDTO: GPinDTO
    ): Call<ResponseBody>


    //Change Status Rejected
    @POST(CHANGE_STATUS_REJECTED)
    fun changeStatusRejected(
        @Query("inCustomerRequestId") inCustomerRequestId: Long,
        @Query("rejectedRemarks") rejectedRemarks: String
    ): Call<ResponseBody>


    @POST(GET_USER_BY_MOBILE)
    fun getUserByMobile(
//        @Header("Authorization") access_token: String,
        @Query("mobile") mobileNo: String
    ): Call<ResponseBody>

    @POST(SAVE_VISITOR_DETAILS)
    fun saveVisitorDetails(
//        @Header("Authorization") access_token: String,
        @Body userDetailsDTO: UserDetailsDTO
    ): Call<ResponseBody>

    @POST(VERIFY_OTP_URL)
    fun verifyOtpVisitor(
//        @Header("Authorization") access_token: String,
        @Query("mobileNo") mobileNo: String,
        @Query("otp") otp: String,
        @Query("transactionId") transactionId: String
    ): Call<ResponseBody>

    @POST(SEND_OTP_URL)
    fun sendOtpVisitor(
//        @Header("Authorization") access_token: String,
        @Query("mobileNo") mobileNo: String,
        @Query("message") message: String,
        @Query("transactionId") transactionId: String
    ): Call<ResponseBody>

    @POST(SAVE_STAFF_CHECKIN_VISITOR)
    fun checkInVisitor(
//        @Header("Authorization") access_token: String,
        @Body checkInDTO: CheckInDTO
    ): Call<ResponseBody>

    //Bill Generation
    @POST(GENERATE_BILl)
    fun generateBill(
//        @Header("Authorization") access_token: String,
//        @Query("userId") userId: Long,
//        @Query("outletId") outletId: Long,
//        @Query("inCustomerRequestId") inCustomerRequestId: Long,
//        @Query("inCheckInCustomerRequestId") inCheckInCustomerRequestId: Long
        @Body commonRequestDTO: CommonRequestDTO

    ): Call<ResponseBody>

    //Bill Generation
    @POST(GET_BILL_DETAILS_BY_ID)
    fun getBillDetails(
//        @Query("inCheckInCustomerRequestId") inCheckInCustomerRequestId: Long
        @Body commonRequestDTO: CommonRequestDTO
    ): Call<ResponseBody>

    // Change Bill Details Status as Paid
    @POST(CHANGE_BILL_DETAILS_STATUS_PAID)
    fun changeBillDetailsStatusToPaid(
//        @Query("inCheckInCustomerRequestId") inCheckInCustomerRequestId: Long
        @Body commonRequestDTO: CommonRequestDTO
    ): Call<ResponseBody>

    // Get the wishlist by user Id
    @POST(FETCH_WISHLIST_BY_USER_ID)
    fun getWishListByUserId(
        @Query("userId") userId: Long
    ): Call<ResponseBody>

    // change the wishlist item status by item Id
    @POST(CHANGE_WISHLIST_ITEM_STATUS_BY_ITEM_ID)
    fun changeWishListItemStatus(
        @Query("itemID") itemId: Long,
        @Query("status") status: String
    ): Call<ResponseBody>

    // delete the wishlist item status by item Id
    @POST(DELETE_WISHLIST_ITEM_STATUS_BY_ITEM_ID)
    fun deleteWishListItem(
        @Query("itemId") itemId: Long,
        @Query("status") status: Boolean
    ): Call<ResponseBody>


    // Stock

    // Save Stocks
    @POST(SAVE_STOCK_CUSTOMIZATIONS)
    fun saveStockCustomizations(
        @Body arrayList: ArrayList<StocksCustomizationDTO>
    ): Call<ResponseBody>

    // Fetch Stocks by product/Dish Id
    @POST(FETCH_ALL_STOCK_CUSTOMIZATIONS_BY_PRODUCT_OR_DISH_ID)
    fun fetchAllStockCustomizationsByProductOrDishId(
        @Query("productId") productId: Long,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

}
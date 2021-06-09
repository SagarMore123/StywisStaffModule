package com.astrika.stywis_staff.network.services

import com.astrika.stywis_staff.models.CommonListingDTO
import com.astrika.stywis_staff.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MastersService {

    // Outlet Types
    @GET(FETCH_SVM)
    fun fetchSVM(
//        @Header("Authorization") access_token: String
    ): Call<ResponseBody>

    // Outlet Types
    @POST(FETCH_VISIT_PURPOSE_MASTER)
    fun fetchVisitPurposeMaster(@Body commonListingDTO: CommonListingDTO): Call<ResponseBody>


    // Designation
    @POST(DESIGNATION_LISTING)
    fun fetchDesignation(
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>


    // Stock Customization Options Master
    @POST(FETCH_STOCK_CUSTOMIZATION_OPTION_MASTER)
    fun fetchStockCustomizationOptionsMaster(@Body commonListingDTO: CommonListingDTO): Call<ResponseBody>


    // Discount Category
    @POST(FETCH_DISCOUNT_CATEGORIES)
    fun fetchDiscountCategories(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    //  Discount Membership Plan // Basic, Corporate Plan
    @POST(FETCH_DISCOUNT_MEMBERSHIP_PLAN)
    fun fetchDiscountMembershipPlan(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Cravx Card Discount Membership Holder
    @POST(FETCH_CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchCravxCardsDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // HW Discount Membership Holder
    @POST(FETCH_HW_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchHWDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // In-House Discount Membership Holder
    @POST(FETCH_IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchInHouseDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>


}


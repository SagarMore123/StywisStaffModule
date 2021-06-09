package com.astrika.stywis_staff.network.services

import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {

    @POST(IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID)
    fun isFirstTimeLoginWithLoginId(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(LOGIN_MASTERS)
    fun loginMasters(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(REFRESH_TOKEN)
    fun refreshToken(@Body refreshTokenDTO: RefreshTokenDTO): Call<AccessTokenDTO>

    @POST(LOGIN_WITH_LOGIN_ID)
    fun login(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(LOGIN_WITH_LOGIN_ID)
    fun verifyOtp(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(RESET_PASSWORD)
    fun resetPassword(
//        @Header("Authorization") accessToken: String,
        @Body resetPassword: ResetPassword
    ): Call<ResponseBody>

    @POST(LOGOUT_USER_URL)
    fun logoutUser(
//        @Header("Authorization") access_token: String
    ): Call<ResponseBody>


    //Fetch Discount Details List
    @POST(FETCH_OUTLET_APPLICABLE_DISCOUNTS_LIST)
    fun fetchOutletApplicableDiscountsList(
/*
        @Query("outletId") outletId: Long,
        @Query("userId") userId: Long,
        @Query("weekDay") weekDay: Long,
        @Query("weekend") weekend: Boolean,
        @Query("weekly") weekly: Boolean
*/
        @Body commonRequestDTO: CommonRequestDTO

    ): Call<ResponseBody>

    //Fetch Discount Details List
    @POST(FETCH_OUTLET_DISCOUNT_DETAILS_LIST)
    fun fetchOutletDiscountDetailsList(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Fetch Membership Plan Mapping
    @POST(FETCH_OUTLET_MEMBERSHIP_PLAN_MAPPING)
    fun fetchOutletMembershipPlanMapping(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Fetch One Dashboard Discount or Corporate Membership Masters
    @POST(FETCH_OUTLET_CORPORATE_MEMBERSHIP_OR_ONE_DASHBOARD)
    fun fetchOutletOneDashboardMasterDetails(
//        @Header("Authorization") access_token: String,
        @Query("productId") productId: Long,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

}
package com.astrika.stywis_staff.network

import android.content.Context
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.ProductAvailabilityDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.network.network_utils.NetworkResponseCallback
import com.astrika.stywis_staff.network.network_utils.NetworkUtils.Companion.HTTP_RETROFIT_FAILURE
import com.astrika.stywis_staff.network.network_utils.NetworkUtils.Companion.HTTP_SUCCESS
import com.astrika.stywis_staff.network.network_utils.NetworkUtils.Companion.getStringResponseFromRaw
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.Constants.Companion.CATALOGUE_CATEGORY_TYPE
import com.astrika.stywis_staff.utils.CustomGsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkController {

    companion object {

        var instance: NetworkController? = null
        lateinit var mContext: Context

        const val SERVER_ERROR = "Something went wrong on the server"

        fun getInstance(context: Context): NetworkController {
            mContext = context

            if (instance == null) {
                instance = NetworkController()
            }
            accessToken = Constants.getAccessToken(mContext) ?: ""
            outletId = Constants.getOutletId(mContext)

            return instance as NetworkController
        }

        const val contentType = "application/json"
        var accessToken = ""
        var outletId = 0L

    }

    class RetrofitServiceTask(var networkResponseCallback: NetworkResponseCallback) :
        Callback<ResponseBody> {

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.code() == HTTP_SUCCESS) {
                getStringResponseFromRaw(response)?.let { data ->
                    networkResponseCallback.onSuccess(data)
                }
            } else {
                var errorMsg = SERVER_ERROR
                val jsonError = getStringResponseFromRaw(response.errorBody()!!)
                try {
                    val jsonObject = JSONObject(jsonError.toString())
                    if (jsonObject.has("error")) {
                        if (jsonObject.getString("error")
                                .equals("invalid_token", ignoreCase = true)
                        ) {
//                            val intent = Intent(mContext, UserLoginActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            intent.putExtra(Constants.ACCESS_TOKEN_INVALID, true)
                            Constants.clearSharedPrefs(mContext)
//                            mContext.startActivity(intent)

//                            accessToken = Constants.getRefreshToken(mContext) ?: ""
                        }

                        if (!jsonObject.getJSONObject("error").getString("message")
                                .isNullOrBlank()
                        ) {
                            networkResponseCallback.onError(
                                response.code(),
                                jsonObject.getJSONObject("error").getString("message")
                            )
                        } else {
                            networkResponseCallback.onError(response.code(), SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    networkResponseCallback.onError(response.code(), SERVER_ERROR)
                }

            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            try {


                if (t.message != null) {
                    if (t.message?.contains("Failed") == true || t.message?.contains("failed to connect") == true) {
                        networkResponseCallback.onError(
                            HTTP_RETROFIT_FAILURE,
                            mContext.resources.getString(R.string.network_failure_string)
                        )
                    } else {
                        networkResponseCallback.onError(500, SERVER_ERROR)
                    }
                } else {
                    networkResponseCallback.onError(500, SERVER_ERROR)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    // returns CommonResponseDTO
    fun commonResponseDTO(data: String, callback: IDataSourceCallback<CommonResponseDTO>) {

        try {

            if (data.isNotBlank()) {

                val jsonObject = JSONObject(data)
                val gson = CustomGsonBuilder().getInstance().create()
                val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                    jsonObject.toString(),
                    CommonResponseDTO::class.java
                )

                when {
                    commonResponseDTO.success != null -> {
                        callback.onDataFound(commonResponseDTO)
                    }
                    commonResponseDTO.error != null -> {
                        callback.onError(commonResponseDTO.error.message)
                    }
                    else -> {
                        callback.onError(SERVER_ERROR)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            callback.onError(SERVER_ERROR)
        }

    }

    // returns Message String
    fun commonResponseString(data: String, callback: IDataSourceCallback<String>) {

        try {

            if (data.isNotBlank()) {

                val jsonObject = JSONObject(data)
                val gson = CustomGsonBuilder().getInstance().create()
                val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                    jsonObject.toString(),
                    CommonResponseDTO::class.java
                )

                when {
                    commonResponseDTO.success != null -> {
                        callback.onDataFound(commonResponseDTO.success.message)
                    }
                    commonResponseDTO.error != null -> {
                        callback.onError(commonResponseDTO.error.message)
                    }
                    else -> {
                        callback.onError(SERVER_ERROR)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            callback.onError(SERVER_ERROR)
        }

    }


    fun isFirstTimeLoginWithLoginId(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.isFirstTimeLoginWithLoginId(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun loginMasters(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        loginDTO.username = "visitor@syspiretechnologies.com"
        loginDTO.password = Constants.passwordEncrypt("P@ssw0rd")
        val response = UserApi.retrofitService.loginMasters(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun login(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.verifyOtp(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    /*

        fun refreshToken(refreshTokenDTO: RefreshTokenDTO, callback: NetworkResponseCallback) {
            val response = UserApi.retrofitService.refreshToken(refreshTokenDTO)
            val responseCall: Call<ResponseBody> = response
            responseCall.enqueue(RetrofitServiceTask(callback))
        }

    */
    fun verifyOtp(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.verifyOtp(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun resetPassword(resetPassword: ResetPassword, callback: NetworkResponseCallback) {
//        val response = UserApi.retrofitService.resetPassword(accessToken, resetPassword)
        val response = UserApi.retrofitService.resetPassword(resetPassword)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // logout service
    fun logoutUser(
        callback: NetworkResponseCallback
    ) {
        val response =
//            UserApi.retrofitService.logoutUser(accessToken)
            UserApi.retrofitService.logoutUser()
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getDrawerMenu(callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.getDrawerMenu(
            Constants.PRODUCT_ID_VALUE
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun fetchOutletDetails(callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.fetchOutletDetails(
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun getAllMenuCategories(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.outletId = outletId
        commonRequestDTO.categoryType = CATALOGUE_CATEGORY_TYPE

        val response = UserApi.retrofitDashboardService.getAllMenuCategories(
            /*outletId,
            CATALOGUE_CATEGORY_TYPE*/
            commonRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllDishes(
        productCategoryId: Long,
        outletId: Long,
        productName: String,
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.catalogueCategoryId = productCategoryId
        commonRequestDTO.outletId = outletId
        commonRequestDTO.searchProduct = productName
        commonRequestDTO.categoryType = CATALOGUE_CATEGORY_TYPE
        commonRequestDTO.commonListingDTO = commonListingDTO

        val response = UserApi.retrofitDashboardService.getAllDishes(
//            accessToken,
            /*productCategoryId,
            outletId,
            productName,
            CATALOGUE_CATEGORY_TYPE,
            commonListingDTO*/
            commonRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveDishAvailability(
        productAvailabilityList: ArrayList<ProductAvailabilityDTO>,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveDishAvailability(
//            accessToken,
            CATALOGUE_CATEGORY_TYPE,
            productAvailabilityList
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    //get customization listing
    fun getCustomizationListing(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.getCustomizationListing(
                CATALOGUE_CATEGORY_TYPE,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getOrderDetails(
        inCustomerRequestId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getOrderDetails(
//            accessToken,
            inCustomerRequestId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveOrderDetails(
        saveOrderDTO: SaveOrderDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveOrderDetails(
//            accessToken,
            saveOrderDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
/*

    fun fetchCheckInListing(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.getCheckInListing(accessToken, commonListingDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
*/

    fun fetchCheckInListing(
        outletId: Long,
        requestTypes: String,
        requestStatuses: ArrayList<String>,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.getCheckInListing(
                outletId,
                requestTypes,
                requestStatuses
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // SVM
    fun fetchSVM(callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchSVM(
//            accessToken
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    // Visit Purpose
    fun fetchVisitPurpose(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchVisitPurposeMaster(
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    // Designation listing
    fun fetchDesignation(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchDesignation(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    // Stock Customization Option
    fun fetchStockCustomizationOptionsMaster(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchStockCustomizationOptionsMaster(
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

/*

    fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.saveTableListing(
                tableListManagementDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun getTableListing(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.getTableListing(
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun reAssignTable(tableIdOld: Long, tableIdNew: Long, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.reAssignTable(
                tableIdOld,
                tableIdNew
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


*/

    fun getProfileDetails(userId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.getProfileDetails(
//            accessToken,
            userId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun updateProfileDetails(userDetailsDTO: UserDetailsDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.updateProfileDetails(
                userDetailsDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeStatusApproved(inCustomerRequestId: Long, callback: NetworkResponseCallback) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.inCustomerRequestId = inCustomerRequestId

        val response =
            UserApi.retrofitDashboardService.changeStatusApproved(
//                inCustomerRequestId
                commonRequestDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeDineInStatus(
        inCustomerRequestId: Long,
        status: String,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.changeDineInStatus(
                inCustomerRequestId,
                status
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun checkOut(
        inCheckInCustomerRequestId: Long,
        inCustomerRequestId: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.inCheckInCustomerRequestId = inCheckInCustomerRequestId
        commonRequestDTO.inCustomerRequestId = inCustomerRequestId
        val response =
            UserApi.retrofitDashboardService.checkOut(
//                inCheckInCustomerRequestId,
//                inCustomerRequestId
                commonRequestDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun verifyGPin(gPinDTO: GPinDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.verifyGPin(
                gPinDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun changeStatusRejected(
        inCustomerRequestId: Long,
        rejectedRemarks: String,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.changeStatusRejected(
                inCustomerRequestId,
                rejectedRemarks
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getUserByMobile(mobileNo: String, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.getUserByMobile(
//            accessToken,
            mobileNo
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveVisitorDetails(userDetailsDTO: UserDetailsDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.saveVisitorDetails(
                userDetailsDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun verifyOtpVisitor(mobileNo: String, otp: String, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.verifyOtpVisitor(
                mobileNo, otp, "1"
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun sendOtpVisitor(mobileNo: String, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.sendOtpVisitor(
                mobileNo,
                Constants.CHECK_IN_OTP,
                "1"
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun checkInVisitor(checkInDTO: CheckInDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.checkInVisitor(
                checkInDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun generateBill(
        userId: Long,
        outletId: Long,
        inCustomerRequestId: Long,
        inCheckInCustomerRequestId: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.userId = userId
        commonRequestDTO.outletId = outletId
        commonRequestDTO.inCustomerRequestId = inCustomerRequestId
        commonRequestDTO.inCheckInCustomerRequestId = inCheckInCustomerRequestId
        val response = UserApi.retrofitDashboardService.generateBill(
//            accessToken,
            /*userId,
            outletId,
            inCustomerRequestId,
            inCheckInCustomerRequestId*/
            commonRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun getBillDetails(
        inCheckInCustomerRequestId: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.inCheckInCustomerRequestId = inCheckInCustomerRequestId
        val response = UserApi.retrofitDashboardService.getBillDetails(
//            inCheckInCustomerRequestId
            commonRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeBillDetailsStatusToPaid(
        inCheckInCustomerRequestId: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.inCheckInCustomerRequestId = inCheckInCustomerRequestId

        val response = UserApi.retrofitDashboardService.changeBillDetailsStatusToPaid(
//            inCheckInCustomerRequestId
            commonRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    // Discount Section

    fun fetchOutletApplicableDiscountsList(
        outletId: Long,
        userId: Long,
        checkInId: Long,
        weekDay: Long,
        callback: NetworkResponseCallback
    ) {
        val commonRequestDTO = CommonRequestDTO()
        commonRequestDTO.outletId = outletId
        commonRequestDTO.userId = userId
        commonRequestDTO.checkInId = checkInId
        commonRequestDTO.weekDay = weekDay
        commonRequestDTO.weekend = false
        commonRequestDTO.weekly = false

        val response = UserApi.retrofitService.fetchOutletApplicableDiscountsList(
            commonRequestDTO
            /*outletId, userId, weekDay,
            weekend = false,
            weekly = false*/
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun fetchOutletDiscountDetailsList(outletId: Long, callback: NetworkResponseCallback) {
//        val response = UserApi.retrofitService.fetchOutletDiscountDetailsList(accessToken, outletId)
        val response = UserApi.retrofitService.fetchOutletDiscountDetailsList(outletId)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun getWishListByUserId(userId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.getWishListByUserId(userId)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeWishListItemStatus(itemId: Long, status: String, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.changeWishListItemStatus(itemId, status)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun deleteWishListItem(itemId: Long, status: Boolean, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.deleteWishListItem(itemId, status)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    // StocksCustomization

    fun saveStockCustomizations(
        arrayList: ArrayList<StocksCustomizationDTO>,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveStockCustomizations(arrayList)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun fetchAllStockCustomizationsByProductOrDishId(
        productId: Long,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.fetchAllStockCustomizationsByProductOrDishId(
                productId,
                outletId
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


}
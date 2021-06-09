package com.astrika.stywis_staff.source.dashboard.remote

import android.content.Context
import android.content.SharedPreferences
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.GetDishListResponseDTO
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails
import com.astrika.stywis_staff.models.menu.ProductAvailabilityDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.NetworkController
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.network.network_utils.NetworkResponseCallback
import com.astrika.stywis_staff.source.dashboard.DashboardDataSource
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomGsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class DashboardRemoteDataSource : DashboardDataSource {

    companion object {
        private var instance: DashboardRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context?): DashboardDataSource? {
            mContext = context
            networkController = NetworkController.getInstance(mContext!!)
            sharedPreferences =
                Constants.getSharedPreferences(context!!.applicationContext)
            if (instance == null) {
                instance = DashboardRemoteDataSource()
            }
            return instance
        }

    }

    override fun fetchCheckInListing(
        outletId: Long,
        requestTypes: String,
        requestStatuses: ArrayList<String>,
        callback: IDataSourceCallback<ArrayList<CheckInDTO>>
    ) {
        networkController?.fetchCheckInListing(
            outletId,
            requestTypes,
            requestStatuses, object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val response: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            if (response.success != null) {
                                if (response.success.status == "200") {

                                    when {

                                        jsonObject.has("inList") -> {
                                            val checkInListJsonArray =
                                                jsonObject.getJSONArray("inList")
                                            val checkInList: ArrayList<CheckInDTO> =
                                                gson.fromJson(
                                                    checkInListJsonArray.toString(),
                                                    object :
                                                        TypeToken<ArrayList<CheckInDTO?>>() {}.type
                                                )
                                            callback.onDataFound(checkInList)
                                        }
                                        response.error?.message != null -> {
                                            callback.onError(response.error.message)
                                        }
                                        else -> {
                                            callback.onDataNotFound()
                                        }
                                    }
                                }
//                            callback.onDataFound(data)
                            } else {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }

                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun getAllMenuCategories(
        outletId: Long,
        callback: IDataSourceCallback<GetMenuCategoriesResponseDTO>
    ) {
        networkController?.getAllMenuCategories(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: GetMenuCategoriesResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetMenuCategoriesResponseDTO::class.java
                        )

                        if (response.success != null) {
                            callback.onDataFound(response)
                            if (response.activeCatalogueCategoryDTOs.isNotEmpty()) {
                            } else {
                                callback.onDataNotFound()
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }

                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }


            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAllDishes(
        productCategoryId: Long,
        outletId: Long,
        productName: String,
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<MenuSectionWithDishDetails>>
    ) {
        networkController?.getAllDishes(productCategoryId, outletId, productName, commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (!data.isNullOrBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val response: GetDishListResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                GetDishListResponseDTO::class.java
                            )

                            if (response.success != null) {
                                if (response.productSectionListingDTOs.isNotEmpty()) {
                                    callback.onDataFound(response.productSectionListingDTOs)
                                } else {
                                    callback.onDataNotFound()
                                }
                            } else if (response.error != null) {
                                callback.onError(response.error.message)
                            } else {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                    } catch (e: Exception) {
                        callback.onError("Error")
                    }
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun saveDishAvailability(
        productAvailabilityList: ArrayList<ProductAvailabilityDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveDishAvailability(
            productAvailabilityList,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun getCustomizationListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CustomizationDTO>
    ) {
        networkController?.getCustomizationListing(
            commonListingDTO,
            object : NetworkResponseCallback {

                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val response: CustomizationDTO = gson.fromJson(
                                jsonObject.toString(),
                                CustomizationDTO::class.java
                            )
                            if (response != null) {
                                callback.onDataFound(response)
                            } else {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    // Fetch Order Details
    override fun getOrderDetails(
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<OrderDTO>
    ) {
        networkController?.getOrderDetails(
            inCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val response: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {

                                jsonObject.has("orderDto") -> {
                                    val checkInListJsonArray =
                                        jsonObject.optJSONObject("orderDto")
                                    val orderDTO: OrderDTO =
                                        gson.fromJson(
                                            checkInListJsonArray?.toString(),
                                            object :
                                                TypeToken<OrderDTO>() {}.type
                                        )
                                    callback.onDataFound(orderDTO)
                                }
                                response.error?.message != null -> {
                                    callback.onError(response.error.message)
                                }
                                else -> {
                                    callback.onDataNotFound()
                                }
                            }

                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }

                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    // Save Order Details
    override fun saveOrderDetails(
        saveOrderDTO: SaveOrderDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOrderDetails(saveOrderDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

/*
    override fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveTableListing(
            tableListManagementDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun getTableListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        networkController?.getTableListing(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            if (response.success.status == "200") {
                                when {
                                    jsonObject.has("TableList") -> {
                                        val tableListJsonArray =
                                            jsonObject.getJSONArray("TableList")
                                        val tableList: ArrayList<TableManagementDTO> =
                                            gson.fromJson(
                                                tableListJsonArray.toString(),
                                                object :
                                                    TypeToken<ArrayList<TableManagementDTO?>>() {}.type
                                            )
                                        callback.onDataFound(tableList)
                                    }
                                    response.error?.message != null -> {
                                        callback.onError(response.error.message)
                                    }
                                    else -> {
                                        callback.onError(NetworkController.SERVER_ERROR)
                                    }
                                }
                            }
//                            callback.onDataFound(data)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun reAssignTable(
        tableIdOld: Long, tableIdNew: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.reAssignTable(
            tableIdOld, tableIdNew,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }
*/


    override fun getProfileDetails(userId: Long, callback: IDataSourceCallback<UserDTO>) {
        networkController?.getProfileDetails(userId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        if (jsonObject.has("userMasterDTO")) {

                            val userJson = jsonObject.optJSONObject("userMasterDTO")
                            val gson = CustomGsonBuilder().getInstance().create()
                            val userDTO = gson.fromJson(userJson.toString(), UserDTO::class.java)

                            callback.onDataFound(userDTO)

                        } else if (jsonObject.has("error")) {
                            val error = jsonObject.getJSONObject("error")
                            val errorMessage = error.getString("message")
                            callback.onError(errorMessage)

                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }

        })
    }


    override fun updateProfileDetails(
        userDetailsDTO: UserDetailsDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.updateProfileDetails(userDetailsDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                callback.onDataFound("Profile image updated successfully")
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }


    override fun changeStatusApproved(
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeStatusApproved(
            inCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun changeDineInStatus(
        inCustomerRequestId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeDineInStatus(
            inCustomerRequestId,
            status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun checkOut(
        inCheckInCustomerRequestId: Long,
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.checkOut(
            inCheckInCustomerRequestId,
            inCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun verifyGPin(gPinDTO: GPinDTO, callback: IDataSourceCallback<String>) {
        networkController?.verifyGPin(gPinDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }


    override fun changeStatusRejected(
        inCustomerRequestId: Long,
        rejectedRemarks: String,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeStatusRejected(
            inCustomerRequestId,
            rejectedRemarks,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun getUserByMobile(
        mobileNo: String,
        callback: IDataSourceCallback<UserDTO>
    ) {

        networkController?.getUserByMobile(mobileNo, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)

                        if (jsonObject.has("userId")) {
                            Constants.userId = jsonObject.getLong("userId")
                        }

                        if (jsonObject.has("userMasterDTO")) {

                            val userJson = jsonObject.optJSONObject("userMasterDTO")
                            val gson = CustomGsonBuilder().getInstance().create()
                            val userDTO = gson.fromJson(userJson.toString(), UserDTO::class.java)

                            callback.onDataFound(userDTO)

                        } else if (jsonObject.has("error")) {
                            val error = jsonObject.getJSONObject("error")
                            val errorMessage = error.getString("message")
                            callback.onError(errorMessage)

                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                if (errorCode == 404 && errorData == "User not found") {
                    callback.onDataNotFound()
                } else {
                    callback.onError(errorData)
                }
            }

        })

    }

    override fun saveVisitorDetails(
        userDetailsDTO: UserDetailsDTO,
        callback: IDataSourceCallback<VisitorResponseDTO>
    ) {
        networkController?.saveVisitorDetails(userDetailsDTO, object : NetworkResponseCallback {

            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()

                        if (jsonObject.has("status")) {
                            if (jsonObject.getInt("status") == 200) {
                                val visitorResponseDTO = gson.fromJson(
                                    jsonObject.toString(),
                                    VisitorResponseDTO::class.java
                                )
                                callback.onDataFound(visitorResponseDTO)
                            }
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun verifyOtpVisitor(
        mobileNo: String,
        otp: String,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.verifyOtpVisitor(mobileNo, otp, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotBlank()) {
                    val jsonObject = JSONObject(data)
                    if (jsonObject.has("201")) {
                        val successMessage = jsonObject.getString("201")
                        callback.onDataFound(successMessage)
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun sendOtpVisitor(
        mobileNo: String,
        callback: IDataSourceCallback<VisitorResponseDTO>
    ) {
        networkController?.sendOtpVisitor(mobileNo, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotBlank()) {
                    val jsonObject = JSONObject(data)
                    val gson = CustomGsonBuilder().getInstance().create()
                    if (jsonObject.has("otp")) {
                        val visitorResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            VisitorResponseDTO::class.java
                        )
                        callback.onDataFound(visitorResponseDTO)
                    }
                } else {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun checkInVisitor(checkInDTO: CheckInDTO, callback: IDataSourceCallback<String>) {
        networkController?.checkInVisitor(checkInDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
/*
                if(data.isNotBlank()){
                    val jsonObject = JSONObject(data)
                    if(jsonObject.has("201")){
                        val successMessage = jsonObject.getString("201")
                        callback.onDataFound(successMessage)
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
*/

                networkController?.commonResponseString(data, callback)

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }


    override fun generateBill(
        userId: Long,
        outletId: Long,
        inCustomerRequestId: Long,
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<BillGenerationDTO>
    ) {
        networkController?.generateBill(userId,
            outletId,
            inCustomerRequestId,
            inCheckInCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("billDetailsDTO") -> {

                                    val billDTO =
                                        jsonObject.optJSONObject("billDetailsDTO")

                                    val data: BillGenerationDTO =
                                        gSon.fromJson(
                                            billDTO?.toString(),
                                            object :
                                                TypeToken<BillGenerationDTO?>() {}.type
                                        )
                                    if (data != null) {
                                        callback.onDataFound(data)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
                            }
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }

                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun getBillDetails(
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<BillGenerationDTO>
    ) {
        networkController?.getBillDetails(inCheckInCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("billingDetailsDto") -> {

                                    val billDTO =
                                        jsonObject.optJSONObject("billingDetailsDto")

                                    val data: BillGenerationDTO =
                                        gSon.fromJson(
                                            billDTO?.toString(),
                                            object :
                                                TypeToken<BillGenerationDTO?>() {}.type
                                        )
                                    if (data != null) {
                                        callback.onDataFound(data)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
                            }
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }

                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun changeBillDetailsStatusToPaid(
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeBillDetailsStatusToPaid(inCheckInCustomerRequestId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun getWishListByUserId(userId: Long, callback: IDataSourceCallback<WishListDTO>) {
        networkController?.getWishListByUserId(userId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("wishlistDTO") -> {

                                val wishListJsonObject =
                                    jsonObject.optJSONObject("wishlistDTO")

                                if (wishListJsonObject != null) {
                                    val wishListDTO: WishListDTO =
                                        gSon.fromJson(
                                            wishListJsonObject.toString(),
                                            object :
                                                TypeToken<WishListDTO?>() {}.type
                                        )
                                    if (wishListDTO != null) {
                                        callback.onDataFound(wishListDTO)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                } else {
                                    callback.onDataNotFound()
                                }

                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }

                } catch (e: Exception) {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun changeWishListItemStatus(
        itemId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeWishListItemStatus(itemId, status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun deleteWishListItem(
        itemId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.deleteWishListItem(itemId, status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun saveStockCustomizations(
        arrayList: ArrayList<StocksCustomizationDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveStockCustomizations(
            arrayList,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


    override fun fetchAllStockCustomizationsByProductOrDishId(
        productId: Long,
        callback: IDataSourceCallback<ArrayList<StocksCustomizationDTO>>
    ) {
        networkController?.fetchAllStockCustomizationsByProductOrDishId(
            productId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val response: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            if (response.success != null) {
                                if (response.success.status == "200") {

                                    when {

                                        jsonObject.has("stockDTOList") -> {
                                            val array =
                                                jsonObject.optJSONArray("stockDTOList")
                                            if (array != null) {
                                                val checkInList: ArrayList<StocksCustomizationDTO> =
                                                    gson.fromJson(
                                                        array.toString(),
                                                        object :
                                                            TypeToken<ArrayList<StocksCustomizationDTO?>>() {}.type
                                                    )
                                                if (!checkInList.isNullOrEmpty()) {
                                                    callback.onDataFound(checkInList)
                                                } else {
                                                    callback.onError(NetworkController.SERVER_ERROR)
                                                }
                                            } else {
                                                callback.onError(NetworkController.SERVER_ERROR)
                                            }
                                        }
                                        response.error?.message != null -> {
                                            callback.onError(response.error.message)
                                        }
                                        else -> {
                                            callback.onDataNotFound()
                                        }
                                    }
                                }
//                            callback.onDataFound(data)
                            } else {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }

                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }


}
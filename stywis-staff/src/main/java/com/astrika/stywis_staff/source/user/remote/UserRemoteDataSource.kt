package com.astrika.stywis_staff.source.user.remote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.NetworkController
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.network.network_utils.NetworkResponseCallback
import com.astrika.stywis_staff.source.user.UserDataSource
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomGsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class UserRemoteDataSource : UserDataSource {

    companion object {
        private var instance: UserRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context?): UserDataSource? {
            mContext = context
            networkController = NetworkController.getInstance(mContext!!)
            sharedPreferences =
                Constants.getSharedPreferences(context!!.applicationContext)
            if (instance == null) {
                instance = UserRemoteDataSource()
            }
            return instance
        }

    }

    override fun isFirstTimeLoginWithLoginId(
        loginDTO: LoginDTO,
        callback: IDataSourceCallback<LoginResponseDTO>
    ) {
        networkController?.isFirstTimeLoginWithLoginId(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val loginResponseDTO =
                            gson.fromJson(jsonObject.toString(), LoginResponseDTO::class.java)

                        if (loginResponseDTO.success != null && loginResponseDTO.error == null) {
                            if (loginResponseDTO.success.status == "200") {
                                callback.onDataFound(loginResponseDTO)
                            }
                        } else {
                            if (loginResponseDTO.error != null) {
                                if (loginResponseDTO.error.message != null) {
                                    if (loginResponseDTO.error.message.contains("blocked")) {
                                        callback.onError(loginResponseDTO.error.message)
                                    } else {
                                        callback.onError(loginResponseDTO.error.message)
                                    }
                                }

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

    override fun login(loginDTO: LoginDTO, callback: IDataSourceCallback<UserDTO>) {
        networkController?.login(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        var accessToken = ""
                        if (jsonObject.has("userId")) {
                            val userId = jsonObject.optLong("userId")
                            userId.let {
                                sharedPreferences.edit().putString(
                                    Constants.LOGIN_USER_ID,
                                    Constants.encrypt(userId.toString())
                                ).apply()
                            }
                        }
                        if (jsonObject.has("data")) {
                            val dataObject = jsonObject.getJSONObject("data")

                            // Access Token
                            if (dataObject.has("access_token")) {
                                accessToken = dataObject.getString("access_token")
                                Log.e("accessToken", accessToken)
                                NetworkController.accessToken = "Bearer $accessToken"
                                sharedPreferences.edit()?.putString(
                                    Constants.ACCESS_TOKEN,
                                    Constants.encrypt(accessToken)
                                )?.apply()
                            }

                            // Refresh Token
                            if (dataObject.has("refresh_token")) {
                                accessToken = dataObject.getString("refresh_token")
                                Log.e("refreshToken", accessToken)
                                sharedPreferences.edit()?.putString(
                                    Constants.REFRESH_TOKEN,
                                    Constants.encrypt(accessToken)
                                )?.apply()
                            }

                            if (dataObject.has("user")) {
                                val userJson = dataObject.getJSONObject("user")
                                val gson = CustomGsonBuilder().getInstance().create()
                                val userDTO =
                                    gson.fromJson(userJson.toString(), UserDTO::class.java)

                                mContext?.let {
                                    Constants.setUserDTOInSharePreferences(
                                        it,
                                        userDTO
                                    )
                                }
//                                sharedPreferences.edit().putString(Constants.LOGIN_DTO,Constants.encrypt(Utils.setLoginDTO(loginDTO))).apply()
                                callback.onDataFound(userDTO)
                            }
                        } else {
                            if (jsonObject.has("error")) {
                                val error = jsonObject.getJSONObject("error")
                                val errorMessage = error.getString("message")
                                callback.onError(errorMessage)
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

    override fun verifyOtp(loginDTO: LoginDTO, callback: IDataSourceCallback<String>) {
        networkController?.verifyOtp(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        commonCallback(data, callback)
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

    override fun resetPassword(
        resetPassword: ResetPassword,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.resetPassword(resetPassword, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        commonCallback(data, callback)
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

    private fun commonCallback(
        data: String,
        callback: IDataSourceCallback<String>
    ) {
        val jsonObject = JSONObject(data)
        var accessToken = ""
        if (jsonObject.has("data")) {
            val dataObject = jsonObject.getJSONObject("data")
            if (dataObject.has("access_token")) {
                accessToken = dataObject.getString("access_token")
                NetworkController.accessToken = "Bearer $accessToken"
                sharedPreferences.edit()?.putString(
                    Constants.ACCESS_TOKEN,
                    Constants.encrypt(accessToken)
                )?.apply()
            }
            callback.onDataFound("success")
        } else {
            if (jsonObject.has("error")) {
                val error = jsonObject.getJSONObject("error")
                val errorMessage = error.getString("message")
                callback.onError(errorMessage)
            }
        }
    }


    // Discount
    override fun fetchOutletApplicableDiscountsList(
        outletId: Long,
        userId: Long,
        checkInId: Long,
        weekDay: Long,
        callback: IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>>
    ) {

        networkController?.fetchOutletApplicableDiscountsList(
            outletId, userId, checkInId, weekDay,
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

                                jsonObject.has("outletDiscountDetails") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("outletDiscountDetails")

                                    val arrayList: ArrayList<OutletDiscountDetailsDTO> =
                                        gSon.fromJson<ArrayList<OutletDiscountDetailsDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<OutletDiscountDetailsDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
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

    override fun fetchOutletDiscountDetailsList(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>>
    ) {

        networkController?.fetchOutletDiscountDetailsList(
            outletId,
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

                                jsonObject.has("outletDiscountDetails") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("outletDiscountDetails")

                                    val arrayList: ArrayList<OutletDiscountDetailsDTO> =
                                        gSon.fromJson<ArrayList<OutletDiscountDetailsDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<OutletDiscountDetailsDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
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


    /* override fun fetchTimings(outletId: Long, callback: IDataSourceCallback<OutletTimingDTO>) {

         networkController?.fetchTimings(outletId, object : NetworkResponseCallback {
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

                             jsonObject.has("outletTimingDTO") -> {

                                 val outLetTimingsJsonObject =
                                     jsonObject.getJSONObject("outletTimingDTO")

                                 val outletTimingDTO: OutletTimingDTO =
                                     gSon.fromJson<OutletTimingDTO>(
                                         outLetTimingsJsonObject.toString(),
                                         object :
                                             TypeToken<OutletTimingDTO?>() {}.type
                                     )
                                 if (!outletTimingDTO.timings.isNullOrEmpty()) {
                                     callback.onDataFound(outletTimingDTO)
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

     override fun saveTimings(
         timings: OutletTimingDTO,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.saveTimings(timings, object : NetworkResponseCallback {
             override fun onSuccess(data: String) {
                 NetworkController().commonResponseString(data, callback)
             }

             override fun onError(errorCode: Int, errorData: String) {
                 callback.onError(errorData)
             }

         })
     }

     // Closed Dates
     override fun deleteOutletClosedDate(
         outletDateId: Long,
         status: Boolean,
         callback: IDataSourceCallback<String>
     ) {

         networkController?.deleteOutletClosedDate(
             outletDateId,
             status,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }

     override fun fetchOutletClosedDates(
         outletId: Long,
         callback: IDataSourceCallback<ArrayList<ClosedDatesDTO>>
     ) {

         networkController?.fetchOutletClosedDates(outletId, object : NetworkResponseCallback {
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

                             jsonObject.has("outletDateRestriction") -> {

                                 val outLetTimingsJsonArray =
                                     jsonObject.getJSONArray("outletDateRestriction")

                                 val closedDatesListing: ArrayList<ClosedDatesDTO> =
                                     gSon.fromJson<ArrayList<ClosedDatesDTO>>(
                                         outLetTimingsJsonArray.toString(),
                                         object :
                                             TypeToken<ArrayList<ClosedDatesDTO>>() {}.type
                                     )
                                 if (!closedDatesListing.isNullOrEmpty()) {
                                     callback.onDataFound(closedDatesListing)
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

     override fun saveOutletClosedDates(
         closedDate: ClosedDatesDTO,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.saveOutletClosedDates(closedDate, object : NetworkResponseCallback {
             override fun onSuccess(data: String) {
                 NetworkController().commonResponseString(data, callback)
             }

             override fun onError(errorCode: Int, errorData: String) {
                 callback.onError(errorData)
             }

         })
     }


     override fun deleteOutletSecurityMeasureById(
         outletSecurityMeasuresId: Long,
         status: Boolean,
         callback: IDataSourceCallback<String>
     ) {

         networkController?.deleteOutletSecurityMeasureById(
             outletSecurityMeasuresId,
             status,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }

     override fun fetchSafetyMeasures(
         outletId: Long,
         callback: IDataSourceCallback<OutletSecurityMeasuresDTO>
     ) {

         networkController?.fetchSafetyMeasures(outletId, object : NetworkResponseCallback {
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

                             jsonObject.has("outletSecurityMeasuresDTOs") -> {

                                 val safetyMeasuresJsonObject =
                                     jsonObject.optJSONObject("outletSecurityMeasuresDTOs")

                                 val safetyMeasuresObject: OutletSecurityMeasuresDTO =
                                     gSon.fromJson<OutletSecurityMeasuresDTO>(
                                         safetyMeasuresJsonObject?.toString(),
                                         object :
                                             TypeToken<OutletSecurityMeasuresDTO>() {}.type
                                     )
                                 if (safetyMeasuresObject != null) {
                                     callback.onDataFound(safetyMeasuresObject)
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

     override fun saveSafetyMeasures(
         outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.saveSafetyMeasures(
             outletSecurityMeasuresDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }

     override fun fetchRestaurantDetails(
         outletId: Long,
         callback: IDataSourceCallback<RestaurantMasterDTO>
     ) {

         networkController?.fetchRestaurantDetails(outletId, object : NetworkResponseCallback {
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

                             jsonObject.has("restuarantMasterDTO") -> {

                                 val restaurantMasterJsonObject =
                                     jsonObject.optJSONObject("restuarantMasterDTO")

                                 val restaurantMasterDTO: RestaurantMasterDTO =
                                     gSon.fromJson<RestaurantMasterDTO>(
                                         restaurantMasterJsonObject?.toString(),
                                         object :
                                             TypeToken<RestaurantMasterDTO>() {}.type
                                     )
                                 if (restaurantMasterDTO != null) {
                                     callback.onDataFound(restaurantMasterDTO)
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

     override fun saveRestaurantDetails(
         restaurantMasterDTO: RestaurantMasterDTO,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.saveRestaurantDetails(
             restaurantMasterDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }


     override fun saveRestaurantDetailsByVisitor(
         restaurantMasterDTO: RestaurantMasterDTO,
         callback: IDataSourceCallback<CommonResponseDTO>
     ) {
         networkController?.saveRestaurantDetailsByVisitor(
             restaurantMasterDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseDTO(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }


     // Communication Info
     override fun fetchCommunicationInfo(
         outletId: Long,
         callback: IDataSourceCallback<CommunicationInfoDTO>
     ) {

         networkController?.fetchCommunicationInfo(outletId, object : NetworkResponseCallback {
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

                             jsonObject.has("communicationInfoDTO") -> {

                                 val restaurantMasterJsonObject =
                                     jsonObject.optJSONObject("communicationInfoDTO")

                                 val restaurantMasterDTO: CommunicationInfoDTO =
                                     gSon.fromJson<CommunicationInfoDTO>(
                                         restaurantMasterJsonObject?.toString(),
                                         object :
                                             TypeToken<CommunicationInfoDTO>() {}.type
                                     )
                                 if (restaurantMasterDTO != null) {
                                     callback.onDataFound(restaurantMasterDTO)
                                 } else {
                                     callback.onError(NetworkController.SERVER_ERROR)
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


     override fun saveCommunicationInfo(
         communicationInfoDTO: CommunicationInfoDTO,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.saveCommunicationInfo(
             communicationInfoDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }

             })
     }

     override fun saveRestaurantProfileImage(
         restaurantProfileImageDTO: RestaurantProfileImageDTO,
         callback: IDataSourceCallback<String>
     ) {

         networkController?.saveRestaurantProfileImage(restaurantProfileImageDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     try {
                         if (data.isNotBlank()) {

                             val jsonObject = JSONObject(data)
                             val gson = CustomGsonBuilder().getInstance().create()
                             val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                 jsonObject.toString(),
                                 CommonResponseDTO::class.java
                             )

                             when {
                                 commonResponseDTO.success?.message != null -> {
                                     callback.onDataFound(commonResponseDTO.success.message)
                                 }

                                 commonResponseDTO.error?.message != null -> {
                                     callback.onError(commonResponseDTO.error.message)
                                 }
                                 else -> {
                                     callback.onError(NetworkController.SERVER_ERROR)
                                 }
                             }

                         }
                     } catch (e: Exception) {

                     }

 //                    NetworkController().commonResponseString(data, callback)
                 }

                 override fun onError(errorCode: Int, errorData: String) {
                     callback.onError(errorData)
                 }
             })
     }

     override fun getRestaurantProfileImage(
         outletId: Long,
         callback: IDataSourceCallback<RestaurantProfileImageDTO>
     ) {

         networkController?.getRestaurantProfileImage(outletId, object : NetworkResponseCallback {
             override fun onSuccess(data: String) {
                 try {
                     if (data.isNotBlank()) {

                         val jsonObject = JSONObject(data)
                         val gson = CustomGsonBuilder().getInstance().create()
                         val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                             jsonObject.toString(),
                             CommonResponseDTO::class.java
                         )

                         when {
                             jsonObject.has("restaurantMasterProfileImageDTO") -> {

                                 val restaurantProfileImageDTOJSONObject =
                                     jsonObject.getJSONObject("restaurantMasterProfileImageDTO")

                                 val restaurantProfileImageDTO: RestaurantProfileImageDTO =
                                     gson.fromJson(
                                         restaurantProfileImageDTOJSONObject.toString(),
                                         object :
                                             TypeToken<RestaurantProfileImageDTO?>() {}.type
                                     )
                                 callback.onDataFound(restaurantProfileImageDTO)
                             }
                             commonResponseDTO.error != null -> {
                                 callback.onError(commonResponseDTO.error.message)
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

     override fun getGalleryImageCategory(
         commonListingDTO: CommonListingDTO,
         callback: IDataSourceCallback<CommonCategoryDTO>
     ) {
         networkController?.getGalleryImageCategory(commonListingDTO,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     try {
                         if (data.isNotBlank()) {

                             val jsonObject = JSONObject(data)
                             val gson = CustomGsonBuilder().getInstance().create()
                             val commonCategoryDTO: CommonCategoryDTO = gson.fromJson(
                                 jsonObject.toString(),
                                 CommonCategoryDTO::class.java
                             )
                             if (commonCategoryDTO.galleryImageCategoryList.isNotEmpty()) {
                                 callback.onDataFound(commonCategoryDTO)
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

     override fun saveGalleryImagesByCategory(
         galleryImageCategory: GalleryImageCategory,
         callback: IDataSourceCallback<Long>
     ) {
         networkController?.saveGalleryImagesByCategory(galleryImageCategory,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     try {
                         if (data.isNotBlank()) {
                             val jsonObject = JSONObject(data)
                             val gson = CustomGsonBuilder().getInstance().create()
                             val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                 jsonObject.toString(),
                                 CommonResponseDTO::class.java
                             )

                             when {
                                 jsonObject.has("id") -> {
                                     callback.onDataFound(jsonObject.getLong("id"))
                                 }

                                 commonResponseDTO.error?.message != null -> {
                                     callback.onError(commonResponseDTO.error.message)
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

     override fun getAllGalleryImages(
         outletId: Long,
         callback: IDataSourceCallback<ArrayList<GalleryImageCategory>>
     ) {
         networkController?.getAllGalleryImages(outletId, object : NetworkResponseCallback {
             override fun onSuccess(data: String) {
                 try {
                     if (data.isNotBlank()) {
                         val jsonObject = JSONObject(data)
                         val gson = CustomGsonBuilder().getInstance().create()
                         val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                             jsonObject.toString(),
                             CommonResponseDTO::class.java
                         )
                         when {
                             jsonObject.has("imageGalleryDTO") -> {

                                 val imageGalleryJsonArray =
                                     jsonObject.getJSONArray("imageGalleryDTO")

                                 val galleryImageCategory: ArrayList<GalleryImageCategory> =
                                     gson.fromJson(
                                         imageGalleryJsonArray.toString(),
                                         object :
                                             TypeToken<ArrayList<GalleryImageCategory?>>() {}.type
                                     )
                                 callback.onDataFound(galleryImageCategory)
                             }
                             commonResponseDTO.error?.message != null -> {
                                 callback.onError(commonResponseDTO.error.message)
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

     override fun removeGalleryImageByImageId(
         imageId: Long,
         id: Long,
         callback: IDataSourceCallback<String>
     ) {
         networkController?.removeGalleryImageByImageId(imageId, id,
             object : NetworkResponseCallback {
                 override fun onSuccess(data: String) {
                     try {
                         if (data.isNotBlank()) {
                             val jsonObject = JSONObject(data)
                             val gson = CustomGsonBuilder().getInstance().create()
                             val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                 jsonObject.toString(),
                                 CommonResponseDTO::class.java
                             )
                             when {
                                 commonResponseDTO.success?.message != null -> {
                                     callback.onDataFound(commonResponseDTO.success.message)
                                 }
                                 *//* jsonObject.has("imageGalleryDTO") -> {

                                    val imageGalleryJsonArray =
                                        jsonObject.getJSONArray("imageGalleryDTO")

                                    val galleryImageCategory: ArrayList<GalleryImageCategory> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<GalleryImageCategory?>>() {}.type
                                        )
                                    callback.onDataFound(galleryImageCategory)
                                }*//*
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun discardGalleryImagesByCategoryId(
        outletId: Long,
        galleryImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.discardGalleryImageByCategoryId(outletId, galleryImageCategoryId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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


    override fun getMenuImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CommonCategoryDTO>
    ) {
        networkController?.getMenuImageCategory(commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonCategoryDTO: CommonCategoryDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonCategoryDTO::class.java
                            )
                            if (commonCategoryDTO.menuImageCategoryList.isNotEmpty()) {
                                callback.onDataFound(commonCategoryDTO)
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

    override fun saveMenuImagesByCategory(
        menuImageCategory: MenuImageCategory,
        callback: IDataSourceCallback<Long>
    ) {
        networkController?.saveMenuImagesByCategory(menuImageCategory,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {
                                jsonObject.has("id") -> {
                                    callback.onDataFound(jsonObject.getLong("id"))
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun getAllMenuImages(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<MenuImageCategory>>
    ) {
        networkController?.getAllMenuImages(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        when {
                            jsonObject.has("menuImageGalleryDTO") -> {

                                val imageGalleryJsonArray =
                                    jsonObject.getJSONArray("menuImageGalleryDTO")

                                val menuImageCategory: ArrayList<MenuImageCategory> =
                                    gson.fromJson(
                                        imageGalleryJsonArray.toString(),
                                        object :
                                            TypeToken<ArrayList<MenuImageCategory?>>() {}.type
                                    )
                                callback.onDataFound(menuImageCategory)
                            }
                            commonResponseDTO.error?.message != null -> {
                                callback.onError(commonResponseDTO.error.message)
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

    override fun removeMenuImageByImageId(
        imageId: Long,
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.removeMenuImageByImageId(imageId, id,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun discardMenuImagesByCategoryId(
        outletId: Long,
        menuImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.discardGalleryImageByCategoryId(outletId, menuImageCategoryId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun saveOutletDiscountDetails(
        outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOutletDiscountDetails(
            outletDiscountDetailsDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }


    override fun saveOutletSubAdmin(
        subAdminDTO: SubAdminDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOutletSubAdmin(subAdminDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)

                        when {
                            jsonObject.has("success") -> {
                                val success = jsonObject.getString("success")
                                callback.onDataFound(success)
                            }
                            jsonObject.has("error") -> {
                                val error = jsonObject.getString("error")
                                callback.onError(error)
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                        *//*val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        when {
                            commonResponseDTO.success?.message != null -> {
                                callback.onDataFound(commonResponseDTO.success.message)
                            }
                            commonResponseDTO.error?.message != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }*//*
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

    override fun outletSubAdminListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<SubAdminDTO>>
    ) {
        networkController?.outletSubAdminListing(commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                jsonObject.has("userMasterDTOs") -> {

                                    val imageGalleryJsonArray =
                                        jsonObject.getJSONArray("userMasterDTOs")

                                    val subAdminDTOList: ArrayList<SubAdminDTO> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<SubAdminDTO?>>() {}.type
                                        )
                                    callback.onDataFound(subAdminDTOList)
                                }
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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


    override fun changeSubAdminStatus(
        userId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeSubAdminStatus(userId, status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            if (jsonObject.has("201")) {
                                val success = jsonObject.getString("201")
                                callback.onDataFound("UserId $success")
                            } else {
                                callback.onError(data)
                            }
                            *//*val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onError(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
                            }*//*
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
    }*/

    override fun logoutUser(callback: IDataSourceCallback<String>) {
        networkController?.logoutUser(
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            if (jsonObject.has("status")) {
                                val status = jsonObject.getString("status")
                                if (status == "Logout successfully")
                                    callback.onDataFound(status)
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
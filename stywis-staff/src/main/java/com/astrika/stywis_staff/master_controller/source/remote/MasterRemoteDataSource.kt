package com.astrika.stywis_staff.master_controller.source.remote

import android.content.Context
import android.content.SharedPreferences
import com.astrika.stywis_staff.master_controller.source.MasterDataSource
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.network.NetworkController
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.network.network_utils.NetworkResponseCallback
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomGsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MasterRemoteDataSource : MasterDataSource() {

    companion object {
        private var instance: MasterRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences


        @JvmStatic
        fun getInstance(context: Context?): MasterDataSource? {
            mContext = context
            if (context != null) {
                networkController = NetworkController.getInstance(context)
                sharedPreferences = Constants.getSharedPreferences(context.applicationContext)
            }

            if (instance == null) {
                instance = MasterRemoteDataSource()
            }
            return instance
        }
    }


    override fun fetchDashboardDrawerMasterDataRemote(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {

        networkController?.getDrawerMenu(object : NetworkResponseCallback {
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

                            jsonObject.has("modules") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("modules")

                                val arrayList: ArrayList<DashboardDrawerDTO> =
                                    gSon.fromJson<ArrayList<DashboardDrawerDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<DashboardDrawerDTO?>?>() {}.type
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

    // System Value Master
    override fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {

        networkController?.fetchSVM(object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {

                    val jsonArray = JSONArray(data)
                    val gSon = CustomGsonBuilder().getInstance().create()

                    val svmList: List<SystemValueMasterDTO> =
                        gSon.fromJson<List<SystemValueMasterDTO>>(
                            jsonArray.toString(),
                            object : TypeToken<List<SystemValueMasterDTO?>?>() {}.type
                        )
                    if (!svmList.isNullOrEmpty()) {
                        callback.onDataFound(svmList)
                    } else {
                        callback.onDataNotFound()
                    }

                } catch (e: java.lang.Exception) {

                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })

    }


    override fun fetchVisitPurposeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {
        networkController?.fetchVisitPurpose(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        if (jsonObject.has("visitorList")) {
                            val jsonArray = jsonObject.optJSONArray("visitorList")
                            val gSon = CustomGsonBuilder().getInstance().create()

                            val list: List<VisitPurposeDTO> =
                                gSon.fromJson<List<VisitPurposeDTO>>(
                                    jsonArray?.toString(),
                                    object : TypeToken<List<VisitPurposeDTO?>?>() {}.type
                                )
                            if (!list.isNullOrEmpty()) {
                                callback.onDataFound(list)
                            } else {
                                callback.onDataNotFound()
                            }

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

    override fun fetchDesignationMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        networkController?.fetchDesignation(commonListingDTO, object : NetworkResponseCallback {
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

                            jsonObject.has("DesignationList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("DesignationList")

                                val arrayList: ArrayList<DesignationDTO> =
                                    gSon.fromJson<ArrayList<DesignationDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<DesignationDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onError(NetworkController.SERVER_ERROR)
//                                    callback.onDataNotFound()
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


    override fun fetchOutletDetails(callback: IDataSourceCallback<OutletDetailsDTO>) {

        networkController?.fetchOutletDetails(object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        val outletDetailsDTO: OutletDetailsDTO =
                            gSon.fromJson<OutletDetailsDTO>(
                                jsonObject.toString(),
                                object :
                                    TypeToken<OutletDetailsDTO?>() {}.type
                            )
                        if (outletDetailsDTO != null) {
                            callback.onDataFound(outletDetailsDTO)
                        } else {
                            callback.onDataNotFound()
                        }

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
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


    override fun fetchStockCustomizationOptionsMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        networkController?.fetchStockCustomizationOptionsMaster(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            if (jsonObject.has("customizationOptionsList")) {
                                val jsonArray = jsonObject.optJSONArray("customizationOptionsList")
                                val gSon = CustomGsonBuilder().getInstance().create()

                                val list: ArrayList<StockCustomizationMasterDTO> =
                                    gSon.fromJson(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<StockCustomizationMasterDTO?>?>() {}.type
                                    )
                                if (!list.isNullOrEmpty()) {
                                    callback.onDataFound(list)
                                } else {
                                    callback.onDataNotFound()
                                }

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
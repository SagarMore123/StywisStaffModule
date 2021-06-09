package com.astrika.stywis_staff.master_controller.sync

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.utils.Constants

class SyncData {

    companion object {
        @Volatile
        var masterRepository: MasterRepository? = null

        private val commonListingDTO = CommonListingDTO()

        /**
         * Performs the network request for updated weather, parses the JSON from that request, and
         * inserts the new weather information into our ContentProvider. Will notify the user that new
         * weather has been loaded if the user hasn't been notified of the weather within the last day
         * AND they haven't disabled notifications in the preferences screen.
         *
         * @param context Used to access utility methods and the ContentResolver
         */

        var services = 1
        var servicesCounts = MutableLiveData<Int>(0)
        var loginMasterServices = 5
        var loginMasterServicesCounts = MutableLiveData<Int>(0)

        @Synchronized
        fun splashSyncData(context: Context?) {
//            context?.let { Constants.clearSharedPrefs(it) }
            masterRepository = MasterRepository.getInstance(context)
            /*--------  Drawer section -----------*/
            /*try {
                masterRepository?.fetchDashboardDrawerMasterDataRemote(object :
                    IDataSourceCallback<List<DashboardDrawerDTO>> {

                    override fun onDataFound(data: List<DashboardDrawerDTO>) {

                    }

                    override fun onError(error: String) {
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                e.printStackTrace()
            }*/

        }


        @Synchronized
        fun syncData(context: Context?) {
            masterRepository = MasterRepository.getInstance(context)

            /*--------  Drawer section -----------*/
            try {
                masterRepository?.fetchDashboardDrawerMasterDataRemote(object :
                    IDataSourceCallback<List<DashboardDrawerDTO>> {

                    override fun onDataFound(data: List<DashboardDrawerDTO>) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })


            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  SVM section -----------*/
            try {

                masterRepository?.fetchSystemValueMasterDataRemote(object :
                    IDataSourceCallback<List<SystemValueMasterDTO>> {

                    override fun onDataFound(data: List<SystemValueMasterDTO>) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Visit Purpose Section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.VISIT_PURPOSE_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER_ASC

                masterRepository?.fetchVisitPurposeMasterDataRemote(commonListingDTO, object :
                    IDataSourceCallback<List<VisitPurposeDTO>> {

                    override fun onDataFound(data: List<VisitPurposeDTO>) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
            }


            /*--------  Designation section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.DESIGNATION_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER_ASC

                masterRepository?.fetchDesignationMasterDataRemote(commonListingDTO,
                    object : IDataSourceCallback<List<DesignationDTO>> {

                        override fun onDataFound(data: List<DesignationDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Outlet Details Section -----------*/
            try {

                masterRepository?.fetchOutletDetails(object :
                    IDataSourceCallback<OutletDetailsDTO> {

                    override fun onDataFound(data: OutletDetailsDTO) {

                        context?.let {
                            Constants.getSharedPreferences(it).edit()
                                .putString(
                                    Constants.OUTLET_NAME,
                                    Constants.encrypt(data.OutletName ?: "")
                                ).apply()
                        }

                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
            }


        }
    }
}
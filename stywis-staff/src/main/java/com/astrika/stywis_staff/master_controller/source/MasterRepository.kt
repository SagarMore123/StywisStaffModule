package com.astrika.stywis_staff.master_controller.source

import android.content.Context
import com.astrika.stywis_staff.master_controller.StywisStaffRoomDatabase
import com.astrika.stywis_staff.master_controller.source.local.MasterLocalDataSource
import com.astrika.stywis_staff.master_controller.source.remote.MasterRemoteDataSource
import com.astrika.stywis_staff.master_controller.sync.AppExecutors
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback

class MasterRepository(
    private var mMasterRemoteDataSource: MasterDataSource?,
    private var mMasterLocalDataSource: MasterDataSource?
) : MasterDataSource() {

    companion object {
        @Volatile
        private var INSTANCE: MasterRepository? = null

        fun getInstance(context: Context?): MasterRepository? {
            if (INSTANCE == null) {
//                synchronized(UserRepository::class.java) {
                if (INSTANCE == null) {
                    val databaseStywis: StywisStaffRoomDatabase =
                        StywisStaffRoomDatabase.getDatabase(context!!)
                    INSTANCE = MasterRepository(
                        MasterRemoteDataSource.getInstance(context),
                        MasterLocalDataSource.getInstance(
                            AppExecutors(),
                            databaseStywis.dashboardDrawerDao(),
                            databaseStywis.systemValueMasterDao(),
                            databaseStywis.visitPurposeMasterDao(),
                            databaseStywis.stockCustomizationOptionsMasterDao(),
                            databaseStywis.designationDao()
                        )
                    )
//                    }
                }
            }
            return INSTANCE
        }
    }

    override fun saveDashboardDrawerMasterDataLocal(
        dataList: List<DashboardDrawerDTO>,
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        mMasterLocalDataSource?.saveDashboardDrawerMasterDataLocal(dataList, callback)
    }

    override fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {
        mMasterLocalDataSource?.fetchDashboardDrawerMasterDataLocal(callback)
    }

    override fun fetchDashboardDrawerMasterDataRemote(
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        mMasterRemoteDataSource?.fetchDashboardDrawerMasterDataRemote(
            object : IDataSourceCallback<List<DashboardDrawerDTO>> {

                override fun onDataFound(data: List<DashboardDrawerDTO>) {
                    saveDashboardDrawerMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
        mMasterLocalDataSource?.fetchDashboardDrawerMasterDataById(id, callback)
    }

    override fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveSystemValueMasterDataLocal(dataList, callback)
    }

    override fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchSystemValueMasterValueByIdLocal(systemValueId, callback)
    }

    override fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchSystemMasterValueByNameLocal(systemValueName, callback)
    }


    override fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {
        mMasterRemoteDataSource?.fetchSystemValueMasterDataRemote(object :
            IDataSourceCallback<List<SystemValueMasterDTO>> {

            override fun onDataFound(data: List<SystemValueMasterDTO>) {
                saveSystemValueMasterDataLocal(data, callback)
            }

            override fun onError(error: String) {
                callback.onError(error)
            }

        })
    }

    // Visit Purpose
    override fun saveVisitPurposeMasterDataLocal(
        dataList: List<VisitPurposeDTO>,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {
        mMasterLocalDataSource?.saveVisitPurposeMasterDataLocal(dataList, callback)
    }

    override fun fetchAllVisitPurposeMasterDataLocal(callback: IDataSourceCallback<List<VisitPurposeDTO>>) {
        mMasterLocalDataSource?.fetchAllVisitPurposeMasterDataLocal(callback)
    }

    override fun fetchVisitPurposeMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {
        mMasterLocalDataSource?.fetchVisitPurposeMasterValueByIdLocal(id, callback)
    }

    override fun fetchVisitPurposeMasterValueByNameLocal(
        name: String,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {
        mMasterLocalDataSource?.fetchVisitPurposeMasterValueByNameLocal(name, callback)
    }

    override fun fetchVisitPurposeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {
        mMasterRemoteDataSource?.fetchVisitPurposeMasterDataRemote(commonListingDTO, object :
            IDataSourceCallback<List<VisitPurposeDTO>> {

            override fun onDataFound(data: List<VisitPurposeDTO>) {
                saveVisitPurposeMasterDataLocal(data, callback)
            }

            override fun onError(error: String) {
                callback.onError(error)
            }

        })
    }


    // Stock Customization Options Master

    override fun saveStockCustomizationOptionsMasterDataLocal(
        dataList: List<StockCustomizationMasterDTO>,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveStockCustomizationOptionsMasterDataLocal(dataList, callback)
    }

    override fun fetchStockAllCustomizationOptionsMasterDataLocal(callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>) {
        mMasterLocalDataSource?.fetchStockAllCustomizationOptionsMasterDataLocal(callback)
    }


    override fun fetchStockCustomizationOptionsMasterListDataByProductIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchStockCustomizationOptionsMasterListDataByProductIdLocal(
            id,
            callback
        )
    }

    override fun fetchStockCustomizationOptionsMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<StockCustomizationMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchStockCustomizationOptionsMasterValueByIdLocal(id, callback)
    }

    override fun fetchStockCustomizationOptionsMasterValueByNameLocal(
        name: String,
        callback: IDataSourceCallback<StockCustomizationMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchStockCustomizationOptionsMasterValueByNameLocal(name, callback)
    }

    override fun fetchStockCustomizationOptionsMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        mMasterRemoteDataSource?.fetchStockCustomizationOptionsMasterDataRemote(
            commonListingDTO,
            object :
                IDataSourceCallback<List<StockCustomizationMasterDTO>> {

                override fun onDataFound(data: List<StockCustomizationMasterDTO>) {
                    saveStockCustomizationOptionsMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    // Outlet Details
    override fun fetchOutletDetails(callback: IDataSourceCallback<OutletDetailsDTO>) {
        mMasterRemoteDataSource?.fetchOutletDetails(object : IDataSourceCallback<OutletDetailsDTO> {

            override fun onDataFound(data: OutletDetailsDTO) {
                callback.onDataFound(data)
            }

            override fun onError(error: String) {
                callback.onError(error)
            }

        })
    }

    // Designation

    override fun saveDesignationMasterDataLocal(
        dataList: List<DesignationDTO>,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        mMasterLocalDataSource?.saveDesignationMasterDataLocal(dataList, callback)
    }

    override fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {
        mMasterLocalDataSource?.fetchDesignationMasterDataLocal(callback)
    }

    override fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
        mMasterLocalDataSource?.fetchDesignationMasterDataById(id, callback)
    }

    override fun fetchDesignationMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {

        mMasterRemoteDataSource?.fetchDesignationMasterDataRemote(commonListingDTO,
            object : IDataSourceCallback<List<DesignationDTO>> {

                override fun onDataFound(data: List<DesignationDTO>) {
                    saveDesignationMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


}
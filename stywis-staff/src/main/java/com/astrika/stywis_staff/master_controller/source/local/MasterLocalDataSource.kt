package com.astrika.stywis_staff.master_controller.source.local

import com.astrika.stywis_staff.master_controller.source.MasterDataSource
import com.astrika.stywis_staff.master_controller.source.daos.*
import com.astrika.stywis_staff.master_controller.sync.AppExecutors
import com.astrika.stywis_staff.models.DashboardDrawerDTO
import com.astrika.stywis_staff.models.DesignationDTO
import com.astrika.stywis_staff.models.SystemValueMasterDTO
import com.astrika.stywis_staff.models.VisitPurposeDTO
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback

class MasterLocalDataSource(
    mAppExecutors: AppExecutors,
    dashboardDrawerDao: DashboardDrawerDao,
    systemValueMasterDao: SystemValueMasterDao,
    visitPurposeMasterDao: VisitPurposeMasterDao,
    stockCustomizationOptionsMasterDao: StockCustomizationOptionsMasterDao,
    designationDao: DesignationDao


) : MasterDataSource() {

    companion object {

        @Volatile
        private var INSTANCE: MasterLocalDataSource? = null

        @JvmStatic
        fun getInstance(
            appExecutors: AppExecutors,
            dashboardDrawerDao: DashboardDrawerDao,
            systemValueMasterDao: SystemValueMasterDao,
            visitPurposeMasterDao: VisitPurposeMasterDao,
            stockCustomizationOptionsMasterDao: StockCustomizationOptionsMasterDao,
            designationDao: DesignationDao

        ): MasterLocalDataSource? {
            if (INSTANCE == null) {
                synchronized(
                    MasterLocalDataSource::class.java
                ) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            MasterLocalDataSource(
                                appExecutors,
                                dashboardDrawerDao,
                                systemValueMasterDao,
                                visitPurposeMasterDao,
                                stockCustomizationOptionsMasterDao,
                                designationDao

                            )
                    }
                }
            }
            return INSTANCE
        }


    }

    private var mAppExecutors: AppExecutors? = mAppExecutors

    private var dashboardDrawerDao: DashboardDrawerDao? = dashboardDrawerDao
    private var systemValueMasterDao: SystemValueMasterDao? = systemValueMasterDao
    private var visitPurposeMasterDao: VisitPurposeMasterDao? = visitPurposeMasterDao
    private var stockCustomizationOptionsMasterDao: StockCustomizationOptionsMasterDao? =
        stockCustomizationOptionsMasterDao
    private var designationDao: DesignationDao? = designationDao


    override fun saveDashboardDrawerMasterDataLocal(
        dataList: List<DashboardDrawerDTO>,
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                dashboardDrawerDao?.deleteAllMasterData()
                dashboardDrawerDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {
        val completeRunnable = Runnable {
            try {
                val categoryList: List<DashboardDrawerDTO>? = dashboardDrawerDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: DashboardDrawerDTO? = dashboardDrawerDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // SVM
    override fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                systemValueMasterDao?.deleteAllSystemMasterData()
                systemValueMasterDao?.insertSystemMasterData(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val systemMastersValue: SystemValueMasterDTO? =
                    systemValueMasterDao?.getValueById(systemValueId)
                mAppExecutors?.mainThread()
                    ?.execute { systemMastersValue?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {

                mAppExecutors?.mainThread()?.execute {
                    callback.onDataFound(
                        systemValueMasterDao?.getAllByCode(systemValueName) ?: arrayListOf()
                    )
                }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // Visit Purpose
    override fun saveVisitPurposeMasterDataLocal(
        dataList: List<VisitPurposeDTO>,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                visitPurposeMasterDao?.deleteAllMasterData()
                visitPurposeMasterDao?.insertMasterData(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchAllVisitPurposeMasterDataLocal(callback: IDataSourceCallback<List<VisitPurposeDTO>>) {
        val completeRunnable = Runnable {
            try {
                val list: List<VisitPurposeDTO>? =
                    visitPurposeMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchVisitPurposeMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: VisitPurposeDTO =
                    visitPurposeMasterDao?.getValueById(id) ?: VisitPurposeDTO()
                mAppExecutors?.mainThread()
                    ?.execute { dto.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchVisitPurposeMasterValueByNameLocal(
        name: String,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: VisitPurposeDTO? = visitPurposeMasterDao?.getValueByName(name)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // Stock

    override fun saveStockCustomizationOptionsMasterDataLocal(
        dataList: List<StockCustomizationMasterDTO>,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                stockCustomizationOptionsMasterDao?.deleteAllMasterData()
                stockCustomizationOptionsMasterDao?.insertMasterData(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }

    override fun fetchStockAllCustomizationOptionsMasterDataLocal(callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>) {
        val completeRunnable = Runnable {
            try {
                val list: List<StockCustomizationMasterDTO>? =
                    stockCustomizationOptionsMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchStockCustomizationOptionsMasterListDataByProductIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                val list: List<StockCustomizationMasterDTO>? =
                    stockCustomizationOptionsMasterDao?.fetchListByProductId(id)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchStockCustomizationOptionsMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<StockCustomizationMasterDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val masterDto: StockCustomizationMasterDTO =
                    stockCustomizationOptionsMasterDao?.getValueById(id)
                        ?: StockCustomizationMasterDTO()
                mAppExecutors?.mainThread()
                    ?.execute { masterDto.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // Designation

    override fun saveDesignationMasterDataLocal(
        dataList: List<DesignationDTO>,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                designationDao?.deleteAllDetails()
                designationDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }

    override fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<DesignationDTO>? =
                    designationDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }

    override fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: DesignationDTO? = designationDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


}

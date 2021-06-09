package com.astrika.stywis_staff.master_controller.source

import androidx.annotation.NonNull
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback

abstract class MasterDataSource {

    //System Value Master
    open fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {

    }

    open fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {

    }

    open fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {

    }

    open fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {}


    // Visit Purpose Master
    open fun saveVisitPurposeMasterDataLocal(
        dataList: List<VisitPurposeDTO>,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {

    }

    open fun fetchAllVisitPurposeMasterDataLocal(callback: IDataSourceCallback<List<VisitPurposeDTO>>) {}

    open fun fetchVisitPurposeMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {

    }

    open fun fetchVisitPurposeMasterValueByNameLocal(
        name: String,
        callback: IDataSourceCallback<VisitPurposeDTO>
    ) {

    }

    open fun fetchVisitPurposeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<VisitPurposeDTO>>
    ) {
    }


    // Stock Customization Options Master
    open fun saveStockCustomizationOptionsMasterDataLocal(
        dataList: List<StockCustomizationMasterDTO>,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {

    }

    open fun fetchStockAllCustomizationOptionsMasterDataLocal(callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>) {}
    open fun fetchStockCustomizationOptionsMasterListDataByProductIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
    }

    open fun fetchStockCustomizationOptionsMasterValueByIdLocal(
        id: Long,
        callback: IDataSourceCallback<StockCustomizationMasterDTO>
    ) {

    }

    open fun fetchStockCustomizationOptionsMasterValueByNameLocal(
        name: String,
        callback: IDataSourceCallback<StockCustomizationMasterDTO>
    ) {

    }

    open fun fetchStockCustomizationOptionsMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<StockCustomizationMasterDTO>>
    ) {
    }


    //Dashboard Drawer
    open fun saveDashboardDrawerMasterDataLocal(
        @NonNull dataList: List<DashboardDrawerDTO>,
        @NonNull callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
    }

    open fun fetchDashboardDrawerMasterDataRemote(
        @NonNull callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
    }

    open fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {}

    open fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
    }


    //Designation
    open fun fetchDesignationMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
    }


    open fun saveDesignationMasterDataLocal(
        @NonNull dataList: List<DesignationDTO>,
        @NonNull callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
    }

    open fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {}

    open fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
    }


    // Outlet Details

    open fun fetchOutletDetails(
        @NonNull callback: IDataSourceCallback<OutletDetailsDTO>
    ) {
    }


}
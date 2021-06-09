package com.astrika.stywis_staff.source.dashboard

import android.app.Application
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails
import com.astrika.stywis_staff.models.menu.ProductAvailabilityDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.remote.DashboardRemoteDataSource

class DashboardRepository(private var mDashboardDataSource: DashboardDataSource?) :
    DashboardDataSource {

    companion object {
        @Volatile
        private var INSTANCE: DashboardRepository? = null

        @JvmStatic
        fun getInstance(
            mApplication: Application,
            initRemoteRepository: Boolean
        ): DashboardRepository? {
            if (INSTANCE == null) {
                synchronized(DashboardRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = DashboardRepository(
                            if (initRemoteRepository) DashboardRemoteDataSource.getInstance(
                                mApplication
                            ) else null
                        )
                    }
                }
            }
            return INSTANCE
        }

    }

    override fun fetchCheckInListing(
        outletId: Long,
        requestTypes: String,
        requestStatuses: ArrayList<String>,
        callback: IDataSourceCallback<ArrayList<CheckInDTO>>
    ) {
        mDashboardDataSource?.fetchCheckInListing(outletId, requestTypes, requestStatuses, callback)
    }

    override fun getAllMenuCategories(
        outletId: Long,
        callback: IDataSourceCallback<GetMenuCategoriesResponseDTO>
    ) {
        mDashboardDataSource?.getAllMenuCategories(outletId, callback)
    }

    override fun getAllDishes(
        productCategoryId: Long,
        outletId: Long,
        productName: String,
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<MenuSectionWithDishDetails>>
    ) {
        mDashboardDataSource?.getAllDishes(
            productCategoryId,
            outletId,
            productName,
            commonListingDTO,
            callback
        )
    }

    override fun saveDishAvailability(
        productAvailabilityList: ArrayList<ProductAvailabilityDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveDishAvailability(productAvailabilityList, callback)
    }

    override fun getCustomizationListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CustomizationDTO>
    ) {
        mDashboardDataSource?.getCustomizationListing(commonListingDTO, callback)
    }

    override fun getOrderDetails(
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<OrderDTO>
    ) {
        mDashboardDataSource?.getOrderDetails(inCustomerRequestId, callback)
    }

    override fun saveOrderDetails(
        saveOrderDTO: SaveOrderDTO,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveOrderDetails(saveOrderDTO, callback)
    }
/*

    override fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveTableListing(tableListManagementDTO, callback)
    }


    override fun getTableListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        mDashboardDataSource?.getTableListing(commonListingDTO, callback)
    }

    override fun reAssignTable(
        tableIdOld: Long,
        tableIdNew: Long,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.reAssignTable(tableIdOld, tableIdNew, callback)
    }
*/

    override fun getProfileDetails(userId: Long, callback: IDataSourceCallback<UserDTO>) {
        mDashboardDataSource?.getProfileDetails(userId, callback)
    }

    override fun updateProfileDetails(
        userDetailsDTO: UserDetailsDTO,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.updateProfileDetails(userDetailsDTO, callback)
    }


    override fun changeStatusApproved(
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeStatusApproved(inCustomerRequestId, callback)
    }

    override fun changeDineInStatus(
        inCustomerRequestId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeDineInStatus(inCustomerRequestId, status, callback)
    }

    override fun checkOut(
        inCheckInCustomerRequestId: Long,
        inCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.checkOut(inCheckInCustomerRequestId, inCustomerRequestId, callback)
    }


    override fun verifyGPin(gPinDTO: GPinDTO, callback: IDataSourceCallback<String>) {
        mDashboardDataSource?.verifyGPin(gPinDTO, callback)
    }


    override fun changeStatusRejected(
        inCustomerRequestId: Long,
        rejectedRemarks: String,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeStatusRejected(inCustomerRequestId, rejectedRemarks, callback)
    }

    override fun getUserByMobile(mobileNo: String, callback: IDataSourceCallback<UserDTO>) {
        mDashboardDataSource?.getUserByMobile(mobileNo, callback)
    }

    override fun saveVisitorDetails(
        userDetailsDTO: UserDetailsDTO,
        callback: IDataSourceCallback<VisitorResponseDTO>
    ) {
        mDashboardDataSource?.saveVisitorDetails(userDetailsDTO, callback)
    }

    override fun verifyOtpVisitor(
        mobileNo: String,
        otp: String,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.verifyOtpVisitor(mobileNo, otp, callback)
    }

    override fun sendOtpVisitor(
        mobileNo: String,
        callback: IDataSourceCallback<VisitorResponseDTO>
    ) {
        mDashboardDataSource?.sendOtpVisitor(mobileNo, callback)
    }

    override fun checkInVisitor(checkInDTO: CheckInDTO, callback: IDataSourceCallback<String>) {
        mDashboardDataSource?.checkInVisitor(checkInDTO, callback)
    }

    override fun generateBill(
        userId: Long,
        outletId: Long,
        inCustomerRequestId: Long,
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<BillGenerationDTO>
    ) {
        mDashboardDataSource?.generateBill(
            userId,
            outletId,
            inCustomerRequestId,
            inCheckInCustomerRequestId,
            callback
        )
    }

    override fun getBillDetails(
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<BillGenerationDTO>
    ) {
        mDashboardDataSource?.getBillDetails(inCheckInCustomerRequestId, callback)
    }

    override fun changeBillDetailsStatusToPaid(
        inCheckInCustomerRequestId: Long,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeBillDetailsStatusToPaid(inCheckInCustomerRequestId, callback)
    }

    override fun getWishListByUserId(userId: Long, callback: IDataSourceCallback<WishListDTO>) {
        mDashboardDataSource?.getWishListByUserId(userId, callback)
    }

    override fun changeWishListItemStatus(
        itemId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeWishListItemStatus(itemId, status, callback)

    }

    override fun deleteWishListItem(
        itemId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.deleteWishListItem(itemId, status, callback)
    }


    override fun saveStockCustomizations(
        arrayList: ArrayList<StocksCustomizationDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveStockCustomizations(arrayList, callback)
    }

    override fun fetchAllStockCustomizationsByProductOrDishId(
        productId: Long,
        callback: IDataSourceCallback<ArrayList<StocksCustomizationDTO>>
    ) {
        mDashboardDataSource?.fetchAllStockCustomizationsByProductOrDishId(
            productId,
            callback
        )
    }


}

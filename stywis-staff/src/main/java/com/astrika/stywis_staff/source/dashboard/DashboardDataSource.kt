package com.astrika.stywis_staff.source.dashboard

import androidx.annotation.NonNull
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails
import com.astrika.stywis_staff.models.menu.ProductAvailabilityDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback

interface DashboardDataSource {

    fun getAllMenuCategories(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<GetMenuCategoriesResponseDTO>
    )

    fun getAllDishes(
        @NonNull productCategoryId: Long,
        @NonNull outletId: Long,
        @NonNull productName: String,
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<MenuSectionWithDishDetails>>
    )

    fun fetchCheckInListing(
        @NonNull outletId: Long,
        @NonNull requestTypes: String,
        @NonNull requestStatuses: ArrayList<String>,
        @NonNull callback: IDataSourceCallback<ArrayList<CheckInDTO>>
    )

    fun saveDishAvailability(
        @NonNull productAvailabilityList: ArrayList<ProductAvailabilityDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getCustomizationListing(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<CustomizationDTO>
    )

    fun getOrderDetails(
        @NonNull inCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<OrderDTO>
    )

    fun saveOrderDetails(
        @NonNull saveOrderDTO: SaveOrderDTO,
        @NonNull callback: IDataSourceCallback<String>
    )
/*

    fun saveTableListing(
        @NonNull tableListManagementDTO: ArrayList<TableManagementDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )
*/


    fun getProfileDetails(@NonNull userId: Long, @NonNull callback: IDataSourceCallback<UserDTO>)
    fun updateProfileDetails(
        @NonNull userDetailsDTO: UserDetailsDTO,
        @NonNull callback: IDataSourceCallback<String>
    )
/*

    fun getTableListing(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    )

    fun reAssignTable(
        @NonNull tableIdOld: Long,
        @NonNull tableIdNew: Long,
        @NonNull callback: IDataSourceCallback<String>
    )
*/

    fun changeStatusApproved(
        @NonNull inCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun changeDineInStatus(
        @NonNull inCustomerRequestId: Long,
        @NonNull status: String,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun checkOut(
        @NonNull inCheckInCustomerRequestId: Long,
        @NonNull inCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun verifyGPin(
        @NonNull gPinDTO: GPinDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun changeStatusRejected(
        @NonNull inCustomerRequestId: Long,
        @NonNull rejectedRemarks: String,
        @NonNull callback: IDataSourceCallback<String>
    )


    fun getUserByMobile(
        @NonNull mobileNo: String,
        @NonNull callback: IDataSourceCallback<UserDTO>
    )

    fun saveVisitorDetails(
        @NonNull userDetailsDTO: UserDetailsDTO,
        @NonNull callback: IDataSourceCallback<VisitorResponseDTO>
    )

    fun verifyOtpVisitor(
        @NonNull mobileNo: String,
        @NonNull otp: String,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun sendOtpVisitor(
        @NonNull mobileNo: String,
        @NonNull callback: IDataSourceCallback<VisitorResponseDTO>
    )

    fun checkInVisitor(
        @NonNull checkInDTO: CheckInDTO,
        @NonNull callback: IDataSourceCallback<String>
    )


    fun generateBill(
        @NonNull userId: Long,
        @NonNull outletId: Long,
        @NonNull inCustomerRequestId: Long,
        @NonNull inCheckInCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<BillGenerationDTO>
    )


    fun getBillDetails(
        @NonNull inCheckInCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<BillGenerationDTO>
    )

    fun changeBillDetailsStatusToPaid(
        @NonNull inCheckInCustomerRequestId: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getWishListByUserId(
        @NonNull userId: Long,
        @NonNull callback: IDataSourceCallback<WishListDTO>
    )

    fun changeWishListItemStatus(
        @NonNull itemId: Long,
        @NonNull status: String,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun deleteWishListItem(
        @NonNull itemId: Long,
        @NonNull status: Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )


    fun saveStockCustomizations(
        arrayList: ArrayList<StocksCustomizationDTO>,
        callback: IDataSourceCallback<String>
    )

    fun fetchAllStockCustomizationsByProductOrDishId(
        productId: Long,
        callback: IDataSourceCallback<ArrayList<StocksCustomizationDTO>>
    )


}
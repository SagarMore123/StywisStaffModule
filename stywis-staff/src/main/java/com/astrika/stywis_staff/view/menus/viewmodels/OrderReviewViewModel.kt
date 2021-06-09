package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.OrderBundleDTO
import com.astrika.stywis_staff.models.OrderDTO
import com.astrika.stywis_staff.models.SaveOrderDTO
import com.astrika.stywis_staff.models.SaveOrderProductDetails
import com.astrika.stywis_staff.models.menu.CatalogueCategoryDTO
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.*
import java.math.BigDecimal
import java.math.RoundingMode

class OrderReviewViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var onConfirmOrderClick = SingleLiveEvent<Void>()

    var showProgressBar = MutableLiveData<Boolean>()
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CatalogueCategoryDTO>>()
    var selectedOrderDishArrayList = ArrayList<DishDetailsDTO>()
    var selectedOrderDishArrayListMutableLiveData = MutableLiveData<ArrayList<DishDetailsDTO>>()

    var quantityPriceMutable = MutableLiveData("")

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var outletId = 0L
    var userId = 0L
    var tableId = 1L
    var displayTableNos = MutableLiveData("")
    var orderId = 0L
    var totalOrderItems = 0
    var taxPercentage = 5
    var totalOrderPrice = MutableLiveData(BigDecimal.ZERO)
    var discountPrice = MutableLiveData(BigDecimal.ZERO)
    var taxAndCharges = MutableLiveData(BigDecimal.ZERO)
    var toPayableAmount = MutableLiveData(BigDecimal.ZERO)
    var isViewOrder = MutableLiveData(false)
    var isSaveOrderSuccess = MutableLiveData(false)
    var orderBundleDTO = OrderBundleDTO()

    init {

        //        sharedPreferences.edit().putBoolean(Constants.IS_ORDER_BACK_PRESSED, true).apply()

        outletId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        userId =
            Constants.decrypt(sharedPreferences.getString(Constants.LOGIN_USER_ID, ""))?.toLong()
                ?: 0L

        orderBundleDTO = Utils.getOrderBundleDTO(
            Constants.decrypt(
                sharedPreferences.getString(
                    Constants.ORDER_BUNDLE_DTO,
                    ""
                )
            )
        ) ?: OrderBundleDTO()
        displayTableNos.value = orderBundleDTO.displayTableNos

        when (orderBundleDTO.requestType) {

            RequestTypeEnum.CHECK_IN.name, RequestTypeEnum.CALL_FOR_SERVICE.name -> isViewOrder.value =
                false

            RequestTypeEnum.CALL_FOR_ORDER.name -> {

                when (orderBundleDTO.requestStatus) {

                    RequestStatusEnum.PENDING.name -> isViewOrder.value = false
                    RequestStatusEnum.AWAITING_CONFIRMATION.name -> isViewOrder.value = false
                    RequestStatusEnum.APPROVED.name -> isViewOrder.value = true
                    RequestStatusEnum.COMPLETED.name -> isViewOrder.value = true

                }
            }

            RequestTypeEnum.CALL_FOR_BILL.name -> isViewOrder.value = true


        }


    }

    fun getAllMenuCategories() {

        showProgressBar.value = true
        dashboardRepository.getAllMenuCategories(
            outletId,
            object : IDataSourceCallback<GetMenuCategoriesResponseDTO> {
                override fun onDataFound(data: GetMenuCategoriesResponseDTO) {
                    menuCategoryMutableArrayList.value = data.activeCatalogueCategoryDTOs
                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    menuCategoryMutableArrayList.value = arrayListOf()
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    menuCategoryMutableArrayList.value = arrayListOf()
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }

    fun onConfirmOrderClick() {
        if (!selectedOrderDishArrayList.isNullOrEmpty()) {
//            onConfirmOrderClick.call()
            saveOrder()
        }
    }

    fun showOrderDetails() {
        totalOrderItems = 0
        var totalPrice = BigDecimal(0)
        var totalTax = BigDecimal(0)
        for (dishDetails in selectedOrderDishArrayList) {
            totalOrderItems += dishDetails.quantity ?: 0
//            val dishPrice = if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) dishDetails.productDiscountPrice ?: BigDecimal.ZERO else dishDetails.productOriPrice ?: BigDecimal.ZERO
            var dishPrice: BigDecimal? = BigDecimal(0)
            if (dishDetails.isCustomizable) {
                dishPrice = dishDetails.productCustomizedPrice
            } else {
                dishPrice =
                    if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) dishDetails.productDiscountPrice
                        ?: BigDecimal.ZERO else dishDetails.productOriPrice ?: BigDecimal.ZERO

            }
            totalPrice += (dishPrice?.times(
                dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO
            )) ?: BigDecimal.ZERO
            totalTax += (dishPrice?.times(
                dishDetails.quantity?.toBigDecimal()
                    ?: BigDecimal.ZERO
            )?.times(
                dishDetails.taxValue?.toBigDecimal()
                    ?.divide(BigDecimal(100).setScale(2, RoundingMode.HALF_UP)) ?: BigDecimal.ZERO
            )) ?: BigDecimal.ZERO
        }
        totalOrderPrice.value =
            totalPrice.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO

        taxAndCharges.value =
            totalTax.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
        val amount = totalPrice.plus(totalTax)
        toPayableAmount.value =
            amount.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
//            totalOrderPrice.value ?: 0 .plus(taxAndCharges.value ?: 0).plus(discountPrice.value ?: 0)

        quantityPriceMutable.value = "$totalOrderItems Items | ₹  ${toPayableAmount.value}"

        val value = Utils.setData(selectedOrderDishArrayList)
        sharedPreferences.edit().putString(Constants.ORDER_DISH_LIST, Constants.encrypt(value))
            .apply()

        selectedOrderDishArrayListMutableLiveData.value = selectedOrderDishArrayList


    }

    private fun saveOrder() {
        val saveOrderDetailsList = ArrayList<SaveOrderProductDetails>()
        for (dish in selectedOrderDishArrayList) {
            val dishPrice =
                if (dish.productDiscountPrice != null && dish.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) dish.productDiscountPrice
                    ?: BigDecimal.ZERO else dish.productOriPrice ?: BigDecimal.ZERO
            val totalDishPrice = dishPrice

            saveOrderDetailsList.add(
                SaveOrderProductDetails(
                    productId = dish.productId,
                    productName = dish.productName ?: "",
//                    productOriPrice = dish.productOriPrice?.toLong() ?: 0L,
//                    productDisPrice = dish.productDiscountPrice?.toLong() ?: 0L,
//                    totalItemPrice = totalDishPrice.toLong(),
                    itemId = dish.itemId,
                    sectionId = dish.catalogueSectionId?.get(0) ?: 0,
                    quantity = dish.quantity,
                    optionValueIds = dish.optionValueIds,
                    addOnValueIds = dish.addOnValueIds
//                    status = Constants.ORDER_PLACED,
//                    status = orderBundleDTO.requestStatus,
//                    status = Constants.ORDER_IN_PROGRESS,
                )
            )
        }
        val saveOrderDTO = SaveOrderDTO(
            orderId = orderId,
            outletId = outletId,
//            tableId = tableId,
            userId = orderBundleDTO.userId,
//            totalAmount = totalOrderPrice.value,
//            discountAmount = discountPrice.value,
//            netAmount = toPayableAmount.value,
//            paid = false,
//            takeOut = false,
//            remarks = "",
//            status = Constants.ORDER_IN_PROGRESS,
//            status = orderBundleDTO.requestStatus,
            inCheckInCustomerRequestId = if (orderBundleDTO.inCheckInCustomerRequestId == 0L) orderBundleDTO.inCustomerRequestId else orderBundleDTO.inCheckInCustomerRequestId,
            inCustomerRequestId = if (orderBundleDTO.inCheckInCustomerRequestId == 0L) orderBundleDTO.inCheckInCustomerRequestId else orderBundleDTO.inCustomerRequestId,
//            inCustomerRequestId = orderBundleDTO.inCheckInCustomerRequestId,
            items = saveOrderDetailsList
        )

        showProgressBar.value = true
        dashboardRepository.saveOrderDetails(saveOrderDTO, object : IDataSourceCallback<String> {
            override fun onDataFound(data: String) {
                Constants.getSharedPreferences(activity).edit().remove(Constants.ORDER_DISH_LIST)
                    .apply()
                getmSnackbar().value = data
                showProgressBar.value = false
                isSaveOrderSuccess.value = true

            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun populateOrderDetails() {

        showProgressBar.value = true
/*

        when(orderBundleDTO.requestType){

            RequestTypeEnum.CALL_FOR_ORDER.name->{
//                orderBundleDTO.inCustomerRequestId = orderBundleDTO.inCheckInCustomerRequestId
            }

        }
*/

        dashboardRepository.getOrderDetails(
            orderBundleDTO.inCustomerRequestId ?: 0,
            object : IDataSourceCallback<OrderDTO> {
                override fun onDataFound(data: OrderDTO) {

                    orderId = data.orderId ?: 0
                    outletId = data.outletId ?: 0
                    tableId = data.tableId ?: 0
                    /* var tables=""
                     for (tablesNo in  orderBundleDTO.tableIdsAlloted?: arrayListOf()){
                         tables += tablesNo.toString()
                     }*/
//                    displayTableNos.value = orderBundleDTO.displayTableNos

                    userId = data.userId ?: 0

                    val arrayList = arrayListOf<DishDetailsDTO>()

                    for (item in data.items ?: arrayListOf()) {

                        val dishDetailsDTO = DishDetailsDTO()
                        dishDetailsDTO.productId = item.productId ?: 0
                        dishDetailsDTO.productName = item.productName ?: ""
                        dishDetailsDTO.quantity = item.quantity ?: 0
                        dishDetailsDTO.taxValue = item.taxAmount ?: 0.0
                        dishDetailsDTO.productOriPrice =
                            item.productOriPrice?.toBigDecimal() ?: BigDecimal.ZERO
                        dishDetailsDTO.productDiscountPrice =
                            item.totalItemPrice?.toBigDecimal() ?: BigDecimal.ZERO
                        dishDetailsDTO.itemId = item.itemId ?: 0

                        if (!item.optionValueIds.isNullOrEmpty() || !item.addOnValueIds.isNullOrEmpty()) {
                            dishDetailsDTO.isCustomizable = true
                        }
                        dishDetailsDTO.optionValueIds = item.optionValueIds ?: arrayListOf()
                        dishDetailsDTO.addOnValueIds = item.addOnValueIds ?: arrayListOf()

                        arrayList.add(dishDetailsDTO)
                    }

                    selectedOrderDishArrayList = arrayList
                    selectedOrderDishArrayListMutableLiveData.value = selectedOrderDishArrayList

                    taxAndCharges.value =
                        data.taxAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
                            ?: BigDecimal.ZERO
                    totalOrderPrice.value =
                        data.netAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
                            ?: BigDecimal.ZERO
                    toPayableAmount.value =
                        data.totalAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
                            ?: BigDecimal.ZERO
                    discountPrice.value =
                        data.discountAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
                            ?: BigDecimal.ZERO


                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }


}
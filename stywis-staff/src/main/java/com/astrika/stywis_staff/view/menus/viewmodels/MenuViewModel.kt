package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.OrderBundleDTO
import com.astrika.stywis_staff.models.menu.CatalogueCategoryDTO
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.SingleLiveEvent
import com.astrika.stywis_staff.utils.Utils

class MenuViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var onPreviewOrder = SingleLiveEvent<Void>()
    var onWishListClick = SingleLiveEvent<Void>()

    var showProgressBar = MutableLiveData<Boolean>()
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CatalogueCategoryDTO>>()
    var selectedOrderDishArrayList = ArrayList<DishDetailsDTO>()
    var selectedOrderDishArrayListMutableLiveData = MutableLiveData<ArrayList<DishDetailsDTO>>()

    var quantityPriceMutable = MutableLiveData<String>("")
    var displayTableNos = MutableLiveData<String>("")

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var outLetId = 0L
    var totalOrderItems = 0
    var totalOrderPrice = 0
    var orderBundleDTO = OrderBundleDTO()

    init {

        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        orderBundleDTO = Utils.getOrderBundleDTO(
            Constants.decrypt(
                sharedPreferences.getString(
                    Constants.ORDER_BUNDLE_DTO,
                    ""
                )
            )
        ) ?: OrderBundleDTO()

        displayTableNos.value = orderBundleDTO.displayTableNos

    }

    fun getAllMenuCategories() {

        showProgressBar.value = true
        dashboardRepository.getAllMenuCategories(
            outLetId,
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

    fun onWishListClick() {
        onWishListClick.call()
    }

    fun onPreviewOrderClick() {
        if (selectedOrderDishArrayList.size > 0) {
            onPreviewOrder.call()
        }
    }


    fun displayOrderDetails(quantityPrice: String, arrayList: ArrayList<DishDetailsDTO>) {
        quantityPriceMutable.value = quantityPrice
        this.selectedOrderDishArrayList = arrayList
    }


/*


    fun showOrderDetails(arrayList: ArrayList<DishDetailsDTO>) {
        this.selectedOrderDishArrayList = arrayList
        totalOrderItems = 0
        var totalPrice = BigDecimal(0)
        var totalTax = BigDecimal(0)
        for (dishDetails in arrayList ?: arrayListOf()) {
            totalOrderItems += dishDetails.quantity ?: 0
            val dishPrice =
                if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice > BigDecimal.ZERO) dishDetails.productDiscountPrice else dishDetails.productOriPrice
            totalPrice += (dishPrice * (dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO))
            totalTax += (dishPrice * (dishDetails.quantity?.toBigDecimal()
                ?: BigDecimal.ZERO).times(
                dishDetails.taxValue?.toBigDecimal()
                    ?.divide(BigDecimal(100).setScale(2, RoundingMode.HALF_UP)) ?: BigDecimal.ZERO
            ))
        }
//        totalOrderPrice = totalPrice.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO

//        taxAndCharges.value = totalTax.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
//        val amount = totalPrice.plus(totalTax)
//        toPayableAmount.value = amount.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
//            totalOrderPrice.value ?: 0 .plus(taxAndCharges.value ?: 0).plus(discountPrice.value ?: 0)

        quantityPriceMutable.value = "$totalOrderItems Items | ₹  $totalPrice"


    }


    fun displayCalculation(position: Int, menuDishDetails: DishDetailsDTO){

        var dishAlreadyAdded = false
        totalOrderItems = 0
        totalOrderPrice = 0

        for (dishDetails in selectedOrderDishArrayList) {
            if (dishDetails.productId == menuDishDetails.productId) {
                dishDetails.quantity = menuDishDetails.quantity
                dishAlreadyAdded = true
            }
            val dishPrice =
                if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice > BigDecimal.ZERO) dishDetails.productDiscountPrice else dishDetails.productOriPrice
            totalOrderItems += dishDetails.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(
                dishPrice.times(
                    dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO
                ).toInt()
            )
        }
        if (!dishAlreadyAdded) {

            selectedOrderDishArrayList.add(menuDishDetails)

            val dishPrice =
                if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice > BigDecimal.ZERO) menuDishDetails.productDiscountPrice else menuDishDetails.productOriPrice
            totalOrderItems += menuDishDetails.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(dishPrice.toInt())
        }

        if (totalOrderItems > 0)
            quantityPriceMutable.value =
                "$totalOrderItems Items | ₹  $totalOrderPrice"
        else
            quantityPriceMutable.value = ""
    }

*/


}
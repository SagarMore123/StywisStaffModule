package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.CommonListingDTO
import com.astrika.stywis_staff.models.CommonSortDTO
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import java.math.BigDecimal

class MenuPagerViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var searchString = MutableLiveData<String>()
    var menuSectionArrayList = ArrayList<MenuSectionWithDishDetails>()
    var menuSectionMutableArrayList = MutableLiveData<ArrayList<MenuSectionWithDishDetails>>()
    var clearSearchVisible = ObservableBoolean(false)

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var productCategoryId = 0L
    var outLetId = 0L
    var isConfirm = MutableLiveData<Boolean>(false)


    var quantityPriceMutable = MutableLiveData("")
    var totalOrderItems = 0
    var totalOrderPrice = 0
    var selectedOrderDishArrayList = ArrayList<DishDetailsDTO>()
    var selectedOrderDishArrayListMutableLiveData = MutableLiveData<ArrayList<DishDetailsDTO>>()


    init {

        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
//        outLetId = 13

    }


    fun getAllDishesWithSections(search: String) {

        val commonListingDTO = CommonListingDTO()
        val defaultSort = CommonSortDTO()
        defaultSort.sortField = Constants.DISH_FLAG_ID_COLUMN
        defaultSort.sortOrder = Constants.SORT_ORDER_ASC
        commonListingDTO.defaultSort = defaultSort

        showProgressBar.value = true
        dashboardRepository.getAllDishes(
            productCategoryId,
            outLetId,
            search,
            commonListingDTO,
            object : IDataSourceCallback<ArrayList<MenuSectionWithDishDetails>> {
                override fun onDataFound(data: ArrayList<MenuSectionWithDishDetails>) {

                    val updatedList =
                        data.filter { !it.activeProductList.isNullOrEmpty() } as ArrayList
                    updateProductList(updatedList)

                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    menuSectionMutableArrayList.value = arrayListOf()
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }


    fun updateProductList(data: ArrayList<MenuSectionWithDishDetails>) {

        var totalOrderItems = 0
        var totalOrderPrice = 0

        for ((i, item) in selectedOrderDishArrayList.withIndex()) {

            sectionLoop@ for (item2 in data) {

                if (item.catalogueSectionId?.get(0)
                        ?.toLong() ?: 0 == item2.catalogueSectionDTO.catalogueSectionId ?: 0
                ) {

                    for (item3 in item2.activeProductList ?: arrayListOf()) {

                        if (item.productId == item3.productId) {

                            item3.quantity = item3.quantity?.plus(item.quantity ?: 0)

                            break@sectionLoop
//


                        }
                    }
                }

            }

            var dishPrice: BigDecimal? = BigDecimal(0)
            if (item.isCustomizable) {
                dishPrice = item.productCustomizedPrice ?: BigDecimal.ZERO
            } else {
                dishPrice =
                    if (item.productDiscountPrice != null && item.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) item.productDiscountPrice
                        ?: BigDecimal.ZERO else item.productOriPrice ?: BigDecimal.ZERO

            }
            totalOrderItems += item.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(
                dishPrice?.times(
                    item.quantity?.toBigDecimal() ?: BigDecimal.ZERO
                )?.toInt() ?: 0
            )

        }

        menuSectionArrayList = data
        menuSectionMutableArrayList.value = data


        if (totalOrderItems > 0)
            quantityPriceMutable.value =
                "$totalOrderItems Items | ₹  $totalOrderPrice"
        else
            quantityPriceMutable.value = ""


    }


    fun onCloseSearchClick() {
        searchString.value = ""
        getAllDishesWithSections("")
    }

/*
    fun onUpdateMenuClick(){

        showProgressBar.value = true

        var dishAvalabilityList = ArrayList<DishAvailabilityDTO>()
        for (menuSection in menuSectionMutableArrayList.value!!){
            for(dishDetails in menuSection.activedishList){
                dishAvalabilityList.add(
                    DishAvailabilityDTO(
                        dishDetails.productId,
                        dishDetails.outletId,
                        dishDetails.isOutOfStock
                    )
                )
            }
        }

        dashboardRepository?.saveDishAvailability(dishAvalabilityList, object : IDataSourceCallback<String>{
            override fun onSuccess(message: String) {
                getmSnackbar().value = message
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })

    }
    */


    fun displayCalculation(isRepeat: Boolean, menuDishDetails: DishDetailsDTO) {

        var dishAlreadyAdded = false
        totalOrderItems = 0
        totalOrderPrice = 0

        for ((i, dishDetails) in selectedOrderDishArrayList.withIndex()) {
            if (dishDetails.productId == menuDishDetails.productId) {
//                dishDetails.quantity = menuDishDetails.quantity

                updateProducts(dishDetails, menuDishDetails)
                dishAlreadyAdded = true
            }

            var dishPrice: BigDecimal? = BigDecimal(0)
            if (dishDetails.isCustomizable) {
                dishPrice = dishDetails.productCustomizedPrice
            } else {
                dishPrice =
                    if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) dishDetails.productDiscountPrice
                        ?: BigDecimal.ZERO else dishDetails.productOriPrice ?: BigDecimal.ZERO

            }
            totalOrderItems += dishDetails.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(
                dishPrice?.times(
                    dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO
                )?.toInt() ?: 0
            )
        }
        if (!dishAlreadyAdded) {
            selectedOrderDishArrayList.add(menuDishDetails)

            var dishPrice: BigDecimal? = BigDecimal(0)
            if (menuDishDetails.isCustomizable) {
                dishPrice = menuDishDetails.productCustomizedPrice
            } else {
                dishPrice =
                    if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) menuDishDetails.productDiscountPrice
                        ?: BigDecimal.ZERO else menuDishDetails.productOriPrice ?: BigDecimal.ZERO
            }
            totalOrderItems += menuDishDetails.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(dishPrice?.toInt() ?: 0)
        }

        if (totalOrderItems > 0)
            quantityPriceMutable.value =
                "$totalOrderItems Items | ₹  $totalOrderPrice"
        else
            quantityPriceMutable.value = ""
    }


    fun displayCalculation2(isRepeat: Boolean, menuDishDetails: DishDetailsDTO) {

//        menuDishDetails.quantity = menuDishDetails.quantity?.plus(1)
        menuDishDetails.quantity = 1
        selectedOrderDishArrayList.add(menuDishDetails)

        var dishAlreadyAdded = false
        totalOrderItems = 0
        totalOrderPrice = 0

        for ((i, dishDetails) in selectedOrderDishArrayList.withIndex()) {

            var dishPrice: BigDecimal? = BigDecimal(0)
            if (dishDetails.isCustomizable) {
                dishPrice = dishDetails.productCustomizedPrice
            } else {
                dishPrice =
                    if (dishDetails.productDiscountPrice != null && dishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) dishDetails.productDiscountPrice
                        ?: BigDecimal.ZERO else dishDetails.productOriPrice ?: BigDecimal.ZERO

            }
            totalOrderItems += dishDetails.quantity ?: 0
            totalOrderPrice = totalOrderPrice.plus(
                dishPrice?.times(dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO)?.toInt()
                    ?: 0
            )
        }

        if (totalOrderItems > 0)
            quantityPriceMutable.value =
                "$totalOrderItems Items | ₹  $totalOrderPrice"
        else
            quantityPriceMutable.value = ""
    }


    private fun updateProducts(dishDetails: DishDetailsDTO, menuDishDetails: DishDetailsDTO) {

        dishDetails.productId = menuDishDetails.productId
        dishDetails.productName = menuDishDetails.productName
        dishDetails.productDesc = menuDishDetails.productDesc
        dishDetails.productOriPrice = menuDishDetails.productOriPrice ?: BigDecimal.ZERO
        dishDetails.productDiscountPrice = menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO
        dishDetails.productCustomizedPrice =
            menuDishDetails.productCustomizedPrice ?: BigDecimal.ZERO
        dishDetails.itemId = menuDishDetails.itemId
        dishDetails.productImage = menuDishDetails.productImage
        dishDetails.availableAllTime = menuDishDetails.availableAllTime
        dishDetails.productSequenceNo = menuDishDetails.productSequenceNo
        dishDetails.maxOrderQty = menuDishDetails.maxOrderQty
        dishDetails.minOrderQty = menuDishDetails.minOrderQty
        dishDetails.outletId = menuDishDetails.outletId
        dishDetails.specialNotes = menuDishDetails.specialNotes
        dishDetails.cuisineIds = menuDishDetails.cuisineIds
        dishDetails.productFlags = menuDishDetails.productFlags
        dishDetails.productFlagDTOs = menuDishDetails.productFlagDTOs
        dishDetails.catalogueCategoryDTO = menuDishDetails.catalogueCategoryDTO
        dishDetails.catalogueCategoryId = menuDishDetails.catalogueCategoryId
        dishDetails.menuSectionDTO = menuDishDetails.menuSectionDTO
        dishDetails.catalogueSectionId = menuDishDetails.catalogueSectionId
        dishDetails.active = menuDishDetails.active
        dishDetails.isOutOfStock = menuDishDetails.isOutOfStock
        dishDetails.isCustomizable = menuDishDetails.isCustomizable
        dishDetails.productTimingDTO = menuDishDetails.productTimingDTO
        dishDetails.quantity = menuDishDetails.quantity
        dishDetails.taxValue = menuDishDetails.taxValue
        dishDetails.optionValueIds = menuDishDetails.optionValueIds
        dishDetails.addOnValueIds = menuDishDetails.addOnValueIds

    }

}
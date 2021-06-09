package com.astrika.stywis_staff.view.stockmanagement.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.CommonListingDTO
import com.astrika.stywis_staff.models.CommonSortDTO
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable

class StockPagerViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var searchString = MutableLiveData<String>()
    var menuCategoryArrayList = ArrayList<MenuSectionWithDishDetails>()
    var menuSectionMutableArrayList = MutableLiveData<ArrayList<MenuSectionWithDishDetails>>()
    var clearSearchVisible = ObservableBoolean(false)

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var productCategoryId = 0L
    var outLetId = 0L

    init {

        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
//        outLetId = 13

    }

    fun getAllDishesWithSections(search: String) {

        val commonListingDTO = CommonListingDTO()
        var defaultSort = CommonSortDTO()
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
                    menuCategoryArrayList = data
                    menuSectionMutableArrayList.value = data
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
}
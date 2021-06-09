package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.menu.CatalogueCategoryDTO
import com.astrika.stywis_staff.models.menu.GetMenuCategoriesResponseDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable

class DishAvailabilityViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CatalogueCategoryDTO>>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var outLetId = 0L

    init {

        outLetId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong()
            ?: 0L
//        outLetId = 13

    }

    fun getAllMenuCategories() {

        showProgressBar.value = true
        dashboardRepository.getAllMenuCategories(outLetId, object :
            IDataSourceCallback<GetMenuCategoriesResponseDTO> {
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


}
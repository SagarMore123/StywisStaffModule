package com.astrika.stywis_staff.view.stockmanagement.viewmodels

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

class StockViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var onPreviewOrder = SingleLiveEvent<Void>()

    var showProgressBar = MutableLiveData<Boolean>()
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CatalogueCategoryDTO>>()
    var selectedOrderDishArrayList = ArrayList<DishDetailsDTO>()

    var quantityPriceMutable = MutableLiveData<String>("")
    var displayTableNos = MutableLiveData<String>("")

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var outLetId = 0L
    var totalOrderItems = 0
    var totalOrderPrice = 0
    var orderBundleDTO = OrderBundleDTO()

    init {

        outLetId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong()
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

    fun onPreviewOrderClick() {
        if (selectedOrderDishArrayList.size > 0) {
            onPreviewOrder.call()
        }
    }

}
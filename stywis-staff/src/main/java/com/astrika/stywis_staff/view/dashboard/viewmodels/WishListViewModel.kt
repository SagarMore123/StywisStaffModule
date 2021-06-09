package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.WishListDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.SingleLiveEvent

class WishListViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var checkInUserId: Long? = 0L
    var onCloseDialogClick = SingleLiveEvent<Void>()
    var wishListMutableLiveData = MutableLiveData<WishListDTO>()
    var noWishListItemsFound = ObservableBoolean()

    fun closeDialog() {
        onCloseDialogClick.call()
    }

    fun populateWishList() {
        showProgressBar.value = true
        checkInUserId?.let {
            dashboardRepository.getWishListByUserId(it, object : IDataSourceCallback<WishListDTO> {

                override fun onDataFound(data: WishListDTO) {
                    showProgressBar.value = false
                    wishListMutableLiveData.value = data
                    noWishListItemsFound.set(false)
                }

                override fun onDataNotFound() {
                    noWishListItemsFound.set(true)
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }
    }

    fun changeWishListItemStatus(itemId: Long, status: String) {
        showProgressBar.value = true
        dashboardRepository.changeWishListItemStatus(
            itemId,
            status,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }

    fun deleteWishListItem(itemId: Long, status: Boolean) {
        dashboardRepository.deleteWishListItem(
            itemId,
            status,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    getmSnackbar().value = data
                    populateWishList()
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

    }

}
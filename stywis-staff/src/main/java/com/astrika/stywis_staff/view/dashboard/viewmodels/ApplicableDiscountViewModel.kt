package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.DaysEnum
import com.astrika.stywis_staff.models.OrderBundleDTO
import com.astrika.stywis_staff.models.OutletDiscountDetailsDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class ApplicableDiscountViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private var outLetId = 0L
    private var userId = 0L

    var applicableDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var applicableDiscountMutableLiveArrayList =
        MutableLiveData<ArrayList<OutletDiscountDetailsDTO>>()
    var orderBundleDTO = OrderBundleDTO()
    var displayTableNos = MutableLiveData("")

    init {
        outLetId =
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

        populateOutletApplicableDiscountsList()
    }

    private fun populateOutletApplicableDiscountsList() {

        showProgressBar.value = true
        val day = Calendar.getInstance()
            .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())

        var weekDay = 0
        for (item in DaysEnum.values()) {
            if (item.value == day) {
                weekDay = item.id
                break
            }
        }

        userRepository.fetchOutletApplicableDiscountsList(outLetId, userId,
            orderBundleDTO.inCheckInCustomerRequestId ?: 0,
            weekDay.toLong(),
            object : IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>> {

                override fun onDataFound(data: ArrayList<OutletDiscountDetailsDTO>) {
                    applicableDiscountArrayList = data
                    applicableDiscountMutableLiveArrayList.value = applicableDiscountArrayList

                    showProgressBar.value = false

                }

                override fun onDataNotFound() {
                    applicableDiscountMutableLiveArrayList.value = arrayListOf()
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }


}
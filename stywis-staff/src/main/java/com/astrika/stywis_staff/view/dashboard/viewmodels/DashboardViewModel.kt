package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.DashboardDrawerDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.view.login.UserLoginActivity

class DashboardViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var isConfirm = MutableLiveData<Boolean>()

    var dashboardDrawerArrayList = ArrayList<DashboardDrawerDTO>()
    var dashboardDrawerListMutableLiveData = MutableLiveData<ArrayList<DashboardDrawerDTO>>()

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    var profilePathObservableField = ObservableField("")

    var firstName = MutableLiveData("")
    var lastName = MutableLiveData("")
    var mobileNumber = ""
    var location = MutableLiveData("")
    var outletName = ""

    init {
        firstName.value =
            Constants.decrypt(sharedPreferences.getString(Constants.FIRST_NAME, "")).toString()
        lastName.value =
            Constants.decrypt(sharedPreferences.getString(Constants.LAST_NAME, "")).toString()
        mobileNumber =
            Constants.decrypt(sharedPreferences.getString(Constants.MOBILE_NO, "")).toString()
        outletName =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_NAME, "")).toString()

        if (firstName.value.isNullOrBlank() || firstName.value.equals("null", true)) {
            firstName.value = "Hi, "
        } else {
            firstName.value = "Hi, " + firstName.value
        }

        if (lastName.value.isNullOrBlank() || lastName.value.equals("null", true)) {
            lastName.value = "Guest"
        }

        if (mobileNumber.isNullOrBlank() || mobileNumber.equals("null", true)) {
            mobileNumber = ""
        }

    }

    fun fetchDrawerMenu() {
        masterRepository.fetchDashboardDrawerMasterDataLocal(object :
            IDataSourceCallback<List<DashboardDrawerDTO>> {

            override fun onDataFound(data: List<DashboardDrawerDTO>) {
                dashboardDrawerArrayList.clear()
                dashboardDrawerArrayList = data as ArrayList<DashboardDrawerDTO>
                /*for (item in dashboardDrawerArrayList) {
                    if (item.name == Constants.MENU_MANAGEMENT) {
                        item.applicationModules = setAdditionalMenuData()
                        break
                    }
                }*/
                val dashboardDrawerDTO = DashboardDrawerDTO()
                dashboardDrawerDTO.name = "Logout"
                dashboardDrawerArrayList.add(dashboardDrawerArrayList.size, dashboardDrawerDTO)
                dashboardDrawerListMutableLiveData.value = dashboardDrawerArrayList
//                Log.e("data", data.toString())
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })
    }

    fun showLogoutDialog() {
        Constants.showCommonDialog(
            activity,
            isConfirm,
            activity.resources.getString(R.string.do_you_want_to_logout)
        )

/*

        val alreadyLoginPopUpLayoutBinding: AlreadyLoginPopupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.already_login_popup_layout_staff, null, false
            )
        val view: View = alreadyLoginPopUpLayoutBinding.root
        val alert =
            AlertDialog.Builder(activity)
        // this is set the view from XML inside AlertDialog
        alert.setView(view)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()
        alreadyLoginPopUpLayoutBinding.titleDescription.text =
            activity.resources.getString(R.string.do_you_want_to_logout)
        alreadyLoginPopUpLayoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            logoutUser()
        })
        alreadyLoginPopUpLayoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
*/

    }


    fun logoutUser() {
        showProgressBar.value = true
        userRepository.logoutUser(object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                showProgressBar.value = false
                Constants.clearSharedPrefs(activity)
                Constants.changeActivityAndFinish<UserLoginActivity>(activity)
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun drawerAppModuleItemSelection(mainContainerPosition: Int, position: Int) {

        if (!dashboardDrawerArrayList.isNullOrEmpty()
            && dashboardDrawerArrayList[mainContainerPosition] != null
            && !dashboardDrawerArrayList[mainContainerPosition].applicationModules.isNullOrEmpty()
        ) {

            for ((i, module) in dashboardDrawerArrayList.withIndex()) {

                if (i == mainContainerPosition) {
                    for ((j, appModuleItem) in module.applicationModules?.withIndex()
                        ?: arrayListOf()) {

                        appModuleItem.selected = j == position
                    }
                } else {
                    for ((j, appModuleItem) in module.applicationModules?.withIndex()
                        ?: arrayListOf()) {
                        appModuleItem.selected = false
                    }
                }

            }
        }

        dashboardDrawerListMutableLiveData.value = dashboardDrawerArrayList
    }
}
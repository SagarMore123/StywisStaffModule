package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable

class DashboardFragmentViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?
) : GenericBaseObservable(application, owner, view) {

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)


}
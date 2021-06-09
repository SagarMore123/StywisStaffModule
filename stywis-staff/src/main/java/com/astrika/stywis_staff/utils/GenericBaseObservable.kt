package com.astrika.stywis_staff.utils

import android.app.Application
import android.content.Context
import android.view.View
import androidx.databinding.BaseObservable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.utils.SnackbarMessage.SnackBarObserver

open class GenericBaseObservable(application: Application, owner: LifecycleOwner?, view: View?) :
    BaseObservable() {
    @JvmField
    val dataLoading = MutableLiveData<Boolean>(false)
    private val mSnackbar = SnackbarMessage()
    fun getmSnackbar(): SnackbarMessage {
        return mSnackbar
    }

    private val mContext: Context
    fun getmContext(): Context {
        return mContext
    }

    fun setupSnackbar(owner: LifecycleOwner?, view: View?) {
        getmSnackbar().observe(owner, object : SnackBarObserver {
            override fun onNewMessage(message: String?) {

                SnackbarUtils.showSnackbar(view, message)
            }
        })
    }

    init {
        mContext = application
        owner?.let { setupSnackbar(it, view) }
    }
}
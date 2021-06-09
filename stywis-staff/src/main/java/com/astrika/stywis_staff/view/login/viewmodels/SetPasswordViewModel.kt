package com.astrika.stywis_staff.view.login.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.ResetPassword
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.ErrorCheckUtils
import com.astrika.stywis_staff.utils.GenericBaseObservable

class SetPasswordViewModel : GenericBaseObservable {

    var context: Context
    var application: Application
    var activity: Activity
    private var userRepository: UserRepository
    var showProgressBar = MutableLiveData<Boolean>()
    var loginId = MutableLiveData<String>("")
    var newPassword = MutableLiveData<String>("")
    var newPasswordError = MutableLiveData<String>("")
    var confirmPassword = MutableLiveData<String>("")
    var confirmPasswordError = MutableLiveData<String>("")
    lateinit var error: String
    var navigateToNextScreen = MutableLiveData<Boolean>(false)
    var closeClicked = MutableLiveData<Boolean>(false)

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository
    )
            : super(application, owner, view) {
        this.context = application.applicationContext
        this.application = application
        this.activity = activity
        this.userRepository = userRepository
    }

    fun onSignUpClick() {
//        Constants.changeActivity<SignUpActivity>(activity)
    }

    fun onProceed() {
        if (validateNewPassword() && validateConfirmPassword()) {
            val resetPassword = ResetPassword()
            resetPassword.newPassword = Constants.passwordEncrypt(newPassword.value ?: "")
            resetPassword.confirmNewPassword = Constants.passwordEncrypt(
                confirmPassword.value
                    ?: ""
            )
            resetPasswordServiceCall(resetPassword)
        }
    }

    private fun passwordsMatch(): Boolean {
        return newPassword.value!! == confirmPassword.value
    }

    private fun resetPasswordServiceCall(resetPassword: ResetPassword) {
        showProgressBar.value = true
        userRepository.resetPassword(resetPassword, object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                navigateToNextScreen.value = true
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                Constants.showToastMessage(context, error)
                showProgressBar.value = false
            }
        })
    }

    fun closeClick() {
        closeClicked.value = true
    }


    private fun validateNewPassword(): Boolean {
        error = ErrorCheckUtils.checkValidPassword(newPassword.value)
        if (error.isNotEmpty()) {
            newPasswordError.value = error
            return false
        } else {
            newPasswordError.value = ""
        }
        return true

    }

    private fun validateConfirmPassword(): Boolean {
        error = ErrorCheckUtils.checkValidPassword(confirmPassword.value)
        if (error.isNotEmpty()) {
            confirmPasswordError.value = error
            return false
        } else {
            confirmPasswordError.value = ""
        }
        return true
    }

}
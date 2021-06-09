package com.astrika.stywis_staff.view.login.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.AlreadyLoginPopupLayoutBinding
import com.astrika.stywis_staff.models.LoginDTO
import com.astrika.stywis_staff.models.LoginResponseDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.ErrorCheckUtils
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class FirstTimeLoginViewModel : GenericBaseObservable {

    var context: Context
    var activity: Activity
    private var userRepository: UserRepository
    var loginId = MutableLiveData("")
    var loginIdError = MutableLiveData("")
    lateinit var error: String
    var showProgressBar = MutableLiveData<Boolean>()
    var navigateToLogin = MutableLiveData<Boolean>()
    var loginResponseDTOLiveData = MutableLiveData<LoginResponseDTO>()
    private var firebaseToken: String? = null
    private var sharedPreferences: SharedPreferences

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository
    )
            : super(application, owner, view) {
        this.context = application.applicationContext
        this.activity = activity
        this.userRepository = userRepository
        sharedPreferences = Constants.getSharedPreferences(application)
        getFireBaseInstanceId()
    }

    private fun getFireBaseInstanceId() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            firebaseToken = task.result
            sharedPreferences.edit()
                .putString(Constants.DEVICE_KEY, Constants.encrypt(firebaseToken)).apply()
            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d("TAG", msg)
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        })
    }

    fun onSignUpClick() {
//        Constants.changeActivity<SignUpActivity>(activity)
    }

    fun onProceed() {
        validate()
    }


    private fun validate() {
        if (validateLoginId()) {
            val loginDTO = LoginDTO()
            loginDTO.username = loginId.value
            loginDTO.role = Constants.ROLE
            loginDTO.sourceId = Constants.SOURCE
            if (firebaseToken != null) {
//                loginDTO.deviceKey = firebaseToken
            }
            loginDTO.deviceKey = Constants.getDeviceKey(context)
            isFirstTimeLogin(loginDTO)
        }
    }

    private fun isFirstTimeLogin(loginDTO: LoginDTO) {
        showProgressBar.value = true
        userRepository.isFirstTimeLoginWithLoginId(
            loginDTO,
            object : IDataSourceCallback<LoginResponseDTO> {

                override fun onDataFound(data: LoginResponseDTO) {
                    if (data.isAlreadyLoggin) {
                        showAlreadyLoginDialog()
                    }
                    if (data.firstTimeLogin) {
                        loginResponseDTOLiveData.value = data
                        navigateToLogin.value = false
                    } else {
                        // redirect to login screen
                        navigateToLogin.value = true
                    }
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    if (error.contains("blocked") || error.contains("credentials do not match") || error.contains(
                            "User don't has access for this Application"
                        )
                    ) {
                        loginIdError.value = error
                    } else {
                        getmSnackbar().value = error
                    }
                    showProgressBar.value = false
                }
            })
    }

    private fun validateLoginId(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(loginId.value)
        if (error.isNotEmpty()) {
            loginIdError.value = error
            return false
        } else {
            loginIdError.value = ""
        }
        return true
    }

    fun showAlreadyLoginDialog() {
        val alreadyLoginPopUpLayoutBinding: AlreadyLoginPopupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.already_login_popup_layout, null, false
            )
        val view: View = alreadyLoginPopUpLayoutBinding.root
        val alert =
            AlertDialog.Builder(activity)
        // this is set the view from XML inside AlertDialog
        alert.setView(view)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()
        alreadyLoginPopUpLayoutBinding.yesBtn.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        alreadyLoginPopUpLayoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            Constants.clearSharedPrefs(context)
            activity.finishAffinity()
        })
        dialog.show()
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


}
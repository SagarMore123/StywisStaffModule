package com.astrika.stywis_staff.view.login.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.models.LoginDTO
import com.astrika.stywis_staff.models.LoginResponseDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.ErrorCheckUtils
import com.astrika.stywis_staff.utils.GenericBaseObservable
import java.util.concurrent.TimeUnit

class VerifyOtpViewModel : GenericBaseObservable {

    var context: Context
    var application: Application
    var activity: Activity
    private var userRepository: UserRepository
    lateinit var error: String
    var verifyOtpLoginId = MutableLiveData("")
    var countDownTimer = MutableLiveData("")
    var verifyOtpTimerVisibility = MutableLiveData<Boolean>(true)
    var verifyOtpEditTxt = MutableLiveData("")
    var loginResponseDTOLiveData = MutableLiveData<LoginResponseDTO>()
    var resendOtpClicked = MutableLiveData<Boolean>(false)
    var verifyOtpLoginIdError = MutableLiveData<String>()
    var verifyOtpLoginIdErrorVisibility = MutableLiveData<Boolean>(false)
    var verifyOtpError = MutableLiveData<String>()
    var verifyOtpErrorVisibility = MutableLiveData<Boolean>()
    var showProgressBar = MutableLiveData<Boolean>()
    var navigateToNextScreen = MutableLiveData<Boolean>(false)
    var isForgotPassword = MutableLiveData<Boolean>(false)
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

    fun onVerifyOtpProceed() {
        verifyOtpValidate()
    }

    private fun verifyOtpValidate() {
        if (validateOtp()) {
            val loginDTO = LoginDTO()
            loginDTO.isForgotPassword = isForgotPassword.value
            loginDTO.username = verifyOtpLoginId.value
            loginDTO.password = verifyOtpEditTxt.value
            loginDTO.role = Constants.ROLE
            loginDTO.sourceId = Constants.SOURCE
            verifyOtpServiceCall(loginDTO)
        }
    }

    private fun verifyOtpServiceCall(loginDTO: LoginDTO) {
        showProgressBar.value = true
        userRepository.verifyOtp(loginDTO, object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                showProgressBar.value = false
                navigateToNextScreen.value = true
            }

            override fun onError(error: String) {
                if (error.contains("blocked")) {
                    verifyOtpLoginIdErrorVisibility.value = true
                    verifyOtpTimerVisibility.value = false
//                    verifyOtpLoginIdError.value = error
                }
                verifyOtpEditTxt.value = ""
                showProgressBar.value = false
                Constants.showToastMessage(context, error)
            }
        })

    }

    fun closeClick() {
        closeClicked.value = true
    }

    private fun validateLoginId(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(verifyOtpLoginId.value)
        if (error.isNotEmpty()) {
            verifyOtpLoginIdError.value = error
            return false
        } else {
            verifyOtpLoginIdError.value = ""
        }
        return true
    }


    private fun validateOtp(): Boolean {
        error = ErrorCheckUtils.checkValidOTP(verifyOtpEditTxt.value)
        if (error.isNotEmpty()) {
            verifyOtpError.value = error
            verifyOtpErrorVisibility.value = true
            return false
        } else {
            verifyOtpError.value = ""
        }
        return true
    }

    fun resendOtp() {
        if (validateLoginId()) {
            val loginDTO = LoginDTO()
            loginDTO.username = verifyOtpLoginId.value
            loginDTO.isForgotPassword = isForgotPassword.value
            loginDTO.role = Constants.ROLE
            loginDTO.sourceId = Constants.SOURCE
            resendOtpClicked.value = true
            resendOtpServiceCall(loginDTO)
        }
    }

    private fun resendOtpServiceCall(loginDTO: LoginDTO) {
        showProgressBar.value = true
        userRepository.isFirstTimeLoginWithLoginId(loginDTO, object :
            IDataSourceCallback<LoginResponseDTO> {

            override fun onDataFound(data: LoginResponseDTO) {
                loginResponseDTOLiveData.value = data
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                if (error.contains("blocked")) {
                    verifyOtpLoginIdErrorVisibility.value = true
                    verifyOtpTimerVisibility.value = false
//                    verifyOtpLoginIdError.value = error
                }
                verifyOtpEditTxt.value = ""
                Constants.showToastMessage(context, error)
                showProgressBar.value = false
            }
        })
    }

    fun startTimer(timer: Int) {
        otpVerificationTimer = timer.toLong()

        if (otpCountDownTimer != null) {
            otpCountDownTimer?.cancel()
        }

        otpCountDownTimer = object : CountDownTimer((timer * 1000).toLong(), 1000) {
            override fun onTick(millis: Long) {
                otpVerificationTimer--
                val minuteSeconds = String.format(
                    "%02d:%02d",
                    (TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millis)
                    ))
                )
                countDownTimer.value = minuteSeconds
            }

            override fun onFinish() {
                verifyOtpTimerVisibility.value = false
            }

        }.start()


    }

    companion object {
        var otpVerificationTimer: Long = 0
        var otpCountDownTimer: CountDownTimer? = null
    }

}
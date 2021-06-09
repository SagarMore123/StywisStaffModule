package com.astrika.stywis_staff.view.login.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.FragmentForgotPasswordBinding
import com.astrika.stywis_staff.models.LoginResponseDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.login.viewmodels.VerifyOtpViewModel

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgotPasswordBinding
    lateinit var viewModel: VerifyOtpViewModel
    var progressBar = CustomProgressBar()
    lateinit var loginResponseDTO: LoginResponseDTO
    var emailAddress: String = ""
    var forgotPassword = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginResponseDTO = arguments?.get(Constants.LOGIN_RESPONSE) as LoginResponseDTO
        emailAddress = arguments?.getString(Constants.EMAIL_ID).toString()
        forgotPassword = arguments?.getBoolean(Constants.IS_FORGOT_PASS)!!


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forgot_password, container, false
        )
        viewModel = Utils.obtainBaseObservable(
            activity as AppCompatActivity,
            VerifyOtpViewModel::class.java,
            this,
            binding.root
        )!!
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserver()
        setData()
        hideViews()
    }


    private fun hideViews() {
        viewModel.verifyOtpErrorVisibility.value = false
    }

    private fun setData() {

        viewModel.isForgotPassword.value = forgotPassword
        emailAddress.let {
            viewModel.verifyOtpLoginId.value = it
        }
        showOtpMessage()
    }


    private fun viewModelObserver() {
        viewModel.loginResponseDTOLiveData.observe(viewLifecycleOwner, Observer {
            loginResponseDTO = it
            if (viewModel.resendOtpClicked.value == true) {
                showOtpMessage()
            }
        })

        viewModel.verifyOtpEditTxt.observe(viewLifecycleOwner, Observer {
            viewModel.verifyOtpError.value = ""
        })

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(activity as Activity, "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.navigateToNextScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                val bundle = Bundle()
                bundle.putString(Constants.EMAIL_ID, emailAddress)
                binding.root.findNavController()
                    .navigate(R.id.action_forgotPasswordFragment_to_setPasswordFragment, bundle)
            }
        })

        viewModel.closeClicked.observe(viewLifecycleOwner, Observer {
            if (it) {
                val bundle = Bundle()
                bundle.putString(Constants.EMAIL_ID, emailAddress)
                binding.root.findNavController()
                    .navigate(R.id.action_forgotPasswordFragment_to_loginFragment, bundle)
            }
        })


    }

    //start the timer and show the message
    private fun showOtpMessage() {
        val timer = loginResponseDTO.otpExpireTime
        viewModel.startTimer(timer)
        viewModel.verifyOtpTimerVisibility.value = true

        if (loginResponseDTO.message != null) {
            val message = loginResponseDTO.message
            Constants.showToastMessage(context, message)
        }
    }
}

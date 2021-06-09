package com.astrika.stywis_staff.view.profile.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.EditPasswordPopupLayoutBinding
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.utils.*

class ProfileViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    val profileImagePath = ObservableField<String>()
    lateinit var error: String
    var newPassword = MutableLiveData("")
    var newPasswordError = MutableLiveData("")
    var confirmPassword = MutableLiveData("")
    var confirmPasswordError = MutableLiveData("")

    var showProgressBar = MutableLiveData<Boolean>()
    var userDtoMutableLiveData = MutableLiveData<UserDTO>()
    var userDTO: UserDTO? = null
    var onEditClick = SingleLiveEvent<Void>()
    var isProfileUpdated = MutableLiveData<Boolean>(false)

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var userId = 0L
    var uri: Uri? = null


    init {
        userId =
            Constants.decrypt(sharedPreferences.getString(Constants.LOGIN_USER_ID, ""))?.toLong()
                ?: 0L
    }

    fun populateData() {

        showProgressBar.value = true
        dashboardRepository.getProfileDetails(
            userId,
            object : IDataSourceCallback<UserDTO> {
                override fun onDataFound(data: UserDTO) {

                    profileImagePath.set(data.profileImage?.path)

                    masterRepository.fetchDesignationMasterDataById(data.designationId ?: 0,
                        object : IDataSourceCallback<DesignationDTO> {

                            override fun onDataFound(designationDTO: DesignationDTO) {

                                data.designation = designationDTO.designationName ?: ""
                                userDTO = data
                                userDtoMutableLiveData.value = data
                                showProgressBar.value = false
                            }

                            override fun onError(error: String) {
                                userDTO = data
                                userDtoMutableLiveData.value = data
                                showProgressBar.value = false
                            }
                        })

                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }

/*
    fun onEditClick() {
        onEditClick.call()

    }
*/

    fun onEditClick() {

        val binding: EditPasswordPopupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.edit_password_popup_layout, null, false
            )
        binding.viewModel = this
        val view: View = binding.root
        val alert =
            AlertDialog.Builder(activity)
        // this is set the view from XML inside AlertDialog
        alert.setView(view)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true)
        val dialog = alert.create()


        binding.saveBtn.setOnClickListener {

            var isValid = true

            error = ErrorCheckUtils.checkValidPassword(binding.newPassEt.text.toString().trim())
            if (error.isNotBlank()) {
                binding.newPassErrEt.text = error
                binding.newPassErrEt.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.newPassErrEt.visibility = View.GONE
            }

            error = ErrorCheckUtils.checkValidPassword(binding.confirmPassEt.text.toString().trim())
            if (error.isNotBlank()) {
                binding.confirmPassErrEt.text = error
                binding.confirmPassErrEt.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.confirmPassErrEt.visibility = View.GONE
            }


            if (isValid) {

                if (binding.newPassEt.text.toString()
                        .trim() == binding.confirmPassEt.text.toString().trim()
                ) {
                    binding.confirmPassErrEt.visibility = View.GONE

                    val resetPassword = ResetPassword()
                    resetPassword.newPassword = Constants.passwordEncrypt(newPassword.value ?: "")
                    resetPassword.confirmNewPassword =
                        Constants.passwordEncrypt(confirmPassword.value ?: "")

                    showProgressBar.value = true
                    userRepository.resetPassword(
                        resetPassword,
                        object : IDataSourceCallback<String> {

                            override fun onDataFound(data: String) {
                                dialog.dismiss()
                                binding.newPassEt.setText("")
                                binding.confirmPassEt.setText("")
                                showProgressBar.value = false
                            }

                            override fun onError(error: String) {
                                getmSnackbar().value = error
                                showProgressBar.value = false
                            }
                        })

                } else {
                    binding.confirmPassErrEt.text = "Password did not match"
                    binding.confirmPassErrEt.visibility = View.VISIBLE
                }
            }

        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun updateProfileDetails() {

        if (uri != null) {
            val userDetailsDTO = UserDetailsDTO()
            val imageDTO = ImageDTO()
            imageDTO.documentName = "STAFF_PROFILE_IMAGE_${System.currentTimeMillis()}.jpg"
            imageDTO.base64 = Utils.getBitmapFromUri(application, uri)
                ?.let { Utils.convertBitmapToBase64(it) }
            imageDTO.id = 0
            userDetailsDTO.profileImage = imageDTO

            showProgressBar.value = true
            userDetailsDTO.let {
                dashboardRepository.updateProfileDetails(it, object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        Constants.PROFILE_IMAGE_URI = uri
                        sharedPreferences.edit()
                            .putString(Constants.PROFILE_IMAGE_PATH, Constants.encrypt("")).apply()
                        getmSnackbar().value = data
                        isProfileUpdated.value = true
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                        profileImagePath.set(profileImagePath.get()) // reset the profile image
                    }
                })
            }
        }
    }

}
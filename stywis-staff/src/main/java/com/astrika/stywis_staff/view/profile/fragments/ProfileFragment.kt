package com.astrika.stywis_staff.view.profile.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.PagerAdapter
import com.astrika.stywis_staff.databinding.FragmentProfileStaffBinding
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.profile.UpdateProfile
import com.astrika.stywis_staff.view.profile.viewmodels.ProfileViewModel
import com.theartofdev.edmodo.cropper.CropImage


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileStaffBinding
    lateinit var viewModel: ProfileViewModel

    lateinit var pagerAdapter: PagerAdapter
    private var progressBar = CustomProgressBar()

    lateinit var updateProfile: UpdateProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_staff, container, false)
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ProfileViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        pagerAdapter = PagerAdapter(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setObserver()
        viewModel.populateData()
        updateProfile = activity as UpdateProfile
        return binding.root
    }

    private fun setObserver() {

        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.userDtoMutableLiveData.observe(requireActivity(), Observer {
            it.let {

                pagerAdapter.addFragment(ProfileAboutPagerFragment(it), "About")
                pagerAdapter.addFragment(ProfileAddressInfoPagerFragment(it), "Address Info")
                pagerAdapter.addFragment(ProfileOtherInfoPagerFragment(it), "Other Info")

                pagerAdapter.notifyDataSetChanged()
            }
        })

        viewModel.isProfileUpdated.observe(viewLifecycleOwner, Observer {
            if (it) {
                updateProfile.updateProfile()
            }
        })

        binding.usrProfileImg.setOnClickListener {
            context?.let { it1 ->
                activity?.let { it2 ->
                    Utils.showChooseProfileDialog(
                        true,
                        it2,
                        it1,
                        this
                    )
                }
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        Utils.hideKeyboard(requireActivity())
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(resultData)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                binding.usrProfileImg.setImageURI(resultUri)
                viewModel.uri = resultUri
                viewModel.updateProfileDetails()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                viewModel.getmSnackbar().value = error.toString()
            }
        }

    }


}
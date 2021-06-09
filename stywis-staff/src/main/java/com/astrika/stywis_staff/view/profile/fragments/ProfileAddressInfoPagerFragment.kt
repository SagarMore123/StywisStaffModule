package com.astrika.stywis_staff.view.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.FragmentAddressInfoProfilePagerBinding
import com.astrika.stywis_staff.models.UserDTO
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.profile.viewmodels.ProfileViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileAddressInfoPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileAddressInfoPagerFragment(private val userDTO: UserDTO) : Fragment() {


    lateinit var binding: FragmentAddressInfoProfilePagerBinding
    lateinit var viewModel: ProfileViewModel

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_address_info_profile_pager,
            container,
            false
        )
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ProfileViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.userDTO = userDTO

        return binding.root
    }


}
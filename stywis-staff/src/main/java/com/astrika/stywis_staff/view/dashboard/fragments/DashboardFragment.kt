package com.astrika.stywis_staff.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.DashboardPagerAdapter
import com.astrika.stywis_staff.databinding.FragmentDashboardBinding
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.DashboardViewModel
import com.google.android.material.tabs.TabLayoutMediator


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    lateinit var mContext: Context
    lateinit var namesArray: Array<String>
    private lateinit var dashboardPagerAdapter: DashboardPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )
//        mContext = container?.context!!
        namesArray = resources.getStringArray(R.array.names)
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DashboardViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

//        dashboardPagerAdapter = activity?.let { DashboardPagerAdapter(it, namesArray) }
        dashboardPagerAdapter = DashboardPagerAdapter(requireActivity(), namesArray)
        binding.dashboardViewPager.adapter = dashboardPagerAdapter

        binding.dashboardTitleLayout.setOnClickListener { // Dashboard Toolbar Profile Image
            findNavController().navigate(R.id.profileFragment)
        }

        if (!Constants.decrypt(
                viewModel.sharedPreferences.getString(
                    Constants.PROFILE_IMAGE_PATH,
                    ""
                )
            )
                .isNullOrBlank()
        ) {
            viewModel.profilePathObservableField.set(
                Constants.decrypt(
                    viewModel.sharedPreferences.getString(
                        Constants.PROFILE_IMAGE_PATH,
                        ""
                    )
                )
            )
        } else {
            binding.profileImage.setImageURI(Constants.PROFILE_IMAGE_URI) // Dashboard Toolbar Profile Image
        }


        TabLayoutMediator(binding.dashboardTabLayout, binding.dashboardViewPager) { tab, position ->
//            tab.text = namesArray[position]
//            tab.orCreateBadge.number = 1
            tab.setCustomView(R.layout.tab_title_layout)
            val tabName: TextView? = tab.customView?.findViewById(R.id.tabTitle)
            val tabCount: TextView? = tab.customView?.findViewById(R.id.tabCount)
            tabName?.text = namesArray[position]
            tabCount?.text = "0"
        }.attach()


        //Back From saving order
        val boolean: Boolean = Constants.decrypt(
            viewModel.sharedPreferences.getString(
                Constants.IS_SAVE_ORDER_SUCCESS,
                ""
            )
        )?.toBoolean() ?: false
        if (boolean) {
            binding.dashboardViewPager.setCurrentItem(1, true)
            viewModel.sharedPreferences.edit().putString(
                Constants.IS_SAVE_ORDER_SUCCESS, Constants.encrypt(
                    false.toString()
                )
            ).apply()
        }


        return binding.root

    }


}

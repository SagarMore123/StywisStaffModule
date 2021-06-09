package com.astrika.stywis_staff.view.menus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.DishAvailabilityPagerAdapter
import com.astrika.stywis_staff.databinding.FragmentDishAvailabilityBinding
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.menus.viewmodels.DishAvailabilityViewModel
import com.google.android.material.tabs.TabLayout


class DishAvailabilityFragment : Fragment() {

    lateinit var binding: FragmentDishAvailabilityBinding
    lateinit var viewModel: DishAvailabilityViewModel

    lateinit var dishAvailabilityPagerAdapter: DishAvailabilityPagerAdapter
    private var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu, container, false)

//        binding = DataBindingUtil.findBinding<>()
        binding = DataBindingUtil.inflate<FragmentDishAvailabilityBinding>(
            inflater, R.layout.fragment_dish_availability,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DishAvailabilityViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dishAvailabilityPagerAdapter = DishAvailabilityPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = dishAvailabilityPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

//        setMenuPager()

        setObserver()

        viewModel.getAllMenuCategories()


    }

    fun setMenuPager() {

        /* dishAvailabilityPagerAdapter.addFragment(DishAvailabilityPagerFragment(), "Food")
         dishAvailabilityPagerAdapter.addFragment(DishAvailabilityPagerFragment(), "Bar Menu")
         dishAvailabilityPagerAdapter.addFragment(DishAvailabilityPagerFragment(), "Breakfast Menu")
         dishAvailabilityPagerAdapter.addFragment(DishAvailabilityPagerFragment(), "Breakfast Menu")
         dishAvailabilityPagerAdapter.addFragment(DishAvailabilityPagerFragment(), "Breakfast Menu")

         binding.viewPager.adapter = dishAvailabilityPagerAdapter
         binding.tabLayout.setupWithViewPager(binding.viewPager)*/

    }

    private fun setObserver() {
        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.menuCategoryMutableArrayList.observe(requireActivity(), Observer {
            it.let {
                for (menuCategory in it ?: arrayListOf()) {
                    dishAvailabilityPagerAdapter.addFragment(
                        DishAvailabilityPagerFragment(menuCategory.catalogueCategoryId ?: 0),
                        menuCategory.catalogueCategoryName ?: ""
                    )
                }
                if (it.size < 4)
                    binding.tabLayout.tabMode = TabLayout.MODE_FIXED
                /* else
                     binding.tabLayout.tabMode = TabLayout.MODE_FIXED*/
                dishAvailabilityPagerAdapter.notifyDataSetChanged()
            }
        })
    }
}
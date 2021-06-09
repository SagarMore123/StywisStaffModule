package com.astrika.stywis_staff.view.menus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.DishAvailabilitySectionAdapter
import com.astrika.stywis_staff.databinding.FragmentDishAvailabilityPagerBinding
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.menus.viewmodels.DishAvailabilityPagerViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DishAvailabilityPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DishAvailabilityPagerFragment(
    private val productCategoryId: Long
) : Fragment() {

    lateinit var binding: FragmentDishAvailabilityPagerBinding
    lateinit var viewModel: DishAvailabilityPagerViewModel

    lateinit var dishAvailabilitySectionAdapter: DishAvailabilitySectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_pager, container, false)

        binding = DataBindingUtil.inflate<FragmentDishAvailabilityPagerBinding>(
            inflater, R.layout.fragment_dish_availability_pager,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DishAvailabilityPagerViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.productCategoryId = productCategoryId

        dishAvailabilitySectionAdapter = DishAvailabilitySectionAdapter(requireContext())

        binding.dishAvailabilitySectionRecycler.adapter = dishAvailabilitySectionAdapter

        viewModel.getAllDishesWithSections("")

        viewModel.menuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let {
//                dishCategoryAdapter.setDishCategoryList(it)
                dishAvailabilitySectionAdapter.setMenuSectionList(it)
            }
        })
    }
}
package com.astrika.stywis_staff.view.stockmanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.stock.StockDishAdapter
import com.astrika.stywis_staff.adapters.stock.StockSectionAdapter
import com.astrika.stywis_staff.databinding.FragmentStockPagerBinding
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockPagerViewModel


class StockPagerFragment(
    private val productCategoryId: Long,
    private val listener: StockDishAdapter.OnDishClickListener
) : Fragment() {

    lateinit var binding: FragmentStockPagerBinding
    lateinit var viewModel: StockPagerViewModel

    lateinit var stockSectionAdapter: StockSectionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock_pager,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            StockPagerViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.productCategoryId = productCategoryId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stockSectionAdapter = StockSectionAdapter(requireContext(), listener)
        binding.menuSectionRecycler.adapter = stockSectionAdapter
//        menuSectionAdapter.setMenuSectionList(arrayListOf(null,null,null))

        viewModel.getAllDishesWithSections("")

        viewModel.menuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let {
//                dishCategoryAdapter.setDishCategoryList(it)
                stockSectionAdapter.setMenuSectionList(it)
            }
        })

        viewModel.searchString.observe(requireActivity(), Observer {
            it.let {
                viewModel.getAllDishesWithSections(it)
/*
                if (it.length > 2) {
                    viewModel.getAllDishesWithSections(it)
                    viewModel.clearSearchVisible.set(true)
                } else if (it.isEmpty()) {
                    viewModel.menuSectionMutableArrayList.value = viewModel.menuCategoryArrayList
                    viewModel.clearSearchVisible.set(false)
                }
*/
            }
        })
    }

}
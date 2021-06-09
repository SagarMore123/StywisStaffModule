package com.astrika.stywis_staff.view.stockmanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.PagerAdapter
import com.astrika.stywis_staff.adapters.stock.StockDishAdapter
import com.astrika.stywis_staff.databinding.FragmentStockBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockViewModel
import com.google.android.material.tabs.TabLayout


class StockFragment : Fragment(), StockDishAdapter.OnDishClickListener {

    lateinit var binding: FragmentStockBinding
    lateinit var viewModel: StockViewModel

    lateinit var pagerAdapter: PagerAdapter
    private var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            StockViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = PagerAdapter(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
/*
        if (!viewModel.sharedPreferences.getBoolean(Constants.IS_ORDER_BACK_PRESSED, false)) {

        }
*/
        setObserver()
        viewModel.getAllMenuCategories()

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
                for (menuCategory in it) {
                    pagerAdapter.addFragment(
                        StockPagerFragment(menuCategory.catalogueCategoryId ?: 0, this),
                        menuCategory.catalogueCategoryName ?: ""
                    )
                }
                if (it.size < 4)
                    binding.tabLayout.tabMode = TabLayout.MODE_FIXED
                /* else
                     binding.tabLayout.tabMode = TabLayout.MODE_FIXED*/

                pagerAdapter.notifyDataSetChanged()
            }
        })

        viewModel.onPreviewOrder.observeChange(requireActivity(), Observer {
            val dishList = ArrayList<DishDetailsDTO>()
            for (dish in viewModel.selectedOrderDishArrayList) {
                if (dish.quantity != 0) {
                    dishList.add(dish)
                }
            }

/*
            val abc = Utils.setData(dishList)
//            val bundle = bundleOf(Constants.ORDER_DISH_LIST to dishList)
            val intent = Intent(requireActivity(), OrderViewActivity::class.java)
            intent.putExtra(Constants.ORDER_DISH_LIST, abc)
            requireActivity().startActivityForResult(intent, 5)
//            binding.root.findNavController().navigate(R.id.action_MenuFragment_to_orderReviewFragment, bundle)
*/
        })
    }

    override fun onItemClick(menuDishDetails: DishDetailsDTO) {
//        Constants.showToastMessage(requireActivity(), "Dish Name : " +menuDishDetails.productName)

        val bottomSheetDialog = StockCustomizationBottomSheetDialog(menuDishDetails)
        bottomSheetDialog.show(childFragmentManager, "Stock Customization BottomSheet Dialog")

/*
        var dishAlreadyAdded = false
        viewModel.totalOrderItems = 0
        viewModel.totalOrderPrice = 0

        for (dishDetails in viewModel.selectedOrderDishArrayList) {
            if (dishDetails.productId == menuDishDetails.productId) {
                dishDetails.quantity = menuDishDetails.quantity
                dishAlreadyAdded = true
            }
            val dishPrice =
                if (dishDetails.productDiscountPrice > BigDecimal.ZERO) dishDetails.productDiscountPrice else dishDetails.productOriPrice
            viewModel.totalOrderItems += dishDetails.quantity ?: 0
            viewModel.totalOrderPrice = viewModel.totalOrderPrice.plus(
                dishPrice.times(
                    dishDetails.quantity?.toBigDecimal() ?: BigDecimal.ZERO
                ).toInt()
            )
        }
        if (!dishAlreadyAdded) {

            viewModel.selectedOrderDishArrayList.add(menuDishDetails)

            val dishPrice =
                if (menuDishDetails.productDiscountPrice > BigDecimal.ZERO) menuDishDetails.productDiscountPrice else menuDishDetails.productOriPrice
            viewModel.totalOrderItems += menuDishDetails.quantity ?: 0
            viewModel.totalOrderPrice = viewModel.totalOrderPrice.plus(dishPrice.toInt())
        }

        if (viewModel.totalOrderItems > 0)
            viewModel.quantityPriceMutable.value =
                "${viewModel.totalOrderItems} Items | ₹  ${viewModel.totalOrderPrice}"
        else
            viewModel.quantityPriceMutable.value = ""
*/

    }


}
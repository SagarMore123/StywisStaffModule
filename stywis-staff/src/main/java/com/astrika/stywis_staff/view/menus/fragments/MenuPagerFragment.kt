package com.astrika.stywis_staff.view.menus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.MenuDishAdapter
import com.astrika.stywis_staff.adapters.menu.MenuSectionAdapter
import com.astrika.stywis_staff.databinding.FragmentMenuPagerBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.menus.viewmodels.MenuPagerViewModel


class MenuPagerFragment(
    private val productCategoryId: Long,
    private val menuFragment: MenuFragment
//    private val listener: MenuDishAdapter.OnDishClickListener
) : Fragment(), MenuDishAdapter.OnDishClickListener,
    CustomizationBottomSheetDialog.UpdateProductInterface {

    lateinit var binding: FragmentMenuPagerBinding
    lateinit var viewModel: MenuPagerViewModel

    lateinit var menuSectionAdapter: MenuSectionAdapter
    private lateinit var bottomSheet: CustomizationBottomSheetDialog

    /*  companion object {

          lateinit var listener : MenuDishAdapter.OnDishClickListener
          var productCategoryId : Long = 0

          @JvmStatic
          fun newInstance(productCategoryId: Long, listener : MenuDishAdapter.OnDishClickListener) {
              this.productCategoryId = productCategoryId
              this.listener = listener

          }
      }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_pager, container, false)

        binding = DataBindingUtil.inflate<FragmentMenuPagerBinding>(
            inflater, R.layout.fragment_menu_pager,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuPagerViewModel::class.java,
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

        menuSectionAdapter = MenuSectionAdapter(requireContext(), this)
        binding.menuSectionRecycler.adapter = menuSectionAdapter
//        menuSectionAdapter.setMenuSectionList(arrayListOf(null,null,null))

//        viewModel.getAllDishesWithSections("")

        viewModel.menuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let {
//                dishCategoryAdapter.setDishCategoryList(it)
                menuSectionAdapter.setMenuSectionList(it)
            }
        })

        viewModel.searchString.observe(requireActivity(), Observer {
            it.let {
                viewModel.getAllDishesWithSections(it)
/*
                if (it.length > 2){
                    viewModel.getAllDishesWithSections(it)
                    viewModel.clearSearchVisible.set(true)
                }else if (it.isEmpty()){
                    viewModel.menuSectionMutableArrayList.value = viewModel.menuCategoryArrayList
                    viewModel.clearSearchVisible.set(false)
                }
*/
            }
        })


        viewModel.isConfirm.observe(requireActivity(), Observer {
            if (it) {
                // Write code here for payment done successfully
                menuFragment.viewModel.onPreviewOrderClick()
            }

        })

        viewModel.quantityPriceMutable.observe(viewLifecycleOwner, Observer {
            menuFragment.viewModel.displayOrderDetails(
                viewModel.quantityPriceMutable.value ?: "",
                viewModel.selectedOrderDishArrayList
            )
        })

    }


    var sectionPosition = -1
    override fun onItemClick(position: Int, sectionPosition: Int, menuDishDetails: DishDetailsDTO) {
//        Constants.showToastMessage(requireActivity(), "Dish Name : " +menuDishDetails.productName)
        this.sectionPosition = sectionPosition

        var isRemove = false
        for (item in viewModel.selectedOrderDishArrayList) {
            if (item.productId == menuDishDetails.productId && item.isCustomizable && (!item.optionValueIds.isNullOrEmpty() || !item.addOnValueIds.isNullOrEmpty())) {
//                if (item.productId == menuDishDetails.productId && menuDishDetails.isCustomizable && (!menuDishDetails.optionValueIds.isNullOrEmpty() || !menuDishDetails.addOnValueIds.isNullOrEmpty())) {
                isRemove = true
                break
//                }
            }

        }

        if (isRemove) {

            Constants.showCommonDialog(
                requireActivity(),
                viewModel.isConfirm,
                requireActivity().resources.getString(R.string.remove_customized_product)
            )

        } else {

            viewModel.displayCalculation(false, menuDishDetails)

            viewModel.menuSectionMutableArrayList.value?.get(sectionPosition)?.activeProductList?.set(
                position,
                menuDishDetails
            )
            viewModel.menuSectionMutableArrayList.value =
                viewModel.menuSectionMutableArrayList.value


            menuFragment.viewModel.displayOrderDetails(
                viewModel.quantityPriceMutable.value ?: "",
                viewModel.selectedOrderDishArrayList
            )
        }


    }


    override fun onItemAddClick(
        position: Int,
        sectionPosition: Int,
        menuDishDetails: DishDetailsDTO
    ) {
        this.sectionPosition = sectionPosition

        if (menuDishDetails.isCustomizable) {
            if (menuDishDetails.quantity ?: 0 > 0) {
                bottomSheet = CustomizationBottomSheetDialog(this, position, true, menuDishDetails)
            } else {
                bottomSheet = CustomizationBottomSheetDialog(this, position, false, menuDishDetails)
            }
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        } else {
            viewModel.displayCalculation(false, menuDishDetails)

            viewModel.menuSectionMutableArrayList.value?.get(sectionPosition)?.activeProductList?.set(
                position,
                menuDishDetails
            )
            viewModel.menuSectionMutableArrayList.value =
                viewModel.menuSectionMutableArrayList.value


            menuFragment.viewModel.displayOrderDetails(
                viewModel.quantityPriceMutable.value ?: "",
                viewModel.selectedOrderDishArrayList
            )

        }
    }

    override fun onItemCustomizeClick(
        position: Int,
        sectionPosition: Int,
        menuDishDetails: DishDetailsDTO
    ) {
/*
        this.sectionPosition = sectionPosition

        if (!viewModel.selectedOrderDishArrayList.isNullOrEmpty() && menuDishDetails.isCustomizable) {
            bottomSheet = CustomizationBottomSheetDialog(this, position, false, menuDishDetails)
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        } else {
            viewModel.getmSnackbar().value = "Please first add product to customize"
        }
*/

    }

    override fun updateProduct(
        position: Int,
        isRepeat: Boolean,
        isAddClick: Boolean,
        dto: DishDetailsDTO
    ) {


        viewModel.displayCalculation2(true, dto)


        val data1 =
            viewModel.menuSectionMutableArrayList.value?.get(sectionPosition)?.activeProductList?.get(
                position
            )

        data1?.quantity = data1?.quantity?.plus(1)
        data1?.let {
            viewModel.menuSectionMutableArrayList.value?.get(sectionPosition)?.activeProductList?.set(
                position,
                it
            )
        }
        viewModel.menuSectionMutableArrayList.value = viewModel.menuSectionMutableArrayList.value


//        viewModel.selectedOrderDishArrayListMutableLiveData.value = viewModel.selectedOrderDishArrayList
        menuFragment.viewModel.displayOrderDetails(
            viewModel.quantityPriceMutable.value ?: "",
            viewModel.selectedOrderDishArrayList
        )
        bottomSheet.dismiss()

    }

    override fun onResume() {
        super.onResume()
        viewModel.selectedOrderDishArrayList = Utils.getData(
            Constants.decrypt(
                viewModel.sharedPreferences.getString(
                    Constants.ORDER_DISH_LIST,
                    ""
                )
            )
        ) ?: arrayListOf()

        viewModel.getAllDishesWithSections("")
//        viewModel.updateProductList()

    }
}
package com.astrika.stywis_staff.view.menus.fragments

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
import com.astrika.stywis_staff.databinding.FragmentMenuStaffBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.fragments.WishListDialogFragment
import com.astrika.stywis_staff.view.menus.OrderViewActivity
import com.astrika.stywis_staff.view.menus.viewmodels.MenuViewModel
import com.google.android.material.tabs.TabLayout


class MenuFragment : Fragment() {

    lateinit var binding: FragmentMenuStaffBinding
    lateinit var viewModel: MenuViewModel

    lateinit var pagerAdapter: PagerAdapter
    private var progressBar = CustomProgressBar()
    var checkInUserId: Long? = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu, container, false)0.


//        binding = DataBindingUtil.findBinding<>()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu_staff,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        checkInUserId = arguments?.getLong(Constants.CHECK_IN_USER_ID) ?: 0L


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

//        setMenuPager()
    }

    /*fun setMenuPager(){
        menuPagerAdapter  = MenuPagerAdapter(childFragmentManager)

        menuPagerAdapter.addFragment(MenuPagerFragment(), "Food")
        menuPagerAdapter.addFragment(MenuPagerFragment(), "Bar Menu")
        menuPagerAdapter.addFragment(MenuPagerFragment(), "Breakfast Menu")
        menuPagerAdapter.addFragment(MenuPagerFragment(), "Breakfast Menu")
        menuPagerAdapter.addFragment(MenuPagerFragment(), "Breakfast Menu")

        binding.viewPager.adapter = menuPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }*/

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
                        MenuPagerFragment(menuCategory.catalogueCategoryId ?: 0, this),
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


        viewModel.onWishListClick.observeChange(requireActivity(), Observer {
            val wishListFragment = WishListDialogFragment(checkInUserId)
            wishListFragment.show(childFragmentManager, "WishList Dialog Fragment")
        })


        viewModel.onPreviewOrder.observeChange(requireActivity(), Observer {
            val dishList = ArrayList<DishDetailsDTO>()
            for (dish in viewModel.selectedOrderDishArrayList) {
                if (dish.quantity != 0) {
                    dishList.add(dish)
                }
            }

            val value = Utils.setData(dishList)
//            val bundle = bundleOf(Constants.ORDER_DISH_LIST to dishList)
            val intent = Intent(requireActivity(), OrderViewActivity::class.java)
            intent.putExtra(Constants.ORDER_DISH_LIST, value ?: "")
            this.startActivity(intent)
//            requireActivity().startActivityForResult(intent, 5)
//            binding.root.findNavController().navigate(R.id.action_MenuFragment_to_orderReviewFragment, bundle)
        })
    }

/*

    override fun onItemClick(position: Int, menuDishDetails: DishDetailsDTO) {
//        Constants.showToastMessage(requireActivity(), "Dish Name : " +menuDishDetails.productName)

        viewModel.displayCalculation(position, menuDishDetails)

    }


    override fun onItemAddClick(position: Int, menuDishDetails: DishDetailsDTO) {

//        viewModel.displayCalculation(position, menuDishDetails)

        if (menuDishDetails.isCustomizable) {
            bottomSheet = CustomizationBottomSheetDialog(this, position, true, menuDishDetails)
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        } else {
            viewModel.displayCalculation(position, menuDishDetails)
        }
    }

    override fun onItemCustomizeClick(position: Int, menuDishDetails: DishDetailsDTO) {

        if (!viewModel.selectedOrderDishArrayList.isNullOrEmpty() && menuDishDetails.isCustomizable) {
            bottomSheet = CustomizationBottomSheetDialog(this, position, false, menuDishDetails)
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        } else {
            viewModel.getmSnackbar().value = "Please first add product to customize"
        }

    }

    override fun updateProduct(
        position: Int,
        isRepeat: Boolean,
        isAddClick: Boolean,
        dto: DishDetailsDTO
    ) {

        viewModel.displayCalculation(position, dto)

        if (isAddClick) {

            if (isRepeat) {
                viewModel.selectedOrderDishArrayList[position].quantity =
                    viewModel.selectedOrderDishArrayList[position].quantity?.plus(1)
                viewModel.selectedOrderDishArrayList[position].isCustomizable = dto.isCustomizable
            } else {

                var isAdd = false
                if (viewModel.selectedOrderDishArrayList[position].optionValueIds.isNullOrEmpty()
                    && viewModel.selectedOrderDishArrayList[position].addOnValueIds.isNullOrEmpty()
                ) {

                    viewModel.selectedOrderDishArrayList[position] = dto

                } else if (!viewModel.selectedOrderDishArrayList[position].optionValueIds.isNullOrEmpty() && viewModel.selectedOrderDishArrayList[position].addOnValueIds.isNullOrEmpty()) {

                    var isSameOptionPresent = true
                    loop@ for (optionValueDTO in dto.optionValueIds ?: arrayListOf()) {

                        for (optListDTO in viewModel.selectedOrderDishArrayList[position].optionValueIds
                            ?: arrayListOf()) {
                            if (optListDTO.id != optionValueDTO.id) {
                                isSameOptionPresent = false
                                break@loop
                            }
                        }
                    }

                    if (isSameOptionPresent) {
                        dto.quantity =
                            viewModel.selectedOrderDishArrayList[position].quantity?.plus(1)
                        viewModel.selectedOrderDishArrayList[position] = dto
                    } else {
                        isAdd = true
                    }

                } else if (viewModel.selectedOrderDishArrayList[position].optionValueIds.isNullOrEmpty() && !viewModel.selectedOrderDishArrayList[position].addOnValueIds.isNullOrEmpty()) {

                    var isSameAddonsPresent = true

                    loop@ for (addonValueDTO in dto.addOnValueIds ?: arrayListOf()) {

                        for (addonListDTO in viewModel.selectedOrderDishArrayList[position].addOnValueIds
                            ?: arrayListOf()) {
                            if (addonListDTO.id != addonValueDTO.id) {
                                isSameAddonsPresent = false
                                break@loop
                            }
                        }
                    }

                    if (isSameAddonsPresent) {
                        dto.quantity =
                            viewModel.selectedOrderDishArrayList[position].quantity?.plus(1)
                        viewModel.selectedOrderDishArrayList[position] = dto
                    } else {
                        isAdd = true
                    }

                } else if (!viewModel.selectedOrderDishArrayList[position].optionValueIds.isNullOrEmpty() && !viewModel.selectedOrderDishArrayList[position].addOnValueIds.isNullOrEmpty()) {

                    // Options
                    var isSameOptionPresent = true
                    loop@ for (optionValueDTO in dto.optionValueIds ?: arrayListOf()) {

                        for (optListDTO in viewModel.selectedOrderDishArrayList[position].optionValueIds
                            ?: arrayListOf()) {
                            if (optListDTO.id != optionValueDTO.id) {
                                isSameOptionPresent = false
                                break@loop
                            }
                        }
                    }

                    if (isSameOptionPresent) {

                        // Addons
                        var isSameAddonsPresent = true
                        loop@ for (addonValueDTO in dto.addOnValueIds ?: arrayListOf()) {

                            for (addonListDTO in viewModel.selectedOrderDishArrayList[position].addOnValueIds
                                ?: arrayListOf()) {
                                if (addonListDTO.id != addonValueDTO.id) {
                                    isSameAddonsPresent = false
                                    break@loop
                                }
                            }
                        }

                        if (isSameAddonsPresent) {
                            dto.quantity =
                                viewModel.selectedOrderDishArrayList[position].quantity?.plus(1)
                            viewModel.selectedOrderDishArrayList[position] = dto
                        } else {
                            isAdd = true
                        }

                    } else {
                        isAdd = true
                    }

                }

                if (isAdd) {
                    var isAddAgain = false
                    for ((i, item) in viewModel.selectedOrderDishArrayList.withIndex()) {

                        if (item.productId == dto.productId) {

                            isAddAgain = false

//                        if (i != position) {

                            if (item.optionValueIds.isNullOrEmpty()
                                && item.addOnValueIds.isNullOrEmpty()
                            ) {

                                viewModel.selectedOrderDishArrayList[i] = dto

                            } else if (!item.optionValueIds.isNullOrEmpty() && item.addOnValueIds.isNullOrEmpty()) {

                                var isSameOptionPresent = true
                                for (optionValueDTO in dto.optionValueIds ?: arrayListOf()) {

                                    loop@ for (optListDTO in item.optionValueIds ?: arrayListOf()) {
                                        if (optListDTO.id != optionValueDTO.id) {
                                            isSameOptionPresent = false
                                            break@loop
                                        }
                                    }
                                }

                                if (isSameOptionPresent) {
                                    dto.quantity = item.quantity?.plus(1)
                                    viewModel.selectedOrderDishArrayList[i] = dto
                                } else {
                                    isAddAgain = true
                                }

                            } else if (item.optionValueIds.isNullOrEmpty() && !item.addOnValueIds.isNullOrEmpty()) {

                                var isSameAddonsPresent = true

                                for (addonValueDTO in dto.addOnValueIds ?: arrayListOf()) {

                                    loop@ for (addonListDTO in item.addOnValueIds
                                        ?: arrayListOf()) {
                                        if (addonListDTO.id != addonValueDTO.id) {
                                            isSameAddonsPresent = false
                                            break@loop
                                        }
                                    }
                                }

                                if (isSameAddonsPresent) {
                                    dto.quantity = item.quantity?.plus(1)
                                    viewModel.selectedOrderDishArrayList[i] = dto
                                } else {
                                    isAddAgain = true
                                }

                            } else if (!item.optionValueIds.isNullOrEmpty() && !item.addOnValueIds.isNullOrEmpty()) {

                                // Options
                                var isSameOptionPresent = true
                                for (optionValueDTO in dto.optionValueIds ?: arrayListOf()) {

                                    loop@ for (optListDTO in item.optionValueIds ?: arrayListOf()) {
                                        if (optListDTO.id != optionValueDTO.id) {
                                            isSameOptionPresent = false
                                            break@loop
                                        }
                                    }
                                }

                                if (isSameOptionPresent) {

                                    // Addons
                                    var isSameAddonsPresent = true
                                    for (addonValueDTO in dto.addOnValueIds
                                        ?: arrayListOf()) {

                                        loop@ for (addonListDTO in item.addOnValueIds
                                            ?: arrayListOf()) {
                                            if (addonListDTO.id != addonValueDTO.id) {
                                                isSameAddonsPresent = false
                                                break@loop
                                            }
                                        }
                                    }

                                    if (isSameAddonsPresent) {
                                        dto.quantity = item.quantity?.plus(1)
                                        viewModel.selectedOrderDishArrayList[i] = dto
                                    } else {
                                        isAddAgain = true
                                    }

                                } else {
                                    isAddAgain = true
                                }


                            }

//                            break

//                        }

                        }
                    }

                    if (isAddAgain) {
                        dto.quantity = 1
                        viewModel.selectedOrderDishArrayList.add(dto)
                    }
                }

            }


        } else {
            viewModel.selectedOrderDishArrayList[position] = dto
        }

        viewModel.selectedOrderDishArrayListMutableLiveData.value =
            viewModel.selectedOrderDishArrayList
        viewModel.showOrderDetails()
        bottomSheet.dismiss()

    }

*/

}
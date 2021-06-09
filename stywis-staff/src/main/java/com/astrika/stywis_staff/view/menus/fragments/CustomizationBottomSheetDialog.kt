package com.astrika.stywis_staff.view.menus.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.customization.CustomizationAddOnsGroupAdapter
import com.astrika.stywis_staff.adapters.customization.CustomizationAddOnsRowAdapter
import com.astrika.stywis_staff.adapters.customization.CustomizationOptionsRowAdapter
import com.astrika.stywis_staff.databinding.CustomizationBottomSheetDialogBinding
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.menus.viewmodels.CustomizationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigDecimal


class CustomizationBottomSheetDialog(
    private val updateProductInterface: UpdateProductInterface,
    private val position: Int,
    private val isAddClick: Boolean,
    private val menuDishDetails: DishDetailsDTO
) :
    BottomSheetDialogFragment(),
    CustomizationAddOnsGroupAdapter.OnItemClickListener,
    CustomizationAddOnsRowAdapter.OnGroupItemClickListener,
    CustomizationOptionsRowAdapter.OnItemClickListener {

    private lateinit var binding: CustomizationBottomSheetDialogBinding
    private lateinit var viewModel: CustomizationViewModel
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()
    lateinit var addOnsGroupAdapter: CustomizationAddOnsGroupAdapter
    lateinit var optionsRowAdapter: CustomizationOptionsRowAdapter
    var customizationOptionsList = ArrayList<CustomizationOptionDTO>()
    var customizationAddOnsList = ArrayList<CustomizationAddOnDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if (container != null) {
            mContext = container.context
        }
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.customization_bottom_sheet_dialog,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            CustomizationViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel
        binding.menuDishDetails = menuDishDetails
        binding.lifecycleOwner = this
        viewModel.isChooseAddonsVisibilityLayout.value = isAddClick

        viewModel.menuDishDetails.productId = menuDishDetails.productId
        viewModel.menuDishDetails.productName = menuDishDetails.productName
        viewModel.menuDishDetails.productDesc = menuDishDetails.productDesc
        viewModel.menuDishDetails.productOriPrice =
            menuDishDetails.productOriPrice ?: BigDecimal.ZERO
        viewModel.menuDishDetails.productDiscountPrice =
            menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO
        viewModel.menuDishDetails.itemId = menuDishDetails.itemId
        viewModel.menuDishDetails.productImage = menuDishDetails.productImage
        viewModel.menuDishDetails.availableAllTime = menuDishDetails.availableAllTime
        viewModel.menuDishDetails.productSequenceNo = menuDishDetails.productSequenceNo
        viewModel.menuDishDetails.maxOrderQty = menuDishDetails.maxOrderQty
        viewModel.menuDishDetails.minOrderQty = menuDishDetails.minOrderQty
        viewModel.menuDishDetails.outletId = menuDishDetails.outletId
        viewModel.menuDishDetails.specialNotes = menuDishDetails.specialNotes
        viewModel.menuDishDetails.cuisineIds = menuDishDetails.cuisineIds
        viewModel.menuDishDetails.productFlags = menuDishDetails.productFlags
        viewModel.menuDishDetails.productFlagDTOs = menuDishDetails.productFlagDTOs
        viewModel.menuDishDetails.catalogueCategoryDTO = menuDishDetails.catalogueCategoryDTO
        viewModel.menuDishDetails.catalogueCategoryId = menuDishDetails.catalogueCategoryId
        viewModel.menuDishDetails.menuSectionDTO = menuDishDetails.menuSectionDTO
        viewModel.menuDishDetails.catalogueSectionId = menuDishDetails.catalogueSectionId
        viewModel.menuDishDetails.active = menuDishDetails.active
        viewModel.menuDishDetails.isOutOfStock = menuDishDetails.isOutOfStock
        viewModel.menuDishDetails.productTimingDTO = menuDishDetails.productTimingDTO
        viewModel.menuDishDetails.quantity = menuDishDetails.quantity
        viewModel.menuDishDetails.taxValue = menuDishDetails.taxValue
        viewModel.menuDishDetails.isCustomizable = menuDishDetails.isCustomizable
        viewModel.menuDishDetails.optionValueIds = menuDishDetails.optionValueIds
        viewModel.menuDishDetails.addOnValueIds = menuDishDetails.addOnValueIds

        viewModel.displayDishPrice.value =
            if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice?.toLong() ?: 0 > 0) menuDishDetails.productDiscountPrice?.toLong()
                ?: 0 else menuDishDetails.productOriPrice?.toLong() ?: 0

        viewModel.productId = menuDishDetails.productId ?: 0
        initAdapter()
        observers()
        viewModel.getCustomizationListing()

        return binding.root

    }

    private fun initAdapter() {
//        addOnsRowAdapter = CustomizationAddOnsRowAdapter(this, this, this, this,false)
        optionsRowAdapter = CustomizationOptionsRowAdapter(requireActivity(), this)
        binding.optionsRecyclerView.adapter = optionsRowAdapter

        addOnsGroupAdapter =
            CustomizationAddOnsGroupAdapter(requireActivity(), this, this, viewModel.productId)
        binding.addonsRecyclerView.adapter = addOnsGroupAdapter

    }

    private fun observers() {
        viewModel.customizationOptionsLiveList.observe(this, Observer {
            optionsRowAdapter.optionsArrayList = it
        })

        viewModel.customizationAddOnsLiveList.observe(this, Observer {
            addOnsGroupAdapter.arrayList = it
        })

        viewModel.repeatEvent.observeChange(this, Observer {

            updateProductInterface.updateProduct(
                position,
                true,
                isAddClick,
                viewModel.menuDishDetails
            )
        })


        viewModel.saveEvent.observeChange(this, Observer {
            updateProductInterface.updateProduct(
                position,
                false,
                isAddClick,
                viewModel.menuDishDetails
            )
        })

    }

    override fun onDeleteGroup(
        position: Int,
        customizationAddOnDTOGroup: CustomizationAddOnDTOGroup?
    ) {
        binding.addonsRecyclerView.requestFocus()
        addOnsGroupAdapter.removeItem(position, customizationAddOnDTOGroup)
    }

    //addons group child
    override fun onRemoveItem(
        groupPosition: Int,
        childPosition: Int,
        customizationAddOnDTOGroupRow: CustomizationAddOnDTOGroupRow
    ) {

    }

    //removing the row item
    override fun onRemoveItem(
        position: Int,
        customizationOptionDTOGroupRow: CustomizationOptionDTOGroupRow
    ) {
    }

    // Options Click
    override fun onItemClick(position: Int, dto: CustomizationOptionDTOGroupRow) {
        viewModel.showTotalOrderDetails(true, position, dto, null)
    }

    // Add-Ons Click
    override fun onItemClick(position: Int, dto: CustomizationAddOnDTOGroupRow) {
        viewModel.showTotalOrderDetails(false, position, null, dto)
    }

    interface UpdateProductInterface {
        fun updateProduct(
            position: Int,
            isRepeat: Boolean,
            isAddClick: Boolean,
            dto: DishDetailsDTO
        )
    }


}
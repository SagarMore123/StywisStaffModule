package com.astrika.stywis_staff.view.stockmanagement.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.stock.StockCustomizationAdapter
import com.astrika.stywis_staff.databinding.StockCustomizationBottomSheetDialogBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.utils.AutocompleteViewActivity
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockCustomizationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StockCustomizationBottomSheetDialog(
    private val menuDishDetails: DishDetailsDTO
) :
    BottomSheetDialogFragment(),
    StockCustomizationAdapter.OnItemClickListener {

    private lateinit var binding: StockCustomizationBottomSheetDialogBinding
    private lateinit var viewModel: StockCustomizationViewModel
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()
    lateinit var stockCustomizationAdapter: StockCustomizationAdapter

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
            R.layout.stock_customization_bottom_sheet_dialog,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            StockCustomizationViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel
        binding.menuDishDetails = menuDishDetails
        binding.lifecycleOwner = this
//        viewModel.isChooseAddonsVisibilityLayout.value = isAddClick

        viewModel.menuDishDetails.productId = menuDishDetails.productId
        viewModel.menuDishDetails.productName = menuDishDetails.productName
        viewModel.menuDishDetails.productDesc = menuDishDetails.productDesc
        viewModel.menuDishDetails.productOriPrice = menuDishDetails.productOriPrice
        viewModel.menuDishDetails.productDiscountPrice = menuDishDetails.productDiscountPrice
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
        viewModel.isCustomizable.value = menuDishDetails.isCustomizable

//        viewModel.menuDishDetails.optionValueIds = menuDishDetails.optionValueIds
//        viewModel.menuDishDetails.addOnValueIds = menuDishDetails.addOnValueIds

        viewModel.displayDishPrice.value =
            if (menuDishDetails.productDiscountPrice?.toLong() ?: 0 > 0) menuDishDetails.productDiscountPrice?.toLong()
                ?: 0 else menuDishDetails.productOriPrice?.toLong() ?: 0

        viewModel.productId = menuDishDetails.productId ?: 0
        initAdapter()
        observers()
        viewModel.getStockCustomizationListing()

        return binding.root

    }

    private fun initAdapter() {

        stockCustomizationAdapter = StockCustomizationAdapter(requireActivity(), this)
        binding.optionsRecyclerView.adapter = stockCustomizationAdapter

    }

    private fun observers() {

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.stockCustomizationOptionQuantity.observe(viewLifecycleOwner, Observer {
            viewModel.quantityError.value = ""
        })

        viewModel.stockCustomizationOptionsMutableLiveList.observe(this, Observer {
            stockCustomizationAdapter.arrayList = it
        })


        viewModel.onStockCustomizationOptionClickEvent.observeChange(this, Observer {

            val requestCode = Constants.STOCK_CUSTOMIZATION_OPTIONS_CODE
            val intent = Intent(requireActivity(), AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(Constants.STOCKS_PRODUCT_ID, viewModel.productId)
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.dismissEvent.observeChange(viewLifecycleOwner, Observer {
            dismiss()
        })

    }

    override fun onRemoveItemClick(position: Int, dto: StocksCustomizationDTO) {
//        stockCustomizationAdapter.removeItem(position)
        viewModel.removeItem(position, dto)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        // BEGIN_INCLUDE (parse_open_document_response)
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.
        Utils.hideKeyboard(requireActivity())

        if (resultCode == Constants.STOCK_CUSTOMIZATION_OPTIONS_CODE) {
            val stockCustomizationDTO =
                resultData?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as StockCustomizationMasterDTO
            if (stockCustomizationDTO != null) {
                viewModel.stockCustomizationOptionId = stockCustomizationDTO.customizeOptionId ?: 0
                viewModel.stockCustomizationOption.value =
                    stockCustomizationDTO.customizeOptionName ?: ""

                viewModel.stockCustomizationOptionError.value = ""
            }
        }


    }


}
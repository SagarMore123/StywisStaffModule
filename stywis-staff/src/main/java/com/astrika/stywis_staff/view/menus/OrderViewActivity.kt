package com.astrika.stywis_staff.view.menus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.OrderReviewAdapter
import com.astrika.stywis_staff.databinding.ActivityOrderViewBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.DashboardActivity
import com.astrika.stywis_staff.view.menus.fragments.CustomizationBottomSheetDialog
import com.astrika.stywis_staff.view.menus.viewmodels.OrderReviewViewModel

class OrderViewActivity : AppCompatActivity(), OrderReviewAdapter.OnDishClickListener,
    CustomizationBottomSheetDialog.UpdateProductInterface {

    private lateinit var binding: ActivityOrderViewBinding
    lateinit var viewModel: OrderReviewViewModel
    lateinit var orderReviewAdapter: OrderReviewAdapter
    private var progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_view)

        viewModel = Utils.obtainBaseObservable(
            this,
            OrderReviewViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setObserver()
        setAdapter()

        val bundle = intent?.extras
        if (bundle != null) {
            if (bundle.containsKey(Constants.ORDER_DISH_LIST) && !bundle.getString(Constants.ORDER_DISH_LIST)
                    .isNullOrBlank()
            ) {
                val value = Utils.getData(bundle.getString(Constants.ORDER_DISH_LIST) ?: "")

                viewModel.selectedOrderDishArrayList = value as ArrayList<DishDetailsDTO>
//                viewModel.selectedOrderDishArrayListMutableLiveData.value = viewModel.selectedOrderDishArrayList
            }
        } else {
            viewModel.populateOrderDetails()
        }

        viewModel.showOrderDetails()
    }

    private fun setAdapter() {
        orderReviewAdapter = OrderReviewAdapter(this, this, viewModel.isViewOrder.value ?: false)
        binding.orderReviewRecycler.adapter = orderReviewAdapter
    }

    private fun setObserver() {

        viewModel.showProgressBar.observe(this, Observer {
            if (it)
                progressBar.show(this, "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.isSaveOrderSuccess.observe(this, Observer {
            if (it) {
                viewModel.sharedPreferences.edit()
                    .putString(Constants.IS_SAVE_ORDER_SUCCESS, Constants.encrypt(true.toString()))
                    .apply()
                Constants.changeActivityAndFinish<DashboardActivity>(this)
            }

        })


        binding.discountLayout.setOnClickListener {
            val intent = Intent(this, ApplicableDiscountsActivity::class.java)
            intent.putExtra(Constants.IS_VIEW_DISCOUNT, true)
            startActivity(intent)
        }

        viewModel.onConfirmOrderClick.observeChange(this, Observer {
            val dishList = ArrayList<DishDetailsDTO>()
            for (dish in viewModel.selectedOrderDishArrayList) {
                if (dish.quantity != 0) {
                    dishList.add(dish)
                }
            }
        })

        viewModel.selectedOrderDishArrayListMutableLiveData.observe(this, Observer {
            orderReviewAdapter.menuDishList = it
        })
    }

    override fun onItemClick(position: Int, menuDishDetails: DishDetailsDTO) {

        if (menuDishDetails.quantity == 0) {
            viewModel.selectedOrderDishArrayList.indexOf(menuDishDetails).let {
                viewModel.selectedOrderDishArrayList.removeAt(it)
            }
        } else {
            viewModel.selectedOrderDishArrayList.indexOf(menuDishDetails).let {
                viewModel.selectedOrderDishArrayList.get(it).quantity = menuDishDetails.quantity
            }
        }
        viewModel.showOrderDetails()


    }

    override fun onItemAddClick(position: Int, menuDishDetails: DishDetailsDTO) {
        bottomSheet = CustomizationBottomSheetDialog(this, position, true, menuDishDetails)
        bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
    }

    private lateinit var bottomSheet: CustomizationBottomSheetDialog

    override fun onItemCustomizeClick(position: Int, menuDishDetails: DishDetailsDTO) {
        bottomSheet = CustomizationBottomSheetDialog(this, position, false, menuDishDetails)
        bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
    }

    override fun updateProduct(
        position: Int,
        isRepeat: Boolean,
        isAddClick: Boolean,
        dto: DishDetailsDTO
    ) {

        if (isAddClick) {

            if (isRepeat) {
                viewModel.selectedOrderDishArrayList[position].quantity =
                    viewModel.selectedOrderDishArrayList[position].quantity?.plus(1)
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

//        viewModel.selectedOrderDishArrayListMutableLiveData.value = viewModel.selectedOrderDishArrayList
        viewModel.showOrderDetails()
        bottomSheet.dismiss()

    }

}
package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.OrderReviewCellLayoutBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import java.math.BigDecimal
import java.math.RoundingMode


class OrderReviewAdapter(
    private val context: Context,
    private val listener: OnDishClickListener,
    private val isView: Boolean
) : RecyclerView.Adapter<OrderReviewAdapter.OrderReviewViewHolder>() {

    var menuDishList = ArrayList<DishDetailsDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setOrderList(menuDishList: ArrayList<DishDetailsDTO>) {
//        this.menuDishList = menuDishList
//        notifyDataSetChanged()
    }

    class OrderReviewViewHolder(private val binding: OrderReviewCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            isView: Boolean,
            menuDishDetails: DishDetailsDTO,
            listener: OnDishClickListener
        ) {

            binding.isView = isView
            binding.dishDetails = menuDishDetails
            var orderCount = menuDishDetails.quantity ?: 0
            var dishPrice: BigDecimal? = BigDecimal(0)

            dishPrice =
                if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) menuDishDetails.productDiscountPrice else menuDishDetails.productOriPrice
                    ?: BigDecimal.ZERO


            if (menuDishDetails.isCustomizable) { // Customizable


                var itemsNames = ""
                // Options
                for ((i, optionsDTO) in menuDishDetails.optionValueIds?.withIndex()
                    ?: arrayListOf()) {
                    itemsNames = if (itemsNames.isBlank()) {
                        optionsDTO.value ?: ""
                    } else {
                        itemsNames + ", " + optionsDTO.value
                    }

                    if (!isView) {
                        dishPrice = optionsDTO.optionPrice?.toBigDecimal() ?: BigDecimal.ZERO
                    }

                    break
                }

                // Addons
                for ((i, addValueDTO) in menuDishDetails.addOnValueIds?.withIndex()
                    ?: arrayListOf()) {
                    itemsNames = if (itemsNames.isBlank()) {
                        addValueDTO.value ?: ""
                    } else {
                        itemsNames + ", " + addValueDTO.value
                    }

                    if (!isView) {
                        dishPrice = dishPrice?.plus(
                            addValueDTO.addOnPrice?.toBigDecimal() ?: BigDecimal.ZERO
                        ) ?: BigDecimal.ZERO
                    }

                }

                if (itemsNames.isNullOrBlank()) {
                    binding.customizedTxt.visibility = View.GONE
                } else {
                    binding.customizedTxt.visibility = View.VISIBLE
                    binding.customizedTxt.text = itemsNames
                }

            } else {
                binding.customizedTxt.visibility = View.GONE
                binding.customizeDropDownTxt.visibility = View.GONE
            }

/*
            if (!isView && menuDishDetails.isCustomizable) {
                binding.customizeDropDownTxt.visibility = View.VISIBLE
                dishPrice = menuDishDetails.productCustomizedPrice ?: BigDecimal.ZERO

            } else {
                binding.customizeDropDownTxt.visibility = View.GONE
            }
*/


            var totalDishPrice = BigDecimal(0)
            if (isView) {
                binding.customizeDropDownTxt.visibility = View.GONE
                totalDishPrice = dishPrice ?: BigDecimal.ZERO
            } else {

                /*val taxPrice: BigDecimal? = dishPrice?.times(
                    menuDishDetails.taxValue?.toBigDecimal()
                        ?.divide(BigDecimal(100).setScale(2, RoundingMode.HALF_UP))
                        ?: BigDecimal.ZERO
                )

                dishPrice = dishPrice?.add(taxPrice) ?: BigDecimal(0)*/

                totalDishPrice = dishPrice?.times(orderCount.toBigDecimal()) ?: BigDecimal.ZERO

//                dishPrice = totalDishPrice
            }
            binding.totalPriceTextView.text =
                "₹ ${totalDishPrice.setScale(2, RoundingMode.HALF_UP)}"


            binding.addImageView.setOnClickListener {
/*
                orderCount++
                binding.quantityTextView.text = orderCount.toString()
                totalDishPrice += dishPrice
                binding.totalPriceTextView.text = "₹ $totalDishPrice"
                menuDishDetails?.quantity = orderCount
*/
                if (menuDishDetails.isCustomizable) {
                    listener.onItemAddClick(position, menuDishDetails)
                } else {
                    orderCount++
                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice += dishPrice ?: BigDecimal.ZERO
                    binding.totalPriceTextView.text =
                        "₹ ${totalDishPrice.setScale(2, RoundingMode.HALF_UP)}"
                    menuDishDetails.quantity = orderCount
                    listener.onItemClick(position, menuDishDetails)
                }

            }

            binding.removeImageView.setOnClickListener {
                if (orderCount >= 1) {
                    orderCount--
                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice -= dishPrice ?: BigDecimal.ZERO
                    binding.totalPriceTextView.text =
                        "₹ ${totalDishPrice.setScale(2, RoundingMode.HALF_UP)}"
                    menuDishDetails.quantity = orderCount

/*
                    dishPrice = dishPrice.plus(
                        dishPrice.times(
                            menuDishDetails.taxValue?.toBigDecimal()
                                ?.divide(BigDecimal(100).setScale(2, RoundingMode.HALF_UP))
                                ?: BigDecimal.ZERO
                        )
                    )

                    if (!isView && menuDishDetails.isCustomizable) {
                        dishPrice = menuDishDetails.productCustomizedPrice ?: BigDecimal.ZERO
                    }else{
                        dishPrice =
                            if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) menuDishDetails.productDiscountPrice else menuDishDetails.productOriPrice?: BigDecimal.ZERO
                    }

                    dishPrice = (dishPrice?.plus(
                        dishPrice?.times(
                            menuDishDetails.taxValue?.toBigDecimal()
                                ?.divide(BigDecimal(100).setScale(2, RoundingMode.HALF_UP))
                                ?: BigDecimal.ZERO
                        ) ?: BigDecimal.ZERO
                    ))

                    totalDishPrice -= dishPrice ?: BigDecimal.ZERO

                    binding.totalPriceTextView.text =
                        "₹ ${totalDishPrice.setScale(2, RoundingMode.HALF_UP)}"
*/
                } else {
//                    binding.addButton.visibility = View.VISIBLE
//                    binding.quantityButton.visibility = View.GONE
                    binding.quantityLayout.visibility = View.GONE
                    binding.totalPriceTextView.text = "₹ 0"
                    orderCount = 0
                }

                menuDishDetails.quantity = orderCount
                listener.onItemClick(position, menuDishDetails)
            }


            binding.customizeDropDownTxt.setOnClickListener {
                listener.onItemCustomizeClick(adapterPosition, menuDishDetails)
            }
        }


        companion object {

            fun from(parent: ViewGroup): OrderReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    OrderReviewCellLayoutBinding.inflate(layoutInflater, parent, false)
                return OrderReviewViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReviewViewHolder {
        return OrderReviewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderMenu: OrderReviewViewHolder, position: Int) {

        val menuDishItem = menuDishList[position]
        holderMenu.bind(context, position, isView, menuDishItem, listener)

    }


    interface OnDishClickListener {
        fun onItemClick(
            position: Int,
            menuDishDetails: DishDetailsDTO
        )

        fun onItemAddClick(
            position: Int,
            menuDishDetails: DishDetailsDTO
        )

        fun onItemCustomizeClick(
            position: Int,
            menuDishDetails: DishDetailsDTO
        )
    }


    override fun getItemCount(): Int {
        return menuDishList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}
package com.astrika.stywis_staff.adapters.stock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.StockProductCellLayoutBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import java.math.BigDecimal


class StockDishAdapter(
    private val context: Context,
    private val listener: OnDishClickListener
) : RecyclerView.Adapter<StockDishAdapter.StockProductViewHolder>() {

    private var menuDishList = ArrayList<DishDetailsDTO>()
    private var taxValue: Double = 0.0
    private var sectionPosition = 0

    fun setMenuDishList(
        sectionPosition: Int,
        menuDishList: ArrayList<DishDetailsDTO>,
        taxValue: Double
    ) {
        this.sectionPosition = sectionPosition
        this.menuDishList = menuDishList
        this.taxValue = taxValue
        notifyDataSetChanged()
    }

    class StockProductViewHolder(private val binding: StockProductCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            sectionPosition: Int,
            menuDishDetails: DishDetailsDTO,
            listener: OnDishClickListener
        ) {

            binding.dishDetails = menuDishDetails
            binding.bigDecimal = BigDecimal(0)

            if (menuDishDetails.isCustomizable) {
                binding.customizeDropDownTxt.visibility = View.VISIBLE
            } else {
                binding.customizeDropDownTxt.visibility = View.GONE
            }

            if (menuDishDetails.productDiscountPrice == null || menuDishDetails.productDiscountPrice?.compareTo(
                    BigDecimal.ZERO
                ) == 0
            ) {
                binding.originalPriceTextView.visibility = View.GONE
            } else {
                binding.originalPriceTextView.visibility = View.VISIBLE
            }

            binding.productImg.clipToOutline = true

            val orderCount =
                if (menuDishDetails.quantity == null) 0 else menuDishDetails.quantity ?: 0
            val dishPrice =
                if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) menuDishDetails.productDiscountPrice
                    ?: BigDecimal.ZERO else menuDishDetails.productOriPrice ?: BigDecimal.ZERO
            var totalDishPrice = dishPrice?.times(orderCount.toBigDecimal())

            binding.manageStockBtn.setOnClickListener {
/*
                orderCount++
                binding.quantityTextView.text = orderCount.toString()
                totalDishPrice += dishPrice
                binding.totalPriceTextView.text = "₹ $totalDishPrice"

                binding.addButton.visibility = View.GONE
                binding.quantityButton.visibility = View.VISIBLE
                binding.quantityTextView.visibility = View.VISIBLE
                binding.totalPriceTextView.visibility = View.VISIBLE

                menuDishDetails.quantity = orderCount
                menuDishDetails.taxValue = taxValue
*/
                listener.onItemClick(menuDishDetails)
            }

/*
            binding.removeImageView.setOnClickListener {
                orderCount--
                if (orderCount == 0) {
                    binding.addButton.visibility = View.VISIBLE
                    binding.quantityButton.visibility = View.GONE
                    binding.quantityTextView.visibility = View.GONE
                    binding.totalPriceTextView.visibility = View.GONE
                }

                binding.quantityTextView.text = orderCount.toString()
                totalDishPrice -= dishPrice
                binding.totalPriceTextView.text = "₹ $totalDishPrice"

*/
/*
                if (orderCount >= 1) {
                    orderCount--
                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice -= dishPrice
                    binding.totalPriceTextView.text = "₹ $totalDishPrice"

                } else {

                    binding.addButton.visibility = View.VISIBLE
                    binding.quantityButton.visibility = View.GONE
                    binding.quantityTextView.visibility = View.GONE
                    binding.totalPriceTextView.visibility = View.GONE
                    orderCount = 0
                }
*//*

                menuDishDetails.quantity = orderCount
                listener.onItemClick(menuDishDetails)
            }
            binding.addImageView.setOnClickListener {
                orderCount++
                binding.quantityTextView.text = orderCount.toString()
                totalDishPrice += dishPrice
                binding.totalPriceTextView.text = "₹ $totalDishPrice"
                menuDishDetails.quantity = orderCount
                listener.onItemClick(menuDishDetails)
            }
*/

        }


        companion object {

            fun from(parent: ViewGroup): StockProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    StockProductCellLayoutBinding.inflate(layoutInflater, parent, false)
                return StockProductViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockProductViewHolder {
        return StockProductViewHolder.from(parent)
    }

    override fun getItemCount(): Int = menuDishList.size

    override fun onBindViewHolder(holderMenu: StockProductViewHolder, position: Int) {

        val menuDishItem = menuDishList[position]
        menuDishItem.taxValue = taxValue
        holderMenu.bind(context, position, sectionPosition, menuDishItem, listener)

    }


    interface OnDishClickListener {
        fun onItemClick(
            menuDishDetails: DishDetailsDTO
        )
    }

}
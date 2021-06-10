package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.DishCellLayoutStaffBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import java.math.BigDecimal

class MenuDishAdapter(
    private val context: Context,
    private val listener: OnDishClickListener
) : RecyclerView.Adapter<MenuDishAdapter.MenuDishViewHolder>() {

    private var sectionPosition = 0
    private var menuDishList = ArrayList<DishDetailsDTO>()
    private var taxValue: Double = 0.0

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuDishViewHolder {
        return MenuDishViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderMenu: MenuDishViewHolder, position: Int) {

        val menuDishItem = menuDishList[position]
        menuDishItem.taxValue = taxValue
        holderMenu.bind(context, position, sectionPosition, menuDishItem, listener)

    }


    class MenuDishViewHolder(private val binding: DishCellLayoutStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            sectionPosition: Int,
            menuDishDetails: DishDetailsDTO,
            listener: OnDishClickListener
        ) {

//            binding.dishMenuSection = menuSection

            binding.bigDecimal = BigDecimal(0)
            binding.dishDetails = menuDishDetails

            if (menuDishDetails.productDiscountPrice == null || menuDishDetails.productDiscountPrice?.compareTo(
                    BigDecimal.ZERO
                ) == 0
            ) {
                binding.originalPriceTextView.visibility = View.GONE
            } else {
                binding.originalPriceTextView.visibility = View.VISIBLE
            }

            binding.productImg.clipToOutline = true


            if (menuDishDetails.quantity ?: 0 > 0) {
                binding.addButton.visibility = View.GONE
                binding.quantityButton.visibility = View.VISIBLE
                binding.quantityTextView.visibility = View.VISIBLE
//                binding.totalPriceTextView.visibility = View.VISIBLE
            }

            if (menuDishDetails.isCustomizable) {
                binding.customizeDropDownTxt.visibility = View.VISIBLE
            } else {
                binding.customizeDropDownTxt.visibility = View.GONE
            }

            var orderCount =
                if (menuDishDetails.quantity == null) 0 else menuDishDetails.quantity ?: 0
            val dishPrice =
                if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice ?: BigDecimal.ZERO > BigDecimal.ZERO) menuDishDetails.productDiscountPrice else menuDishDetails.productOriPrice
                    ?: BigDecimal.ZERO

            menuDishDetails.productCustomizedPrice = dishPrice

            var totalDishPrice = dishPrice?.times(orderCount.toBigDecimal())

            binding.quantityTextView.text = orderCount.toString()
//            binding.totalPriceTextView.text = "₹ $totalDishPrice"


/*
            var itemsNames = ""
            // Options
            for ((i, optionsDTO) in menuDishDetails.optionValueIds?.withIndex() ?: arrayListOf()) {
                itemsNames = if (itemsNames.isBlank()) {
                    optionsDTO.value ?: ""
                } else {
                    itemsNames + ", " + optionsDTO.value
                }
            }

            // Addons
            for ((i, addValueDTO) in menuDishDetails.addOnValueIds?.withIndex() ?: arrayListOf()) {
                itemsNames = if (itemsNames.isBlank()) {
                    addValueDTO.value ?: ""
                } else {
                    itemsNames + ", " + addValueDTO.value
                }
            }

            if (itemsNames.isNullOrBlank()) {
                binding.customizedTxt.visibility = View.GONE
            } else {
                binding.customizedTxt.visibility = View.VISIBLE
                binding.customizedTxt.text = itemsNames
            }
*/


            binding.addButton.setOnClickListener {

                if (menuDishDetails.isCustomizable) {
                    listener.onItemAddClick(position, sectionPosition, menuDishDetails)
                } else {
                    orderCount++
                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice = totalDishPrice?.plus(dishPrice ?: BigDecimal.ZERO)
//                    binding.totalPriceTextView.text = "₹ $totalDishPrice"

                    binding.addButton.visibility = View.GONE
                    binding.quantityButton.visibility = View.VISIBLE
                    binding.quantityTextView.visibility = View.VISIBLE
//                    binding.totalPriceTextView.visibility = View.VISIBLE

                    menuDishDetails.quantity = orderCount
                    listener.onItemAddClick(position, sectionPosition, menuDishDetails)
                }


            }


            binding.addImageView.setOnClickListener {
/*
                orderCount++
                binding.quantityTextView.text = orderCount.toString()
                totalDishPrice += dishPrice
                binding.totalPriceTextView.text = "₹ $totalDishPrice"
                menuDishDetails?.quantity = orderCount
*/
                if (menuDishDetails.isCustomizable) {
                    listener.onItemAddClick(position, sectionPosition, menuDishDetails)
                } else {
                    orderCount++
                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice = totalDishPrice?.plus(dishPrice ?: BigDecimal.ZERO)
//                    binding.totalPriceTextView.text = "₹ $totalDishPrice"
//                        "₹ ${totalDishPrice.setScale(2, RoundingMode.HALF_UP)}"
                    menuDishDetails.quantity = orderCount
                    listener.onItemAddClick(position, sectionPosition, menuDishDetails)
                }


            }


            binding.removeImageView.setOnClickListener {

                if (menuDishDetails.isCustomizable) {
                    listener.onItemClick(position, sectionPosition, menuDishDetails)
                } else {
                    orderCount--
                    if (orderCount == 0) {
                        binding.addButton.visibility = View.VISIBLE
                        binding.quantityButton.visibility = View.GONE
                        binding.quantityTextView.visibility = View.GONE
//                        binding.totalPriceTextView.visibility = View.GONE
                    }

                    binding.quantityTextView.text = orderCount.toString()
                    totalDishPrice = totalDishPrice?.minus(dishPrice ?: BigDecimal.ZERO)
//                    binding.totalPriceTextView.text = "₹ $totalDishPrice"

                    menuDishDetails.quantity = orderCount
                    listener.onItemClick(position, sectionPosition, menuDishDetails)
                }
            }

            binding.customizeDropDownTxt.setOnClickListener {
//                listener.onItemCustomizeClick(adapterPosition, sectionPosition, menuDishDetails)
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
*/


/*
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


            fun from(parent: ViewGroup): MenuDishViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishCellLayoutStaffBinding.inflate(layoutInflater, parent, false)
                return MenuDishViewHolder(binding)
            }
        }
    }


    override fun getItemCount(): Int = menuDishList.size


    interface OnDishClickListener {
        fun onItemClick(
            position: Int,
            sectionPosition: Int,
            menuDishDetails: DishDetailsDTO
        )

        fun onItemAddClick(
            position: Int,
            sectionPosition: Int,
            menuDishDetails: DishDetailsDTO
        )

        fun onItemCustomizeClick(
            position: Int,
            sectionPosition: Int,
            menuDishDetails: DishDetailsDTO
        )

        /* fun onInactiveListClick(
             position: Int,
             dishMenuSection: DishWithSectionDetails
         )*/
    }

}
package com.astrika.stywis_staff.adapters.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.BillGenerationOrderViewCellLayoutBinding
import com.astrika.stywis_staff.models.OrderProductDetails


class BillGenerationOrderDishAdapter(
    private val context: Context
) : RecyclerView.Adapter<BillGenerationOrderDishAdapter.ViewHolder>() {

    var arrayList = ArrayList<OrderProductDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dto = arrayList[position]
        holder.bind(context, position, dto)
    }

    override fun getItemCount(): Int = arrayList.size


    class ViewHolder(private val binding: BillGenerationOrderViewCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            context: Context,
            position: Int,
            details: OrderProductDetails
        ) {

            binding.dishDetails = details

            val orderCount = details.quantity ?: 0
            val dishPrice =
                if (details.productDisPrice ?: 0 > 0) details.productDisPrice else details.productOriPrice
                    ?: 0
//            val totalDishPrice = dishPrice ?: 0 * orderCount
            val totalDishPrice = details.totalItemPrice ?: 0 * orderCount
            binding.totalPriceTextView.text = "₹ $totalDishPrice"
//            binding.dishPriceTextView.text = "₹ $dishPrice"

            var itemsNames = ""
            // Options
            for ((i, optionsDTO) in details.optionValueIds?.withIndex() ?: arrayListOf()) {
                itemsNames = if (itemsNames.isBlank()) {
                    optionsDTO.value ?: ""
                } else {
                    itemsNames + ", " + optionsDTO.value
                }
            }

            // Addons
            for ((i, addValueDTO) in details.addOnValueIds?.withIndex() ?: arrayListOf()) {
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


        }


        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    BillGenerationOrderViewCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}
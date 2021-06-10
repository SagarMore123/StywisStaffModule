package com.astrika.stywis_staff.adapters.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.AutocompleteTextItemCellLayoutStaffBinding
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO

class StockCustomizationOptionsAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<StockCustomizationOptionsAdapter.ViewHolder>() {

    var list = listOf<StockCustomizationMasterDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(position, item, clickListener)
    }

    class ViewHolder(private val binding: AutocompleteTextItemCellLayoutStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            masterDto: StockCustomizationMasterDTO,
            itemClick: OnItemClickListener
        ) {


            binding.text = masterDto.customizeOptionName ?: ""

            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, masterDto)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    AutocompleteTextItemCellLayoutStaffBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, masterDto: StockCustomizationMasterDTO)

    }

}
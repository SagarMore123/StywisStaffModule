package com.astrika.stywis_staff.adapters.customization

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.CustomizationOptionsRowCellLayoutBinding
import com.astrika.stywis_staff.models.CustomizationOptionDTOGroupRow

class CustomizationOptionsRowAdapter(
    val context: Context,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<CustomizationOptionsRowAdapter.ViewHolder>() {

    var optionsArrayList = ArrayList<CustomizationOptionDTOGroupRow>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class ViewHolder(val binding: CustomizationOptionsRowCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            customizationAddOnDTOGroupRow: CustomizationOptionDTOGroupRow,
            listener: OnItemClickListener,
            optionsArrayList: ArrayList<CustomizationOptionDTOGroupRow>
        ) {

            binding.customizationOptionsValueDTO = customizationAddOnDTOGroupRow
            binding.mainLayout.setOnClickListener {
                binding.checkBox.performClick()
            }

            binding.checkBox.setOnClickListener {
                optionsArrayList[adapterPosition].selected = binding.checkBox.isChecked
                listener.onItemClick(adapterPosition, optionsArrayList[adapterPosition])
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomizationOptionsRowCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customizationOptionDTOGroupRow: CustomizationOptionDTOGroupRow?
        customizationOptionDTOGroupRow = optionsArrayList[position]
        holder.bind(context, position, customizationOptionDTOGroupRow, listener, optionsArrayList)
    }

    override fun getItemCount(): Int {
        return optionsArrayList.size
    }

    interface OnItemClickListener {

        fun onItemClick(
            position: Int,
            dto: CustomizationOptionDTOGroupRow
        )

        fun onRemoveItem(
            position: Int,
            customizationOptionDTOGroupRow: CustomizationOptionDTOGroupRow
        )

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
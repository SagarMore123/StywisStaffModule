package com.astrika.stywis_staff.adapters.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.AutocompleteTextItemCellLayoutBinding
import com.astrika.stywis_staff.models.VisitPurposeDTO

class VisitPurposeAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<VisitPurposeAdapter.ViewHolder>() {

    var list = listOf<VisitPurposeDTO>()
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

    class ViewHolder(private val binding: AutocompleteTextItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            dto: VisitPurposeDTO,
            itemClick: OnItemClickListener
        ) {


            binding.text = dto.name

            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, dto)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    AutocompleteTextItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, dto: VisitPurposeDTO)

    }

}
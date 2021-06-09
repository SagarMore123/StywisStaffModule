package com.astrika.stywis_staff.adapters.applicablediscount

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.ApplicableDiscountsCellLayoutBinding
import com.astrika.stywis_staff.models.OutletDiscountDetailsDTO

class ApplicableDiscountsAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener,
    private var isViewDiscount: Boolean
) :
    RecyclerView.Adapter<ApplicableDiscountsAdapter.ViewHolder>() {

    var list = listOf<OutletDiscountDetailsDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(mActivity, position, item, clickListener, isViewDiscount)
    }

    class ViewHolder(private val binding: ApplicableDiscountsCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: OutletDiscountDetailsDTO,
            itemClick: OnItemClickListener,
            isViewDiscount: Boolean
        ) {

            binding.applicableDiscountDTO = dto
            binding.isViewDiscount = isViewDiscount
            binding.discountDaysTimingDTO = dto.discountTimingList[0]

            binding.applyBtn.setOnClickListener {
                itemClick.onItemClick(position, dto)
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ApplicableDiscountsCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            dto: OutletDiscountDetailsDTO
        )
    }


}
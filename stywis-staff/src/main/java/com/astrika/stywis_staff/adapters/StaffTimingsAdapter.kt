package com.astrika.stywis_staff.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.StaffTimingCellLayoutBinding
import com.astrika.stywis_staff.models.StaffTimingDTO
import com.astrika.stywis_staff.utils.DaysEnum

class StaffTimingsAdapter : RecyclerView.Adapter<StaffTimingsAdapter.ViewHolder>() {

    var arrayList = ArrayList<StaffTimingDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dto = arrayList[position]
        holder.bind(position, dto)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val dto = arrayList[position]
        return dto.timingId.toInt()
    }


    class ViewHolder(val binding: StaffTimingCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            dto: StaffTimingDTO
        ) {
            for (day in DaysEnum.values()) {
                if (dto.weekDay == day.id.toLong()) {
                    dto.weekDayName = day.value
                    break
                }
            }
            binding.dto = dto


        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    StaffTimingCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}
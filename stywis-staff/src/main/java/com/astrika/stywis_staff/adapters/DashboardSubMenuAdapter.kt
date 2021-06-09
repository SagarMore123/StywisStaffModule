package com.astrika.stywis_staff.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.DashboardDrawerSubCellLayoutBinding
import com.astrika.stywis_staff.models.DashboardDrawerDTO

class DashboardSubMenuAdapter(
    private val context: Context,
    private val listener: OnSubMenuItemClickListener
) : RecyclerView.Adapter<DashboardSubMenuAdapter.DashboardDrawerViewHolder>() {

    private var dashboardDrawerSubMenuList = ArrayList<DashboardDrawerDTO>()

    private var mainContainerPosition = 0
    fun setDrawerDashboardSubMenuList(
        mainContainerPosition: Int,
        dashboardDrawerSubMenuList: ArrayList<DashboardDrawerDTO>
    ) {
        this.mainContainerPosition = mainContainerPosition
        this.dashboardDrawerSubMenuList = dashboardDrawerSubMenuList
        notifyDataSetChanged()
    }


    /*var selectedPosition by Delegates.observable(-1) { property, oldPos, newPos ->
        if (newPos in dashboardDrawerSubMenuList.indices) {
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)
        }
    }*/

    class DashboardDrawerViewHolder(val binding: DashboardDrawerSubCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            mainContainerPosition: Int,
            position: Int,
            dashboardDrawerDTO: DashboardDrawerDTO,
            listener: OnSubMenuItemClickListener
        ) {

            binding.dashboardSubMenuDrawerDTO = dashboardDrawerDTO

            binding.root.setOnClickListener {
//                binding.basicInfoLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary))
//                binding.titleTxt.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                listener.onSubMenuItemClick(mainContainerPosition, position, dashboardDrawerDTO)
            }

        }

        companion object {

            var prevSelectedItem: DashboardDrawerDTO? = null

            fun from(parent: ViewGroup): DashboardDrawerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DashboardDrawerSubCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DashboardDrawerViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardDrawerViewHolder {
        return DashboardDrawerViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dashboardDrawerSubMenuList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val dashboardDrawerSubMenuDTO = dashboardDrawerSubMenuList[position]
        return dashboardDrawerSubMenuDTO.moduleId.toInt()
    }


    override fun onBindViewHolder(holder: DashboardDrawerViewHolder, position: Int) {

        val dashboardDrawerSubMenuDTO = dashboardDrawerSubMenuList[position]
        holder.bind(context, mainContainerPosition, position, dashboardDrawerSubMenuDTO, listener)
    }


    interface OnSubMenuItemClickListener {
        fun onSubMenuItemClick(
            mainContainerPosition: Int,
            position: Int, dashboardDrawerDTO: DashboardDrawerDTO
        )
    }

}
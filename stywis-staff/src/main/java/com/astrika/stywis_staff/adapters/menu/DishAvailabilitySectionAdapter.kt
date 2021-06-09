package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.DishAvailabilitySectionCellLayoutBinding
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails


class DishAvailabilitySectionAdapter(
    private val context: Context
) : RecyclerView.Adapter<DishAvailabilitySectionAdapter.DishAvailabilitySectionViewHolder>() {

    private var menuSectionList = ArrayList<MenuSectionWithDishDetails>()

    fun setMenuSectionList(menuSectionList: ArrayList<MenuSectionWithDishDetails>) {
        this.menuSectionList = menuSectionList
        notifyDataSetChanged()
    }

    class DishAvailabilitySectionViewHolder(private val binding: DishAvailabilitySectionCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            menuSection: MenuSectionWithDishDetails?
        ) {

            binding.sectionDetails = menuSection?.catalogueSectionDTO

            val dishAvailabilityAdapter = DishAvailabilityAdapter(context)
            binding.dishRecyclerView.adapter = dishAvailabilityAdapter

            dishAvailabilityAdapter.setDishList(menuSection?.activeProductList ?: arrayListOf())


        }

        companion object {

            fun from(parent: ViewGroup): DishAvailabilitySectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishAvailabilitySectionCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DishAvailabilitySectionViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishAvailabilitySectionViewHolder {
        return DishAvailabilitySectionViewHolder.from(parent)
    }

    override fun getItemCount(): Int = menuSectionList.size

    override fun onBindViewHolder(
        holderMenuSection: DishAvailabilitySectionViewHolder,
        position: Int
    ) {

        val menuSectionItem = menuSectionList[position]
        holderMenuSection.bind(context, position, menuSectionItem)

    }


/*
    interface OnDishAddClickListener {
        fun onAddItemClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )
        fun onInactiveListClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )
    }*/

}
package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.MenuSectionCellLayoutStaffBinding
import com.astrika.stywis_staff.models.menu.MenuSectionWithDishDetails

class MenuSectionAdapter(
    private val context: Context,
    private val listener: MenuDishAdapter.OnDishClickListener
) : RecyclerView.Adapter<MenuSectionAdapter.MenuSectionViewHolder>() {

    private var menuSectionList = ArrayList<MenuSectionWithDishDetails>()

    fun setMenuSectionList(menuSectionList: ArrayList<MenuSectionWithDishDetails>) {
        this.menuSectionList = menuSectionList
        notifyDataSetChanged()
    }

    class MenuSectionViewHolder(private val binding: MenuSectionCellLayoutStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            menuSection: MenuSectionWithDishDetails?,
            listener: MenuDishAdapter.OnDishClickListener
        ) {

//            binding.dishMenuSection = menuSection
            binding.sectionDetails = menuSection?.catalogueSectionDTO

            val dishListAdapter = MenuDishAdapter(context, listener)
            binding.dishRecyclerView.adapter = dishListAdapter

            dishListAdapter.setMenuDishList(
                position,
                menuSection?.activeProductList ?: arrayListOf(),
                menuSection?.catalogueSectionDTO?.taxValue ?: 0.0
            )

            binding.sectionNameTextView.setOnClickListener {
                binding.expandImageView.performClick()
            }

            binding.expandImageView.setOnClickListener {

                if (binding.dishRecyclerView.visibility == View.VISIBLE) {
                    binding.dishRecyclerView.visibility = View.GONE
                    binding.expandImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_down_dashboard, null
                        )
                    )
                } else {
                    binding.dishRecyclerView.visibility = View.VISIBLE
                    binding.expandImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_up_dashboard, null
                        )
                    )
                }

            }

        }

        companion object {

            fun from(parent: ViewGroup): MenuSectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuSectionCellLayoutStaffBinding.inflate(layoutInflater, parent, false)
                return MenuSectionViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuSectionViewHolder {
        return MenuSectionViewHolder.from(parent)
    }

    override fun getItemCount(): Int = menuSectionList.size

    override fun onBindViewHolder(holderMenu: MenuSectionViewHolder, position: Int) {

        val menuSectionItem = menuSectionList[position]
        holderMenu.bind(context, position, menuSectionItem, listener)

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
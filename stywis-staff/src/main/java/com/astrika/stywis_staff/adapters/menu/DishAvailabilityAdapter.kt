package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.DishAvailabilityCellLayoutBinding
import com.astrika.stywis_staff.models.menu.DishDetailsDTO


class DishAvailabilityAdapter(
    private val context: Context
) : RecyclerView.Adapter<DishAvailabilityAdapter.DishAvailabilityViewHolder>() {

    private var menuDishList = ArrayList<DishDetailsDTO>()

    fun setDishList(menuDishList: ArrayList<DishDetailsDTO>) {
        this.menuDishList = menuDishList
        notifyDataSetChanged()
    }

    class DishAvailabilityViewHolder(private val binding: DishAvailabilityCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            menuDishDetails: DishDetailsDTO?
        ) {

            menuDishDetails?.isOutOfStock = false
            binding.dishDetails = menuDishDetails

            binding.dishAvailabilityImageView.setOnClickListener {
                if (menuDishDetails?.isOutOfStock!!) {
                    menuDishDetails?.isOutOfStock = false
                    binding.dishAvailabilityImageView.setImageDrawable(
                        null
                    )
                } else {
                    menuDishDetails?.isOutOfStock = true
                    binding.dishAvailabilityImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_close_accent_24dp,
                            null
                        )
                    )
                }
            }

        }


        companion object {


            fun from(parent: ViewGroup): DishAvailabilityViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishAvailabilityCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DishAvailabilityViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishAvailabilityViewHolder {
        return DishAvailabilityViewHolder.from(parent)
    }

    override fun getItemCount(): Int = menuDishList.size

    override fun onBindViewHolder(holderAvailability: DishAvailabilityViewHolder, position: Int) {

        val menuDishItem = menuDishList[position]
        holderAvailability.bind(context, position, menuDishItem)

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
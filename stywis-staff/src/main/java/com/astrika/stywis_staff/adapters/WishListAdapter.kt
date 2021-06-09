package com.astrika.stywis_staff.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.WishListCellLayoutBinding
import com.astrika.stywis_staff.models.WishListItemStatus
import com.astrika.stywis_staff.models.WishListItems

class WishListAdapter(
    private val context: Context,
    private var clickListener: OnItemClickListener
) : RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {

    var arrayList = ArrayList<WishListItems>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class WishListViewHolder(val binding: WishListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            item: WishListItems,
            clickListener: OnItemClickListener
        ) {

            binding.wishListItem = item

            when (item.status) {
                WishListItemStatus.COMPLETED.name -> {
                    binding.isSelected = true
                }
                WishListItemStatus.PENDING.name -> {
                    binding.isSelected = false
                }
            }

            binding.checkBox.setOnClickListener {
                item.selected = !item.selected
                clickListener.onItemClick(position, item, false)
            }

            binding.removeItem.setOnClickListener {
                clickListener.onItemClick(position, item, true)
            }

        }

        companion object {

            fun from(parent: ViewGroup): WishListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    WishListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return WishListViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(context, position, item, clickListener)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            item: WishListItems,
            isRemove: Boolean
        )
    }
}
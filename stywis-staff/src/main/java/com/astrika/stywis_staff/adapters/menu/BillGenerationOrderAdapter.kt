package com.astrika.stywis_staff.adapters.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.BillGenerationOrderCellLayoutBinding
import com.astrika.stywis_staff.models.OrderDTO


class BillGenerationOrderAdapter(
    private val context: Context
) : RecyclerView.Adapter<BillGenerationOrderAdapter.ViewHolder>() {

    var arrayList = ArrayList<OrderDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holderMenu: ViewHolder, position: Int) {

        val dto = arrayList[position]
        holderMenu.bind(context, position, dto)

    }


    class ViewHolder(private val binding: BillGenerationOrderCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            orderDTO: OrderDTO?
        ) {

            binding.orderDTO = orderDTO

            val dishListAdapter = BillGenerationOrderDishAdapter(context)
            binding.dishRecyclerView.adapter = dishListAdapter
            dishListAdapter.arrayList = orderDTO?.items ?: arrayListOf()
            /* dishListAdapter.arrayList = orderDTO.items
                 dishListAdapter.setMenuDishList(
                     orderDTO!!.activedishList,
                     orderDTO.menuSectionDTO.taxValue ?: 0.0
                 )*/


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

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    BillGenerationOrderCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}
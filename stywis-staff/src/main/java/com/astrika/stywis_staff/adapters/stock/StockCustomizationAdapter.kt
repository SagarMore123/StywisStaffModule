package com.astrika.stywis_staff.adapters.stock

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.StockCustomizationItemCellLayoutBinding
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback


class StockCustomizationAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StockCustomizationAdapter.StockProductViewHolder>() {

    var arrayList = ArrayList<StocksCustomizationDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockProductViewHolder {
        return StockProductViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderMenu: StockProductViewHolder, position: Int) {
        val dto = arrayList[position]
        holderMenu.bind(context, position, dto, listener)
    }


    class StockProductViewHolder(private val binding: StockCustomizationItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            dto: StocksCustomizationDTO,
            listener: OnItemClickListener
        ) {

            val masterRepository = MasterRepository.getInstance(context)
            if (dto.customizeOptionId != null && dto.customizeOptionId != 0L) {
                masterRepository?.fetchStockCustomizationOptionsMasterValueByIdLocal(dto.customizeOptionId,
                    object : IDataSourceCallback<StockCustomizationMasterDTO> {

                        override fun onDataFound(data: StockCustomizationMasterDTO) {

                            dto.customizeOptionName = data.customizeOptionName ?: ""
                            binding.dto = dto
                        }

                        override fun onError(error: String) {
                            binding.dto = dto
                        }

                    })
            } else {
                dto.customizeOptionName = ""
                binding.dto = dto
            }



            binding.icRemove.setOnClickListener {
                dto.active = false
                listener.onRemoveItemClick(position, dto)
            }

        }


        companion object {

            fun from(parent: ViewGroup): StockProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    StockCustomizationItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return StockProductViewHolder(binding)
            }
        }
    }


    override fun getItemCount(): Int = arrayList.size


    // remove item
    fun removeItem(position: Int) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnItemClickListener {
        fun onRemoveItemClick(
            position: Int,
            dto: StocksCustomizationDTO
        )
    }

}
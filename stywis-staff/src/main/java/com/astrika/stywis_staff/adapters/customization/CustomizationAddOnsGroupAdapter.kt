package com.astrika.stywis_staff.adapters.customization

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.CustomizationAddonsGroupCellLayoutStaffBinding
import com.astrika.stywis_staff.models.CustomizationAddOnDTO
import com.astrika.stywis_staff.models.CustomizationAddOnDTOGroup
import com.astrika.stywis_staff.models.CustomizationAddOnDTOGroupRow

class CustomizationAddOnsGroupAdapter(
    val context: Context,
    val listener: OnItemClickListener,
    val groupRowDeleteListener: CustomizationAddOnsRowAdapter.OnGroupItemClickListener,
    productId: Long
) : RecyclerView.Adapter<CustomizationAddOnsGroupAdapter.ViewHolder>() {

    var arrayList = ArrayList<CustomizationAddOnDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var productId = -1L

    init {
        this.productId = productId
    }

    //group added
    fun addData(position: Int, customizationAddOnDTO: CustomizationAddOnDTO) {

        //adding the productId
        /*val customizationAddOnDTOGroup = CustomizationAddOnDTOGroup()
        customizationAddOnDTOGroup.productId = productId
        customizationAddOnDTO.customizationAddOnDTO = customizationAddOnDTOGroup*/

        arrayList.add(customizationAddOnDTO)
//        notifyItemInserted(position)
        notifyDataSetChanged()
    }

    //group removed
    fun removeItem(position: Int, customizationAddOnDTOGroup: CustomizationAddOnDTOGroup?) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
//        notifyItemRangeChanged(position,arrayList.size)
    }

    //row removed
    fun removeChildItem(
        groupPosition: Int,
        position: Int,
        customizationAddOnDTOGroupRow: CustomizationAddOnDTOGroupRow
    ) {
        arrayList[groupPosition].customizationAddOnValueDTO?.removeAt(position)
        notifyItemChanged(groupPosition)
//        notifyDataSetChanged()
//        notifyItemRangeChanged(position,arrayList.size)
    }


    class ViewHolder(val binding: CustomizationAddonsGroupCellLayoutStaffBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun bind(
            context: Context,
            arrayList: ArrayList<CustomizationAddOnDTO>,
            position: Int,
            productId: Long,
            customizationAddOnDTO: CustomizationAddOnDTO,
            listener: OnItemClickListener,
            groupRowDeleteListener: CustomizationAddOnsRowAdapter.OnGroupItemClickListener
        ) {

            binding.customizationAddOnDTO = arrayList[adapterPosition].customizationAddOnDTO

            //adding the productId
            customizationAddOnDTO.customizationAddOnDTO.productId = productId

            binding.groupName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable) {
                    arrayList[adapterPosition].customizationAddOnDTO.customizeAddOnName =
                        s.toString()
                }
            })

            /*if(!binding.groupName.text.isNullOrEmpty() && binding.groupName.text.isNotBlank()){
                customizationAddOnDTO.customizationAddOnDTO?.customizeAddOnName = binding.groupName.text.toString() ?: ""
            }

            if(!binding.min.text.isNullOrEmpty() && binding.min.text.isNotBlank()){
                customizationAddOnDTO.customizationAddOnDTO?.minSelection = binding.min.text.toString().toLong() ?: 0
            }

            if(!binding.max.text.isNullOrEmpty() && binding.max.text.isNotBlank()){
                customizationAddOnDTO.customizationAddOnDTO?.maxSelection = binding.max.text.toString().toLong()  ?: 0
            }*/

            val customizationAdapter =
                CustomizationAddOnsRowAdapter(context, groupRowDeleteListener, listener, true)
            binding.addonsRowCellRecyclerView.adapter = customizationAdapter

            val customizationAddOnDTOGroupRowList =
                arrayList[adapterPosition].customizationAddOnValueDTO ?: arrayListOf()
            customizationAdapter.addData(
                adapterPosition,
                customizationAddOnDTOGroupRowList,
                arrayList
            )


            /*if(!customizationAddOnDTOGroupRowList.isNullOrEmpty()){
                for(item in customizationAddOnDTOGroupRowList){
                    customizationAdapter.addData(adapterPosition,customizationAddOnDTOGroupRowList.size, item)
                }
            }*/


        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomizationAddonsGroupCellLayoutStaffBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customizationAddOnDTO = arrayList[position]
        holder.bind(
            context,
            arrayList,
            position,
            productId,
            customizationAddOnDTO,
            listener,
            groupRowDeleteListener
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    interface OnItemClickListener {
        fun onDeleteGroup(position: Int, customizationAddOnDTOGroup: CustomizationAddOnDTOGroup?)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}
package com.astrika.stywis_staff.adapters.customization

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.CustomizationAddonsRowCellLayoutStaffBinding
import com.astrika.stywis_staff.models.CustomizationAddOnDTO
import com.astrika.stywis_staff.models.CustomizationAddOnDTOGroup
import com.astrika.stywis_staff.models.CustomizationAddOnDTOGroupRow
import com.astrika.stywis_staff.models.CustomizationDTO


class CustomizationAddOnsRowAdapter(
    val context: Context,
    val groupListener: OnGroupItemClickListener,
    val groupOnItemClickListener: CustomizationAddOnsGroupAdapter.OnItemClickListener,
    isAddOns: Boolean
) : RecyclerView.Adapter<CustomizationAddOnsRowAdapter.ViewHolder>(),
    CustomizationAddOnsGroupAdapter.OnItemClickListener {

    val arrayList = ArrayList<CustomizationDTO>()
    var addOnsArrayList = ArrayList<CustomizationAddOnDTOGroupRow>()
    var addOnsGroupArrayList = ArrayList<CustomizationAddOnDTO>()

    var groupPosition = 0

    /*
        companion object{
            val arrayList = ArrayList<CustomizationDTO>()
            val addOnsArrayList = ArrayList<CustomizationDTO>()
        }
    */
    var isAddOns = false

    init {
        this.isAddOns = isAddOns
    }

    fun addData(
        groupPosition: Int,
        addOnsArrayList: ArrayList<CustomizationAddOnDTOGroupRow>,
        arrayList: ArrayList<CustomizationAddOnDTO>
    ) {
        if (isAddOns) {
//            addOnsArrayList.add(customizationAddOnDTOGroupRow)
            this.addOnsArrayList = addOnsArrayList
            this.addOnsGroupArrayList = arrayList

        } else {
//            arrayList.add(customizationDTO)

        }
        this.groupPosition = groupPosition
//        notifyItemInserted(position?:0)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int, customizationDTO: CustomizationDTO, isAddOns: Boolean) {
        if (isAddOns) {
//            addOnsArrayList.removeAt(position)
//            notifyItemRangeChanged(position,addOnsArrayList.size)
        } else {
            arrayList.removeAt(position)
            notifyItemRangeChanged(position, arrayList.size)
        }

/*d
        notifyItemRemoved(position)
        if(isAddOns){
            notifyItemRangeChanged(position,addOnsArrayList.size)
        }else{
            notifyItemRangeChanged(position,arrayList.size)
        }
*/

    }


    class ViewHolder(val binding: CustomizationAddonsRowCellLayoutStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            groupPosition: Int,
            customizationAddOnDTOGroupRow: CustomizationAddOnDTOGroupRow,
            groupListener: OnGroupItemClickListener,
            isAddOns: Boolean,
            addOnsArrayList: ArrayList<CustomizationAddOnDTOGroupRow>
        ) {

            binding.customizationAddOnValueDTO = addOnsArrayList[adapterPosition]

            binding.mainLayout.setOnClickListener {
                binding.checkBox.performClick()
            }
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                binding.checkBox.isChecked = isChecked
                addOnsArrayList[adapterPosition].selected = isChecked
                groupListener.onItemClick(adapterPosition, addOnsArrayList[adapterPosition])
            }

            binding.discountPrice.addTextChangedListener(object : TextWatcher {
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
                    if (s.toString() == "null" || s.toString() == "") {
                        addOnsArrayList[adapterPosition].productDisPrice = 0L
                    } else {
                        addOnsArrayList[adapterPosition].productDisPrice = s.toString().toLong()
                    }
                }
            })


            /*if(!binding.rowName.text.isNullOrEmpty() && binding.rowName.text.isNotBlank()){
                customizationAddOnDTOGroupRow.customizeAddOnValueName = binding.rowName.text.toString()
            }

            if(!binding.discountPrice.text.isNullOrEmpty() && binding.discountPrice.text.isNotBlank()){
                customizationAddOnDTOGroupRow.discountPrice = binding.discountPrice.text.toString().toLong()
            }

            if(!binding.price.text.isNullOrEmpty() && binding.price.text.isNotBlank()){
                customizationAddOnDTOGroupRow.originalPrice = binding.price.text.toString().toLong()
            }*/

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomizationAddonsRowCellLayoutStaffBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customizationAddOnDTOGroupRow: CustomizationAddOnDTOGroupRow?
        if (isAddOns) {
            customizationAddOnDTOGroupRow = addOnsArrayList[position]
            holder.bind(
                context,
                position,
                groupPosition,
                customizationAddOnDTOGroupRow,
                groupListener,
                isAddOns,
                addOnsArrayList
            )
        }

    }

    override fun getItemCount(): Int {
        return if (isAddOns) {
            addOnsArrayList.size
        } else {
            arrayList.size
        }
    }

    interface OnGroupItemClickListener {
        fun onItemClick(position: Int, dto: CustomizationAddOnDTOGroupRow)

        fun onRemoveItem(
            groupPosition: Int,
            childPosition: Int,
            customizationAddOnDTOGroupRow: CustomizationAddOnDTOGroupRow
        )

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDeleteGroup(
        position: Int,
        customizationAddOnDTOGroup: CustomizationAddOnDTOGroup?
    ) {

    }

}
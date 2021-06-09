package com.astrika.stywis_staff.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.databinding.ServerCallsItemCellLayoutBinding
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.utils.RequestTypeEnum
import com.astrika.stywis_staff.utils.Utils

class ServerCallsAdapter(private val onDoneClickListener: OnDoneClickListener) :
    RecyclerView.Adapter<ServerCallsAdapter.ViewHolder>() {

    var arrayList = ArrayList<CheckInDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dto = arrayList[position]
        holder.bind(position, dto, onDoneClickListener)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val dto = arrayList[position]
        return dto.inCustomerRequestId?.toInt() ?: 0
    }


    class ViewHolder(val binding: ServerCallsItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            dto: CheckInDTO,
            onDoneClickListener: OnDoneClickListener
        ) {

            binding.serialNoTxt.text = "${position.plus(1)}."

            binding.utils = Utils.Companion
            binding.checkInDTO = dto

            for (requestTypeEnum in RequestTypeEnum.values()) {
                if (requestTypeEnum.name == dto.requestType) {
                    binding.requestTypeTxt.text = requestTypeEnum.value
                }
            }

            binding.doneBtn.setOnClickListener {
                onDoneClickListener.onDoneClick(position, dto)
            }

            val counter: Long = Utils.getLongFromTime(dto.checkInTime)
            val diff: Long = 1000

            val handler = Handler()

            handler.post(object : Runnable {
                override fun run() {

                    val millis: Long = System.currentTimeMillis() - (counter)

                    // Change the milliseconds to days, hours, minutes and seconds
//                    val days = millis / (24 * 60 * 60 * 1000)

                    val hours = millis / (1000 * 60 * 60) % 24
                    val minutes = millis / (1000 * 60) % 60
                    val seconds = (millis / 1000) % 60

                    binding.timerTxt.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    // Post the code again
                    // with a delay of 1 second.
                    handler.postDelayed(this, 1000)
                }
            })

        }


        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ServerCallsItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    interface OnDoneClickListener {
        fun onDoneClick(position: Int, dto: CheckInDTO)

    }

}
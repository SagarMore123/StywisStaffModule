package com.astrika.stywis_staff.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.ApprovedCheckinCellLayoutBinding
import com.astrika.stywis_staff.databinding.AwaitingCheckinCellLayoutBinding
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.models.SystemValueMasterDTO
import com.astrika.stywis_staff.models.TableManagementDTO
import com.astrika.stywis_staff.models.VisitPurposeDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.network.network_utils.SERVER_IMG_URL
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.RequestStatusEnum
import com.astrika.stywis_staff.utils.RequestTypeEnum
import com.astrika.stywis_staff.utils.Utils
import com.bumptech.glide.Glide

class CheckInAdapter(
    val context: Context,
    val listener: OnItemClickListener,
    val onDoneClickListener: ServerCallsAdapter.OnDoneClickListener
) :
    RecyclerView.Adapter<CheckInAdapter.BaseViewHolder<*>>() {

    var arrayList = listOf<CheckInDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var tableManagementList = ArrayList<TableManagementDTO>()
    private var tableIdsAlloted = arrayListOf<Int>()
    var tableSelected = false
    var currentTabSelected = -1

    private var occupiedTableArrayList = listOf<SystemValueMasterDTO>()
    private var vacantTableArrayList = listOf<SystemValueMasterDTO>()
    private var reservedTableArrayList = listOf<SystemValueMasterDTO>()
    val masterRepository = MasterRepository.getInstance(context)


    companion object {
        private const val APPROVED_CHECK_IN = 0
        private const val AWAITING_CHECK_IN = 1
        const val STATUS_PENDING = "PENDING"
        const val STATUS_APPROVED = "APPROVED"
    }

    fun submitList(updatedList: List<CheckInDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }

    fun vacantTableArrayList(vacantTableArrayList: List<SystemValueMasterDTO>) {
        this.vacantTableArrayList = vacantTableArrayList
    }

    fun occupiedTableArrayList(occupiedTableArrayList: List<SystemValueMasterDTO>) {
        this.occupiedTableArrayList = occupiedTableArrayList
    }

    fun reservedTableArrayList(reservedTableArrayList: List<SystemValueMasterDTO>) {
        this.reservedTableArrayList = reservedTableArrayList
    }

    fun setupTable(tableManagementList: ArrayList<TableManagementDTO>) {
        this.tableManagementList = tableManagementList
    }

    class ItemDiffCallback(
        private var oldList: List<CheckInDTO>,
        private var newList: List<CheckInDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].inCustomerRequestId == newList[oldItemPosition].inCustomerRequestId)
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].equals(newList[oldItemPosition]))
        }

    }


    inner class ApprovedCheckInViewHolder(val binding: ApprovedCheckinCellLayoutBinding) :
        BaseViewHolder<CheckInDTO>(binding.root) {

        @SuppressLint("SetTextI18n")
        override fun bind(checkInDTO: CheckInDTO, position: Int, listener: OnItemClickListener) {

/*
            var takeOrderBackground: Drawable? = null
            var generateBillBackground: Drawable? = null
            var takeOrderBtnEnabled: Boolean? = null
            var generateBillBtnEnabled: Boolean? = null
            var takeOrderBtnTxtColor: Int = -1
            var generateBillBtnTxtColor: Int = -1
            when (currentTabSelected) {
                0, 1 -> {
                    takeOrderBackground = ContextCompat.getDrawable(
                        context,
                        R.drawable.rectangular_button_drawable
                    )
                    generateBillBackground =
                        ContextCompat.getDrawable(context, R.drawable.rectangular_button_drawable)
                    takeOrderBtnEnabled = true
                    generateBillBtnEnabled = true
                    takeOrderBtnTxtColor = ContextCompat.getColor(context, R.color.colorWhite)
                    generateBillBtnTxtColor = ContextCompat.getColor(context, R.color.colorWhite)
                }
                2 -> {
                    binding.doneBtn.visibility = View.INVISIBLE
                    takeOrderBackground =
                        ContextCompat.getDrawable(context, R.drawable.rectangular_button_drawable)
                    generateBillBackground = ContextCompat.getDrawable(
                        context,
                        R.drawable.circular_rounded_corner_grey_background
                    )
                    takeOrderBtnEnabled = true
                    generateBillBtnEnabled = false
                    takeOrderBtnTxtColor = ContextCompat.getColor(context, R.color.colorWhite)
                    generateBillBtnTxtColor = ContextCompat.getColor(context, R.color.colorBlack)
                }
                3 -> {
                    binding.doneBtn.visibility = View.INVISIBLE
                    takeOrderBackground = ContextCompat.getDrawable(
                        context,
                        R.drawable.circular_rounded_corner_grey_background
                    )
                    generateBillBackground =
                        ContextCompat.getDrawable(context, R.drawable.rectangular_button_drawable)
                    takeOrderBtnEnabled = false
                    generateBillBtnEnabled = true
                    takeOrderBtnTxtColor = ContextCompat.getColor(context, R.color.colorBlack)
                    generateBillBtnTxtColor = ContextCompat.getColor(context, R.color.colorWhite)
                }
            }
            binding.takeOrderBtn.background = takeOrderBackground
            binding.takeOrderBtn.isEnabled = takeOrderBtnEnabled ?: false
            binding.takeOrderBtn.setTextColor(takeOrderBtnTxtColor)
            binding.generateBillBtn.background = generateBillBackground
            binding.generateBillBtn.isEnabled = generateBillBtnEnabled ?: false
            binding.generateBillBtn.setTextColor(generateBillBtnTxtColor)
*/

            val takeOrder = "Take Order"
            val viewOrder = "View Order"
            val viewBill = "View Bill"


            when (currentTabSelected) {

                0 -> {   // Check In

                    setGreyBackground(binding.generateBillBtn)

                    when (checkInDTO.status) {

                        RequestStatusEnum.APPROVED.name -> {


                            val billList = checkInDTO.inList?.filter {
                                it.requestType == RequestTypeEnum.CALL_FOR_BILL.name
                            }

                            if (!billList.isNullOrEmpty()) {
                                setColorPrimaryBackground(binding.generateBillBtn)
                            }


                            binding.takeOrderBtn.text = takeOrder
//                            binding.checkoutBtn.isEnabled = false
//                            binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.grey))
                        }

                        RequestStatusEnum.COMPLETED.name -> {
                            binding.takeOrderBtn.text = viewOrder
                            binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.colorPrimary))
                            setColorPrimaryBackground(binding.generateBillBtn)
                        }
                    }
                }
/*

                1 -> {   // Service Request

                    binding.checkoutBtn.isEnabled = false
                    binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.grey))
                    setGreyBackground(binding.generateBillBtn)

                    when (checkInDTO.status) {

                        RequestStatusEnum.PENDING.name -> {
                            binding.takeOrderBtn.text = takeOrder
                            checkInDTO.inList = arrayListOf(checkInDTO)
                        }

                        RequestStatusEnum.COMPLETED.name -> {

                            binding.takeOrderBtn.text = viewOrder
                            setColorPrimaryBackground(binding.generateBillBtn)
                        }
                    }
                }
*/

                1 -> {   // Food Order

//                    binding.checkoutBtn.isEnabled = false
//                    binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.grey))
                    setGreyBackground(binding.generateBillBtn)

                    when (checkInDTO.status) {

                        RequestStatusEnum.PENDING.name -> {
                            binding.takeOrderBtn.text = takeOrder
                            checkInDTO.inList = arrayListOf(checkInDTO)
                        }
                        RequestStatusEnum.AWAITING_CONFIRMATION.name -> {
                            binding.takeOrderBtn.text = viewOrder
                            checkInDTO.inList = arrayListOf(checkInDTO)
                        }
                        RequestStatusEnum.APPROVED.name -> {
                            binding.takeOrderBtn.text = viewOrder
                            checkInDTO.inList = arrayListOf(checkInDTO)
                        }
                        RequestStatusEnum.COMPLETED.name -> {
                            binding.takeOrderBtn.text = viewOrder
                            setColorPrimaryBackground(binding.generateBillBtn)
                        }
                    }
                }

                2 -> {   // Bill Request

//                    binding.checkoutBtn.isEnabled = true
//                    binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.colorPrimary))

/*
                    when (checkInDTO.status) {

                        RequestStatusEnum.COMPLETED.name -> {
//                            binding.checkoutBtn.isEnabled = true
                            binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.colorPrimary))
                        }
                        else -> {
                            checkInDTO.inList = arrayListOf(checkInDTO)
//                            binding.checkoutBtn.isEnabled = false
//                            binding.checkoutBtn.setTextColor(context.resources.getColor(R.color.grey))
                        }
                    }
*/

                    binding.reAssignTxt.isEnabled = false
                    binding.reAssignTxt.setTextColor(context.resources.getColor(R.color.grey))
                    setColorPrimaryBackground(binding.generateBillBtn)
                    setGreyBackground(binding.takeOrderBtn)

                    binding.takeOrderBtn.text = viewOrder
                    binding.generateBillBtn.text = viewBill
                }

            }


            var tables = ""
            for (tableNo in checkInDTO.tableIdsAlloted ?: arrayListOf()) {
                tables += tableNo
            }
            binding.tableNumbersTxt.text = "Table $tables"

            when (currentTabSelected) {
                1 -> {
                    if (checkInDTO.inList.isNullOrEmpty()) {
                        binding.requestCallsRecyclerView.visibility = View.GONE
                    } else {
                        binding.requestCallsRecyclerView.visibility = View.VISIBLE
                        val serverCallsAdapter = ServerCallsAdapter(onDoneClickListener)
                        serverCallsAdapter.arrayList = checkInDTO.inList ?: arrayListOf()
                        binding.requestCallsRecyclerView.adapter = serverCallsAdapter
                    }

                }
            }

            if (checkInDTO.visitPurposeId != null && checkInDTO.visitPurposeId != 0L) {
                masterRepository?.fetchVisitPurposeMasterValueByIdLocal(
                    checkInDTO.visitPurposeId ?: 0,
                    object : IDataSourceCallback<VisitPurposeDTO> {
                        override fun onDataFound(data: VisitPurposeDTO) {
                            checkInDTO.visitPurpose = data.name ?: ""
                            binding.checkInDTO = checkInDTO
                        }

                        override fun onError(error: String) {
                            binding.checkInDTO = checkInDTO
                        }

                    })
            } else {
                checkInDTO.visitPurpose = ""
                binding.checkInDTO = checkInDTO

            }


            //on Done Button Click
            binding.checkoutBtn.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_CHECKEDOUT)
            }

            binding.mainLayout.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_TAKE_ORDER)
            }

            binding.takeOrderBtn.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_TAKE_ORDER)
            }

            binding.generateBillBtn.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_GENERATE_BILL)
            }

            binding.reAssignTxt.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_RE_ASSIGN)
            }

        }

        private fun setGreyBackground(textView: TextView) {
            textView.isEnabled = false
            textView.setTextColor(context.resources.getColor(R.color.grey))
            textView.background = ContextCompat.getDrawable(
                context,
                R.drawable.circular_rounded_corner_grey_background
            )
        }

        private fun setColorPrimaryBackground(textView: TextView) {
            textView.isEnabled = true
            textView.setTextColor(context.resources.getColor(R.color.colorWhite))
            textView.background =
                ContextCompat.getDrawable(context, R.drawable.rectangular_button_drawable)
        }

    }

    inner class AwaitingCheckInViewHolder(val binding: AwaitingCheckinCellLayoutBinding) :
        BaseViewHolder<CheckInDTO>(binding.root) {

        override fun bind(checkInDTO: CheckInDTO, position: Int, listener: OnItemClickListener) {

            binding.utils = Utils.Companion


            if (checkInDTO.visitPurposeId != null && checkInDTO.visitPurposeId != 0L) {
                masterRepository?.fetchVisitPurposeMasterValueByIdLocal(
                    checkInDTO.visitPurposeId ?: 0,
                    object : IDataSourceCallback<VisitPurposeDTO> {
                        override fun onDataFound(data: VisitPurposeDTO) {
                            checkInDTO.visitPurpose = data.name ?: ""
                            binding.checkInDTO = checkInDTO
                        }

                        override fun onError(error: String) {
                            binding.checkInDTO = checkInDTO
                        }

                    })
            } else {
                checkInDTO.visitPurpose = ""
                binding.checkInDTO = checkInDTO

            }

            //approve button not needed
            binding.approveBtnClick.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_APPROVE_CHECKIN)
            }

/*
            //configure the table management setup for awaiting checkins
            if (tableManagementList.isNotEmpty()) {
                tableSetup(binding.tableSetupLayout, checkInDTO)
            }
*/

            binding.rejectCheckInBtn.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_REJECT_CHECKIN)
            }

            binding.confirmCheckInBtn.setOnClickListener {
                listener.onItemClick(checkInDTO, Constants.ACTION_CONFIRM_CHECKIN)
/*
                if (!tableIdsAlloted.isNullOrEmpty()) {
                    listener.onItemClick(checkInDTO, Constants.ACTION_CONFIRM_CHECKIN)
                } else {
                    Constants.showToastMessage(context, "Please select a table")
                }
*/
            }

            /*if(Constants.isTableManagement == true){
                binding.tableMainLayout.visibility = View.VISIBLE
            }else{
                binding.tableMainLayout.visibility = View.GONE
            }*/


            val counter: Long = Utils.getLongFromTime(checkInDTO.checkInTime)
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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {

            APPROVED_CHECK_IN -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ApprovedCheckinCellLayoutBinding.inflate(layoutInflater, parent, false)
                ApprovedCheckInViewHolder(binding)
            }
            AWAITING_CHECK_IN -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    AwaitingCheckinCellLayoutBinding.inflate(layoutInflater, parent, false)
                AwaitingCheckInViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val checkInDTO = arrayList[position]
        when (holder) {
            is ApprovedCheckInViewHolder -> holder.bind(checkInDTO, position, listener)
            is AwaitingCheckInViewHolder -> holder.bind(checkInDTO, position, listener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val checkInDTO = arrayList[position]
/*
        return when (checkInDTO.status) {
            STATUS_PENDING -> AWAITING_CHECK_IN
            STATUS_APPROVED -> APRROVED_CHECK_IN
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
*/

        if (checkInDTO.requestType == RequestTypeEnum.CHECK_IN.name) { // For CheckIn Tab

            when (checkInDTO.status) {
                RequestStatusEnum.PENDING.name -> {
                    return AWAITING_CHECK_IN
                }
                RequestStatusEnum.APPROVED.name -> {
                    return APPROVED_CHECK_IN
                }
                else -> APPROVED_CHECK_IN
            }

        } else {
            return APPROVED_CHECK_IN // For Other Tabs , Eg. Server Calls, Food Order, Bill Requests,etc
        }

        return APPROVED_CHECK_IN // For Default
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(checkInDTO: CheckInDTO, position: Int, listener: OnItemClickListener)
    }


    private fun tableSetup(tableSetupLayout: RelativeLayout, checkInDTO: CheckInDTO) {

/*
        if (tableManagementList.isNotEmpty()) {
            for (table in tableManagementList) {
                val inflater = LayoutInflater.from(context)
                val childLayout =
                    inflater.inflate(R.layout.table_cell_layout, tableSetupLayout, false)
                tableSetupLayout.addView(childLayout)
                val imageView = childLayout.findViewById<ImageView>(R.id.imageView)

                //if the table id is already allotted to customer(table occupied)
                if (table.occupied == true) {
                    childLayout.isEnabled = false
                    loadImage(table, true, imageView)
                } else {
                    loadImage(table, false, imageView)
                }

                if (table.reserved != null) {
                    if (table.reserved == true) {
                        childLayout.isEnabled = false
                    }
                }

                */
/*if (table.capacity != 0L) {
                    val typeOfSeater =
                        Constants.getTypeOfSeaterInFullName(table.capacity.toString())
                    for (item in unOccupiedTableArrayList) {
                        if (item.name != null) {
                            if (item.name!!.startsWith(typeOfSeater)) {
                                Glide.with(context)
                                    .load(SERVER_IMG_URL + item.value).into(imageView)
                                break
                            }
                        }
                    }
                }*//*

                val tableNameTxt = childLayout.findViewById<TextView>(R.id.tableNameTxt)
                tableNameTxt.text = table.tableCode

                table.xcoordinate = table.xcoordinate?.times(tableSetupLayout.height)?.div(100)
                table.ycoordinate = table.ycoordinate?.times(tableSetupLayout.width)?.div(100)

                childLayout.x = (table.xcoordinate ?: 0f) - 20f
                val (tableSetupLayoutX, tableSetupLayoutY) = tableSetupLayout.screenLocationInWindow
                childLayout.y = (table.ycoordinate ?: 0f) - tableSetupLayoutY.toFloat()

//                childLayout.x = table.xcoordinate ?: 0f
//                childLayout.y = table.ycoordinate ?: 0f

                childLayout.setOnClickListener {
                    //color change and allocate seat
                    if (!tableSelected) {
                        loadImage(table, true, imageView)
                        if (table.tableId != null) {
                            tableIdsAlloted.add(table.tableId!!.toInt())
                            tableSelected = true
                        }
                    } else if (tableSelected) {
                        loadImage(table, false, imageView)
                        tableIdsAlloted.remove(table.tableId!!.toInt())
                        tableSelected = false
                    }
                    checkInDTO.tableIdsAlloted = tableIdsAlloted
//                    listener.onItemClick(checkInDTO,Constants.ACTION_CONFIRM_CHECKIN)
                }
            }
        }
*/
    }

    interface OnItemClickListener {
        fun onItemClick(checkInDTO: CheckInDTO, action: String)
    }

    private fun loadImage(table: TableManagementDTO, isOccupied: Boolean, imageView: ImageView) {
        if (table.capacity != 0L) {
            val typeOfSeater =
                Constants.getTypeOfSeaterInFullName(table.capacity.toString())
            val list: List<SystemValueMasterDTO> = if (isOccupied) {
                occupiedTableArrayList
            } else {
                vacantTableArrayList
            }
            for (item in list) {
                if (item.name != null) {
                    if (item.name!!.startsWith(typeOfSeater)) {
                        Glide.with(context)
                            .load(SERVER_IMG_URL + item.value).into(imageView)
                        break
                    }
                }
            }
        }
    }

    fun tabPosition(position: Int) {
        this.currentTabSelected = position
    }

    val View.screenLocationInWindow
        get():IntArray {
            val point = IntArray(2)
            getLocationInWindow(point)
            return point
        }

}
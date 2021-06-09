package com.astrika.stywis_staff.view.dashboard.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.BottomSheetAddCheckInDialogBinding
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.models.VisitPurposeDTO
import com.astrika.stywis_staff.utils.AutocompleteViewActivity
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.BottomSheetCheckInViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCheckInDialog(
    private val fragment: Fragment,
    private val isReAssignTable: Boolean,
    private val tableIdOld: Int,
    private val dto: CheckInDTO
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddCheckInDialogBinding
    private lateinit var viewModel: BottomSheetCheckInViewModel
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()
//    private var tableManagementList = ArrayList<TableManagementDTO>()

    private lateinit var dashboardSubFragment: DashboardSubFragment
    private lateinit var awaitingCheckInsFragment: AwaitingCheckInsFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if (container != null) {
            mContext = container.context
        }
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_add_check_in_dialog,
            container,
            false
        )


        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            BottomSheetCheckInViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.tableSetupLayout = binding.tableSetupLayout
//        viewModel.tableSetupLayoutWidth = viewModel.tableSetupLayout.width
//        viewModel.tableSetupLayoutHeight = viewModel.tableSetupLayout.height

        viewModel.isReAssignTable = isReAssignTable
        viewModel.tableIdOld = tableIdOld



        if (fragment is DashboardSubFragment) {
            dashboardSubFragment = fragment
/*

            if (isReAssignTable) {
                binding.confirmCheckInBtn.text = "Re-assign\nTable"
                viewModel.viewType.value = 6
            }
*/
        } else if (fragment is AwaitingCheckInsFragment) {
            awaitingCheckInsFragment = fragment
            viewModel.viewType.value = 5
            viewModel.userId = dto.userId
            viewModel.checkInDTO = dto
            viewModel.visitorMobileNumber.value = dto.mobileNo
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()

    }

    private fun observers() {

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.mobileNumber.observe(viewLifecycleOwner, Observer {
            viewModel.mobileNumberError.value = ""
        })

        viewModel.visitorFirstName.observe(viewLifecycleOwner, Observer {
            viewModel.visitorFirstNameError.value = ""
        })

        viewModel.visitorLastName.observe(viewLifecycleOwner, Observer {
            viewModel.visitorLastNameError.value = ""
        })

        viewModel.visitorEmail.observe(viewLifecycleOwner, Observer {
            viewModel.visitorEmailError.value = ""
        })

        viewModel.visitorMobileNumber.observe(viewLifecycleOwner, Observer {
            viewModel.visitorMobileNumberError.value = ""
        })

        viewModel.visitPurpose.observe(viewLifecycleOwner, Observer {
            viewModel.visitPurposeError.value = ""
        })

        viewModel.noOfGuest.observe(viewLifecycleOwner, Observer {
            viewModel.noOfGuestError.value = ""
        })

        viewModel.cancelClickEvent.observe(viewLifecycleOwner, Observer {

            if (fragment is DashboardSubFragment) {
                dashboardSubFragment.bottomSheetDialogDismiss(dto)
            } else if (fragment is AwaitingCheckInsFragment) {
                awaitingCheckInsFragment.bottomSheetDialogDismiss(dto)
            }
            dismiss()
        })

        viewModel.verifyOtpEditTxt.observe(viewLifecycleOwner, Observer {
            viewModel.verifyOtpError.value = ""
        })

/*

        viewModel.tableManagementListLiveData.observe(requireActivity(), Observer {
            //configure the table management setup
//            viewModel.arrangeTablesOnLayout()
            tableManagementList = it
            if (tableManagementList.isNotEmpty()) {
                tableSetup(binding.tableSetupLayout)
            }
        })

*/

        viewModel.onVisitorPurposeClickEvent.observeChange(this, Observer {

            val requestCode = Constants.VISIT_PURPOSE_CODE
            val intent = Intent(requireActivity(), AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)

        })


    }

/*
    private fun tableSetup(tableSetupLayout: RelativeLayout) {
        if (tableManagementList.isNotEmpty()) {
            for (table in tableManagementList) {
                val inflater = LayoutInflater.from(context)
                val childLayout =
                    inflater.inflate(R.layout.table_cell_layout, tableSetupLayout, false)
                tableSetupLayout.addView(childLayout)
                val imageView = childLayout.findViewById<ImageView>(R.id.imageView)
                childLayout.isEnabled = true

                //if the table id is already allotted to customer(table occupied)
                for (tableStatus in TableStatusEnum.values()) {

                    when (table.status) {
                        TableStatusEnum.UNOCCUPIED.name, TableStatusEnum.VACANT.name -> {
                            loadImage(table, imageView)
                        }
                        TableStatusEnum.OCCUPIED.name -> {
                            childLayout.isEnabled = false
                            loadImage(table, imageView)
                        }
                        TableStatusEnum.RESERVED.name -> {
                            childLayout.isEnabled = false
                        }
                        else -> {
                            loadImage(table, imageView)
                        }
                    }
                }

*/
/*
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
*//*


                val tableNameTxt = childLayout.findViewById<TextView>(R.id.tableNameTxt)
                tableNameTxt.text = table.tableCode
//                childLayout.x = table.xcoordinate ?: 0f
//                childLayout.y = table.ycoordinate ?: 0f
                val (tableSetupLayoutX, tableSetupLayoutY) = tableSetupLayout.screenLocationInWindow

*/
/*
                val tableX = table.xcoordinate?.times(tableSetupLayout.width)?.div(100)
                val tableY = table.ycoordinate?.times(tableSetupLayout.height)?.div(100)

                // Viewing Position Modification as the Container layout
                childLayout.x = (tableX ?: 0f) - 20f
                childLayout.y = (tableY ?: 0f) - (tableSetupLayoutY.toFloat() / 1.35f)
*//*


                if (isReAssignTable) {

                    val tableX = table.xcoordinate?.times(1200)?.div(100)
                    val tableY = table.ycoordinate?.times(1020)?.div(100)

                    // Viewing Position Modification as the Container layout
                    childLayout.x = (tableX ?: 0f) - 20f
                    childLayout.y = (tableY ?: 0f) - (tableSetupLayoutY.toFloat() / 1.2f)
                } else {


                    val tableX = table.xcoordinate?.times(1200)?.div(100)
                    val tableY = table.ycoordinate?.times(1020)?.div(100)

                    // Viewing Position Modification as the Container layout
                    childLayout.x = (tableX ?: 0f) - 20f
                    childLayout.y = (tableY ?: 0f) - (tableSetupLayoutY.toFloat() / 1.75f)
                }


                childLayout.setOnClickListener {
                    //color change and allocate seat
                    if (!viewModel.tableSelected) {
                        table.status = TableStatusEnum.OCCUPIED.name
                        loadImage(table, imageView)
                        if (table.tableId != null) {
                            viewModel.tableIdsAlloted.add(table.tableId?.toInt() ?: 0)
                            viewModel.tableSelected = true
                        }
                    } else if (viewModel.tableSelected) {
                        table.status = TableStatusEnum.VACANT.name
                        loadImage(table, imageView)
                        viewModel.tableIdsAlloted.remove(table.tableId?.toInt() ?: 0)
                        viewModel.tableSelected = false
                    }
                }
            }
        }
    }

    private fun loadImage(table: TableManagementDTO, imageView: ImageView) {
        if (table.capacity != 0L) {
            val typeOfSeater =
                Constants.getTypeOfSeaterInFullName(table.capacity.toString())
*/
/*

            val list: List<SystemValueMasterDTO> = if (isOccupied) {
                viewModel.occupiedTableArrayList
//                viewModel.reservedTableArrayList
            } else {
                viewModel.vacantTableArrayList
            }
*//*


            val list: List<SystemValueMasterDTO> = when (table.status) {
                TableStatusEnum.UNOCCUPIED.name, TableStatusEnum.VACANT.name -> {
                    viewModel.vacantTableArrayList
                }
                TableStatusEnum.OCCUPIED.name -> {
                    viewModel.occupiedTableArrayList
                }
                TableStatusEnum.RESERVED.name -> {
                    viewModel.reservedTableArrayList
                }
                else -> {
                    viewModel.vacantTableArrayList
                }
            }

            for (item in list) {
                if (item.name != null) {
                    if (item.name!!.startsWith(typeOfSeater)) {
                        Glide.with(this)
                            .load(SERVER_IMG_URL + item.value).into(imageView)
                        break
                    }
                }
            }
        }
    }


    private val View.screenLocationInWindow
        get():IntArray {
            val point = IntArray(2)
            getLocationInWindow(point)
            return point
        }

*/

    interface BottomSheetDialogInterface {
        fun bottomSheetDialogDismiss(checkInDTO: CheckInDTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        // BEGIN_INCLUDE (parse_open_document_response)
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.
        Utils.hideKeyboard(requireActivity())

        if (resultCode == Constants.VISIT_PURPOSE_CODE) {
            val visitPurposeDTO =
                resultData?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as VisitPurposeDTO
            if (visitPurposeDTO != null) {
                viewModel.visitPurposeId = visitPurposeDTO.visitPurposeId ?: 0
                viewModel.visitPurpose.value = visitPurposeDTO.name ?: ""
            }
        }


    }

}
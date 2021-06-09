package com.astrika.stywis_staff.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.CheckInAdapter
import com.astrika.stywis_staff.adapters.ServerCallsAdapter
import com.astrika.stywis_staff.databinding.FragmentAwaitingCheckInsBinding
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.utils.*
import com.astrika.stywis_staff.view.dashboard.viewmodels.DashboardSubViewModel

class AwaitingCheckInsFragment : Fragment(), CheckInAdapter.OnItemClickListener,
    ServerCallsAdapter.OnDoneClickListener, BottomSheetCheckInDialog.BottomSheetDialogInterface {

    private lateinit var binding: FragmentAwaitingCheckInsBinding
    private lateinit var viewModel: DashboardSubViewModel
    lateinit var checkInAdapter: CheckInAdapter
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()

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
            R.layout.fragment_awaiting_check_ins,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DashboardSubViewModel::class.java,
            this,
            binding.root
        )!!

        binding.dashboardSubViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInAdapter = CheckInAdapter(requireContext(), this, this)
        binding.awaitingCheckInListRecyclerView.adapter = checkInAdapter

//        viewModel.populateTableListing(null)

        val arrayList = arrayListOf(RequestStatusEnum.PENDING.name)
        viewModel.fetchDashboardListing(RequestTypeEnum.CHECK_IN.name, arrayList)
        observers()

    }

    private fun observers() {

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.awaitingCheckInLiveList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                //set the data to recycler view
                viewModel.setNoDataFoundMessage(RequestTypeEnum.CHECK_IN.name)
                viewModel.noDataFoundVisibility.set(true)
            } else {
                viewModel.noDataFoundVisibility.set(false)
            }
//            checkInAdapter.submitList(it)
            checkInAdapter.arrayList = (it)
        })

/*

        //VACANT(green)
        viewModel.vacantTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            checkInAdapter.vacantTableArrayList(it)
        })

        //OCCUPIED(blue)
        viewModel.occupiedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            checkInAdapter.occupiedTableArrayList(it)
        })

        //RESERVED(red)
        viewModel.reservedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            checkInAdapter.reservedTableArrayList(it)
        })

        */
/*viewModel.unOccupiedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            checkInAdapter.unOccupiedTableArrayList(it)
        })

        viewModel.occupiedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            checkInAdapter.occupiedTableArrayList(it)
        })*//*


        viewModel.tableManagementListLiveData.observe(requireActivity(), Observer {
            checkInAdapter.setupTable(it)
        })
*/

        viewModel.checkInApproved.observe(viewLifecycleOwner, Observer {
            if (it) {
                checkInAdapter.notifyDataSetChanged()
            }
        })


    }

    override fun onItemClick(checkInDTO: CheckInDTO, action: String) {
        // approve the checkin of a customer
        when (action) {
            Constants.ACTION_CONFIRM_CHECKIN -> {
/*

                if (checkInDTO.tableIdsAlloted.isNullOrEmpty()) {
                    val bottomSheet = BottomSheetCheckInDialog(
                        this, false,
                        0,
                        checkInDTO
                    )
                    bottomSheet.show(childFragmentManager, "ModalBottomSheet")
                }
*/
                viewModel.isGPinVerified.observe(viewLifecycleOwner, Observer {
                    if (it) {

                        val bottomSheet = BottomSheetCheckInDialog(
                            this, false,
                            0,
                            checkInDTO
                        )
                        bottomSheet.show(childFragmentManager, "ModalBottomSheet")
                    } else {
                        viewModel.verifyGPin(checkInDTO)
                    }
                })


            }

            // Reject
            Constants.ACTION_REJECT_CHECKIN -> {
                viewModel.rejectCheckIn(checkInDTO)
            }

            //not needed
            Constants.ACTION_APPROVE_CHECKIN -> {
//                viewModel.approveCheckIn(checkInDTO.inCustomerRequestId)
            }
        }
    }

    override fun onDoneClick(position: Int, dto: CheckInDTO) {

    }

    override fun bottomSheetDialogDismiss(checkInDTO: CheckInDTO) {
        viewModel.fetchDashboardListing(
            RequestTypeEnum.CHECK_IN.name,
            arrayListOf(RequestStatusEnum.PENDING.name)
        )
    }

}
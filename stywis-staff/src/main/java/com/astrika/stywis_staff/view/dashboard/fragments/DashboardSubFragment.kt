package com.astrika.stywis_staff.view.dashboard.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.CheckInAdapter
import com.astrika.stywis_staff.adapters.ServerCallsAdapter
import com.astrika.stywis_staff.databinding.FragmentDashboardSubStaffBinding
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.models.OrderBundleDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.RequestStatusEnum
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.DashboardSubViewModel
import com.astrika.stywis_staff.view.dashboard.viewmodels.DashboardViewModel
import com.astrika.stywis_staff.view.menus.OrderViewActivity

class DashboardSubFragment : Fragment(), CheckInAdapter.OnItemClickListener,
    ServerCallsAdapter.OnDoneClickListener, BottomSheetCheckInDialog.BottomSheetDialogInterface {

    private lateinit var binding: FragmentDashboardSubStaffBinding
    lateinit var viewModel: DashboardSubViewModel
    lateinit var dashboardViewModel: DashboardViewModel
    var position = -1
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
            R.layout.fragment_dashboard_sub_staff,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DashboardSubViewModel::class.java,
            this,
            binding.root
        )!!

        dashboardViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DashboardViewModel::class.java,
            this,
            binding.root
        )!!

        if (arguments != null) {
            if (arguments?.getInt(ARG_POSITION) != null) {
                position = arguments?.getInt(ARG_POSITION) ?: 0
                when (position) {
                    0 -> viewModel.awaitingCheckInRequestsVisibility.set(true)
                    else -> {
                        viewModel.awaitingCheckInRequestsVisibility.set(false)
                    }
                }
            }
        }
        binding.lifecycleOwner = this

        binding.dashboardSubViewModel = viewModel
        binding.dashboardViewModel = dashboardViewModel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInAdapter = CheckInAdapter(requireContext(), this, this)
        binding.checkInListRecyclerView.adapter = checkInAdapter

        observers()
    }

    private fun observers() {

        /*viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })*/



        viewModel.autocompleteText.observe(viewLifecycleOwner, Observer {
            viewModel.searchByText(position, it)
        })

        viewModel.awaitingClickEvent.observeChange(viewLifecycleOwner, Observer {
//            Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.AwaitingCheckInsFragment)
        })

        viewModel.addCheckInClickEvent.observeChange(viewLifecycleOwner, Observer {
            val bottomSheet = BottomSheetCheckInDialog(this, false, 0, CheckInDTO())
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        })

        viewModel.awaitingCheckInLiveList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.checkInRequestsTxt.text =
                    resources.getString(R.string.awaiting_requests, it.size)
            }
        })
/*

        viewModel.awaitingCheckInLiveList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                //set the data to recycler view
                viewModel.noDataFoundVisibility.set(true)
            } else {
                viewModel.noDataFoundVisibility.set(false)
            }
            checkInAdapter.tabPosition(position)
//            checkInAdapter.submitList(it)
            checkInAdapter.arrayList = (it)

        })
*/

        viewModel.approvedCheckInLiveList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                //set the data to recycler view
                viewModel.noDataFoundVisibility.set(true)
            } else {
                viewModel.noDataFoundVisibility.set(false)
            }
            checkInAdapter.tabPosition(position)
//            checkInAdapter.submitList(it)
            checkInAdapter.arrayList = (it)

        })
/*

        viewModel.tableManagementListLiveData.observe(requireActivity(), Observer {
            checkInAdapter.setupTable(it)
        })
*/


    }

    companion object {

        const val ARG_POSITION = "position"

        @JvmStatic
        fun getInstance(position: Int): Fragment {
            val fragment = DashboardSubFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
//        viewModel.populateTableListing(position)
        viewModel.callServiceAt(position)
    }


    //On Done Button Click
    override fun onItemClick(checkInDTO: CheckInDTO, action: String) {
        when (action) {

            Constants.ACTION_CHECKEDOUT -> {
                if (checkInDTO.inCustomerRequestId != null) {
                    viewModel.checkOut(
                        checkInDTO.inCheckInCustomerRequestId ?: 0,
                        checkInDTO.inCustomerRequestId ?: 0
                    )
                }
            }
            Constants.ACTION_TAKE_ORDER -> {

                val orderBundleDTO = OrderBundleDTO()
                orderBundleDTO.userId = checkInDTO.userId
                orderBundleDTO.requestType = checkInDTO.requestType
                orderBundleDTO.requestStatus = checkInDTO.status
                orderBundleDTO.inCheckInCustomerRequestId =
                    checkInDTO.inCheckInCustomerRequestId ?: 0
                orderBundleDTO.inCustomerRequestId = checkInDTO.inCustomerRequestId ?: 0
                orderBundleDTO.displayTableNos = checkInDTO.displayTableNos ?: ""


//                viewModel.sharedPreferences.edit().putBoolean(Constants.IS_ORDER_BACK_PRESSED, false).apply()
//                val bundle = bundleOf(Constants.ORDER_DISH_LIST to dishList)
//                val bundle = bundleOf()

                // Write code in bundle
                val intent = Intent(requireActivity(), OrderViewActivity::class.java)

                when (position) {

                    0 -> {   // Check In

                        orderBundleDTO.inCheckInCustomerRequestId = 0

                        when (checkInDTO.status) {

                            RequestStatusEnum.APPROVED.name -> {
                                checkInDTO.userId.let {
                                    val bundle =
                                        bundleOf(Constants.CHECK_IN_USER_ID to checkInDTO.userId)
                                    findNavController().navigate(R.id.MenuFragment, bundle)
                                }

                            }
                            RequestStatusEnum.COMPLETED.name -> {
                                requireActivity().startActivityForResult(intent, 6)
                            }
                        }

                    }
/*

                    1 -> {   // Service Request

                        when (checkInDTO.status) {

                            RequestStatusEnum.PENDING.name -> {
                                findNavController().navigate(R.id.MenuFragment)
                            }

                            RequestStatusEnum.COMPLETED.name -> {
                                requireActivity().startActivityForResult(intent, 6)
                            }
                        }

                    }
*/

                    1 -> {   // Food Order

                        when (checkInDTO.status) {

                            RequestStatusEnum.PENDING.name -> {
                                findNavController().navigate(R.id.MenuFragment)
                            }
                            RequestStatusEnum.AWAITING_CONFIRMATION.name -> {
                                requireActivity().startActivityForResult(intent, 6)
                            }
                            RequestStatusEnum.APPROVED.name -> {
                                requireActivity().startActivityForResult(intent, 6)
                            }
                            RequestStatusEnum.COMPLETED.name -> {
                                requireActivity().startActivityForResult(intent, 6)
                            }
                        }

                    }

                    2 -> {   // Bill Request
                        requireActivity().startActivityForResult(intent, 6)
                    }

                }


                viewModel.sharedPreferences.edit().putString(
                    Constants.ORDER_BUNDLE_DTO,
                    Constants.encrypt(Utils.setOrderBundleDTO(orderBundleDTO))
                ).apply()


            }
            Constants.ACTION_GENERATE_BILL -> {

                var isViewBill = false
                when (position) {
                    2 -> isViewBill = true  // Bill Request
                }
                val bundle = Bundle()
                bundle.putBoolean("isViewBill", isViewBill)
                bundle.putSerializable(Constants.CHECK_IN_DTO, checkInDTO)
                findNavController().navigate(R.id.billGenerationFragment, bundle)
            }
            Constants.ACTION_RE_ASSIGN -> {
                if (!checkInDTO.tableIdsAlloted.isNullOrEmpty()) {
                    val bottomSheet = BottomSheetCheckInDialog(
                        this, true,
                        0, CheckInDTO()
                    )
                    bottomSheet.show(childFragmentManager, "ModalBottomSheet")
                } else {
                    viewModel.getmSnackbar().value = "Table not assigned"
                }
            }
/*

            Constants.ACTION_CONFIRM_CHECKIN -> {
                val bottomSheet = BottomSheetCheckInDialog(
                    this, true,
                     0,
                    checkInDTO
                )
                bottomSheet.show(childFragmentManager, "ModalBottomSheet")
            }
*/


        }
    }


    override fun onDoneClick(position: Int, dto: CheckInDTO) {

        if (dto.inCustomerRequestId != null) {
            viewModel.changeDineInStatus(
                dto.inCustomerRequestId ?: 0,
                RequestStatusEnum.COMPLETED.name
            )
        }
    }

    override fun bottomSheetDialogDismiss(checkInDTO: CheckInDTO) {
        viewModel.callServiceAt(0)
    }

}
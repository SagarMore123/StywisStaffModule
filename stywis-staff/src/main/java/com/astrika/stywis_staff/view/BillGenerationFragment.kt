package com.astrika.stywis_staff.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.menu.BillGenerationOrderAdapter
import com.astrika.stywis_staff.databinding.FragmentBillGenerationBinding
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.models.OutletDiscountDetailsDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.menus.ApplicableDiscountsActivity
import com.astrika.stywis_staff.view.menus.viewmodels.BillGenerationViewModel
import java.math.BigDecimal


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BillGenerationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BillGenerationFragment : Fragment() {

    lateinit var binding: FragmentBillGenerationBinding
    lateinit var viewModel: BillGenerationViewModel
    private var progressBar = CustomProgressBar()
    private lateinit var billGenerationOrderAdapter: BillGenerationOrderAdapter
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bill_generation,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            BillGenerationViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.bigDecimal = BigDecimal(0)
        binding.lifecycleOwner = this

        billGenerationOrderAdapter = BillGenerationOrderAdapter(requireContext())
        binding.billGenerationOrderReviewRecycler.adapter = billGenerationOrderAdapter


        viewModel.onApplicableDiscountsClick.observeChange(requireActivity(), Observer {
            val requestCode = Constants.APPLICABLE_DISCOUNT_CODE
            val intent = Intent(requireActivity(), ApplicableDiscountsActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)
        })

        if (requireArguments() != null && requireArguments().containsKey(Constants.CHECK_IN_DTO)) {
            viewModel.checkInDTO =
                requireArguments().getSerializable(Constants.CHECK_IN_DTO) as CheckInDTO

            if (requireArguments().getBoolean("isViewBill")) {
                viewModel.getBillDetails()
            } else {
                viewModel.generateBill()
            }
        }

        observer()

        return binding.root
    }

    private fun observer() {

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.checkOutSuccess.observeChange(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_billGenerationFragment_to_DashboardFragment)
        })

        viewModel.orderMutableLiveDataList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                billGenerationOrderAdapter.arrayList = it
            }
        })

        viewModel.isConfirm.observe(requireActivity(), Observer {
            if (it) {
                // Write code here for payment done successfully
                viewModel.changeBillDetailsStatusToPaid()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.APPLICABLE_DISCOUNT_CODE) {

            val outletDiscountDetailsDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as OutletDiscountDetailsDTO

            if (outletDiscountDetailsDTO != null) {
                viewModel.displayBillDetails(outletDiscountDetailsDTO)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BillGenerationFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BillGenerationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
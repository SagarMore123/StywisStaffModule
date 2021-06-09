package com.astrika.stywis_staff.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.applicablediscount.ApplicableDiscountsAdapter
import com.astrika.stywis_staff.databinding.FragmentApplicableDiscountBinding
import com.astrika.stywis_staff.models.OutletDiscountDetailsDTO
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.ApplicableDiscountViewModel


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApplicableDiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplicableDiscountFragment : Fragment(), ApplicableDiscountsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentApplicableDiscountBinding
    private lateinit var viewModel: ApplicableDiscountViewModel
    lateinit var applicableDiscountsAdapter: ApplicableDiscountsAdapter
    private var progressBar = CustomProgressBar()

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
            inflater,
            R.layout.fragment_applicable_discount,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ApplicableDiscountViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        applicableDiscountsAdapter = ApplicableDiscountsAdapter(requireActivity(), this, false)
        binding.applicableDiscountsReviewRecycler.adapter = applicableDiscountsAdapter

        viewModel.applicableDiscountMutableLiveArrayList.observe(requireActivity(), Observer {
            applicableDiscountsAdapter.list = it
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApplicableDiscountFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicableDiscountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(position: Int, dto: OutletDiscountDetailsDTO) {

    }
}
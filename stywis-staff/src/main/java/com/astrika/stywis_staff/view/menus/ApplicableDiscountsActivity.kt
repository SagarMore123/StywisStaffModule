package com.astrika.stywis_staff.view.menus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.applicablediscount.ApplicableDiscountsAdapter
import com.astrika.stywis_staff.databinding.ActivityApplicableDiscountBinding
import com.astrika.stywis_staff.models.OutletDiscountDetailsDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.ApplicableDiscountViewModel

class ApplicableDiscountsActivity : AppCompatActivity(),
    ApplicableDiscountsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityApplicableDiscountBinding
    lateinit var viewModel: ApplicableDiscountViewModel
    lateinit var applicableDiscountsAdapter: ApplicableDiscountsAdapter
    private var progressBar = CustomProgressBar()
    private var resultCode = 0
    private var isViewDiscount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_applicable_discount)
        resultCode = intent.getIntExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, 0)
        isViewDiscount = intent.getBooleanExtra(Constants.IS_VIEW_DISCOUNT, false)

        viewModel = Utils.obtainBaseObservable(
            this,
            ApplicableDiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setAdapter()
        setObserver()
    }

    private fun setAdapter() {
        applicableDiscountsAdapter = ApplicableDiscountsAdapter(this, this, isViewDiscount)
        binding.applicableDiscountsReviewRecycler.adapter = applicableDiscountsAdapter
    }

    private fun setObserver() {

        viewModel.showProgressBar.observe(this, Observer {
            if (it)
                progressBar.show(this, "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.applicableDiscountMutableLiveArrayList.observe(this, Observer {
            applicableDiscountsAdapter.list = it
        })
    }


    override fun onItemClick(position: Int, dto: OutletDiscountDetailsDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, dto)
        setResult(resultCode, resultIntent)
        finish()
    }
}
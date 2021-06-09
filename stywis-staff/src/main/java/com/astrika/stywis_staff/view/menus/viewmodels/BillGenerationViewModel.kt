package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.SingleLiveEvent
import com.astrika.stywis_staff.utils.Utils
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class BillGenerationViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var checkOutSuccess = SingleLiveEvent<Void>()
    var isConfirm = MutableLiveData<Boolean>()
    var isPaymentDone = MutableLiveData<Boolean>(false)
    var orderMutableLiveDataList = MutableLiveData<ArrayList<OrderDTO>>()

    var quantityPriceMutable = MutableLiveData<String>("")
    var appliedDiscountString = MutableLiveData<String>("Apply Discounts and Complementary")
    var isAppliedDiscount = MutableLiveData(false)

    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var userId = 0L
    var outletId = 0L
    var inCustomerRequestId = 0L
    var tableId = 1L
    var totalOrderItems = 0
    var taxPercentage = 5
    var totalOrderPrice = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    var discountPrice = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    var taxAndCharges = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    var toPayableAmount = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    var billGenerationDTO = BillGenerationDTO()
    var checkInDTO = CheckInDTO()
    var orderBundleDTO = OrderBundleDTO()
    var displayTableNos = MutableLiveData("")

    var onApplicableDiscountsClick = SingleLiveEvent<Void>()


    init {

        outletId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        orderBundleDTO = Utils.getOrderBundleDTO(
            Constants.decrypt(
                sharedPreferences.getString(
                    Constants.ORDER_BUNDLE_DTO,
                    ""
                )
            )
        ) ?: OrderBundleDTO()
        displayTableNos.value = orderBundleDTO.displayTableNos

//        populateData()

    }

    fun generateBill() {

        showProgressBar.value = true

        dashboardRepository.generateBill(checkInDTO.userId ?: 0,
            checkInDTO.outletId ?: 0,
            checkInDTO.inCustomerRequestId ?: 0,
            checkInDTO.inCheckInCustomerRequestId ?: 0,
            object : IDataSourceCallback<BillGenerationDTO> {
                override fun onDataFound(data: BillGenerationDTO) {
                    setPayableAmt(data)
                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    fun getBillDetails() {

        showProgressBar.value = true

        dashboardRepository.getBillDetails(checkInDTO.inCheckInCustomerRequestId ?: 0,
            object : IDataSourceCallback<BillGenerationDTO> {
                override fun onDataFound(data: BillGenerationDTO) {
                    setPayableAmt(data)
                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    fun changeBillDetailsStatusToPaid() {

        showProgressBar.value = true

        dashboardRepository.changeBillDetailsStatusToPaid(checkInDTO.inCheckInCustomerRequestId
            ?: 0,
            object : IDataSourceCallback<String> {
                override fun onDataFound(data: String) {
//                    setPayableAmt(data)
                    isPaymentDone.value = true
                    getmSnackbar().value = data
                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    private fun setPayableAmt(data: BillGenerationDTO) {
        billGenerationDTO = data

        isPaymentDone.value = data.paid ?: false

        orderMutableLiveDataList.value = data.orderDTO ?: arrayListOf()
        taxAndCharges.value =
            data.taxAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
        totalOrderPrice.value =
            data.netAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
        toPayableAmount.value =
            data.totalAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
        discountPrice.value = data.discountAmount?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
            ?: BigDecimal.ZERO
    }

    fun onApplicableDiscountsClick() {
        onApplicableDiscountsClick.call()
    }

    fun onRemoveAppliedCouponClick() {
        isAppliedDiscount.value = false
        appliedDiscountString.value =
            activity.resources.getString(R.string.apply_discount_and_complementary)
        toPayableAmount.value = toPayableAmount.value?.plus(discountPrice.value ?: BigDecimal.ZERO)
            ?.setScale(2, RoundingMode.HALF_UP)
        discountPrice.value = BigDecimal.ZERO

    }

    fun onPaymentModeClick() {

        Constants.showCommonDialog(
            activity,
            isConfirm,
            activity.resources.getString(R.string.is_payment_done)
        )
    }

    fun onCheckOutClick() {
        showProgressBar.value = true
        dashboardRepository.checkOut(
            checkInDTO.inCheckInCustomerRequestId ?: 0,
            checkInDTO.inCustomerRequestId ?: 0,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    Thread.sleep(1000)
                    checkOutSuccess.call()
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error

                }
            })
    }


    fun displayBillDetails(outletDiscountDetailsDTO: OutletDiscountDetailsDTO) {
        isAppliedDiscount.value = true
        appliedDiscountString.value = outletDiscountDetailsDTO.outletDiscountCategory

        val discountPercent = outletDiscountDetailsDTO.discountTimingList[0].discountApplicable
        discountPrice.value =
            toPayableAmount.value?.times(discountPercent.toBigDecimal())?.div(BigDecimal(100))
                ?.setScale(2, RoundingMode.HALF_UP)
        toPayableAmount.value = toPayableAmount.value?.minus(discountPrice.value ?: BigDecimal.ZERO)
            ?.setScale(2, RoundingMode.HALF_UP)
//        toPayableAmount.value = (payAmt?.times(100) ?: 0.0).roundToLong() / 100.0

    }

}
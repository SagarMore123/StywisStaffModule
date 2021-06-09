package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.RejectCheckinRemarkPopupLayoutBinding
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.CheckInDTO
import com.astrika.stywis_staff.models.GPinDTO
import com.astrika.stywis_staff.models.SystemValueMasterDTO
import com.astrika.stywis_staff.models.TableManagementDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog


class DashboardSubViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var awaitingCheckInArrayList = ArrayList<CheckInDTO>()
    var awaitingCheckInLiveList = MutableLiveData<ArrayList<CheckInDTO>>()
    var approvedCheckInArrayList = ArrayList<CheckInDTO>()
    var approvedCheckInLiveList = MutableLiveData<ArrayList<CheckInDTO>>()
    var awaitingCheckInRequestsVisibility = ObservableBoolean(false)
    var noDataFound = MutableLiveData<String>("")
    var noDataFoundVisibility = ObservableBoolean(false)
    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var userId = 0L
    var outletId = 0L
    var requestTypes = RequestTypeEnum.CHECK_IN.name
    var awaitingClickEvent = SingleLiveEvent<Void>()
    var addCheckInClickEvent = SingleLiveEvent<Void>()
    var checkInApproved = MutableLiveData(false)
    var isGPinVerified = MutableLiveData(false)
    var isShowMyTables = false
    var autocompleteText = MutableLiveData("")

    var unOccupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var unOccupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    //    var occupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
//    var occupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var vacantTableArrayList = ArrayList<SystemValueMasterDTO>()
    var vacantTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var occupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var occupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var reservedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var reservedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()


    var tableManagementListLiveData = MutableLiveData<ArrayList<TableManagementDTO>>()

    init {
        userId =
            Constants.decrypt(sharedPreferences.getString(Constants.LOGIN_USER_ID, ""))?.toLong()
                ?: 0L
        outletId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        isShowMyTables =
            Constants.decrypt(sharedPreferences.getString(Constants.IS_MY_TABLES, ""))?.toBoolean()
                ?: false
//        populateMasters()
//        populateTableListing()
    }

/*

    private fun populateMasters() {
        //fetch vacant green seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_VACANT,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    vacantTableArrayList.clear()
                    vacantTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    vacantTableArrayListMutableLiveData.value = vacantTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        //fetch occupied blue seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_OCCUPIED,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    occupiedTableArrayList.clear()
                    occupiedTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    occupiedTableArrayListMutableLiveData.value = occupiedTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        //fetch reserved red seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_RESERVED,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    reservedTableArrayList.clear()
                    reservedTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    reservedTableArrayListMutableLiveData.value = reservedTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })
    }
*/

/*

    fun populateTableListing(position: Int) {
        showProgressBar.value = true
        val commonListingDTO = CommonListingDTO()
        val defaultSort = CommonSortDTO()
        defaultSort.sortField = Constants.TABLE_ID_COLUMN
        defaultSort.sortOrder = Constants.SORT_ORDER_ASC
        commonListingDTO.defaultSort = defaultSort
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        val commonSearchDTOList = ArrayList<CommonSearchDTO>()
        val commonSearchDTO = CommonSearchDTO()
        outletId.let {
            commonSearchDTO.search = it.toString()
        }
        commonSearchDTO.searchCol = Constants.OUTLET_ID_COLUMN
        commonSearchDTOList.add(commonSearchDTO)
        commonListingDTO.search = commonSearchDTOList

        dashboardRepository.getTableListing(commonListingDTO,
            object : IDataSourceCallback<ArrayList<TableManagementDTO>> {

                override fun onDataFound(data: ArrayList<TableManagementDTO>) {
                    showProgressBar.value = false
                    tableManagementListLiveData.value = data
                    callServiceAt(position)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                    callServiceAt(position)
                }
            })
    }
*/

    fun fetchDashboardListing(requestType: String, requestStatuses: ArrayList<String>) {

        showProgressBar.value = true
        dashboardRepository.fetchCheckInListing(
            outletId = outletId,
            requestTypes = requestType,
            requestStatuses = requestStatuses,
            callback = object : IDataSourceCallback<ArrayList<CheckInDTO>> {

                override fun onDataFound(data: ArrayList<CheckInDTO>) {
                    noDataFoundVisibility.set(false)
                    var arrayList = arrayListOf<CheckInDTO>()
                    if (isShowMyTables) {
                        for (checkInDTO in data) {
                            if (userId == checkInDTO.staffId) {
                                arrayList.add(checkInDTO)
                            }
                        }

                    } else {
                        arrayList = data
                    }

/*

                    if (!tableManagementListLiveData.value.isNullOrEmpty()) {
                        for (checkInDTO in arrayList) {

                            if (checkInDTO.tableIdsAlloted.isNullOrEmpty()) {
                                continue
                            }

                            var tables = ""
                            for (tablesNo in tableManagementListLiveData.value ?: arrayListOf()) {

                                if (checkInDTO.tableIdsAlloted?.contains(tablesNo.tableId?.toInt()) == true
                                ) {
                                    tables += tablesNo.tableCode.toString()
                                }

                            }
                            checkInDTO.displayTableNos = tables
                        }
                    }
*/

                    if (requestType == RequestTypeEnum.CHECK_IN.name) {
                        if (requestStatuses[0] == RequestStatusEnum.PENDING.name) {
                            awaitingCheckInArrayList = arrayList
                            awaitingCheckInLiveList.value = arrayList
                        } else {
                            approvedCheckInArrayList = arrayList
                            approvedCheckInLiveList.value = arrayList
                        }
                    } else {
                        approvedCheckInArrayList = arrayList
                        approvedCheckInLiveList.value = arrayList
                    }

                    if (requestType == RequestTypeEnum.CHECK_IN.name && requestStatuses.contains(
                            RequestStatusEnum.APPROVED.name
                        )
                    ) {
                        fetchDashboardListing(
                            RequestTypeEnum.CHECK_IN.name,
                            arrayListOf(RequestStatusEnum.PENDING.name)
                        )   // PENDING (Awaiting CheckIn list)
                    }

                    checkInApproved.value = true

                    showProgressBar.value = false

                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                    noDataFoundVisibility.set(true)
                    setNoDataFoundMessage(requestType)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                    noDataFoundVisibility.set(true)
                    setNoDataFoundMessage(requestType)
                }
            })
    }

    fun onAwaitingRequests() {
        awaitingClickEvent.call()
    }

    fun onAddCheckIn() {
        addCheckInClickEvent.call()
//        openAddCheckInDialog()
    }

    private fun openAddCheckInDialog() {
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(R.layout.bottom_sheet_add_check_in_dialog)
        dialog.setCancelable(true)
        dialog.show()
    }


    fun approveCheckIn(inCustomerRequestId: Long?) {
        showProgressBar.value = true
        inCustomerRequestId?.let { id ->
            dashboardRepository.changeStatusApproved(id, object : IDataSourceCallback<String> {

                override fun onSuccess(message: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = message
                    checkInApproved.value = true
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error

                }
            })
        }
    }

    fun changeDineInStatus(inCustomerRequestId: Long, status: String) {
        showProgressBar.value = true
        dashboardRepository.changeDineInStatus(
            inCustomerRequestId,
            status,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    approvedCheckInLiveList.value?.clear()
                    callServiceAt(tabPosition)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error

                }
            })

    }


    fun checkOut(inCheckInCustomerRequestId: Long, inCustomerRequestId: Long) {
        showProgressBar.value = true
        dashboardRepository.checkOut(
            inCheckInCustomerRequestId,
            inCustomerRequestId,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    approvedCheckInLiveList.value?.clear()
                    callServiceAt(tabPosition)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error

                }
            })

    }

    fun rejectCheckIn(checkInDTO: CheckInDTO) {
        val rejectCheckinRemarkPopupLayoutBinding: RejectCheckinRemarkPopupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.reject_checkin_remark_popup_layout,
                null,
                false
            )
        val view: View = rejectCheckinRemarkPopupLayoutBinding.root
        val alert =
            AlertDialog.Builder(activity)
        // this is set the view from XML inside AlertDialog
        alert.setView(view)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()

//        rejectCheckinRemarkPopupLayoutBinding.titleDescription.text = message
        rejectCheckinRemarkPopupLayoutBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        rejectCheckinRemarkPopupLayoutBinding.confirmBtn.setOnClickListener {

            if (rejectCheckinRemarkPopupLayoutBinding.remarkEt.text.isNullOrBlank()) {
                rejectCheckinRemarkPopupLayoutBinding.remarkErrorTxt.text =
                    "Please enter remark"
            } else {
                checkInDTO.rejectedRemarks =
                    rejectCheckinRemarkPopupLayoutBinding.remarkEt.text.toString()

//                showProgressBar.value = true
                checkInDTO.inCustomerRequestId?.let { id ->
                    checkInDTO.rejectedRemarks?.let { string ->
                        dashboardRepository.changeStatusRejected(
                            id,
                            string,
                            object : IDataSourceCallback<String> {

                                override fun onDataFound(data: String) {
//                                    showProgressBar.value = false
                                    getmSnackbar().value = data

                                    fetchDashboardListing(
                                        RequestTypeEnum.CHECK_IN.name,
                                        arrayListOf(RequestStatusEnum.PENDING.name)
                                    )   // PENDING (Awaiting CheckIn list)

                                    dialog.dismiss()
                                }

                                override fun onError(error: String) {
                                    showProgressBar.value = false
                                    getmSnackbar().value = error

                                }
                            })
                    }
                }


            }

        }



        rejectCheckinRemarkPopupLayoutBinding.remarkEt.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                rejectCheckinRemarkPopupLayoutBinding.remarkErrorTxt.text = ""
            }

        })



        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun verifyGPin(checkInDTO: CheckInDTO) {
        showProgressBar.value = true

        val gPinDTO = GPinDTO()
        gPinDTO.inCustomerRequestId = checkInDTO.inCustomerRequestId
        gPinDTO.uniquePin = checkInDTO.uniquePin

        dashboardRepository.verifyGPin(gPinDTO, object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                showProgressBar.value = false
                getmSnackbar().value = data
                isGPinVerified.value = true
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error

            }
        })
    }

    fun confirmTableCheckIn(checkInDTO: CheckInDTO) {
        showProgressBar.value = true
        checkInDTO.staffId =
            Constants.decrypt(sharedPreferences.getString(Constants.LOGIN_USER_ID, ""))?.toLong()
                ?: 0
        dashboardRepository.checkInVisitor(checkInDTO, object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                showProgressBar.value = false
                getmSnackbar().value = data
                fetchDashboardListing(
                    RequestTypeEnum.CHECK_IN.name,
                    arrayListOf(RequestStatusEnum.PENDING.name)
                )
//                checkInApproved.value = true
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error

            }
        })
    }

    var tabPosition = 0
    fun callServiceAt(position: Int) {

        tabPosition = position
        val requestStatusArrayList = arrayListOf(RequestStatusEnum.PENDING.name) // Request Statuses

        when (position) {

            0 -> {  // CheckIn

/*
                fetchDashboardListing(
                    RequestTypeEnum.CHECK_IN.name,
                    requestStatusArrayList
                )   // PENDING (Awaiting CheckIn list)
*/

                fetchDashboardListing(
                    RequestTypeEnum.CHECK_IN.name,
                    arrayListOf(
                        RequestStatusEnum.APPROVED.name,
                        RequestStatusEnum.AWAITING_CONFIRMATION.name,
                        RequestStatusEnum.COMPLETED.name
                    )
                ) // APPROVED (CheckIn list)

                setNoDataFoundMessage(RequestTypeEnum.CHECK_IN.name)
            }

/*

            1 -> {  // Service Request

                requestStatusArrayList.add(RequestStatusEnum.COMPLETED.name)
                fetchDashboardListing(
                    RequestTypeEnum.CALL_FOR_SERVICE.name,
                    requestStatusArrayList
                ) // PENDING & COMPLETED

                setNoDataFoundMessage(RequestTypeEnum.CALL_FOR_SERVICE.name)
            }
*/

            1 -> {  // Food Order
                requestStatusArrayList.add(RequestStatusEnum.AWAITING_CONFIRMATION.name)
                requestStatusArrayList.add(RequestStatusEnum.APPROVED.name)
                requestStatusArrayList.add(RequestStatusEnum.COMPLETED.name)

                fetchDashboardListing(
                    RequestTypeEnum.CALL_FOR_ORDER.name,
                    requestStatusArrayList
                )  // PENDING , AWAITING_CONFIRMATION, APPROVED, COMPLETED

                setNoDataFoundMessage(RequestTypeEnum.CALL_FOR_ORDER.name)
            }
            2 -> {
                requestStatusArrayList.add(RequestStatusEnum.AWAITING_CONFIRMATION.name)
                requestStatusArrayList.add(RequestStatusEnum.APPROVED.name)
//                requestStatusArrayList.add(RequestStatusEnum.COMPLETED.name)

                fetchDashboardListing(
                    RequestTypeEnum.CALL_FOR_BILL.name,
                    requestStatusArrayList
                )  // PENDING , AWAITING_CONFIRMATION, APPROVED, COMPLETED
                setNoDataFoundMessage(RequestTypeEnum.CALL_FOR_BILL.name)
            }

        }
    }

    fun setNoDataFoundMessage(requestType: String) {

        when (requestType) {

            RequestTypeEnum.CHECK_IN.name -> {
                noDataFound.value = "No CheckIns Found"
            }
            RequestTypeEnum.CALL_FOR_SERVICE.name -> {
                noDataFound.value = "No Server Calls Found"
            }
            RequestTypeEnum.CALL_FOR_ORDER.name -> {
                noDataFound.value = "No Orders Found"
            }
            RequestTypeEnum.CALL_FOR_BILL.name -> {
                noDataFound.value = "No Bill Requests Found"
            }

        }
    }


    fun searchByText(position: Int, searchQuery: String?) {

        if (!searchQuery.isNullOrEmpty()) {
            val searchedDataList = ArrayList<CheckInDTO>()
            for (item in approvedCheckInArrayList) {
                if ((item.userName?.contains(searchQuery, true) == true)) {
                    searchedDataList.add(item)
                }
            }

            setNoDataFoundMessage(RequestTypeEnum.CHECK_IN.name)
            approvedCheckInLiveList.value = searchedDataList

        } else {
//                    noResultsFoundVisible.set(false)
            approvedCheckInLiveList.value = approvedCheckInArrayList
        }

    }

}
package com.astrika.stywis_staff.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.master_controller.source.daos.SystemValueMasterDao
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.*
import java.util.concurrent.TimeUnit


class BottomSheetCheckInViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository


) : GenericBaseObservable(application, owner, view) {

    lateinit var tableSetupLayout: RelativeLayout
    var tableManagementList = ArrayList<TableManagementDTO>()

    var tableSetupLayoutWidth = 0
    var tableSetupLayoutHeight = 0

    var showProgressBar = MutableLiveData<Boolean>()
    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    //find by number layout visible by default
    var viewType = MutableLiveData<Int>(1)
    var mobileNumber = MutableLiveData<String>("")
    var mobileNumberError = MutableLiveData<String>("")
    var noOfGuest = MutableLiveData<String>("")
    var noOfGuestError = MutableLiveData<String>("")
    var visitPurposeId = 0L
    var visitPurpose = MutableLiveData<String>("")
    var visitPurposeError = MutableLiveData<String>("")
    var isCheckInApproval = MutableLiveData(false)
    var userDetailsVisible = ObservableBoolean(false)
    var visitorFirstName = MutableLiveData<String>("")
    var visitorFirstNameError = MutableLiveData<String>("")
    var visitorLastName = MutableLiveData<String>("")
    var visitorLastNameError = MutableLiveData<String>("")
    var visitorEmail = MutableLiveData<String>("")
    var visitorEmailError = MutableLiveData<String>("")
    var visitorMobileNumber = MutableLiveData<String>("")
    var visitorMobileNumberError = MutableLiveData<String>("")
    var visitorLoginId = MutableLiveData<String>("")
    lateinit var error: String
    val profileImagePath = ObservableField<String>()
    lateinit var userDetailsDTO: UserDetailsDTO
    lateinit var visitorResponseDTO: VisitorResponseDTO
    var countDownTimer = MutableLiveData("")
    var cancelClickEvent = SingleLiveEvent<Void>()
    var verifyOtpTimerVisibility = MutableLiveData<Boolean>(true)
    var verifyOtpEditTxt = MutableLiveData("")
    var verifyOtpError = MutableLiveData<String>()
    var verifyOtpErrorVisibility = MutableLiveData<Boolean>()
    var resendOtpClicked = MutableLiveData<Boolean>(false)
    var outLetId: Long? = 0
    var userId: Long? = 0
    var checkInDTO = CheckInDTO()

    val onVisitorPurposeClickEvent = SingleLiveEvent<Void>()

    var tableManagementListLiveData = MutableLiveData<ArrayList<TableManagementDTO>>()
//    var unOccupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
//    var unOccupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    var vacantTableArrayList = ArrayList<SystemValueMasterDTO>()
    var vacantTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var occupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var occupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var reservedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var reservedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    //    var occupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
//    var occupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var tableSelected = false
    var tableIdsAlloted = arrayListOf<Int>()
    var isSelectUser = false
    var isReAssignTable = false
    var tableIdOld = 0

    init {
        outLetId = Constants.getOutletId(application)
        populateMasters()
//        populateTableListing()
    }

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

    fun onFindBtn() {
        //validate the mobile number
        if (validateMobileNumber()) {
            //service call for find by number
            showProgressBar.value = true
            mobileNumber.value.let {
                if (!it.isNullOrEmpty()) {
                    dashboardRepository.getUserByMobile(it, object : IDataSourceCallback<UserDTO> {

                        override fun onDataFound(data: UserDTO) {
                            showProgressBar.value = false
                            //set the data to the ui and make the ui visible
                            userDetailsVisible.set(true)
                            setUserDetails(data)
                        }

                        override fun onDataNotFound() {
                            //Add new customer layout visible
                            showProgressBar.value = false
                            viewType.value = 3
                            clearFields()
                        }

                        override fun onError(error: String) {
                            showProgressBar.value = false
                            Constants.showToastMessage(activity, error)
                        }
                    })

                }
            }
        }
    }

    private fun clearFields() {
        // auto populating the mobile number
        visitorMobileNumber.value = mobileNumber.value
        visitorEmail.value = ""
        visitorFirstName.value = ""
        visitorLastName.value = ""
    }

    fun userDetailsClick() {
        //load the customer view ui and hide the find by mobile layout
        if (userDetailsVisible.get()) {
            viewType.value = 2
        }
    }

    fun onSelectUser() {
        //assign table to the user first and then check in
        viewType.value = 5
        isSelectUser = true
    }


    // Visit Purpose Click
    fun onVisitorPurposeClick() {
        onVisitorPurposeClickEvent.call()
    }

    fun onSubmitDetails() {
        //assign table to the user first and then check in

        var isValid = true
        if (noOfGuest.value?.toString()?.trim()?.isBlank() == true) {
            noOfGuestError.value = "Please enter no. of people"
            isValid = false
        } else {
            noOfGuestError.value = ""
        }

        if (visitPurpose.value?.toString()?.trim()?.isBlank() == true) {
            visitPurposeError.value = "Please enter visit purpose"
            isValid = false
        } else {
            visitPurposeError.value = ""
        }

        if (isValid) {
//            viewType.value = 6
            checkInVisitorServiceCall(isSelectUser)

        }
    }
/*

    fun onConfirmCheckIn() {
        //approve check in for the visitor here
        if (isReAssignTable) {
            reAssignTable()
        } else {
            checkInVisitorServiceCall(isSelectUser)
        }
    }
*/

    fun onCancel() {
        cancelClickEvent.call()
    }

    fun onSave() {
        //validate the visitor fields before signing up the user
        if (validateVisitorFields()) {
            //save service call visitor
            showProgressBar.value = true
            dashboardRepository.saveVisitorDetails(
                userDetailsDTO,
                object : IDataSourceCallback<VisitorResponseDTO> {

                    override fun onDataFound(data: VisitorResponseDTO) {
                        showProgressBar.value = false
                        visitorResponseDTO = data
                        userId = data.userId
                        viewType.value = 4
                        onVerifyOtpLayout()
                    }


                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })
        }
    }

    private fun onVerifyOtpLayout() {
        if (viewType.value == 4) {
            visitorLoginId.value = visitorResponseDTO.loginId
            showOtpMessage()
        }
    }

    private fun showOtpMessage() {
        val timer = visitorResponseDTO.otpExpireTime
        startTimer(timer)
        verifyOtpTimerVisibility.value = true

        if (visitorResponseDTO.otp != null) {
//            val message = visitorResponseDTO.success
            Constants.showToastMessage(activity, "Otp is ${visitorResponseDTO.otp}")
        }
    }

    private fun validateVisitorFields(): Boolean {
        var isValid: Boolean = true

        userDetailsDTO = UserDetailsDTO()

        if (validateFirstName()) {
            userDetailsDTO.userFirstName = visitorFirstName.value
        } else {
            isValid = false
        }

        if (validateLastName()) {
            userDetailsDTO.userLastName = visitorLastName.value
        } else {
            isValid = false
        }

        if (validateVisitorMobileNumber()) {
            userDetailsDTO.mobileNo = visitorMobileNumber.value
        } else {
            isValid = false
        }

        if (validateEmail()) {
            userDetailsDTO.emailAddress = visitorEmail.value
        } else {
            isValid = false
        }
        return isValid
    }

    private fun setUserDetails(userDTO: UserDTO) {
        if (!userDTO.userFirstName.isNullOrEmpty()) {
            visitorFirstName.value = userDTO.userFirstName
        }
        if (!userDTO.userLastName.isNullOrEmpty()) {
            visitorLastName.value = userDTO.userLastName
        }
        if (!userDTO.mobileNo.isNullOrEmpty()) {
            visitorMobileNumber.value = userDTO.mobileNo
        }
        if (!userDTO.emailAddress.isNullOrEmpty()) {
            visitorEmail.value = userDTO.emailAddress
        }
        profileImagePath.set(userDTO.profileImage?.path)

    }

    fun resendOtp() {
        resendOtpServiceCall()
    }

    fun onVerifyOtpProceed() {
        verifyOtpValidate()
    }

    private fun verifyOtpValidate() {
        if (validateOtp()) {
            val visitorMobileNo = visitorMobileNumber.value ?: ""
            val otp = verifyOtpEditTxt.value ?: ""
            verifyOtpServiceCall(visitorMobileNo, otp)
        }
    }

    private fun verifyOtpServiceCall(visitorMobileNo: String, otp: String) {
        showProgressBar.value = true
        dashboardRepository.verifyOtpVisitor(
            visitorMobileNo, otp, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    //checkin visitor service call
                    //first assign the table to the user and then checkin
                    if (isCheckInApproval.value == true) {
                        cancelClickEvent.call()
                    } else {
                        viewType.value = 5
                        isSelectUser = false
                    }
                }

                override fun onError(error: String) {
                    if (error.contains("blocked")) {
                        verifyOtpTimerVisibility.value = false
                        //                    verifyOtpLoginIdError.value = error
                    }
                    verifyOtpEditTxt.value = ""
                    showProgressBar.value = false
                    Constants.showToastMessage(activity, error)
                }
            })

    }

    private fun resendOtpServiceCall() {
        val visitorMobileNo = visitorMobileNumber.value ?: ""
        if (visitorMobileNumber.value != null) {
            dashboardRepository.sendOtpVisitor(
                visitorMobileNo,
                object : IDataSourceCallback<VisitorResponseDTO> {

                    override fun onDataFound(data: VisitorResponseDTO) {
                        showProgressBar.value = false
                        //checkin visitor service call
//                    checkInVisitorServiceCall(false)
                        visitorResponseDTO = data
                        showOtpMessage()
//                    Constants.showToastMessage(activity,"Otp is ${data}")
                    }

                    override fun onError(error: String) {
                        if (error.contains("blocked")) {
                            verifyOtpTimerVisibility.value = false
                            //                    verifyOtpLoginIdError.value = error
                        }
                        verifyOtpEditTxt.value = ""
                        showProgressBar.value = false
                        Constants.showToastMessage(activity, error)
                    }
                })
        }
    }

    private fun checkInVisitorServiceCall(selectUser: Boolean) {
        val checkInDTO = checkInDTO
        checkInDTO.userName = "${visitorFirstName.value} ${visitorLastName.value}"
        checkInDTO.outletId = outLetId
        checkInDTO.requestType = RequestTypeEnum.CHECK_IN.name
//        checkInDTO.status = RequestStatusEnum.PENDING.name
        checkInDTO.status = RequestStatusEnum.APPROVED.name
        checkInDTO.tableIdsAlloted = tableIdsAlloted
        checkInDTO.userGuestCount = noOfGuest.value?.toLong()
        checkInDTO.mobileNo = visitorMobileNumber.value
        checkInDTO.visitPurposeId = visitPurposeId
        checkInDTO.visitPurpose = visitPurpose.value ?: ""
        checkInDTO.staffId =
            Constants.decrypt(sharedPreferences.getString(Constants.LOGIN_USER_ID, ""))?.toLong()
                ?: 0
        if (selectUser) {
            checkInDTO.userId = Constants.userId
        } else {
            checkInDTO.userId = userId
        }

        dashboardRepository.checkInVisitor(checkInDTO, object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                Constants.showToastMessage(activity, data)
                cancelClickEvent.call()
            }

            override fun onError(error: String) {
                Constants.showToastMessage(activity, error)
            }
        })
    }

    private fun startTimer(timer: Int) {
        otpVerificationTimer = timer.toLong()

        if (otpCountDownTimer != null) {
            otpCountDownTimer?.cancel()
        }

        otpCountDownTimer = object : CountDownTimer((timer * 1000).toLong(), 1000) {
            override fun onTick(millis: Long) {
                otpVerificationTimer--
                val minuteSeconds = String.format(
                    "%02d:%02d",
                    (TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millis)
                    ))
                )
                countDownTimer.value = minuteSeconds
            }

            override fun onFinish() {
                verifyOtpTimerVisibility.value = false
            }

        }.start()


    }

    /*

        fun populateTableListing() {
            showProgressBar.value = true
            val commonListingDTO = CommonListingDTO()
            val defaultSort = CommonSortDTO()
            defaultSort.sortField = Constants.TABLE_ID_COLUMN
            defaultSort.sortOrder = Constants.SORT_ORDER_ASC
            commonListingDTO.defaultSort = defaultSort
            commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
            val commonSearchDTOList = ArrayList<CommonSearchDTO>()
            val commonSearchDTO = CommonSearchDTO()
            commonSearchDTO.search = outLetId.toString()
            commonSearchDTO.searchCol = Constants.OUTLET_ID_COLUMN
            commonSearchDTOList.add(commonSearchDTO)
            commonListingDTO.search = commonSearchDTOList

            dashboardRepository.getTableListing(commonListingDTO,
                object : IDataSourceCallback<ArrayList<TableManagementDTO>> {

                    override fun onDataFound(data: ArrayList<TableManagementDTO>) {
                        showProgressBar.value = false
                        tableManagementList = data
                        tableManagementListLiveData.value = tableManagementList
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })
        }


        private fun reAssignTable() {

            if (!tableIdsAlloted.isNullOrEmpty()) {
                dashboardRepository.reAssignTable(tableIdOld.toLong(), tableIdsAlloted[0].toLong(),
                    object : IDataSourceCallback<String> {

                        override fun onDataFound(data: String) {
                            showProgressBar.value = false
    //                    tableManagementListLiveData.value = data
                            Constants.showToastMessage(activity, data)
                            cancelClickEvent.call()
                        }

                        override fun onError(error: String) {
                            showProgressBar.value = false
                            Constants.showToastMessage(activity, error)
                        }
                    })
            } else {
                Constants.showToastMessage(activity, "Please select a table")
            }
        }

    */
    companion object {
        var otpVerificationTimer: Long = 0
        var otpCountDownTimer: CountDownTimer? = null
    }

    fun onBackPressUserDetails() {
        viewType.value = 1
        userDetailsVisible.set(true)
    }

    private fun validateFirstName(): Boolean {
        error = ErrorCheckUtils.checkValidName(visitorFirstName.value)
        if (error.isNotEmpty()) {
            visitorFirstNameError.value = error
            return false
        } else {
            visitorFirstNameError.value = ""
        }
        return true
    }

    private fun validateLastName(): Boolean {
        error = ErrorCheckUtils.checkValidName(visitorLastName.value)
        if (error.isNotEmpty()) {
            visitorLastNameError.value = error
            return false
        } else {
            visitorLastNameError.value = ""
        }
        return true
    }

    private fun validateMobileNumber(): Boolean {
        error = ErrorCheckUtils.checkValidMobile(mobileNumber.value)
        if (error.isNotEmpty()) {
            mobileNumberError.value = error
            return false
        } else {
            mobileNumberError.value = ""
        }
        return true
    }

    private fun validateVisitorMobileNumber(): Boolean {
        error = ErrorCheckUtils.checkValidMobile(visitorMobileNumber.value)
        if (error.isNotEmpty()) {
            visitorMobileNumberError.value = error
            return false
        } else {
            visitorMobileNumberError.value = ""
        }
        return true
    }

    private fun validateEmail(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(visitorEmail.value)
        if (error.isNotEmpty()) {
            visitorEmailError.value = error
            return false
        } else {
            visitorEmailError.value = ""
        }
        return true
    }

    private fun validateOtp(): Boolean {
        error = ErrorCheckUtils.checkValidOTP(verifyOtpEditTxt.value)
        if (error.isNotEmpty()) {
            verifyOtpError.value = error
            verifyOtpErrorVisibility.value = true
            return false
        } else {
            verifyOtpError.value = ""
        }
        return true
    }
/*


    fun arrangeTablesOnLayout() {

        if (tableManagementList.isNotEmpty()) {

            for (table in tableManagementList) {
                val inflater = LayoutInflater.from(activity)
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

                val tableX = table.xcoordinate?.times(tableSetupLayout.width)?.div(100)
                val tableY = table.ycoordinate?.times(tableSetupLayout.height)?.div(100)

                // Viewing Position Modification as the Container layout
                childLayout.x = (tableX ?: 0f) - 20f
                childLayout.y = (tableY ?: 0f) - (tableSetupLayoutY.toFloat() / 1.35f)

*/
/*
                if (isReAssignTable) {
                    childLayout.y =
                        (table.ycoordinate ?: 0f) - (tableSetupLayoutY.toFloat() / 1.35f)
                } else {
                    childLayout.y =
                        (table.ycoordinate ?: 0f) - (tableSetupLayoutY.toFloat() / 2f)
                }
*//*



                childLayout.setOnClickListener {
                    //color change and allocate seat
                    if (!tableSelected) {
                        table.status = TableStatusEnum.OCCUPIED.name
                        loadImage(table, imageView)
                        if (table.tableId != null) {
                            tableIdsAlloted.add(table.tableId?.toInt() ?: 0)
                            tableSelected = true
                        }
                    } else if (tableSelected) {
                        table.status = TableStatusEnum.VACANT.name
                        loadImage(table, imageView)
                        tableIdsAlloted.remove(table.tableId?.toInt() ?: 0)
                        tableSelected = false
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
                occupiedTableArrayList
//                reservedTableArrayList
            } else {
                vacantTableArrayList
            }
*//*


            val list: List<SystemValueMasterDTO> = when (table.status) {
                TableStatusEnum.UNOCCUPIED.name, TableStatusEnum.VACANT.name -> {
                    vacantTableArrayList
                }
                TableStatusEnum.OCCUPIED.name -> {
                    occupiedTableArrayList
                }
                TableStatusEnum.RESERVED.name -> {
                    reservedTableArrayList
                }
                else -> {
                    vacantTableArrayList
                }
            }

            for (item in list) {
                if (item.name != null) {
                    if (item.name?.startsWith(typeOfSeater) == true) {
                        Glide.with(activity)
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

}

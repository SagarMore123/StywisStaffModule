package com.astrika.stywis_staff.models

import java.io.Serializable


class CheckInDTO : Serializable {
    var active: Boolean? = false
    var checkInTime: String? = ""
    var checkOutTime: String? = ""
    var createdBy: String? = ""
    var createdOn: String? = ""
    var inCheckInCustomerRequestId: Long? = 0
    var inCustomerRequestId: Long? = 0
    var productCount: Long? = 0
    var lastRequestedTime: String? = ""
    var modifiedBy: String? = ""
    var modifiedOn: String? = ""
    var noOfSameRequest: Long? = 0
    var outletId: Long? = 0
    var rejectedRemarks: String? = ""
    var requestType: String? = ""
    var requestedTime: String? = ""
    var responseTime: String? = ""
    var staffId: Long? = 0
    var status: String? = ""
    var displayTableNos: String? = ""
    var tableIdsAlloted: ArrayList<Int>? = arrayListOf()
    var inList: ArrayList<CheckInDTO>? = arrayListOf()
    var uniquePin: Long? = 0
    var userGuestCount: Long? = 0
    var userId: Long? = 0
    var userName: String? = ""
    var visitPurpose: String? = ""
    var visitPurposeId: Long? = 0
    var mobileNo: String? = ""
    var profileImage: ImageDTO? = null
}


class GPinDTO : Serializable {
    var inCustomerRequestId: Long? = 0
    var uniquePin: Long? = 0

}
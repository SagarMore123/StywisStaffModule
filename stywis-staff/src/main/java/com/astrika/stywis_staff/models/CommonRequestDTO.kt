package com.astrika.stywis_staff.models

import java.io.Serializable

class CommonRequestDTO : Serializable {

    var userId: Long? = 0
    var outletId: Long? = 0
    var inCustomerRequestId: Long? = 0
    var inCheckInCustomerRequestId: Long? = 0
    var categoryType: String? = ""
    var searchProduct: String? = ""
    var catalogueCategoryId: Long? = 0

    var weekDay: Long? = 0
    var weekend: Boolean? = false
    var weekly: Boolean? = false
    var checkInId: Long? = 0
    var commonListingDTO = CommonListingDTO()


}
package com.astrika.stywis_staff.models

import java.io.Serializable

class OutletDiscountDetailsDTO : Serializable {

    var outletDiscountDetailsId: Long = 0L
    var outletId: Long = 0L
    var cardId: Long = 0L
    var oneDashboardPlanId: Long = 0L

    //    var outletDiscountCategoryId: Long = 0L
    var outletDiscountCategory: String = "" // Provide Discount Enum Name
    var outletMembershipPlanId: Long = 0L
    var outletMembershipPlanName: String = ""
    var memberShipHolderId: Long = 0L
    var userMembershipTypeId: Long = 0L
    var discountTimingList = ArrayList<DiscountDaysTimingDTO>()

}
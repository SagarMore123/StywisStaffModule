package com.astrika.stywis_staff.models.menu

import java.io.Serializable

class DishTimingDTO : Serializable {
    var productId: Long? = 0
    var dishTimingId: Long = 0
    var outletId: Long = 0
    var weekDay: Long = 0
    var opensAt: String = ""
    var closesAt: String = ""

}
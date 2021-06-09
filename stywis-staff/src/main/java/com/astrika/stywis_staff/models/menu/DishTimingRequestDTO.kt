package com.astrika.stywis_staff.models.menu

import java.io.Serializable

class DishTimingRequestDTO : Serializable {
    var timings: ArrayList<DishTimingDTO>? = arrayListOf()
    var productId: Long? = 0
}
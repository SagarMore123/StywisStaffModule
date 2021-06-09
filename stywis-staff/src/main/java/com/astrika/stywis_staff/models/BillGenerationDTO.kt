package com.astrika.stywis_staff.models

class BillGenerationDTO {

    var billId: Long? = 0
    var userId: Long? = 0
    var outletId: Long? = 0
    var inCheckInCustomerRequestId: Long? = 0
    var orderDTO: ArrayList<OrderDTO>? = arrayListOf()
    var takeOut: Boolean? = false
    var netAmount: Double? = 0.0
    var discountAmount: Double? = 0.0
    var taxAmount: Double? = 0.0
    var totalAmount: Double? = 0.0

    //    var status: OrderStatus? = null
    var paid: Boolean? = false
    var remarks: String? = ""
    var active = true


}
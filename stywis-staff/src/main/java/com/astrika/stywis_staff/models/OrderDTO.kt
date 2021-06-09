package com.astrika.stywis_staff.models

import java.io.Serializable

class OrderDTO(
    var orderId: Long?,
    var outletId: Long?,
    var tableId: Long? = 0,
    var userId: Long?,
    var taxAmount: Double? = 0.0,
    var totalAmount: Double? = 0.0,
    var discountAmount: Int? = 0,
    var netAmount: Int? = 0,
    var paid: Boolean? = true,
    var takeOut: Boolean? = false,
    var remarks: String? = "",
    var status: String? = "",
    var inCheckInCustomerRequestId: Long? = 0,
    var inCustomerRequestId: Long? = 0,
    var items: ArrayList<OrderProductDetails>?
)


class SaveOrderDTO(
    var orderId: Long?,
    var outletId: Long?,
    var userId: Long?,
    var inCheckInCustomerRequestId: Long? = 0,
    var inCustomerRequestId: Long? = 0,
    var takeOut: Boolean? = false,
    var remarks: String? = "",
    var items: ArrayList<SaveOrderProductDetails>?
)

class OrderBundleDTO : Serializable {
    var userId: Long? = 0
    var requestType: String? = ""
    var requestStatus: String? = ""
    var displayTableNos: String? = ""
    var inCheckInCustomerRequestId: Long? = 0
    var inCustomerRequestId: Long? = 0
}
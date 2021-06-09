package com.astrika.stywis_staff.utils

enum class RequestTypeEnum(val id: Int, val value: String) {
    CHECK_IN(0, "Check In"),
    CALL_FOR_SERVICE(1, "Service Request"),
    CALL_FOR_ORDER(2, "Food Order"),
    CALL_FOR_BILL(3, "Bill Request"),
    CHECKEDOUT(4, "Check Out");
}

enum class RequestStatusEnum(val id: Int, val value: String) {
    PENDING(0, "PENDING"),
    APPROVED(1, "APPROVED"),
    CANCELLED(2, "CANCELLED"),
    REJECTED(3, "REJECTED"),
    COMPLETED(4, "COMPLETED"),
    BILL_PAID(5, "BILL_PAID"),
    CHECKEDOUT(6, "CHECKEDOUT"),
    AWAITING_CONFIRMATION(7, "AWAITING_CONFIRMATION");
}

enum class TableStatusEnum(val id: Int, val value: String) {
    UNOCCUPIED(0, "UNOCCUPIED"),
    OCCUPIED(1, "OCCUPIED"),
    VACANT(2, "VACANT"),
    RESERVED(3, "RESERVED");
}

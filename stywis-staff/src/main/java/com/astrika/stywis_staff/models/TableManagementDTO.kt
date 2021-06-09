package com.astrika.stywis_staff.models

class TableManagementDTO {

    var active: Boolean? = true

    var capacity: Long = 0

    var occupied: Boolean? = null

    var reserved: Boolean? = null

    var outletId: Long? = 0

    //tableName
    var tableCode: String? = ""

    var status: String? = ""

    var tableId: Long? = 0

    var xcoordinate: Float? = 0f

    var ycoordinate: Float? = 0f

}
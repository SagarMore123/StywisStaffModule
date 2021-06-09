package com.astrika.stywis_staff.models.menu

import java.io.Serializable

class MenuSectionDTO : Serializable {
    var catalogueSectionId: Long? = 0
    var catalogueSectionName: String? = ""
    var catalogueCategoryId: Long? = 0
    var catalogueSectionSequenceNo: Long? = 0
    var outletId: Long? = 0
    var taxValue: Double? = 0.0
    var active: Boolean? = true
    var isSelected: Boolean = false
}
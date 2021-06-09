package com.astrika.stywis_staff.models.menu

import java.io.Serializable

class CatalogueCategoryDTO : Serializable {
    var catalogueCategoryId: Long? = 0
    var catalogueCategoryName: String? = ""
    var catalogueCategoryDesc: String? = ""
    var catalogueCategorySequenceNo: Long? = 0
    var active: Boolean? = true
    var outletId: Long? = 0
    var isSelected: Boolean = false
}
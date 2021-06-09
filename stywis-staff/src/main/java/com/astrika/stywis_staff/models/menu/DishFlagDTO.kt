package com.astrika.stywis_staff.models.menu

import java.io.Serializable

class DishFlagDTO : Serializable {
    var dishFlagId: Long? = 0
    var dishFlagName: String? = ""
    var isSelected: Boolean? = true
}
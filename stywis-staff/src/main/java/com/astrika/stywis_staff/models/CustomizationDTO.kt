package com.astrika.stywis_staff.models

import java.io.Serializable

class CustomizationDTO {
    //options List
    var optionsDTOList = ArrayList<CustomizationOptionDTO>()

    //addons list
    var addOnDTOList = ArrayList<CustomizationAddOnDTO>()

//    var id:Long = 0
//    var customizationType:Int?= -1
}

//list of options
class CustomizationOptionDTO {
    var customizationOptionDTO = CustomizationOptionDTOGroup()
    var customizationOptionValueDTO: ArrayList<CustomizationOptionDTOGroupRow>? = arrayListOf()
}

//list of groups
class CustomizationAddOnDTO {
    var customizationAddOnDTO = CustomizationAddOnDTOGroup()
    var customizationAddOnValueDTO: ArrayList<CustomizationAddOnDTOGroupRow>? = arrayListOf()
}

//For Options (Group)
class CustomizationOptionDTOGroup : Serializable {
    //    var active:Boolean = false
//    var createdBy:String ?= null
//    var createdOn:String ?= null
    var customizeOptionDesc: String? = ""
    var customizeOptionId: Long? = 0L
    var customizeOptionName: String? = ""

    //    var modifiedBy:String ?= null
//    var modifiedOn:String ?= null
    var productId: Long? = 0L
    var sequence: Long? = 0L
    var selected: Boolean? = false
}

//For Options (Rows)
class CustomizationOptionDTOGroupRow : Serializable {
    //    var active:Boolean = false
//    var createdBy:String ?= null
//    var createdOn:String ?= null
    var customizeOptionId: Long? = 0L
    var customizeOptionValueId: Long? = 0L
    var customizeOptionValueName: String? = ""
    var productDisPrice: Long? = 0L

    //    var modifiedBy:String ?= null
//    var modifiedOn:String ?= null
    var productOriPrice: Long? = 0L
    var sequence: Long? = 0L
    var selected: Boolean = false
}


//For Groups (AddOns)
class CustomizationAddOnDTOGroup : Serializable {
    //    var active:Boolean = false
//    var createdBy:String ?= null
//    var createdOn:String ?= null
    var customizeAddOnDesc: String? = ""
    var customizeAddOnId: Long? = 0L
    var customizeAddOnName: String? = ""
    var maxSelection: Long? = 0L
    var minSelection: Long? = 0L

    //    var modifiedBy:String ?= null
//    var modifiedOn:String ?= null
    var productId: Long? = 0L
    var sequence: Long? = 0L
    var selected: Boolean? = false
}

//For Group Rows (AddOns)
class CustomizationAddOnDTOGroupRow : Serializable {
    //    var active:Boolean = false
//    var createdBy:String ?= null
//    var createdOn:String ?= null
    var customizeAddOnId: Long? = 0L
    var customizeAddOnValueId: Long? = 0L
    var customizeAddOnValueName: String? = ""
    var productDisPrice: Long? = 0L

    //    var modifiedBy:String ?= null
//    var modifiedOn:String ?= null
    var productOriPrice: Long? = 0L
    var outOfStock: Boolean? = false
    var sequence: Long? = 0L
    var selected: Boolean = false
}
package com.astrika.stywis_staff.models.menu

import com.astrika.stywis_staff.models.ImageDTO
import java.io.Serializable
import java.math.BigDecimal


class DishDetailsDTO : Serializable {
    var productId: Long? = 0
    var productName: String? = ""
    var productDesc: String? = ""
    var productOriPrice: BigDecimal? = BigDecimal(0)
    var productDiscountPrice: BigDecimal? = BigDecimal(0)
    var productCustomizedPrice: BigDecimal? = BigDecimal(0)
    var itemId: Long? = 0 // For saving/taking Order
    var productImage: ImageDTO? = null
    var availableAllTime: Boolean? = false
    var productSequenceNo: Long? = 0
    var maxOrderQty: Long? = 0
    var minOrderQty: Long? = 0
    var outletId: Long? = 0
    var specialNotes: String? = ""
    var cuisineIds: ArrayList<Long>? = arrayListOf()
    var productFlags: ArrayList<Long>? = arrayListOf()
    var productFlagDTOs: ArrayList<DishFlagDTO>? = arrayListOf()
    var catalogueCategoryDTO: ArrayList<CatalogueCategoryDTO>? = arrayListOf()
    var catalogueCategoryId: ArrayList<Long>? = arrayListOf()
    var menuSectionDTO: ArrayList<MenuSectionDTO>? = arrayListOf()
    var catalogueSectionId: ArrayList<Long>? = arrayListOf()
    var active: Boolean? = true
    var isOutOfStock: Boolean? = false
    var isCustomizable = false
    var productTimingDTO: DishTimingRequestDTO? = null
    var quantity: Int? = 0
    var taxValue: Double? = 0.0
    var optionValueIds: ArrayList<ProductOptionsCustomizationDTO>? = arrayListOf()
    var addOnValueIds: ArrayList<ProductAddonsCustomizationDTO>? = arrayListOf()

}

class ProductOptionsCustomizationDTO : Serializable {
    var id: Long? = 0
    var optionMappingId: Long? = 0
    var value: String? = ""
    var itemId: Long? = 0
    var optionPrice: Long? = 0L

}

class ProductAddonsCustomizationDTO : Serializable {
    var id: Long? = 0
    var addOnMappingId: Long? = 0
    var value: String? = ""
    var itemId: Long? = 0
    var addOnPrice: Long? = 0L

}
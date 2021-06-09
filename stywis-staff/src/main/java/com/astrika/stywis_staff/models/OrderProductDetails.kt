package com.astrika.stywis_staff.models

import com.astrika.stywis_staff.models.menu.ProductAddonsCustomizationDTO
import com.astrika.stywis_staff.models.menu.ProductOptionsCustomizationDTO

data class OrderProductDetails(
    var productId: Long?,
    var productName: String? = "",
    var itemId: Long?,
    var productOriPrice: Long? = 0,
    var productDisPrice: Long? = 0,
    var taxAmount: Double? = 0.0,
    var totalItemPrice: Double? = 0.0,
    var quantity: Int?,
    var remarks: String? = "",
    var status: String? = "",
    var active: Boolean? = true,
    var optionValueIds: ArrayList<ProductOptionsCustomizationDTO>? = arrayListOf(),
    var addOnValueIds: ArrayList<ProductAddonsCustomizationDTO>? = arrayListOf()
)


data class SaveOrderProductDetails(
    var productId: Long?,
    var productName: String? = "",
    var itemId: Long?,
    var sectionId: Long?,
    var quantity: Int?,
    var remarks: String? = "",
    var optionValueIds: ArrayList<ProductOptionsCustomizationDTO>? = arrayListOf(),
    var addOnValueIds: ArrayList<ProductAddonsCustomizationDTO>? = arrayListOf()
)
package com.astrika.stywis_staff.models.menu

data class ProductAvailabilityDTO(
    var productId: Long?,
    var outletId: Long?,
    var isOutOfStock: Boolean?
)
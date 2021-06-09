package com.astrika.stywis_staff.models.stock

import java.io.Serializable

class StocksCustomizationDTO : Serializable {
    var stockId = 0L
    var productId = 0L
    var customizeOptionId = 0L
    var customizeOptionName = "" // Temp variable for UI Purpose
    var outletId = 0L
    var presentQuantity = 0L
    var active = true
}
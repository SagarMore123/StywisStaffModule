package com.astrika.stywis_staff.models.stock

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "stock_customization_master")
class StockCustomizationMasterDTO : Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    var customizeOptionId: Long? = 0

    @ColumnInfo
    var customizeOptionName: String? = ""

    @ColumnInfo
    var customizeOptionDesc: String? = ""

    @ColumnInfo
    var sequence: Long? = 0

    @ColumnInfo
    var productId: Long? = 0

    @ColumnInfo
    var active: Boolean = true

}
package com.astrika.stywis_staff.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "visit_purpose_master")
class VisitPurposeDTO : Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    var visitPurposeId: Long? = 0

    @ColumnInfo
    var name: String? = ""

}
package com.astrika.stywis_staff.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "system_value_master")
class SystemValueMasterDTO : Serializable {

    @PrimaryKey
    var serialId: Long = 0

    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var value: String? = null

    @ColumnInfo
    var code: String? = null

    @ColumnInfo
    var active = false

    var selected = false


    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as SystemValueMasterDTO

        if (serialId != other.serialId) {
            return false
        }

        if (value != other.value) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = serialId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + active.hashCode()
        result = 31 * result + selected.hashCode()
        return result
    }


}
package com.astrika.stywis_staff.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*


@Entity(tableName = "dashboard_drawer")
class DashboardDrawerDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var moduleId: Long = 0

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    @TypeConverters(DashboardDrawerTypeConverter::class)
    var applicationModules: List<DashboardDrawerDTO>? = null

    @ColumnInfo
    var applicableForRole: String? = null

    var selected = false

}

class DashboardDrawerTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<DashboardDrawerDTO> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<DashboardDrawerDTO>>() {

        }.type

        return gson.fromJson<List<DashboardDrawerDTO>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<DashboardDrawerDTO>): String {
        return gson.toJson(someObjects)
    }

}


@Entity(tableName = "designation")
class DesignationDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var designationId: Long = 0

    @ColumnInfo
    var designationName: String? = ""

    @ColumnInfo
    var designationDesc: String? = ""

    var selected = false
}


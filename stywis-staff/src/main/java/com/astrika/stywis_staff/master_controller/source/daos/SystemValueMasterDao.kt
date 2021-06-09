package com.astrika.stywis_staff.master_controller.source.daos

import androidx.annotation.StringDef
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.stywis_staff.models.SystemValueMasterDTO

@Dao
interface SystemValueMasterDao {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        TABLE_CONFIG_OCCUPIED, //BLUE
        TABLE_CONFIG_UNOCCUPIED, //GREY
        TABLE_CONFIG_VACANT, //GREEN
        TABLE_CONFIG_RESERVED, //RED
        MEMBERSHIP_HOLDER,
        MEMBERSHIP_TYPE
    )
    annotation class SystemValueMasterName

    companion object {
        const val TABLE_CONFIG_OCCUPIED = "TABLE_CONFIG_OCCUPIED"
        const val TABLE_CONFIG_UNOCCUPIED = "TABLE_CONFIG_UNOCCUPIED"
        const val TABLE_CONFIG_VACANT = "TABLE_CONFIG_VACANT"
        const val TABLE_CONFIG_RESERVED = "TABLE_CONFIG_RESERVED"
        const val MEMBERSHIP_HOLDER = "MEMBERSHIP_HOLDER"
        const val MEMBERSHIP_TYPE = "MEMBERSHIP_TYPE"
    }

    /**
     * Insert list of system master values in the database. If the user already exists, replace it.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSystemMasterData(svmArrayList: List<SystemValueMasterDTO>)

    @Query("delete from system_value_master")
    fun deleteAllSystemMasterData()

    @Query("select * from system_value_master")
    fun getAllSystemMasterData(): List<SystemValueMasterDTO>

    @Query("select * from system_value_master where system_value_master.name like :name")
    fun getAllByName(name: String): List<SystemValueMasterDTO>

    @Query("select * from system_value_master where system_value_master.code like :code")
    fun getAllByCode(code: String): List<SystemValueMasterDTO>

    @Query("select * from system_value_master where serialId = :id")
    fun getValueById(id: Long): SystemValueMasterDTO

}
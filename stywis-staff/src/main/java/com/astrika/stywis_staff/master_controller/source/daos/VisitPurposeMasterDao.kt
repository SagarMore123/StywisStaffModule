package com.astrika.stywis_staff.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.stywis_staff.models.VisitPurposeDTO

@Dao
interface VisitPurposeMasterDao {

    /**
     * Insert list of system master values in the database. If the user already exists, replace it.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMasterData(arrayList: List<VisitPurposeDTO>)

    @Query("delete from visit_purpose_master")
    fun deleteAllMasterData()

    @Query("select * from visit_purpose_master")
    fun fetchAllData(): List<VisitPurposeDTO>

    @Query("select * from visit_purpose_master")
    fun getList(): List<VisitPurposeDTO>

    @Query("select * from visit_purpose_master where name like :name")
    fun getValueByName(name: String): VisitPurposeDTO

    @Query("select * from visit_purpose_master where visitPurposeId = :id")
    fun getValueById(id: Long): VisitPurposeDTO

}
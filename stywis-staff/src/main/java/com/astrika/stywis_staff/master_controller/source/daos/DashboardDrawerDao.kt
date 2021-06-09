package com.astrika.stywis_staff.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.stywis_staff.models.DashboardDrawerDTO

@Dao
interface DashboardDrawerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<DashboardDrawerDTO>)

    @Query("delete from dashboard_drawer")
    fun deleteAllMasterData()

    @Query("select * from dashboard_drawer")
    fun fetchAllData(): List<DashboardDrawerDTO>

    @Query("delete from dashboard_drawer")
    fun deleteAllDetails()

    @Query("select * from dashboard_drawer where name = :name ")
    fun fetchDetailsByName(name: String): List<DashboardDrawerDTO>

    @Query("select * from dashboard_drawer where moduleId = :id ")
    fun fetchDetailsById(id: Long): DashboardDrawerDTO


}
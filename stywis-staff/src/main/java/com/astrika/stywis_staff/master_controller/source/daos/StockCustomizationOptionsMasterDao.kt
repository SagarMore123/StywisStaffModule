package com.astrika.stywis_staff.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO

@Dao
interface StockCustomizationOptionsMasterDao {

    /**
     * Insert list of system master values in the database. If the user already exists, replace it.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMasterData(arrayList: List<StockCustomizationMasterDTO>)

    @Query("delete from stock_customization_master")
    fun deleteAllMasterData()

    @Query("select * from stock_customization_master")
    fun fetchAllData(): List<StockCustomizationMasterDTO>

    @Query("select * from stock_customization_master where productId = :id")
    fun fetchListByProductId(id: Long): List<StockCustomizationMasterDTO>

    @Query("select * from stock_customization_master where customizeOptionName like :name")
    fun getValueByName(name: String): StockCustomizationMasterDTO

    @Query("select * from stock_customization_master where customizeOptionId = :id")
    fun getValueById(id: Long): StockCustomizationMasterDTO

}
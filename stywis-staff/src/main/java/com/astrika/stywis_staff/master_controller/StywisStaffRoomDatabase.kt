package com.astrika.stywis_staff.master_controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.astrika.stywis_staff.master_controller.source.LongListDTOConverter
import com.astrika.stywis_staff.master_controller.source.daos.*
import com.astrika.stywis_staff.models.DashboardDrawerDTO
import com.astrika.stywis_staff.models.DesignationDTO
import com.astrika.stywis_staff.models.SystemValueMasterDTO
import com.astrika.stywis_staff.models.VisitPurposeDTO
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.utils.Constants

// Annotates class to be a Room Database with a table (entity) of the SubCategory class
@Database(
    entities = [
        DashboardDrawerDTO::class, SystemValueMasterDTO::class,
        VisitPurposeDTO::class, StockCustomizationMasterDTO::class,
        DesignationDTO::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
//    ImageDTOConverter::class,
    LongListDTOConverter::class
)
abstract class StywisStaffRoomDatabase : RoomDatabase() {

    abstract fun dashboardDrawerDao(): DashboardDrawerDao
    abstract fun systemValueMasterDao(): SystemValueMasterDao
    abstract fun visitPurposeMasterDao(): VisitPurposeMasterDao
    abstract fun stockCustomizationOptionsMasterDao(): StockCustomizationOptionsMasterDao
    abstract fun designationDao(): DesignationDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same journalTime.
        @Volatile
        private var INSTANCE: StywisStaffRoomDatabase? = null

        fun getDatabase(context: Context): StywisStaffRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StywisStaffRoomDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
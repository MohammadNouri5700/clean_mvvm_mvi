package com.leo.clean_mvvm_mvi.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leo.clean_mvvm_mvi.core.database.entity.DriverEntity
import com.leo.clean_mvvm_mvi.core.database.entity.DriverStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Query("SELECT * FROM driver_info LIMIT 1")
    fun getDriverFlow(): Flow<DriverEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriver(driver: DriverEntity)

    @Query("DELETE FROM driver_info")
    fun clearDriver()

    @Query("SELECT * FROM driver_status LIMIT 1")
    fun getDriverStatus(): Flow<DriverStatusEntity?>

    @Query("SELECT * FROM driver_status LIMIT 1")
    fun getDriverStatusOneShot(): DriverStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverStatus(driverStatus: DriverStatusEntity)

    @Query("UPDATE driver_status SET isOnline = :isOnline WHERE driverId = :driverId")
    fun updateOnlineStatus(driverId: String, isOnline: Boolean)
}

package com.leo.clean_mvvm_mvi.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leo.clean_mvvm_mvi.core.database.dao.DriverDao
import com.leo.clean_mvvm_mvi.core.database.entity.DriverEntity
import com.leo.clean_mvvm_mvi.core.database.entity.DriverStatusEntity

@Database(entities = [DriverStatusEntity::class, DriverEntity::class], version = 2, exportSchema = false)
abstract class DriverDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao
}

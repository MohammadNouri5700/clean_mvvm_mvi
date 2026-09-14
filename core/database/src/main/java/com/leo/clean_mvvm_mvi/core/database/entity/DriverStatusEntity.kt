package com.leo.clean_mvvm_mvi.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_status")
data class DriverStatusEntity(
    @PrimaryKey val driverId: String,
    val name: String,
    val isOnline: Boolean,
    val totalTripsToday: Int
)


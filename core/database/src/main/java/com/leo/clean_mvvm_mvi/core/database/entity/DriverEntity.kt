package com.leo.clean_mvvm_mvi.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_info")
data class DriverEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isOnline: Boolean
)


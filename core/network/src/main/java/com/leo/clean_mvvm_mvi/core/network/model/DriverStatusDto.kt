package com.leo.clean_mvvm_mvi.core.network.model

import com.google.gson.annotations.SerializedName

data class DriverStatusDto(
    @SerializedName("driver_id") val driverId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("is_online") val isOnline: Boolean?,
    @SerializedName("total_trips_today") val totalTripsToday: Int?
)

